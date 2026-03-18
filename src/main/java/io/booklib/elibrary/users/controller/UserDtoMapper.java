package io.booklib.elibrary.users.controller;

import io.booklib.elibrary.users.service.UserDTO;
import io.booklib.elibrary.users.service.UserRole;

public class UserDtoMapper {

    public UserDTO mapToDTO(UserRegistrationRequest userRegistrationRequest){
         return new UserDTO(null, userRegistrationRequest.username(),
                userRegistrationRequest.password(), userRegistrationRequest.email(), userRegistrationRequest.role());
    }

    public UserResponse mapToResponse(UserDTO userDTO){
        return new UserResponse(userDTO.userId(), userDTO.username(), userDTO.email(),  userDTO.role());
    }
}
