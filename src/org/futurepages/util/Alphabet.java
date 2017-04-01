package org.futurepages.util;

public class Alphabet {

	private final String DIGITS;
	private final int BASE;

	public Alphabet(String elements) {
		this.DIGITS = elements;
		this.BASE = elements.length();
	}

	public String encode(int i) {

		if (i == 0) return String.valueOf(DIGITS.charAt(0));

		StringBuilder sb = new StringBuilder();
		while (i > 0) {
			sb.append(String.valueOf(DIGITS.charAt(i % BASE)));
			i = i / BASE;
		}
		sb.reverse();
		return sb.toString();
	}

	public int decode(String s) {
		int i = 0;

		for (int c = 0; c < s.length(); c++) {
			i = (i * BASE) + DIGITS.indexOf(s.charAt(c));
		}
		return i;
	}

	public boolean contains(char ch){
		for(int i = 0; i<DIGITS.length();i++){
			if(ch==DIGITS.charAt(i)){
				return true;
			}
		}
		return false;
	}

	public static void main(String[] args) {
//		Alphabet alphabet = new Alphabet("abcdefghijklmNOPQRSTUVWXYZ0987654321ABCDEFGHIJKLMnopqrstuvwxyz");
//		26+26+10
//		System.out.println(alphabet.DIGITS.length());
//		for(int i = 0; i<1000; i++){
//			System.out.print(i+": ");
//			System.out.println(alphabet.encode(i));
//		}
//		System.out.println(alphabet.decode("A")); //52
//		System.out.println(alphabet.decode("Tu")); //944
//		System.out.println(alphabet.decode("9")); //8
//		System.out.println(alphabet.decode("GG")); //427
//		System.out.println(alphabet.decode("Ym"));//99
	}

}
