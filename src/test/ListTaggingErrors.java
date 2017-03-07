package test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import models.ConfusionMatrix;
import models.TaggedSentence;
import utils.Constants;
import utils.EnglishDictionary;
import utils.FileManager;
import utils.TaggedSentenceService;

public class ListTaggingErrors {

	static EnglishDictionary englishDictionary;

	public static void main(String[] args) throws FileNotFoundException, IOException {
		englishDictionary = new EnglishDictionary();
		writeTaggingErrorsToFile(Constants.TEST_STANFORD_UNTAGGED, Constants.TEST_STANFORD_TAGGED,
				Constants.TAGGING_ERROR_LIST);

	}

	private static void writeTaggingErrorsToFile(String untaggedFile, String taggedFile, String outputFile)
			throws FileNotFoundException, IOException {
		MaxentTagger tagger = new MaxentTagger(Constants.LEFT3WORDS);
		List<String> untagged = FileManager.readFile(new File(untaggedFile));
		List<TaggedSentence> gold = TaggedSentenceService
				.getTaggedSentences(FileManager.readFile(new File(taggedFile)));

		FileManager errorListFile = new FileManager(outputFile);
		errorListFile.createFile();
		List<String> errorList = new ArrayList<>();
		ConfusionMatrix cm = new ConfusionMatrix();

		for (int x = 0; x < untagged.size(); x++) {
			String t = tagger.tagTokenizedString(untagged.get(x));
			TaggedSentence tagged = TaggedSentenceService.getTaggedSentence(x, t);
			for (int i = 0; i < tagged.getWords().size(); i++) {
				if (tagged.getTags().get(i).equals("NNC")) {
					if (englishDictionary.isAnEnglishNoun(tagged.getWords().get(i)))
						tagged.getTags().set(i, "FW");
				}

				cm.increaseValue(gold.get(x).getTags().get(i), tagged.getTags().get(i));

				if (!tagged.getTags().get(i).equals(gold.get(x).getTags().get(i))) {
					errorList.add("G:" + gold.get(x).getTags().get(i) + " " + "P:" + tagged.getTags().get(i) + " "
							+ tagged.getWords().get(i) + " " + x + " " + i + " \t\tContext - "
							+ getLeft2Right2(tagged, i));
				}
			}
		}

		errorListFile.writeToFile(cm.printDescendingClassDistributionandAccuracy());
		errorListFile.writeToFile("Average Precision: " + Double.toString(cm.getAvgPrecision()));
		errorListFile.writeToFile("Average Recall: " + Double.toString(cm.getAvgRecall()));
		errorListFile
				.writeToFile("Accuracy: " + cm.getAccuracy() + "(" + cm.getCorrect() + "/" + cm.getTotalSum() + ")");

		errorListFile.writeToFile("GoldTag\tPredTag\tWord\tSent#\tWord#");
		Collections.sort(errorList);
		for (String e : errorList) {
			errorListFile.writeToFile(e);
		}

		errorListFile.writeToFile(cm.toString());
		errorListFile.close();

		cm.getClassMistaggedDistribution("NNC");
	}

	private static String getLeft2Right2(TaggedSentence tagged, int i) {
		StringBuilder sb = new StringBuilder();
		if (i - 2 >= 0)
			sb.append(tagged.getWords().get(i - 2) + "|" + tagged.getTags().get(i - 2) + " ");
		if (i - 1 >= 0)
			sb.append(tagged.getWords().get(i - 1) + "|" + tagged.getTags().get(i - 1) + " ");
		sb.append("*" + tagged.getWords().get(i) + "|" + tagged.getTags().get(i) + "* ");
		if (i + 1 < tagged.getTags().size())
			sb.append(tagged.getWords().get(i + 1) + "|" + tagged.getTags().get(i + 1) + " ");
		if (i + 2 < tagged.getTags().size())
			sb.append(tagged.getWords().get(i + 2) + "|" + tagged.getTags().get(i + 2) + " ");

		// TODO Auto-generated method stub
		return sb.toString();
	}

}
