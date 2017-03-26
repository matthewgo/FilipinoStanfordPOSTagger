package train.tagfixing;

import java.io.FileNotFoundException;
import java.io.IOException;

import utils.Constants;

public class TagFixerMain {
	private static TagsOverwriter tagsOverwriter;
	private static JoeyCheckedDataReviser jcdReviser;

	public static void main(String[] args) throws FileNotFoundException, IOException {
		tagsOverwriter = new TagsOverwriter();
		jcdReviser = new JoeyCheckedDataReviser();
		// clean symbols

		tagsOverwriter.reviseEnglishNNCasFWFromFile(Constants.TRAIN_STANFORD,
				Constants.TRAIN_STANFORD_REVISED_ENG_NOUNS);
		tagsOverwriter.reviseEnglishNNCasFWFromFile(Constants.TEST_STANFORD_TAGGED,
				Constants.TEST_STANFORD_TAGGED_REVISED_ENG_NOUNS);

		jcdReviser.reviseJoeyTestData(Constants.TEST_STANFORD_TAGGED, Constants.TEST_STANFORD_TAGGED_JOEY_CHECKED,
				Constants.CHECKED_TEST_DATA_TAGGING_ERRORS, 0);
		jcdReviser.reviseJoeyTestData(Constants.TEST_STANFORD_TAGGED_JOEY_CHECKED,
				Constants.TEST_STANFORD_TAGGED_JOEY_CHECKED, Constants.CHECKED_TEST_DATA_TAGGING_ERRORS_SECOND_HALF,
				1632);

		tagsOverwriter.reviseEnglishNNCasFWFromFile(Constants.TEST_STANFORD_TAGGED_JOEY_CHECKED,
				Constants.TEST_STANFORD_TAGGED_JOEY_CHECKED_REVISED_ENG_NOUNS);

	}

}
