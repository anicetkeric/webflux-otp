package com.bootteam.springsecuritywebfluxotp.domain.model;

import lombok.*;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class OtpRequest implements Serializable {

    @Size(max = 4)
    private String code;

    private Instant time;
}
