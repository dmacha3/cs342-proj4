import java.util.ArrayList;

public class Superhero extends Category {
    
    public Superhero() {
        super();
        this.setWords();
    }

    @Override
    protected void setWords() {
        super.allWords = new ArrayList<>();
        super.allWords.add("spiderman");
        super.allWords.add("ironman");
        super.allWords.add("batman");
        super.allWords.add("superman");
        super.allWords.add("wonderwoman");
        super.allWords.add("wolverine");

    }

}