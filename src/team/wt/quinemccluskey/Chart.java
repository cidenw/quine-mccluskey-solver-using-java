package team.wt.quinemccluskey;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
/**
 * It represents the chart of the Prime Implicants and all of the minterms in the function
 * 
 * @author Waleed Cruz Occidental and Lorenz Timothy Barco Ranera
 *
 */
public class Chart {
	/**
	 * List of Prime Implicants in the function
	 */
	private ArrayList<String> row = new ArrayList<>();
	/**
	 * List of Minterms in the function
	 */
	private ArrayList<String> column = new ArrayList<>();
	/**
	 * The chart 
	 */
	private int[][] chart;
	
	/**
	 * List of Essential Prime Implicants
	 */
	private ArrayList<String> essentialPrime = new ArrayList<>();
	/**
	 * List of not Essential Prime Implicants in an arraylist of Strings
	 */
	private ArrayList<String> notEssentialPrime = new ArrayList<>();
	/**
	 * List of Minterms included in the Essential Prime Implicants
	 */
	private ArrayList<String> varInEP = new ArrayList<>();
	/**
	 * List of Minterms not included in the Essential Prime Implicants
	 */
	private ArrayList<String> varNotInEP = new ArrayList<>();
    /**
     *List of Essential Prime Implicants in an arraylist of Implicants
     */
	private ArrayList<Implicant> ep = new ArrayList<>();

	/**
	 * 
	 * @param row
	 *            The Implicants
	 * @param column
	 *            The Minterms
	 * @param noOfVar
	 *            The number of variables
	 * @param variables
	 *            the variables
	 */

	public Chart(ArrayList<String> row, ArrayList<String> column, int noOfVar, String variables) {
		this.row = row;
		this.column = column;

		chart = new int[row.size()][column.size()];
		for (int i = 0; i < row.size(); i++) {
			Arrays.fill(chart[i], 0);
		}

		for (int i = 0; i < row.size(); i++) {
			for (int j = 0; j < column.size(); j++) {
				String[] temp = row.get(i).split(",");
				for (int k = 0; k < temp.length; k++) {
					if (temp[k].equals(column.get(j))) {
						chart[i][j] = 1;
					}
				}
			}
		}

		for (int i = 0; i < column.size(); i++) {
			int count = 0;
			int temp = 0;
			for (int j = 0; j < row.size(); j++) {
				if (chart[j][i] == 1) {
					count++;
					temp = j;

				}
			}

			if (count == 1) {
				essentialPrime.add(row.get(temp));
				String[] a = row.get(temp).split(",");
				for (int j = 0; j < a.length; j++) {
					if (column.contains(a[j])) {
						varInEP.add(a[j]);
					}
				}
			}
		}

		for (int i = 0; i < row.size(); i++) {
			if (!essentialPrime.contains(row.get(i))) {

				String[] temp = row.get(i).split(",");
				for (int j = 0; j < temp.length; j++) {
					if (!varInEP.contains(temp[j]) && column.contains(temp[j])) {
						varNotInEP.add(temp[j]);
					}
				}

				notEssentialPrime.add(row.get(i));
			}
		}

		varInEP = removeRepitition(varInEP);
		essentialPrime = removeRepitition(essentialPrime);
		notEssentialPrime = removeRepitition(notEssentialPrime);
		varNotInEP = removeRepitition(varNotInEP);

		for (int i = 0; i < essentialPrime.size(); i++) {
			Implicant temp = new Implicant(noOfVar, variables, essentialPrime.get(i));
			ep.add(temp);
		}
	}

	/**
	 * 
	 * @return draws a chart
	 */

	public String draw() {
		StringBuilder chartBuilder = new StringBuilder();
		chartBuilder.append("\t");
		for (int i = 0; i < column.size(); i++) {
			chartBuilder.append("\t" + column.get(i));
		}
		chartBuilder.append("\n");
		chartBuilder.append("------");
		for (int i = 0; i < column.size(); i++) {
			chartBuilder.append("--------------------------------");
		}
		chartBuilder.append("\n");

		for (int i = 0; i < row.size(); i++) { // draws the chartBuilder on the
												// console
			for (int j = 0; j < column.size(); j++) {
				if (j == 0) {
					chartBuilder.append(row.get(i) + "\t");
				}

				if (chart[i][j] == 1) {
					chartBuilder.append("\tx");
				} else {
					chartBuilder.append("\t");
				}
			}
			chartBuilder.append("\n");
		}

		chartBuilder.append("------");
		for (int i = 0; i < column.size(); i++) {
			chartBuilder.append("--------------------------------");
		}

		chartBuilder.append("\n\n");
		return chartBuilder.toString();
	}

	/**
	 * 
	 * @return Essential Prime Implicants
	 */
	public ArrayList<Implicant> getEP() {
		return ep;
	}

	/**
	 * 
	 * @return get the Prime Implicants that are not essential
	 */
	public ArrayList<String> getNEP() {
		return notEssentialPrime;
	}

	/**
	 * 
	 * @return get Minterms that are included in the Essential Prime Implicants
	 */
	public ArrayList<String> getVarInEP() {
		return varInEP;
	}

	/**
	 * 
	 * @return get Minterms that are not inlcluded in the Essential Prime
	 *         Implicants
	 */

	public ArrayList<String> getVarNotInNEP() {
		return varNotInEP;
	}

	/**
	 * 
	 * @return true if the function needs to use Petrick's method
	 */
	public Boolean doesItNeedPetricks() {
		if (essentialPrime.size() == 0 && notEssentialPrime.size() != 0 && varInEP.size() == 0) {
			return true;
		}

		return false;
	}

	/**
	 * 
	 * @param list
	 *            an arraylist of string
	 * @return a list without any repitition
	 */
	public static ArrayList<String> removeRepitition(ArrayList<String> list) {
		Set<String> hs = new HashSet<>();
		hs.addAll(list);
		list.clear();
		list.addAll(hs);

		return list;
	}
}
