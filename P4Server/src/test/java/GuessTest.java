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

	@Test
	void testInfoConnected(){
		assertFalse(data.connected, "Failed");
	}

	@Test
	void testInfoLetter(){
		assertEquals('0', data.guessedLetter, "Failed");
	}

	@Test
	void testInfoInvalid(){
		assertEquals(0, data.numberOfInvalidGuesses, "Failed");
	}

	@Test
	void testInfoIndexes(){
		assertNull(data.indexesToUpdate, "Failed");
	}
	@Test
	void testInfoCurrentWord(){
		assertEquals(0, data.currentWordLength, "Failed");
	}
	@Test
	void testInfoCurrentCat(){
		assertNull(data.currentCategory, "Failed");
	}
	@Test
	void testInfoCatOnePass(){
		assertFalse(data.animalCategoryPassed, "Failed");
	}
	@Test
	void testInfoCatTwoPass(){
		assertFalse(data.countryCategoryPassed, "Failed");
	}
	@Test
	void testInfoCatThreePass(){
		assertFalse(data.superheroCategoryPassed, "Failed");
	}

	@Test
	void testInfoGameOver(){
		assertFalse(data.gameOver, "Failed");
	}

	@Test
	void testInfoGameWon(){
		assertFalse(data.gameWon, "Failed");
	}


}
