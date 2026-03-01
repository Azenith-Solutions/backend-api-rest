package com.azenithsolutions.backendapirest.v2.core.domain.model.ai;

public class AiAssistantResponse {
    private String response;
    private String type;

    public AiAssistantResponse() {}

    public AiAssistantResponse(String response, String type) {
        this.response = response;
        this.type = type;
    }

    public static AiAssistantResponse sqlResponse(String response) {
        return new AiAssistantResponse(response, "SQL_QUERY");
    }

    public static AiAssistantResponse chatResponse(String response) {
        return new AiAssistantResponse(response, "CHAT");
    }

    public static AiAssistantResponse errorResponse(String response) {
        return new AiAssistantResponse(response, "ERROR");
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
