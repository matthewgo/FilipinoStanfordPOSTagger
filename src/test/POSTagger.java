package test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import models.TaggedSentence;
import utils.FileManager;
import utils.TaggedSentenceService;

public class POSTagger {

	private String taggerModelFilename;
	private MaxentTagger tagger;

	public POSTagger(String taggerModelFilename) {
		tagger = new MaxentTagger(taggerModelFilename);
	}

	public List<TaggedSentence> tagFile(String untaggedFilename) throws FileNotFoundException, IOException {
		List<String> untagged = FileManager.readFile(new File(untaggedFilename));
		List<TaggedSentence> tagged = new ArrayList<>();
		for (int i = 0; i < untagged.size(); i++) {
			String t = tagger.tagTokenizedString(untagged.get(i));
			tagged.add(TaggedSentenceService.getTaggedSentence(i, t));
		}
		return tagged;
	}

	public TaggedSentence tagSentence(String sentence) {
		return TaggedSentenceService.getTaggedSentence(tagger.tagTokenizedString(sentence));
	}
}
