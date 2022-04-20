package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import GuessingGame.GuessingGame;
import GuessingGame.Word;
import java.util.ArrayList;

public class ClientHandler extends Thread {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private GuessingGame GuessingGame; // word object for the current word.
    private Long startGameTime;
    private final static int TIME_LIMIT = 60000; // timelimit in milli seconds;
    private int Round = 0;
    private int Score; // keep track of the score of current player.
    // private Boolean Completed; // true for word gussed correctly;
    private Word CurrentWord;
    ArrayList<ClientHandler> AllClients;

    public ClientHandler(Socket socket, GuessingGame gameObj, ArrayList<ClientHandler> clients) throws IOException {
        this.socket = socket;
        GuessingGame = gameObj;
        AllClients = clients;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

    }

    public void println(String s) {
        out.println(s);
    }

    public void run() {

        try {
            String request;
            startNewRound();
            println(" " + GetScore());

            while ((request = in.readLine()) != null) 
            {
                request = request.toLowerCase();
                if (Checktimer()) {
                    println("Time is up");
                    println(CurrentWord.getWord());
                    println(" " + GetScore());
                    startNewRound();
                    continue;
                }

                if (IsMatchCompleted()) 
                {
                    println("You have completed the game");
//                    println("Your score is : " + GetScore());
                    println(" " + GetScore());
                    println("BroadCastWinner");
                    BroadCastWinner();
                    continue;
                }
                if (request.equals("!newgame") && Checktimer()) 
                {
                    startNewRound();
                }
                if (request.equals("!hint")) 
                {
                    updateScore(-10);
                    println(CurrentWord.getHint());
                    println(" " + GetScore());
                    continue;
                } 
                else if (request.equals("!quit")) 
                {
               	 	System.out.printf("Quit the game!\n");
                    break;
                }
                else 
                {

                    if (VerifyWord(request)) {
                        updateScore(100);
                        println("Correct");
                        println("You gussed the word");
                        println(" " + GetScore());
                        startNewRound();
                    } 
                    else 
                    {
                        println("Incorrect Guess");
                        println(" " + GetScore());
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

    public void StartTimer() 
    {
        startGameTime = System.currentTimeMillis();
    }

    public boolean Checktimer() {
    	long timeDiff = System.currentTimeMillis() - startGameTime;
        if (timeDiff >= TIME_LIMIT) 
        {
            return true;
        }
        return false;
    }

    public synchronized void updateScore(int value) {
        Score += value;
    }

    public synchronized int GetScore() {
        return Score;
    }

    public boolean VerifyWord(String text) {
    	if (CurrentWord.getWord().equals(text)) 
    	{
            return true;
        }
    	else
    	{
            return false;
    	}
//        return CurrentWord.getWord().equals(text);
    }

    public boolean IsMatchCompleted() 
    {
        return Round > GuessingGame.GetNumberOfWord();
    }

    private void startNewRound() 
    {
        Round++;
    	println("startNewRound");
        if (!IsMatchCompleted()) {
            CurrentWord = GuessingGame.GetWordAtIndex(Round);
            StartTimer();
            println("3");
            println("Starting new round ");
            println("Round : " + Round + "/" + GuessingGame.GetNumberOfWord());
            println("You have " + (TIME_LIMIT / 1000) + " seconds to guess the word ");
            println("The word is of length: " + CurrentWord.getWord().length());
        } 
        else 
        {
        	println("1");
            println("Your game is completed. waiting for other players to complete the game");

//            println("BroadCastWinner");
            BroadCastWinner();
        }
    }
    private void BroadCastWinner() 
    {
        boolean EveyoneCompletedGame = false;

        for (ClientHandler client : AllClients) 
        {
        	if(!client.isAlive()){
                AllClients.remove(client);
            }
            if (client.IsMatchCompleted())
                EveyoneCompletedGame = true;
        }
        if (EveyoneCompletedGame)
            println("The Game has been completed : " + socket.getLocalAddress().getHostName() +" is the winner");
    
    }
}