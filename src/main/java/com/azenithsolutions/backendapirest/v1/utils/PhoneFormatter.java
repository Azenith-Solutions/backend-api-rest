package com.azenithsolutions.backendapirest.v1.utils;

public class PhoneFormatter {
    /**
     * Formata um número de telefone completo a partir de suas partes
     * @param ddi Código de Discagem Direta Internacional (ex: "55")
     * @param ddd Código de Discagem Direta à Distância (ex: "11")
     * @param numero Número do telefone (ex: "987654321")
     * @return Número formatado (ex: "+55 (11) 98765-4321")
     */

    public static String formatarTelefoneCompleto(String ddi, String ddd, String numero) {
        // Tratar valores nulos
        ddd = ddd == null ? "" : ddd.trim();
        numero = numero == null ? "" : numero.trim();

        // Formatar com base no comprimento do número
        String numeroFormatado;
        if (numero.length() == 9) { // Celular
            numeroFormatado = numero.substring(0, 5) + "-" + numero.substring(5);
        } else {
            numeroFormatado = numero; // Mantém como está se não seguir os padrões
        }

        // Construir número completo formatado
        StringBuilder telefoneCompleto = new StringBuilder();

        if (!ddd.isEmpty()) {
            telefoneCompleto.append("(").append(ddd).append(") ");
        }

        telefoneCompleto.append(numeroFormatado);

        return telefoneCompleto.toString();
    }
}
