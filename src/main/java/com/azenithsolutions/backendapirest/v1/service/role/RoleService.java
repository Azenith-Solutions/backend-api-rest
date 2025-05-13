package com.azenithsolutions.backendapirest.v1.service.role;

import com.azenithsolutions.backendapirest.v1.model.Role;
import com.azenithsolutions.backendapirest.v1.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;

    public List<Role> findAllRoles() {
        return roleRepository.findAll();
    }
}
