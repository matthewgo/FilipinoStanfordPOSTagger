package utils;

public class Constants {

	// Tagger Models
	public static String LEFT3WORDS = "tagger_models/filipino-left3words-cg.tagger";
	public static String LEFT3WORDS_REVISED_ENG_NOUNS = "tagger_models/filipino-left3words-cg-revised-eng-nouns.tagger";

	// Dictionary Lookup for post-processing
	public static String ENGLISH_NOUNS = "data/english_words/nounlist.txt";

	// HPOST Training Data
	public static String TRAIN_HPOST_WORDS = "data/HPOST/trainHPOST.words";
	public static String TRAIN_HPOST_TAGS = "data/HPOST/trainHPOST.tags";
	public static String TEST_HPOST_WORDS = "data/HPOST/testHPOST.words";
	public static String TEST_HPOST_TAGS = "data/HPOST/testHPOST.tags";
	public static String TEST_HPOST_WORDS_SECOND_HALF = "data/HPOST/testHPOST-secondhalf.words";
	public static String TEST_HPOST_TAGS_SECOND_HALF = "data/HPOST/testHPOST-secondhalf.tags";

	// Stanford (Produced from HPOST) Traning Data
	public static String TRAIN_STANFORD = "data/stanford/train-filipino";
	public static String TEST_STANFORD_TAGGED = "data/stanford/test-filipino-tagged";
	public static String TEST_STANFORD_UNTAGGED = "data/stanford/test-filipino-untagged";
	public static String TEST_STANFORD_TAGGED_SECOND_HALF = "data/stanford/test-filipino-tagged-secondhalf";
	public static String TEST_STANFORD_UNTAGGED_SECOND_HALF = "data/stanford/test-filipino-untagged-secondhalf";

	public static String TRAIN_STANFORD_REVISED_ENG_NOUNS = "data/stanford/train-filipino-revised-eng-nouns";
	public static String TEST_STANFORD_TAGGED_REVISED_ENG_NOUNS = "data/stanford/test-filipino-tagged-revised-eng-nouns";

	public static String TEST_STANFORD_TAGGED_JOEY_CHECKED = "data/stanford/test-filipino-tagged-joey-checked";
	public static String TEST_STANFORD_TAGGED_REVISED_ENG_NOUNS_JOEY_CHECKED = "data/stanford/test-filipino-tagged-revised-eng-nouns-joey-checked";
	public static String TEST_STANFORD_TAGGED_JOEY_CHECKED_REVISED_ENG_NOUNS = "data/stanford/test-filipino-tagged-joey-checked-revised-eng-nouns";

	public static String TEST_HPOST_WORDS_TAGGED_JOEY_CHECKED_REVISED_ENG_NOUNS = "data/HPOST/testHPOST-joey-checked-revised-eng-nouns.words";
	public static String TEST_HPOST_TAGS_TAGGED_JOEY_CHECKED_REVISED_ENG_NOUNS = "data/HPOST/testHPOST-joey-checked-revised-eng-nouns.tags";

	/* Tagging Errors File */
	public static String TAGGING_ERROR_LEFT3WORDS = "data/Eclipse tagging/tagging_errors_left3words.txt";
	public static String TAGGING_ERROR_LEFT3WORDS_JOEY_CHECKED = "data/Eclipse tagging/tagging_errors_left3words_joey_checked.txt";
	public static String TAGGING_ERROR_LEFT3WORDS_JOEY_CHECKED_REVISED_ENG_NOUNS = "data/Eclipse tagging/tagging_errors_left3words_joey_checked_revised_eng_nouns.txt";
	public static String TAGGING_ERROR_LEFT3WORDS_TREN = "data/Eclipse tagging/tagging_errors_left3words_tren.txt";
	public static String TAGGING_ERROR_LEFT3WORDS_TREN_JOEY_CHECKED = "data/Eclipse tagging/tagging_errors_left3words_tren_joey_checked.txt";
	public static String TAGGING_ERROR_LEFT3WORDS_TREN_JOEY_CHECKED_REVISED_ENG_NOUNS = "data/Eclipse tagging/tagging_errors_left3words_tren_joey_checked_revised_eng_nouns.txt";

	public static String TAGGING_RESULTS_LEFT3WORDS = "data/Eclipse tagging/tagging_results_left3words.txt";
	public static String TAGGING_RESULTS_LEFT3WORDS_JOEY_CHECKED = "data/Eclipse tagging/tagging_results_left3words_joey_checked.txt";
	public static String TAGGING_RESULTS_LEFT3WORDS_JOEY_CHECKED_REVISED_ENG_NOUNS = "data/Eclipse tagging/tagging_results_left3words_joey_checked_revised_eng_nouns.txt";
	public static String TAGGING_RESULTS_LEFT3WORDS_TREN = "data/Eclipse tagging/tagging_results_left3words_tren.txt";
	public static String TAGGING_RESULTS_LEFT3WORDS_TREN_JOEY_CHECKED = "data/Eclipse tagging/tagging_results_left3words_tren_joey_checked.txt";
	public static String TAGGING_RESULTS_LEFT3WORDS_TREN_JOEY_CHECKED_REVISED_ENG_NOUNS = "data/Eclipse tagging/tagging_results_left3words_tren_joey_checked_revised_eng_nouns.txt";

	/* Tagging Errors File - Second Half - for Joey */
	public static String TAGGING_ERROR_SECOND_HALF_LEFT3WORDS = "data/Eclipse tagging/tagging_errors_second_half_left3words.txt";
	public static String TAGGING_RESULTS_SECOND_HALF_LEFT3WORDS = "data/Eclipse tagging/tagging_results_left3words_second_half.txt";
	// Joey's correction for test data results.
	public static String CHECKED_TEST_DATA_TAGGING_ERRORS = "data/results/left3words_tagging_errors_CHECKING_for_eclipse.txt";

}
