
import java.util.*;

public class WordFrequency {

    public static void main(String[] args) {

        String paragraph = "java is easy and java is powerful and java is popular";

        HashMap<String, Integer> map = new HashMap<>();

        // Split paragraph into words
        String[] words = paragraph.toLowerCase().split("\\s+");

        // Count frequency
        for (String word : words) {
            map.put(word, map.getOrDefault(word, 0) + 1);
        }

        // Sort by frequency (highest first)
        List<Map.Entry<String, Integer>> list =
                new ArrayList<>(map.entrySet());

        list.sort((a, b) ->
                b.getValue().compareTo(a.getValue()));

        // Print result
        System.out.println("Words sorted by frequency:");

        for (Map.Entry<String, Integer> entry : list) {
            System.out.println(
                    entry.getKey() + " : " + entry.getValue());
        }
    }
}
