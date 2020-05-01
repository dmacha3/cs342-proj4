import java.util.ArrayList;

public class Country extends Category {

    public Country() {
        super();
        this.setWords();
    }

    @Override
    protected void setWords() {
        super.allWords = new ArrayList<>();
        super.allWords.add("cameroon");
        super.allWords.add("finland");
        super.allWords.add("lithuania");
        super.allWords.add("paraguay");
        super.allWords.add("slovakia");
        super.allWords.add("zimbabwe");

    }

}