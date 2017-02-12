package test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import utils.Constants;
import utils.FileManager;

public class Tagging {

	public static void main(String[] args) throws FileNotFoundException, IOException {
		MaxentTagger tagger = new MaxentTagger("data/filipino.tagger");
		List<String> sentences = FileManager.readFile(new File(Constants.TEST_STANFORD_UNTAGGED));
		int[] exclusions = { 3, 7, 10, 11, 21, 29, 30, 39, 43, 61, 83, 86, 102, 106, 109, 144, 164, 219, 224, 232, 252,
				263, 311 };
		for (int i = 312; i < sentences.size(); i++) {
			boolean shouldBeExcluded = false;
			for (int x : exclusions) {
				if (x == i)
					shouldBeExcluded = true;
			}
			if (shouldBeExcluded == false)
				System.out.println(i + " " + tagger.tagTokenizedString(sentences.get(i)));
		}
	}

}
