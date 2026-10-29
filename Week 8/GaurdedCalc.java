import java.util.Scanner;

public class GaurdedCalc {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        boolean finish = false;

        while (!finish) {

            try {
                System.out.print("Enter the first number : ");
                String num1 = sc.nextLine();

                System.out.print("Enter the second number : ");
                String num2 = sc.nextLine();

                System.out.print("Enter the operator (+,-,*,/) : ");
                String op = sc.nextLine();

                int n1 = Integer.parseInt(num1);
                int n2 = Integer.parseInt(num2);

                validate(n1, n2, op);

                int result = calculate(n1, n2, op);

                System.out.println("Result : " + result);

            } catch (NumberFormatException e) {
                System.out.println("Error : Enter valid integers.");
            } catch (NegativeNumberException | InvalidOperatorException | ZeroMultiplicationException
                    | DivideByZeroException e) {
                System.out.println("Error : " + e.getMessage());
            } catch (ArithmeticException e) {
                System.out.println("Error : Integer overflow.");
            } finally {
                System.out.println("Log : Attempt completed.");
            }

            System.out.print("\nDo you want to continue? (y/n): ");
            String choice = sc.nextLine();

            if (choice.equalsIgnoreCase("n")) {
                finish = true;
            }
        }

        sc.close();
        System.out.println("Calculator exited.");
    }

    static void validate(int n1, int n2, String op)
            throws NegativeNumberException,
            InvalidOperatorException,
            ZeroMultiplicationException,
            DivideByZeroException {

        if (n1 < 0 || n2 < 0)
            throw new NegativeNumberException("Negative numbers not allowed.");

        if (!op.matches("[+\\-*/]"))
            throw new InvalidOperatorException("Invalid operator.");

        if (op.equals("*") && (n1 == 0 || n2 == 0))
            throw new ZeroMultiplicationException("Multiplication by zero not allowed.");

        if (op.equals("/") && n2 == 0)
            throw new DivideByZeroException("Division by zero not allowed.");
    }

    static int calculate(int n1, int n2, String op) {
        return switch (op) {
            case "+" -> Math.addExact(n1, n2);
            case "-" -> Math.subtractExact(n1, n2);
            case "*" -> Math.multiplyExact(n1, n2);
            case "/" -> n1 / n2;
            default -> 0;
        };
    }
}

class NegativeNumberException extends Exception {
    NegativeNumberException(String msg) {
        super(msg);
    }
}

class InvalidOperatorException extends Exception {
    InvalidOperatorException(String msg) {
        super(msg);
    }
}

class ZeroMultiplicationException extends Exception {
    ZeroMultiplicationException(String msg) {
        super(msg);
    }
}

class DivideByZeroException extends Exception {
    DivideByZeroException(String msg) {
        super(msg);
    }
}

