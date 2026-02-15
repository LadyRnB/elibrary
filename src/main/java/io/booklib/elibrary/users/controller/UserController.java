package io.booklib.elibrary.users.controller;

import io.booklib.elibrary.users.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("users")
public class UserController {
    private final UserService userService;
    private final UserDtoMapper mapper = new UserDtoMapper();

    public UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping
    public UserResponse registerUser(@RequestBody @Valid UserRegistrationRequest userRegistrationRequest) {
        return mapper.mapToResponse(userService.registerUser(mapper.mapToDTO(userRegistrationRequest)));
    }
}
