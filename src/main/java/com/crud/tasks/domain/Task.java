package com.crud.tasks.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Task {

    private Long id;
    private String title;
    private String content;
}
