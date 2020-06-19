import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

public class Deck {
    private Map<String, String> cards;
    private Logger logger;
    private Map<String, Integer> mistakes;

    public Deck() {
        this.cards = new LinkedHashMap<>();
        this.logger = new Logger();
        this.mistakes = new HashMap<>();
    }

    public Logger getLogger() {
        return logger;
    }

    public void addCard() {
        logger.print("The card:");
        String term = logger.readLine();
        if (cards.containsKey(term)) {
            logger.print(String.format("The card \"%s\" already exists.", term));
            return;
        }
        logger.print("The definition of the card:");
        String definition = logger.readLine();
        if (cards.containsValue(definition)) {
            logger.print(String.format("The definition \"%s\" already exists.", definition));
            return;
        }
        cards.put(term, definition);
        logger.print(String.format("The pair (\"%s\":\"%s\") has been added.", term, definition));
    }

    public void importCards() {
        logger.print("File name:");
        File file = new File(logger.readLine());
        int cardsNo = 0;
        try (Scanner sc = new Scanner(file)) {
            while (sc.hasNext()) {
                String[] split = logger.readLine().split(";");
                if (cards.containsKey(split[0])) {
                    cards.replace(split[0], split[1]);
                } else {
                    cards.put(split[0], split[1]);
                }
                cardsNo++;
            }
            logger.print(String.format("%d cards have been loaded.\n", cardsNo));
        } catch (FileNotFoundException e) {
            logger.print("File not found.");
        }
    }

    public void importCards(String fileName) {
        File file = new File(fileName);
        int cardsNo = 0;
        try (Scanner sc = new Scanner(file)) {
            while (sc.hasNext()) {
                String input = sc.nextLine();
                if (input.isEmpty()) {
                    continue;
                }
                String[] split = input.split(";");
                if (cards.containsKey(split[0])) {
                    cards.replace(split[0], split[1]);
                } else {
                    cards.put(split[0], split[1]);
                }

                if (Integer.parseInt(split[2]) != 0) {
                    mistakes.put(split[0], Integer.parseInt(split[2]));
                }
                cardsNo++;
            }
            logger.print(String.format("%d cards have been loaded.", cardsNo));
        } catch (FileNotFoundException e) {
            logger.print("File not found.");
        }
    }

    public void exportCards() {
        logger.print("File name:");
        File file = new File(logger.readLine());
        try (PrintWriter writer = new PrintWriter(file)) {
            for (var entry : cards.entrySet()) {
                writer.printf("%s;%s;%d\n", entry.getKey(), entry.getValue(), mistakes.getOrDefault(entry.getKey(), 0));
            }
            logger.print(String.format("%d cards have been saved.", cards.size()));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void exportCards(String fileName) {
        File file = new File(fileName);
        try (PrintWriter writer = new PrintWriter(file)) {
            for (var entry : cards.entrySet()) {
                writer.printf("%s;%s;%d\n", entry.getKey(), entry.getValue(), mistakes.getOrDefault(entry.getKey(), 0));
            }
            logger.print(String.format("%d cards have been saved.", cards.size()));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void exit(Map<String, String> options) {
        logger.print("Bye bye");
        if (options.containsKey("-export")) {
            exportCards(options.get("-export"));
        }
    }

    public void removeCard() {
        logger.print("The card:");
        String term = logger.readLine();
        if (cards.containsKey(term)) {
            cards.remove(term);
            mistakes.remove(term);
            logger.print("The card has been removed.");
        } else {
            logger.print(String.format("Can't remove \"%s\": there is no such card.", term));
        }
    }

    public void resetStats() {
        mistakes.clear();
        logger.print("Card statistics has been reset.");
    }

    public void printHardestCards() {
        if (mistakes.isEmpty()) {
            logger.print("There are no cards with errors.");
        } else {
            int mistakesNo = 0;
            List<String> cards = new ArrayList<>();
            for (var entry : mistakes.entrySet()) {
                if (entry.getValue() > mistakesNo) {
                    cards.clear();
                    mistakesNo = entry.getValue();
                    cards.add(entry.getKey());
                } else if (entry.getValue() == mistakesNo) {
                    cards.add(entry.getKey());
                }
            }
            if (cards.size() == 1) {
                logger.print(String.format("The hardest card is \"%s\". You have %d errors answering them.", cards.get(0), mistakesNo));
            } else {
                StringBuilder sb = new StringBuilder("The hardest cards are ");
                for (String card : cards) {
                    sb.append(String.format("\"%s\", ", card));
                }
                sb.replace(sb.length() - 2, sb.length() - 1, ".");
                sb.append(String.format("You have %d errors answering them.", mistakesNo));
                logger.print(sb.toString());
            }
        }
    }

    public void saveLogs() {
        logger.print("File name:");
        File file = new File(logger.readLine());
        try (PrintWriter writer = new PrintWriter(file)) {
            logger.getLogs().forEach(writer::println);
            logger.print("The log has been saved.");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void ask() {
        Random random = new Random();
        logger.print("How many times to ask?");
        int numberOfAsks = logger.nextInt();

        int counter = 0;
        Set<String> keys = cards.keySet();
        String[] keyArray = new String[keys.size()];
        keys.toArray(keyArray);
        while (counter < numberOfAsks) {
            String key = keyArray[random.nextInt(keyArray.length)];
            logger.print(String.format("Print the definition of \"%s\":", key));
            String answer = logger.readLine();
            if (answer.equals(cards.get(key))) {
                logger.print("Correct answer.");
            } else if (cards.containsValue(answer)) {
                logger.print(String.format("Wrong answer. The correct one is \"%s\", you've just written the definition of \"%s\".",
                        cards.get(key), getTerm(answer)));
                mistakes.put(key, mistakes.getOrDefault(key, 0) + 1);
            } else {
                logger.print(String.format("Wrong answer. The correct one is \"%s\".", cards.get(key)));
                mistakes.put(key, mistakes.getOrDefault(key, 0) + 1);
            }
            counter++;
        }
    }

    private String getTerm(String definition) {
        for (var entry : cards.entrySet()) {
            if (entry.getValue().equals(definition)) {
                return entry.getKey();
            }
        }
        return "";
    }

}
