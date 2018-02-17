package team.wt.quinemccluskey;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

/**
 * This program minimizes boolean function using Quine-McCluskey algorithm. This
 * includes a GUI for the user to input the minterms and to show the solution as
 * well as the minimized boolean function.
 * 
 * @author Lorenz Timothy Barco Ranera and Waleed Cruz Occidental
 *
 */

public class QuineMcCluskey {

	/**
	 * Contains the variables.
	 */
	private static String variables;

	/**
	 * Number of variables.
	 */
	private static int noOfVar;

	/**
	 * List of minterms.
	 */
	private static ArrayList<String> minterms = new ArrayList<>();

	/**
	 * ArrayList of the table instances. Stores every table made.
	 */
	private static ArrayList<Table> table = new ArrayList<>();

	/**
	 * ArrayList of the chart instances.
	 */
	private static ArrayList<Chart> chart = new ArrayList<>();

	/**
	 * List of all prime implicants.
	 */
	private static ArrayList<Implicant> primeImplicants = new ArrayList<>();

	/**
	 * List of all essential prime implicants.
	 */
	private static ArrayList<Implicant> essentialPrimeI = new ArrayList<>();

	/**
	 * List of all possible essential prime implicants.
	 */
	private static ArrayList<ArrayList<String>> possibleEssentialP = new ArrayList<>();

	/**
	 * Implements the Quine-McCluskey algorithm to minimize boolean function.
	 * 
	 * @param input
	 *            list of variables and minterms entered by the user
	 * @throws Exception
	 *             when the input is invalid
	 */
	public static void solveQMC(String input) throws Exception {

		StringBuilder solution = new StringBuilder();
		variables = input.substring(input.indexOf("(") + 1, input.indexOf(")"));
		noOfVar = variables.length();
		String[] temp_minterms = input.substring(input.lastIndexOf("(") + 1, input.lastIndexOf(")")).split(",");
		minterms = new ArrayList<String>(Arrays.asList(temp_minterms));
		minterms = sortMinterms();

		solution.append(" \n NOTE: Without '*' means it is a Prime Implicant \n");

		findPrimeImplicant();

		for (int i = 0; i < table.size(); i++) {
			solution.append("\n  Implicants (Order " + i + ") \n");
			solution.append(table.get(i).printAll());
			solution.append(" \n");
		}

		solution.append(
				"=============================================================================================================================================================");
		solution.append("\n");
		solution.append("\n Get Prime Implicants: \n");
		for (int i = 0; i < primeImplicants.size(); i++) {
			solution.append(
					"\n   " + primeImplicants.get(i).getImplicant() + "\t\t  " + primeImplicants.get(i).getMinterm());

		}
		solution.append("\n");
		solution.append(
				"===============================================================================================================================");
		solution.append("\n");
		makeChart();

		for (int i = 0; i < chart.size(); i++) {
			solution.append("\n\n");
			solution.append(chart.get(i).draw());
			;
			solution.append("  Get Essential Prime Implicants (" + (i + 1) + ")\n");
			for (int j = 0; j < chart.get(i).getEP().size(); j++) {
				solution.append("   " + chart.get(i).getEP().get(j).getImplicant() + "\t\t  "
						+ chart.get(i).getEP().get(j).getMinterm() + "\n");
			}
			solution.append(
					"\n\n=============================================================================================================================================================");
		}
		solution.append("\n\n");

		if (chart.get(chart.size() - 1).doesItNeedPetricks()) {
			solution.append("Petrick's Method: \n\n");

			ArrayList<String> nep = chart.get(chart.size() - 1).getNEP();
			int r = 1;
			Boolean end = true;
			do {
				possibleEssentialP.clear();
				combination(nep, new String[r], 0, nep.size() - 1, 0, r);

				for (int i = 0; i < possibleEssentialP.size(); i++) {
					if (check(possibleEssentialP.get(i), chart.get(chart.size() - 1).getVarNotInNEP())) {
						for (int j = 0; j < possibleEssentialP.get(i).size(); j++) {
							solution.append(possibleEssentialP.get(i).get(j));
							Implicant temp = new Implicant(noOfVar, variables, possibleEssentialP.get(i).get(j));
							essentialPrimeI.add(temp);
						}
						end = false;
						break;
					}
				}
				r++;
			} while (r <= nep.size() && end);

		}

		UserInterface.showSolution(solution.toString(), findBooleanFunction());
	}

	/**
	 * Finds all the prime implicants and stores them in a table.
	 * 
	 * @see Table
	 */
	private static void findPrimeImplicant() {
		ArrayList<Implicant> order = new ArrayList<>();
		for (int i = 0; i < minterms.size(); i++) {
			Implicant temp = new Implicant(noOfVar, variables, minterms.get(i));
			order.add(temp);
		}

		Table aTable = new Table(order, noOfVar, variables);
		int end = 0;

		do {
			table.add(aTable);
			Table prevTable = aTable;
			order = prevTable.getNewImplicants();
			end = order.size();
			Table newTable = new Table(order, noOfVar, variables);
			aTable = newTable;
		} while (end != 0);

		for (int i = 0; i < table.size(); i++) {
			primeImplicants.addAll(table.get(i).getPrimeImplicants());
		}
	}

	/**
	 * Makes the chart to find prime implicants.
	 * 
	 * @see Chart
	 * 
	 */
	private static void makeChart() {
		ArrayList<String> column = minterms;
		ArrayList<String> row = new ArrayList<>();

		for (int i = 0; i < primeImplicants.size(); i++) {
			row.add(primeImplicants.get(i).getImplicant());
		}

		Boolean end = true;
		do {

			Chart order = new Chart(row, column, noOfVar, variables);
			chart.add(order);
			row = simplify(order.getNEP(), order.getVarNotInNEP());
			column = order.getVarNotInNEP();

			if (order.getEP().size() == 0) {
				end = false;
			} else {
				essentialPrimeI.addAll(order.getEP());
			}

		} while (column.size() != 0 && row.size() != 0 && end);
	}

	/**
	 * Constructs the boolean function.
	 * 
	 * @return the minimized boolean function
	 */
	public static String findBooleanFunction() {
		String function = "";

		for (int i = 0; i < essentialPrimeI.size(); i++) {
			if (i == 0) {
				function = essentialPrimeI.get(i).getMinterm();
			} else {
				function = function + " + " + essentialPrimeI.get(i).getMinterm();
			}
		}

		return (function);
	}

	/**
	 * Simplifies the list of implicants by removing those implicants whose
	 * variables are already included on other implicants
	 * 
	 * @param nEP
	 *            Prime Implicants that are not Essential
	 * @param varNotInEP
	 *            Minterms that are not in the Essential Prime Implicants
	 * @return a simplified Essential Prime Implicants which doesn't contain
	 *         redundant prime implicants. For example (6,7),(11,15),(7,15) and
	 *         the Minterms are 7,15. (6,7) and (7,15) will be removed because
	 *         they are like the subset of (7,15).
	 */

	public static ArrayList<String> simplify(ArrayList<String> nEP, ArrayList<String> varNotInEP) {
		ArrayList<String> simplified = new ArrayList<>();
		ArrayList<Integer> indextoremove = new ArrayList<>();

		ArrayList<ArrayList<String>> a = new ArrayList<>();

		for (int i = 0; i < nEP.size(); i++) {
			ArrayList<String> b = new ArrayList<>();
			String[] temp = nEP.get(i).split(",");
			for (int j = 0; j < temp.length; j++) {
				for (int k = 0; k < varNotInEP.size(); k++) {
					if (temp[j].equals(varNotInEP.get(k))) {
						b.add(varNotInEP.get(k));
					}
				}
			}

			Collections.sort(b);
			a.add(b);

		}

		for (int i = 0; i < a.size(); i++) {
			for (int j = 0; j < a.size(); j++) {
				if (i != j) {
					if (a.get(i).size() == 0) {
						indextoremove.add(i);
					} else if (a.get(j).size() == 0) {
						indextoremove.add(j);
					} else if (a.get(j).size() == a.get(i).size()) {
						if (a.get(j).containsAll(a.get(i))) {
							if (i < j) {
								indextoremove.add(i);
							} else {
								indextoremove.add(j);
							}
						}
					} else if (a.get(i).size() < a.get(j).size()) {
						if (a.get(j).containsAll(a.get(i))) {
							indextoremove.add(i);
						}
					}
				}
			}
		}

		for (int i = 0; i < nEP.size(); i++) {
			if (!indextoremove.contains(i)) {
				simplified.add(nEP.get(i));
			}
		}

		return simplified;
	}

	/**
	 * Mimic's the principle of Petrick's method. It creates a list of
	 * combination of the remaining prime Implicants form choose 1 to choose k
	 * and it stops if a combination that satisfies all columns is generated.
	 *
	 * @param nep
	 *            Prime Implicants that are not essential
	 * @param item
	 *            this stores a particular combination
	 * @param start
	 *            the start index of item
	 * @param end
	 *            the end index of item
	 * @param index
	 *            the index of item
	 * @param r
	 *            number of objects to be taken from a population
	 */

	public static void combination(ArrayList<String> nep, String item[], int start, int end, int index, int r) {
		ArrayList<String> possibleEP = new ArrayList<String>();
		if (index == r) {
			for (int j = 0; j < r; j++) {
				possibleEP.add(item[j]);
			}
			possibleEssentialP.add(possibleEP);
			return;
		}

		for (int i = start; i <= end && end - i + 1 >= r - index; i++) {
			item[index] = nep.get(i);
			combination(nep, item, i + 1, end, index + 1, r);
		}
	}

	/**
	 * It checks if a certain combination can fulfill the remaining minterms
	 * 
	 * @param pEP
	 *            the possible essential prime implicants
	 * @param varNotInEP
	 *            Minterms that are not the essential prime implicants
	 * @return true if success
	 */

	public static Boolean check(ArrayList<String> pEP, ArrayList<String> varNotInEP) {
		ArrayList<String> collectVarsInEP = new ArrayList<>();
		for (int i = 0; i < pEP.size(); i++) {
			String[] temp = pEP.get(i).split(",");
			for (int j = 0; j < temp.length; j++) {
				collectVarsInEP.add(temp[j]);
			}
		}

		if (collectVarsInEP.containsAll(varNotInEP)) {
			return true;
		}

		return false;
	}

	/**
	 * Removes repetition in an arraylist
	 * 
	 * @param list
	 *            an arraylist of string
	 * @return the list without repetitions
	 */
	public static ArrayList<String> removeRepetition(ArrayList<String> list) {
		HashSet<String> hs = new HashSet<>();
		hs.addAll(list);
		list.clear();
		list.addAll(hs);

		return list;
	}

	/**
	 * Sorts the minterms.
	 * 
	 * @return sorted minterms
	 */
	private static ArrayList<String> sortMinterms() {
		ArrayList<Integer> intMinTerms = new ArrayList<Integer>();
		for (int i = 0; i < minterms.size(); i++) {
			intMinTerms.add(Integer.parseInt(minterms.get(i)));
		}
		for (int i = 0; i < intMinTerms.size(); i++) {
			for (int j = i + 1; j < intMinTerms.size(); j++) {
				if (intMinTerms.get(j) < intMinTerms.get(i)) {
					int temp = intMinTerms.get(i);
					intMinTerms.set(i, intMinTerms.get(j));
					intMinTerms.set(j, temp);
				}
			}
		}
		ArrayList<String> sortedMinterms = new ArrayList<String>();
		for (int i = 0; i < intMinTerms.size(); i++) {
			sortedMinterms.add(intMinTerms.get(i).toString());
		}

		return sortedMinterms;
	}

	/**
	 * Clears all fields.
	 */
	public static void clearAll() {
		minterms.clear();
		table.clear();
		chart.clear();
		primeImplicants.clear();
		essentialPrimeI.clear();
		possibleEssentialP.clear();
		noOfVar = 0;
		variables = "";
	}
}
