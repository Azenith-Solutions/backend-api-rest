package com.azenithsolutions.backendapirest.v1.service.component;

import com.azenithsolutions.backendapirest.v1.dto.component.ComponentRequestDTO;
import com.azenithsolutions.backendapirest.v1.model.Box;
import com.azenithsolutions.backendapirest.v1.model.Category;
import com.azenithsolutions.backendapirest.v1.model.Component;
import com.azenithsolutions.backendapirest.v1.model.enums.ComponentCondition;
import com.azenithsolutions.backendapirest.v1.repository.BoxRepository;
import com.azenithsolutions.backendapirest.v1.repository.CategoryRepository;
import com.azenithsolutions.backendapirest.v1.repository.ComponentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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

    public Optional<Component> findById(Long id) {
        return componentRepository.findById(id);
    }

    public List<Component> getLowStockComponents() {
        return componentRepository.findByQuantityLessThanTen();
    }

    public List<Component> getCriticsAndObservations() {
        return componentRepository.findByCriticAndObservationCondition(ComponentCondition.CRITICO, ComponentCondition.OBSERVACAO);
    }

    public List<Component> getIncompleteComponents() {
        return componentRepository.findByIncomplete(ComponentCondition.OBSERVACAO);
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
