package train;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import utils.Constants;
import utils.FileManager;

public class ConvertHPOSTtoStanfordTrainFile {

	public static void main(String[] args) throws FileNotFoundException, IOException {
		List<String> trainWords = FileManager.readFile(new File(Constants.TRAIN_HPOST_WORDS));
		List<String> trainTags = FileManager.readFile(new File(Constants.TRAIN_HPOST_TAGS));
		List<String> trainFeatures = FileManager.readFile(new File(Constants.TRAIN_HPOST_FEATURES));
		List<String> testWords = FileManager.readFile(new File(Constants.TEST_HPOST_WORDS));
		List<String> testTags = FileManager.readFile(new File(Constants.TEST_HPOST_TAGS));
		List<String> testFeatures = FileManager.readFile(new File(Constants.TEST_HPOST_FEATURES));

		createTrainFile(trainWords, trainTags);
		//
		createTestFile(testWords, testTags);
		//
		// createTestUntaggedFile(testWords, testTags);
		//
		// createTestShortFile(testWords, testTags);
		//
		// createTestUntaggedShortFile(testWords, testTags);
		//
		// createTrainFeatureFile(trainTags, trainFeatures);
		//
		createTestFeatureFile(testTags, testFeatures);
	}

	private static void createTestFeatureFile(List<String> testTags, List<String> testFeatures) throws IOException {
		FileManager testFile = new FileManager(Constants.TEST_STANFORD_TAGGED_FEATURES);
		testFile.createFile();
		for (int i = 0; i < testFeatures.size(); i++) {
			String[] features = testFeatures.get(i).trim().split(" ");
			String[] tags = testTags.get(i).trim().split(" ");
			StringBuilder sb = new StringBuilder();

			for (int j = 0; j < features.length; j++) {
				sb.append(features[j] + "|" + tags[j]);
				if (j < features.length - 1)
					sb.append(" ");
			}
			System.out.println(sb.toString());
			testFile.writeToFile(sb.toString());
		}
		testFile.close();
	}

	private static void createTrainFeatureFile(List<String> trainTags, List<String> trainFeatures) throws IOException {
		FileManager trainFile = new FileManager(Constants.TRAIN_STANFORD_FEATURES);
		trainFile.createFile();
		for (int i = 0; i < trainFeatures.size(); i++) {
			String[] features = trainFeatures.get(i).trim().split(" ");
			String[] tags = trainTags.get(i).trim().split(" ");
			StringBuilder sb = new StringBuilder();

			for (int j = 0; j < features.length; j++) {
				sb.append(features[j] + "|" + tags[j]);
				if (j < features.length - 1)
					sb.append(" ");
			}
			System.out.println(sb.toString());
			trainFile.writeToFile(sb.toString());
		}
		trainFile.close();
	}

	private static void createTestUntaggedShortFile(List<String> testWords, List<String> testTags) throws IOException {
		FileManager testFileUntagged = new FileManager(Constants.TEST_STANFORD_UNTAGGED_SHORT);
		testFileUntagged.createFile();
		for (int i = 0; i < testWords.size(); i++) {
			String[] words = testWords.get(i).trim().split(" ");
			String[] tags = testTags.get(i).trim().split(" ");
			StringBuilder sb = new StringBuilder();

			// System.out.println(testWords.get(i));
			// System.out.println(testTags.get(i));
			// System.out.println("Line#" + i + " - " + words.length + " " +
			// tags.length);
			for (int j = 0; j < words.length; j++) {
				sb.append(words[j]);
				if (j < words.length - 1)
					sb.append(" ");
			}
			System.out.println(sb.toString());
			if (words.length <= 12)
				testFileUntagged.writeToFile(sb.toString());
		}
		testFileUntagged.close();
	}

	private static void createTestShortFile(List<String> testWords, List<String> testTags) throws IOException {
		FileManager testFile = new FileManager(Constants.TEST_STANFORD_TAGGED_SHORT);
		testFile.createFile();
		for (int i = 0; i < testWords.size(); i++) {
			String[] words = testWords.get(i).trim().split(" ");
			String[] tags = testTags.get(i).trim().split(" ");
			StringBuilder sb = new StringBuilder();

			// System.out.println(testWords.get(i));
			// System.out.println(testTags.get(i));
			// System.out.println("Line#" + i + " - " + words.length + " " +
			// tags.length);
			for (int j = 0; j < words.length; j++) {
				sb.append(words[j] + "|" + tags[j]);
				if (j < words.length - 1)
					sb.append(" ");
			}
			System.out.println(sb.toString());
			if (words.length <= 12)
				testFile.writeToFile(sb.toString());
		}
		testFile.close();
	}

	private static void createTestUntaggedFile(List<String> testWords, List<String> testTags) throws IOException {
		FileManager testFileUntagged = new FileManager(Constants.TEST_STANFORD_UNTAGGED);
		testFileUntagged.createFile();
		for (int i = 0; i < testWords.size(); i++) {
			String[] words = testWords.get(i).trim().split(" ");
			String[] tags = testTags.get(i).trim().split(" ");
			StringBuilder sb = new StringBuilder();

			// System.out.println(testWords.get(i));
			// System.out.println(testTags.get(i));
			// System.out.println("Line#" + i + " - " + words.length + " " +
			// tags.length);
			for (int j = 0; j < words.length; j++) {
				sb.append(words[j]);
				if (j < words.length - 1)
					sb.append(" ");
			}
			System.out.println(sb.toString());
			testFileUntagged.writeToFile(sb.toString());
		}
		testFileUntagged.close();
	}

	private static void createTestFile(List<String> testWords, List<String> testTags) throws IOException {
		FileManager testFile = new FileManager(Constants.TEST_STANFORD_TAGGED);
		testFile.createFile();
		for (int i = 0; i < testWords.size(); i++) {
			String[] words = testWords.get(i).trim().split(" ");
			String[] tags = testTags.get(i).trim().split(" ");
			StringBuilder sb = new StringBuilder();

			for (int j = 0; j < words.length; j++) {
				sb.append(words[j] + "|" + tags[j]);
				if (j < words.length - 1)
					sb.append(" ");
			}
			System.out.println(sb.toString());
			testFile.writeToFile(sb.toString());
		}
		testFile.close();
	}

	private static void createTrainFile(List<String> trainWords, List<String> trainTags) throws IOException {
		FileManager trainFile = new FileManager(Constants.TRAIN_STANFORD);
		trainFile.createFile();
		for (int i = 0; i < trainWords.size(); i++) {
			String[] words = trainWords.get(i).trim().split(" ");
			String[] tags = trainTags.get(i).trim().split(" ");
			StringBuilder sb = new StringBuilder();

			for (int j = 0; j < words.length; j++) {
				sb.append(words[j] + "|" + tags[j]);
				if (j < words.length - 1)
					sb.append(" ");
			}
			System.out.println(sb.toString());
			trainFile.writeToFile(sb.toString());
		}
		trainFile.close();
	}

}
