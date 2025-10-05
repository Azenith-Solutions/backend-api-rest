package com.azenithsolutions.backendapirest.v2.core.usecase.components.command;

import java.util.HashMap;

public record FilterComponentCommand(
    HashMap<String, Object> filtros
) { }
