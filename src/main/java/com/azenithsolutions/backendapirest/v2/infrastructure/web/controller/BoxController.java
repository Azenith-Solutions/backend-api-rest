package com.azenithsolutions.backendapirest.v2.infrastructure.web.controller;

import com.azenithsolutions.backendapirest.v2.core.domain.command.BoxDashboardCommand;
import com.azenithsolutions.backendapirest.v2.core.usecase.box.FindLimitBoxesUseCase;
import com.azenithsolutions.backendapirest.v2.core.usecase.box.ListBoxesUseCase;
import com.azenithsolutions.backendapirest.v2.infrastructure.web.dto.box.ApiResponseRest;
import com.azenithsolutions.backendapirest.v2.infrastructure.web.dto.box.BoxListRest;
import com.azenithsolutions.backendapirest.v2.infrastructure.web.mappers.BoxRestMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v2/boxes")
@Tag(name = "Box Management - v2", description = "Clean architecture endpoint for Box")
@RequiredArgsConstructor
public class BoxController {
    private final FindLimitBoxesUseCase limiteCaixa;
    private final ListBoxesUseCase listBoxes;

    @GetMapping
    public ResponseEntity<ApiResponseRest<List<BoxListRest>>> getAll() {
    List<BoxListRest> data = listBoxes.execute().stream()
        .map(BoxRestMapper::toList)
        .toList();
    ApiResponseRest<List<BoxListRest>> body = new ApiResponseRest<>(
        java.time.LocalDateTime.now(),
        200,
        "Success",
        data
    );
    return ResponseEntity.ok(body);
    }

    @GetMapping("/kpi/box-dashboard")
    public ResponseEntity<List<BoxDashboardCommand>> getBoxesGreaterOrAlmostOrInLimitOfComponents() {
        List<BoxDashboardCommand> boxes = limiteCaixa.execute();
        return ResponseEntity.ok(boxes);
    }
}
