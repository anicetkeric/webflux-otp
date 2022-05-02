package com.bootteam.springsecuritywebfluxotp.service;

import com.bootteam.springsecuritywebfluxotp.domain.document.Role;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RoleService {

    Mono<Role> createRole(String roleName);

    Mono<Role> getRoleByName(String roleName);

    Flux<Role> getAllRoles();
}
