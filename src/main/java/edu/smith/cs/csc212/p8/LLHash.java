package edu.smith.cs.csc212.p8;

import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * This is a simple HashSet that resolves collisions with a LinkedList.
 * All buckets are also in an *intrusive* Singly-Linked-List, for easier iteration.
 * @author jfoley
 *
 */
public class LLHash extends AbstractSet<String> {
	/**
	 * All the buckets, whether used or not.
	 */
	List<Bucket> buckets;
	/**
	 * The start of the linked list of used buckets.
	 */
	Bucket head;
	
	/**
	 * A LLHash is of a fixed-size.
	 * @param numBuckets - the number of buckets to create.
	 */
	public LLHash(int numBuckets) {
		buckets = new ArrayList<>(numBuckets);
		for (int i=0; i<numBuckets; i++) {
			buckets.add(new Bucket());
		}
		head = null;
	}
	

	/**
	 * Add a value to this LLHash, if it is new.
	 * @param h - the string to add.
	 * @return true, because we could add it.
	 */
	@Override
	public boolean add(String h) {
		int hash = Math.abs(h.hashCode());
		int index = hash % this.buckets.size();
		Bucket bin = this.buckets.get(index);
		
		// Store value, if it is new:
		if (!bin.values.contains(h)) {
			bin.values.add(h);
			
			// Keep track that we're using this bucket.
			// SLL.addFront(bin)
			if (bin.next == null) {
				bin.next = head;
				head = bin;
			}
			
			// we added it.
			return true;
		} else {
			// it was already in this set.
			return false;
		}
	}
	
	/**
	 * Check whether the object is already stored. (Pre-Java 5, should be String).
	 * @param obj - really a String.
	 * @return true if it is in the appropriate bucket, false if not.
	 */
	@Override
	public boolean contains(Object obj) {
		int hash = Math.abs(obj.hashCode());
		int index = hash % this.buckets.size();
		Bucket bin = this.buckets.get(index);
		return bin.values.contains(obj);
	}

	/**
	 * Let us for-loop over all the values.
	 * @return an iterator over a copy of the current items.
	 */
	@Override
	public Iterator<String> iterator() {
		ArrayList<String> items = new ArrayList<>();
		for (Bucket b = this.head; b!= null; b = b.next) {
			items.addAll(b.values);
		}
		return items.iterator();
	}

	/**
	 * Count up the number of items in this hash-set.
	 * @return the total number of stored strings.
	 */
	@Override
	public int size() {
		int count = 0;
		for (Bucket b = this.head; b!= null; b = b.next) {
			count += b.values.size();
		}
		return count;
	}
	
	/**
	 * TODO, count all the collisions.
	 * @return the number of buckets with more than one value.
	 */
	public int countCollisions() {
		return 0;
	}

	/**
	 * TODO, count all the buckets with a value.
	 * @return the number of buckets with any value.
	 */
	public int countUsedBuckets() {
		return 0;
	}
	
	/**
	 * This is a bucket! It is a node in a linked list with a value that has a list of the values that collided here.
	 */
	private static class Bucket {
		Bucket next = null;
		List<String> values = new LinkedList<>();
	}
}