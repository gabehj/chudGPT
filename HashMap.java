import java.util.ArrayList;

import javax.swing.text.Position;

public class HashMap<K, V> {
	
	/* Use Array of DoublylinkedList<K,V> for the Underlying storage for the map of entries.
	 * 
	 */
	private LinkedChain<Entry<K,V>>[]  table;
	int size;  // number of mappings(entries) 
	int capacity; // The size of the hash table. 
	int DefaultCapacity = 17; //The default hash table size
	
	/* Maintain the load factor <= 0.75.
	 * If the load factor is greater than 0.75, 
	 * then double the table, rehash the entries, and put then into new places. 
	 */
	double  loadfactor= 0.75;  
	
	/**
	 * Constructor that takes a hash size
	 * @param hashtable size: the number of buckets to initialize 
	 */
	@SuppressWarnings("unchecked")
	public HashMap(int hashtablesize) {
		capacity = hashtablesize; // Set capacity to inputed value
		
		size = 0; // no elements
		table = (LinkedChain<Entry<K,V>>[]) new LinkedChain[capacity]; // Construct table of given size
	
		for (int i = 0; i < capacity; i++) table[i] = new LinkedChain<>(); // Construct empty lists for each table index
	}
	
	/**
	 * Constructor that takes no argument
	 * Initialize the hash table with default hash table size: 17
	 */
	@SuppressWarnings("unchecked")
	public HashMap() {
		capacity = DefaultCapacity; // Initialize capacity with default 17
				
		size = 0; // no elements
		table = (LinkedChain<Entry<K,V>>[]) new LinkedChain[capacity]; // Construct table of default size
		
		for (int i = 0; i < capacity; i++) table[i] = new LinkedChain<>(); // Construct empty lists for each table index

	}
	
	/* This method should be called by map an integer to the index range of the hash table 
	 */
	private int hashValue(K key) {
		return Math.abs(key.hashCode()) % capacity;
	}
	
	/*
	 * The purpose of this method is for testing if the table was doubled when rehashing is needed. 
	 * Return the the size of the hash table. 
	 * It should be 17 initially, after the load factor is more than 0.75, it should be doubled.
	 */
	public int tableSize() {
		return capacity; // Return maximum capacity of table
	}
	
	
	public int size() {
		return size; // Return variable for number of elements
	}

	public boolean isEmpty() {
		return size==0; // True if no elements else false
	}

	//@TimeComplexity(" O(n) ") //This is for worst case time complexities.
	//@TimeComplexityExpected(" O(1) ") //This is for amortized time complexities.
	public V get(K key) {
		/* TCJ:
		 * Value is indexed in trivial time. 
		 * If there is one or zero values in linked list at given index, then O(1) amortized time
		 * Value is referenced immediately without traversal
		 * 
		 * Otherwise, the linked list is crowded and it costs O(n) to traverse values
		 */
		int index = hashValue(key); // Hash key to get table index
		
		for (Entry<K, V> entry: table[index].toArray()) { // Iterate through list at table index
			if (entry.getKey().equals(key)) { // See if any entries with matching indices have matching keys
				return entry.getValue(); // Return value for entry with inputed key
			}
		}
		return null; // Nothing to get
	}

	//@TimeComplexity(" O(n) ") //This is for worst case time complexities.
	//@TimeComplexityExpected(" O(1) ") //This is for amortized time complexities.
	public V put(K key, V value) {
		/* TCJ:
		 * If linked list at calculated index for inputed key is relatively empty, that the 
		 * new value can be appended in trivial time, making an amortized O(1)
		 * 
		 * However, more elements need to be traversed if the linked list is crowded. 
		 * Also, the table may need to be rehashed after addition of element.
		 * Thus, worst-case of O(n)
		 */
		int index = hashValue(key); // Hash key to get table index
		
		// Case 1: Key is already in table
		for (Entry<K, V> entry: table[index].toArray()) {// Iterate through each node in list for table index // O(n)
			
			if (entry.getKey().equals(key)) { // Check if entry key equals inputed key for each node of matching index
				V old = entry.getValue(); // Store old entry value to get replaced
				entry.setValue(value); // Replace value of entry O(1)
				
				return old; // Return old entry value
			}
		}
		// Case 2: New element is added
		Entry<K,V> newEntry = new Entry<>(key, value); // Create new entry object with inputed variables
		
		LinkedChain<Entry<K,V>> list = table[index];
		list.add(newEntry); // Append new entry to correct list O(1)
        
		size++; // Increment size
		
		if ((double) size / (double) capacity > loadfactor) { // Determine if table is crowded and rehashing is needed
			rehash(); // Call rehash method O(n)
		}
		return null; // No previous value
	}
	
	
	@SuppressWarnings("unchecked")
	protected void rehash() { // O(n) Create larger table with smaller lists // O(n)
	    LinkedChain<Entry<K,V>>[] oldTable = table; // Reassign table to different variable

		capacity *= 2; // Double capacity
		table = (LinkedChain<Entry<K,V>>[]) new LinkedChain[capacity]; // Recreate table object of greater capacity
		for (int i = 0; i < capacity; i++) table[i] = new LinkedChain<>(); // Create empty lists objects in table
		
		size = 0; // Reset size to 0
		
		for (LinkedChain<Entry<K,V>> list: oldTable) { // Iterate through each list in old table
			for (Entry<K,V> entry: list.toArray()) { // iterate through each entry in old list // O(n)
				put(entry.getKey(),entry.getValue()); // Add entry to new, larger table // O(n)
			}
		}
	}
	

	//@TimeComplexity(" O(n) ") //This is for worst case time complexities.
	//@TimeComplexityExpected(" O(1) ") //This is for amortized time complexities.
	public V remove(K key) {
			/* TCJ:
			 * Value is indexed in trivial time. 
			 * If there is one or zero values in linked list at given index, then O(1) amortized time
			 * Value is referenced immediately without traversal
			 * 
			 * Otherwise, the linked list is crowded and it costs O(n) to traverse values before removal
			 */
		int index = hashValue(key); // Hash key to get table index
		
		for (Entry<K, V> entry: table[index].toArray()) { // Iterate through each node in list for table index
			
			if (entry.getKey().equals(key)) { // Determine if key matches inputed key
				V old = entry.getValue(); // Store old entry vale to variable
				table[index].remove(entry); // remove entry from linked list
				
				size--; // Decrement size
				return old; // Return old value
			}
		}
		return null; // Nothing to remove
	}

	public Iterable<V> values() {
		// Hint: call entrySet of each bucket map. 
		ArrayList<V> buffer = new ArrayList<>(); // Create empty new arraylist

		for (int i=0; i<table.length; i++) { // Iterate through every list in table
			for (Entry<K, V> node : table[i].toArray()) { // Iterate through every entry in list
				buffer.addLast(node.getValue()); // Add each entry to arraylist
			}
		}
		return buffer; // Return arraylist
	}

    public Iterable<K> keys() {
		// Hint: call entrySet of each bucket map. 
		ArrayList<K> buffer = new ArrayList<>(); // Create empty new arraylist

		for (int i=0; i<table.length; i++) { // Iterate through every list in table
			for (Entry<K, V> node : table[i].toArray()) { // Iterate through every entry in list
				buffer.addLast(node.getKey()); // Add each entry to arraylist
			}
		}
		return buffer; // Return arraylist
	}
}