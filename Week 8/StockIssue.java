import java.util.Scanner;

public class StockIssue {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Warehouse w = new Warehouse();

        System.out.print("Enter number of requests: ");
        int n = Integer.parseInt(sc.nextLine());

        for (int i = 1; i <= n; i++) {
            System.out.println("\nRequest " + i);
            System.out.print("Enter item: ");
            String item = sc.nextLine();
            System.out.print("Enter quantity: ");
            int qty = Integer.parseInt(sc.nextLine());

            try {
                w.issue(item, qty);
                System.out.println("Issued successfully.");
            } catch (InvalidQuantityException | OutOfStockException | ItemNotFoundException e) {
                System.out.println("Issue failed: " + e.getMessage());
            }
        }

        System.out.println("\nRemaining Stock:");
        w.showStock();
        sc.close();
    }
}

class Warehouse {
    String[] items = { "Mobile", "Laptop", "IPad", "Tablet" };
    int[] stock = { 45, 25, 35, 18 };

    void issue(String item, int qty)
            throws InvalidQuantityException, OutOfStockException,
            ItemNotFoundException {

        if (qty <= 0)
            throw new InvalidQuantityException("Quantity must be greater than 0.");

        int index = -1;

        for (int i = 0; i < items.length; i++) {
            if (items[i].equalsIgnoreCase(item)) {
                index = i;
                break;
            }
        }

        if (index == -1)
            throw new ItemNotFoundException("Item not found.");

        if (qty > stock[index]) {
            int shortfall = qty - stock[index];
            throw new OutOfStockException(
                    "Out of stock. Shortfall = " + shortfall, shortfall);
        }

        stock[index] -= qty;
        System.out.println(qty + " " + items[index] + " issued.");
    }

    void showStock() {
        for (int i = 0; i < items.length; i++)
            System.out.println(items[i] + " : " + stock[i]);
    }
}

class OutOfStockException extends Exception {
    int shortfall;

    OutOfStockException(String message, int shortfall) {
        super(message);
        this.shortfall = shortfall;
    }
}

class InvalidQuantityException extends Exception {
    InvalidQuantityException(String message) {
        super(message);
    }
}

class ItemNotFoundException extends Exception {
    ItemNotFoundException(String message) {
        super(message);
    }
}

