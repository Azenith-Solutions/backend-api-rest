package com.azenithsolutions.backendapirest.v1.service.Box;

import com.azenithsolutions.backendapirest.v1.dto.box.BoxDashboardDTO;
import com.azenithsolutions.backendapirest.v1.model.Box;
import com.azenithsolutions.backendapirest.v1.repository.BoxRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.sql.SQLOutput;
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
        System.out.println("=== BUSCANDO BOXES COM COMPONENTES ===");
        try {
            System.out.println("=== **************************************** SUCESSO AO BUSCAR BOXES COM COMPONENTES ===");
            System.out.println("Max components per box: " + maxComponentsPerBox);
            List<BoxDashboardDTO> components = boxRepository.findBoxesGreaterOrAlmostOrInLimitOfComponents(maxComponentsPerBox);

            return components.stream()
                    .map(component -> new BoxDashboardDTO(
                            component.id(),
                            component.name(),
                            boxRepository.countComponentsInBoxes(component.id())
                    ))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            System.out.println("=== **************************************** ERRO AO BUSCAR BOXES COM COMPONENTES ===");
            WebClientResponseException wcre = (WebClientResponseException) e;

            System.err.println("=== ERRO DE RESPOSTA DOS COMPONENTES POR BOX ===");
            System.err.println("Status code: " + wcre.getRawStatusCode());
            System.err.println("Status text: " + wcre.getStatusText());
            System.err.println("Response headers: " + wcre.getHeaders());
            System.err.println("Response body: " + wcre.getResponseBodyAsString());

            if (wcre.getRequest() != null) {
                System.err.println("Request URI: " + wcre.getRequest().getURI());
                System.err.println("Request method: " + wcre.getRequest().getMethod());
            }

            List<BoxDashboardDTO> emptyList = List.of();

            System.out.println("Retornando uma lista vazia devido ao erro: " + e.getMessage());

            return emptyList;
        }
    }
}
