package test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import test.models.TaggedSentence;
import utils.Constants;
import utils.FileManager;

public class ScoreChecking {
	public static void main(String[] args) throws FileNotFoundException, IOException {
		List<String> linesPartial = FileManager.readFile(new File("data/partial-tagging-results.txt"));
		List<String> gold = FileManager.readFile(new File(Constants.TEST_STANFORD_TAGGED));

		List<TaggedSentence> taggerOutput = new ArrayList<>();
		List<TaggedSentence> goldOutput = new ArrayList<>();
		for (String s : linesPartial) {
			String[] split = s.split(" ");
			int index = Integer.parseInt(split[0]);
			List<String> words = new ArrayList<>();
			List<String> tags = new ArrayList<>();
			for (int i = 1; i < split.length; i++) {
				String[] split2 = split[i].split("\\|");
				words.add(split2[0]);
				tags.add(split2[1]);
			}
			taggerOutput.add(new TaggedSentence(index, words, tags, s));
		}
		System.out.println(taggerOutput.size());

		for (int k = 0; k < gold.size(); k++) {
			String s = gold.get(k);
			String[] split = s.split(" ");
			List<String> words = new ArrayList<>();
			List<String> tags = new ArrayList<>();
			for (int i = 0; i < split.length; i++) {
				String[] split2 = split[i].split("\\|");
				words.add(split2[0]);
				tags.add(split2[1]);
			}
			goldOutput.add(new TaggedSentence(k, words, tags, s));
		}
		System.out.println(goldOutput.size());

		int correct = 0;
		int wrong = 0;
		int unknown_wrong = 0;
		int totalTokens = 0;
		for (TaggedSentence g : goldOutput) {
			for (TaggedSentence t : taggerOutput) {
				if (g.getIndex() == t.getIndex()) {
					System.out.println(t.getSentence());
					for (int i = 0; i < g.getWords().size(); i++) {
						// System.out.println(g.getWords().get(i) + " " +
						// g.getTags().get(i) + " " + t.getWords().get(i)
						// + " " + t.getTags().get(i));
						totalTokens++;
						if (g.getTags().get(i).equals(t.getTags().get(i)))
							correct++;
						else if (g.getTags().get(i).charAt(0) == t.getTags().get(i).charAt(0))
							wrong++;
						else {
							System.out.println(g.getTags().get(i) + " " + t.getTags().get(i));
							wrong++;
							unknown_wrong++;
						}
					}
					System.out.println("Score Update: " + " T - " + totalTokens + " C - " + correct + " W - " + wrong
							+ " UW - " + unknown_wrong);
					System.out.println("Total # of Sentences: " + taggerOutput.size());
					System.out.println("Accuracy: " + correct * 1.00 / totalTokens * 100 + "%");
				}
			}
		}
	}
}
