import java.util.Scanner;

enum Coin {
    ONE(1), TWO(2), FIVE(5), TEN(10);
    Coin(int value) { this.value = value; }

    private final int value;
    public int value() { return value; }
}

public class VendingMachine {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    int result = 0;
    int a=0, b=0, c=0, d=0;
    while (result<15) {
        System.out.print("Enter coin (ONE, TWO, FIVE, TEN): ");
            String money = sc.nextLine().toUpperCase();
            Coin myVar = Coin.valueOf(money);

        switch(myVar) {
      case ONE:
        result += Coin.ONE.value();
        a++;
        break;
      case TWO:
         result += Coin.TWO.value();
         b++;
        break;
      case FIVE:
        result += Coin.FIVE.value();
        c++;
        break;
      case TEN:
        result += Coin.TEN.value();
        d++;
        break;
        default:
            System.out.println("Invalid coin");
            break;
    }
        
    }

    
    sc.close();
    
    System.out.println("Coins inserted:");
    System.out.println("ONE: " + a);
    System.out.println("TWO: " + b);
    System.out.println("FIVE: " + c);
    System.out.println("TEN: " + d);
    System.out.println("Total inserted: " + result + " change: " + (result - 15));

  }
}