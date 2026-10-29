package lab14;
import org.junit.jupiter.api.Test;
import lab14.service.ItemService;
import static org.junit.jupiter.api.Assertions.*;
public class ItemServiceTest {
    @Test
    void addingItemIncreasesCount() {
        ItemService service = new ItemService();

        service.addItem("Book");

        assertEquals(1, service.getCount());
    }
    @Test
    void addingMultipleItemsIncreasesCount() {
        ItemService service = new ItemService();

        service.addItem("Pen");
        service.addItem("Pencil");

        assertEquals(2, service.getCount());
    }
    @Test
    void invalidActionThrowsException() {
        ItemService service = new ItemService();
        assertThrows(IllegalArgumentException.class,
                () -> service.performAction(9, ""));
    }
}

