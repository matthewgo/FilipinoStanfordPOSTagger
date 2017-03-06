package utils;

import java.util.ArrayList;
import java.util.List;

import test.models.TaggedSentence;

public class TaggedSentenceService {
	public static List<TaggedSentence> getTaggedSentences(List<String> stanfordTaggedSentences) {
		List<TaggedSentence> taggedSentences = new ArrayList<>();
		for (int k = 0; k < stanfordTaggedSentences.size(); k++) {
			String s = stanfordTaggedSentences.get(k);
			String[] split = s.split(" ");
			List<String> words = new ArrayList<>();
			List<String> tags = new ArrayList<>();
			for (int i = 0; i < split.length; i++) {
				String[] split2 = split[i].split("\\|");
				words.add(split2[0]);
				tags.add(split2[1]);
			}
			taggedSentences.add(new TaggedSentence(k, words, tags, s));
		}
		return taggedSentences;
	}

	public static TaggedSentence getTaggedSentence(int index, String stanfordTagged) {
		String[] split = stanfordTagged.split(" ");
		List<String> words = new ArrayList<>();
		List<String> tags = new ArrayList<>();
		for (int i = 0; i < split.length; i++) {
			String[] split2 = split[i].split("\\|");
			words.add(split2[0]);
			tags.add(split2[1]);
		}
		return new TaggedSentence(index, words, tags, stanfordTagged);
	}

	public static String getString(TaggedSentence ts) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < ts.getTags().size(); i++) {
			sb.append(ts.getWords().get(i) + "|" + ts.getTags().get(i));
			if (i < ts.getTags().size() - 1)
				sb.append(" ");
		}
		return sb.toString();
	}
}
