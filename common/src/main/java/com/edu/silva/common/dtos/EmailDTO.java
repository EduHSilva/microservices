package com.edu.silva.common.dtos;


import com.edu.silva.common.enums.EmailType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmailDTO {
    UUID userID;
    String emailTo;
    String localeTag;
    EmailType emailType;
}
