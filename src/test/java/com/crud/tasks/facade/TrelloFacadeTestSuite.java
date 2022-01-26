package com.crud.tasks.facade;

import com.crud.tasks.domain.*;
import com.crud.tasks.mapper.TrelloMapper;
import com.crud.tasks.validator.TrelloValidator;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class TrelloFacadeTestSuite {

    TrelloMapper mapper = new TrelloMapper();
    TrelloValidator validator = new TrelloValidator();


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

    @Test
    void testMapToDtoBothListAndBoard() {
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
    void testMapToListAndBoard() {
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
}
