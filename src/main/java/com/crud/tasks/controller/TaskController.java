package com.crud.tasks.controller;

import com.crud.tasks.domain.Task;
import com.crud.tasks.domain.TaskDto;
import com.crud.tasks.mapper.TaskMapper;
import com.crud.tasks.service.DbService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
@CrossOrigin("*")
public class TaskController {

    private final DbService dbService;
    private final TaskMapper mapper;


    @RequestMapping(method = RequestMethod.GET, value = "/tasks")
    public List<TaskDto> getTasks() {
        List<Task> tasks = dbService.getAllTasks();
        return mapper.mapToTaskDtoList(tasks);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/tasks/{taskId}")
    public TaskDto getTask(@RequestParam("id") Long taskId) throws TaskNotFoundException {
        return mapper.mapToTaskDto(
                dbService.getTaskById(taskId).orElseThrow(TaskNotFoundException::new)
        );
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/tasks/{taskId}")
    public void deleteTask(@RequestParam("id") Long taskId) throws TaskNotFoundException {
        dbService.deleteById(taskId);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/tasks")
    public TaskDto updateTask(@RequestBody TaskDto taskDto) {
        Task task = mapper.mapToTask(taskDto);
        Task savedTask = dbService.save(task);
        return mapper.mapToTaskDto(savedTask);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/tasks", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void createTask(@RequestBody TaskDto taskDto) {
        Task task = mapper.mapToTask(taskDto);
        dbService.save(task);
    }

}
