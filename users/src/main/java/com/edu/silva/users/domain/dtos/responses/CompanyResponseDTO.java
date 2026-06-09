package com.edu.silva.users.domain.dtos.responses;

import com.edu.silva.users.domain.entities.Company;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Schema(description= "Dados da empresa")
public class CompanyResponseDTO {
    @Schema(description = "UUID", example = "45c49cf1-2eef-4896-a2fd-02fd7380a5f0")
    private UUID id;
    @Schema(description = "Data de criação", example = "01/01/2020")
    private Date createdDate;
    @Schema(description = "Nome", example = "Eduardo Henrique LTDA")
    private String name;
    @Schema(description = "Usuários", example = "[{}, {}]")
    private List<UserResponseDTO> users = new ArrayList<>();

    public CompanyResponseDTO(Company company) {
        BeanUtils.copyProperties(company, this);
        this.users = company.getUsers().stream().map(UserResponseDTO::new).toList();
    }
}
