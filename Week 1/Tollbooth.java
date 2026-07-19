import java.util.Scanner;

enum Coin {
    BIKE(30), CAR(50), TRUCK(150);

    Coin(int value) {
        this.value = value;
    }

    private final int value;

    public int value() {
        return value;
    }
}

public class Tollbooth {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        int result = 0;
        int a = 0, b = 0, c = 0;

        while (true) {

            System.out.print("Enter vehicle (BIKE, CAR, TRUCK) or DONE: ");
            String money = sc.nextLine();

            if (money.equalsIgnoreCase("done")) {
                break;
            }

            Coin myVar = Coin.valueOf(money.toUpperCase());

            switch (myVar) {

                case BIKE:
                    result += Coin.BIKE.value();
                    a++;
                    break;

                case CAR:
                    result += Coin.CAR.value();
                    b++;
                    break;

                case TRUCK:
                    result += Coin.TRUCK.value();
                    c++;
                    break;
            }
        }

        int x = Math.max(a, Math.max(b, c));

        System.out.println(b + " cars + " + a + " bikes + " + c + " trucks");
        System.out.println("Total Toll: " + result);
        if (a == x && b==x || a==x && c==x || c==x && b == x) {
          System.out.println("there are 2 most frequent vehicles");
        }
        else if (a == x)
            System.out.println("Most Frequent: bike");
        else if (b == x)
            System.out.println("Most Frequent: car");
        else
            System.out.println("Most Frequent: truck");

        sc.close();
    }
}