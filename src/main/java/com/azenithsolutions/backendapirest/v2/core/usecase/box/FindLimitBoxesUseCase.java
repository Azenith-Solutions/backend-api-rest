package com.azenithsolutions.backendapirest.v2.core.usecase.box;

import com.azenithsolutions.backendapirest.v2.core.domain.command.BoxDashboardCommand;
import com.azenithsolutions.backendapirest.v2.core.domain.model.box.Box;
import com.azenithsolutions.backendapirest.v2.core.domain.repository.BoxGateway;

import java.util.List;
import java.util.stream.Collectors;

public class FindLimitBoxesUseCase {
    private final BoxGateway repository;

    public FindLimitBoxesUseCase(BoxGateway repository) {
        this.repository = repository;
    }

    public List<BoxDashboardCommand> execute() {
        Integer maxComponentsPerBox = 200;
        System.out.println("=== INICIANDO BUSCA DE BOXES COM COMPONENTES ===");

        try {
            List<Box> components = repository.findBoxesGreaterOrAlmostOrInLimitOfComponents();
            System.out.println("✅ SUCESSO AO BUSCAR BOXES COM COMPONENTES");
            System.out.println("📊 Limite máximo de componentes por caixa: ");
            System.out.println("📦 Quantidade de boxes encontradas: " + components.size());

            return components.stream()
                    .map(component -> new BoxDashboardCommand(
                            component.getIdCaixa(),
                            component.getNomeCaixa(),
                            repository.countComponentsInBoxes(component.getIdCaixa())
                    ))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println("\n❌ ERRO INESPERADO AO BUSCAR BOXES COM COMPONENTES ❌");
            System.err.println("└── Tipo de erro: " + e.getClass().getName());
            System.err.println("└── Mensagem: " + e.getMessage());
            System.err.println("└── Causa raiz: " + (e.getCause() != null ? e.getCause().getMessage() : "Não especificada"));

            // Log stack trace for debugging
            System.err.println("\nStack trace:");
            e.printStackTrace();

            System.out.println("⚠️ Retornando lista vazia devido ao erro interno");
            return List.of();
        }
    }
}
