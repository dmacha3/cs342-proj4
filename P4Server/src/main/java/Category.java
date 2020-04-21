import java.util.ArrayList;
import java.util.Random;

public abstract class Category {

    private String currentWord;
    private ArrayList<String> wordsUsed;
    private boolean categoryGuessed;
    private int numberOfLettersGuessed;
    private int numberOfGuesses;

    protected ArrayList<String> allWords;

    protected abstract void setWords();

    public Category() {
        super();
        this.currentWord = null;
        this.categoryGuessed = false;
        this.numberOfLettersGuessed = 0;
        this.numberOfGuesses = 0;
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
        if (this.currentWord.indexOf(letter) > 0) {
            this.numberOfLettersGuessed++;

            if (this.numberOfLettersGuessed == this.currentWord.length()) {
                this.categoryGuessed = true;
            }

            return true;

        } else {
            this.numberOfGuesses++;

            return false;
        }
    }

    // Function that returns true or false whether
    // Category has been guessed already
    public boolean isCategoryGuessed() {
        return this.categoryGuessed;
    }

}