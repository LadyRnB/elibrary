package io.booklib.elibrary.users.service;

import io.booklib.elibrary.users.repository.UserEntity;
import io.booklib.elibrary.users.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserEntityMapper mapper;

    public UserService(UserRepository userRepository,  UserEntityMapper mapper) {
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    public UserDTO registerUser(UserDTO userDTO) {
        UserEntity userEntity = mapper.mapToEntity(userDTO);
        return mapper.mapToDTO(userRepository.save(userEntity));
    }
}
