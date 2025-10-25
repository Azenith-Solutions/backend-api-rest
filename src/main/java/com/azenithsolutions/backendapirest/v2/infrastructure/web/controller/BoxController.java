package com.azenithsolutions.backendapirest.v2.infrastructure.web.controller;

import com.azenithsolutions.backendapirest.v2.core.domain.command.BoxDashboardCommand;
import com.azenithsolutions.backendapirest.v2.core.usecase.box.FindLimitBoxesUseCase;
import com.azenithsolutions.backendapirest.v2.core.usecase.box.ListBoxesUseCase;
import com.azenithsolutions.backendapirest.v2.infrastructure.web.dto.box.ApiResponseRest;
import com.azenithsolutions.backendapirest.v2.infrastructure.web.dto.box.BoxListRest;
import com.azenithsolutions.backendapirest.v2.infrastructure.web.dto.shared.ApiResponseDTO;
import com.azenithsolutions.backendapirest.v2.infrastructure.web.mappers.BoxRestMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/v2/boxes")
@Tag(name = "Box Management - v2", description = "Clean architecture endpoint for Box")
@RequiredArgsConstructor
public class BoxController {
    private final FindLimitBoxesUseCase limiteCaixa;
    private final ListBoxesUseCase listBoxes;

    @GetMapping
    public ResponseEntity<ApiResponseDTO<?>> getAll(HttpServletRequest request) {
    try {
        List<BoxListRest> data = listBoxes.execute().stream()
                .map(BoxRestMapper::toList)
                .toList();

        return ResponseEntity.ok(
                new ApiResponseDTO<>(
                        LocalDateTime.now(),
                        HttpStatus.OK.value(),
                        "Success",
                        data,
                        request.getRequestURI()
                )
        );
    }catch (Exception e) {
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

    @GetMapping("/kpi/box-dashboard")
    public ResponseEntity<ApiResponseDTO<?>> getBoxesGreaterOrAlmostOrInLimitOfComponents(HttpServletRequest request) {
        try {
            List<BoxDashboardCommand> boxes = limiteCaixa.execute();
            return ResponseEntity.ok(
                    new ApiResponseDTO<>(
                            LocalDateTime.now(),
                            HttpStatus.OK.value(),
                            "Success",
                            boxes,
                            request.getRequestURI()
                    )
            );
        }catch (Exception e) {
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
