package team.wt.quinemccluskey;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;
import java.util.*;

/**
 * It stores the Tabular form of the Implicants
 * @author Waleed Cruz Occidental and Lorenz Timothy Barco Ranera
 * 
 *
 */
public class Table {
	/**
	 * Number of variables
	 */
	private int noOfVar;
	/**
	 * The table
	 */
	private ArrayList<ArrayList<Hashtable<String, String>>> aTable = new ArrayList<>();
	/**
	 * stores the new Implicants produced from the table
	 */
	private ArrayList<Implicant> newImplicants = new ArrayList<>();
	/**
	 * stores all of the variables
	 */
	private String variables;
	/**
	 * stores all of the Prime Implicants in the table
	 */
	private ArrayList<Implicant> primeImplicants = new ArrayList<>();

	public Table(ArrayList<Implicant> implicants, int noOfVar, String variables) {
		this.variables = variables;
		this.noOfVar = noOfVar;

		for (int i = 0; i <= noOfVar; i++) {
			ArrayList<Hashtable<String, String>> temp = new ArrayList<>();
			for (int j = 0; j < implicants.size(); j++) {
				Hashtable<String, String> a = new Hashtable<String, String>();
				a.put("Implicant", implicants.get(j).getImplicant());
				a.put("Binary", implicants.get(j).getBinary());
				a.put("Minterm", implicants.get(j).getMinterm());
				a.put("isUsed", "0");

				if (implicants.get(j).countNumberOf1() == i) {
					temp.add(a);
				}
			}

			if (!temp.isEmpty()) {
				aTable.add(temp);
			}
		}

		process();
	}

	/**
	 * 
	 * @return size of table
	 */
	public int getSize() { // returns the size of the group
		return aTable.size();
	}

	/**
	 * from this table we will create a new table
	 */
	public void process() {
		ArrayList<String> impi = new ArrayList<>();

		for (int i = 0; i < getSize() - 1; i++) {
			for (int j = 0; j < aTable.get(i).size(); j++) {
				for (int k = 0; k < aTable.get(i + 1).size(); k++) {
					if (compare(aTable.get(i).get(j).get("Implicant"), aTable.get(i + 1).get(k).get("Implicant"))) {

						String[] temp1 = aTable.get(i).get(j).get("Implicant").split(",");
						String[] temp2 = aTable.get(i + 1).get(k).get("Implicant").split(",");

						int[] array1 = Arrays.stream(temp1).mapToInt(Integer::parseInt).toArray();
						int[] array2 = Arrays.stream(temp2).mapToInt(Integer::parseInt).toArray();

						ArrayList<Integer> temp3 = new ArrayList<>();
						for (int h = 0; h < array1.length; h++) {
							temp3.add(array1[h]);
						}
						for (int h = 0; h < array2.length; h++) {
							temp3.add(array2[h]);
						}

						Collections.sort(temp3);

						String toadd = "";

						for (int h = 0; h < temp3.size(); h++) {
							if (!((temp3.size() - 1) == h)) {
								toadd = toadd + temp3.get(h) + ",";
							} else {
								toadd = toadd + temp3.get(h);
							}
						}

						impi.add(toadd);
						aTable.get(i).get(j).put("isUsed", "1");
						aTable.get(i + 1).get(k).put("isUsed", "1");
					}
				}
			}
		}

		impi = removeRepitition(impi);

		for (int i = 0; i < impi.size(); i++) {
			Implicant temp = new Implicant(noOfVar, variables, impi.get(i));
			newImplicants.add(temp);
		}

		for (int i = 0; i < aTable.size(); i++) {
			for (int j = 0; j < aTable.get(i).size(); j++) {
				if (aTable.get(i).get(j).get("isUsed").equals("0")) {
					Implicant temp = new Implicant(noOfVar, variables, aTable.get(i).get(j).get("Implicant"));
					primeImplicants.add(temp);
				}
			}
		}
	}

	/**
	 * 
	 * @return new Implicants produced from processing the table
	 */
	public ArrayList<Implicant> getNewImplicants() {
		// process();
		return newImplicants;
	}

	/**
	 * 
	 * @return the prime implicants from the table
	 */

	public ArrayList<Implicant> getPrimeImplicants() {

		return primeImplicants;
	}

	/**
	 * 
	 * @return the table in string
	 */
	public String printAll() { // prints everything w/ binary value & minterm
		StringBuilder primeImp = new StringBuilder();
		for (int i = 0; i < aTable.size(); i++) {
			for (int j = 0; j < aTable.get(i).size(); j++) {
				if (aTable.get(i).get(j).get("isUsed").equals("1")) {
					primeImp.append("   *  " + aTable.get(i).get(j).get("Implicant") + "  ->  "
							+ aTable.get(i).get(j).get("Binary") + "  ->  " + aTable.get(i).get(j).get("Minterm"));
					primeImp.append("\n");
				} else {
					primeImp.append("      " + aTable.get(i).get(j).get("Implicant") + "  ->  "
							+ aTable.get(i).get(j).get("Binary") + "  ->  " + aTable.get(i).get(j).get("Minterm"));
					primeImp.append("\n");
				}
			}
		}
		return primeImp.toString();
	}

	/**
	 * 
	 * @param list
	 *            an arraylist of string
	 * @return a list without any repetition
	 */
	public ArrayList<String> removeRepitition(ArrayList<String> list) {
		Set<String> hs = new HashSet<>();
		hs.addAll(list);
		list.clear();
		list.addAll(hs);

		return list;
	}

	/**
	 * 
	 * @param a
	 *            a first number
	 * @param b
	 *            a second number
	 * @return checks the difference between two number if it can be expressed
	 *         in 2^x
	 */

	public Boolean checkDifference(int a, int b) {
		int c = findDifference(a, b);

		if ((c) < 1) {
			return false;
		}

		int d = 1;

		while (true) {
			if (d < c) {
				d = d * 2;
			} else {
				if (d > c) {
					return false;
				} else if (d == c) {
					return true;
				}
			}
		}
	}

	/**
	 * 
	 * @param a
	 *            a first number
	 * @param b
	 *            a second number
	 * @return the difference between two number
	 */
	public int findDifference(int a, int b) { // returns the difference of two
												// numbers
		return b - a;
	}

	/**
	 * 
	 * @param value1
	 *            an Implicant
	 * @param value2
	 *            another Implicant
	 * @return true if the implicant has only one bit difference
	 */
	public Boolean compare(String value1, String value2) {
		String[] a = value1.split(",");
		String[] b = value2.split(",");

		int c = findDifference(Integer.parseInt(a[0]), Integer.parseInt(b[0]));

		for (int i = 0; i < a.length; i++) {
			if (!checkDifference(Integer.parseInt(a[i]), Integer.parseInt(b[i]))) {
				return false;
			}

			if (c != findDifference(Integer.parseInt(a[i]), Integer.parseInt(b[i]))) {
				return false;
			}
		}

		return true;
	}

}
