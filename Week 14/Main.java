
package lab14;
import lab14.service.ItemService;
import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ItemService service = new ItemService();
        while (true) {
            System.out.println("\n1. Add Item");
            System.out.println("2. Show Count");
            System.out.println("3. Exit");
            System.out.print("Enter choice: ");
            int choice = sc.nextInt();
            sc.nextLine();

            try {
                switch (choice) {
                    case 1:
                        System.out.print("Enter item name: ");
                        String name = sc.nextLine();
                        service.performAction(1, name);
                        System.out.println("Item added successfully");
                        break;

                    case 2:
                        service.performAction(2, "");
                        break;

                    case 3:
                        System.out.println("Exiting...");
                        sc.close();
                        return;

                    default:
                        service.performAction(choice, "");
                }
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}

