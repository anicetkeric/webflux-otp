package com.bootteam.springsecuritywebfluxotp.service;

import com.bootteam.springsecuritywebfluxotp.common.exception.EmailAlreadyUsedException;
import com.bootteam.springsecuritywebfluxotp.common.exception.RoleAlreadyUsedException;
import com.bootteam.springsecuritywebfluxotp.common.exception.UsernameAlreadyUsedException;
import com.bootteam.springsecuritywebfluxotp.domain.document.Role;
import com.bootteam.springsecuritywebfluxotp.domain.document.User;
import com.bootteam.springsecuritywebfluxotp.repository.RoleRepository;
import com.bootteam.springsecuritywebfluxotp.repository.UserRepository;
import com.bootteam.springsecuritywebfluxotp.service.mapper.UserMapper;
import com.bootteam.springsecuritywebfluxotp.service.mapper.dto.UserPasswordDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service class for managing roles.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public Mono<Role> createRole(String roleName) {

        return roleRepository.findByName(roleName)
                .flatMap(existingRole -> Mono.error(new RoleAlreadyUsedException()))
                .cast(Role.class)

                .switchIfEmpty(Mono.defer(() -> roleRepository.save(Role.builder().name(roleName).build())));
    }


    @Override
    public Mono<Role> getRoleByName(String roleName) {
        return roleRepository.findByName(roleName)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND,
                        String.format("Unable to find Role. Role with ID: '%s' Not Found.", roleName))));
    }

    @Override
    public Flux<Role> getAllRoles() {
        return roleRepository.findAll()
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NO_CONTENT, "There aren't any role in DB.")));
    }

}
