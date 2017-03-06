package utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EnglishDictionary {
	List<String> englishNouns = new ArrayList<>();

	public EnglishDictionary() throws FileNotFoundException, IOException {
		englishNouns = FileManager.readFile(new File(Constants.ENGLISH_NOUNS));
	}

	public boolean isAnEnglishNoun(String word) {
		if (Collections.binarySearch(englishNouns, word.toLowerCase()) >= 0) {
			return true;
		} else
			return false;
	}
}
