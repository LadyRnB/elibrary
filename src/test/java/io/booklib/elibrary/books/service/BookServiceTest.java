package io.booklib.elibrary.books.service;

import io.booklib.elibrary.books.repository.BookEntity;
import io.booklib.elibrary.books.repository.BookRepository;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.bean.override.mockito.MockitoBean;


import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;
//    Or simply:
//    BookService bookService = new BookService(bookRepository);

    @Nested
    class CreateBookTest {
        @Test
        public void testCreateBookWithValidDto(){
            //Arrange
            UUID bookId = UUID.randomUUID();
            BookDTO bookDTO = new BookDTO(null, "Titanic", "Rahma", "Action","isbn");
            when(bookRepository.save(any(BookEntity.class))).thenReturn(new BookEntity(bookId, "Titanic", "Rahma", "Action", "isbn"));

            //Act
            BookDTO result = bookService.createBook(bookDTO);

            //Assert
            assertNotNull(result);
            assertEquals("Titanic", result.title());
            assertEquals("Rahma", result.author());
            assertEquals("Action", result.genre());
            assertEquals("isbn", result.isbn());
            assertEquals(bookId, result.id());
            verify(bookRepository).save(any(BookEntity.class));
        }

        @Test
        public void testCreateBookWithEmptyDto(){
            //Arrange
            UUID bookId = UUID.randomUUID();
            BookDTO bookDTO = new BookDTO(bookId, null, null, null,null);
            when(bookRepository.save(any(BookEntity.class))).thenReturn(new BookEntity(bookId,null,null,null,null));

            //Act
            BookDTO result = bookService.createBook(bookDTO);

            //Assert
            assertNotNull(result);
            assertNull(result.title());
            assertNull(result.author());
            assertNull(result.genre());
            assertNull(result.isbn());
            assertEquals(bookId, result.id());
            verify(bookRepository).save(any(BookEntity.class));
        }
        @Test
        public void testCreateBookWithNullDto(){
            //Arrange
            BookDTO bookDTO = null;

            //Act
            BookDTO result = bookService.createBook(bookDTO);

            //Assert
            assertNull(result);
            assertEquals(bookDTO, result);
            verify(bookRepository, never()).save(any(BookEntity.class));
        }
    }

}
