import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class WordFrequencyCounter {
    public static Map<String, Integer> countWords(String filePath) {
        Map<String, Integer> wordCounts = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] words = line
                        .toLowerCase()
                        .replaceAll("[^a-z0-9\\s]", " ")
                        .trim()
                        .split("\\s+");
                for (String word : words) {
                    if (word.isEmpty()) {
                        continue;
                    }
                    wordCounts.put(word, wordCounts.getOrDefault(word, 0) + 1);
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + filePath);
            return new HashMap<>();
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            return new HashMap<>();
        }
        return wordCounts;
    }
}
