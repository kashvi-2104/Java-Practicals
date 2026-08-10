public class Chatfilter {

    public static String filter(String[] log, String keywd) {

        StringBuilder s = new StringBuilder();
        int c = 0;

        for (String i : log) {

            String[] s1 = i.split(" ", 3);

            if (s1.length < 3)
                continue;

            String time = s1[0];
            String user = s1[1];
            String msg = s1[2];

            if (msg.toLowerCase().contains(keywd.toLowerCase())) {
                c++;
                s.append(time)
                .append(" ")
                .append(user)
                .append(": ")
                .append(msg)
                .append("\n");
            }
        }
        return "Matches: " + c + "\n" + s.toString();
    }
}
