package at.fhv.bpmn;

import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class DeliveryNoteWorkerTest {
    private final DeliveryNoteWorker dnw = new DeliveryNoteWorker();
    private final static HashMap<String, Long> chosenBooks = new HashMap<String, Long>() {{
        put("Harry Potter", 30L);
        put("Haskell for Dummies", 5L);
        put("A", 2L);
    }};
    private final String realName = "Armin Admin";
    private final double cost = 35.0;
    private final String expectedNote = "\nYour order Armin Admin: \n" +
            "\tBookname: A, amount: 2\n" +
            "\tBookname: Harry Potter, amount: 30\n" +
            "\tBookname: Haskell for Dummies, amount: 5\n" +
            "Cost for delivery: 35.0";

    @Test
    void buildDeliveryNote() {
        String actual = dnw.buildDeliveryNote(chosenBooks, realName, cost);
        assertEquals(expectedNote, actual);
        assertTrue(actual.contains(realName));
        assertTrue(actual.contains(Double.toString(cost)));
        chosenBooks.forEach((name, amount) -> {
            assertTrue(actual.contains(name));
            assertTrue(actual.contains(Long.toString(amount)));
        });
    }

    @Test
    void buildMinimalNote() {
        String expected = "\n" +
                "Your order : \n" +
                "Cost for delivery: 0.0";
        assertEquals(expected, dnw.buildDeliveryNote(new HashMap<>(), "", 0.0));
    }
}