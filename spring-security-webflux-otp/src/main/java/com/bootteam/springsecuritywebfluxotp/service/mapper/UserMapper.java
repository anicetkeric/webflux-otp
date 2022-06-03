package com.bootteam.springsecuritywebfluxotp.service.mapper;

import com.bootteam.springsecuritywebfluxotp.domain.document.User;
import com.bootteam.springsecuritywebfluxotp.service.mapper.dto.UserDTO;
import com.bootteam.springsecuritywebfluxotp.service.mapper.dto.UserPasswordDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link User} and its DTO {@link UserDTO}.
 */
@Mapper
public interface UserMapper {

    User toEntity(UserDTO dto);

    UserDTO toDto(User entity);


    default User userPasswordDTOToUser(UserPasswordDTO userPasswordDTO){
        return User.builder()
                .username(userPasswordDTO.getUsername())
                .email(userPasswordDTO.getEmail()).firstName(userPasswordDTO.getFirstName())
                .lastName(userPasswordDTO.getLastName()).password(userPasswordDTO.getPassword())
                .roles(userPasswordDTO.getRoles()).enabled(true).accountNonLocked(false)
                .accountNonExpired(false).credentialsNonExpired(false)
                .build();
    }
}
