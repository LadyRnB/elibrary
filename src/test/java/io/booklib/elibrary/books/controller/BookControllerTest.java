package io.booklib.elibrary.books.controller;

import io.booklib.elibrary.books.service.BookDTO;
import io.booklib.elibrary.books.service.BookService;
import io.booklib.elibrary.configuration.SecurityConfig;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookController.class)
@Import(SecurityConfig.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BookService bookService;

    @Nested
    class CreateBookTest{
        @Test
        @WithMockUser(roles = "AUTHOR")
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
        @WithMockUser(roles = "AUTHOR")
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
        public void testCreateBookWithoutAuthentication() throws Exception {
            String bookRequestJson = """
                    {
                        "title": "Titanic",
                        "author": "Spielberg",
                        "genre": "Fantasy",
                        "isbn": "O123456789012"}
            """;
            mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookRequestJson)
                        .with(csrf()))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        @WithMockUser
        public void testCreateBookWithInsufficientRole() throws Exception{
            String bookRequestJson = """
                    {
                        "title": "Title",
                        "author": "Spielberg",
                        "genre": "Fantasy",
                        "isbn": "0123456789012"
                    }
            """;
            when(bookService.createBook(any(BookDTO.class))).thenReturn(new BookDTO(UUID.randomUUID(), "Titanic", "Spielberg", "Fantasy", "O123456789012" ));
            mockMvc.perform(post("/books")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(bookRequestJson)
                    .with(csrf())
                            .with(user("user").roles("READER")))
                    .andExpect(status().isForbidden());

            mockMvc.perform(post("/books")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(bookRequestJson)
                            .with(csrf())
                            .with(user("user").roles("AUTHOR")))
                    .andExpect(status().isOk());

            mockMvc.perform(post("/books")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(bookRequestJson)
                            .with(csrf())
                            .with(user("user").roles("ADMIN")))
                    .andExpect(status().isOk());

        }
    }

    @Nested
    class GetBookTest{
        @Test
        @WithMockUser(roles = "READER")
        public void testGetBookByIdWithValidRequest() throws Exception {
            UUID bookId = UUID.randomUUID();

            String expectedBookResponseJson = """
                    { "bookId" : "%s",
                    "title": "Titanic",
                    "author": "Spielberg",
                    "genre": "Fantasy",
                    "isbn": "O123456789012"}
            """.formatted(bookId);

            when(bookService.findBookById(any())).thenReturn(Optional.of(new BookDTO(bookId, "Titanic", "Spielberg", "Fantasy", "O123456789012")));

            mockMvc.perform(get("/books/{bookId}", bookId))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(content().json(expectedBookResponseJson));
        }

        @Test
        @WithMockUser(roles = "READER")
        public void testGetBookByIdWithNotFoundBook() throws Exception {
            UUID bookId = UUID.randomUUID();
            when(bookService.findBookById(any())).thenReturn(Optional.empty());
            mockMvc.perform(get("/books/{bookId}", bookId))
                    .andExpect(status().isNotFound());
        }

        @Test
        //This time @WithMockUser won't be added since: no authentication means no user
        public void testGetBookByIdWithoutAuthentication() throws Exception {
            UUID bookId = UUID.randomUUID();
            mockMvc.perform(get("/books/{bookId}", bookId))
                    .andExpect(status().isUnauthorized());
        }
}}
