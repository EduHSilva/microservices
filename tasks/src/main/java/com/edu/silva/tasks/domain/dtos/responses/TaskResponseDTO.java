package com.edu.silva.tasks.domain.dtos.responses;

import com.edu.silva.tasks.domain.entities.Task;
import com.edu.silva.tasks.domain.enums.TaskStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class TaskResponseDTO {
    private UUID id;
    private Date createdDate;
    private String title;
    private Date dueDate;
    private TaskStatus status;

    public TaskResponseDTO(Task task) {
        BeanUtils.copyProperties(task, this);
    }
}
