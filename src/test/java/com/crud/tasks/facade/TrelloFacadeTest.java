package com.crud.tasks.facade;

import com.crud.tasks.domain.*;
import com.crud.tasks.mapper.TrelloMapper;
import com.crud.tasks.service.TrelloService;
import com.crud.tasks.trello.facade.TrelloFacade;
import com.crud.tasks.validator.TrelloValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TrelloFacadeTest {

    @InjectMocks
    private TrelloFacade trelloFacade;

    @Mock
    private TrelloService trelloService;

    @Mock
    private TrelloValidator trelloValidator;

    @Mock
    private TrelloMapper trelloMapper;

    @Test
    void shouldFetchEmptyList() {
        //given
        List<TrelloListDto> trelloLists =
                List.of(new TrelloListDto("1", "test_list", false));

        List<TrelloBoardDto> trelloBoards =
                List.of(new TrelloBoardDto("1", "test", trelloLists));

        List<TrelloList> mappedTrelloLists =
                List.of(new TrelloList("1", "test_list", false));

        List<TrelloBoard> mappedTrelloBoards =
                List.of(new TrelloBoard("1", "test", mappedTrelloLists));

        when(trelloService.fetchTrelloBoards()).thenReturn(trelloBoards);
        when(trelloMapper.mapToBoards(trelloBoards)).thenReturn(mappedTrelloBoards);
        when(trelloMapper.mapToBoardDto(anyList())).thenReturn(trelloBoards);
        when(trelloValidator.validateTrelloBoards(mappedTrelloBoards)).thenReturn(mappedTrelloBoards);
        //when
        List<TrelloBoardDto> trelloBoardDtos = trelloFacade.fetchAllTrelloBoards();
        //then
        assertThat(trelloBoardDtos).isNotNull();
        assertThat(trelloBoardDtos.size()).isEqualTo(1);

        trelloBoardDtos.forEach(trelloBoardDto -> {
            assertThat(trelloBoardDto.getId()).isEqualTo("test");
            assertThat(trelloBoardDto.getName()).isEqualTo("1");

            trelloBoardDto.getLists().forEach(trelloListDto -> {
                assertThat(trelloListDto.getId()).isEqualTo("1");
                assertThat(trelloListDto.getName()).isEqualTo("test_list");
                assertThat(trelloListDto.isClosed()).isFalse();
            });
        });
    }

    @Test
    void shouldBuildCardWithService() {
        //given
        TrelloCard card =
                new TrelloCard("Card for test purpose", "Nothing clever to say", "2", "5");
        TrelloCardDto mappedCard =
                new TrelloCardDto("Card for test purpose", "Nothing clever to say", "2", "5");
        CreatedTrelloCardDto createdDto
                = new CreatedTrelloCardDto("5", "Card for test purpose", "http://test.com");

        when(trelloService.createTrelloCard(mappedCard)).thenReturn(createdDto);
        when(trelloMapper.mapToCardDto(card)).thenReturn(mappedCard);
        when(trelloMapper.mapToCard(mappedCard)).thenReturn(card);
        //when
        CreatedTrelloCardDto cardToBeExpected = trelloFacade.createCard(mappedCard);
        //then
        assertThat(cardToBeExpected).isEqualTo(createdDto);
        assertThat(cardToBeExpected).isNotNull();
    }

    @Test
    void shouldReturnExpectedObjectFromListOfCreatedCardsDtos() {
        //given
        TrelloCard card =
                new TrelloCard("Card for test purpose", "Nothing clever to say", "2", "2");
        TrelloCardDto mappedCard =
                new TrelloCardDto("Card for test purpose", "Nothing clever to say", "2", "2");

        List<CreatedTrelloCardDto> listOfCreated = crateList();

        CreatedTrelloCardDto fromList = listOfCreated.stream()
                .filter(cd -> cd.getShortUrl().contains("2"))
                .collect(Collectors.toList()).get(0);

        when(trelloService.createTrelloCard(mappedCard)).thenReturn(fromList);
        when(trelloMapper.mapToCardDto(card)).thenReturn(mappedCard);
        when(trelloMapper.mapToCard(mappedCard)).thenReturn(card);
        //when
        CreatedTrelloCardDto toExpect = trelloFacade.createCard(mappedCard);
        //then
        assertThat(toExpect).isNotNull();
        assertThat(toExpect).isEqualTo(fromList);

        listOfCreated.forEach(created -> {
            assertThat(toExpect.getId()).isNotEqualTo("5");
            assertThat(toExpect.getShortUrl().contains(toExpect.getId())).isTrue();
            assertThat(toExpect.getName().contains("testing this assertion")).isFalse();
        });
    }

    private List<CreatedTrelloCardDto> crateList() {
        List<CreatedTrelloCardDto> createdTrelloCardDtoList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            createdTrelloCardDtoList.add(
                    new CreatedTrelloCardDto(
                            String.valueOf(i),
                            "Card for test purpose",
                            "http://testNo"+i+".com"));
        }
        return createdTrelloCardDtoList;
    }

}
