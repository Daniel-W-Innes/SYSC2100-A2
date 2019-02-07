import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class CountSubstrings {
    public static void main(String[] args) {

        HashMap<String, List<List<Character>>> texts = new HashMap<>();

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

    private static HashMap<String, Result> countOccurrences(HashMap<String, List<List<Character>>> texts, List<Character> substring) {
        long time;
        HashMap<String, Result> Results = new HashMap<>();
        for (String key : texts.keySet()) {
            List<List<Character>> text = texts.get(key);
            long deltaTime;
            int count = 0;
            time = System.currentTimeMillis();
            for (List<Character> str : text) {
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
        List<Character> substring;
        do {
            System.out.print("Please enter the path for the input file: ");
            file = new File(scannerConsole.nextLine());
        } while (!(file.exists() && !file.isDirectory()));
        System.out.print("Enter the pattern to look for: ");
        substring = Arrays.asList(scannerConsole.nextLine().chars().mapToObj(c ->(char)c).toArray(Character[]::new));
        scannerConsole.close();
        return new UserInput(file, substring);
    }

    private static ArrayList<List<Character>> readToArrayLists(File file) throws FileNotFoundException {
        Scanner scannerFile = new Scanner(file);
        scannerFile.useDelimiter(" ");
        ArrayList<List<Character>> text = new ArrayList<>();
        while (scannerFile.hasNextLine())
            text.add(Arrays.asList(scannerFile.next().chars().mapToObj(c ->(char)c).toArray(Character[]::new)));
        scannerFile.close();
        return text;
    }

    private static LinkedList<List<Character>> readToLinkedList(File file) throws FileNotFoundException {
        Scanner scannerFile = new Scanner(file);
        scannerFile.useDelimiter(" ");
        LinkedList<List<Character>> text = new LinkedList<>();
        while (scannerFile.hasNextLine())
            text.add(Arrays.asList(scannerFile.next().chars().mapToObj(c ->(char)c).toArray(Character[]::new)));
        scannerFile.close();
        return text;
    }

    private static int countBrute(List<Character> text, List<Character> substring) {
        int m = substring.size();
        int n = text.size();
        int count = 0;
        for (int i = 0; i <= n - m; i++) {
            int j;
            for (j = 0; j < m; j++) {
                if (text.get(i + j) != substring.get(j))
                    break;
            }
            if (j == m) count++;
        }
        return count;
    }

    public static class UserInput {
        private File file;
        private List<Character> substring;

        UserInput(File file, List<Character> substring) {
            this.file = file;
            this.substring = substring;
        }

        File getFile() {
            return file;
        }

        List<Character> getSubstring() {
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
