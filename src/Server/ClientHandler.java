package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import GuessingGame.Word;

public class ClientHandler extends Thread {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private Word word;
    private String CurrentWord;

    public ClientHandler(Socket socket,Word word) throws IOException {
        this.socket = socket;
        this.word = word;
        this.CurrentWord = word.getWord();
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

    }

    public void println(String s) {
        out.println(s);
    }

    public void run() {
        try {
        	String request;
            while ((request = in.readLine()) != null) {
            	if(request.equals("!start"))
            		println(CurrentWord);
                if(request.equals("!hint")){
                    println(word.getHint());
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


}
