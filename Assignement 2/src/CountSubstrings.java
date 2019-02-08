import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Count occurrences of substring in texts using various different implementations of the ADT List and timing it.
 * @author Daniel Innes 101067175
 */

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

        HashMap<String, Result> results = averageResults(texts, userInput.getSubstring());

        for (String key : results.keySet()) {
            Result result = results.get(key);
            System.out.println("Using " + key + ": " + result.getCount() + " matches, derived in " + result.getDeltaTime() + " milliseconds.");
        }
    }

    /**
     * Count occurrences of substring in all texts and time it.
     * @param texts HashMap of the texts using various different implementations of the ADT List. Key is the name of implementation and the List of List of Characters.
     * @param substring A List of Characters to find in the List of List of Characters.
     * @return A custom helper class (Result) to pass deltaTime and count for each implementation.
     * @see Result
     */

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

    /**
     * Average over 1000 of Count occurrences of substring in all texts and time it.
     * @param texts HashMap of the texts using various different implementations of the ADT List. Key is the name of implementation and the List of List of Characters.
     * @param substring A List of Characters to find in the List of List of Characters.
     * @return A custom helper class (Result) to pass deltaTime and count for each implementation.
     */
    private static HashMap<String, Result> averageResults(HashMap<String, List<List<Character>>> texts, List<Character> substring) {
        int number = 1000;
        HashMap<String, Result> results;
        HashMap<String, Result> resultsSum = new HashMap<>();
        HashMap<String, Result> resultsAverage = new HashMap<>();
        results = countOccurrences(texts, substring);
        for (String key :texts.keySet()) {
            resultsSum.put(key,results.get(key));
        }

        for (int i = 1; i < number; i++) {
            results = countOccurrences(texts, substring);
            for (String key :texts.keySet()) {
                Result resultSum =  new Result(results.get(key).getDeltaTime() + resultsSum.get(key).getDeltaTime(),results.get(key).getCount() + resultsSum.get(key).getCount());
                resultsSum.put(key,resultSum);
            }
        }
        for (String key :texts.keySet()) {
            Result resultAverage =  new Result(resultsSum.get(key).getDeltaTime()/number,resultsSum.get(key).getCount()/number);
            resultsAverage.put(key,resultAverage);
        }
        return resultsAverage;
    }

    /**
     * Get the fileName and pattern from console input.
     * @return A custom helper class (Result) to pass deltaTime and count for each implementation.
     */
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

    /**
     * Read from file to ArrayLists parse by space.
     * @param file The file to read from.
     * @return The file as ArrayLists.
     * @throws FileNotFoundException If the file dose not exist, this should never happen because file checking is done during input stage.
     */
    private static ArrayList<List<Character>> readToArrayLists(File file) throws FileNotFoundException {
        Scanner scannerFile = new Scanner(file);
        scannerFile.useDelimiter(" ");
        ArrayList<List<Character>> text = new ArrayList<>();
        while (scannerFile.hasNextLine())
            text.add(Arrays.asList(scannerFile.next().chars().mapToObj(c ->(char)c).toArray(Character[]::new)));
        scannerFile.close();
        return text;
    }

    /**
     * Read from file to LinkedList parse by space.
     * @param file The file to read from.
     * @return The file as LinkedList.
     * @throws FileNotFoundException If the file dose not exist, this should never happen because file checking is done during input stage.
     */
    private static LinkedList<List<Character>> readToLinkedList(File file) throws FileNotFoundException {
        Scanner scannerFile = new Scanner(file);
        scannerFile.useDelimiter(" ");
        LinkedList<List<Character>> text = new LinkedList<>();
        while (scannerFile.hasNextLine())
            text.add(Arrays.asList(scannerFile.next().chars().mapToObj(c ->(char)c).toArray(Character[]::new)));
        scannerFile.close();
        return text;
    }

    /**
     * Count occurrences of substring in string using the brute-force substring matching algorithm.
     *
     * Note findBrute is not used because it dose not find multiple occurrences of substring in each string. Therefore will be less efficient for substrings that occur multiple times within a word.
     * @param string The string to search in.
     * @param substring The string to search for.
     * @return The number of substring in string.
     */
    private static int countBrute(List<Character> string, List<Character> substring) {
        int m = substring.size();
        int n = string.size();
        int count = 0;
        for (int i = 0; i <= n - m; i++) {
            int j;
            for (j = 0; j < m; j++) {
                if (string.get(i + j) != substring.get(j))
                    break;
            }
            if (j == m) count++;
        }
        return count;
    }


    /**
     * Helper class containing user input.
     */
    private static class UserInput {
        private final File file;
        private final List<Character> substring;

        /**
         * Initialize file and substring.
         * @param file The file to search.
         * @param substring The substring to search for.
         */
        UserInput(File file, List<Character> substring) {
            this.file = file;
            this.substring = substring;
        }

        /**
         * Get the file.
         * @return The file.
         */
        File getFile() {
            return file;
        }

        /**
         * Get the substring.
         * @return The substring.
         */
        List<Character> getSubstring() {
            return substring;
        }
    }

    /**
     * Helper class containing the result of a search.
     */
    private static class Result {
        private final long deltaTime;
        private final int count;

        /**
         * Initialize deltaTime and count.
         * @param deltaTime The Time need to search the text.
         * @param count The number of substrings in the text.
         */
        Result(long deltaTime, int count) {
            this.deltaTime = deltaTime;
            this.count = count;
        }

        /**
         * Get the deltaTime.
         * @return The deltaTime.
         */
        long getDeltaTime() {
            return deltaTime;
        }

        /**
         * Get the count.
         * @return The count.
         */
        int getCount() {
            return count;
        }
    }
}