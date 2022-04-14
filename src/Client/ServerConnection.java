package Client;

import java.awt.*;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Formatter;
import java.util.Scanner;

import javax.swing.*;
import java.awt.event.*;

public class ServerConnection extends JFrame implements Runnable {
    private Socket server;
    private BufferedReader in;
    private PrintWriter out;
    private JButton startButton;
    private JButton quitButton;
    private JTextField guess;
    private JTextArea result;
    private JLabel word;
    private JLabel hintLabel;
    private static int hint = 0;
    
    public ServerConnection(Socket s) throws IOException
    {

        server = s;
        in = new BufferedReader(new InputStreamReader(server.getInputStream()));
        out = new PrintWriter(server.getOutputStream(), true);
        Font font = new Font("Times", Font.BOLD, 18);
        Font font2 = new Font("Times", Font.ITALIC, 14);

        JFrame frame = new JFrame("Word Guessing Game");

//        startButton = new JButton("Start");
//        quitButton = new JButton("Quit");
        
        guess = new JTextField(30);
//        guess.setText("Click start to begin...");
        
        
        result = new JTextArea();
        result.setEditable( false );
		result.append("\n");

        word = new JLabel();
        word.setFont(font);
        word.setForeground(Color.BLACK);
        word.setAlignmentX(Component.CENTER_ALIGNMENT);

        hintLabel = new JLabel();
        hintLabel.setFont(font2);
        hintLabel.setForeground(Color.BLACK);
        hintLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
//        Dimension size = startButton.getPreferredSize();
//        startButton.setBounds(10, 3, size.width, size.height);
//        quitButton.setBounds(220, 3, size.width, size.height);
       

        
//        frame.add(startButton);
//        frame.add(quitButton);

        Box box = Box.createVerticalBox();
        box.setAlignmentY(CENTER_ALIGNMENT);
        box.setAlignmentX(LEFT_ALIGNMENT);
        frame.add(box, BorderLayout.CENTER);

        box.add(Box.createVerticalGlue());
        box.add(word);
        box.add(hintLabel);
//        box.add(startButton);
//        box.add(quitButton);
        box.add(Box.createVerticalGlue());

        
        frame.add(result, BorderLayout.NORTH);
        frame.add(guess, BorderLayout.SOUTH);
        box.add(Box.createHorizontalGlue());        

        try {
        	String start;
 			for (int i=0; i<3; i++)
 			{
 				start = in.readLine();
 				result.append(start + "\n");
 			}
 				
 		}
 		catch (IOException e1) 
 		{
 			e1.printStackTrace();
 		}
        
       
//        quitButton.addActionListener(new ActionListener()
//        {
//        	public void actionPerformed(ActionEvent e)
//        	{
//        		System.out.printf("quit\n");
//        		out.println("!quit");
//        	}
//        });
//        if(!start)
//        {
//        	 startButton.addActionListener(new ActionListener()
//             {
//     			public void actionPerformed(ActionEvent e)
//     			{
//     				System.out.printf("start\n");
//     				start = true;
//     			}
//             });
//			System.out.printf("2\n");
//
//        }
//        else
//        {
            guess.setText("Guess the word!!");

             
             guess.addActionListener(new ActionListener()
             {
     		   public void actionPerformed(ActionEvent e)
     		   {
     			   	result.setText("");
			        result.setForeground(Color.BLUE);
			        result.setFont(font);
			        
			        
     	    		String playerGuess = guess.getText();
     	    		if(playerGuess != null)
     	    			if(playerGuess.equals("!hint"))
     	    			{
     	    				hint = 1;
 	    					result.setText("A hint has been displayed");
     	    			}
             			else
             				hint = 0;
     		        	out.println(playerGuess);
     		        	guess.setText("");
     			        	
     		    	if(hint == 1)
     		    	{
     		    		String hintResult;
     		    		try {
//     		    			while ((hintResult = in.readLine()) != null) 
     		    				hintResult = in.readLine();
     		    				if(!hintResult.equals("Time is up"))
     		    				{
     		    					String hint_Result = "Hint: " + hintResult;
         		    				hintLabel.setText(hint_Result);
     		    				}
     		    				else
     		    				{
     		    					result.setText(hintResult);
     								String ans = in.readLine();
     								word.setText("The correct word is: " + ans);
     		    				}
     		    				
     		    		}
     		    		catch (IOException e1) 
     		    		{
     		    			e1.printStackTrace();
     		    		}
     		    		hint = 0;
     		    	}
     		    	else
     		    	{
     			    	try 
     					{
     						String isWinner;
     						if((isWinner = in.readLine()) != null) 
     							if(isWinner.equals("Correct"))
     							{
     								isWinner = in.readLine();
     								result.append(isWinner);
     								
     							}
     							else if(isWinner.equals("Time is up"))
     							{
     								result.setText(isWinner);
     								String ans = in.readLine();
     								word.setText("The correct word is" + ans);
     							}
     							else
     							{
     								result.setText(isWinner);
     							}
     					}
     					catch (IOException e1) 
     					{
     						e1.printStackTrace();
     					}
     		    	}
     		   }
             });
           
                     
        frame.setSize(350, 250);
        frame.setVisible( true );

    }

    
  
    @Override
    public void run() 
    {
    	
    }
}

