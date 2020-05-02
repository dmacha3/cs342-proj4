import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public abstract class Category {

    private String currentWord;
    private ArrayList<Integer> indexesToUpdateForCurrentGuess;
    private ArrayList<String> wordsUsed;
    private boolean categoryGuessed;
    private int numberOfLettersGuessed;
    private int numberOfInvalidGuesses;

    protected ArrayList<String> allWords;

    protected abstract void setWords();

    public Category() {
        super();
        this.currentWord = null;
        this.categoryGuessed = false;
        this.numberOfLettersGuessed = 0;
        this.numberOfInvalidGuesses = 0;
        this.indexesToUpdateForCurrentGuess = new ArrayList<Integer>();
        this.wordsUsed = new ArrayList<String>();
    }

    public void setCurrentWord() {

        // If all of the words were used
        // Start from begining
        if (allWords.isEmpty()) {
            this.allWords.addAll(this.wordsUsed);
        }

        // Set currentWord random words from allWords
        Random r = new Random();
        int randomIndex = r.nextInt(allWords.size());
        this.currentWord = this.allWords.get(randomIndex);
        this.wordsUsed.add(this.allWords.get(randomIndex));
        this.allWords.remove(randomIndex);

    }

    public boolean checkLetter(char letter) {

        this.indexesToUpdateForCurrentGuess.clear();

        // First check if there is at least one occurance of letter
        if (this.currentWord.indexOf(letter) > -1) {

            // Now loop over every letter in case there are multiple same letter
            for (int i = 0; i < this.currentWord.length(); i++) {
                if (this.currentWord.charAt(i) == letter) {
                    this.numberOfLettersGuessed++;
                    this.indexesToUpdateForCurrentGuess.add(i);
                }
            }

            if (this.numberOfLettersGuessed == this.currentWord.length()) {
                this.categoryGuessed = true;
            }

            return true;

        } else {
            this.numberOfInvalidGuesses++;

            // If user guessed 6 times
            // set currentWord to null that would indicate that user did not guess word
            if (this.numberOfInvalidGuesses == 7) {
                this.currentWord = null;
            }

            return false;
        }
    }

    // Function that returns true or false whether
    // Category has been guessed already
    public boolean isCategoryGuessed() {
        return this.categoryGuessed;
    }

    // Return current word length
    public int getCurrentWordLenght() {
        if (this.currentWord != null) {
            return this.currentWord.length();
        }
        return -1;
    }

    public String getCurrentWord() {
        return this.currentWord;
    }

    public void resetForNextGame() {
        this.currentWord = null;
        this.categoryGuessed = false;
        this.numberOfLettersGuessed = 0;
        this.numberOfInvalidGuesses = 0;
        this.indexesToUpdateForCurrentGuess.clear();
    }

    public boolean categoryFailed() {
        if (this.wordsUsed.size() == 4) {
            return true;
        }

        return false;
    }

    public void copyIndexesToUpdateForCurrentGuess(ArrayList<Integer> from) {

        for (int i = 0; i < this.indexesToUpdateForCurrentGuess.size(); i++) {
            from.add(this.indexesToUpdateForCurrentGuess.get(i));
        }
    }

    public int getNumberOfInvalidGuesses() {
        return this.numberOfInvalidGuesses;
    }

}