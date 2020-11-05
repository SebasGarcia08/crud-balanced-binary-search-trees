package com.dalmatians.datastructures;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

public class TestAVLBSTree<K extends Comparable<K>, V> extends BinarySearchTree<K, V>{
	
	AVLBSTree<Integer, Integer> tree;
	
	public void emptySetup() {
		tree = new AVLBSTree<Integer, Integer>();
	}
	
	public void nonEmptySetup() {
		tree = new AVLBSTree<Integer, Integer>();
		tree.add(1, 1);
		tree.add(2, 2);
		tree.add(3, 3);
		tree.add(4, 4);
		tree.add(5, 5);
	}

	@Test
	public void testAddEmpty() {
		emptySetup();
		tree.add(1, 1);
		assertTrue(tree.root.value == 1);
		tree.add(2, 2);
		assertTrue(tree.root.right.value == 2);
		tree.add(3, 3);
		assertTrue(tree.root.value == 2);
		assertTrue((tree.root.left.value == 1) && (tree.root.right.value == 3));
	}
	
	@Test
	public void testAddNonEmpty() {
		nonEmptySetup();
		assertTrue(tree.inorder().equals(Arrays.asList(new Integer[] {1, 2, 3, 4, 5})));
		assertTrue(tree.preorder().equals(Arrays.asList(new Integer[] {2, 1, 4, 3, 5})));
		assertTrue(tree.postorder().equals(Arrays.asList(new Integer[] {1, 3, 5, 4, 2})));
		tree.add(6, 6);
		tree.add(7, 7);
		assertTrue(tree.inorder().equals(Arrays.asList(new Integer[] {1, 2, 3, 4, 5, 6, 7})));
		assertTrue(tree.preorder().equals(Arrays.asList(new Integer[] {4, 2, 1, 3, 6, 5, 7})));
		assertTrue(tree.postorder().equals(Arrays.asList(new Integer[] {1, 3, 2, 5, 7, 6, 4})));
	}

}
