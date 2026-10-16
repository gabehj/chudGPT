import static org.junit.Assert.*;
import org.junit.Test;
import java.util.ArrayList;

public class ChudGPTTests {

    // ---------- Entry tests ----------

    @Test
    public void testEntryGetters() {
        Entry<String, Integer> entry = new Entry<>("hello", 5);

        assertEquals("hello", entry.getKey());
        assertEquals(Integer.valueOf(5), entry.getValue());
    }

    @Test
    public void testEntrySetValue() {
        Entry<String, Integer> entry = new Entry<>("score", 5);

        entry.setValue(10);

        assertEquals(Integer.valueOf(10), entry.getValue());
    }

    // ---------- LinkedChain tests ----------

    @Test
    public void testLinkedChainStartsEmpty() {
        LinkedChain<String> chain = new LinkedChain<>();

        assertTrue(chain.isEmpty());
        assertEquals(0, chain.size());
        assertNull(chain.remove());
    }

    @Test
    public void testAddAndGet() {
        LinkedChain<String> chain = new LinkedChain<>();

        chain.add("first");
        chain.add("second");

        // add() inserts at the front.
        assertEquals("second", chain.get(0));
        assertEquals("first", chain.get(1));
        assertEquals(2, chain.size());
    }

    @Test
    public void testAddToEndPreservesOrder() {
        LinkedChain<String> chain = new LinkedChain<>();

        chain.addToEnd("first");
        chain.addToEnd("second");
        chain.addToEnd("third");

        assertEquals("first", chain.get(0));
        assertEquals("second", chain.get(1));
        assertEquals("third", chain.get(2));
    }

    @Test
    public void testRemoveSpecificEntry() {
        LinkedChain<String> chain = new LinkedChain<>();
        chain.addToEnd("A");
        chain.addToEnd("B");
        chain.addToEnd("C");

        assertTrue(chain.remove("B"));
        assertFalse(chain.contains("B"));
        assertEquals(2, chain.size());
        assertEquals("A", chain.get(0));
        assertEquals("C", chain.get(1));
    }

    @Test
    public void testRemoveMissingEntry() {
        LinkedChain<String> chain = new LinkedChain<>();
        chain.add("A");

        assertFalse(chain.remove("missing"));
        assertEquals(1, chain.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddRejectsNull() {
        LinkedChain<String> chain = new LinkedChain<>();
        chain.add(null);
    }

    @Test
    public void testClear() {
        LinkedChain<Integer> chain = new LinkedChain<>();
        chain.add(1);
        chain.add(2);

        chain.clear();

        assertTrue(chain.isEmpty());
        assertEquals(0, chain.size());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetRejectsInvalidIndex() {
        LinkedChain<String> chain = new LinkedChain<>();
        chain.get(0);
    }

    // ---------- HashMap tests ----------

    @Test
    public void testHashMapStartsEmpty() {
        HashMap<String, Integer> map = new HashMap<>();

        assertTrue(map.isEmpty());
        assertEquals(0, map.size());
        assertEquals(17, map.tableSize());
        assertNull(map.get("missing"));
    }

    @Test
    public void testPutAndGet() {
        HashMap<String, Integer> map = new HashMap<>();

        assertNull(map.put("Alice", 10));
        assertNull(map.put("Bob", 20));

        assertEquals(Integer.valueOf(10), map.get("Alice"));
        assertEquals(Integer.valueOf(20), map.get("Bob"));
        assertEquals(2, map.size());
    }

    @Test
    public void testPutUpdatesExistingKey() {
        HashMap<String, Integer> map = new HashMap<>();

        map.put("Alice", 10);
        Integer oldValue = map.put("Alice", 25);

        assertEquals(Integer.valueOf(10), oldValue);
        assertEquals(Integer.valueOf(25), map.get("Alice"));
        assertEquals(1, map.size());
    }

    @Test
    public void testHashMapHandlesCollisions() {
        HashMap<String, Integer> map = new HashMap<>(5);

        // These strings have the same Java hash code.
        map.put("Aa", 1);
        map.put("BB", 2);

        assertEquals(Integer.valueOf(1), map.get("Aa"));
        assertEquals(Integer.valueOf(2), map.get("BB"));
        assertEquals(2, map.size());
    }

    @Test
    public void testRemoveExistingKey() {
        HashMap<String, Integer> map = new HashMap<>();
        map.put("Alice", 10);

        assertEquals(Integer.valueOf(10), map.remove("Alice"));
        assertNull(map.get("Alice"));
        assertEquals(0, map.size());
        assertTrue(map.isEmpty());
    }

    @Test
    public void testRemoveMissingKey() {
        HashMap<String, Integer> map = new HashMap<>();
        map.put("Alice", 10);

        assertNull(map.remove("Bob"));
        assertEquals(1, map.size());
    }

    @Test
    public void testHashMapRehashesAndRetainsEntries() {
        HashMap<String, Integer> map = new HashMap<>(2);

        map.put("A", 1);
        map.put("B", 2);

        // 2 entries / 2 buckets exceeds the 0.75 load factor.
        assertEquals(4, map.tableSize());

        assertEquals(Integer.valueOf(1), map.get("A"));
        assertEquals(Integer.valueOf(2), map.get("B"));
        assertEquals(2, map.size());
    }

    // ---------- MessageNode tests ----------

    @Test
    public void testMessageNodeGetters() {
        ArrayList<String> themes = new ArrayList<>();
        themes.add("comedic");
        themes.add("stem");

        MessageNode node =
            new MessageNode("Hello there", themes, 2.5);

        assertEquals("Hello there", node.getMessage());
        assertEquals(themes, node.getThemes());
        assertEquals(2.5, node.getLogit(), 0.0001);
    }

    @Test
    public void testMessageNodeSetLogit() {
        MessageNode node =
            new MessageNode("Hello", new ArrayList<>(), 1.0);

        node.setLogit(4.5);

        assertEquals(4.5, node.getLogit(), 0.0001);
    }

    // ---------- Response tests ----------

    @Test
    public void testNewVocabStoresMessage() {
        Response response = new Response();

        response.newVocab("Hello there");

        assertNotNull(response.theMasterList.get("Hello there"));
        assertEquals(
            "Hello there",
            response.theMasterList.get("Hello there").getMessage()
        );
    }

    @Test
    public void testSamplingReturnsOnlyAvailableMessage() {
        Response response = new Response();
        response.newVocab("Hello there");

        assertEquals(
            "Hello there",
            response.sampleBotMessage("How are you?")
        );
    }

    @Test
    public void testNewVocabInitializesLogitToZero() {
        Response response = new Response();
        response.newVocab("A new message");

        assertEquals(
            0.0,
            response.theMasterList.get("A new message").getLogit(),
            0.0001
        );
    }
}
