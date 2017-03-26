package test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import models.ConfusionMatrix;
import models.TaggedSentence;
import train.tagfixing.TagsOverwriter;
import utils.Constants;
import utils.FileManager;
import utils.TaggedSentenceService;

public class ListTaggingErrors {

	static FileManager resultsSummaryFile;

	public static void main(String[] args) throws FileNotFoundException, IOException {

		resultsSummaryFile = new FileManager("data/results/error_summary.csv");
		resultsSummaryFile.createFile();
		resultsSummaryFile.writeToFile(
				"Training Data Size,Test Data Size,Test Tokens Size,Tagging Results Filename, Gold File, Correct Tags,Accuracy,Precision, Recall,Remarks");

		TagsOverwriter tagsOverwriter = new TagsOverwriter();
		POSTagger left5WordsOWLQN2DistsimPref6Inf2Tagger = new POSTagger(
				Constants.LEFT5WORDS_OWLQN2_DISTSIM_PREF6_INF2);

		List<TaggedSentence> left5WordsOWLQN2DistsimPref6Inf2TaggedSentences = left5WordsOWLQN2DistsimPref6Inf2Tagger
				.tagFile(Constants.TEST_STANFORD_UNTAGGED);

		POSTagger.saveTagsIntoFile(left5WordsOWLQN2DistsimPref6Inf2TaggedSentences,
				Constants.TAGGING_RESULTS_LEFT5WORDS_OWLQN2_DISTSIM_PREF6_INF2);
		// or

		for (String taggingResultsFile : Constants.getTaggingResultsFiles()) {
			for (String testFile : Constants.getTestFiles()) {
				List<TaggedSentence> taggedSentences = TaggedSentenceService
						.getTaggedSentencesFromFile(taggingResultsFile);
				compareResults(taggedSentences, taggingResultsFile, testFile, false, null, "");
				tagsOverwriter.reviseEnglishNNCasFW(taggedSentences);
				compareResults(taggedSentences, taggingResultsFile, testFile, false, null, "overwrote EngNNCAsFW");
			}

		}

		resultsSummaryFile.close();
	}

	private static void compareResults(List<TaggedSentence> predictedTaggedSentences, String taggingResultsFilename,
			String goldFilename, boolean writeToErrorFile, String errorOutputFilename, String remarks)
					throws IOException {
		List<TaggedSentence> gold = TaggedSentenceService.getTaggedSentencesFromFile(goldFilename);

		List<String> errorList = new ArrayList<>();
		ConfusionMatrix cm = new ConfusionMatrix();

		for (int x = 0; x < predictedTaggedSentences.size(); x++) {
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
		String removedTaggingResultsFilenamePrefix = taggingResultsFilename.split("_results_")[1];
		String removedGoldFilenamePrefix = goldFilename.split("test-")[1];

		System.out.println(removedTaggingResultsFilenamePrefix + " | " + removedGoldFilenamePrefix);
		System.out.println("Average Precision: " + Double.toString(cm.getAvgPrecision()));
		System.out.println("Average Recall: " + Double.toString(cm.getAvgRecall()));
		System.out.println("Accuracy: " + cm.getAccuracy() + "(" + cm.getCorrect() + "/" + cm.getTotalSum() + ")");

		resultsSummaryFile.writeToFile("12134," + predictedTaggedSentences.size() + "," + cm.getTotalSum() + ","
				+ removedTaggingResultsFilenamePrefix + "," + removedGoldFilenamePrefix + "," + cm.getCorrect() + ","
				+ cm.getAccuracy() + "," + cm.getAvgPrecision() + "," + cm.getAvgRecall() + "," + remarks);

		if (writeToErrorFile) {
			FileManager errorOutputFile = new FileManager(errorOutputFilename);
			errorOutputFile.createFile();
			errorOutputFile.writeToFile(cm.printDescendingClassDistributionandAccuracy());
			errorOutputFile.writeToFile("Average Precision: " + Double.toString(cm.getAvgPrecision()));
			errorOutputFile.writeToFile("Average Recall: " + Double.toString(cm.getAvgRecall()));
			errorOutputFile.writeToFile(
					"Accuracy: " + cm.getAccuracy() + "(" + cm.getCorrect() + "/" + cm.getTotalSum() + ")");
			errorOutputFile.writeToFile("GoldTag\tPredTag\tWord\tSent#\tWord#");
			Collections.sort(errorList);
			for (String e : errorList)
				errorOutputFile.writeToFile(e);

			errorOutputFile.writeToFile(cm.toString());
			errorOutputFile.close();
		}
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
