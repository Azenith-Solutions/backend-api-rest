package com.azenithsolutions.backendapirest.v1.service.Box;

import com.azenithsolutions.backendapirest.v1.dto.box.BoxDashboardDTO;
import com.azenithsolutions.backendapirest.v1.model.Box;
import com.azenithsolutions.backendapirest.v1.repository.BoxRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BoxService {
    @Autowired
    private BoxRepository boxRepository;

    public List<Box> findAllBoxes() {
        return boxRepository.findAll();
    }

    public List<BoxDashboardDTO> findBoxesGreaterOrAlmostOrInLimitOfComponents() {
        Integer maxComponentsPerBox = 200;
        System.out.println("=== INICIANDO BUSCA DE BOXES COM COMPONENTES ===");
        try {
            List<Box> components = boxRepository.findBoxesGreaterOrAlmostOrInLimitOfComponents();
            System.out.println("✅ SUCESSO AO BUSCAR BOXES COM COMPONENTES");
            System.out.println("📊 Limite máximo de componentes por caixa: ");
            System.out.println("📦 Quantidade de boxes encontradas: " + components.size());

            return components.stream()
                    .map(component -> new BoxDashboardDTO(
                            component.getIdCaixa(),
                            component.getNomeCaixa(),
                            boxRepository.countComponentsInBoxes(component.getIdCaixa())
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
