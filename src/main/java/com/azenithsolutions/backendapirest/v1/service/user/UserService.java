package com.azenithsolutions.backendapirest.v1.service.user;

import com.azenithsolutions.backendapirest.v1.model.Role;
import com.azenithsolutions.backendapirest.v1.model.User;
import com.azenithsolutions.backendapirest.v1.repository.RoleRepository;
import com.azenithsolutions.backendapirest.v1.repository.UserRepository;
import jakarta.persistence.EntityExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    public List<User> findAllUsers(){
        return userRepository.findAll();
    }

    public User register(User user) throws EntityExistsException{
        if(userRepository.existsByEmail(user.getEmail())) {
            throw new EntityExistsException("User with this email already exists");
        }

        user.setCreatedAt(LocalDate.now());
        user.setUpdatedAt(LocalDate.now());

        return userRepository.save(user);
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<Role> findRoleById(Long id) {
        return roleRepository.findById(id);
    }
}
