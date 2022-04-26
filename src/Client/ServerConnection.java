package Client;

import java.awt.*;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Formatter;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.*;

import Server.ClientHandler;

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
//    private static int hint = 0;
    private String playerScore;
    private static int finishGame = 0;

    public ServerConnection(Socket s) throws IOException{

        server = s;
        in = new BufferedReader(new InputStreamReader(server.getInputStream()));
        out = new PrintWriter(server.getOutputStream(), true);
        Font font = new Font("Times", Font.BOLD, 15);
        Font font2 = new Font("Times", Font.ITALIC, 14);
        Font font3 = new Font("Times", Font.PLAIN, 16);


        JFrame frame = new JFrame("Word Guessing Game");
        

        textArea = new JTextArea( 3, 10 ); 
        textArea.setEditable( false );
        
        guess = new JTextField(30);
        
        result = new JTextArea(0, 0);
        result.setEditable( false );
		result.append("\n");
		result.setForeground(Color.BLUE);
        result.setFont(font);
        
//		timer = new JLabel();
//		timer.setFont(font3);
//		timer.setForeground(Color.BLACK);
//		timer.setAlignmentX(Component.CENTER_ALIGNMENT);
//		timer.setText("timer");
		
        hintButton = new JButton("Hint");
        hintButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        
		quitButton = new JButton("Quit");
        quitButton.setAlignmentX(Component.RIGHT_ALIGNMENT);
        
		score = new JLabel();
		score.setFont(font3);
		score.setForeground(Color.BLACK);
		score.setAlignmentX(Component.CENTER_ALIGNMENT);
//		score.setAlignmentX(Component.LEFT_ALIGNMENT);        

        word = new JLabel();
        word.setFont(font);
        word.setForeground(Color.BLACK);
        word.setAlignmentX(Component.CENTER_ALIGNMENT);

        hintLabel = new JLabel();
        hintLabel.setFont(font2);
        hintLabel.setForeground(Color.BLACK);
        hintLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
//        Dimension size = quitButton.getPreferredSize();
//        quitButton.setBounds(220, 3, size.width, size.height);
       
        Box box1 = Box.createHorizontalBox();
        box1.setAlignmentY(LEFT_ALIGNMENT);
        box1.setAlignmentX(LEFT_ALIGNMENT);
        frame.add(box1, BorderLayout.NORTH);
        box1.add(hintButton);
        box1.add(box1.createHorizontalGlue());

        box1.add(score);
        box1.add(box1.createHorizontalGlue());

        box1.add(quitButton);
//        Box additionalBox = Box.createVerticalBox();
//        additionalBox.add(additionalBox.createVerticalGlue());
//        additionalBox.setAlignmentY(CENTER_ALIGNMENT);
//        additionalBox.setAlignmentX(RIGHT_ALIGNMENT);
//        additionalBox.add(result);
//      box2.add(additionalBox, BorderLayout.CENTER);

        
        Box box2 = Box.createVerticalBox();
        
        box2.setAlignmentX(RIGHT_ALIGNMENT);
        box2.setAlignmentY(CENTER_ALIGNMENT);//CENTER_ALIGNMENT

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
        box3.add(new JScrollPane( textArea ));
        box3.add(guess);
        box3.add(box2.createVerticalGlue());
        
        frame.setSize(350, 350);
        frame.setVisible( true );

        hintButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e){
        		String hintResult;
		    		try {
		    			out.println("!hint");
// 		    			while ((hintResult = in.readLine()) != null) 
		    				hintResult = in.readLine();
		    				if(hintResult.equals("startNewRound")){
//		    					System.out.println("New round!!");
		    					hintResult = in.readLine();
				         		if(hintResult.equals("3")){
				             		for (int i=0; i<4; i++){
				             			hintResult = in.readLine();
				          				result.append(hintResult + '\n');
				          			}	
				             	}
				             	else{
				             		for (int i=0; i<2; i++){
				             			hintResult = in.readLine();
					          			result.append(hintResult + '\n');
				          			}	
				             		finishGame = 1;
				          			guess.setEnabled(false);
				          			hintButton.setEnabled(false);
//				             		hintResult = in.readLine();
//				          			result.append(hintResult + '\n');
//				             		hintResult = in.readLine();
//				          			result.append(hintResult + '\n');
				             	}
				         		hintResult = in.readLine();
				     			result.append(hintResult + '\n');
		    	         	}
		    				else if(!hintResult.equals("Time is up")){
		    					String hint_Result = "Hint: " + hintResult;
		    					result.setText("\nA hint has been displayed");
 		    				hintLabel.setText(hint_Result);
		    				}
		    				else{
		    					result.setText('\n' + hintResult);
								String ans = in.readLine();
								word.setText("The correct word is: " + ans);
								textArea.setText("");
								hintLabel.setText("");
								out.println("!newgame");
		    				}
		    				playerScore = in.readLine();
		    				score.setText(playerScore);
		    		}
		    		catch (IOException e1) {
		    			e1.printStackTrace();
		    		}
        	}
        });
        
        
        quitButton.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e){
        		out.println("!quit");
        		frame.setVisible( false );
//        		System.exit(0);
        	}
        });

        guess.setText("Guess the word!!");
        
        guess.addMouseListener(new MouseAdapter() {
    	  @Override
    	  public void mouseClicked(MouseEvent e) {
    		  guess.setText("");
    	  }
    	});
        
         guess.addActionListener(new ActionListener(){
 		   public void actionPerformed(ActionEvent e){
		        
 			   	result.setText("");
 	    		String playerGuess = guess.getText();
 	    		if(playerGuess != null)
// 	    			if(playerGuess.equals("!hint"))
// 	    			{
// 	    				hint = 1;
// 	    			}
//         			else
//         				hint = 0;
 		        	out.println(playerGuess);
 		        	textArea.append(playerGuess + '\n');
 		        	guess.setText("");
	        	
// 		    	if(hint == 1)
// 		    	{

// 		    		String hintResult;
// 		    		try {
////     		    			while ((hintResult = in.readLine()) != null) 
// 		    				hintResult = in.readLine();
// 		    				if(hintResult.equals("startNewRound"))
// 		    	         	{
//// 		    					System.out.println("New round!!");
// 		    					hintResult = in.readLine();
// 				         		if(hintResult.equals("3"))
// 				             	{
// 				             		for (int i=0; i<3; i++)
// 				          			{
// 				             			hintResult = in.readLine();
// 				          				result.append(hintResult + '\n');
// 				          			}	
// 				             	}
// 				             	else
// 				             	{
// 				             		hintResult = in.readLine();
// 				          			result.append(hintResult + '\n');
// 				             		hintResult = in.readLine();
// 				          			result.append(hintResult + '\n');
// 				             	}
// 				         		hintResult = in.readLine();
// 				     			result.append(hintResult + '\n');
// 		    	         	}
// 		    				else if(!hintResult.equals("Time is up"))
// 		    				{
// 		    					String hint_Result = "Hint: " + hintResult;
// 		    					result.setText("\nA hint has been displayed");
//     		    				hintLabel.setText(hint_Result);
// 		    				}
// 		    				else
// 		    				{
// 		    					result.setText('\n' + hintResult);
// 								String ans = in.readLine();
// 								word.setText("The correct word is: " + ans);
// 								textArea.setText("");
// 		        				hintLabel.setText("");
// 								out.println("!newgame");
// 		    				}
// 		    				playerScore = in.readLine();
// 		    				score.setText(playerScore);
// 		    		}
// 		    		catch (IOException e1) 
// 		    		{
// 		    			e1.printStackTrace();
// 		    		}
// 		    		hint = 0;
// 		    	}
// 		    	else
// 		    	{
 			    	try {
 						String isWinner;
 						if((isWinner = in.readLine()) != null) 
 						
 							if(isWinner.equals("Correct")){
 								isWinner = in.readLine();
 								result.append('\n' + isWinner + '\n');
 								textArea.setText("");
 								hintLabel.setText("");
// 								out.println("!newgame");
 							}
 							else if(isWinner.equals("Time is up")){

 								result.setText('\n' + isWinner + '\n');
 								String ans = in.readLine();
 								word.setText("The correct word is " + ans);
 								textArea.setText("");
 								hintLabel.setText("");
// 								out.println("!newgame");
 							}
 							else{
 								result.append('\n' + isWinner);
 							}
 						
 						playerScore = in.readLine();
	    				score.setText(playerScore);
	    				isWinner = in.readLine();
	    				if(isWinner.equals("startNewRound")){
							isWinner = in.readLine();
			         		if(isWinner.equals("3")){
			             		for (int i=0; i<4; i++){
			             			isWinner = in.readLine();
			          				result.append(isWinner + '\n');
			          			}	
			             	}
			             	else{
			             		for (int i=0; i<2; i++){
			             			isWinner = in.readLine();
				          			result.append(isWinner + '\n');

			          			}
			          			
//			             		finishGame = 1;

			          			guess.setEnabled(false);
			          			hintButton.setEnabled(false);
			          			
		          			
//			          			try {
//									Thread.sleep(10000);
//								} catch (InterruptedException e1) {
//									// TODO Auto-generated catch block
//									e1.printStackTrace();
//								}
//			          			String endGame = in.readLine();
			          			
//			          			synchronized(this) {
//			          				try {
//										wait();
//									} catch (InterruptedException e1) {
//										// TODO Auto-generated catch block
//										e1.printStackTrace();
//									}
//			          			}
//			          			
//			          			 synchronized (this) {
//			          	            try {
//										wait();
//									} catch (InterruptedException e1) {
//										// TODO Auto-generated catch block
//										e1.printStackTrace();
//									}
//
//			          	        }
//			          			System.out.println("arrived!!");
//
//			          			String endGame;
//		          				if((endGame = in.readLine()) != null)
//			    	         	{
//				          			System.out.println("result added!!");
//
//			    	         		result.setForeground(Color.red);
//			    	                result.setFont(font);
//			    	         		result.append(endGame + '\n');
//			    	         	}  
//			          			
			          		    
			          			
//			          			while(true) 
//			          			{
//			          				String endGame;
//			          				if((endGame = in.readLine()) != null)
////			    	         		if(endGame.equals("Game ended"))
//				    	         	{
//				    	         		endGame = in.readLine();
//				    	         		result.setForeground(Color.red);
//				    	                result.setFont(font);
//				    	         		result.append(endGame + '\n');
//				    	         		break;
//				    	         	}
//			    	         		else
//			    	         		{
//			    	         			Thread.sleep(3000);
//			    	         		}
//				    	         	 
//			          			}
			          		}
			             	
			         		
//			         		isWinner = in.readLine();
//			     			result.append(isWinner + '\n');
	    	         	}
 					}
 			    	
 			    	//| InterruptedException
 					catch (IOException e1) {
 						e1.printStackTrace();
 					}
// 		    	}
//		    	 if(finishGame == 1)
//		         {
		    		 
//		    		 try {
//		    			 	
//		    	         	String endGame = in.readLine();
////		    	         	while ((endGame = in.readLine()) != null) {
//		    	         		if(endGame.equals("Game ended"))
//			    	         	{
//			    	         		endGame = in.readLine();
//			    	         		result.setForeground(Color.red);
//			    	                result.setFont(font);
//			    	         		result.append(endGame + '\n');
//			    	         	}
////		    	         	}
//		    	         	
//		    	         	
//		    	         	
//		    	     	}
//		    	  		catch (IOException e1) 
//		    	  		{
//		    	  			e1.printStackTrace();
//		    	  		}
//		         }
 		   }
         });
         
    }
    
//    public void waitForInputs() throws InterruptedException {
//        synchronized (this) {
//            wait();
//        }
//    }
  
    @Override
    public void run() 
    {

    	 try {
         	String start = in.readLine();
         	if(start.equals("startNewRound")){
         		start = in.readLine();
         		if(start.equals("3")){
             		for (int i=0; i<3; i++){
          				start = in.readLine();
          				result.append(start + '\n');
          			}	
             	}
             	else{
             		for (int i=0; i<2; i++)
          			{
          				start = in.readLine();
          				result.append(start + '\n');
          			}	
             		guess.setEnabled(false);
          			hintButton.setEnabled(false);
          			
//             		start = in.readLine();
//          			result.append(start + '\n');
//          			start = in.readLine();
//          			result.append(start + '\n');

             	}
             	start = in.readLine();
     			result.append(start + '\n');
      			playerScore = in.readLine();
      			score.setText(playerScore);
      		}
     	}
  		catch (IOException e1) {
  			e1.printStackTrace();
  		}
    }
}

