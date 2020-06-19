import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Logger {
    private static Scanner sc = new Scanner(System.in);
    private List<String> logs;
    public Logger() {
        this.logs = new LinkedList<>();
    }

    public void print(String string) {
        logs.add(string);
        System.out.println(string);
    }

    public String readLine() {
        String input = sc.nextLine();
        logs.add(input);
        return input;
    }

    public int nextInt() {
        int input = sc.nextInt();
        sc.nextLine();
        logs.add(Integer.toString(input));
        return input;
    }

    public List<String> getLogs() {
        return logs;
    }
}
