package com.crud.tasks.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class TrelloBoard {

    private String name;
    private String id;
    private List<TrelloList> lists;
}
