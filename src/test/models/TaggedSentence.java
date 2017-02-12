package test.models;

import java.util.List;

public class TaggedSentence {
	int index;
	List<String> words;
	List<String> tags;
	String sentence;

	public TaggedSentence(int index, List<String> words, List<String> tags, String sentence) {
		super();
		this.index = index;
		this.words = words;
		this.tags = tags;
		this.sentence = sentence;
	}

	public String getSentence() {
		return sentence;
	}

	public void setSentence(String sentence) {
		this.sentence = sentence;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public List<String> getWords() {
		return words;
	}

	public void setWords(List<String> words) {
		this.words = words;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

}
