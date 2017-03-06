package train.tagfixing;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import test.models.TaggedSentence;
import utils.EnglishDictionary;
import utils.FileManager;
import utils.TaggedSentenceService;

public class TagsOverwriter {
	EnglishDictionary englishDictionary;

	public TagsOverwriter() throws FileNotFoundException, IOException {
		englishDictionary = new EnglishDictionary();

	}

	public void reviseEnglishNNCasFW(String origFilename, String revisedFilename)
			throws FileNotFoundException, IOException {
		List<TaggedSentence> orig = TaggedSentenceService
				.getTaggedSentences(FileManager.readFile(new File(origFilename)));
		FileManager revised = new FileManager(revisedFilename);
		revised.createFile();
		for (TaggedSentence o : orig) {
			for (int i = 0; i < o.getTags().size(); i++) {
				if (englishDictionary.isAnEnglishNoun(o.getWords().get(i)) && o.getTags().get(i).equals("NNC")) {
					o.getTags().set(i, "FW");
				}
			}
			revised.writeToFile(TaggedSentenceService.getString(o));
		}
		revised.close();
	}

}
