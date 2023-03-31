package pt.ua.games;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import pt.ua.games.domain.Game;
import pt.ua.games.repositories.GameRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class GamesApplicationTests {

	@Container
	public static PostgreSQLContainer container = new PostgreSQLContainer("postgres:latest")
			.withUsername("duke")
			.withPassword("password")
			.withDatabaseName("test");

	@Autowired
	private GameRepository gameRepository;

	@DynamicPropertySource
	static void properties(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url", container::getJdbcUrl);
		registry.add("spring.datasource.password", container::getPassword);
		registry.add("spring.datasource.username", container::getUsername);
	}

	@Test
	void contextLoads() {
		String[] names = {"CS:GO", "Overwatch", "Uncharted 3"};
		int[] years = {2012, 2016, 2011};
		int[] ratings = {10, 6, 10};


		for (int i = 0; i < 3; i++) {
			Game game = new Game();
			game.setTitle(names[i]);
			game.setReleaseYear(years[i]);
			game.setRating(ratings[i]);
			gameRepository.save(game);
		}
	}

	@Test
	@Order(1)
	public void testCreateGame() {
		Game game = new Game();
		game.setTitle("Minecraft");
		game.setReleaseYear(2011);
		game.setRating(9);
		gameRepository.save(game);

		Optional<Game> optionalGame = gameRepository.findById(game.getId());
		assertThat(optionalGame).isPresent().hasValueSatisfying(gameInDb -> {assertThat(gameInDb.getTitle()).isEqualTo(game.getTitle());
			assertThat(gameInDb.getReleaseYear()).isEqualTo(game.getReleaseYear());
			assertThat(gameInDb.getRating()).isEqualTo(game.getRating());
		});
	}

	@Test
	@Order(2)
	public void testUpdateGame() {
		Optional<Game> optionalGame = gameRepository.findByTitle("Minecraft");

		assertThat(optionalGame).isPresent();
		Game game = optionalGame.get();
		game.setRating(7);
		gameRepository.save(game);

		Optional<Game> optionalGameUpdated = gameRepository.findById(game.getId());
		assertThat(optionalGameUpdated).isPresent().hasValueSatisfying(gameInDb -> assertThat(gameInDb.getRating()).isEqualTo(7));
	}

	@Test
	@Order(3)
	public void testDeleteGame() {
		Optional<Game> optionalGame = gameRepository.findByTitle("Minecraft");

		assertThat(optionalGame).isPresent();
		Game game = optionalGame.get();
		gameRepository.delete(game);

		Optional<Game> optionalGameDeleted = gameRepository.findById(game.getId());
		assertThat(optionalGameDeleted).isNotPresent();
	}
}
