import java.lang.annotation.*;
import java.lang.reflect.Method;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@interface Run {
}

class MyTests {

    @Run
    public void testLogin() {
        System.out.println("Login test passed");
    }

    @Run
    public void testSignup() {
        System.out.println("Signup test passed");
    }

    public void normalMethod() {
        System.out.println("This method should not run");
    }

    @Run
    public void testLogout() {
        System.out.println("Logout test passed");
    }
}

public class Main2 {

    public static void main(String[] args) {

        MyTests obj = new MyTests();

        int count = 0;

        Method[] methods = MyTests.class.getDeclaredMethods();

        for (Method method : methods) {

            if (method.isAnnotationPresent(Run.class)) {

                try {
                    // Run the method
                    method.invoke(obj);
                    count++;
                } catch (Exception e) {
                    System.out.println("Error running test");
                }
            }
        }

        System.out.println("Total tests run: " + count);
    }
}

