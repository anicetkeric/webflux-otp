package com.bootteam.springsecuritywebfluxotp.service;

import com.bootteam.springsecuritywebfluxotp.common.exception.EmailAlreadyUsedException;
import com.bootteam.springsecuritywebfluxotp.common.exception.OtpCheckingException;
import com.bootteam.springsecuritywebfluxotp.common.exception.UsernameAlreadyUsedException;
import com.bootteam.springsecuritywebfluxotp.domain.document.User;
import com.bootteam.springsecuritywebfluxotp.domain.model.OtpRequest;
import com.bootteam.springsecuritywebfluxotp.repository.RoleRepository;
import com.bootteam.springsecuritywebfluxotp.repository.UserRepository;
import com.bootteam.springsecuritywebfluxotp.security.TokenProvider;
import com.bootteam.springsecuritywebfluxotp.service.mapper.UserMapper;
import com.bootteam.springsecuritywebfluxotp.service.mapper.dto.OtpTokenDTO;
import com.bootteam.springsecuritywebfluxotp.service.mapper.dto.UserPasswordDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.internal.constraintvalidators.hv.EmailValidator;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotNull;
import java.time.Instant;

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
    private final TokenProvider tokenProvider;
    private final UserMapper mapper;
    @Override
    public Mono<String> checkCode(String authenticationName, @NotNull String code) {

        String username = StringUtils.trimToNull(authenticationName.toLowerCase());
        Mono<User> userMono = (new EmailValidator().isValid(username, null)) ? userRepository.findOneByEmailIgnoreCase(username)
                : userRepository.findOneByUsernameIgnoreCase(username);

        return userMono.flatMap(u -> {
                    if(code.equals(u.getOtpRequest().getCode())){
                        if (Instant.now().isAfter(u.getOtpRequest().getTime())){
                            return  Mono.error(new OtpCheckingException("Otp code expired"));
                        }
                    }else {
                        return Mono.error(new OtpCheckingException("OTP code not valid"));
                    }
                    return Mono.just(u);
                }).flatMap(u -> tokenProvider.getCurrentUserAuthentication())
                .flatMap(t -> Mono.just(tokenProvider.generateToken(t, true)));
    }
    @Override
    public Mono<User> createUser(UserPasswordDTO userPasswordDTO) {

        return userRepository.findOneByUsernameIgnoreCase(userPasswordDTO.getUsername().toLowerCase())
                .flatMap(existingUser -> Mono.error(new UsernameAlreadyUsedException()))
                .cast(User.class)

                .switchIfEmpty(Mono.defer(() -> userRepository.findOneByEmailIgnoreCase(userPasswordDTO.getEmail()))
                        .flatMap(existingUser -> Mono.error(new EmailAlreadyUsedException()))
                        .cast(User.class)

                        .switchIfEmpty(Mono.defer(() -> {

                                    userPasswordDTO.getRoles().forEach(r -> roleRepository.findByName(r.getName())
                                            .switchIfEmpty(Mono.defer(() -> roleRepository.save(r))));

                                    userPasswordDTO.setUsername(userPasswordDTO.getUsername().toLowerCase());
                                    User newUser = mapper.UserPasswordDTOToUser(userPasswordDTO);

                                    String encryptedPassword = passwordEncoder.encode(userPasswordDTO.getPassword());
                                    newUser.setPassword(encryptedPassword);
                                    return userRepository.save(newUser);
                                })
                        )
                );
    }

    @Override
    public Mono<OtpTokenDTO> setUserOtp(Authentication authentication) {

        String username = StringUtils.trimToNull(authentication.getName().toLowerCase());
        Mono<User> userMono = (new EmailValidator().isValid(username, null)) ? userRepository.findOneByEmailIgnoreCase(username)
                : userRepository.findOneByUsernameIgnoreCase(username);

       return userMono.flatMap(u -> {
            u.setOtpRequest(new OtpRequest(RandomStringUtils.randomAlphanumeric(4), Instant.now()));
            return userRepository.save(u);
        }).zipWhen(token -> Mono.just(tokenProvider.generateToken(authentication, true)))
               .flatMap(t -> Mono.just(new OtpTokenDTO(t.getT2(), t.getT1().getOtpRequest().getCode()))).doOnSuccess(t -> sendOtp(t.otpCode()));
    }

    private void sendOtp(String code){
        LOGGER.info("send code {} to user", code);
    }

}
