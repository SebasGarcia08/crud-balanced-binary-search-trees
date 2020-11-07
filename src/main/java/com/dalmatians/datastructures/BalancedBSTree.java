package com.dalmatians.datastructures;

import java.util.List;

public interface BalancedBSTree<K extends Comparable<K>, V> {
	public void add(K key,V value);
	public List<V> search(K key);
	public void delete(K key);
	public boolean contains(K key);
	public List<V> autoComplete(String key, int maxSize);
}
