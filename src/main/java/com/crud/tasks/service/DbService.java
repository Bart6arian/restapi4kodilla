package com.crud.tasks.service;

import com.crud.tasks.domain.Task;
import com.crud.tasks.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DbService {

    private final TaskRepository taskRepository;

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Optional<Task> getTaskById(final Long id) {
        return taskRepository.findById(id);
    }

    public Task save(final Task task) {
        return taskRepository.save(task);
    }

    public Optional<Task> deleteById(final Long id) {
        return taskRepository.findById(id);
    }
}
