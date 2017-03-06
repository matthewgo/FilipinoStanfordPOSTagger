package train.tagfixing;

import java.io.FileNotFoundException;
import java.io.IOException;

import utils.Constants;

public class TagFixer {
	private static TagsOverwriter tagsOverwriter;

	public static void main(String[] args) throws FileNotFoundException, IOException {
		tagsOverwriter = new TagsOverwriter();

		tagsOverwriter.reviseEnglishNNCasFW(Constants.TRAIN_STANFORD, Constants.TRAIN_STANFORD_REVISED_ENG_NOUNS);
	}

}
