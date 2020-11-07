package com.dalmatians.datastructures;

import java.io.Serializable;
import java.util.List;

public class AVLBSTree<K extends Comparable<K>, V> extends BinarySearchTree<K, V> implements BalancedBSTree<K, V>, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1005016605184816284L;

	public AVLBSTree() {
		super();
	}
	
	public static void main(String[] args) {
		
		AVLBSTree<String, String> avl = new AVLBSTree<>();
		avl.add("Chris", "Chris");
		avl.add("Maria", "Maria");
		avl.add("Juan", "Juan");
		avl.add("Clau", "Clau");
		avl.add("Claudio", "Claudio");
		avl.add("Clausilio", "Clausilio");
		avl.add("Julia", "Julia");
		avl.add("CZ", "CZ");
		avl.add("CZZZZZZZZZZZZZ", "CZZZZZZZZZZZZZ");
		avl.print2D();
		
		System.out.println("========");
		System.out.println(avl.preorderLookUp("JU", 100));
		
		System.out.println(avl.search("Clau"));
	}

	@Override
	protected Node<K, V> add(Node<K, V> node, K key, V value) {
		Node<K, V> x = super.add(node, key, value);
		x.height = 1 + Math.max(height(x.left), height(x.right));
		return balance(x);
	}

	@Override
	protected Node<K, V> delete(Node<K, V> node, K key) {
		Node<K, V> x = super.delete(node, key);
		if(x != null) { // leaf node case
			x.height = 1 + Math.max(height(x.left), height(x.right));						
			return balance(x);
		} else
			return null;
	}

	/**
	 * Restores the AVL tree property of the subtree.
	 * 
	 * @param x the subtree
	 * @return the subtree with restored AVL property
	 */
	private Node<K, V> balance(Node<K, V> x) {
		if (balanceFactor(x) < -1) {
			if (balanceFactor(x.right) > 0) {
				x.right = rotateRight(x.right);
			}
			x = rotateLeft(x);
		} else if (balanceFactor(x) > 1) {
			if (balanceFactor(x.left) < 0) {
				x.left = rotateLeft(x.left);
			}
			x = rotateRight(x);
		}
		return x;
	}

	/**
	 * Returns the balance factor of the subtree. The balance factor is defined as
	 * the difference in height of the left subtree and right subtree, in this
	 * order. Therefore, a subtree with a balance factor of -1, 0 or 1 has the AVL
	 * property since the heights of the two child subtrees differ by at most one.
	 * 
	 * @param x the subtree
	 * @return the balance factor of the subtree
	 */
	protected int balanceFactor(Node<K, V> x) {
		return height(x.left) - height(x.right);
	}

	@Override
	public List<V> autoComplete(String key, int maxSize) {
		return preorderLookUp(key, maxSize);
	}
}