package utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import models.TaggedSentence;

public class HPOSTandStanfordFormatConverter {

	public static void main(String[] args) throws FileNotFoundException, IOException {
		StanfordToHPOST(Constants.TEST_STANFORD_TAGGED_JOEY_CHECKED_REVISED_ENG_NOUNS,
				Constants.TEST_HPOST_WORDS_TAGGED_JOEY_CHECKED_REVISED_ENG_NOUNS,
				Constants.TEST_HPOST_TAGS_TAGGED_JOEY_CHECKED_REVISED_ENG_NOUNS);
	}

	public static void HPOSTToStanford(String hpostWords, String hpostTags, String stanfordOutput)
			throws FileNotFoundException, IOException {
		List<String> hpostWordList = FileManager.readFile(new File(hpostWords));
		List<String> hpostTagList = FileManager.readFile(new File(hpostTags));

		FileManager stanfordOutputFile = new FileManager(stanfordOutput);
		stanfordOutputFile.createFile();
		for (int i = 0; i < hpostWordList.size(); i++) {
			String[] words = hpostWordList.get(i).trim().split(" ");
			String[] tags = hpostTagList.get(i).trim().split(" ");
			StringBuilder sb = new StringBuilder();

			for (int j = 0; j < words.length; j++) {
				sb.append(words[j] + "|" + tags[j]);
				if (j < words.length - 1)
					sb.append(" ");
			}
			System.out.println(sb.toString());
			stanfordOutputFile.writeToFile(sb.toString());
		}
		stanfordOutputFile.close();

	}

	public static void StanfordToHPOST(String stanfordInput, String hpostWords, String hpostTags)
			throws FileNotFoundException, IOException {
		List<TaggedSentence> stanfordLines = TaggedSentenceService
				.getTaggedSentences(FileManager.readFile(new File(stanfordInput)));
		FileManager hpostWordFile = new FileManager(hpostWords);
		FileManager hpostTagFile = new FileManager(hpostTags);
		hpostWordFile.createFile();
		hpostTagFile.createFile();
		for (TaggedSentence ts : stanfordLines) {
			hpostWordFile.writeToFile(TaggedSentenceService.getString(ts.getWords()));
			hpostTagFile.writeToFile(TaggedSentenceService.getString(ts.getTags()));
		}
		hpostWordFile.close();
		hpostTagFile.close();
	}
}
