package com.edu.silva.tasks.services.impl;

import com.edu.silva.tasks.domain.dtos.requests.AddTaskDTO;
import com.edu.silva.tasks.domain.dtos.responses.TaskResponseDTO;
import com.edu.silva.tasks.domain.entities.Task;
import com.edu.silva.tasks.domain.enums.TaskStatus;
import com.edu.silva.tasks.infra.exceptions.CustomExceptions;
import com.edu.silva.tasks.repositories.TaskRepository;
import com.edu.silva.tasks.services.TaskService;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository repository;
//    private final UserProducer producer;

    public TaskServiceImpl(TaskRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public TaskResponseDTO save(AddTaskDTO dto) {
        Task task = new Task();
        BeanUtils.copyProperties(dto, task);
        task.setStatus(TaskStatus.PENDING);
        repository.save(task);
        return new TaskResponseDTO(task);
    }

    @Override
    public void delete(UUID id) {
        repository.deleteById(id);
    }

    @Override
    public Page<@NonNull TaskResponseDTO> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findAll(pageable).map(TaskResponseDTO::new);
    }

    @Override
    public TaskResponseDTO findById(UUID id) {
        return new TaskResponseDTO(repository.findById(id)
                .orElseThrow(() -> new CustomExceptions.EntityNotFoundException(Task.class.getSimpleName(), id)));
    }

    @Override
    public TaskResponseDTO update(UUID id, AddTaskDTO updateUserRequest) {
        return repository.findById(id)
                .map(user -> {
                    user.setTitle(updateUserRequest.title());
                    return new TaskResponseDTO(repository.save(user));
                })
                .orElseThrow(() -> new CustomExceptions.EntityNotFoundException(Task.class.getSimpleName(), id));
    }
}
