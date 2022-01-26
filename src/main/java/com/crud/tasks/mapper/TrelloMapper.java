package com.crud.tasks.mapper;

import com.crud.tasks.domain.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TrelloMapper {

    public TrelloBoard mapToTrelloBoard(final TrelloBoardDto trelloBoardDto) {
        return new TrelloBoard(
                trelloBoardDto.getName(),
                trelloBoardDto.getId(),
                mapToList(trelloBoardDto.getLists()));
    }

    public List<TrelloList> mapToList(List<TrelloListDto> lists) {
        return lists.stream()
                .map(this::mapToTrelloList)
                .collect(Collectors.toList());
    }

    public List<TrelloBoard> mapToBoards(final List<TrelloBoardDto> trelloBoardDto) {
        return trelloBoardDto.stream()
                .map(this::mapToTrelloBoard)
                .collect(Collectors.toList());
    }

    private TrelloList mapToTrelloList(final TrelloListDto trelloListDto) {
        return new TrelloList(
                trelloListDto.getName(),
                trelloListDto.getId(),
                trelloListDto.isClosed()
        );
    }

    public List<TrelloBoardDto> mapToBoardDto(final List<TrelloBoard> boards) {
        return boards.stream()
                .map(this::mapToTrelloBoardDto)
                .collect(Collectors.toList());
    }

    public TrelloListDto mapToTrelloListDto(final TrelloList trelloList) {
        return new TrelloListDto(
                trelloList.getName(),
                trelloList.getId(),
                trelloList.isClosed()
        );
    }

    private List<TrelloListDto> mapToTrelloListDto(final List<TrelloList> trelloList) {
        return trelloList.stream()
                .map(this::mapToTrelloListDto)
                .collect(Collectors.toList());
    }

    public TrelloBoardDto mapToTrelloBoardDto(final TrelloBoard board) {
        return new TrelloBoardDto(
                board.getName(),
                board.getId(),
                mapToTrelloListDto(board.getLists())
        );
    }

    public TrelloCardDto mapToCardDto(final TrelloCard trelloCard) {
        return new TrelloCardDto(
                trelloCard.getName(),
                trelloCard.getDescription(),
                trelloCard.getPos(),
                trelloCard.getListId());
    }

    public TrelloCard mapToCard(final TrelloCardDto trelloCardDto) {
        return new TrelloCard(
                trelloCardDto.getName(),
                trelloCardDto.getDescription(),
                trelloCardDto.getPos(),
                trelloCardDto.getListId());
    }
}
