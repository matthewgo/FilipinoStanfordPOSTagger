package utils;

import java.util.Arrays;
import java.util.List;

public class Constants {

	// LEGEND:
	// TREN - Training Revised English Nouns
	// UTD - Updated Training Data

	// Tagger Models
	public static String LEFT3WORDS_CG = "tagger_models/filipino-left3words-cg.tagger";
	public static String LEFT3WORDS_OWLQN2 = "tagger_models/filipino-left3words-owlqn2.tagger";
	public static String LEFT3WORDS_OWLQN2_DISTSIM = "tagger_models/filipino-left3words-owlqn2-distsim.tagger";
	public static String LEFT3WORDS_OWLQN2_DISTSIM_PREF6 = "tagger_models/filipino-left3words-owlqn2-distsim-pref6.tagger";
	public static String LEFT3WORDS_OWLQN2_PREF6 = "tagger_models/filipino-left3words-owlqn2-pref6.tagger";
	public static String LEFT3WORDS_OWLQN2_PREF6_INF2 = "tagger_models/filipino-left3words-owlqn2-pref6-inf2.tagger";
	public static String LEFT5WORDS_OWLQN2 = "tagger_models/filipino-left5words-owlqn2.tagger";
	public static String LEFT5WORDS_OWLQN2_PREF6 = "tagger_models/filipino-left5words-owlqn2-pref6.tagger";
	public static String LEFT5WORDS_OWLQN2_PREF6_TREN = "tagger_models/filipino-left5words-owlqn2-pref6-tren.tagger";
	public static String LEFT5WORDS_OWLQN2_DISTSIM = "tagger_models/filipino-left5words-owlqn2-distsim.tagger";
	public static String LEFT5WORDS_OWLQN2_DISTSIM_PREF6 = "tagger_models/filipino-left5words-owlqn2-distsim-pref6.tagger";
	public static String LEFT5WORDS_OWLQN2_PREF6_INF2 = "tagger_models/filipino-left5words-owlqn2-pref6-inf2.tagger";
	// Dictionary Lookup for post-processing
	public static String ENGLISH_NOUNS = "data/english_words/nounlist.txt";

	// HPOST Training Data
	public static String TRAIN_HPOST_WORDS = "data/HPOST/trainHPOST.words";
	public static String TRAIN_HPOST_TAGS = "data/HPOST/trainHPOST.tags";
	public static String TEST_HPOST_WORDS = "data/HPOST/testHPOST.words";
	public static String TEST_HPOST_TAGS = "data/HPOST/testHPOST.tags";
	public static String TEST_HPOST_WORDS_SECOND_HALF = "data/HPOST/testHPOST-secondhalf.words";
	public static String TEST_HPOST_TAGS_SECOND_HALF = "data/HPOST/testHPOST-secondhalf.tags";

	// Stanford (Produced from HPOST) Training Data
	public static String TRAIN_STANFORD = "data/stanford/train-filipino";
	public static String TEST_STANFORD_TAGGED = "data/stanford/test-filipino-tagged";
	public static String TEST_STANFORD_UNTAGGED = "data/stanford/test-filipino-untagged";
	public static String TEST_STANFORD_TAGGED_SECOND_HALF = "data/stanford/test-filipino-tagged-secondhalf";
	public static String TEST_STANFORD_UNTAGGED_SECOND_HALF = "data/stanford/test-filipino-untagged-secondhalf";

	public static String TRAIN_STANFORD_REVISED_ENG_NOUNS = "data/stanford/train-filipino-revised-eng-nouns";
	public static String TEST_STANFORD_TAGGED_REVISED_ENG_NOUNS = "data/stanford/test-filipino-tagged-revised-eng-nouns";

	public static String TEST_STANFORD_TAGGED_JOEY_CHECKED = "data/stanford/test-filipino-tagged-joey-checked";
	public static String TEST_STANFORD_TAGGED_JOEY_CHECKED_WITH_SECOND_HALF = "data/stanford/test-filipino-tagged-joey-checked-with-second-half";
	public static String TEST_STANFORD_TAGGED_JOEY_CHECKED_REVISED_ENG_NOUNS = "data/stanford/test-filipino-tagged-joey-checked-revised-eng-nouns";

	public static String TEST_HPOST_WORDS_TAGGED_JOEY_CHECKED_REVISED_ENG_NOUNS = "data/HPOST/testHPOST-joey-checked-revised-eng-nouns.words";
	public static String TEST_HPOST_TAGS_TAGGED_JOEY_CHECKED_REVISED_ENG_NOUNS = "data/HPOST/testHPOST-joey-checked-revised-eng-nouns.tags";

	/* Tagging Errors File */
	public static String TAGGING_ERROR_LEFT3WORDS_CG = "data/Eclipse tagging/tagging_errors_left3words_cg.txt";
	public static String TAGGING_ERROR_LEFT3WORDS_CG_JOEY_CHECKED = "data/Eclipse tagging/tagging_errors_left3words_cg_joey_checked.txt";
	public static String TAGGING_ERROR_LEFT3WORDS_CG_JOEY_CHECKED_REVISED_ENG_NOUNS = "data/Eclipse tagging/tagging_errors_left3words_cg_joey_checked_revised_eng_nouns.txt";
	public static String TAGGING_ERROR_LEFT3WORDS_OWLQN2 = "data/Eclipse tagging/tagging_errors_left3words_owlqn2.txt";
	public static String TAGGING_ERROR_LEFT3WORDS_OWLQN2_JOEY_CHECKED = "data/Eclipse tagging/tagging_errors_left3words_owlqn2_joey_checked.txt";
	public static String TAGGING_ERROR_LEFT3WORDS_OWLQN2_JOEY_CHECKED_REVISED_ENG_NOUNS = "data/Eclipse tagging/tagging_errors_left3words_owlqn2_joey_checked_revised_eng_nouns.txt";
	public static String TAGGING_ERROR_LEFT3WORDS_OWLQN2_DISTSIM = "data/Eclipse tagging/tagging_errors_left3words_owlqn2_distsim.txt";
	public static String TAGGING_ERROR_LEFT3WORDS_OWLQN2_DISTSIM_JOEY_CHECKED = "data/Eclipse tagging/tagging_errors_left3words_owlqn2_distsim_joey_checked.txt";
	public static String TAGGING_ERROR_LEFT3WORDS_OWLQN2_DISTSIM_JOEY_CHECKED_REVISED_ENG_NOUNS = "data/Eclipse tagging/tagging_errors_left3words_owlqn2_distsim_joey_checked_revised_eng_nouns.txt";
	public static String TAGGING_ERROR_LEFT3WORDS_OWLQN2_DISTSIM_PREF6 = "data/Eclipse tagging/tagging_errors_left3words_owlqn2_distsim_pref6.txt";
	public static String TAGGING_ERROR_LEFT3WORDS_OWLQN2_DISTSIM_PREF6_JOEY_CHECKED = "data/Eclipse tagging/tagging_errors_left3words_owlqn2_distsim_pref6_joey_checked.txt";
	public static String TAGGING_ERROR_LEFT3WORDS_OWLQN2_DISTSIM_PREF6_JOEY_CHECKED_REVISED_ENG_NOUNS = "data/Eclipse tagging/tagging_errors_left3words_owlqn2_distsim_pref6_joey_checked_revised_eng_nouns.txt";
	public static String TAGGING_ERROR_LEFT3WORDS_OWLQN2_PREF6 = "data/Eclipse tagging/tagging_errors_left3words_owlqn2_pref6.txt";
	public static String TAGGING_ERROR_LEFT3WORDS_OWLQN2_PREF6_JOEY_CHECKED = "data/Eclipse tagging/tagging_errors_left3words_owlqn2_pref6_joey_checked.txt";
	public static String TAGGING_ERROR_LEFT3WORDS_OWLQN2_PREF6_JOEY_CHECKED_REVISED_ENG_NOUNS = "data/Eclipse tagging/tagging_errors_left3words_owlqn2_pref6_joey_checked_revised_eng_nouns.txt";
	public static String TAGGING_ERROR_LEFT3WORDS_OWLQN2_PREF6_INF2 = "data/Eclipse tagging/tagging_errors_left3words_owlqn2_pref6_inf2.txt";
	public static String TAGGING_ERROR_LEFT3WORDS_OWLQN2_PREF6_INF2_JOEY_CHECKED = "data/Eclipse tagging/tagging_errors_left3words_owlqn2_pref6_inf2_joey_checked.txt";
	public static String TAGGING_ERROR_LEFT3WORDS_OWLQN2_PREF6_INF2_JOEY_CHECKED_REVISED_ENG_NOUNS = "data/Eclipse tagging/tagging_errors_left3words_owlqn2_pref6_inf2_joey_checked_revised_eng_nouns.txt";

	public static String TAGGING_ERROR_LEFT5WORDS_OWLQN2 = "data/Eclipse tagging/tagging_errors_left5words_owlqn2.txt";
	public static String TAGGING_ERROR_LEFT5WORDS_OWLQN2_JOEY_CHECKED = "data/Eclipse tagging/tagging_errors_left5words_owlqn2_joey_checked.txt";
	public static String TAGGING_ERROR_LEFT5WORDS_OWLQN2_JOEY_CHECKED_REVISED_ENG_NOUNS = "data/Eclipse tagging/tagging_errors_left5words_owlqn2_joey_checked_revised_eng_nouns.txt";
	public static String TAGGING_ERROR_LEFT5WORDS_OWLQN2_PREF6 = "data/Eclipse tagging/tagging_errors_left5words_owlqn2_pref6.txt";
	public static String TAGGING_ERROR_LEFT5WORDS_OWLQN2_PREF6_JOEY_CHECKED = "data/Eclipse tagging/tagging_errors_left5words_owlqn2_pref6_joey_checked.txt";
	public static String TAGGING_ERROR_LEFT5WORDS_OWLQN2_PREF6_JOEY_CHECKED_REVISED_ENG_NOUNS = "data/Eclipse tagging/tagging_errors_left5words_owlqn2_pref6_joey_checked_revised_eng_nouns.txt";
	public static String TAGGING_ERROR_LEFT5WORDS_OWLQN2_DISTSIM = "data/Eclipse tagging/tagging_errors_left5words_owlqn2_distsim.txt";
	public static String TAGGING_ERROR_LEFT5WORDS_OWLQN2_DISTSIM_JOEY_CHECKED = "data/Eclipse tagging/tagging_errors_left5words_owlqn2_distsim_joey_checked.txt";
	public static String TAGGING_ERROR_LEFT5WORDS_OWLQN2_DISTSIM_JOEY_CHECKED_REVISED_ENG_NOUNS = "data/Eclipse tagging/tagging_errors_left5words_owlqn2_distsim_joey_checked_revised_eng_nouns.txt";
	public static String TAGGING_ERROR_LEFT5WORDS_OWLQN2_DISTSIM_PREF6 = "data/Eclipse tagging/tagging_errors_left5words_owlqn2_distsim_pref6.txt";
	public static String TAGGING_ERROR_LEFT5WORDS_OWLQN2_DISTSIM_PREF6_JOEY_CHECKED = "data/Eclipse tagging/tagging_errors_left5words_owlqn2_distsim_pref6_joey_checked.txt";
	public static String TAGGING_ERROR_LEFT5WORDS_OWLQN2_DISTSIM_PREF6_JOEY_CHECKED_REVISED_ENG_NOUNS = "data/Eclipse tagging/tagging_errors_left5words_owlqn2_distsim_pref6_joey_checked_revised_eng_nouns.txt";
	public static String TAGGING_ERROR_LEFT5WORDS_OWLQN2_PREF6_TREN = "data/Eclipse tagging/tagging_errors_left5words_owlqn2_pref6_tren.txt";
	public static String TAGGING_ERROR_LEFT5WORDS_OWLQN2_PREF6_TREN_JOEY_CHECKED = "data/Eclipse tagging/tagging_errors_left5words_owlqn2_pref6_tren_joey_checked.txt";
	public static String TAGGING_ERROR_LEFT5WORDS_OWLQN2_PREF6_TREN_JOEY_CHECKED_REVISED_ENG_NOUNS = "data/Eclipse tagging/tagging_errors_left5words_owlqn2_pref6_tren_joey_checked_revised_eng_nouns.txt";
	public static String TAGGING_ERROR_LEFT5WORDS_OWLQN2_PREF6_INF2 = "data/Eclipse tagging/tagging_errors_left5words_owlqn2_pref6_inf2.txt";
	public static String TAGGING_ERROR_LEFT5WORDS_OWLQN2_PREF6_INF2_JOEY_CHECKED = "data/Eclipse tagging/tagging_errors_left5words_owlqn2_pref6_inf2_joey_checked.txt";
	public static String TAGGING_ERROR_LEFT5WORDS_OWLQN2_PREF6_INF2_JOEY_CHECKED_REVISED_ENG_NOUNS = "data/Eclipse tagging/tagging_errors_left5words_owlqn2_pref6_inf2_joey_checked_revised_eng_nouns.txt";

	/* Tagging Results File */
	public static String TAGGING_RESULTS_LEFT3WORDS_CG = "data/Eclipse tagging/tagging_results_left3words_cg.txt";
	public static String TAGGING_RESULTS_LEFT3WORDS_OWLQN2 = "data/Eclipse tagging/tagging_results_left3words_owlqn2.txt";
	public static String TAGGING_RESULTS_LEFT3WORDS_OWLQN2_PREF6 = "data/Eclipse tagging/tagging_results_left3words_owlqn2_pref6.txt";
	public static String TAGGING_RESULTS_LEFT3WORDS_OWLQN2_PREF6_INF2 = "data/Eclipse tagging/tagging_results_left3words_owlqn2_pref6_inf2.txt";

	public static String TAGGING_RESULTS_LEFT3WORDS_OWLQN2_DISTSIM = "data/Eclipse tagging/tagging_results_left3words_owlqn2_distsim.txt";
	public static String TAGGING_RESULTS_LEFT3WORDS_OWLQN2_DISTSIM_PREF6 = "data/Eclipse tagging/tagging_results_left3words_owlqn2_distsim_pref6.txt";
	public static String TAGGING_RESULTS_LEFT5WORDS_OWLQN2 = "data/Eclipse tagging/tagging_results_left5words_owlqn2.txt";
	public static String TAGGING_RESULTS_LEFT5WORDS_OWLQN2_PREF6 = "data/Eclipse tagging/tagging_results_left5words_owlqn2_pref6.txt";
	public static String TAGGING_RESULTS_LEFT5WORDS_OWLQN2_DISTSIM = "data/Eclipse tagging/tagging_results_left5words_owlqn2_distsim.txt";
	public static String TAGGING_RESULTS_LEFT5WORDS_OWLQN2_DISTSIM_PREF6 = "data/Eclipse tagging/tagging_results_left5words_owlqn2_distsim_pref6.txt";
	public static String TAGGING_RESULTS_LEFT5WORDS_OWLQN2_PREF6_TREN = "data/Eclipse tagging/tagging_results_left5words_owlqn2_pref6_tren.txt";
	public static String TAGGING_RESULTS_LEFT5WORDS_OWLQN2_PREF6_INF2 = "data/Eclipse tagging/tagging_results_left5words_owlqn2_pref6_inf2.txt";

	public static List<String> getTaggingResultsFiles() {
		return Arrays.asList(TAGGING_RESULTS_LEFT3WORDS_CG, TAGGING_RESULTS_LEFT3WORDS_OWLQN2,
				TAGGING_RESULTS_LEFT3WORDS_OWLQN2_PREF6, TAGGING_RESULTS_LEFT3WORDS_OWLQN2_PREF6_INF2,
				TAGGING_RESULTS_LEFT3WORDS_OWLQN2_DISTSIM, TAGGING_RESULTS_LEFT3WORDS_OWLQN2_DISTSIM_PREF6,
				TAGGING_RESULTS_LEFT5WORDS_OWLQN2, TAGGING_RESULTS_LEFT5WORDS_OWLQN2_PREF6,
				TAGGING_RESULTS_LEFT5WORDS_OWLQN2_DISTSIM, TAGGING_RESULTS_LEFT5WORDS_OWLQN2_DISTSIM_PREF6,
				TAGGING_RESULTS_LEFT5WORDS_OWLQN2_PREF6_TREN, TAGGING_RESULTS_LEFT5WORDS_OWLQN2_PREF6_INF2);
	}

	/* Tagging Errors File - Second Half - for Joey */
	public static String TAGGING_ERROR_SECOND_HALF_LEFT3WORDS = "data/Eclipse tagging/tagging_errors_second_half_left3words.txt";
	public static String TAGGING_RESULTS_SECOND_HALF_LEFT3WORDS = "data/Eclipse tagging/tagging_results_left3words_second_half.txt";

	// Joey's correction for test data results.
	public static String CHECKED_TEST_DATA_TAGGING_ERRORS = "data/results/left3words_tagging_errors_CHECKING_for_eclipse.txt";
	public static String CHECKED_TEST_DATA_TAGGING_ERRORS_SECOND_HALF = "data/results/left3words_tagging_errors_second_half_CHECKING_for_eclipse.txt";

	public static List<String> getTestFiles() {
		return Arrays.asList(TEST_STANFORD_TAGGED, TEST_STANFORD_TAGGED_JOEY_CHECKED,
				TEST_STANFORD_TAGGED_JOEY_CHECKED_REVISED_ENG_NOUNS);
	}
}
