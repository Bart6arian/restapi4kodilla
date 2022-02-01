package com.crud.tasks.controller;

import com.crud.tasks.domain.Task;
import com.crud.tasks.domain.TaskDto;
import com.crud.tasks.mapper.TaskMapper;
import com.crud.tasks.service.DbService;
import com.google.gson.Gson;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
@SpringJUnitWebConfig
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DbService dbService;

    @MockBean
    private TaskMapper taskMapper;

    @MockBean
    private TaskController taskController;

    private List<Task> listOfTasks() {
        List<Task> list = new ArrayList<>();
        for (long i = 0L; i < 4; i++) {
            list.add(new Task(i, "test title", "test content"));
        }
        return list;
    }

    private List<TaskDto> listOfTasksDtos() {
        List<TaskDto> dtos = new ArrayList<>();
        for (long i = 0L; i < 4; i++) {
            dtos.add(new TaskDto(i, "test title", "test content"));
        }
        return dtos;
    }

    @Test
    void shouldAddNewTask() throws Exception {
        //given
        TaskDto taskDto = new TaskDto(2L, "test title", "test content");
        Gson gson = new Gson();
        String s = gson.toJson(taskDto);
        //when
        //then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/v1/task/createTask")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(s))
                .andExpect(status().is(200));
    }

    @Test
    void shouldReturnTaskByGivenId() throws Exception {
        //given
        TaskDto taskDto = new TaskDto(2L, "test title", "test content");
        Task task = new Task(2L, "test title", "test content");
        when(taskController.getTask(2L)).thenReturn(taskDto);
        when(dbService.getTaskById(2L)).thenReturn(Optional.of(task));
        Gson gson = new Gson();
        String s = gson.toJson(taskDto);
        //when
        //then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/v1/task/getTask")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("id", "2")
                        .content(s)).andDo(print())
                .andExpect(status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.taskId", Matchers.is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title", Matchers.startsWith("test")));
    }

    @Test
    void shouldReturnAllTasks() throws Exception {
        //given
        List<Task> tasks = listOfTasks();
        List<TaskDto> taskDtos = listOfTasksDtos();
        when(taskController.getTasks()).thenReturn(taskDtos);
        when(taskMapper.mapToTaskDtoList(tasks)).thenReturn(taskDtos);
        when(dbService.getAllTasks()).thenReturn(tasks);
        Gson gson = new Gson();
        String s = gson.toJson(taskDtos);
        //when
        //then
        mockMvc
                .perform(
                        MockMvcRequestBuilders
                            .get("/v1/task/getTasks")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(s)).andDo(print())
                .andExpect(status().is(200))
                .andExpect(jsonPath("$[3].taskId", Matchers.is(3)))
                .andExpect(jsonPath("$[3].title", Matchers.endsWith("title")))
                .andExpect(jsonPath("$[2].content", Matchers.is("test content")));
    }

    @Test
    void shouldDeleteTaskById() throws Exception {
        //given
        TaskDto taskDto = new TaskDto(2L, "test title", "test content for tests");
        Gson gson = new Gson();
        String s = gson.toJson(taskDto);
        //when
        //then
        mockMvc
                .perform(
                    MockMvcRequestBuilders
                            .delete("/v1/task/deleteTask")
                            .param("id", "2")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(s)).andDo(print())
                .andExpect(status().is(200));
    }

    @Test
    void shouldUpdateTask() throws Exception {
        //given
        TaskDto taskDto = new TaskDto(5L, "test title for nothing", "test content for tests");
        TaskDto taskUpdated = new TaskDto(6L, "test title for nothing", "test content update");

        when(taskController.updateTask(taskDto)).thenReturn(taskUpdated);
        Gson gson = new Gson();
        String s = gson.toJson(taskUpdated);
        //when
        //then
        mockMvc
                .perform(
                        MockMvcRequestBuilders
                                .put("/v1/task/updateTask")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(s)).andDo(print())
                .andExpect(status().is(200));
    }
}