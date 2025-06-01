package com.azenithsolutions.backendapirest.v1.service.component;

import com.azenithsolutions.backendapirest.v1.dto.component.ComponentCatalogResponseDTO;
import com.azenithsolutions.backendapirest.v1.dto.component.ComponentObservationDTO;
import com.azenithsolutions.backendapirest.v1.dto.component.ComponentRequestDTO;
import com.azenithsolutions.backendapirest.v1.utils.jpaSpecification.ComponentSpecification;
import com.azenithsolutions.backendapirest.v1.model.Box;
import com.azenithsolutions.backendapirest.v1.model.Category;
import com.azenithsolutions.backendapirest.v1.model.Component;
import com.azenithsolutions.backendapirest.v1.model.enums.ComponentCondition;
import com.azenithsolutions.backendapirest.v1.repository.BoxRepository;
import com.azenithsolutions.backendapirest.v1.repository.CategoryRepository;
import com.azenithsolutions.backendapirest.v1.repository.ComponentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ComponentService {
    @Autowired
    private ComponentRepository componentRepository;

    @Autowired
    private BoxRepository boxRepository;

    @Autowired
    private CategoryRepository categoryRepository;


    public List<Component> getAllComponents() {
        return componentRepository.findAll();
    }

    public Page<ComponentCatalogResponseDTO> getPagebleComponents(Pageable pageable, String descricao) {
        Specification<Component> spec = ComponentSpecification.filterBy(descricao);

        Page<Component> page = componentRepository.findAll(spec, pageable);

        List<ComponentCatalogResponseDTO> dtos = page.getContent().stream()
                .map(component -> new ComponentCatalogResponseDTO(
                        component.getIdComponente(),
                        component.getNomeComponente(),
                        component.getFkCategoria(),
                        component.getQuantidade(),
                        component.getDescricao(),
                        component.getImagem()
                ))
                .toList();

        return new PageImpl<>(dtos, pageable, page.getTotalElements());
    }

    public List<ComponentCatalogResponseDTO> getFilterComponentList(HashMap<String, Object> filtros) {
        Specification<Component> spec = ComponentSpecification.whereDinamicFilter(filtros);

        String orderBy = (String) filtros.get("orderBy");
        Sort.Direction direction = (boolean) filtros.get("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Integer limit = filtros.containsKey("limit") ? Integer.parseInt(filtros.get("limit").toString()) : null;

        Pageable pageable = PageRequest.of(0, limit);

        if (orderBy != null && !orderBy.isBlank()) {
            pageable = PageRequest.of(0, limit, Sort.by(direction, orderBy));
        }

        Page<Component> components = componentRepository.findAll(spec, pageable);

        List<ComponentCatalogResponseDTO> dtos = components.stream()
                .map(component -> new ComponentCatalogResponseDTO(
                        component.getIdComponente(),
                        component.getNomeComponente(),
                        component.getFkCategoria(),
                        component.getQuantidade(),
                        component.getDescricao(),
                        component.getImagem()
                ))
                .toList();

        return dtos;
    }


    public Optional<Component> findById(Long id) {
        return componentRepository.findById(id);
    }

    public ComponentCatalogResponseDTO findDetailsComponentById(Long id) {
        Optional<Component> component = componentRepository.findById(id);
        if (!component.isEmpty()) {
            ComponentCatalogResponseDTO componentDto = new ComponentCatalogResponseDTO(
                    component.get().getIdComponente(),
                    component.get().getNomeComponente(),
                    component.get().getFkCategoria(),
                    component.get().getQuantidade(),
                    component.get().getDescricao(),
                    component.get().getImagem()
            );
            return componentDto;
        }
        return null;
    }

    public List<Component> getLowStockComponents() {
        try {
            System.out.println("Chamando repository findByQuantityLessThan...");
            return componentRepository.findByQuantityLessThan(1);
        } catch (Exception e) {
            System.err.println("Erro ao buscar componentes de baixo estoque: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public List<ComponentObservationDTO> getInObservationComponents() {
        List<Component> componentsInObservation = componentRepository.findByObservationCondition(ComponentCondition.OBSERVACAO);

        return componentsInObservation.stream()
                .map(component -> new ComponentObservationDTO(
                        component.getIdHardWareTech(),
                        component.getPartNumber(),
                        component.getObservacao(),
                        component.getDescricao()
                ))
                .collect(Collectors.toList());
    }

    public List<Component> getIncompleteComponents() {
        return componentRepository.findByIncomplete(ComponentCondition.OBSERVACAO);
    }

    public List<Component> getComponentsOutOfLastSaleSLA() {
        LocalDate LastSaleSLA = LocalDate.now().minusDays(30);
        return componentRepository.findByLastSaleSLA(LastSaleSLA);
    }

    public List<Integer> getCountOfTrueAndFalseFlagML() {
        Integer componentsWithFlagMLTrue = componentRepository.findByFlagMLTrue();
        Integer componentsWithFlagMLFalse = componentRepository.findByFlagMLFalse();

        return List.of(componentsWithFlagMLTrue, componentsWithFlagMLFalse);
    }

    public Component save(ComponentRequestDTO componentRequestDTO) {
        Component component = convertDtoToEntity(componentRequestDTO);

        if (component.getIdComponente() == null) {
            component.setCreatedAt(LocalDate.now());
        }
        component.setUpdatedAt(LocalDate.now());

        // Set default visibility for new components
        if (component.getIsVisibleCatalog() == null) {
            component.setIsVisibleCatalog(false);
        }

        return componentRepository.save(component);
    }

    public Component update(Long id, ComponentRequestDTO componentRequestDTO) {
        Optional<Component> existingComponentOpt = componentRepository.findById(id);

        if (existingComponentOpt.isEmpty()) {
            return null;
        }

        Component existingComponent = existingComponentOpt.get();
        Component updatedComponent = convertDtoToEntity(componentRequestDTO);

        updatedComponent.setIdComponente(id);
        updatedComponent.setCreatedAt(existingComponent.getCreatedAt());
        updatedComponent.setUpdatedAt(LocalDate.now());
        updatedComponent.setItens(existingComponent.getItens());

        return componentRepository.save(updatedComponent);
    }

    public Component updateVisibility(Long id, Boolean isVisibleCatalog) {
        Optional<Component> componentOpt = componentRepository.findById(id);

        if (componentOpt.isEmpty()) {
            return null;
        }

        Component component = componentOpt.get();
        component.setIsVisibleCatalog(isVisibleCatalog);
        component.setUpdatedAt(LocalDate.now());

        return componentRepository.save(component);
    }

    private Component convertDtoToEntity(ComponentRequestDTO dto) {
        Component component = new Component();

        component.setIdHardWareTech(dto.getIdHardWareTech());

        // Set Box
        Box box = boxRepository.findById(dto.getFkCaixa()).orElse(null);
        component.setFkCaixa(box);

        // Set Category if provided
        if (dto.getFkCategoria() != null) {
            Category category = categoryRepository.findById(dto.getFkCategoria()).orElse(null);
            component.setFkCategoria(category);
        }

        component.setNomeComponente(dto.getNomeComponente());

        // Set Category
        Category category = categoryRepository.findById(dto.getFkCategoria())
                .orElseThrow(() -> new IllegalArgumentException("Category not found with ID: " + dto.getFkCategoria()));
        component.setFkCategoria(category);
        component.setPartNumber(dto.getPartNumber());
        component.setQuantidade(dto.getQuantidade());
        component.setFlagML(dto.getFlagML());
        component.setCodigoML(dto.getCodigoML());
        component.setFlagVerificado(dto.getFlagVerificado());
        component.setCondicao(dto.getCondicao());
        component.setObservacao(dto.getObservacao());
        component.setDescricao(dto.getDescricao());
        component.setImagem(dto.getImagem());

        return component;
    }

    public void delete(Long id) {
        componentRepository.deleteById(id);
    }
}
