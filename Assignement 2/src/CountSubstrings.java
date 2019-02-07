import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class CountSubstrings {
    public static void main(String[] args) {

        HashMap<String, List<String>> texts = new HashMap<>();

        UserInput userInput = getUserInput();

        try {
            texts.put("ArrayLists", readToArrayLists(userInput.getFile()));
            texts.put("LinkedList", readToLinkedList(userInput.getFile()));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        HashMap<String, Result> results = countOccurrences(texts, userInput.getSubstring());

        for (String key : results.keySet()) {
            Result result = results.get(key);
            System.out.println("Using " + key + ": " + result.getCount() + " matches, derived in " + result.getDeltaTime() + " milliseconds.");
        }
    }

    private static HashMap<String, Result> countOccurrences(HashMap<String, List<String>> texts, String substring) {
        long time;
        HashMap<String, Result> Results = new HashMap<>();
        for (String key : texts.keySet()) {
            List<String> text = texts.get(key);
            long deltaTime;
            int count = 0;
            time = System.currentTimeMillis();
            for (String str : text) {
                count += countBrute(str, substring);
            }
            deltaTime = System.currentTimeMillis() - time;
            Results.put(key, new Result(deltaTime, count));
        }
        return Results;
    }

    private static UserInput getUserInput() {
        Scanner scannerConsole = new Scanner(System.in);
        File file;
        String substring;
        do {
            System.out.print("Please enter the path for the input file: ");
            file = new File(scannerConsole.nextLine());
        } while (!(file.exists() && !file.isDirectory()));
        System.out.print("Enter the pattern to look for: ");
        substring = scannerConsole.nextLine();
        scannerConsole.close();
        return new UserInput(file, substring);
    }

    private static ArrayList<String> readToArrayLists(File file) throws FileNotFoundException {
        Scanner scannerFile = new Scanner(file);
        ArrayList<String> text = new ArrayList<>();
        while (scannerFile.hasNextLine())
            text.add(scannerFile.nextLine());
        scannerFile.close();
        return text;
    }

    private static LinkedList<String> readToLinkedList(File file) throws FileNotFoundException {
        Scanner scannerFile = new Scanner(file);
        LinkedList<String> text = new LinkedList<>();
        while (scannerFile.hasNextLine())
            text.add(scannerFile.nextLine());
        scannerFile.close();
        return text;
    }

    private static int countBrute(String text, String substring) {
        int m = substring.length();
        int n = text.length();
        int count = 0;
        for (int i = 0; i <= n - m; i++) {
            int j;
            for (j = 0; j < m; j++) {
                if (text.charAt(i + j) != substring.charAt(j))
                    break;
            }
            if (j == m) count++;
        }
        return count;
    }

    public static class UserInput {
        private File file;
        private String substring;

        UserInput(File file, String substring) {
            this.file = file;
            this.substring = substring;
        }

        File getFile() {
            return file;
        }

        String getSubstring() {
            return substring;
        }
    }

    public static class Result {
        private long deltaTime;
        private int count;

        Result(long deltaTime, int count) {
            this.deltaTime = deltaTime;
            this.count = count;
        }

        long getDeltaTime() {
            return deltaTime;
        }

        int getCount() {
            return count;
        }
    }
}
