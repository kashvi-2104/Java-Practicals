public class Driver1 {
    public static void main(String[] args) {
        String[] passwords = {"abc", "PasssW","Abcd1234!","HELLO!2"};

        for (String pwd : passwords) {

            System.out.println("Password: " + pwd);
            System.out.println("Length >= 8      : " + PasswordChecker.length(pwd));
            System.out.println("Uppercase Letter : " + PasswordChecker.hasupper(pwd));
            System.out.println("Contains Digit   : " + PasswordChecker.hasdigit(pwd));
            System.out.println("Special Character: " + PasswordChecker.hasspecial(pwd));
            System.out.println("Strength: " + PasswordChecker.strength(pwd));
            System.out.println("-----------------------------");
        }
    }
}
