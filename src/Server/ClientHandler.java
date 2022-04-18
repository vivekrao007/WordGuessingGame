package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.*;

import GuessingGame.GuessingGame;

public class ClientHandler implements Runnable {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private GuessingGame GuessingGame; // word object for the current word.
    private Long startGameTime;
    private final static int TIME_LIMIT = 10000; // timelimit in milli seconds;
    
    public ClientHandler(Socket socket, GuessingGame gameObj) throws IOException {
        this.socket = socket;
        GuessingGame = gameObj;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

    }

    public void println(String s) 
    {
        out.println(s);
    }

    public void run() 
    {
        

    	 try {
             String request;
             ClientGame game = new ClientGame(GuessingGame);
             StartTimer();
             println("The game has started ");
             println("You have 60 seconds to guess the word ");
             println("The Word is of length: " + GuessingGame.GetAWord().getWord().length());

            System.out.printf(GuessingGame.GetAWord().getWord());
             while ((request = in.readLine()) != null) {
                 request = request.toLowerCase();
                 if (Checktimer()) {
                     println("Time is up");
                     println(GuessingGame.GetAWord().getWord());
                     continue;
                 }

                 if (request.equals("!hint")) {
                     game.updateScore(-10);
                     println(GuessingGame.GetAWord().getHint());
                     continue;
                 } else {
                     if (game.VerifyWord(request)) {
                         game.updateScore(100);
                         println("Correct");
                         println("You gussed the word, your score is : " + game.GetScore());
                     } else {
                         println("Incorrect Guess");
                     }
                 }

             }
             in.close();
         } catch (Exception e1) {
             e1.printStackTrace();
         }

         try {
             in.close();
             socket.close();
         } catch (Exception e) {
             e.printStackTrace();
         } finally {
             out.close();
         }
         System.err.println("closing socket");
     }
    public void StartTimer() {
        startGameTime = System.currentTimeMillis();
    }

    public boolean Checktimer() {
        if (System.currentTimeMillis() - startGameTime >= TIME_LIMIT) {
            return true;
        }
        return false;
    }


}
