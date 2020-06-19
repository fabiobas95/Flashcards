import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        Map<String, String> options = new HashMap<>();
        for (int i = 0; i < args.length - 1; i += 2) {
            options.put(args[i], args[i + 1]);
        }

        FlashcardController flashcardController = new FlashcardController(options);
        flashcardController.mainLoop();

    }
}