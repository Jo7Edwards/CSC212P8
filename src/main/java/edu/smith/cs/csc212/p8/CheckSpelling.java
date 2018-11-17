package edu.smith.cs.csc212.p8;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

//import edu.smith.cs.csc212.p3.WordSplitter;

/*
 * Cite: https://docs.oracle.com/javase/7/docs/api/java/lang/System.html#nanoTime() (for help with System.nanoTime())
 * https://stackoverflow.com/questions/4685450/why-is-the-result-of-1-3-0
 * https://alvinalexander.com/java/java-strip-characters-string-letters-numbers-replace
 * https://piazza.com/class/jlmq9jjkmz53xv?cid=198
 */



public class CheckSpelling {
	/**
	 * Read all lines from the UNIX dictionary.
	 * @return a list of words!
	 */
	public static List<String> loadDictionary() {
		long start = System.nanoTime();
		List<String> words;
		try {
			words = Files.readAllLines(new File("src/main/resources/words").toPath());
		} catch (IOException e) {
			throw new RuntimeException("Couldn't find dictionary.", e);
		}
		long end = System.nanoTime();
		double time = (end - start) / 1e9;
		System.out.println("Loaded " + words.size() + " entries in " + time +" seconds.");
		return words;
	}
	
	
	/**
	 * This method looks for all the words in a dictionary.
	 * @param words - the "queries"
	 * @param dictionary - the data structure.
	 */
	public static void timeLookup(List<String> words, Collection<String> dictionary) {
		long startLookup = System.nanoTime();
		
		int found = 0;
		for (String w : words) {
			if (dictionary.contains(w)) {
				found++;
			}
		}
		
		long endLookup = System.nanoTime();
		double fractionFound = found / (double) words.size();
		double timeSpentPerItem = (endLookup - startLookup) / ((double) words.size());
		int nsPerItem = (int) timeSpentPerItem;
		System.out.println(dictionary.getClass().getSimpleName()+": Lookup of items found="+ fractionFound+" (" + fractionFound*100+ "%), time="+nsPerItem+" ns/item");
		
	}
	
	/**
	 * Checks what ratio of words in the book are misspelled. Anything not found in the given dictionary will count as a misspell. 
	 * Prints out what the ratio of words misspelled is and the time it takes to figure it out whether it's misspelled or not in nanoseconds/item 
	 * 
	 * @param book - takes a File book 
	 * @param dictionary - takes a Collection<String> that should comprise a dictionary, which you will use to check if the book's words are in the dictionary (spelled correctly) or not (misspelled)
	 */
	public static void checkBookSpelling(File book, Collection<String> dictionary) {
		
		//Need to read through book file to get a list of words in the book
		List<String> bookWords = new ArrayList<>();
		try {

			BufferedReader reader = new BufferedReader(new FileReader(book));
			
			String line;
			
			while(true) {
				line = reader.readLine();
				if (line == null) {
					break;
				}
				List<String> lineWords = WordSplitter.splitTextToWords(line);
				for (String w: lineWords) {
					bookWords.add(w);
				}	
			}	reader.close();	
			
		} catch (FileNotFoundException e) {
			System.err.println("Couldn't find the txt");
			System.exit(-1);
		} catch (IOException e) {
			System.err.println("Couldn't read the txt");
			System.exit(-1);
		}
		
		//now that we have a list of words in the book, check which ones are misspelled 
		
		int misspelled = 0;
		int found = 0;
		List<String> bookWords2 = new ArrayList<>();
		
		/*
		 * This takes all the words in the book and takes out any characters that 
		 * aren't letters, because if you don't do this then CharTrie will have problems
		 */
		for (String w : bookWords) {
			String newString = w.replaceAll("[^a-zA-Z]", "");
			bookWords2.add(newString);
		}
		
		long start = System.nanoTime();
		for (String w : bookWords2) {
			if (dictionary.contains(w)) {
				found++;
			} else {
				misspelled++;
				//To check what misspelled words there are, just put a print w here
			}
		}
		long end = System.nanoTime(); 
		
		//since it's a fraction <1 need to cast float
		float missRatio = (float) misspelled / (float) bookWords2.size();
		
		double timeSpentPerItem = (end - start) / ((double) bookWords2.size());
		System.out.println(dictionary.getClass().getSimpleName()+ ": has this many ratio misspelled: " + missRatio + " & time is:" + timeSpentPerItem);
	}
	
	/**
	 * This creates a data set that can have some amount of words that will not 
	 * be in the dictionary. 
	 * @param listOfWords - List<String> of all words in the dictionary 
	 * @param num - how many words should be in the data set
	 * @param fraction - ratio that should be misspelled
	 * @return a List<String> of the specified size and ratio of misspelled words
	 */
	public static List<String> createMixedDataset(List<String> listOfWords, int num, double fraction) {
		List<String> words = new ArrayList<>();
		//amount of misspelled words there should be
		double numOfMisses = num * fraction;
		
		for (int i=0; i <=num; i++) {
			if (i <= numOfMisses) { //make some amount fake words by adding xyz to the end of the word
				words.add(listOfWords.get(i) + "xyz");
			} else {
				words.add(listOfWords.get(i)); //These should be real words
			}
		}
		
		return words;
	}
	
	
	
	
	
	
	public static void main(String[] args) {
		// --- Load the dictionary.
		List<String> listOfWords = loadDictionary();
		
		//---------------------------------------------------------------------------------------------------------------------------
		// --- Create a bunch of data structures for testing:
		
		// ((Measure insertion)) Section!
		System.out.println("\n");
		
		//Measure TreeSet insertion time
		long startTree = System.nanoTime();
		TreeSet<String> treeOfWords = new TreeSet<>(listOfWords);
		long endTree = System.nanoTime();
		long insertTimeTree = endTree-startTree;
		System.out.println("TreeSet fill/insertion time!!: " + insertTimeTree + " nanoseconds, and " + insertTimeTree/1e9 + " seconds!");
		
		//Measure HashSet insertion time 
		long startHash = System.nanoTime();
		HashSet<String> hashOfWords = new HashSet<>(listOfWords);
		long endHash = System.nanoTime();
		long insertTimeHash = endHash-startHash;
		System.out.println("HashSet fill/insertion time!!: " + insertTimeHash + " nanoseconds, and " + insertTimeHash/1e9 + " seconds!");
		
		//Measure SortedStringListSet insertion time
		long startSortedString = System.nanoTime();
		SortedStringListSet bsl = new SortedStringListSet(listOfWords);
		long endSortedString = System.nanoTime();
		long insertTimeSorted = endSortedString - startSortedString;
		System.out.println("SortedStringListSet fill/insertion time!!: " + insertTimeSorted + " nanoseconds, and " + insertTimeSorted/1e9 + " seconds!");

		//Measure CharTrie insertion time
		long startChar = System.nanoTime();
		CharTrie trie = new CharTrie();
		for (String w : listOfWords) {
			trie.insert(w);
		}
		long endChar = System.nanoTime();
		long insertTimeChar = endChar - startChar;
		System.out.println("CharTrie fill/insertion time!!: " + insertTimeChar + " nanoseconds, and " + insertTimeChar/1e9 + " seconds!");

		//Measure LLHash insertion time
		long startLLHash = System.nanoTime();
		LLHash hm100k = new LLHash(100_000);
		for (String w : listOfWords) {
			hm100k.add(w);
		}
		long endLLHash = System.nanoTime();
		long insertTimeLLHash = endLLHash - startLLHash;
		System.out.println("LLHash fill/insertion time!!: " + insertTimeLLHash + " nanoseconds, and " + insertTimeLLHash/1e9 + " seconds!");

		System.out.println("\n");
		
		// --- Make sure that every word in the dictionary is in the dictionary:
		timeLookup(listOfWords, treeOfWords);
		timeLookup(listOfWords, hashOfWords);
		timeLookup(listOfWords, bsl);
		timeLookup(listOfWords, trie);
		timeLookup(listOfWords, hm100k);
		System.out.println("\n");
		
		//---------------------------------------------------------------------------------------------------------------------------
		
		// --- Create a data set of mixed hits and misses:
		/*
		 * ((Plot Query Speed)) section!
		 */
		System.out.println("\n");
		List<String> hitsAndMisses = new ArrayList<>();
		System.out.println("LOOKUP FOR hitsAndMisses WORDS: " );
		for (int i=0; i<11; i++) {
			double fraction = i / 10.0;
			hitsAndMisses = createMixedDataset(listOfWords, 1000, fraction);
			timeLookup(hitsAndMisses, treeOfWords);
			timeLookup(hitsAndMisses, hashOfWords);
			timeLookup(hitsAndMisses, bsl);
			timeLookup(hitsAndMisses, trie);
			timeLookup(hitsAndMisses, hm100k);
			System.out.println("\n");
		}
		System.out.println("\n");
		
		
		//---------------------------------------------------------------------------------------------------------------------------
		
		/*
		 * ((Spell check a project Gutenberg book)) section!
		 */
		
		String target = "src/main/resources/AliceInWonderland.txt";
		
		//initialize book file and do some preliminary checks on the file
		File book = new File(target);
		if (!book.canRead()) {
			System.err.println("Book can not be read: "+book);
			System.exit(-1);
		}
		if (!book.exists()) {
			System.err.println("Book does not exist: "+book);
			System.exit(-2);
		}
		if (!book.isFile()) {
			System.err.println("Book is not a file: "+book);
			System.exit(-3);
		}
		
		//check what words in the book are misspelled when compared the the dictionary of a certain type/data structure
		checkBookSpelling(book, treeOfWords);
		checkBookSpelling(book, hashOfWords);
		checkBookSpelling(book, bsl);
		checkBookSpelling(book, trie);
		checkBookSpelling(book, hm100k);
		
		
		
		//---------------------------------------------------------------------------------------------------------------------------
		
		// --- linear list timing:
		// Looking up in a list is so slow, we need to sample:
		System.out.println("Start of list: ");
		timeLookup(listOfWords.subList(0, 1000), listOfWords);
		System.out.println("End of list: ");
		timeLookup(listOfWords.subList(listOfWords.size()-100, listOfWords.size()), listOfWords);
		
	
		// --- print statistics about the data structures:
		System.out.println("Count-Nodes: "+trie.countNodes());
		System.out.println("Count-Items: "+hm100k.size());

		System.out.println("Count-Collisions[100k]: "+hm100k.countCollisions());
		System.out.println("Count-Used-Buckets[100k]: "+hm100k.countUsedBuckets());
		System.out.println("Load-Factor[100k]: "+hm100k.countUsedBuckets() / 100000.0);

		
		System.out.println("log_2 of listOfWords.size(): "+listOfWords.size());
		
		System.out.println("Done!");
	}
}
