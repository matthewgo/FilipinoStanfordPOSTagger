package test.old;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Tagging {

	public static void main(String[] args) throws FileNotFoundException, IOException {
		// tagWithFilipinoBidirectionalTaggerModel();
		// tagWithFilipinoLeft3WordsTaggerModel();
	}

	// private static void tagWithFilipinoLeft3WordsTaggerModel() throws
	// FileNotFoundException, IOException {
	// MaxentTagger tagger = new
	// MaxentTagger("data/filipino-left3words-distsim.tagger");
	// List<String> sentences = FileManager.readFile(new
	// File(Constants.TEST_STANFORD_UNTAGGED));
	// Scanner sc = new Scanner(System.in);
	// while (true) {
	// System.out.println("Input sentence to tag:");
	// String sentence = sc.nextLine();
	// if (sentence.equals("quit"))
	// break;
	// System.out.println(tagger.tagTokenizedString(sentence));
	// }
	// }
	//
	// private static void tagWithFilipinoBidirectionalTaggerModel() throws
	// FileNotFoundException, IOException {
	// MaxentTagger tagger = new MaxentTagger("data/filipino.tagger");
	// List<String> sentences = FileManager.readFile(new
	// File(Constants.TEST_STANFORD_UNTAGGED));
	//
	// FileManager temp_write = new FileManager(Constants.TEMP_TAGGING_RESULTS);
	//
	// int[] exclusions = { 3, 7, 10, 11, 21, 29, 30, 39, 43, 61, 83, 86, 102,
	// 106, 109, 144, 164, 219, 224, 232, 252,
	// 263, 311, 321, 339, 344, 350, 367, 390, 473, 506, 521, 535, 537, 583,
	// 599, 604 };
	// for (int i = 611; i < sentences.size(); i++) {
	// boolean shouldBeExcluded = false;
	// for (int x : exclusions) {
	// if (x == i)
	// shouldBeExcluded = true;
	// }
	// if (shouldBeExcluded == false) {
	// System.out.println(i + " " +
	// tagger.tagTokenizedString(sentences.get(i)));
	// temp_write.appendToFile(i + " " +
	// tagger.tagTokenizedString(sentences.get(i)));
	// temp_write.close();
	// }
	// }
	// }

}
