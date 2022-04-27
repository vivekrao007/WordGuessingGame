package GuessingGame;

import java.util.Random;
import java.util.ArrayList;

public class GuessingGameService {

	private ArrayList<Word> _words = new ArrayList<Word>();

	GuessingGameService() {
		_words.add(new Word(1, "java", "most used OOP language"));
		_words.add(new Word(2, "xml", "markup language"));
		_words.add(new Word(3, "angular", "javascript framework"));
	}

	// returns list of words.
	public ArrayList<Word> getAllWords() {
		return _words;
	}

	// this method returns a random word from the words array
	// and remove returned word from the list, so when we want
	// to get new word for next round, same word don't get repeated

	public Word GetRandomWord() {
		Random rand = new Random();
		int randNumber = rand.nextInt(_words.size());
		Word w = _words.get(randNumber);
		// _words.remove(randNumber);
		return w;
	}

	// checks weather the word at a provided index is same or not.
	public boolean checkWord(String _word, int index) {
		Word word = _words.get(index);
		if (word.getWord().equals(_word))
			return true;
		return false;
	}

	public Word GetWordAtIndex(int index){

		if(index > _words.size()) throw new IndexOutOfBoundsException();

		return _words.get(index - 1);
	}

	public int GetNumberOfWord(){
		return _words.size();
	}
}
