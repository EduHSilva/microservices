package com.silva.edu.finances.domain.dtos.response;

import com.silva.edu.finances.domain.entities.Transaction;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class TransactionResponseDTO {
    private UUID id;

    public TransactionResponseDTO(Transaction transaction) {
        BeanUtils.copyProperties(transaction, this);
    }

}
