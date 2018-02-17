package team.wt.quinemccluskey;

import java.util.Arrays;
/**
 * It stores all of the Implicants, its binary and minterm form and its status if its a prime Implicant or not
 * 
 * @author Waleed Cruz Occidental and Lorenz Timothy Barco Ranera
 *
 */
public class Implicant {
	/**
	 * The number of variables
	 */
	private int noOfVar;
	/**
	 * stores all of the variables
	 */
	private String variable;
	/**
	 * stores the implicant
	 */
	private String implicant;
	/**
	 * stores the binary value of the implicant
	 */
	private String binary;
	/**
	 * stores the minterm form
	 */
	private String minterm;

	public Implicant(int noOfVar, String variable, String implicant) {
		this.noOfVar = noOfVar;
		this.variable = variable;
		this.implicant = implicant;
	}

	/**
	 * converts an implicant to binary
	 */
	public void convertBin() { // convert binary
		String[] binOfEachVar = implicant.split(",");
		for (int i = 0; i < binOfEachVar.length; i++) {
			String binary = Integer.toBinaryString(Integer.parseInt(binOfEachVar[i]));
			int b = noOfVar - binary.length();
			char[] temp = new char[b];
			Arrays.fill(temp, '0');
			String leftPadString = new String(temp);
			binary = leftPadString + binary;
			binOfEachVar[i] = binary;
		}

		if (binOfEachVar.length == 1) {
			binary = binOfEachVar[0];
		} else {
			String binaryValue = "";
			for (int i = 0; i < noOfVar; i++) {
				Boolean temp = true;

				for (int j = 1; j < binOfEachVar.length; j++) {
					if (!(binOfEachVar[j - 1].charAt(i) == binOfEachVar[j].charAt(i))) {
						temp = false;
					}
				}

				if (temp) {
					binaryValue = binaryValue + binOfEachVar[0].charAt(i);
				} else {
					binaryValue = binaryValue + "-";
				}
			}

			binary = binaryValue;
		}
	}

	/**
	 * 
	 * @return the implicant
	 */
	public String getImplicant() { // return Implicant
		return implicant;
	}

	/**
	 * 
	 * @return the binary value of minterm
	 */
	public String getBinary() { // return binary
		convertBin();
		return binary;
	}

	/**
	 * 
	 * @return the number one in the binary value of the minterm
	 */
	public int countNumberOf1() { // counts the number of "1" in the binary of a
									// variable
		int count = 0;
		for (int k = 0; k < binary.length(); k++) {
			if (binary.charAt(k) == '1') {
				count++;
			}
		}

		return count;
	}

	/**
	 * convert minterm from binary value, example 1101 to ABC'D
	 */
	public void convertMin() {
		convertBin();

		String m = "";
		for (int i = 0; i < noOfVar; i++) {
			if (binary.charAt(i) == '1') {
				m = m + variable.charAt(i);
			} else if (binary.charAt(i) == '0') {
				m = m + variable.charAt(i) + "'";
			}
		}

		minterm = m;
	}

	/**
	 * 
	 * @return the minterm
	 */
	public String getMinterm() {
		convertMin();
		return minterm;
	}
}