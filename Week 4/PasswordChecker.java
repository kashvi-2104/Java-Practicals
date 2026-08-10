public class PasswordChecker{
    public static Boolean length(String pwd){
        return pwd.length()>=8;
    }
    public static Boolean hasupper(String pwd){
        return pwd.matches(".*[A-Z].*");
    }
    public static Boolean hasdigit(String pwd){
        return pwd.matches(".*[0-9].*");
    }
    public static Boolean hasspecial(String pwd){
        return pwd.matches(".*[^a-zA-Z0-9].*");
    }
    public static String strength(String pwd){
        int count=0;
        if(length(pwd))
            count++;
        if(hasupper(pwd))
            count++;
        if(hasdigit(pwd))
            count++;
        if(hasspecial(pwd))
            count++;

        if(count<=1)
            return "Weak";
        else if(count==4)
            return "Strong";
        else
            return "Medium";
    }
}
