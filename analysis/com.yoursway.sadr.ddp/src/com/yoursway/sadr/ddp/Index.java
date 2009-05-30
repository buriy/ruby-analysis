package com.yoursway.sadr.ddp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Index<K, V> {

	protected Map<K, Collection<IndexEntry<V>>> map = new HashMap<K, Collection<IndexEntry<V>>>();

	public void put(K key, V value, String file) {
		IndexEntry<V> entry = new IndexEntry<V>(value, file);

		Collection<IndexEntry<V>> c = map.get(key);
		if (c == null) {
			c = new ArrayList<IndexEntry<V>>();
			map.put(key, c);
		}

		c.add(entry);
	}

	public Collection<V> get(K key) {
		List<V> result = new ArrayList<V>();
		Collection<IndexEntry<V>> collection = map.get(key);
		if (collection != null) {
			for (IndexEntry<V> i : collection)
				result.add(i.value());
		}
		return result;
	}

	public void removeEntriesFrom(String file) {
		for (Collection<IndexEntry<V>> c : map.values()) {
			List<IndexEntry<V>> kill = new ArrayList<IndexEntry<V>>();
			for (IndexEntry<V> i : c)
				if (i.fileName().equals(file))
					kill.add(i);
			c.removeAll(kill);
		}
	}

}
