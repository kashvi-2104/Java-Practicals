public class Driver1 {
    public static void main(String[] args) {
        String[] passwords = {"abc", "PasssW","Abcd1234!","HELLO!2"};

        for (String pwd : passwords) {

            System.out.println("Password: " + pwd);
            System.out.println("Length >= 8      : " + Passwordchecker.length(pwd));
            System.out.println("Uppercase Letter : " + Passwordchecker.hasupper(pwd));
            System.out.println("Contains Digit   : " + Passwordchecker.hasdigit(pwd));
            System.out.println("Special Character: " + Passwordchecker.hasspecial(pwd));
            System.out.println("Strength: " + Passwordchecker.strength(pwd));
            System.out.println("----------------------------");
        }
    }
}
