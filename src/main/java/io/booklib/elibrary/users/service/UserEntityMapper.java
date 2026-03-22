package io.booklib.elibrary.users.service;

import io.booklib.elibrary.users.repository.UserEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserEntityMapper {

    private final PasswordEncoder passwordEncoder;

    public UserEntityMapper (PasswordEncoder passwordEncoder){
        this.passwordEncoder = passwordEncoder;
    }

    public UserEntity mapToEntity(UserDTO userDTO){
        return new UserEntity(userDTO.userId(), userDTO.username(), passwordEncoder.encode(userDTO.password()), userDTO.email(), userDTO.role());
    }

    public UserDTO mapToDTO(UserEntity userEntity){
        return new UserDTO(userEntity.getId(), userEntity.getUsername(), userEntity.getPassword(), userEntity.getEmail(), userEntity.getRole());
    }
}
