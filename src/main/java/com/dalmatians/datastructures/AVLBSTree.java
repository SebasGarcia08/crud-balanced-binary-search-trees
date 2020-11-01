package com.dalmatians.datastructures;

public class AVLBSTree<K extends Comparable<K>, V> extends BinarySearchTree<K, V> {

	public AVLBSTree() {
		super();
	}

	@Override
	public Node<K, V> add(Node<K, V> node, K key, V value) {
		Node<K, V> x = super.add(root, key, value);
		x.height = 1 + Math.max(height(node.left), height(node.right));
		return balance(x);
	}

	@Override
	protected Node<K, V> delete(Node<K, V> node, K key, V value) {
		Node<K, V> x = super.delete(node, key, value);
		x.height = 1 + Math.max(height(x.left), height(x.right));
		return balance(x);
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
	private int balanceFactor(Node<K, V> x) {
		return height(x.left) - height(x.right);
	}

	/**
	 * Rotates the given subtree to the right.
	 * 
	 * @param x the subtree
	 * @return the right rotated subtree
	 */
	private Node<K, V> rotateRight(Node<K, V> x) {
		Node<K, V> y = x.left;
		x.left = y.right;
		y.right = x;
		x.height = 1 + Math.max(height(x.left), height(x.right));
		y.height = 1 + Math.max(height(y.left), height(y.right));
		return y;
	}

	/**
	 * Rotates the given subtree to the left.
	 * 
	 * @param x the subtree
	 * @return the left rotated subtree
	 */
	private Node<K, V> rotateLeft(Node<K, V> x) {
		Node<K, V> y = x.right;
		x.right = y.left;
		y.left = x;
		x.height = 1 + Math.max(height(x.left), height(x.right));
		y.height = 1 + Math.max(height(y.left), height(y.right));
		return y;
	}
}