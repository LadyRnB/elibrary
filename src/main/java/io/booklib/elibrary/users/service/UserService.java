package io.booklib.elibrary.users.service;

import io.booklib.elibrary.users.repository.UserEntity;
import io.booklib.elibrary.users.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserEntityMapper mapper = new UserEntityMapper();

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDTO registerUser(UserDTO userDTO) {
        UserEntity userEntity = mapper.mapToEntity(userDTO);
        return mapper.mapToDTO(userRepository.save(userEntity));
    }
}
