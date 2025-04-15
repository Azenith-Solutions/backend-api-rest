package com.azenithsolutions.backendapirest.v1.service;

import com.azenithsolutions.backendapirest.v1.dto.ComponentResponseDTO;
import com.azenithsolutions.backendapirest.v1.model.Component;
import com.azenithsolutions.backendapirest.v1.repository.ComponentRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ComponentService {
    @Autowired
    private ComponentRepository componentRepository;

    public List<ComponentResponseDTO> getAllComponents() {
        return componentRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ComponentResponseDTO findById(String id) {
        return componentRepository.findById(Long.valueOf(id))
                .map(this::convertToDTO)
                .orElseThrow(() -> new EntityNotFoundException("Componente não encontrado"));
    }

    public ComponentResponseDTO save(Component component) {
        validateComponent(component);
        return convertToDTO(componentRepository.save(component));
    }

    public void delete(String id) {
        if (!componentRepository.existsById(Long.valueOf(id))) {
            throw new EntityNotFoundException("Componente não encontrado");
        }
        componentRepository.deleteById(Long.valueOf(id));
    }

    private ComponentResponseDTO convertToDTO(Component component) {
        ComponentResponseDTO dto = new ComponentResponseDTO(component);
        dto.setIdHardWareTech(component.getIdHardWareTech());
        dto.setCaixa(component.getCaixa());
        dto.setPartNumber(component.getPartNumber());
        dto.setQuantidade(component.getQuantidade());
        dto.setFlagML(component.getFlagML());
        dto.setCodigoML(component.getCodigoML());
        dto.setFlagVerificado(component.getFlagVerificado());
        dto.setCondicao(component.getCondicao());
        dto.setObservacao(component.getObservacao());
        dto.setDescricao(component.getDescricao());
        return dto;
    }

    private void validateComponent(Component component) {
        if (component.getIdHardWareTech() == null || component.getIdHardWareTech().trim().isEmpty()) {
            throw new IllegalArgumentException("ID do componente é obrigatório");
        }
        if (component.getQuantidade() < 0) {
            throw new IllegalArgumentException("Quantidade não pode ser negativa");
        }
    }
}
