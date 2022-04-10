package Server;

import GuessingGame.GuessingGame;

public class ClientGame {
    
    private static GuessingGame game;
    private int Score; // keep track of the score of current player.
    private Boolean Completed; // true for word gussed correctly;

    public ClientGame(GuessingGame game){
        this.game = game;
    }

    public void Completed() {
        Completed = true;
    }

    public boolean IsCompleted() {
        return Completed;
    }

    public synchronized void updateScore(int value) {
        Score += value;
    }
    public synchronized int GetScore(){
        return Score;
    }
	
	public boolean VerifyWord(String text) {
        if (game.GetAWord().getWord().toLowerCase().equals(text)) {
            Completed();
            return true;
        }
        return false;
    }
}
