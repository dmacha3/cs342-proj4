import java.util.ArrayList;

public class Animal extends Category {

    @Override
    protected void setWords() {
        super.allWords = new ArrayList<>();
        super.allWords.add("elephant");
        super.allWords.add("rabbit");
        super.allWords.add("chicken");
        super.allWords.add("kangaroo");
        super.allWords.add("coyote");
        super.allWords.add("reindeer");

    }

}