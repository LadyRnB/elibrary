package io.booklib.elibrary.books.controller;

import io.booklib.elibrary.books.service.BookDTO;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class BookDtoMapperTest {

    @Nested
    class MapRequestToDtoTest {
        @Test
        public void testMapRequestToDtoWithValidRequest() {
            // arrange
            CreationBookRequest creationBookRequest = new CreationBookRequest("Titanic", "Rahma", "action", "isbn");

            // act
            BookDTO result = BookDtoMapper.mapRequestToDto(creationBookRequest);

            // assert
            assertNotNull(result);
            assertEquals("Titanic", result.title());
            assertEquals("Rahma", result.author());
            assertEquals("action", result.genre());
            assertEquals("isbn", result.isbn());
            assertNull(result.id());
        }

        @Test
        public void testMapRequestToDtoWithEmptyRequest(){
            //Arrange
            CreationBookRequest creationBookRequest = new CreationBookRequest(null, null, null, null);

            //Act
            BookDTO result = BookDtoMapper.mapRequestToDto(creationBookRequest);
            
            //Assert
            assertNotNull(result);
            assertNull(result.title());
            assertNull(result.author());
            assertNull(result.genre());
            assertNull(result.isbn());
            assertNull(result.id());
        }

        @Test
        public void testMapRequestToDtoWithNullRequest() {
            // arrange
            CreationBookRequest creationBookRequest = null;

            // act
            BookDTO result = BookDtoMapper.mapRequestToDto(creationBookRequest);

            // assert
            assertNotNull(result);
            assertNull(result.title());
            assertNull(result.author());
            assertNull(result.genre());
            assertNull(result.isbn());
            assertNull(result.id());
        }
    }

    @Nested
    class MapDtoToResponseTest {

        @Test
        public void testMapDtoToResponseWithValidDto() {
            //Arrange
            UUID bookId = UUID.randomUUID();
            BookDTO bookDTO = new BookDTO(bookId, "Red Flower", "Hans", "Romance", "isbn01");

            //Act
            BookResponse result = BookDtoMapper.mapDtoToResponse(bookDTO);

            //Assert
            assertNotNull(result);
            assertEquals(bookId, result.bookId()); // Or: assertEquals(bookDTO.id(), result.bookId());
            assertEquals("Red Flower", result.title());
            assertEquals("Hans", result.author());
            assertEquals("Romance", result.genre());
            assertEquals("isbn01", result.isbn());
        }

        @Test
        public void testMapDtoToResponseTestWithEmptyDto(){

            //Arrange
            BookDTO bookDTO = new BookDTO(null, null, null, null, null);

           //Act
            BookResponse result = BookDtoMapper.mapDtoToResponse(bookDTO);

            //Assert
            assertNotNull(result);
            assertNull(result.bookId());
            assertNull(result.title());
            assertNull(result.author());
            assertNull(result.genre());
            assertNull(result.isbn());
        }

        @Test
        public void testMapDtoToResponseTestWithNullDto(){
            //Arrange
            BookDTO bookDTO = null;

            //Act
            BookResponse result = BookDtoMapper.mapDtoToResponse(bookDTO);

            //Assert
            assertNotNull(result);
            assertNull(result.bookId());
            assertNull(result.title());
            assertNull(result.author());
            assertNull(result.genre());
            assertNull(result.isbn());


        }
    }

    @Nested
    class mapRequestAndIdToDTOTest {

        @Test
        public void testMapRequestAndIdToDtoWithValidRequest(){
            //Arrange
            UUID bookId = UUID.randomUUID();
            CreationBookRequest creationBookRequest = new CreationBookRequest("Strange Things", "Stephen Cringe", "Thriller", "isbn02");
            //Act
            BookDTO result = BookDtoMapper.mapRequestAndIdToDTO(creationBookRequest, bookId);

            //Assert
            assertNotNull(result);
            assertEquals("Strange Things", result.title());
            assertEquals("Stephen Cringe", result.author());
            assertEquals("Thriller", result.genre());
            assertEquals("isbn02", result.isbn());
            assertEquals(bookId, result.id());
        }

        @Test
        public void testMapRequestAndIdToDtoWithEmptyRequest(){
            //Arrange
            UUID bookId = UUID.randomUUID();
            CreationBookRequest creationBookRequest = new CreationBookRequest(null,null,null,null);

            //Act
            BookDTO result = BookDtoMapper.mapRequestAndIdToDTO(creationBookRequest, bookId);

            //Assert
            assertNotNull(result);
            assertNull(result.title());
            assertNull(result.author());
            assertNull(result.genre());
            assertNull(result.isbn());
            assertEquals(bookId, result.id());
        }
    }

}