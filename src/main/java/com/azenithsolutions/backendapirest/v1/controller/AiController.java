package com.azenithsolutions.backendapirest.v1.controller;

import com.azenithsolutions.backendapirest.v1.dto.AiGeminiRequest;
import com.azenithsolutions.backendapirest.v1.dto.ApiResponseDTO;
import com.azenithsolutions.backendapirest.v1.model.ChatMessage;
import com.azenithsolutions.backendapirest.v1.service.GeminiService; // Import the service
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext; // Use PersistenceContext for EntityManager
import jakarta.persistence.Query;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger; // Use a proper logger
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional; // Add for DB operations
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono; // Import Mono

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/v1/ai")
public class AiController {

    private static final Logger log = LoggerFactory.getLogger(AiController.class);

    @Autowired
    private GeminiService geminiService;

    @PersistenceContext
    private EntityManager entityManager;
    private static final String DB_SCHEMA = """
            As tabelas do banco são:
        Tabela Caixa (id_caixa [INT, PK], nome_caixa [VARCHAR(50)])
        Tabela Categoria (id_categoria [INT, PK], nome_categoria [VARCHAR(45)])
        Tabela Componente (
          id_hardwaretech [VARCHAR(9), PK],
          fk_caixa [INT, FK -> Caixa(id_caixa)],
          fk_categoria [INT, FK -> Categoria(id_categoria)],
          part_number [VARCHAR(100)],
          quantidade [INT],
          flag_ml [TINYINT],
          codigo_ml [VARCHAR(100)],
          flag_verificado [TINYINT],
          condicao [CHAR(10)],
          observacao [VARCHAR(50)],
          descricao [VARCHAR(225)],
          created_at [DATETIME]
        )
        Tabela Funcao (id_funcao [INT, PK], funcao [VARCHAR(45)])
        Tabela Usuario (
          id [INT, PK],
          nome [VARCHAR(45)],
          email [VARCHAR(45)],
          senha [VARCHAR(225)],
          fk_funcao [INT, FK -> Funcao(id_funcao)],
          updated_at [DATETIME],
          created_at [DATETIME]
        )
        Tabela Empresa (
          id_empresa [INT, PK],
          cnpj [CHAR(14)],
          nome [VARCHAR(45)],
          qntd_solicitacoes [INT]
        )
        Tabela Solicitacao (
          id_solicitacao [INT, PK],
          codigo [VARCHAR(45)],
          nome_comprador [VARCHAR(45)],
          email_comprador [VARCHAR(45)],
          tel_celular [VARCHAR(45)],
          status [VARCHAR(45)],
          fk_empresa [INT, FK -> Empresa(id_empresa)]
        )
        Tabela Pedido (
          id_hardwaretech [VARCHAR(9), FK -> Componente(id_hardwaretech)],
          id_pedido [INT, FK -> Solicitacao(id_solicitacao)],
          quantidade [INT],
          created_at [DATETIME],
          PK composto (id_hardwaretech, id_pedido)
        )
    """;

    private static final Pattern SQL_MARKDOWN_PATTERN = Pattern.compile("```(?:sql)?\\s*(SELECT\\s+.*?)\\s*```", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
    private static final Pattern PLAIN_SQL_PATTERN = Pattern.compile("^\\s*(SELECT\\s+.*)", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
    private static final String NAO_SQL_MARKER = "NAO_SQL";
    private static final String ERROR_API_CALL_MARKER = "ERROR_API_CALL";


    @PostMapping("/gemini/chat")
    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponseDTO<?>> getGeminiSQL(@RequestBody AiGeminiRequest body, HttpServletRequest request) {
        String userQuestion = body.getMessage();
        List<ChatMessage> history = body.getHistory() != null ? body.getHistory() : new ArrayList<>();
        String requestUri = request.getRequestURI();

        if (userQuestion == null || userQuestion.trim().isEmpty()) {
            return createErrorResponse(HttpStatus.BAD_REQUEST, List.of("A mensagem não pode estar vazia."), requestUri);
        }

        String conversationContext = buildConversationContext(history, userQuestion);

        try {
            String sqlResponse = generateSql(conversationContext).block();

            if (ERROR_API_CALL_MARKER.equals(sqlResponse)) {
                return createErrorResponse(HttpStatus.SERVICE_UNAVAILABLE, List.of("Falha ao contatar o serviço de IA para gerar SQL."), requestUri);
            }

            String extractedSql = extractSql(sqlResponse);

            if (isValidSelectSql(extractedSql)) {
                log.debug("Generated SQL: {}", extractedSql);
                try {
                    Object queryResult = executeSql(extractedSql);

                    String summary = summarizeResults(conversationContext, queryResult).block(); // block() for simplicity

                    if (ERROR_API_CALL_MARKER.equals(summary)) {
                        log.warn("SQL executed successfully, but failed to summarize results for context: {}", conversationContext);
                        return createOkResponse("Consulta realizada, mas falha ao sumarizar.", Map.of("raw_result", queryResult), requestUri);
                    }

                    log.info("Successfully processed SQL query and generated summary.");
                    return createOkResponse("Success", Map.of("response", summary), requestUri);

                } catch (Exception e) {
                    log.error("Error executing SQL: {} - SQL: {}", e.getMessage(), extractedSql, e);
                    String chatResponse = generateChatResponse(conversationContext).block();
                    if (ERROR_API_CALL_MARKER.equals(chatResponse)) {
                        return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, List.of("Erro ao executar consulta SQL e falha ao gerar resposta alternativa."), requestUri);
                    }
                    return createOkResponse("Erro na consulta, resposta alternativa:", Map.of("response", chatResponse), requestUri);
                }
            } else {
                String chatResponse;
                if (NAO_SQL_MARKER.equalsIgnoreCase(sqlResponse.trim())) {
                    log.info("Received NAO_SQL marker, generating chat response.");
                    chatResponse = generateChatResponse(conversationContext).block();
                } else {
                    log.warn("Received non-SQL, non-NAO_SQL response from SQL generation prompt: {}", sqlResponse);
                    chatResponse = generateChatResponse(conversationContext).block();
                }

                if (ERROR_API_CALL_MARKER.equals(chatResponse)) {
                    return createErrorResponse(HttpStatus.SERVICE_UNAVAILABLE, List.of("Falha ao contatar o serviço de IA para gerar resposta."), requestUri);
                }

                return createOkResponse("Pergunta geral", Map.of("response", chatResponse), requestUri);
            }

        } catch (Exception e) {
            log.error("Unexpected error in /gemini/chat endpoint: {}", e.getMessage(), e);
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, List.of("Ocorreu um erro interno inesperado."), requestUri);
        }
    }

    private Mono<String> generateSql(String context) {
        String prompt = String.format(
            """
            Histórico da Conversa:
            %s

            Instrução: Baseado no histórico e no esquema de banco de dados abaixo, converta a ÚLTIMA pergunta do usuário em um comando SQL SELECT VÁLIDO para consulta.
            REGRAS IMPORTANTES:
            1. Gere APENAS o comando SQL SELECT.
            2. deixe EXPLICITO que não pode INSERIR, ATUALIZAR e principalmente DELETAR dados.
            3. NÃO inclua explicações, comentários, markdown (```) ou qualquer texto adicional fora do SQL.
            4. Se a pergunta não puder ser convertida em um comando SQL SELECT ou não for relacionada a consultar dados do esquema fornecido, responda EXATAMENTE com: %s

            Esquema do Banco de Dados:
            %s

            Comando SQL ou %s:""",
            context, NAO_SQL_MARKER, DB_SCHEMA, NAO_SQL_MARKER
        );
        log.debug("Generating SQL with prompt:\n{}", prompt);
        return geminiService.generateContent(prompt);
    }

    private String extractSql(String response) {
        if (response == null) return "";
        String trimmedResponse = response.trim();

        if (NAO_SQL_MARKER.equalsIgnoreCase(trimmedResponse)) {
            return NAO_SQL_MARKER;
        }

        Matcher markdownMatcher = SQL_MARKDOWN_PATTERN.matcher(trimmedResponse);
        if (markdownMatcher.matches()) {
            return markdownMatcher.group(1).trim();
        }

        Matcher plainMatcher = PLAIN_SQL_PATTERN.matcher(trimmedResponse);
        if (plainMatcher.matches()) {
            return plainMatcher.group(1).trim();
        }

        return trimmedResponse;
    }


    private boolean isValidSelectSql(String sql) {
        if (sql == null || sql.isEmpty() || NAO_SQL_MARKER.equalsIgnoreCase(sql)) {
            return false;
        }

        String upperSql = sql.toUpperCase().trim();
        boolean looksSafe = upperSql.startsWith("SELECT") &&
                !upperSql.contains("INSERT ") &&
                !upperSql.contains("UPDATE ") &&
                !upperSql.contains("DELETE ") &&
                !upperSql.contains("DROP ") &&
                !upperSql.contains("CREATE ") &&
                !upperSql.contains("ALTER ") &&
                !upperSql.contains("TRUNCATE ");
        if (!looksSafe) {
            log.warn("Potentially unsafe or non-SELECT SQL detected: {}", sql);
        }
        return looksSafe;
    }

    private String buildConversationContext(List<ChatMessage> history, String lastMessage) {
        StringBuilder context = new StringBuilder();
        String userPrefix = "User: ";
        String assistantPrefix = "Assistant: ";

        for (ChatMessage msg : history) {
            String prefix = "user".equalsIgnoreCase(msg.getRole()) ? userPrefix : assistantPrefix;
            context.append(prefix).append(msg.getContent()).append("\n");
        }
        context.append(userPrefix).append(lastMessage);
        return context.toString();
    }

    private Object executeSql(String sql) {
        Query nativeQuery = entityManager.createNativeQuery(sql);
        List<?> resultList = nativeQuery.getResultList();

        if (resultList.isEmpty()) {
            return "Nenhum resultado encontrado.";
        } else if (resultList.size() == 1) {
            Object item = resultList.get(0);
            if (item instanceof Object[] && ((Object[]) item).length == 1) {
                return ((Object[]) item)[0];
            }
            return item;
        } else {
            return resultList;
        }
    }

    private Mono<String> summarizeResults(String context, Object result) {
        String resultString = result != null ? result.toString() : "Nenhum resultado.";
        int maxLength = 1500;
        if (resultString.length() > maxLength) {
            resultString = resultString.substring(0, maxLength) + "... (resultado truncado)";
        }

        String prompt = String.format(
            """
            Histórico da Conversa:
            %s

            Resultado da Consulta SQL (já executada):
            %s

            Instrução: Com base no histórico da conversa e no resultado da consulta fornecido, elabore uma resposta curta e clara para o usuário em Português.
            REGRAS IMPORTANTES:
            1. NÃO mencione SQL, tabelas, ou o processo de consulta.
            2. deixe EXPLICITO que você NÃO pode INSERIR, ATUALIZAR ou DELETAR itens
            3. Foque em apresentar a informação do resultado de forma útil e direta.
            4. Seja conciso e amigável.

            Resposta para o usuário:""",
            context, resultString
        );
        log.debug("Generating summary with prompt:\n{}", prompt);
        return geminiService.generateContent(prompt);
    }

    private Mono<String> generateChatResponse(String context) {
        String prompt = String.format(
            """
            Histórico da Conversa:
            %s

            Instrução: Responda à ÚLTIMA pergunta do usuário de forma conversacional, útil e amigável, em Português. Use o histórico como contexto. Não tente gerar SQL nem mencionar o banco de dados. Apenas converse.

            Resposta:""",
            context
        );
        log.debug("Generating chat response with prompt:\n{}", prompt);
        return geminiService.generateContent(prompt);
    }

    private ResponseEntity<ApiResponseDTO<?>> createOkResponse(String message, Object data, String path) {
        return ResponseEntity.ok(new ApiResponseDTO<>(
                LocalDateTime.now(),
                HttpStatus.OK.value(),
                message,
                data,
                path
        ));
    }

    private ResponseEntity<ApiResponseDTO<?>> createErrorResponse(HttpStatus status, List<String> errors, String path) {
        return ResponseEntity.status(status).body(new ApiResponseDTO<>(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                errors,
                path
        ));
    }
}