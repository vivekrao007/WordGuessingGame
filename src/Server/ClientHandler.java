package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import GuessingGame.GuessingGame;
import GuessingGame.Word;

public class ClientHandler implements Runnable {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private GuessingGame GuessingGame; // word object for the current word.
    private Long startGameTime;
    private final static int TIME_LIMIT = 10000; // timelimit in milli seconds;
    private int Round = 0;
    private int Score; // keep track of the score of current player.
    // private Boolean Completed; // true for word gussed correctly;
    private Word CurrentWord;

    public ClientHandler(Socket socket, GuessingGame gameObj) throws IOException {
        this.socket = socket;
        GuessingGame = gameObj;
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
            println(" --- The game has started ---");

            while ((request = in.readLine()) != null) {
                request = request.toLowerCase();
                if (Checktimer()) {
                    println("Time is up");
                    println(CurrentWord.getWord());
                    continue;
                }

                if (IsMatchCompleted()) {
                    println("You have completed the game");
                    println("Your score is : " + GetScore());
                    continue;
                }

                if (request.equals("!newgame") && Checktimer()) {
                    startNewRound();
                }
                if (request.equals("!hint")) {
                    updateScore(-10);
                    println(CurrentWord.getHint());
                    continue;
                } else {
                    if (VerifyWord(request)) {
                        updateScore(100);
                        println("Correct");
                        println("You gussed the word, your score is : " + GetScore());
                        startNewRound();
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

    public synchronized void updateScore(int value) {
        Score += value;
    }

    public synchronized int GetScore() {
        return Score;
    }

    public boolean VerifyWord(String text) {
        return CurrentWord.getWord() == text;
    }

    public boolean IsMatchCompleted() {
        return Round == GuessingGame.GetNumberOfWord();
    }

    private void startNewRound() {
        if (!IsMatchCompleted()) {
            Round++;
            CurrentWord = GuessingGame.GetWordAtIndex(Round);
            StartTimer();
            println("Round : " + Round + "/" + GuessingGame.GetNumberOfWord());
            println("You have " + (TIME_LIMIT / 1000) + " seconds to guess the word ");
            println("The Word is of length: " + CurrentWord.getWord().length());
        } else {
            println("Your game is completed. waiting for other players to complete the game");
        }
    }
}
