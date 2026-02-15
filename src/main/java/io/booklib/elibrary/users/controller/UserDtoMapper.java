package io.booklib.elibrary.users.controller;

import io.booklib.elibrary.users.service.UserDTO;

public class UserDtoMapper {

    public UserDTO mapToDTO(UserRegistrationRequest userRegistrationRequest){
         return new UserDTO(null, userRegistrationRequest.username(),
                userRegistrationRequest.password(), userRegistrationRequest.email());
    }

    public UserResponse mapToResponse(UserDTO userDTO){
        return new UserResponse(userDTO.userId(), userDTO.username(), userDTO.password(), userDTO.email());
    }
}
