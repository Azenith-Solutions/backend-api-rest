package com.azenithsolutions.backendapirest.v2.core.usecase.ai;

import com.azenithsolutions.backendapirest.v2.core.domain.model.ai.AiAssistantResponse;
import com.azenithsolutions.backendapirest.v2.core.domain.model.ai.ChatMessage;
import com.azenithsolutions.backendapirest.v2.core.domain.repository.AiAssistantGateway;
import com.azenithsolutions.backendapirest.v2.core.domain.repository.DatabaseQueryGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProcessAiChatUseCase {

    private static final Logger log = LoggerFactory.getLogger(ProcessAiChatUseCase.class);

    private final AiAssistantGateway aiGateway;
    private final DatabaseQueryGateway databaseQueryGateway;

    private static final Pattern SQL_MARKDOWN_PATTERN = Pattern.compile(
            "```(?:sql)?\\s*(SELECT\\s+.*?)\\s*```", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
    private static final Pattern PLAIN_SQL_PATTERN = Pattern.compile(
            "^\\s*(SELECT\\s+.*)", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
    private static final String NAO_SQL_MARKER = "NAO_SQL";
    private static final String ERROR_API_CALL_MARKER = "ERROR_API_CALL";

    private static final String DB_SCHEMA = """
            IMPORTANTE: Todos os nomes de tabelas e colunas sao em MINUSCULO no banco. Use exatamente como escrito abaixo.

            tabela `caixa` (id_caixa [INT, PK], nome_caixa [VARCHAR(50)])
            tabela `categoria` (id_categoria [INT, PK], nome_categoria [VARCHAR(45)])
            tabela `componente` (
              id_componente [BIGINT, PK, AUTO_INCREMENT],
              id_hardwaretech [VARCHAR(9), UNIQUE],
              nome_componente [VARCHAR(100)],
              fk_caixa [INT, FK -> caixa(id_caixa)],
              fk_categoria [INT, FK -> categoria(id_categoria)],
              part_number [VARCHAR(100)],
              quantidade [INT] -- quantidade em estoque,
              flag_ml [BOOLEAN],
              codigo_ml [VARCHAR(100)],
              flag_verificado [BOOLEAN],
              condicao [CHAR(10)],
              observacao [VARCHAR(50)],
              descricao [VARCHAR(225)],
              imagem [VARCHAR(500)],
              is_visible_catalog [BOOLEAN],
              data_ultima_venda [DATE],
              quantidade_vendido [INT] -- total ja vendido,
              created_at [DATETIME],
              updated_at [DATETIME]
            )
            tabela `funcao` (id_funcao [INT, PK], nome_funcao [VARCHAR(45)])
            tabela `usuario` (
              id [INT, PK],
              nome [VARCHAR(45)],
              email [VARCHAR(45), UNIQUE],
              senha [VARCHAR(225)],
              foto [VARCHAR(500)],
              status [BOOLEAN],
              fk_funcao [INT, FK -> funcao(id_funcao)],
              created_at [DATE],
              updated_at [DATE]
            )
            tabela `pedido` (
              id_pedido [BIGINT, PK, AUTO_INCREMENT],
              codigo [VARCHAR(45), NOT NULL],
              nome_comprador [VARCHAR(50)],
              email_comprador [VARCHAR(50)],
              cnpj [CHAR(14)],
              valor [VARCHAR(45)],
              status [VARCHAR(45)] -- valores: PENDING, APPROVED, REJECTED, CANCELLED,
              tel_celular [CHAR(11)],
              created_at [DATETIME],
              updated_at [DATETIME]
            )
            tabela `item` (
              id_item [BIGINT, PK, AUTO_INCREMENT],
              fkComponente [BIGINT, FK -> componente(id_componente)],
              fkPedido [BIGINT, FK -> pedido(id_pedido)],
              quantidade [INT]
            )
            """;

    public ProcessAiChatUseCase(AiAssistantGateway aiGateway, DatabaseQueryGateway databaseQueryGateway) {
        this.aiGateway = aiGateway;
        this.databaseQueryGateway = databaseQueryGateway;
    }

    public AiAssistantResponse execute(String message, List<ChatMessage> history) {
        if (message == null || message.trim().isEmpty()) {
            return AiAssistantResponse.errorResponse("A mensagem nao pode estar vazia.");
        }

        String conversationContext = buildConversationContext(history, message);

        try {
            String sqlResponse = aiGateway.generateContent(buildSqlPrompt(conversationContext));

            if (ERROR_API_CALL_MARKER.equals(sqlResponse)) {
                return AiAssistantResponse.errorResponse("Falha ao contatar o servico de IA.");
            }

            String extractedSql = extractSql(sqlResponse);

            if (isValidSelectSql(extractedSql)) {
                log.debug("Generated SQL: {}", extractedSql);
                try {
                    Object queryResult = databaseQueryGateway.executeSelectQuery(extractedSql);
                    String summary = aiGateway.generateContent(buildSummarizePrompt(conversationContext, queryResult));

                    if (ERROR_API_CALL_MARKER.equals(summary)) {
                        return AiAssistantResponse.sqlResponse("Consulta realizada com sucesso, mas nao foi possivel sumarizar os resultados.");
                    }

                    return AiAssistantResponse.sqlResponse(summary);
                } catch (Exception e) {
                    log.error("Error executing SQL: {} - SQL: {}", e.getMessage(), extractedSql, e);
                    String chatResponse = aiGateway.generateContent(buildChatPrompt(conversationContext));
                    if (ERROR_API_CALL_MARKER.equals(chatResponse)) {
                        return AiAssistantResponse.errorResponse("Erro ao executar consulta e falha ao gerar resposta alternativa.");
                    }
                    return AiAssistantResponse.chatResponse(chatResponse);
                }
            } else {
                String chatResponse = aiGateway.generateContent(buildChatPrompt(conversationContext));
                if (ERROR_API_CALL_MARKER.equals(chatResponse)) {
                    return AiAssistantResponse.errorResponse("Falha ao contatar o servico de IA.");
                }
                return AiAssistantResponse.chatResponse(chatResponse);
            }
        } catch (Exception e) {
            log.error("Unexpected error in ProcessAiChatUseCase: {}", e.getMessage(), e);
            return AiAssistantResponse.errorResponse("Ocorreu um erro interno inesperado.");
        }
    }

    private String buildConversationContext(List<ChatMessage> history, String lastMessage) {
        StringBuilder context = new StringBuilder();
        if (history != null) {
            for (ChatMessage msg : history) {
                String prefix = "user".equalsIgnoreCase(msg.getRole()) ? "User: " : "Assistant: ";
                context.append(prefix).append(msg.getContent()).append("\n");
            }
        }
        context.append("User: ").append(lastMessage);
        return context.toString();
    }

    private String buildSqlPrompt(String context) {
        return String.format(
                """
                **Persona e Contexto:** Voce e um assistente de IA analitico da **Hardware Tech**, especialista em **gerenciamento de estoque de componentes eletronicos industriais, status de pedidos/solicitacoes, detalhes de componentes (capacitores, resistores, PLCs, inversores de frequencia, fontes chaveadas, reles, contatores, sensores, etc.) e informacoes sobre clientes/empresas**. Sua funcao principal e analisar a pergunta do usuario e, SE E SOMENTE SE for necessario consultar dados especificos do banco para responde-la, gerar o comando SQL `SELECT` apropriado.

                **Conhecimento Tecnico:** Voce entende sobre componentes eletronicos industriais:
                - Componentes passivos: resistores, capacitores, indutores
                - Semicondutores: diodos, transistores, CIs, MOSFETs, IGBTs
                - Automacao: PLCs, inversores de frequencia, soft-starters, reles, contatores
                - Fontes de alimentacao: fontes chaveadas, transformadores, retificadores
                - Sensores: proximidade, temperatura, pressao, nivel, vazao
                - Conectores, bornes, cabos, fusoes, disjuntores

                **Historico da Conversa:**
                %s

                **Instrucao Principal:** Analise CUIDADOSAMENTE a ULTIMA pergunta do usuario no historico.
                1.  **Verificacao de Relevancia e Intencao:**
                    *   A pergunta esta ESTRITAMENTE relacionada aos topicos da Hardware Tech (estoque, componentes, pedidos, solicitacoes, clientes, etc.)?
                    *   A pergunta EXIGE dados especificos do banco de dados para ser respondida?
                    *   **NAO (Fora do topico OU pergunta geral/saudacao que nao precisa de dados):** Responda IMEDIATAMENTE e EXATAMENTE com: %s
                    *   **SIM (Relevante e requer dados):** Prossiga para gerar o SQL.

                2.  **Geracao de SQL (APENAS se a etapa 1 indicar SIM):**
                    *   Gere APENAS o comando `SELECT` SQL necessario.
                    *   Use o esquema abaixo para construir a query. Use EXATAMENTE os nomes de tabelas e colunas como estao no esquema (todos em minusculo, com backticks).
                    *   Considere joins quando necessario.
                    *   **REGRAS ESTRITAS:**
                        *   SOMENTE `SELECT`. NUNCA `INSERT`, `UPDATE`, `DELETE`, `DROP`, etc.
                        *   O output deve ser APENAS o comando SQL puro, sem explicacoes, comentarios, ou markdown.
                        *   Use SEMPRE nomes de tabelas em minusculo exatamente como definidos no esquema (ex: `componente`, `pedido`, `categoria`).

                **Esquema do Banco de Dados:**
                %s

                **Comando SQL SELECT Valido ou Marcador (%s):**""",
                context,
                NAO_SQL_MARKER,
                DB_SCHEMA,
                NAO_SQL_MARKER
        );
    }

    private String buildSummarizePrompt(String context, Object result) {
        String resultString = result != null ? result.toString() : "Nenhum resultado.";
        if (resultString.length() > 1500) {
            resultString = resultString.substring(0, 1500) + "... (resultado truncado)";
        }

        return String.format(
                """
                **Contexto:** Voce e um assistente da Hardware Tech, especialista em componentes eletronicos industriais. O usuario fez uma pergunta que exigiu uma consulta ao banco de dados. A consulta foi executada e o resultado esta abaixo.

                **Historico da Conversa (incluindo a pergunta original do usuario):**
                %s

                **Resultado da Consulta SQL (Dados brutos):**
                %s

                **Instrucao Principal:** Interprete o **Resultado da Consulta SQL** e formule uma resposta CLARA, CONCISA e UTIL em Portugues para a ULTIMA pergunta do usuario no historico.
                **REGRAS IMPORTANTES:**
                1.  **NAO mencione SQL**, tabelas, colunas, banco de dados ou o processo de consulta.
                2.  **Responda diretamente a pergunta do usuario** usando os dados do resultado.
                3.  Se o resultado for "Nenhum resultado encontrado.", informe ao usuario que a informacao nao foi encontrada.
                4.  Se o resultado for complexo, resuma os pontos principais. Tente agregar ou analisar se apropriado.
                5.  Mantenha a persona de um assistente prestativo e tecnico da Hardware Tech.
                6.  Formate a resposta usando Markdown quando apropriado (listas, negrito, tabelas) para melhor legibilidade.
                7.  Se for uma lista de componentes, inclua informacoes relevantes como quantidade em estoque, part number, condicao.

                **Resposta final para o usuario:**""",
                context, resultString
        );
    }

    private String buildChatPrompt(String context) {
        String lastUserMessage = extractLastUserMessage(context);

        return String.format(
                """
                **Persona e Contexto:** Voce e um assistente de IA da **Hardware Tech**, especialista em **gerenciamento de estoque de componentes eletronicos industriais, reparo de maquinas industriais, vendas de componentes, status de pedidos e servicos relacionados**.

                **Conhecimento Tecnico Especializado:**
                Voce possui conhecimento profundo sobre componentes eletronicos industriais, incluindo:
                - **Componentes passivos:** resistores (SMD, through-hole, potenciometros), capacitores (eletroliticos, ceramicos, filme), indutores
                - **Semicondutores:** diodos (retificadores, zener, schottky), transistores (BJT, MOSFET, IGBT), circuitos integrados
                - **Automacao industrial:** CLPs/PLCs (Siemens, Allen-Bradley, WEG), inversores de frequencia, soft-starters, servomotores
                - **Protecao:** reles, contatores, disjuntores, fusiveis, varistores, supressores TVS
                - **Fontes:** fontes chaveadas, transformadores, retificadores, reguladores de tensao
                - **Sensores:** proximidade (indutivos, capacitivos), temperatura (PT100, termopar), pressao, nivel, vazao
                - **Conectividade:** conectores industriais, bornes, cabos, reguas de bornes

                Voce pode ajudar com:
                - Identificacao de componentes por part number ou descricao
                - Orientacoes de troubleshooting e manutencao preventiva
                - Sugestoes de componentes equivalentes ou substitutos
                - Informacoes sobre especificacoes tecnicas e datasheets
                - Boas praticas de armazenamento e manuseio de componentes

                **Esquema do Banco de Dados (para referencia sobre estrutura):**
                %s

                **Historico da Conversa:**
                %s

                **Instrucao Principal:** Analise a ULTIMA pergunta do usuario: "%s"
                1.  A pergunta esta dentro do contexto da Hardware Tech?
                    *   **NAO:** Responda educadamente que voce so pode ajudar com assuntos relacionados a Hardware Tech.
                    *   **SIM:** Responda de forma informativa, tecnica quando necessario, e amigavel em Portugues.

                2.  **Tipo de Resposta:**
                    *   **Pergunta tecnica sobre componentes:** Forneca informacoes tecnicas detalhadas, especificacoes, aplicacoes comuns.
                    *   **Pergunta sobre estrutura ou como fazer:** Use o esquema do banco para informar campos necessarios, omitindo IDs e timestamps automaticos.
                    *   **Pergunta geral ou conversacional:** Responda de forma conversacional e informativa.

                **REGRAS:**
                *   NAO execute ou gere comandos SQL.
                *   Formate a resposta usando Markdown (listas, negrito, titulos) para melhor legibilidade.
                *   Seja prestativo, claro e direto ao ponto.

                **Resposta:**""",
                DB_SCHEMA,
                context,
                lastUserMessage
        );
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
        return upperSql.startsWith("SELECT") &&
                !upperSql.contains("INSERT ") &&
                !upperSql.contains("UPDATE ") &&
                !upperSql.contains("DELETE ") &&
                !upperSql.contains("DROP ") &&
                !upperSql.contains("CREATE ") &&
                !upperSql.contains("ALTER ") &&
                !upperSql.contains("TRUNCATE ");
    }

    private String extractLastUserMessage(String context) {
        int lastUserPrefix = context.lastIndexOf("User: ");
        if (lastUserPrefix != -1) {
            return context.substring(lastUserPrefix + "User: ".length()).trim();
        }
        return "";
    }
}
