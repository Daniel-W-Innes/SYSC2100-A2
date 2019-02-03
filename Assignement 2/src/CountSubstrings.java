import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class CountSubstrings {

    public static void main(String[] args) {
        String textString = null;
        ArrayList<String> textArrayList = new ArrayList<>();
        String substring;
        String fileName;
        long time;
        int matches = 0;
        Scanner scanner = new Scanner(System.in);
        while (textString == null) {
            try {
                System.out.print("Please enter the path for the input file: ");
                fileName = scanner.nextLine();
                textString = readToString(fileName);
                textArrayList = readToArrayLists(fileName);
            } catch (IOException e) {
                System.out.println("Incorrect file name");
            }
        }
        System.out.print("Enter the pattern to look for: ");
        substring = scanner.nextLine().toLowerCase();
        time = System.currentTimeMillis();
        System.out.println("Using Strings: " + (textString.split(Pattern.quote(substring), -1).length - 1) + " matches, derived in " + (System.currentTimeMillis() - time) + " milliseconds.");
        time = System.currentTimeMillis();
        for (int i = 0; i < textArrayList.size(); i++)
            matches += (textArrayList.get(i).split(Pattern.quote(substring), -1).length - 1);
        System.out.println("Using Strings: " + matches + " matches, derived in " + (System.currentTimeMillis() - time) + " milliseconds.");
    }

    /*
     * Returns the lowest index at which substring pattern begins in text (or
     * else -1).
     */
    private static int findBrute(List<Character> text, List<Character> pattern) {
        int n = text.size();
        int m = pattern.size();
        for (int i = 0; i <= n - m; i++) { // try every starting index
// within text
            int k = 0; // k is index into pattern
            while (k < m && text.get(i + k) == pattern.get(k))
// kth character of pattern matches
                k++;
            if (k == m) // if we reach the end of the pattern,
                return i; // substring text[i..i+m-1] is a match
        }
        return -1; // search failed
    }

    private static ArrayList<String> readToArrayLists(String fileName) throws IOException {
        ArrayList<String> text = new ArrayList<>();
        File file = new File(fileName);
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String line;
        while ((line = br.readLine()) != null) {
            text.add(line.toLowerCase());
        }
        br.close();
        fr.close();
        return text;
    }

    private static String readToString(String fileName) throws IOException {
        String text = "";
        File file = new File(fileName);
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String line;
        while ((line = br.readLine()) != null) {
            text = text.concat(line);
        }
        br.close();
        fr.close();
        return text.toLowerCase();
    }
}
