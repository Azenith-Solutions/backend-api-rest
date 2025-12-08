package com.azenithsolutions.backendapirest.v2.infrastructure.web.mappers;

import com.azenithsolutions.backendapirest.v2.core.domain.model.user.User;
import com.azenithsolutions.backendapirest.v2.infrastructure.persistence.jpa.entity.RoleEntity;
import com.azenithsolutions.backendapirest.v2.infrastructure.persistence.jpa.entity.UserEntity;
import com.azenithsolutions.backendapirest.v2.infrastructure.web.dto.user.UserListResponseDTO;
import com.azenithsolutions.backendapirest.v2.infrastructure.web.dto.user.UserResponseDTO;
import com.azenithsolutions.backendapirest.v2.infrastructure.web.dto.user.UserUpdateResponseDTO;

import java.util.Objects;

public class UserEntityMapper {
    public static UserEntity toEntity(User user){
        if(Objects.isNull(user)){
            return null;
        }
        RoleEntity roleEntity = RoleEntityMapper.toEntity(user.getFkFuncao());
        UserEntity userEntity = new UserEntity();
        userEntity.setId(Objects.isNull(user.getId()) ? null : user.getId());
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

    public static UserResponseDTO toResposeDTO(User user){
        if(Objects.isNull(user)) return null;

        UserResponseDTO userResponseDTO = new UserResponseDTO(
                user.getFullName().getValue(),
                user.getEmail().getValue(),
                user.getFkFuncao().getFuncao()
        );

        return  userResponseDTO;
    }

    public static UserListResponseDTO toListResposeDTO(User user){
        if(Objects.isNull(user)) return null;

        UserListResponseDTO userListResponseDTO = new UserListResponseDTO(
                user.getId(),
                user.getFullName().getValue(),
                user.getEmail().getValue(),
                user.getFkFuncao().getFuncao(),
                user.getProfilePicture(),
                user.getStatus(),
                user.getCreatedAt()
        );

        return  userListResponseDTO;
    }

    public static UserUpdateResponseDTO toUserUpdateResponseDTO(User user){
        if(Objects.isNull(user)) return null;

        UserUpdateResponseDTO userListResponseDTO = new UserUpdateResponseDTO(
                user.getFullName().getValue(),
                user.getEmail().getValue(),
                user.getFkFuncao().getFuncao(),
                user.getStatus(),
                user.getUpdatedAt()
        );

        return  userListResponseDTO;
    }

}
