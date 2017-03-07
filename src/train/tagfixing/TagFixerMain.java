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

		tagsOverwriter.reviseEnglishNNCasFW(Constants.TEST_STANFORD_TAGGED,
				Constants.TEST_STANFORD_TAGGED_REVISED_ENG_NOUNS);

		jcdReviser.reviseJoeyTestData(Constants.TEST_STANFORD_TAGGED, Constants.TEST_STANFORD_TAGGED_JOEY_CHECKED);
		jcdReviser.reviseJoeyTestData(Constants.TEST_STANFORD_TAGGED_REVISED_ENG_NOUNS,
				Constants.TEST_STANFORD_TAGGED_REVISED_ENG_NOUNS_JOEY_CHECKED);

		tagsOverwriter.reviseEnglishNNCasFW(Constants.TEST_STANFORD_TAGGED_JOEY_CHECKED,
				Constants.TEST_STANFORD_TAGGED_JOEY_CHECKED_REVISED_ENG_NOUNS);
	}

}
