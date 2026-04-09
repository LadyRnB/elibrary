package io.booklib.elibrary.books.repository;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.boot.autoconfigure.web.WebProperties;

import java.util.UUID;

@Entity
@Table(name="books")
@Getter
@Setter               // Replaces manual getters/setters
@NoArgsConstructor            // Required by JPA
@AllArgsConstructor
public class BookEntity {

    @Id @GeneratedValue (strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false)
    private String genre;

    @Column(nullable = false, unique = true)
    private String isbn;

}
