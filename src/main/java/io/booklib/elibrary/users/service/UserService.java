package io.booklib.elibrary.users.service;

import io.booklib.elibrary.users.repository.UserEntity;
import io.booklib.elibrary.users.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByUsername(username);
        if (userEntity == null) {
            throw new UsernameNotFoundException("invalid username " + username);
        }
        return mapper.mapToDTO(userEntity);
    }
}
