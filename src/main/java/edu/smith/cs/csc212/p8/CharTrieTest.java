package edu.smith.cs.csc212.p8;

import org.junit.Assert;
import org.junit.Assert.*;

import org.junit.Test;

/**
 * JUnit test for CharTrie
 * @author J.M.Edwards
 *
 */
public class CharTrieTest {
	/**
	 * test .countNodes() method for CharTrie. Tests with CharTrie sizes 1-3, and when some prefixes are shared
	 */
	@Test
	public void testCountNodes() {
		CharTrie trie = new CharTrie();
		trie.insert("car");
		Assert.assertEquals(1, trie.size());
		Assert.assertEquals(4, trie.countNodes());
		
		trie.insert("apart");
		Assert.assertEquals(2, trie.size());
		Assert.assertEquals(9, trie.countNodes());
		
		trie.insert("care");
		Assert.assertEquals(3,  trie.size());
		Assert.assertEquals(10, trie.countNodes());
		
		
	}

}
