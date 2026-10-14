public interface HashMap<K, V> {

    public V put(K key, V value);

    public V get(K key);

    public V remove(K key);

    public boolean containsKey(K key);

    public boolean containsValue(V value);

    public int size();

    public boolean isEmpty();

    public void clear();

    public Set<K> keySet();

    public ArrayList<V> values();
}