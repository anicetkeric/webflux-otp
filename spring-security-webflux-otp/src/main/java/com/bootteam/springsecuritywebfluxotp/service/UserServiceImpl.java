package com.bootteam.springsecuritywebfluxotp.service;

import com.bootteam.springsecuritywebfluxotp.common.exception.EmailAlreadyUsedException;
import com.bootteam.springsecuritywebfluxotp.common.exception.UsernameAlreadyUsedException;
import com.bootteam.springsecuritywebfluxotp.domain.document.User;
import com.bootteam.springsecuritywebfluxotp.repository.RoleRepository;
import com.bootteam.springsecuritywebfluxotp.repository.UserRepository;
import com.bootteam.springsecuritywebfluxotp.service.mapper.UserMapper;
import com.bootteam.springsecuritywebfluxotp.service.mapper.dto.UserPasswordDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Service class for managing users.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final RoleRepository roleRepository;

    private final UserMapper mapper;

    public Mono<User> checkCode(String code) {


        return Mono.just(null);
    }

    public Mono<User> register(UserPasswordDTO userDTO) {
        return userRepository
            .findOneByUsernameIgnoreCase(userDTO.getUsername().toLowerCase())
            .flatMap(existingUser -> Mono.error(new UsernameAlreadyUsedException()))
            .then(userRepository.findOneByEmailIgnoreCase(userDTO.getEmail()))
            .flatMap(existingUser -> Mono.error(new EmailAlreadyUsedException())
            .then(
                Mono.fromCallable(() -> {
                    userDTO.setUsername(userDTO.getUsername().toLowerCase());
                    User newUser = mapper.UserPasswordDTOToUser(userDTO);

                    String encryptedPassword = passwordEncoder.encode(userDTO.getPassword());
                    newUser.setPassword(encryptedPassword);
                    return newUser;
                })
            )
            .flatMap(userRepository::save));
    }



}
