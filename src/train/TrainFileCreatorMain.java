package train;

import java.io.FileNotFoundException;
import java.io.IOException;

import utils.Constants;
import utils.HPOSTandStanfordFormatConverter;

public class TrainFileCreatorMain {

	public static void main(String[] args) throws FileNotFoundException, IOException {
		HPOSTandStanfordFormatConverter.HPOSTToStanford(Constants.TRAIN_HPOST_WORDS, Constants.TRAIN_HPOST_TAGS,
				Constants.TRAIN_STANFORD, true);
		HPOSTandStanfordFormatConverter.HPOSTToStanford(Constants.TEST_HPOST_WORDS, Constants.TEST_HPOST_TAGS,
				Constants.TEST_STANFORD_TAGGED, true);
		HPOSTandStanfordFormatConverter.HPOSTToStanford(Constants.TEST_HPOST_WORDS, null,
				Constants.TEST_STANFORD_UNTAGGED, false);

		HPOSTandStanfordFormatConverter.HPOSTToStanford(Constants.TEST_HPOST_WORDS_SECOND_HALF,
				Constants.TEST_HPOST_TAGS_SECOND_HALF, Constants.TEST_STANFORD_TAGGED_SECOND_HALF, true);
		HPOSTandStanfordFormatConverter.HPOSTToStanford(Constants.TEST_HPOST_WORDS_SECOND_HALF,
				Constants.TEST_HPOST_TAGS_SECOND_HALF, Constants.TEST_STANFORD_UNTAGGED_SECOND_HALF, false);
	}

}
