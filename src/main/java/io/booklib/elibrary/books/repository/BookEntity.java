package io.booklib.elibrary.books.repository;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name="books")
@Getter
@Setter               // Replaces manual getters/setters
@NoArgsConstructor            // Required by JPA
@AllArgsConstructor
public class BookEntity {

    @Id @GeneratedValue
    private UUID id;
    private String title;
    private String author;
    private String genre;
    private String isbn;

}
