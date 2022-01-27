package com.crud.tasks.facade;

import com.crud.tasks.domain.*;
import com.crud.tasks.mapper.TaskMapper;
import com.crud.tasks.mapper.TrelloMapper;
import com.crud.tasks.validator.TrelloValidator;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class TrelloFacadeTestSuite {

    TrelloMapper mapper = new TrelloMapper();
    TrelloValidator validator = new TrelloValidator();
    TaskMapper taskMapper = new TaskMapper();


    private List<TrelloList> fillList() {
        List<TrelloList> list = new ArrayList<>();
        Random random = new Random();
        boolean b = random.nextBoolean();
        for(int l = 0; l <= 5; l++) {
            list.add(new TrelloList("id"+l, "Name"+l, b));
        }
        return list;
    }

    private List<TrelloListDto> fillListWithDtos() {
        List<TrelloListDto> listDtos = new ArrayList<>();
        Random random = new Random();
        boolean b = random.nextBoolean();
        for(int l = 0; l <= 5; l++) {
            listDtos.add(new TrelloListDto("id"+l, "Name"+l, b));
        }
        return listDtos;
    }

    private List<Task> taskList() {
        List<Task> tasks = new ArrayList<>();
        for(long t = 0l; t <= 4; t++) {
            tasks.add(new Task(t, "title#"+t, "content#"+t));
        }
        return tasks;
    }

    private List<TrelloBoard> fillListWithTrelloBoards() {
        List<TrelloBoard> boards = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            boards.add(new TrelloBoard("name#"+i, "id#"+i, fillList()));
            if(i == 2) {
                boards.add(new TrelloBoard("test", "test id", fillList()));
            }
        }
        return boards;
    }

    @Test
    void testMapToTask() {
        //given
        TaskDto taskDto = new TaskDto(2L, "exampleTitle", "Content 2");
        //when
        Task task = taskMapper.mapToTask(taskDto);
        //then
        assertEquals(2L, task.getId());
    }

    @Test
    void testMapToTaskDto() {
        //given
        Task task = new Task(2L, "exampleTitle", "Content 2");
        //when
        TaskDto taskDto = taskMapper.mapToTaskDto(task);
        //then
        assertEquals(2L, task.getId());
    }

    @Test
    void testMapToTaskDtoList() {
        //given
        List<Task> tasks = taskList();
        //when
        List<TaskDto> taskDtos = taskMapper.mapToTaskDtoList(tasks);
        //then
        assertFalse(taskDtos.isEmpty());
    }

    @Test
    void testMapToCard() {
        //given
        TrelloCardDto cardDto = new TrelloCardDto("Example", "smth", "3", "5");
        //when
        TrelloCard card = mapper.mapToCard(cardDto);
        //then
        assertSame(card.getPos(), cardDto.getPos());
    }

    @Test
    void testMapToCardDto() {
        //given
        TrelloCard card = new TrelloCard("Example", "smthNew", "5", "11");
        //when
        TrelloCardDto trelloCardDto = mapper.mapToCardDto(card);
        //then
        assertAll(
                ()-> assertEquals(trelloCardDto.getName(), card.getName()),
                ()-> assertEquals("11", trelloCardDto.getListId())
        );
    }

    @Test
    void testMapTrelloListToItsDto() {
        //given
        List<TrelloList> trelloLists = fillList();
        //when
        List<TrelloListDto> trelloListDtos = mapper.mapToTrelloListDto(trelloLists);
        //then
        assertAll(
                ()-> assertEquals(trelloLists.get(0).getName(), trelloListDtos.get(0).getName()),
                ()-> assertEquals(trelloLists.size(), trelloListDtos.size()),
                ()-> assertFalse(trelloListDtos.isEmpty())
        );
    }

    @Test
    void testMapListTrelloDtoToTrelloList() {
        //given
        List<TrelloListDto> trelloListDtos = fillListWithDtos();
        //when
        List<TrelloList> trelloLists = mapper.mapToList(trelloListDtos);
        //then
        assertAll(
                ()-> assertFalse(trelloListDtos.isEmpty()),
                ()-> assertEquals(trelloLists.size(), trelloListDtos.size())
        );
    }

    @Test
    void testMapBoardToBoardDto() {
        //given
        List<TrelloList> trelloLists = fillList();
        TrelloBoard board = new TrelloBoard("ToDo", "1432", trelloLists);
        //when
        TrelloBoardDto trelloBoardDto = mapper.mapToTrelloBoardDto(board);
        //then
        assertAll(
                ()-> assertEquals(board.getName(), trelloBoardDto.getName()),
                ()-> assertFalse(trelloBoardDto.getLists().isEmpty()));
    }

    @Test
    void testMapBoardDtoToBoard() {
        //given
        List<TrelloListDto> trelloListsDto = fillListWithDtos();
        TrelloBoardDto boardDto = new TrelloBoardDto("Done", "2254", trelloListsDto);
        //when
        TrelloBoard board = mapper.mapToTrelloBoard(boardDto);
        //then
        assertAll(
                ()-> assertEquals(boardDto.getName(), board.getName()),
                ()-> assertEquals(6, board.getLists().size()));
    }

    @Test
    void testIfValidatorDoesItsJob() {
        //given
        TrelloCard card = new TrelloCard("testForCheck", "smthNew", "5", "11");
        TrelloCard card2 = new TrelloCard("card", "smthSmth", "4", "10");
        //when
        validator.validateCard(card);
        validator.validateCard(card2);
        //then
        assertAll(
                ()-> assertTrue(card.getName().contains("test")),
                ()-> assertNotSame(card2.getListId(), card.getListId())
        );
    }

    @Test
    void testValidateTrelloBoards() {
        //given
        List<TrelloBoard> trelloBoards = fillListWithTrelloBoards();
        //when
        List<TrelloBoard> trelloBoardsToValidate = validator.validateTrelloBoards(trelloBoards);
        List<TrelloBoard> casted =
                trelloBoardsToValidate.stream()
                        .filter(b -> b.getName().equals("test"))
                        .collect(Collectors.toList());
        //then
        assertAll(
                ()-> assertEquals(5, trelloBoardsToValidate.size()),
                ()-> assertEquals(6, trelloBoardsToValidate.get(0).getLists().size()),
                ()-> assertTrue(casted.isEmpty())
        );
    }
}
