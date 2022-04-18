package GuessingGame;

public class GuessingGame {

	private GuessingGameService service = new GuessingGameService();

	private Word word;

	public GuessingGame() {
		word = service.GetWordAtIndex(1);
	}

	public Word GetWord() {
		return word;
	}

	public String getHint() {
		return word.getHint();
	}

	public boolean CheckWord(String _word, int index) {
		return service.checkWord(_word, index);
	}

	public Word GetWordAtIndex(int index){
		word = service.GetWordAtIndex(index);
		return word;
	}

	public int GetNumberOfWord(){
		return service.GetNumberOfWord();
	}
}
