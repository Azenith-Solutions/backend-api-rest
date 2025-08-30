package com.azenithsolutions.backendapirest.v2.infrastructure.persistence.adapter;

import com.azenithsolutions.backendapirest.v2.core.domain.model.user.User;
import com.azenithsolutions.backendapirest.v2.core.domain.repository.UserGateway;
import com.azenithsolutions.backendapirest.v2.infrastructure.persistence.entity.UserEntity;
import com.azenithsolutions.backendapirest.v2.infrastructure.persistence.repository.jpa.SpringDataUserRepository;
import com.azenithsolutions.backendapirest.v2.infrastructure.web.mappers.UserEntityMapper;
import org.springframework.stereotype.Service;

@Service
public class UserRespositoryAdapter implements UserGateway {

    private final SpringDataUserRepository repository;

    public UserRespositoryAdapter(SpringDataUserRepository repository) {
        this.repository = repository;
    }

    @Override
    public User save(User user) {
        UserEntity userEntity = UserEntityMapper.toEntity(user);
        UserEntity registerdUser = repository.save(userEntity);
        User registerDomain = UserEntityMapper.toDomain(registerdUser);
        return registerDomain;
    }

    @Override
    public boolean existsByEmail(String email) {
        return repository.existsByEmail(email);
    }
}
