package com.azenithsolutions.backendapirest.v1.service;

import com.azenithsolutions.backendapirest.v1.model.User;
import com.azenithsolutions.backendapirest.v1.repository.UserRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public List<User> findAllUsers(){
        return userRepository.findAll();
    }
    public User register(User user) throws EntityExistsException{
        if(userRepository.existsByEmail(user.getEmail())) {
            throw new EntityExistsException("User with this email already exists");
        }
        return userRepository.save(user);
    }

    public User findByUserByEmail(String email) throws EntityNotFoundException {
        User userFound = userRepository.findByEmail(email);

        if(userFound == null){
            throw new EntityNotFoundException("User not found");
        }
        return userFound;
    }
}
