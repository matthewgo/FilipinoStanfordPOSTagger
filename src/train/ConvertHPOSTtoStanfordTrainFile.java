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
		List<String> testWords = FileManager.readFile(new File(Constants.TEST_HPOST_WORDS));
		List<String> testTags = FileManager.readFile(new File(Constants.TEST_HPOST_TAGS));

		createTrainFile(trainWords, trainTags);
		//
		createTestFile(testWords, testTags);

		createTestUntaggedFile(testWords, testTags);

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
