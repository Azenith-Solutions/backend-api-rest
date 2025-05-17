package com.azenithsolutions.backendapirest.v1.service.user;

import com.azenithsolutions.backendapirest.v1.dto.user.UserUpdateRequestDTO;
import com.azenithsolutions.backendapirest.v1.dto.user.UserUpdateResponseDTO;
import com.azenithsolutions.backendapirest.v1.model.Role;
import com.azenithsolutions.backendapirest.v1.model.User;
import com.azenithsolutions.backendapirest.v1.repository.RoleRepository;
import com.azenithsolutions.backendapirest.v1.repository.UserRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PutMapping;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

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

    public Optional<User> updateUser(Long id, UserUpdateRequestDTO body) {
        Optional<User> userOptional = userRepository.findById(id);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            if (this.isEmailInUseByAnotherUser(id, body.getEmail())) {
                throw new EntityExistsException("Email is already in use by another user");
            }

            user.setFullName(body.getFullName());
            user.setEmail(body.getEmail());
            user.setFkFuncao(
                    roleRepository.findById(body.getRole())
                            .orElseThrow(() -> new IllegalArgumentException("Invalid role ID"))
            );

            if (body.getPassword() != null && !body.getPassword().isBlank()) {
                user.setPassword(passwordEncoder.encode(body.getPassword()));
            }
            user.setUpdatedAt(LocalDate.now());
            userRepository.save(user);

            return Optional.of(user);
        }
        throw new EntityNotFoundException("User with this ID does not exist");

    }

    public boolean isEmailInUseByAnotherUser(Long userId, String email) {
        Optional<User> userWithEmail = Optional.ofNullable(userRepository.findByEmail(email));
        return userWithEmail.isPresent() && userWithEmail.get().getId() != userId;
    }

    public void deleteUser(Long id) {
        if(userRepository.existsById(id)) {
            userRepository.deleteById(id);
        } else {
            throw new EntityExistsException("User with this ID does not exist");
        }
    }
}
