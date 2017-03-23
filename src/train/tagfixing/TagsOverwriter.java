package train.tagfixing;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import models.TaggedSentence;
import utils.EnglishDictionary;
import utils.FileManager;
import utils.TaggedSentenceService;

public class TagsOverwriter {
	EnglishDictionary englishDictionary;

	public TagsOverwriter() throws FileNotFoundException, IOException {
		englishDictionary = new EnglishDictionary();

	}

	public List<TaggedSentence> reviseEnglishNNCasFW(List<TaggedSentence> taggedSentences) {
		for (TaggedSentence o : taggedSentences) {
			for (int i = 0; i < o.getTags().size(); i++) {
				if (englishDictionary.isAnEnglishNoun(o.getWords().get(i)) && o.getTags().get(i).equals("NNC")) {
					o.getTags().set(i, "FW");
				}
			}
		}
		return taggedSentences;
	}

	public void reviseEnglishNNCasFWFromFile(String origFilename, String revisedFilename)
			throws FileNotFoundException, IOException {
		List<TaggedSentence> orig = TaggedSentenceService
				.getTaggedSentences(FileManager.readFile(new File(origFilename)));
		FileManager revised = new FileManager(revisedFilename);
		revised.createFile();
		List<TaggedSentence> rev = reviseEnglishNNCasFW(orig);
		for (TaggedSentence r : rev) {
			revised.writeToFile(TaggedSentenceService.getString(r));
		}
		revised.close();
	}

}
