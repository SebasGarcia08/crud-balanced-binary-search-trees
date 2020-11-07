package com.dalmatians.datastructures;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

public class TestBinarySearchTree {
	
	private BinarySearchTree<Integer, Integer> tree;
	
	public void nonEmptySetup() {
		tree = new BinarySearchTree<Integer, Integer>();
		tree.add(20, 20);
		tree.add(15, 15);
		tree.add(8, 8);
		tree.add(28, 28);
		tree.add(32, 32);
		tree.add(17, 17);
		tree.add(23, 23);
	}
	
	@Test
	public void testLeftRotate() {
		nonEmptySetup();
		assertTrue(tree.root.left.key == 15);
		tree.root.left = tree.rotateLeft(tree.root.left);
		assertTrue(tree.root.left.key == 17);
		assertTrue(tree.root.key == 20);
		assertTrue(tree.preorder().equals(Arrays.asList(new Integer[] {20, 17, 15, 8, 28, 23, 32})));
		tree.root = tree.rotateLeft(tree.root);
		assertTrue(tree.root.key == 28);
		assertTrue(tree.preorder().equals(Arrays.asList(new Integer[] {28, 20, 17, 15, 8, 23, 32})));
	}
	
	@Test
	public void testRightRotate() {
		nonEmptySetup();
		assertTrue(tree.root.right.key == 28);
		tree.root.right = tree.rotateRight(tree.root.right);
		assertTrue(tree.root.right.key == 23);
		assertTrue(tree.root.key == 20);
		assertTrue(tree.preorder().equals(Arrays.asList(new Integer[] {20, 15, 8, 17, 23, 28, 32})));
		tree.root = tree.rotateRight(tree.root);
		assertTrue(tree.root.key == 15);
		assertTrue(tree.preorder().equals(Arrays.asList(new Integer[] {15, 8, 20, 17, 23, 28, 32})));
	}

}
