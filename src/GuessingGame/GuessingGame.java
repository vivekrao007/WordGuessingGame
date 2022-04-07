package GuessingGame;

public class GuessingGame {
	
	private GuessingGameService service = new GuessingGameService();

	private Word word;
	public GuessingGame(){
		word = service.GetRandomWord();
	}
	public Word GetAWord() {
		return word;
	}

	public String getHint(){
		return word.getHint();
	}
	
	public boolean CheckWord(String _word, int index) {
		return service.checkWord(_word,index);
	}
	
	
}
