package com.azenithsolutions.backendapirest.v2.infrastructure.persistence.jpa.adapter;

import com.azenithsolutions.backendapirest.v2.core.domain.model.user.User;
import com.azenithsolutions.backendapirest.v2.core.domain.repository.UserGateway;
import com.azenithsolutions.backendapirest.v2.infrastructure.persistence.jpa.entity.UserEntity;
import com.azenithsolutions.backendapirest.v2.infrastructure.persistence.jpa.repository.SpringDataUserRepository;
import com.azenithsolutions.backendapirest.v2.infrastructure.web.mappers.UserEntityMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserRespositoryAdapter implements UserGateway {

    private final SpringDataUserRepository repository;

    public UserRespositoryAdapter(SpringDataUserRepository repository) {
        this.repository = repository;
    }

    @Override
    public User findById(Integer id) {
        Optional<UserEntity> userEntity = repository.findById(id);
        if(userEntity.get() == null) return null;
        return UserEntityMapper.toDomain(userEntity.get());
    }

    @Override
    public void deleteById(Integer id) {
        repository.deleteById(id);
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

    @Override
    public List<User> findAll() {
        List<UserEntity> userEntityList = repository.findAll();
        List<User> userList = userEntityList.stream().map(UserEntityMapper::toDomain).toList();

        return userList;
    }

    @Override
    public User findByEmail(String email) {
        Optional<UserEntity> userEntity = repository.findByEmail(email);
        if(userEntity.get() == null) return null;
        return UserEntityMapper.toDomain(userEntity.get());
    }
}
