import java.io.Serializable;
import java.util.ArrayList;

public class WordGuessInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    public int processCode;

    public boolean connected;

    // For current word guess
    public char guessedLetter;

    public ArrayList<Integer> indexesToUpdate;

    public int currentWordLength;

    public boolean wasLetterGuessed;

    public int numberOfInvalidGuesses;

    public String currentCategory;

    // These will indicate which category has been passed
    public boolean animalCategoryPassed;

    public boolean countryCategoryPassed;

    public boolean superheroCategoryPassed;

    public boolean gameOver;

    public boolean gameWon;

    public WordGuessInfo() {
        super();
        this.connected = false;
        this.guessedLetter = '0';
        this.numberOfInvalidGuesses = 0;
        this.indexesToUpdate = null;
        this.currentWordLength = 0;
        this.currentCategory = null;
        this.animalCategoryPassed = false;
        this.countryCategoryPassed = false;
        this.superheroCategoryPassed = false;
        this.gameOver = false;
        this.gameWon = false;
    }

}