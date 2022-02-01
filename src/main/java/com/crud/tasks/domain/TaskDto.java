package com.crud.tasks.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class TaskDto {

    private Long taskId;
    private String title;
    private String content;
}
