import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class GuessTest {
	WordGuessInfo data = new WordGuessInfo();
	Animal animalCategory = new Animal();
	Country countryCategory = new Country();
	Superhero superheroCategory = new Superhero();

	@Test
	void testWordGuessClass() {
		assertEquals("WordGuessInfo", data.getClass().getName(), "Incorrect class");
	}

	@Test
	void testAnimal(){
		assertEquals("Animal", animalCategory.getClass().getName(), "Incorrect class");
	}
	@Test
	void testCountry(){
		assertEquals("Country", countryCategory.getClass().getName(), "Incorrect class");
	}
	@Test
	void testSuperHero(){
		assertEquals("Superhero", superheroCategory.getClass().getName(), "Incorrect class");
	}

}
