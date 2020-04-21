import java.util.ArrayList;

public class Superhero extends Category {

    @Override
    protected void setWords() {
        super.allWords = new ArrayList<>();
        super.allWords.add("spider-man");
        super.allWords.add("iron man");
        super.allWords.add("batman");
        super.allWords.add("superman");
        super.allWords.add("wonder woman");
        super.allWords.add("wolverine");

    }

}