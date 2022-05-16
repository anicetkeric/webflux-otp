package com.bootteam.springsecuritywebfluxotp.service.mapper.dto;

import com.bootteam.springsecuritywebfluxotp.domain.document.Role;
import com.bootteam.springsecuritywebfluxotp.domain.model.OtpRequest;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private String id;

    @NotNull
    @Size(min = 1, max = 50)
    private String username;

    @Size(max = 50)
    private String firstName;

    @Size(max = 50)
    private String lastName;

    @Email
    @Size(min = 5, max = 254)
    private String email;

    private OtpRequest otpRequest;

    @JsonIgnore
    private Set<Role> roles = new HashSet<>();

    private boolean enabled = false;

    private boolean accountNonExpired;

    private boolean credentialsNonExpired;

    private boolean accountNonLocked;

}
