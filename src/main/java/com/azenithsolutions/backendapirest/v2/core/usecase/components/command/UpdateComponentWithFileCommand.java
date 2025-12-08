package com.azenithsolutions.backendapirest.v2.core.usecase.components.command;

import org.springframework.web.multipart.MultipartFile;

public record UpdateComponentWithFileCommand(
    String nome,
    String categoria,
    String partNumber,
    int quantidade,
    MultipartFile file
) { }
