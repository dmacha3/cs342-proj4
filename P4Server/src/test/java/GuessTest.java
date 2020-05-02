import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.DisplayName;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

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
