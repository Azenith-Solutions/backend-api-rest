package com.azenithsolutions.backendapirest.v2.infrastructure.web.controller;


import com.azenithsolutions.backendapirest.v2.core.domain.model.category.Category;
import com.azenithsolutions.backendapirest.v2.core.usecase.category.CreateCategoryUseCase;
import com.azenithsolutions.backendapirest.v2.core.usecase.category.DeleteCategoryUseCase;
import com.azenithsolutions.backendapirest.v2.infrastructure.web.mappers.CategoryEntityMapper;
import com.azenithsolutions.backendapirest.v2.infrastructure.web.dto.category.CategoryCreateRequest;
import com.azenithsolutions.backendapirest.v2.infrastructure.web.dto.category.CategoryListResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.azenithsolutions.backendapirest.v2.core.usecase.category.ListCategoryUseCase;
import com.azenithsolutions.backendapirest.v1.dto.shared.ApiResponseDTO;

import java.time.LocalDateTime;
import java.util.List;

@RestController("categoryContrrolerV2")
@RequestMapping("/v2/categorys")
@Tag(name = "Category Management - v2", description = "Clean architecture endpoint for Category")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class CategoryController {
    private final ListCategoryUseCase listCategoryUseCase;
    private final CreateCategoryUseCase createCategoryUseCase;
    private final DeleteCategoryUseCase deleteCategoryUseCase;

    @GetMapping
    @Operation(summary = "List categories", description = "Returns all categories (v2 clean architecture)")
    public ResponseEntity<ApiResponseDTO<?>> getCategories(HttpServletRequest request) {
        try {
            java.util.List<Category> categories = listCategoryUseCase.execute();

            List<CategoryListResponseDTO> categoryListResponseDTOs = categories.stream()
                    .map(CategoryEntityMapper::toListResponseDTO)
                    .toList();

            return ResponseEntity.ok(
                    new ApiResponseDTO<>(
                            LocalDateTime.now(),
                            HttpStatus.OK.value(),
                            "Success",
                            categoryListResponseDTOs,
                            request.getRequestURI()
                    )
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ApiResponseDTO<>(
                            LocalDateTime.now(),
                            HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "An error occurred while processing the request.",
                            null,
                            request.getRequestURI()
                    )
            );
        }
    }

    @PostMapping
    @Operation(summary = "Create category", description = "Creates a new category")
    public ResponseEntity<ApiResponseDTO<?>> create(@RequestBody CategoryCreateRequest body, HttpServletRequest request) {
        try {
            Category created = createCategoryUseCase.execute(body.nome());
            CategoryListResponseDTO data = CategoryEntityMapper.toListResponseDTO(created);

            return ResponseEntity.status(HttpStatus.CREATED).body(
                    new ApiResponseDTO<>(
                            LocalDateTime.now(),
                            HttpStatus.CREATED.value(),
                            "Category created successfully",
                            data,
                            request.getRequestURI()
                    )
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ApiResponseDTO<>(
                            LocalDateTime.now(),
                            HttpStatus.BAD_REQUEST.value(),
                            e.getMessage(),
                            null,
                            request.getRequestURI()
                    )
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ApiResponseDTO<>(
                            LocalDateTime.now(),
                            HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "An error occurred while processing the request.",
                            null,
                            request.getRequestURI()
                    )
            );
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete category", description = "Deletes a category by ID")
    public ResponseEntity<ApiResponseDTO<?>> delete(@PathVariable Long id, HttpServletRequest request) {
        try {
            deleteCategoryUseCase.execute(id);

            return ResponseEntity.ok(
                    new ApiResponseDTO<>(
                            LocalDateTime.now(),
                            HttpStatus.OK.value(),
                            "Category deleted successfully",
                            null,
                            request.getRequestURI()
                    )
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ApiResponseDTO<>(
                            LocalDateTime.now(),
                            HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "An error occurred while processing the request.",
                            null,
                            request.getRequestURI()
                    )
            );
        }
    }
}
