package com.dalmatians.datastructures;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Stack;

/**
 * Implementation of a generic Binary Search Tree
 * 
 * @author Christian Gallo Pelaez
 * @author Sebastian Garcia Acosta
 * @param <K,V>, any class that implements the Comparable interface
 */
public class BinarySearchTree<K extends Comparable<K>, V> implements Iterable<V> {
	/**
	 * This class represents the node of the BST.
	 * 
	 * The wild-card Node<K,V extends Comparable<? super K,V>> allows K,V to be a
	 * type that is a sub-type of some type that implements Comparable
	 * 
	 * @param <K,V>, any class that implements the Comparable interface
	 */
	@SuppressWarnings("hiding")
	protected class Node<K extends Comparable<? super K>, V> {

		/** Parent node */
		protected Node<K, V> parent;

		/** Left child */
		protected Node<K, V> left;

		/** Right child */
		protected Node<K, V> right;

		/** V, the data that the Node encapsulates */
		protected V value;

		/** K, the key */
		protected K key;

		protected int height;

		/**
		 * The data that Node encapsulates
		 * 
		 * @param data, an object of a class K,V that implements Comparable interface
		 */
		public Node(K key, V data) {
			this.value = data;
			this.left = null;
			this.right = null;
			this.parent = null;
			this.key = key;
			this.height = 0;
		}
	}

	/** Root node of the tree */
	protected Node<K, V> root;

	protected int size;

	/**
	 * Constructor
	 */
	public BinarySearchTree() {
		this.root = null;
		this.size = 0;
	}

	/**
	 * @param root
	 * @param space
	 */
	public void print2DUtil(Node<K, V> root, int space) {
		int count = 5;
		// Base case
		if (root == null)
			return;

		// Increase distance between levels
		space += count;

		// Process right child first
		print2DUtil(root.right, space);

		// Print current node after space
		// count
		for (int i = count; i < space; i++)
			System.out.print(" ");
		System.out.print(root.value + "\n");

		// Process left child
		print2DUtil(root.left, space);
	}

	// Wrapper over print2DUtil()
	public void print2D() {
		// Pass initial space count as 0
		print2DUtil(root, 0);
	}

	/**
	 * Searches an element in the tree
	 * 
	 * @param data, K,V
	 * @return Node<K,V> {@link Node}, the node that contains the data searched. If
	 *         not found, returns null
	 */
	private Node<K, V> searchNode(K key) {
		Node<K, V> nodeFound = root;
		boolean found = false;
		if (root != null) {
			while (!found && nodeFound != null) {
				if (key.compareTo(nodeFound.key) < 0)
					nodeFound = nodeFound.left;
				else if (key.compareTo(nodeFound.key) > 0)
					nodeFound = nodeFound.right;
				else
					found = true;
			}
		}
		if (!found)
			nodeFound = null;
		return nodeFound;
	}

	/**
	 * Determines whether an element is in the tree
	 * 
	 * @param key
	 * @return boolean, true if the element is within the tree; otherwise, false.
	 */
	public boolean contains(K key) {
		Node<K, V> found = searchNode(key);
		return (found != null);
	}

	public V search(K key) {
		Node<K, V> found = searchNode(key);
		return (found == null) ? null : found.value;
	}

	/**
	 * 
	 * @param value
	 */
	public void add(K key, V value) {
		if (key == null)
			throw new IllegalArgumentException("Key cannot be null");
		else {
			root = add(root, key, value);
			size++;
		}
	}

	protected Node<K, V> add(Node<K, V> currentNode, K key, V value) {
		if (currentNode == null)
			return new Node<>(key, value);
		int cmp = key.compareTo(currentNode.key);
		if (cmp < 0) {
			currentNode.left = add(currentNode.left, key, value);
		} else if (cmp > 0) {
			currentNode.right = add(currentNode.right, key, value);
		} else {
			currentNode.right = add(currentNode.right, key, value);
		}
		return currentNode;
	}

	public void delete(K key) {
		if (key == null)
			throw new IllegalArgumentException("Null key to delete");
		root = delete(root, key);
	}

	protected Node<K, V> delete(Node<K, V> node, K key) {
		int cmp = key.compareTo(node.key);
		if (cmp < 0) {
			node.left = delete(node.left, key);
		} else if (cmp > 0) {
			node.right = delete(node.right, key);
		} else {
			if (node.left == null) {
				return node.right;
			} else if (node.right == null) {
				return node.left;
			} else {
				Node<K, V> y = node;
				node = getMinimum(y.right);
				node.right = deleteMin(y.right);
				node.left = y.left;
			}
			size--;
		}
		return node;
	}

	public int size() {
		return size;
	}

	public void deleteMin() {
		if (isEmpty())
			throw new NoSuchElementException("called deleteMin() with empty symbol table");
		root = deleteMin(root);
	}

	protected Node<K, V> deleteMin(Node<K, V> x) {
		if (x.left == null)
			return x.right;
		x.left = deleteMin(x.left);
		x.height = 1 + Math.max(height(x.left), height(x.right));
		return x;
	}

	public Node<K, V> getMaximum(Node<K, V> localRoot) {
		Node<K, V> currentNode = localRoot;
		while (currentNode.right != null)
			currentNode = currentNode.right;
		return currentNode;
	}

	public Node<K, V> getMinimum(Node<K, V> localRoot) {
		Node<K, V> currentNode = localRoot;
		while (currentNode.left != null)
			currentNode = currentNode.left;
		return currentNode;
	}

	/**
	 * Returns the height of the internal AVL tree. It is assumed that the height of
	 * an empty tree is -1 and the height of a tree with just one node is 0.
	 * 
	 * @return the height of the internal AVL tree
	 */
	public int height() {
		return height(root);
	}

	protected int height(Node<K, V> x) {
		if (x == null)
			return -1;
		return x.height;
	}

	public boolean isEmpty() {
		return root == null;
	}

	public V getRootData() {
		return this.root.value;
	}

	public void reset() {
		this.root = null;
	}

	public List<V> inorder() {
		List<V> list = new ArrayList<>();
		inorder(root, list);
		return list;
	}

	private void inorder(Node<K, V> root, List<V> list) {
		if (root != null) {
			inorder(root.left, list);
			list.add(root.value);
			inorder(root.right, list);
		}
	}

	public List<V> preorder() {
		List<V> list = new ArrayList<>();
		preorder(root, list);
		return list;
	}

	private void preorder(Node<K, V> root, List<V> list) {
		if (root != null) {
			list.add(root.value);
			preorder(root.left, list);
			preorder(root.right, list);
		}
	}

	public List<V> postorder() {
		List<V> list = new ArrayList<>();
		postorder(root, list);
		return list;
	}

	private void postorder(Node<K, V> root, List<V> list) {
		if (root != null) {
			postorder(root.left, list);
			postorder(root.right, list);
			list.add(root.value);
		}
	}

	@Override
	public Iterator<V> iterator() {
		return new InorderIterator();
	}

	/* Iterator */
	private class InorderIterator implements Iterator<V> {
		/** The nodes that are still to be visited. */
		private Stack<Node<K, V>> stack;

		/** Construct. */
		private InorderIterator() {
			stack = new Stack<Node<K, V>>();
			pushPathToMin(root);
		}

		/**
		 * Push all the nodes in the path from a given node to the leftmost node in the
		 * subtree.
		 */
		private void pushPathToMin(Node<K, V> localRoot) {
			Node<K, V> current = localRoot;
			while (current != null) {
				stack.push(current);
				current = current.left;
			}
		}

		/** Is there another element in this iterator? */
		public boolean hasNext() {
			return !stack.isEmpty();
		}

		/**
		 * The next element in this iterator; Advance the iterator.
		 */
		public V next() {
			Node<K, V> node = stack.peek();
			stack.pop();
			pushPathToMin(node.right);
			return node.value;
		}

		/** (Don't) remove an element from this iterator. */
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}
}
