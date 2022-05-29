package com.bootteam.springsecuritywebfluxotp.service.mapper.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailSenderDTO {

   private String to;
   private String subject;
   private String content;
   private boolean isMultipart;
   private boolean isHtml;
}
