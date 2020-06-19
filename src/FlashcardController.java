import java.util.Map;

public class FlashcardController {
    private Deck deck;
    private Map<String, String> options;

    public FlashcardController(Map<String, String> options) {
        Deck deck = new Deck();
        if (options.containsKey("-import")) {
            deck.importCards(options.get("-import"));
        }
        this.deck = deck;
        this.options = options;
    }

    public void mainLoop() {
        Actions action = null;
        Logger logger = deck.getLogger();
        do {
            System.out.println();
            deck.getLogger().print("Input the action (add, remove, import, export, ask, exit, log, hardest card, reset stats):");
            action = Actions.getAction(logger.readLine().toLowerCase());
            try {
                executeAction(action);
            } catch (NullPointerException e) {
                logger.print("No such action");
            }
        } while (action != Actions.EXIT);
    }


    private void executeAction(Actions action) {
        switch (action) {
            case ADD:
                deck.addCard();
                break;

            case REMOVE:
                deck.removeCard();
                break;

            case IMPORT:
                deck.importCards();
                break;

            case EXPORT:
                deck.exportCards();
                break;

            case ASK:
                deck.ask();
                break;

            case EXIT:
                exit();
                break;

            case LOG:
                deck.saveLogs();
                break;
            case HARDEST_CARD:
                deck.printHardestCards();
                break;
            case RESET:
                deck.resetStats();
                break;
            default:
                deck.getLogger().print("No such action");
        }
    }

    private void exit() {
        deck.getLogger().print("Bye bye");
        if (options.containsKey("-export")) {
            deck.exportCards(options.get("-export"));
        }
    }

    private enum Actions {
        ADD("add"),
        REMOVE("remove"),
        IMPORT("import"),
        EXPORT("export"),
        ASK("ask"),
        EXIT("exit"),
        LOG("log"),
        HARDEST_CARD("hardest card"),
        RESET("reset stats");


        private String description;

        Actions(String description) {
            this.description = description;
        }

        public static Actions getAction(String action) {
            for (Actions value : Actions.values()) {
                if (value.description.equals(action)) {
                    return value;
                }
            }
            return null;
        }
    }
}

