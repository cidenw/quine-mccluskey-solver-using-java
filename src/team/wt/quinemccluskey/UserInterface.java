package team.wt.quinemccluskey;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * The graphical user interface of the program.
 * 
 * @author Lorenz Timothy Barco Ranera and Waleed Cruz Occidental
 *
 */
public class UserInterface {

	/**
	 * The frame for the GUI.
	 */
	private JFrame frame;

	/**
	 * The text field for the user input.
	 */
	private JTextField inputField;

	/**
	 * Placeholder for the solution.
	 */
	private static JTextArea solutionTextArea;

	/**
	 * Placeholder for the minimized boolean function.
	 */
	private static JTextField answerField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UserInterface window = new UserInterface();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public UserInterface() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		solutionTextArea = new JTextArea();
		answerField = new JTextField();
		frame = new JFrame();
		frame.setBounds(100, 100, 557, 452);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setResizable(false);

		Panel panel = new Panel();
		panel.setBackground(Color.WHITE);
		panel.setBounds(0, 0, 551, 423);
		frame.getContentPane().add(panel);
		panel.setLayout(null);

		inputField = new JTextField();
		inputField.setBounds(10, 85, 521, 20);
		inputField.setFont(new Font("Tahoma", Font.PLAIN, 11));
		inputField.setForeground(Color.BLACK);
		inputField.setBackground(Color.WHITE);
		panel.add(inputField);
		inputField.setColumns(10);

		JLabel lblStrictFormatFabcd = new JLabel("STRICT Format: F(ABCD) = S(1,4,6,7,8,9,10,11,15) ");
		lblStrictFormatFabcd.setBounds(10, 60, 521, 14);
		lblStrictFormatFabcd.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblStrictFormatFabcd.setForeground(Color.BLACK);
		lblStrictFormatFabcd.setBackground(Color.MAGENTA);
		panel.add(lblStrictFormatFabcd);

		JLabel lblQuinemccluskey = new JLabel("Quine-McCluskey");
		lblQuinemccluskey.setBounds(200, 11, 165, 38);
		lblQuinemccluskey.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblQuinemccluskey.setForeground(Color.BLACK);
		panel.add(lblQuinemccluskey);

		JButton btnSolve = new JButton("Solve");
		btnSolve.setBounds(157, 116, 89, 23);
		btnSolve.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					QuineMcCluskey.solveQMC(inputField.getText());
				} catch (Exception e1) {
					inputError();
				}

			}
		});
		btnSolve.setFont(new Font("Tahoma", Font.PLAIN, 11));
		btnSolve.setBackground(Color.WHITE);
		btnSolve.setForeground(Color.BLACK);
		panel.add(btnSolve);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 150, 531, 237);
		panel.add(scrollPane);

		solutionTextArea.setBackground(Color.WHITE);
		solutionTextArea.setForeground(Color.BLACK);
		scrollPane.setViewportView(solutionTextArea);

		JLabel lblFx = new JLabel("F = ");
		lblFx.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblFx.setBackground(Color.WHITE);
		lblFx.setForeground(Color.BLACK);
		lblFx.setBounds(71, 398, 34, 14);
		panel.add(lblFx);

		answerField.setEditable(false);
		answerField.setForeground(Color.BLACK);
		answerField.setBackground(Color.WHITE);
		answerField.setBounds(115, 395, 320, 20);
		panel.add(answerField);
		answerField.setColumns(10);

		JButton btnClear = new JButton("Clear");
		btnClear.setFont(new Font("Tahoma", Font.PLAIN, 11));
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				inputField.setText("");
				solutionTextArea.setText("");
				answerField.setText("");
				QuineMcCluskey.clearAll();
			}
		});
		btnClear.setBackground(Color.WHITE);
		btnClear.setForeground(Color.BLACK);
		btnClear.setBounds(293, 116, 89, 23);
		panel.add(btnClear);

	}

	/**
	 * Opens a warning dialog when the input is invalid.
	 */
	public static void inputError() {
		JOptionPane optionPane = new JOptionPane("Invalid Input", JOptionPane.ERROR_MESSAGE);
		JDialog dialog = optionPane.createDialog("Error");

		optionPane.setBackground(Color.BLACK);
		optionPane.setForeground(Color.GREEN);
		dialog.getContentPane().setForeground(Color.GREEN);
		dialog.getContentPane().setBackground(Color.DARK_GRAY);
		dialog.setAlwaysOnTop(true);
		dialog.setVisible(true);
	}

	/**
	 * Puts the generated solution/answer on their respective placeholder on the
	 * GUI.
	 * 
	 * @param solution
	 *            generated by the Quine-McCluskey algorithm
	 * @param answer
	 *            the minimized boolean function
	 */
	public static void showSolution(String solution, String answer) {
		solutionTextArea.setText(solution);
		answerField.setText(answer);
	}
}
