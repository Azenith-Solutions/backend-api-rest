package com.azenithsolutions.backendapirest.v1.service.component;

import com.azenithsolutions.backendapirest.v1.dto.component.ComponentCatalogResponseDTO;
import com.azenithsolutions.backendapirest.v1.dto.component.ComponentRequestDTO;
import com.azenithsolutions.backendapirest.v1.utils.jpaSpecification.ComponentSpecification;
import com.azenithsolutions.backendapirest.v1.model.Box;
import com.azenithsolutions.backendapirest.v1.model.Category;
import com.azenithsolutions.backendapirest.v1.model.Component;
import com.azenithsolutions.backendapirest.v1.repository.BoxRepository;
import com.azenithsolutions.backendapirest.v1.repository.CategoryRepository;
import com.azenithsolutions.backendapirest.v1.repository.ComponentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

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
                        component.getFkCategoria(),
                        component.getQuantidade(),
                        component.getDescricao()
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

        if(orderBy != null && !orderBy.isBlank()){
            pageable = PageRequest.of(0, limit, Sort.by(direction, orderBy));
        }

        Page<Component> components = componentRepository.findAll(spec, pageable);

        List<ComponentCatalogResponseDTO> dtos = components.stream()
                .map(component -> new ComponentCatalogResponseDTO(
                        component.getIdComponente(),
                        component.getFkCategoria(),
                        component.getQuantidade(),
                        component.getDescricao()
                ))
                .toList();

        return dtos;
    }


    public Optional<Component> findById(Long id) {
        return componentRepository.findById(id);
    }

    public ComponentCatalogResponseDTO findDetailsCoponentById(Long id) {
        Optional<Component> component = componentRepository.findById(id);
        if(!component.isEmpty()){
            ComponentCatalogResponseDTO componentDto = new ComponentCatalogResponseDTO(
                    component.get().getIdComponente(),
                    component.get().getFkCategoria(),
                    component.get().getQuantidade(),
                    component.get().getDescricao()
            );
            return componentDto;
        }
        return null;
    }

    public Component save(ComponentRequestDTO componentRequestDTO) {
        Component component = convertDtoToEntity(componentRequestDTO);
        
        if (component.getIdComponente() == null) {
            component.setCreatedAt(LocalDate.now());
        }
        component.setUpdatedAt(LocalDate.now());
        
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
    
    private Component convertDtoToEntity(ComponentRequestDTO dto) {
        Component component = new Component();
        
        component.setIdHardWareTech(dto.getIdHardWareTech());
        
        // Set Box
        Box box = boxRepository.findById(dto.getCaixa())
                .orElseThrow(() -> new IllegalArgumentException("Box not found with ID: " + dto.getCaixa()));
        component.setFkCaixa(box);
        
        // Set Category
        Category category = categoryRepository.findById(dto.getCategoria())
                .orElseThrow(() -> new IllegalArgumentException("Category not found with ID: " + dto.getCategoria()));
        component.setFkCategoria(category);
        
        component.setPartNumber(dto.getPartNumber());
        component.setQuantidade(dto.getQuantidade());
        component.setFlagML(dto.getFlagML());
        component.setCodigoML(dto.getCodigoML());
        component.setFlagVerificado(dto.getFlagVerificado());
        component.setCondicao(dto.getCondicao());
        component.setObservacao(dto.getObservacao());
        component.setDescricao(dto.getDescricao());
        
        return component;
    }

    public void delete(Long id) {
        componentRepository.deleteById(id);
    }
}
