package project;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * This class codifies messages in binary digits, with variable size
 * 
 * @author fc59858 Madalena Machado
 *
 */
public class Huffman {
	
	/**
	 * This method takes a string corpus and generates a Huffman encoding tree from it. 
	 * It then extracts the character codes from the tree and returns them as a HashMap.
	 * 
	 * @param corpus message to be coded
	 * @return a HashMap containing the Huffman codes for each character in the corpus
	 */
	public static HashMap<Character, String> getCodes(String corpus) {
		return treeFromCorpus(corpus).getHuffmanCodes();
	}
	
	/**
	 * This method converts a HashMap containing character codes into a string representation, with the codes sorted by character value.
	 * 
	 * @param codes a HashMap containing character codes
	 * @return a string representation of the character codes, sorted by character value
	 */
	public static String codesToString(HashMap<Character, String> codes) {
		StringBuilder codesRepresentation = new StringBuilder();
		
		SortedMap<Character, String> orderedCodes = new TreeMap<Character, String>(codes);

		for (Object entry : orderedCodes.entrySet()) {
			codesRepresentation.append(entry);
			codesRepresentation.append(System.lineSeparator());
		}
		return codesRepresentation.toString();		
	}
	/**
	 * This method returns a string representation of the encoded message
	 * 
	 * @param message text to be coded
	 * @param codes hashmap containing the code for each character from the text
	 * @return string representation of the encoded message
	 */
	public static String encode(String message, HashMap<Character, String> codes) {
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < message.length(); i++) {
			if(codes.containsKey(message.charAt(i))) {
				sb.append(codes.get(message.charAt(i)));
			}
			else
				sb.append("-");
			
		}
		return sb.toString();
		
	}
	
	/**
	 * This method makes a tree from the given code
	 * 
	 * @param corpus code
	 * @return tree representation from the given code
	 */
	private static HuffmanTree treeFromCorpus(String corpus) {
		return new HuffmanTree(frequencyTable(corpus));
	}
	 /**
	  * This method makes a frequency table out of the message to be coded. Calculates how many times a 
	  * character appears on the given string and stores it on the hashmap.
	  * 
	  * @param corpus text message
	  * @return a frequency table with the amount of times a character appears
	  */
	private static HashMap<Character, Integer> frequencyTable(String corpus) {
		HashMap <Character, Integer> map = new HashMap<>();
		for(int i = 0; i < corpus.length(); i++) {
			Character c = corpus.charAt(i);

			if(map.containsKey(c)) {
				map.put(c, map.get(c)+1);
			} else {
				map.put(c, 1);
			}	
		}
		return map;
	}
	
	/**
	 * This private class generates a tree
	 */
	private static class HuffmanTree {

		private HuffmanNode root;
		
		/**
		 * This constructor creates a Huffman encoding tree from a HashMap containing 
		 * the frequency of each character in the text message.
		 * 
		 * @param frequencies a HashMap containing the frequency of each character in the text message
		 */
		private HuffmanTree(HashMap<Character, Integer> frequencies) {
			PriorityQueue<HuffmanNode> nodes = new PriorityQueue<>();

			for(Map.Entry<Character, Integer> set: frequencies.entrySet()) {
				Character c = set.getKey();
				int frequency = set.getValue();
				
				HuffmanNode node = new HuffmanNode(frequency,c);
				nodes.add(node);
			}
			while(nodes.size() >= 2) {
				int freq1 = nodes.peek().frequency;
				HuffmanNode node1 = nodes.peek();
				nodes.remove();
				
				int freq2 = nodes.peek().frequency;
				HuffmanNode node2 = nodes.peek();
				nodes.remove();
				
				HuffmanNode dad = new HuffmanNode(freq1+freq2, node1, node2);
				nodes.add(dad);
			}
			root = nodes.peek();
			nodes.remove();	
		}
		/**
		 * This method generates and returns a HashMap containing the Huffman codes for each character in the encoding tree.
		 * 
		 * @return  a HashMap containing the Huffman codes for each character in the encoding tree
		 */
		private HashMap<Character, String> getHuffmanCodes() {
			HashMap<Character, String> codes = new HashMap<>();

			getHuffmanCodesAux(root, "", codes);

			return codes;
		}
		
		/**
		 * This method generates huffman codes out of the huffman root node
		 * 
		 * @param node current node being traversed
		 * @param code code generated
		 * @param codes  a HashMap containing the Huffman codes for each character
		 */
		private void getHuffmanCodesAux(HuffmanNode node, String code, HashMap<Character, String> codes) {
			//fazer com recursão 
			if(node.isLeaf()){
				codes.put(node.c, code);
			}
			else {
				getHuffmanCodesAux(node.left, code + "0" , codes);
				getHuffmanCodesAux(node.right, code + "1" , codes);
			}		
		}
	}
	
	/**
	 * This class generates a huffman node
	 */
	private static class HuffmanNode implements Comparable<HuffmanNode> {

		int frequency;
		char c;
		HuffmanNode left;
		HuffmanNode right;

		/**
		 * This method creates a leaf
		 * @param frequency the character frequency
		 * @param c given character
		 */
		private HuffmanNode(int frequency, char c) {
			this.frequency = frequency;
			this.c = c;
			// this.left not initialized; remains null
			// this.right not initialized; remains null
		}

		/**
		 * This method creates an internal node
		 * 
		 * @param frequency amount of times it appears in the string
		 * @param left    left huffman node
		 * @param right   right huffman node
		 */
		private HuffmanNode(int frequency, HuffmanNode left, HuffmanNode right) {
			this.frequency = frequency;
			// no need to initialize this.c, because it is not used
			this.left = left;
			this.right = right;
		}
		
		/**
		 * This method verifies if a node is on a leaf position
		 * 
		 * @return true if node is leaf and false otherwise
		 */
		private boolean isLeaf() {
			return left == null && right == null;
		}

		@Override
		public int compareTo(HuffmanNode node) {
			return this.frequency - node.frequency;
		}
	}
}
