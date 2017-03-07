package train.tagfixing;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import models.TaggedSentence;
import utils.Constants;
import utils.FileManager;
import utils.TaggedSentenceService;

public class JoeyCheckedDataReviser {
	// Updates the data based on *CHECK* marks made by joey on
	// tagging_errors.txt produced by ListTaggingErrors.java
	public void reviseJoeyTestData(String orig, String revised) throws FileNotFoundException, IOException {
		List<TaggingCorrection> corrections = getCorrectionsFromFile(Constants.CHECKED_TEST_DATA_TAGGING_ERRORS);
		applyCorrections(corrections, orig, revised);
	}

	private void applyCorrections(List<TaggingCorrection> corrections, String orig, String revised)
			throws FileNotFoundException, IOException {
		List<TaggedSentence> origLines = TaggedSentenceService.getTaggedSentences(FileManager.readFile(new File(orig)));
		FileManager revisedFile = new FileManager(revised);
		revisedFile.createFile();
		for (TaggingCorrection tc : corrections) {
			if (origLines.get(tc.getSentenceNumber()).getWords().get(tc.getWordNumber()).equals(tc.getWord())
					&& origLines.get(tc.getSentenceNumber()).getTags().get(tc.getWordNumber())
							.equals(tc.getGoldTag())) {
				origLines.get(tc.getSentenceNumber()).getTags().set(tc.getWordNumber(), tc.getPredictedTag());
			}
		}

		for (TaggedSentence ts : origLines) {
			revisedFile.writeToFile(TaggedSentenceService.getString(ts));
		}
		revisedFile.close();
	}

	private List<TaggingCorrection> getCorrectionsFromFile(String CHECKED_TEST_DATA_TAGGING_ERRORS)
			throws FileNotFoundException, IOException {
		List<String> lines = FileManager.readFile(new File(CHECKED_TEST_DATA_TAGGING_ERRORS));
		List<TaggingCorrection> corrections = new ArrayList<>();
		for (String line : lines) {
			String[] split = line.split("\\s+");
			if (split[0].contains("CHECK")) {
				String goldTag = split[1].split(":")[1];
				String predictedTag = split[2].split(":")[1];
				String word = split[3];
				int sentenceNumber = Integer.parseInt(split[4]);
				int wordNumber = Integer.parseInt(split[5]);
				System.out.println(word + " " + goldTag + " " + predictedTag + " " + sentenceNumber + " " + wordNumber);
				corrections.add(new TaggingCorrection(word, goldTag, predictedTag, sentenceNumber, wordNumber));
			}
		}
		return corrections;
	}
}

class TaggingCorrection {
	private String word;
	private String goldTag;
	private String predictedTag;
	private int sentenceNumber;
	private int wordNumber;

	public TaggingCorrection(String word, String goldTag, String predictedTag, int sentenceNumber, int wordNumber) {
		super();
		this.word = word;
		this.goldTag = goldTag;
		this.predictedTag = predictedTag;
		this.sentenceNumber = sentenceNumber;
		this.wordNumber = wordNumber;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public String getGoldTag() {
		return goldTag;
	}

	public void setGoldTag(String goldTag) {
		this.goldTag = goldTag;
	}

	public String getPredictedTag() {
		return predictedTag;
	}

	public void setPredictedTag(String predictedTag) {
		this.predictedTag = predictedTag;
	}

	public int getSentenceNumber() {
		return sentenceNumber;
	}

	public void setSentenceNumber(int sentenceNumber) {
		this.sentenceNumber = sentenceNumber;
	}

	public int getWordNumber() {
		return wordNumber;
	}

	public void setWordNumber(int wordNumber) {
		this.wordNumber = wordNumber;
	}
}
