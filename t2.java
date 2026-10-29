
import java.util.Scanner;

public class t2 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        
        String original = sc.nextLine();
        String keyword = sc.nextLine();

        
        String sentence = original.trim();

        
        String[] words = sentence.split(" +");

        
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < words.length; i++) {
            if (i > 0) {
                sb.append(" ");
            }
            sb.append(words[i].toLowerCase());
        }

        sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));

        
        String shortestWord = words[0].toLowerCase();
        int keywordCount = 0;

        for (String word : words) {
            String lowerWord = word.toLowerCase();

            if (lowerWord.length() < shortestWord.length()) {
                shortestWord = lowerWord;
            }

            if (lowerWord.equalsIgnoreCase(keyword)) {
                keywordCount++;
            }
        }

        
        int vowelCount = 0;

        for (int i = 0; i < original.length(); i++) {
            char ch = original.charAt(i);

            if ("aeiouAEIOU".indexOf(ch) != -1) {
                vowelCount++;
            }
        }

        
        System.out.println("Sentence: " + sb.toString());
        System.out.println("Words: " + words.length);
        System.out.println("Shortest word: " + shortestWord);
        System.out.println("Keyword count: " + keywordCount);
        System.out.println("Vowels: " + vowelCount);

        sc.close();
    }
}
