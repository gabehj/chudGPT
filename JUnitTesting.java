import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;

public class JUnitTesting {

    private InputStream originalIn;

    @Before
    public void saveSystemIn() {
        originalIn = System.in;
    }

    @After
    public void restoreSystemIn() {
        System.setIn(originalIn);
    }

    // ---------- Helpers ----------

    /** Builds a Response for a user whose only interest is "stem". */
    private Response makeResponse(MessageNode... nodes) {
        User user = new User(new String[] {"stem", "comedic"});
        user.getInterests().addToEnd("stem");

        HashMap<String, MessageNode> bank = new HashMap<>();
        for (MessageNode node : nodes) {
            bank.put(node.getMessage(), node);
        }
        return new Response(user, bank);
    }

    /** Feeds the given text to System.in, then builds a DataVal that reads it. */
    private DataVal dataValWithInput(String text) {
        System.setIn(new ByteArrayInputStream(text.getBytes()));
        return new DataVal();
    }

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
    public void testRemoveFirstReturnsFrontEntry() {
        LinkedChain<String> chain = new LinkedChain<>();
        chain.addToEnd("A");
        chain.addToEnd("B");

        assertEquals("A", chain.remove());
        assertEquals(1, chain.size());
        assertEquals("B", chain.get(0));
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

    @Test
    public void testRemoveNullReturnsFalse() {
        LinkedChain<String> chain = new LinkedChain<>();
        chain.add("A");

        assertFalse(chain.remove(null));
        assertEquals(1, chain.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddRejectsNull() {
        LinkedChain<String> chain = new LinkedChain<>();
        chain.add(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddToEndRejectsNull() {
        LinkedChain<String> chain = new LinkedChain<>();
        chain.addToEnd(null);
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

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetRejectsNegativeIndex() {
        LinkedChain<String> chain = new LinkedChain<>();
        chain.add("A");
        chain.get(-1);
    }

    @Test
    public void testCountAllowsDuplicates() {
        LinkedChain<String> chain = new LinkedChain<>();
        chain.addToEnd("x");
        chain.addToEnd("y");
        chain.addToEnd("x");

        assertEquals(2, chain.count("x"));
        assertEquals(1, chain.count("y"));
        assertEquals(0, chain.count("z"));
        assertEquals(0, chain.count(null));
    }

    @Test
    public void testToArrayFollowsChainOrder() {
        LinkedChain<String> chain = new LinkedChain<>();
        chain.addToEnd("a");
        chain.addToEnd("b");
        chain.addToEnd("c");

        // toArray() is really an Object[] at runtime, so assign it to Object[].
        Object[] values = chain.toArray();

        assertEquals(3, values.length);
        assertEquals("a", values[0]);
        assertEquals("b", values[1]);
        assertEquals("c", values[2]);
    }

    @Test
    public void testToString() {
        LinkedChain<String> chain = new LinkedChain<>();
        assertEquals("[]", chain.toString());

        chain.addToEnd("a");
        chain.addToEnd("b");
        chain.addToEnd("c");
        assertEquals("[a, b, c]", chain.toString());
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
    public void testRemoveFromCollisionChainKeepsOtherKey() {
        HashMap<String, Integer> map = new HashMap<>(5);
        map.put("Aa", 1);
        map.put("BB", 2);

        assertEquals(Integer.valueOf(1), map.remove("Aa"));

        assertNull(map.get("Aa"));
        assertEquals(Integer.valueOf(2), map.get("BB"));
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

    @Test
    public void testDefaultHashMapRehashesOnThirteenthEntry() {
        HashMap<Integer, Integer> map = new HashMap<>();

        // 12 / 17 is about 0.71, still under the 0.75 load factor.
        for (int i = 0; i < 12; i++) {
            map.put(i, i);
        }
        assertEquals(17, map.tableSize());

        // 13 / 17 is about 0.76, which triggers a rehash that doubles the table.
        map.put(12, 12);
        assertEquals(34, map.tableSize());
        assertEquals(13, map.size());

        for (int i = 0; i < 13; i++) {
            assertEquals(Integer.valueOf(i), map.get(i));
        }
    }

    @Test
    public void testKeysAndValuesReturnEveryEntry() {
        HashMap<String, Integer> map = new HashMap<>();
        map.put("Alice", 10);
        map.put("Bob", 20);
        map.put("Cara", 30);

        ArrayList<String> keys = new ArrayList<>();
        for (String key : map.keys()) {
            keys.add(key);
        }
        ArrayList<Integer> values = new ArrayList<>();
        for (Integer value : map.values()) {
            values.add(value);
        }

        // Order depends on the hash codes, so only check membership.
        assertEquals(3, keys.size());
        assertTrue(keys.contains("Alice"));
        assertTrue(keys.contains("Bob"));
        assertTrue(keys.contains("Cara"));

        assertEquals(3, values.size());
        assertTrue(values.contains(10));
        assertTrue(values.contains(20));
        assertTrue(values.contains(30));
    }

    @Test
    public void testKeysAndValuesAreEmptyForEmptyMap() {
        HashMap<String, Integer> map = new HashMap<>();

        assertFalse(map.keys().iterator().hasNext());
        assertFalse(map.values().iterator().hasNext());
    }

    @Test
    public void testHashMapAllowsNullValue() {
        HashMap<String, Integer> map = new HashMap<>();

        assertNull(map.put("Alice", null));

        assertEquals(1, map.size());
        assertNull(map.get("Alice"));
    }

    @Test(expected = NullPointerException.class)
    public void testHashMapRejectsNullKey() {
        HashMap<String, Integer> map = new HashMap<>();
        map.put(null, 1);
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
    public void testMessageNodeArrayConstructorCopiesThemes() {
        MessageNode node =
            new MessageNode("Hello", new String[] {"comedic", "stem"}, 1.0);

        assertEquals(2, node.getThemes().size());
        assertEquals("comedic", node.getThemes().get(0));
        assertEquals("stem", node.getThemes().get(1));
    }

    @Test
    public void testMessageNodeSetLogit() {
        MessageNode node =
            new MessageNode("Hello", new ArrayList<>(), 1.0);

        node.setLogit(4.5);

        assertEquals(4.5, node.getLogit(), 0.0001);
    }

    @Test
    public void testMessageNodeAddTheme() {
        MessageNode node =
            new MessageNode("Hello", new ArrayList<>(), 0.0);

        node.addTheme("romantic");

        assertEquals(1, node.getThemes().size());
        assertEquals("romantic", node.getThemes().get(0));
    }

    // ---------- DataVal tests ----------

    @Test
    public void testSelectFromRangeAcceptsValidNumber() {
        DataVal input = dataValWithInput("5\n");

        assertEquals(5, input.selectFromRange("Pick", 1, 10));
    }

    @Test
    public void testSelectFromRangeAcceptsBoundaries() {
        assertEquals(1, dataValWithInput("1\n").selectFromRange("Pick", 1, 10));
        assertEquals(10, dataValWithInput("10\n").selectFromRange("Pick", 1, 10));
    }

    @Test
    public void testSelectFromRangeRetriesUntilValid() {
        // Non-number, too high, too low, then valid.
        DataVal input = dataValWithInput("abc\n11\n0\n7\n");

        assertEquals(7, input.selectFromRange("Pick", 1, 10));
    }

    @Test
    public void testSelectFromOptionsIsCaseInsensitive() {
        DataVal input = dataValWithInput("yes\n");

        // Returns the option as stored in the array, not what was typed.
        assertEquals("Yes", input.selectFromOptions("Pick", new String[] {"Yes", "No"}));
    }

    @Test
    public void testSelectFromOptionsTrimsAndRetries() {
        DataVal input = dataValWithInput("maybe\n  no  \n");

        assertEquals("No", input.selectFromOptions("Pick", new String[] {"Yes", "No"}));
    }

    // ---------- User tests ----------

    @Test
    public void testUserStartsWithNoNameOrInterests() {
        User user = new User(new String[] {"stem", "comedic"});

        assertNull(user.getName());
        assertTrue(user.getInterests().isEmpty());
    }

    @Test
    public void testPostSurveyReturnsRating() {
        System.setIn(new ByteArrayInputStream("9\n".getBytes()));
        User user = new User(new String[] {"stem"});

        assertEquals(9, user.postSurvey());
    }

    @Test
    public void testPostSurveyRetriesOutOfRangeRating() {
        System.setIn(new ByteArrayInputStream("0\n11\n4\n".getBytes()));
        User user = new User(new String[] {"stem"});

        assertEquals(4, user.postSurvey());
    }

    // ---------- Response tests ----------
    //
    // Note: Response only treats words that end in a space as candidates, and
    // looking up a word that isn't a known keyword currently throws a
    // NullPointerException. So these tests use single-word user messages
    // (or messages whose leading words are known keywords).

    @Test
    public void testResponseLoadsUserInterestsAsProfileKeywords() {
        Response response = makeResponse();

        assertNotNull(response.isUserProfileKeyword.get("stem"));
        assertNull(response.isUserProfileKeyword.get("comedic"));
    }

    @Test
    public void testResponseLoadsBotThemesAsMasterKeywords() {
        Response response = makeResponse(
            new MessageNode("Bot line", new String[] {"comedic", "romantic"}, 0.0));

        assertNotNull(response.isMasterKeyword.get("comedic"));
        assertNotNull(response.isMasterKeyword.get("romantic"));
        assertNull(response.isMasterKeyword.get("stem"));
    }

    @Test
    public void testNewVocabStoresMessage() {
        Response response = makeResponse();

        response.newVocab("Hello");

        assertNotNull(response.theMasterList.get("Hello"));
        assertEquals("Hello", response.theMasterList.get("Hello").getMessage());
    }

    @Test
    public void testNewVocabInitializesLogitToZero() {
        Response response = makeResponse();

        response.newVocab("Hello");

        assertEquals(0.0, response.theMasterList.get("Hello").getLogit(), 0.0001);
    }

    @Test
    public void testNewVocabAddsUserInterestsAsThemes() {
        Response response = makeResponse();

        response.newVocab("Hello");

        assertTrue(response.theMasterList.get("Hello").getThemes().contains("stem"));
    }

    @Test
    public void testNewVocabAddsRecognizedKeywordsAsThemes() {
        Response response = makeResponse(
            new MessageNode("Bot line", new String[] {"comedic"}, 0.0));

        // "comedic" ends in a space, so it gets checked against the master keywords.
        response.newVocab("comedic stuff");

        assertTrue(response.theMasterList.get("comedic stuff").getThemes().contains("comedic"));
    }

    @Test
    public void testSamplingReturnsOnlyAvailableMessage() {
        Response response = makeResponse(
            new MessageNode("Hello there", new String[] {}, 0.0));

        assertEquals("Hello there", response.sampleBotMessage("hi"));
    }

    @Test
    public void testSamplingWithEmptyBankReturnsErrorMessage() {
        Response response = makeResponse();

        assertEquals("ERROR: Message not sampled", response.sampleBotMessage("hi"));
    }

    @Test
    public void testSamplingFavorsMuchHigherLogit() {
        Response response = makeResponse(
            new MessageNode("Low", new String[] {}, 0.0),
            new MessageNode("High", new String[] {}, 100.0));

        // Random noise is under 5, so a 100-point lead always wins.
        for (int i = 0; i < 20; i++) {
            assertEquals("High", response.sampleBotMessage("hi"));
        }
    }

    @Test
    public void testSamplingBoostsProfileKeywordMatches() {
        // Ten "stem" themes give +10 from the user profile, more than the max noise of 5.
        Response response = makeResponse(
            new MessageNode("Match",
                new ArrayList<>(Collections.nCopies(10, "stem")), 0.0),
            new MessageNode("Other", new String[] {}, 0.0));

        for (int i = 0; i < 20; i++) {
            assertEquals("Match", response.sampleBotMessage("hi"));
        }
    }

    @Test
    public void testSamplingBoostsUserMessageKeywordMatches() {
        Response response = makeResponse(
            new MessageNode("Match",
                new ArrayList<>(Collections.nCopies(10, "comedic")), 0.0),
            new MessageNode("Other", new String[] {}, 0.0));

        // "comedic" is a known keyword and ends in a space, so it is picked up.
        for (int i = 0; i < 20; i++) {
            assertEquals("Match", response.sampleBotMessage("comedic stuff"));
        }
    }

    @Test
    public void testSamplingDoesNotChangeStoredLogits() {
        MessageNode node = new MessageNode("Hello there", new String[] {"stem"}, 3.0);
        Response response = makeResponse(node);

        response.sampleBotMessage("hi");

        assertEquals(3.0, node.getLogit(), 0.0001);
    }

    @Test
    public void testLearnCreatesNodesForNewMessages() {
        Response response = makeResponse();

        ArrayList<String> history = new ArrayList<>();
        history.add("Hello");

        response.learn(history, 8);

        assertNotNull(response.theMasterList.get("Hello"));
    }

    @Test
    public void testLearnRecordsEveryMessageInHistory() {
        Response response = makeResponse();

        ArrayList<String> history = new ArrayList<>();
        history.add("Hi");
        history.add("Yo");
        history.add("Hey");

        response.learn(history, 6);

        assertNotNull(response.theMasterList.get("Hi"));
        assertNotNull(response.theMasterList.get("Yo"));
        assertNotNull(response.theMasterList.get("Hey"));
    }

    @Test
    public void testLearnKeepsExistingNode() {
        MessageNode node = new MessageNode("Hello", new String[] {"stem"}, 3.0);
        Response response = makeResponse(node);

        ArrayList<String> history = new ArrayList<>();
        history.add("Hello");
        response.learn(history, 8);

        assertSame(node, response.theMasterList.get("Hello"));
    }

    @Test
    public void testLearnWithEmptyHistoryDoesNothing() {
        Response response = makeResponse();

        response.learn(new ArrayList<>(), 8);

        assertNull(response.expected_TD_Engagement);
        assertTrue(response.theMasterList.isEmpty());
    }

    @Test
    public void testLearnSetsExpectedEngagement() {
        Response response = makeResponse();

        ArrayList<String> history = new ArrayList<>();
        history.add("Hello");
        response.learn(history, 8);

        assertNotNull(response.expected_TD_Engagement);
    }

    // Known gap: learn() computes the new logit in a local variable but never
    // calls setLogit, so scores don't change anything. Remove @Ignore once fixed.
    // With no prior expectation, a score of 10 should add 10 - 5.5 = 4.5.
    @Ignore("learn() never writes the updated logit back to the node")
    @Test
    public void testLearnPersistsScoreBasedLogitChange() {
        MessageNode node = new MessageNode("Hello", new String[] {}, 0.0);
        Response response = makeResponse(node);

        ArrayList<String> history = new ArrayList<>();
        history.add("Hello");
        response.learn(history, 10);

        assertEquals(4.5, node.getLogit(), 0.0001);
    }
}