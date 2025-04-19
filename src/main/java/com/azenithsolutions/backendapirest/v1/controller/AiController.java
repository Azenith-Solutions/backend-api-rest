package com.azenithsolutions.backendapirest.v1.controller;

import com.azenithsolutions.backendapirest.v1.dto.AiGeminiRequest;
import com.azenithsolutions.backendapirest.v1.dto.ApiResponseDTO;
import com.azenithsolutions.backendapirest.v1.model.ChatMessage;
import com.azenithsolutions.backendapirest.v1.service.GeminiService; 
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext; 
import jakarta.persistence.Query;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger; 
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono; 

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
                **Persona e Contexto:** Você é um assistente de IA analítico da **Hardware Tech**, especialista em **gerenciamento de estoque, status de pedidos/solicitações, detalhes de componentes eletrônicos, e informações sobre clientes/empresas** com base nos dados da Hardware Tech. Sua função principal é analisar a pergunta do usuário e, SE E SOMENTE SE for necessário consultar dados específicos do banco para respondê-la, gerar o comando SQL `SELECT` apropriado.

                **Histórico da Conversa:**
                %s

                **Instrução Principal:** Analise CUIDADOSAMENTE a ÚLTIMA pergunta do usuário no histórico.
                1.  **Verificação de Relevância e Intenção:**
                    *   A pergunta está ESTRITAMENTE relacionada aos tópicos da Hardware Tech (estoque, componentes, pedidos, solicitações, clientes, etc.)?
                    *   A pergunta EXIGE dados específicos do banco de dados para ser respondida (ex: "liste os componentes X", "qual o status do pedido Y", "quantos itens Z temos?", "qual o email do cliente W?")?
                    *   **NÃO (Fora do tópico OU pergunta geral/saudação que não precisa de dados):** Responda IMEDIATAMENTE e EXATAMENTE com: %s
                    *   **SIM (Relevante e requer dados):** Prossiga para gerar o SQL.

                2.  **Geração de SQL (APENAS se a etapa 1 indicar SIM):**
                    *   Gere APENAS o comando `SELECT` SQL necessário para obter os dados que respondem DIRETAMENTE à pergunta do usuário.
                    *   Use o esquema abaixo para construir a query. Seja preciso com nomes de tabelas e colunas.
                    *   Considere joins quando necessário para combinar informações (ex: pedido com detalhes do componente ou cliente).
                    *   **REGRAS ESTRITAS:**
                        *   SOMENTE `SELECT`. NUNCA `INSERT`, `UPDATE`, `DELETE`, `DROP`, etc. Qualquer outra coisa resulta em %s.
                        *   O output deve ser APENAS o comando SQL puro, sem explicações, comentários, ou markdown (```).

                **Esquema do Banco de Dados:**
                %s

                **Comando SQL SELECT Válido ou Marcador (%s):**""",
                context,           // Histórico
                NAO_SQL_MARKER,    // Regra 1/2: Fora de contexto ou não requer SQL
                NAO_SQL_MARKER,    // Regra SQL: Não-SELECT
                DB_SCHEMA,         // Esquema
                NAO_SQL_MARKER     // Placeholder final
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
        int maxLength = 1500; // Keep truncation for safety
        if (resultString.length() > maxLength) {
            resultString = resultString.substring(0, maxLength) + "... (resultado truncado)";
        }

        String prompt = String.format(
            """
            **Contexto:** Você é um assistente da Hardware Tech. O usuário fez uma pergunta que exigiu uma consulta ao banco de dados. A consulta foi executada e o resultado está abaixo.

            **Histórico da Conversa (incluindo a pergunta original do usuário):**
            %s

            **Resultado da Consulta SQL (Dados brutos):**
            %s

            **Instrução Principal:** Sua tarefa é interpretar o **Resultado da Consulta SQL** e formular uma resposta CLARA, CONCISA e ÚTIL em Português para a ÚLTIMA pergunta do usuário no histórico.
            **REGRAS IMPORTANTES:**
            1.  **NÃO mencione SQL**, tabelas, colunas, banco de dados ou o processo de consulta. Fale como se você soubesse a informação diretamente.
            2.  **Responda diretamente à pergunta do usuário** usando os dados do resultado.
            3.  Se o resultado for "Nenhum resultado encontrado.", informe ao usuário de forma clara que a informação solicitada não foi encontrada nos registros.
            4.  Se o resultado for complexo (muitas linhas/colunas), resuma os pontos principais ou a informação mais relevante para a pergunta do usuário. Não apenas liste os dados. Tente agregar ou analisar se apropriado.
            5.  Mantenha a persona de um assistente prestativo da Hardware Tech.
            6.  Lembre ao usuário que você não pode modificar dados (inserir, atualizar, deletar), mas faça isso apenas ocasionalmente, quando parecer relevante, não em toda resposta.

            **Resposta final para o usuário:**""",
            context, resultString
        );
        log.debug("Generating summary with prompt:\n{}", prompt);
        return geminiService.generateContent(prompt);
    }

    private Mono<String> generateChatResponse(String context) {
        String lastUserMessage = extractLastUserMessage(context);

        String prompt = String.format(
                """
                **Persona e Contexto:** Você é um assistente de IA da **Hardware Tech**, especialista em **gerenciamento de estoque, reparo de máquinas industriais, vendas de componentes eletrônicos, status de pedidos/solicitações e serviços relacionados** oferecidos pela Hardware Tech. Você também tem conhecimento sobre a estrutura de dados do sistema.

                **Esquema do Banco de Dados (para referência sobre estrutura):**
                %s

                **Histórico da Conversa (incluindo a última pergunta):**
                %s

                **Instrução Principal:** Analise a ÚLTIMA pergunta do usuário: "%s"
                1.  **Verificação de Contexto e Intenção:**
                    *   A pergunta está ESTRITAMENTE dentro do contexto da Hardware Tech (estoque, reparos, vendas, pedidos, solicitações, serviços, funcionalidades gerais da aplicação, estrutura de dados)?
                    *   **NÃO:** Responda educadamente que você só pode ajudar com assuntos relacionados à Hardware Tech e seus serviços. NÃO responda à pergunta fora do tópico.
                    *   **SIM:** Prossiga para a etapa 2.

                2.  **Tipo de Resposta:**
                    *   **Pergunta sobre ESTRUTURA ou COMO FAZER?** (Ex: "quais campos preciso para cadastrar um componente?", "como registro um usuário?"): Use o **Esquema do Banco de Dados** acima para identificar a tabela relevante. Formule uma resposta clara e CONCISA em Português listando APENAS os campos que o usuário precisa **informar manualmente** durante o cadastro/criação.
                        *   **OMITIR:** IDs (chaves primárias), chaves estrangeiras (mas explique o conceito se necessário, ex: "precisa informar a categoria"), timestamps (`created_at`, `updated_at`), e outros campos que são geralmente preenchidos automaticamente pelo sistema.
                        *   **NÃO MENCIONAR:** Tipos de dados (VARCHAR, INT), tamanhos (45, 225), ou restrições de banco de dados, a menos que seja crucial para o entendimento.
                        *   **Exemplo (Usuário):** "Para registrar um novo usuário, você precisa fornecer o Nome, Email, Senha e a Função dele no sistema."
                    *   **Pergunta GERAL ou CONVERSACIONAL?** (Ex: "o que a Hardware Tech faz?", "fale sobre reparos"): Responda de forma conversacional, informativa e amigável, em Português. Use o histórico se relevante. Foque em fornecer informações gerais ou explicações sobre os tópicos permitidos.

                **REGRAS IMPORTANTES:**
                *   NÃO execute ou gere comandos SQL neste modo.
                *   Seja prestativo, claro e direto ao ponto.

                **Resposta Concisa e Direta (baseada no esquema, se aplicável):**""",
                DB_SCHEMA,
                context,
                lastUserMessage
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

    private String extractLastUserMessage(String context) {
        int lastUserPrefix = context.lastIndexOf("User: ");
        if (lastUserPrefix != -1) {
            return context.substring(lastUserPrefix + "User: ".length()).trim();
        }
        return "";
    }
}