package com.dalmatians.datastructures;

public interface BalancedBSTree<K extends Comparable<K>, V> {
	public void add(K key,V value);
	public V search(K key);
	public void delete(K key);
	public boolean contains(K key);
}
