package com.dalmatians.datastructures;

import java.util.LinkedList;
import java.util.List;

public class RedBlackBSTree<K extends Comparable<K>, V> {

	public static void main(String[] args) {

		System.out.println("TEST");
		RedBlackBSTree<Integer, Integer> tree = new RedBlackBSTree<>();

//		tree.add(10, 10);
//		tree.print2D();
//		System.out.println("===========================");
//		
//		tree.add(20, 20);
//		tree.print2D();
//		System.out.println("===========================");
//		
//		tree.add(30, 30);
//		tree.print2D();
//		System.out.println("===========================");
//		
//		tree.add(15, 15);
//		tree.print2D();
//		System.out.println("===========================");
		
		tree.add(8, 8);
		tree.print2D();
		System.out.println("===========================");
		tree.add(18, 18);
		tree.print2D();
		System.out.println("===========================");
		tree.add(5, 5);
		tree.print2D();
		System.out.println("===========================");
		tree.add(15, 15);
		tree.print2D();
		System.out.println("===========================");
		tree.add(17, 17);
		tree.print2D();
		System.out.println("===========================");
		tree.add(25, 25);
		tree.print2D();
		System.out.println("===========================");
		tree.add(40, 40);
		tree.print2D();
		System.out.println("===========================");
		tree.add(80, 80);
		tree.print2D();
		System.out.println("===========================");
	}

	protected class NodeRB<K extends Comparable<? super K>, V> {

		/** Nodes color. Black: false, Red: true */
		protected boolean color;

		/** Parent node */
		protected NodeRB<K, V> parent;

		/** Left child */
		protected NodeRB<K, V> left;

		/** Right child */
		protected NodeRB<K, V> right;

		/** V, the data that the Node encapsulates */
		protected List<V> values;

		/** K, the key */
		protected K key;

		protected int height;

		public NodeRB(K key, V data) {
			this.color = true;
			this.values = new LinkedList<>();
			this.values.add(data);
			this.left = null;
			this.right = null;
			this.parent = null;
			this.key = key;
			this.height = 0;
		}

		private NodeRB<K, V> getGrandParent() {

			NodeRB<K, V> grandParent = null;

			if (parent != null) {
				grandParent = parent.parent;
			}

			return grandParent;
		}

		private NodeRB<K, V> getUncle() {

			NodeRB<K, V> uncle = null;

			// Uncle is the other son of the grandparent (Brother of the parent)
			if (parent != null && parent.parent != null) {
				if (parent.parent.right == parent)
					uncle = parent.parent.left;
				else
					uncle = parent.parent.right;
			}

			return uncle;
		}

		public boolean isLeftChild() {
			if (parent != null && parent.left == this)
				return true;
			return false;
		}

		public boolean isRightChild() {
			if (parent != null && parent.right == this)
				return true;
			return false;
		}

	}

	/** Root node of the tree */
	protected NodeRB<K, V> root;

	/** Black Height of the tree */
	private int blackHeight;

	/** Size of the tree */
	protected int size;

	public RedBlackBSTree() {
		super();
		blackHeight = 0;
	}

	protected int height(NodeRB<K, V> x) {
		if (x == null)
			return -1;
		return x.height;
	}

	public void add(K key, V value) {
		if (key == null)
			throw new IllegalArgumentException("Key cannot be null");
		else {
			NodeRB<K, V> x = addBST(root, key, value);
			root.height = 1 + Math.max(height(root.left), height(root.right));
			balanceRBTree(x);
			size++;
		}
	}

	protected NodeRB<K, V> addBST(NodeRB<K, V> currentNode, K key, V value){

		NodeRB<K, V> addedNode = new NodeRB<>(key, value);
		
		if(root == null){
			root = addBST(currentNode, addedNode);
		}else{
			addBST(currentNode, addedNode);
		}
		
		return addedNode;
	}

	private NodeRB<K, V> addBST(NodeRB<K, V> currentNode, NodeRB<K, V> nodeToAdd){
		
		if(currentNode == null){
			return nodeToAdd;
		}

		int cmp = nodeToAdd.key.compareTo((K) currentNode.key);
		if (cmp < 0) {
			currentNode.left = addBST(currentNode.left, nodeToAdd);
			currentNode.left.parent = currentNode; //Adds parent
		} else if (cmp > 0) {
			currentNode.right = addBST(currentNode.right, nodeToAdd);
			currentNode.right.parent = currentNode; //Adds parent
		} else {
			currentNode.values.add(0, nodeToAdd.values.get(0)); // Add duplicate element at the beginning in order to avoid extra
																// iterations
		}

		return currentNode;
	}

	private void balanceRBTree(NodeRB<K, V> x) {
		// Step 2: If x is root, change color of x as BLACK (Black height of complete
		// tree increases by 1).
		if (x == root) {
			x.color = false;
			blackHeight++;
		}

		// Step 3: If color of x’s parent is not BLACK or x is not root
		if (x.parent != null && x.parent.color == true || x != root) { // TODO RE CHECK EVALUATION
			solveViolation(x);
		}
	}

	// i) Left Left Case (p is left child of g and x is left child of p)
	private void leftLeftCase(NodeRB<K, V> x) {
		// Swap colors of g and p
		boolean aux = x.parent.color;
		x.parent.color = x.getGrandParent().color;
		x.getGrandParent().color = aux;

		// Right Rotate g
		NodeRB<K, V> g = x.getGrandParent();
		if (g.parent == null) {
			root = rotateRight(g);
		} else {
			rotateRight(g);	
		}
	}

	// iii) Right Right Case (Mirror of case i)
	private void rightRightCase(NodeRB<K, V> x) {
		// Swap colors of g and p
		boolean aux = x.parent.color;
		x.parent.color = x.getGrandParent().color;
		x.getGrandParent().color = aux;

		// Left Rotate g
		NodeRB<K, V> g = x.getGrandParent();
		if (g.parent == null) {
			root = rotateLeft(g);
		} else {
			rotateLeft(g);
		}
	}

	private void solveViolation(NodeRB<K, V> x) {
		// Case 1: If x’s uncle is RED (Grand parent must have been black from property
		// 4)
		NodeRB<K, V> uncle = x.getUncle();
		if (uncle != null && uncle.color == true) {

			// Step 1.1 Change color of parent and uncle as BLACK.
			x.parent.color = false;
			uncle.color = false;

			// Step 1.2 color of grand parent as RED.
			x.getGrandParent().color = true;

			// Step 1.3 Change x = x’s grandparent, repeat steps 2 and 3 for new x.
			balanceRBTree(x.getGrandParent());

			// Case 2: If x’s uncle is BLACK, then there can be four configurations for x,
			// x’s parent (p) and x’s grandparent (g)
		} else {
			// TODO

			// i) Left Left Case (p is left child of g and x is left child of p)
			if (x.parent.isLeftChild() && x.isLeftChild()) { // TODO
				leftLeftCase(x);
			}

			// ii) Left Right Case (p is left child of g and x is right child of p)
			else if (x.parent.isLeftChild() && x.isRightChild()) {
				if (x.parent == root) {
					root = rotateLeft(x.parent);
				} else {
					rotateLeft(x.parent);
				}
				leftLeftCase(x.left);
			}

			// iii) Right Right Case (Mirror of case i)
			else if (x.parent.isRightChild() && x.isRightChild()) {
				rightRightCase(x);
			}

			// iv) Right Left Case (Mirror of case ii)
			else if (x.parent.isRightChild() && x.isLeftChild()) {
				if (x.parent == root) {
					root = rotateRight(x.parent);		
				} else {
					rotateRight(x.parent);
				}
				rightRightCase(x.right);
				
			}

		}
	}

	/**
	 * Rotates the given subtree to the right.
	 * 
	 * @param x the subtree
	 * @return the right rotated subtree
	 */
	private NodeRB<K, V> rotateRight(NodeRB<K, V> x) {
		NodeRB<K, V> xParent = x.parent;
		
		NodeRB<K, V> y = x.left;
		x.left = y.right;
		y.right = x;
		
		// Parents relations fix
		if (xParent != null) {
			if (x.isLeftChild()) {
				xParent.left = y;
			} else {
				xParent.right = y;
			}
		}	
		x.parent = y;
		y.parent = xParent;
		if (x.left != null) {
			x.left.parent = x;
		}
		
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
	private NodeRB<K, V> rotateLeft(NodeRB<K, V> x) {
		NodeRB<K, V> xParent = x.parent;
		
		NodeRB<K, V> y = x.right;
		x.right = y.left;
		y.left = x;
		
		// Parents relations fix
		if (xParent != null) {
			if (x.isLeftChild()) {
				xParent.left =  y;
			} else {
				xParent.right = y;
			}
		}
		
		x.parent = y;
		y.parent = xParent;
		if (x.right != null) {
			x.right.parent = x;
		}
		
		x.height = 1 + Math.max(height(x.left), height(x.right));
		y.height = 1 + Math.max(height(y.left), height(y.right));
		return y;
	}

	// Wrapper over print2DUtil()
	public void print2D() {
		// Pass initial space count as 0
		print2DUtil(root, 0);
	}

	public String string2DUtil(NodeRB<K, V> root, int space, String s) {
		int count = 5;
		// Base case
		if (root == null)
			return "";

		// Increase distance between levels
		space += count;

		// Process right child first
		print2DUtil(root.right, space);

		// Print current node after space
		// count
		for (int i = count; i < space; i++)
			s += " ";
		s += root.values + "\n";

		// Process left child
		return string2DUtil(root.left, space, s);
	}

	public void print2DUtil(NodeRB<K, V> root, int space) {
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
		System.out.print(root.values + " " + ((root.color == false) ? "Black" : "Red") + "\n");

		// Process left child
		print2DUtil(root.left, space);
	}
}