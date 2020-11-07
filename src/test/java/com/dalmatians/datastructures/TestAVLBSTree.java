package com.dalmatians.datastructures;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

public class TestAVLBSTree<K extends Comparable<K>, V> extends BinarySearchTree<K, V> {
	
	private AVLBSTree<Integer, Integer> tree;
	
	public void emptySetup() {
		tree = new AVLBSTree<Integer, Integer>();
	}
	
	public void nonEmptySetup() {
		tree = new AVLBSTree<Integer, Integer>();
		tree.add(8, 8);
		tree.add(25, 25);
		tree.add(5, 5);
		tree.add(17, 17);
		tree.add(32, 32);
	}

	@Test
	public void testAddEmpty() {
		emptySetup();
		tree.add(1, 1);
		assertTrue(tree.root.key == 1);
		tree.add(2, 2);
		assertTrue(tree.root.right.key == 2);
		tree.add(3, 3);
		assertTrue(tree.root.key == 2);
		assertTrue((tree.root.left.key == 1) && (tree.root.right.key == 3));
		assertTrue(check());
	}
	
	@Test
	public void testAddNonEmpty() {
		nonEmptySetup();
		assertTrue(tree.inorder().equals(Arrays.asList(new Integer[] {5, 8, 17, 25, 32})));
		assertTrue(tree.preorder().equals(Arrays.asList(new Integer[] {8, 5, 25, 17, 32})));
		assertTrue(tree.postorder().equals(Arrays.asList(new Integer[] {5, 17, 32, 25, 8})));
		tree.add(12, 12);
		tree.add(40, 40);
		assertTrue(tree.inorder().equals(Arrays.asList(new Integer[] {5, 8, 12, 17, 25, 32, 40})));
		assertTrue(tree.preorder().equals(Arrays.asList(new Integer[] {17, 8, 5, 12, 32, 25, 40})));
		assertTrue(tree.postorder().equals(Arrays.asList(new Integer[] {5, 12, 8, 25, 40, 32, 17})));
		assertTrue(check());
	}

	@Test
	public void testDeleteEmpty() {
		emptySetup();
		tree.add(1, 1);
		tree.delete(1);
		assertTrue(tree.root == null);
		tree.add(25, 25);
		tree.add(38, 38);
		tree.add(10, 10);
		assertTrue(tree.root.right.key == 38);
		tree.delete(38);
		assertTrue(tree.root.right == null);
		assertTrue(tree.root.key == 25);
		tree.delete(25);
		assertTrue(tree.root.key == 10);
		assertTrue(check());
	}
	
	@Test
	public void testDeleteNonEmpty() {
		nonEmptySetup();
		assertTrue(tree.root.key == 8);
		tree.delete(8);
		assertTrue(tree.root.key == 17);
		assertTrue(tree.height() == 2);
		tree.delete(5);
		assertTrue(tree.height() == 1);
		assertTrue(tree.root.key == 25);
		assertTrue(check());
	}
	
	/**
     * Checks if the AVL tree invariants are fine.
     * 
     * @return {@code true} if the AVL tree invariants are fine
     */
    private boolean check() {
        if (!isBST()) System.out.println("Symmetric order not consistent");
        if (!isAVL()) System.out.println("AVL property not consistent");
        if (!isSizeConsistent()) System.out.println("Subtree counts not consistent");
        return isBST() && isAVL() && isSizeConsistent();
    }
    
    /**
     * Checks if AVL property is consistent.
     * 
     * @return {@code true} if AVL property is consistent.
     */
    private boolean isAVL() {
        return isAVL(tree.root);
    }

    /**
     * Checks if AVL property is consistent in the subtree.
     * 
     * @param x the subtree
     * @return {@code true} if AVL property is consistent in the subtree
     */
    private boolean isAVL(BinarySearchTree<Integer, Integer>.Node<Integer, Integer> x) {
        if (x == null) return true;
        int bf = tree.balanceFactor(x);
        if (bf > 1 || bf < -1) return false;
        return isAVL(x.left) && isAVL(x.right);
    }

	/**
     * Checks if the symmetric order is consistent.
     * 
     * @return {@code true} if the symmetric order is consistent
     */
    private boolean isBST() {
        return isBST(tree.root, null, null);
    }

    /**
     * Checks if the tree rooted at x is a BST with all keys strictly between
     * min and max (if min or max is null, treat as empty constraint) Credit:
     * Bob Dondero's elegant solution
     * 
     * @param x the subtree
     * @param min the minimum key in subtree
     * @param max the maximum key in subtree
     * @return {@code true} if if the symmetric order is consistent
     */
    private boolean isBST(BinarySearchTree<Integer, Integer>.Node<Integer, Integer> x, Integer min, Integer max) {
        if (x == null) return true;
        if (min != null && x.key.compareTo(min) <= 0) return false;
        if (max != null && x.key.compareTo(max) >= 0) return false;
        return isBST(x.left, min, x.key) && isBST(x.right, x.key, max);
    }

    /**
     * Checks if size is consistent.
     * 
     * @return {@code true} if size is consistent
     */
    private boolean isSizeConsistent() {
        return isSizeConsistent(root);
    }

    /**
     * Checks if the size of the subtree is consistent.
     * 
     * @return {@code true} if the size of the subtree is consistent
     */
    private boolean isSizeConsistent(Node<K, V> x) {
        if (x == null) return true;
        if (x.height != height(x.left) + height(x.right) + 1) return false;
        return isSizeConsistent(x.left) && isSizeConsistent(x.right);
    }

}
