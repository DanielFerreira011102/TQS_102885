package pt.ua.games.domain;

import jakarta.persistence.*;
import lombok.*;
@Getter
@Setter
@Entity
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String title;
    private int releaseYear;
    private int rating;
}
