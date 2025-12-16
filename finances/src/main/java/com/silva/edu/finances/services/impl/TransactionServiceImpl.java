package com.silva.edu.finances.services.impl;

import com.silva.edu.finances.domain.dtos.request.AddTransactionDTO;
import com.silva.edu.finances.domain.dtos.request.UpdateTransactionDTO;
import com.silva.edu.finances.domain.dtos.response.TransactionResponseDTO;
import com.silva.edu.finances.domain.entities.Transaction;
import com.silva.edu.finances.domain.enums.TransactionStatus;
import com.silva.edu.finances.infra.exceptions.CustomExceptions;
import com.silva.edu.finances.repositories.TransactionRepository;
import com.silva.edu.finances.services.TransactionService;
import lombok.NonNull;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository repository;

    public TransactionServiceImpl(TransactionRepository repository) {
        this.repository = repository;
    }

    @Override
    public TransactionResponseDTO save(AddTransactionDTO request) {
        Transaction transaction = new Transaction();
        BeanUtils.copyProperties(transaction, request);
        transaction.setStatus(TransactionStatus.OK);
        repository.save(transaction);
        return new TransactionResponseDTO(transaction);
    }

    @Override
    public void delete(UUID id) {
        repository.deleteById(id);
    }

    @Override
    public Page<@NonNull TransactionResponseDTO> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findAll(pageable).map(TransactionResponseDTO::new);
    }

    @Override
    public TransactionResponseDTO findById(UUID id) {
        return new TransactionResponseDTO(repository.findById(id)
                .orElseThrow(() -> new CustomExceptions.EntityNotFoundException(Transaction.class.getSimpleName(), id)));
    }

    @Override
    public TransactionResponseDTO update(UUID id, UpdateTransactionDTO request) {
        return repository.findById(id)
                .map(transaction -> {
                    transaction.setValue(request.value());
                    transaction.setTitle(request.title());
                    return new TransactionResponseDTO(repository.save(transaction));
                })
                .orElseThrow(() -> new CustomExceptions.EntityNotFoundException(Transaction.class.getSimpleName(), id));
    }
}
