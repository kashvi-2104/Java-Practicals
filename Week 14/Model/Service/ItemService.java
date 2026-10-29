package lab14.service;
import lab14.model.Item;
import java.util.ArrayList;
public class ItemService {
    private ArrayList<Item> items = new ArrayList<>();

    public void addItem(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid item name");
        }
        items.add(new Item(name));
    }
    public int getCount() {
        return items.size();
    }

    public void performAction(int choice, String name) {
        switch (choice) {
            case 1 -> addItem(name);
            case 2 -> System.out.println("Total items: " + getCount());
            default -> throw new IllegalArgumentException("Invalid action");
        }
    }
}
