package io.booklib.elibrary.users.service;

import io.booklib.elibrary.users.repository.UserEntity;

public class UserEntityMapper {

    public UserEntity mapToEntity(UserDTO userDTO){
        return new UserEntity(userDTO.userId(), userDTO.username(), userDTO.password(), userDTO.email());
    }

    public UserDTO mapToDTO(UserEntity userEntity){
        return new UserDTO(userEntity.getId(), userEntity.getUsername(), userEntity.getPassword(), userEntity.getEmail());
    }

}
