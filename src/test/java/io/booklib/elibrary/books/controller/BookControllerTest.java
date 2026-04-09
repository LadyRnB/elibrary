package io.booklib.elibrary.books.controller;

import io.booklib.elibrary.books.service.BookDTO;
import io.booklib.elibrary.books.service.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.web.servlet.function.RequestPredicates.contentType;

@WebMvcTest(controllers = BookController.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BookService bookService;

    @Test
    @WithMockUser
    public void testCreateBookWithValidRequest() throws Exception {
        //Given
        String bookRequestJson = """
                {
                   "title": "Titanic",
                   "author": "Spielberg",
                   "genre": "Fantasy",
                   "isbn": "0123456789012"
                   }
                """;
        UUID bookId = UUID.randomUUID();

        String expectedBookResponseJson = """
                {  "bookId" : "%s",
                   "title": "Titanic",
                   "author": "Spielberg",
                   "genre": "Fantasy",
                   "isbn": "0123456789012"
                }
                """.formatted(bookId);
        when(bookService.createBook(any(BookDTO.class))).thenReturn(new BookDTO(bookId,"Titanic", "Spielberg", "Fantasy", "0123456789012" ));
        // When + Then (Act + Assert)
        mockMvc.perform(post("/books")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookRequestJson))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedBookResponseJson));
    }

    @Test
    @WithMockUser
    public void testCreateBookWithInvalidRequest() throws Exception {
        String invalidBookRequestJson = """
                {
                    "title": "Titanic",
                    "author": "Spielberg",
                    "genre": "Fantasy",
                    "isbn": "0000"
                }
                """;
        mockMvc.perform(post("/books")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(invalidBookRequestJson))
                .andExpect(status().isBadRequest());

    }

    @Test
    public void testUpdateBookWithoutAuthentication() throws Exception {
        String bookRequestJson = """
                {
                    "title": "Titanic",
                    "author": "Spielberg",
                    "genre": "Fantasy",
                    "isbn": "O123456789012"
        """;
        mockMvc.perform(post("/books")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(bookRequestJson))
                .andExpect(status().isForbidden());
    }

}
