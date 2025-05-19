package com.azenithsolutions.backendapirest.v1.controller.Box;

import com.azenithsolutions.backendapirest.v1.dto.box.BoxListDTO;
import com.azenithsolutions.backendapirest.v1.dto.shared.ApiResponseDTO;
import com.azenithsolutions.backendapirest.v1.model.Box;
import com.azenithsolutions.backendapirest.v1.service.Box.BoxService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/boxes")
public class BoxController {
    @Autowired
    private BoxService boxService;

    @GetMapping
    public ResponseEntity<ApiResponseDTO<?>> getBoxes(HttpServletRequest request) {
        try {
            List<Box> boxes = boxService.findAllBoxes();

            List<BoxListDTO> boxListDTOS = boxes.stream()
                    .map(box -> new BoxListDTO(
                            box.getIdCaixa(),
                            box.getNomeCaixa()
                    ))
                    .collect(Collectors.toList());

            // Imprime os dados no console
            System.out.println("Dados das caixas: " + boxListDTOS);

            return ResponseEntity.ok(
                    new ApiResponseDTO<>(
                            LocalDateTime.now(),
                            HttpStatus.OK.value(),
                            "Success",
                            boxListDTOS,
                            request.getRequestURI()
                    )
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ApiResponseDTO<>(
                            LocalDateTime.now(),
                            HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "Internal Server Error",
                            List.of("An error occurred while processing the request"),
                            request.getRequestURI()
                    )
            );
        }
    }
}
