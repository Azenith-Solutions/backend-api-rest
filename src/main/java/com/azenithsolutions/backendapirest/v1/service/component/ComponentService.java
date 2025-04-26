package com.azenithsolutions.backendapirest.v1.service.component;

import com.azenithsolutions.backendapirest.v1.model.Component;
import com.azenithsolutions.backendapirest.v1.repository.ComponentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ComponentService {
    @Autowired
    private ComponentRepository componentRepository;

    public List<Component> getAllComponents() {
        return componentRepository.findAll();

    }

    public Optional<Component> findById(Long id) {
        return componentRepository.findById(id);

    }

    public Component save(Component component) {
        return componentRepository.save(component);
    }

    public void delete(Long id) {
        componentRepository.deleteById(id);
    }
}
