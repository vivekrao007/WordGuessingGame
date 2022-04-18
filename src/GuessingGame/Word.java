package GuessingGame;

public class Word {
	private final int Index;
	private String Word;
	private String Hint;

	public Word(int Index, String Word, String Hint) {
		this.Index = Index;
		this.Word = Word;
		this.Hint = Hint;
	}

	public int getIndex() {
		return Index;
	}

	public String getWord() {
		return Word;
	}

	public void setWord(String word) {
		Word = word;
	}

	public String getHint() {
		return Hint;
	}

	public void setHint(String hint) {
		Hint = hint;
	}
}
