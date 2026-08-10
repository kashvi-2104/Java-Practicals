import java.util.regex.*;
public class Templatefiller {
public static String fill(String template, String[] names, String[] values) {
        Pattern pattern = Pattern.compile("\\{(\\w+)\\}");
        Matcher matcher = pattern.matcher(template);
        StringBuilder result = new StringBuilder();
        int last = 0;
        while (matcher.find()) {
            result.append(template, last, matcher.start());
            String placeholder = matcher.group(1);
            String replacement = "[?]";
            for (int i = 0; i < names.length; i++) {
                if (names[i].equals(placeholder)) {
                    replacement = values[i];
                    break;
                }
            }
            result.append(replacement);
            last = matcher.end();
        }
        result.append(template, last, template.length());
        return result.toString();
    }
}
