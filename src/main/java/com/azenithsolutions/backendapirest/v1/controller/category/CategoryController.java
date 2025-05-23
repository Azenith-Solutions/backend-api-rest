package com.azenithsolutions.backendapirest.v1.controller.category;

import com.azenithsolutions.backendapirest.v1.dto.category.CategoryListDTO;
import com.azenithsolutions.backendapirest.v1.dto.shared.ApiResponseDTO;
import com.azenithsolutions.backendapirest.v1.model.Category;
import com.azenithsolutions.backendapirest.v1.service.category.CategoryService;
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
@RequestMapping("/v1/categorys")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public ResponseEntity<ApiResponseDTO<?>> getCategory(HttpServletRequest request) {
        try {
            List<Category> categorys = categoryService.findAllCategorys();

            List<CategoryListDTO> categoryListDTO = categorys.stream()
                    .map(category -> new CategoryListDTO(
                            category.getIdCategoria(),
                            category.getNomeCategoria()
                    ))
                    .collect(Collectors.toList());

            // Imprime os dados no console
            System.out.println("Dados da categoria: " + categoryListDTO);

            return ResponseEntity.ok(
                    new ApiResponseDTO<>(
                            LocalDateTime.now(),
                            HttpStatus.OK.value(),
                            "Success",
                            categoryListDTO,
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
