package io.booklib.elibrary.books.controller;

import io.booklib.elibrary.books.service.BookDTO;
import io.booklib.elibrary.books.service.BookService;
import io.booklib.elibrary.common.configuration.SecurityConfig;
import io.booklib.elibrary.common.exceptions.NotFoundException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
    class CreateBookTest {

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
            when(bookService.createBook(any(BookDTO.class))).thenReturn(new BookDTO(bookId, "Titanic", "Spielberg", "Fantasy", "0123456789012"));
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
                                "isbn": "O123456789012"
                            }
                    """;
            mockMvc.perform(post("/books")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(bookRequestJson)
                            .with(csrf()))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        @WithMockUser
        public void testCreateBookWithInsufficientRole() throws Exception {
            String bookRequestJson = """
                            {
                                "title": "Title",
                                "author": "Spielberg",
                                "genre": "Fantasy",
                                "isbn": "0123456789012"
                            }
                    """;
            when(bookService.createBook(any(BookDTO.class))).thenReturn(new BookDTO(UUID.randomUUID(), "Titanic", "Spielberg", "Fantasy", "O123456789012"));
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
    class GetBookTest {
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

            when(bookService.findBookById(any())).thenReturn(new BookDTO(bookId, "Titanic", "Spielberg", "Fantasy", "O123456789012"));

            mockMvc.perform(get("/books/{bookId}", bookId))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(content().json(expectedBookResponseJson));
        }

        @Test
        @WithMockUser(roles = "READER")
        public void testGetBookByIdWithNotFoundBook() throws Exception {
            UUID bookId = UUID.randomUUID();
            when(bookService.findBookById(any())).thenThrow(new NotFoundException("Book does not exist"));
            mockMvc.perform(get("/books/{bookId}", bookId))
                    .andExpect(status().isNotFound())
                    .andExpect(content().string("Book does not exist"));
        }

        @Test
        //This time @WithMockUser won't be added since: no authentication means no user
        public void testGetBookByIdWithoutAuthentication() throws Exception {
            UUID bookId = UUID.randomUUID();
            mockMvc.perform(get("/books/{bookId}", bookId))
                    .andExpect(status().isUnauthorized());
        }
    }

    @Nested
    class GetAllBooksTest {
        @Test
        @WithMockUser
        public void testGetAllBooksByIdWithValidRequest() throws Exception {
            UUID bookId1 = UUID.randomUUID();
            UUID bookId2 = UUID.randomUUID();
            String expectedBookResponseJson = """
                            [
                                {
                                    "bookId" : "%s",
                                    "title": "Titanic",
                                    "author": "Spielberg",
                                    "genre": "Fantasy",
                                    "isbn": "O123456789012"
                                },
                                {
                                    "bookId" : "%s",
                                    "title": "La Fleur rouge",
                                    "author": "Cameron",
                                    "genre": "Drama",
                                    "isbn": "O123456789013"
                                }
                            ]
                    """.formatted(bookId1, bookId2);

            List<BookDTO> bookDTOList = new ArrayList<>();
            bookDTOList.add(new BookDTO(bookId1, "Titanic", "Spielberg", "Fantasy", "O123456789012"));
            bookDTOList.add(new BookDTO(bookId2, "La Fleur rouge", "Cameron", "Drama", "O123456789013"));

            when(bookService.getAllBooks()).thenReturn(bookDTOList);
            mockMvc.perform(get("/books"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(content().json(expectedBookResponseJson));
        }
    }

    @Nested
    class DeleteBookByIdTest {
        @Test
        public void testDeleteBookByIdWithValidRequest() throws Exception {
            UUID bookId = UUID.randomUUID();
            mockMvc.perform(delete("/books/{bookId}", bookId)
                            .with(csrf())
                            .with(user("user").roles("ADMIN")))
                    .andExpect(status().isOk());

            mockMvc.perform(delete("/books/{bookId}", bookId)
                            .with(csrf())
                            .with(user("user").roles("AUTHOR")))
                    .andExpect(status().isOk());

            mockMvc.perform(delete("/books/{bookId}", bookId)
                            .with(csrf())
                            .with(user("user").roles("READER")))
                    .andExpect(status().isForbidden());
        }

        @Test
        public void testDeleteBookByIdWithoutAuthentication() throws Exception {
            UUID bookId = UUID.randomUUID();
            mockMvc.perform(delete("/books/{bookId}", bookId)
                            .with(csrf()))
                    .andExpect(status().isUnauthorized());
        }
    }

    @Nested
    class UpdateBookTest {
        @Test
        public void testUpdateBookWithValidRequest() throws Exception {
            UUID bookId = UUID.randomUUID();
            String bookRequestJson = """
                                {
                                 "title": "Titanic",
                                 "author": "Spielberg",
                                 "genre": "Fantasy",
                                 "isbn": "O123456789012"
                                }
                    """;
            String expectedBookResponseJson = """
                                {"bookId" : "%s",
                                "title": "Titanic",
                                "author": "Spielberg",
                                "genre": "Fantasy",
                                "isbn": "O123456789012"
                                }
                    """.formatted(bookId);
            when(bookService.updateBook(any(BookDTO.class))).thenReturn(Optional.of(new BookDTO(bookId, "Titanic", "Spielberg", "Fantasy", "O123456789012")));
            mockMvc.perform(put("/books/{bookId}", bookId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(bookRequestJson)
                            .with(csrf())
                            .with(user("user").roles("ADMIN")))
                    .andExpect(status().isOk())
                    .andExpect(content().json(expectedBookResponseJson));

            mockMvc.perform(put("/books/{bookId}", bookId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(bookRequestJson)
                            .with(csrf())
                            .with(user("user").roles("AUTHOR")))
                    .andExpect(status().isOk())
                    .andExpect(content().json(expectedBookResponseJson));

            mockMvc.perform(put("/books/{bookId}", bookId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(bookRequestJson)
                            .with(csrf())
                            .with(user("user").roles("READER")))
                    .andExpect(status().isForbidden());
        }

        @Test
        public void testUpdateBookWithInvalidRequest() throws Exception {
            UUID bookId = UUID.randomUUID();
            when(bookService.updateBook(any(BookDTO.class))).thenReturn(Optional.of(new BookDTO(bookId, "Titanic", "Spielberg", "Fantasy", "O123456789012")));
            String bookRequestJson = """
                                {
                                "title": "Titanic"
                                "author": "Spielberg",
                                "genre": "Fantasy",
                                "isbn" "O123456789012"
                                }
                    """;

            mockMvc.perform(put("/books/{bookId}", bookId)
                            .with(csrf())
                            .with(user("user").roles("ADMIN"))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(bookRequestJson))
                    .andExpect(status().isBadRequest());
        }

        @Test
        public void testUpdateBookWithoutAuthentication() throws Exception {
            UUID bookId = UUID.randomUUID();
            when(bookService.updateBook(any(BookDTO.class))).thenReturn(Optional.of(new BookDTO(bookId, "Titanic", "Spielberg", "Fantasy", "O123456789012")));
            String bookRequestJson = """
                                {
                                "title": "Titanic",
                                "author": "Spielberg",
                                "genre": "Fantasy",
                                "isbn": "O123456789012"
                                }
                    """;

            mockMvc.perform(put("/books/{bookId}", bookId)
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(bookRequestJson))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        public void testUpdateBookWithNotFoundBook() throws Exception {
            UUID bookId = UUID.randomUUID();
            String bookRequestJson = """
                               {
                                "title": "Titanic",
                                "author": "Spielberg",
                                "genre": "Fantasy",
                                "isbn": "O123456789012"
                                }
                    """;
            when(bookService.updateBook(any(BookDTO.class))).thenReturn(Optional.empty());
            mockMvc.perform(put("/books/{bookId}", bookId)
                            .with(csrf())
                            .with(user("user").roles("ADMIN"))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(bookRequestJson))
                    .andExpect(status().isNotFound())
                    .andExpect(status().reason("Book does not exist"));
        }

    }

    @Nested
    class PartiallyUpdateTest {
        @Test
        public void testPartiallyUpdateBookWithValidRequest() throws Exception {
            UUID bookId = UUID.randomUUID();
            String bookRequestJson = """
                    {
                        "author": "Cameron",
                        "genre": "Drama"
                    }
                    """;
            String expectedBookResponseJson = """
                    {"bookId" : "%s",
                    "title": "Titanic",
                    "author": "Cameron",
                    "genre": "Drama",
                    "isbn": "0123456789012"
                    }
                    """.formatted(bookId);
            when(bookService.partiallyUpdate(any(BookDTO.class))).thenReturn(Optional.of(new BookDTO(bookId, "Titanic", "Cameron", "Drama", "0123456789012")));
            mockMvc.perform(patch("/books/{bookId}", bookId)
                            .with(csrf())
                            .with(user("user").roles("ADMIN"))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(bookRequestJson))
                    .andExpect(status().isOk())
                    .andExpect(content().json(expectedBookResponseJson));

            mockMvc.perform(patch("/books/{bookId}", bookId)
                            .with(csrf())
                            .with(user("user").roles("AUTHOR"))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(bookRequestJson))
                    .andExpect(status().isOk())
                    .andExpect(content().json(expectedBookResponseJson));

            mockMvc.perform(patch("/books/{bookId}", bookId)
                            .with(csrf())
                            .with(user("user").roles("READER"))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(bookRequestJson))
                    .andExpect(status().isForbidden());
        }

        @Test
        @WithMockUser
        public void testPartiallyUpdateBookWithInvalidRequest() throws Exception {
            UUID bookId = UUID.randomUUID();
            String  bookRequestJson = """
                            {
                            "author": "Cameron"
                            "genre" "Drama",
                            }
            """;
            when(bookService.partiallyUpdate(any(BookDTO.class))).thenReturn(Optional.of(new BookDTO(bookId, "Titanic", "Cameron", "Drama", "0123456789012")));
            mockMvc.perform(patch("/books/{bookId}", bookId)
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(bookRequestJson))
                    .andExpect(status().isBadRequest());
        }

        @Test
        public void testPartiallyUpdateBookWithoutAuthentication() throws Exception {
            UUID bookId = UUID.randomUUID();
            when(bookService.partiallyUpdate(any(BookDTO.class))).thenReturn(Optional.of(new BookDTO(bookId, "Titanic", "Cameron", "Drama", "0123456789")));
            String bookRequestJson = """
                    {
                        "author": "Cameron",
                        "genre": "Drama"
                    }
                    """;
            mockMvc.perform(patch("/books/{bookId}", bookId)
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(bookRequestJson))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        @WithMockUser (roles = "ADMIN")
        public void testPartiallyUpdateBookWithNotFoundBook() throws Exception {
            UUID bookId = UUID.randomUUID();
            when(bookService.partiallyUpdate(any(BookDTO.class))).thenReturn(Optional.empty());
            String  bookRequestJson = """
                        {
                        "author": "Cameron"
                        }
            """;
            mockMvc.perform(patch("/books/{bookId}", bookId)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(bookRequestJson))
                    .andExpect(status().isNotFound())
                    .andExpect(status().reason("Book does not exist"));
        }
    }
}
