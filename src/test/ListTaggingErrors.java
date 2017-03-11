package test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import models.ConfusionMatrix;
import models.TaggedSentence;
import utils.Constants;
import utils.FileManager;
import utils.TaggedSentenceService;

public class ListTaggingErrors {

	public static void main(String[] args) throws FileNotFoundException, IOException {

		POSTagger left3WordsTagger = new POSTagger(Constants.LEFT3WORDS);

		writeToFiles(left3WordsTagger.tagFile(Constants.TEST_STANFORD_UNTAGGED), Constants.TEST_STANFORD_TAGGED,
				Constants.TAGGING_ERROR_LEFT3WORDS, Constants.TAGGING_RESULTS_LEFT3WORDS);
		// writeToFiles(left3WordsTagger.tagFile(Constants.TEST_STANFORD_UNTAGGED),
		// Constants.TEST_STANFORD_TAGGED_JOEY_CHECKED,
		// Constants.TAGGING_ERROR_LEFT3WORDS_JOEY_CHECKED,
		// Constants.TAGGING_RESULTS_LEFT3WORDS_JOEY_CHECKED);
		// writeToFiles(left3WordsTagger.tagFile(Constants.TEST_STANFORD_UNTAGGED),
		// Constants.TEST_STANFORD_TAGGED_JOEY_CHECKED_REVISED_ENG_NOUNS,
		// Constants.TAGGING_ERROR_LEFT3WORDS_JOEY_CHECKED_REVISED_ENG_NOUNS,
		// Constants.TAGGING_RESULTS_LEFT3WORDS_JOEY_CHECKED_REVISED_ENG_NOUNS);
		// writeToFiles(left3WordsTagger.tagFile(Constants.TEST_STANFORD_UNTAGGED_SECOND_HALF),
		// Constants.TEST_STANFORD_TAGGED_SECOND_HALF,
		// Constants.TAGGING_ERROR_SECOND_HALF_LEFT3WORDS,
		// Constants.TAGGING_RESULTS_SECOND_HALF_LEFT3WORDS);

		POSTagger left3WordsTRENTagger = new POSTagger(Constants.LEFT3WORDS_REVISED_ENG_NOUNS);
		// writeToFiles(left3WordsTRENTagger.tagFile(Constants.TEST_STANFORD_UNTAGGED),
		// Constants.TEST_STANFORD_TAGGED,
		// Constants.TAGGING_ERROR_LEFT3WORDS_TREN,
		// Constants.TAGGING_RESULTS_LEFT3WORDS_TREN);
		// writeToFiles(left3WordsTRENTagger.tagFile(Constants.TEST_STANFORD_UNTAGGED),
		// Constants.TEST_STANFORD_TAGGED_JOEY_CHECKED,
		// Constants.TAGGING_ERROR_LEFT3WORDS_TREN_JOEY_CHECKED,
		// Constants.TAGGING_RESULTS_LEFT3WORDS_TREN_JOEY_CHECKED);
		// writeToFiles(left3WordsTRENTagger.tagFile(Constants.TEST_STANFORD_UNTAGGED),
		// Constants.TEST_STANFORD_TAGGED_JOEY_CHECKED_REVISED_ENG_NOUNS,
		// Constants.TAGGING_ERROR_LEFT3WORDS_TREN_JOEY_CHECKED_REVISED_ENG_NOUNS,
		// Constants.TAGGING_RESULTS_LEFT3WORDS_TREN_JOEY_CHECKED_REVISED_ENG_NOUNS);

	}

	private static void writeToFiles(List<TaggedSentence> predictedTaggedSentences, String goldFilename,
			String errorOutputFilename, String predictedTagsFilename) throws IOException {
		List<TaggedSentence> gold = TaggedSentenceService.getTaggedSentencesFromFile(goldFilename);

		FileManager errorOutputFile = new FileManager(errorOutputFilename);
		errorOutputFile.createFile();
		List<String> errorList = new ArrayList<>();
		ConfusionMatrix cm = new ConfusionMatrix();

		FileManager predictedTagsFile = null;
		if (predictedTagsFilename != null) {
			predictedTagsFile = new FileManager(predictedTagsFilename);
			predictedTagsFile.createFile();
		}

		for (int x = 0; x < predictedTaggedSentences.size(); x++) {
			if (predictedTagsFile != null)
				predictedTagsFile.writeToFile(TaggedSentenceService.getString(predictedTaggedSentences.get(x)));
			for (int i = 0; i < predictedTaggedSentences.get(x).getWords().size(); i++) {

				cm.increaseValue(gold.get(x).getTags().get(i), predictedTaggedSentences.get(x).getTags().get(i));

				if (!predictedTaggedSentences.get(x).getTags().get(i).equals(gold.get(x).getTags().get(i))) {
					errorList.add("G:" + gold.get(x).getTags().get(i) + " " + "P:"
							+ predictedTaggedSentences.get(x).getTags().get(i) + " "
							+ predictedTaggedSentences.get(x).getWords().get(i) + " " + x + " " + i + " \t\tContext - "
							+ getLeft2Right2(predictedTaggedSentences.get(x), i));

				}
			}
		}

		errorOutputFile.writeToFile(cm.printDescendingClassDistributionandAccuracy());
		errorOutputFile.writeToFile("Average Precision: " + Double.toString(cm.getAvgPrecision()));
		errorOutputFile.writeToFile("Average Recall: " + Double.toString(cm.getAvgRecall()));
		errorOutputFile
				.writeToFile("Accuracy: " + cm.getAccuracy() + "(" + cm.getCorrect() + "/" + cm.getTotalSum() + ")");

		System.out.println(errorOutputFilename);
		System.out.println("Average Precision: " + Double.toString(cm.getAvgPrecision()));
		System.out.println("Average Recall: " + Double.toString(cm.getAvgRecall()));
		System.out.println("Accuracy: " + cm.getAccuracy() + "(" + cm.getCorrect() + "/" + cm.getTotalSum() + ")");

		errorOutputFile.writeToFile("GoldTag\tPredTag\tWord\tSent#\tWord#");
		Collections.sort(errorList);
		for (String e : errorList)
			errorOutputFile.writeToFile(e);

		errorOutputFile.writeToFile(cm.toString());
		errorOutputFile.close();
		if (predictedTagsFile != null)
			predictedTagsFile.close();
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

		return sb.toString();
	}

}
