package Client;

import java.awt.*;
import java.io.*;
import java.net.Socket;
import javax.swing.*;
import java.awt.event.*;

public class ServerConnection extends JFrame implements Runnable {
	private Socket server;
	private BufferedReader in;
	private PrintWriter out;
	private JButton startButton;
	private JButton quitButton;
	private JButton hintButton;
	private JTextField guess;
	private JTextArea result;
	private JTextArea textArea;
	private JLabel word;
	private JLabel hintLabel;
	private JLabel timer;
	private JLabel score;
	// private static int hint = 0;
//	private String playerScore;
//	private static int finishGame = 0;

	public ServerConnection(Socket s) throws IOException {

		server = s;
		in = new BufferedReader(new InputStreamReader(server.getInputStream()));
		out = new PrintWriter(server.getOutputStream(), true);
		Font font = new Font("Times", Font.BOLD, 15);
		Font font2 = new Font("Times", Font.ITALIC, 14);
		Font font3 = new Font("Times", Font.PLAIN, 16);

		JFrame frame = new JFrame("Word Guessing Game");

		textArea = new JTextArea(3, 10);
		textArea.setEditable(false);

		guess = new JTextField(30);

		result = new JTextArea(0, 0);
		result.setEditable(false);
		result.append("\n");
		result.setForeground(Color.BLUE);
		result.setFont(font);

		hintButton = new JButton("Hint");
		hintButton.setAlignmentX(Component.LEFT_ALIGNMENT);

		quitButton = new JButton("Quit");
		quitButton.setAlignmentX(Component.RIGHT_ALIGNMENT);

		score = new JLabel();
		score.setFont(font3);
		score.setForeground(Color.BLACK);
		score.setAlignmentX(Component.CENTER_ALIGNMENT);
		// score.setAlignmentX(Component.LEFT_ALIGNMENT);

		word = new JLabel();
		word.setFont(font);
		word.setForeground(Color.BLACK);
		word.setAlignmentX(Component.CENTER_ALIGNMENT);

		hintLabel = new JLabel();
		hintLabel.setFont(font2);
		hintLabel.setForeground(Color.BLACK);
		hintLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

		// Dimension size = quitButton.getPreferredSize();
		// quitButton.setBounds(220, 3, size.width, size.height);

		Box box1 = Box.createHorizontalBox();
		box1.setAlignmentY(LEFT_ALIGNMENT);
		box1.setAlignmentX(LEFT_ALIGNMENT);
		frame.add(box1, BorderLayout.NORTH);
		box1.add(hintButton);
		box1.add(box1.createHorizontalGlue());

		box1.add(score);
		box1.add(box1.createHorizontalGlue());

		box1.add(quitButton);

		Box box2 = Box.createVerticalBox();

		box2.setAlignmentX(RIGHT_ALIGNMENT);
		box2.setAlignmentY(CENTER_ALIGNMENT);// CENTER_ALIGNMENT

		frame.add(box2, BorderLayout.CENTER);
		box2.add(box2.createVerticalGlue());

		box2.add(result);
		box2.add(box2.createVerticalGlue());
		box2.add(box2.createVerticalGlue());

		word.setText(" ");
		hintLabel.setText(" ");
		box2.add(word);
		box2.add(hintLabel);
		box2.add(box2.createVerticalGlue());

		Box box3 = Box.createVerticalBox();
		box3.setAlignmentY(CENTER_ALIGNMENT);
		box3.setAlignmentX(LEFT_ALIGNMENT);
		frame.add(box3, BorderLayout.SOUTH);
		box3.add(box2.createVerticalGlue());
		box3.add(new JScrollPane(textArea));
		box3.add(guess);
		box3.add(box2.createVerticalGlue());

		frame.setSize(350, 350);
		frame.setVisible(true);

		hintButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					out.println("!hint");

			}
		});

		quitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				out.println("!quit");
				frame.setVisible(false);
			}
		});

		guess.setText("Guess the word!!");

		guess.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				guess.setText("");
			}
		});

		guess.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				result.setText("");
				String playerGuess = guess.getText();
				if (playerGuess != null)
					out.println(playerGuess);
				textArea.append(playerGuess + '\n');
				guess.setText("");


			}
		});

	}

	public void ProcessMessage(String message) {

		try {
			if (message.equals("startNewRound")) {
				hintButton.setEnabled(true);
				result.setText("\n");
				word.setText("");
				return;
			}
			else if(message.contains("Hint"))
			{
				result.setText("\nA hint has been displayed");
				hintLabel.setText(message);
				hintButton.setEnabled(false);
			}
			else if(message.contains("Score"))
			{
				message = message.replaceAll("Score", "");
				score.setText(message);
			}
			else if(message.contains("Time is up")) {
				System.out.println(message);

				String[] newmsg = message.split("The", 2);
				result.append(newmsg[0] + '\n');
				word.setText("The " + newmsg[1]);
				System.out.println(newmsg[1]);

				textArea.setText("");
				hintLabel.setText("");
				
			}
			else if(message.contains("Incorrect Guess")) {
				result.append(message + '\n');
			}
			else{
				if(message.contains("completed")) {
					guess.setEnabled(false);
          			hintButton.setEnabled(false);
				}
				result.append(message + '\n');
				textArea.setText("");
				hintLabel.setText("");
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void run() {

		
		try {
			while (true) {
				String serverResponse = in.readLine();
				if (serverResponse == null)
					break;
				ProcessMessage(serverResponse);
//				System.out.println("Server says : " + serverResponse);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
