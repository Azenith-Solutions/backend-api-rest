package com.azenithsolutions.backendapirest.v2.infrastructure.web.mappers;

import com.azenithsolutions.backendapirest.v2.core.domain.model.user.User;
import com.azenithsolutions.backendapirest.v2.infrastructure.persistence.entity.RoleEntity;
import com.azenithsolutions.backendapirest.v2.infrastructure.persistence.entity.UserEntity;

import java.util.Objects;

public class UserEntityMapper {
    public static UserEntity toEntity(User user){
        if(Objects.isNull(user)){
            return null;
        }
        RoleEntity roleEntity = RoleEntityMapper.toEntity(user.getFkFuncao());
        UserEntity userEntity = new UserEntity();
        userEntity.setFullName(user.getFullName().getValue());
        userEntity.setEmail(user.getEmail().getValue());
        userEntity.setPassword(user.getPassword().getValue());
        userEntity.setProfilePicture(user.getProfilePicture());
        userEntity.setStatus(user.getStatus());
        userEntity.setFkFuncao(roleEntity);
        userEntity.setCreatedAt(user.getCreatedAt());
        userEntity.setUpdatedAt(user.getUpdatedAt());

        return userEntity;
    }

    public static User toDomain(UserEntity userEntity){
        if(userEntity == null){
            return null;
        }

        User user = User.create(
                userEntity.getId(),
                userEntity.getFullName(),
                userEntity.getEmail(),
                userEntity.getPassword(),
                userEntity.getProfilePicture(),
                userEntity.getStatus(),
                userEntity.getFkFuncao().getIdFuncao(),
                userEntity.getFkFuncao().getFuncao(),
                userEntity.getCreatedAt(),
                userEntity.getUpdatedAt()
        );

        return user;
    }

}
