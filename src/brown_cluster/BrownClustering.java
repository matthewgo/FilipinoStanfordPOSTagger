package brown_cluster;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.mutable.MutableDouble;
import org.apache.commons.lang3.mutable.MutableInt;

import be.bagofwords.ui.UI;
import be.bagofwords.util.NumUtils;
import be.bagofwords.util.Utils;
import edu.stanford.nlp.util.Pair;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;

/**
 * Created by Koen Deschacht (koendeschacht@gmail.com) on 02/12/14.
 * <p/>
 * Clustering algorithm of words and phrases described in: Class Based n-gram
 * Models of Natural Language, P.F. Brown, P.V. deSouza, R.L. Mercer, V.J.D.
 * Pietra, J.C. Lai See:
 * http://people.csail.mit.edu/imcgraw/links/research/pubs/ClassBasedNGrams.pdf
 */

public class BrownClustering {

	public static final boolean DO_TESTS = false; // you probably want to enable
													// this during development

	private static final String UNKNOWN_PHRASE = "_UNKNOWN_";

	private final String inputFile;
	private final String outputFile;
	private final int minFrequencyOfPhrase;
	private final int maxNumberOfClusters;
	private boolean onlySwapMostFrequentWords;

	public BrownClustering(String inputFile, String outputFile, int minFrequencyOfPhrase, int maxNumberOfClusters,
			boolean onlySwapMostFrequentWords) {
		this.inputFile = inputFile;
		this.outputFile = outputFile;
		this.minFrequencyOfPhrase = minFrequencyOfPhrase;
		this.maxNumberOfClusters = maxNumberOfClusters;
		this.onlySwapMostFrequentWords = onlySwapMostFrequentWords;
	}

	/**
	 * Read phrases from inputFile, run cluster algorithm and write cluster of
	 * every phrase to outputFile
	 */

	public void run() throws IOException {
		Pair<Map<Integer, String>, Int2IntOpenHashMap> readPhrases = readPhrases();
		Map<Integer, String> phraseMap = readPhrases.first();
		Int2IntOpenHashMap phraseFrequencies = readPhrases.second();
		System.out.println("Read " + phraseMap.size() + " phrases.");
		ContextCountsImpl contextCounts = extractContextCounts(phraseMap);
		doClustering(phraseMap, contextCounts, phraseFrequencies);
	}

	private void doClustering(Map<Integer, String> phraseMap, ContextCountsImpl phraseContextCounts,
			Int2IntOpenHashMap phraseFrequencies) throws IOException {
		/**
		 * STEP 1: create for every unique phrase a unique cluster
		 */
		Int2IntOpenHashMap phraseToClusterMap = initializeClusters(phraseMap.size());
		ContextCountsImpl clusterContextCounts = phraseContextCounts.clone(); // initially
																				// these
																				// counts
																				// are
																				// identical
		if (DO_TESTS) {
			ContextCountsUtils.checkCounts(clusterContextCounts, phraseToClusterMap, phraseContextCounts);
		}
		/**
		 * STEP 2: merge clusters of infrequent phrases with clusters of
		 * frequent phrases, and swap words among clusters
		 */
		if (onlySwapMostFrequentWords) {
			int numOfFrequentPhrases = Math.min(phraseMap.size(), maxNumberOfClusters * 10);
			mergeInfrequentPhrasesWithFrequentPhraseClusters(maxNumberOfClusters, numOfFrequentPhrases,
					phraseToClusterMap, clusterContextCounts);
			swapPhrases(0, numOfFrequentPhrases, phraseToClusterMap, clusterContextCounts, phraseContextCounts);
			mergeInfrequentPhrasesWithFrequentPhraseClusters(numOfFrequentPhrases, phraseMap.size(), phraseToClusterMap,
					clusterContextCounts);
		} else {
			mergeInfrequentPhrasesWithFrequentPhraseClusters(maxNumberOfClusters, phraseMap.size(), phraseToClusterMap,
					clusterContextCounts);
			swapPhrases(0, phraseMap.size(), phraseToClusterMap, clusterContextCounts, phraseContextCounts);
		}
		if (DO_TESTS) {
			ContextCountsUtils.checkCounts(clusterContextCounts, phraseToClusterMap, phraseContextCounts);
		}
		/**
		 * STEP 3: merge clusters hierarchically
		 */
		Map<Integer, ClusterHistoryNode> historyNodes = initializeHistoryNodes(phraseToClusterMap);
		mergeAllClusters(historyNodes, clusterContextCounts);
		writeOutput(phraseMap, phraseToClusterMap, historyNodes, phraseFrequencies);
	}

	/**
	 * see paragraph "We know of ..." on page 472 of [Brown et al.].
	 */

	private void mergeInfrequentPhrasesWithFrequentPhraseClusters(int startPhrase, int endPhrase,
			Int2IntOpenHashMap phraseToClusterMap, ContextCountsImpl clusterContextCounts) {
		for (int infrequentPhrase = startPhrase; infrequentPhrase < endPhrase; infrequentPhrase++) {
			int cluster = findBestClusterToMerge(infrequentPhrase, 0, maxNumberOfClusters, clusterContextCounts)
					.first();
			System.out.println("Will merge phrase " + infrequentPhrase + " with " + cluster);
			clusterContextCounts.mergeClusters(infrequentPhrase, cluster);
			phraseToClusterMap.put(infrequentPhrase, cluster);
		}
	}

	/**
	 * see paragraph "We know of ..." on page 472 of [Brown et al.].
	 */

	private void swapPhrases(int phraseStart, int phraseEnd, Int2IntOpenHashMap phraseToClusterMap,
			ContextCountsImpl clusterContextCounts, ContextCountsImpl phraseContextCounts) {
		int numOfPhrases = phraseEnd - phraseStart;
		int numOfPhrasesChangedInLastIteration = numOfPhrases;
		int iteration = 0;
		// continue swapping phrases until less then 1% of the phrases has
		// changed in the last iteration
		while (numOfPhrasesChangedInLastIteration * 100 > numOfPhrases) {
			numOfPhrasesChangedInLastIteration = 0;
			for (int phrase = phraseStart; phrase < phraseEnd; phrase++) {
				int currCluster = phraseToClusterMap.get(phrase);
				ContextCountsImpl contextCountsForPhrase = mapPhraseCountsToClusterCounts(phrase, phraseToClusterMap,
						phraseContextCounts, SwapWordContextCounts.DUMMY_CLUSTER);
				SwapWordContextCounts swapWordContextCounts = new SwapWordContextCounts(clusterContextCounts,
						contextCountsForPhrase, currCluster);
				Pair<Integer, Double> bestClusterScore = findBestClusterToMerge(SwapWordContextCounts.DUMMY_CLUSTER, 0,
						maxNumberOfClusters, swapWordContextCounts);
				double oldScore = computeMergeScore(SwapWordContextCounts.DUMMY_CLUSTER, 0.0, currCluster,
						swapWordContextCounts);
				if (bestClusterScore.first() != currCluster && bestClusterScore.second() > oldScore + 1e-10) {
					int newCluster = bestClusterScore.first();
					System.out.println("Iteration " + iteration + " assigning phrase " + phrase + " to cluster "
							+ newCluster + " (was cluster " + currCluster + ")");
					phraseToClusterMap.put(phrase, newCluster);
					clusterContextCounts.removeCounts(
							contextCountsForPhrase.mapCluster(SwapWordContextCounts.DUMMY_CLUSTER, currCluster));
					clusterContextCounts.addCounts(
							contextCountsForPhrase.mapCluster(SwapWordContextCounts.DUMMY_CLUSTER, newCluster));
					if (DO_TESTS) {
						ContextCountsUtils.checkCounts(clusterContextCounts, phraseToClusterMap, phraseContextCounts);
						checkSwapScores(phraseToClusterMap, clusterContextCounts, phraseContextCounts, phrase,
								currCluster, bestClusterScore, oldScore, newCluster);
					}
					numOfPhrasesChangedInLastIteration++;
				}
			}
			iteration++;
		}
	}

	/**
	 * see paragraph "Although we have... " on page 473 of [Brown et al.]
	 */

	private void mergeAllClusters(Map<Integer, ClusterHistoryNode> nodes, ContextCountsImpl contextCounts) {
		nodes = new HashMap<>(nodes);
		List<MergeCandidate> mergeCandidates = computeAllScores(contextCounts);
		while (!mergeCandidates.isEmpty()) {
			MergeCandidate next = mergeCandidates.remove(mergeCandidates.size() - 1);
			int cluster1 = next.getCluster1();
			int cluster2 = next.getCluster2();
			UI.write("Will merge cluster " + cluster1 + " with " + cluster2 + " (" + mergeCandidates.size()
					+ " candidates remaining)");
			contextCounts.mergeClusters(cluster1, cluster2);
			updateClusterNodes(nodes, cluster1, cluster2);
			removeMergeCandidates(mergeCandidates, cluster1);
			updateMergeCandidateScores(cluster2, mergeCandidates, contextCounts);
		}
	}

	/**
	 * Utility method to check that the score we computed to swap a phrase from
	 * currCluster to newCluster was correct.
	 */

	private void checkSwapScores(Int2IntOpenHashMap phraseToClusterMap, ContextCountsImpl clusterContextCounts,
			ContextCountsImpl phraseContextCounts, int phrase, int currCluster, Pair<Integer, Double> bestClusterScore,
			double oldScore, int newCluster) {
		ContextCountsImpl debugContextCountsForPhrase = mapPhraseCountsToClusterCounts(phrase, phraseToClusterMap,
				phraseContextCounts, SwapWordContextCounts.DUMMY_CLUSTER);
		SwapWordContextCounts debugSwapWordContextCounts = new SwapWordContextCounts(clusterContextCounts,
				debugContextCountsForPhrase, newCluster);
		double debugOldScore = computeMergeScore(SwapWordContextCounts.DUMMY_CLUSTER, 0.0, currCluster,
				debugSwapWordContextCounts);
		double debugNewScore = computeMergeScore(SwapWordContextCounts.DUMMY_CLUSTER, 0.0, newCluster,
				debugSwapWordContextCounts);
		if (!NumUtils.equal(debugOldScore, oldScore)) {
			throw new RuntimeException("Inconsistent score! " + oldScore + " " + debugOldScore);
		}
		if (!NumUtils.equal(debugNewScore, bestClusterScore.second())) {
			throw new RuntimeException("Inconsistent score! " + bestClusterScore.second() + " " + debugNewScore);
		}
	}

	/**
	 * For a given phrase, collect all cluster counts that correspond to
	 * occurrences of this phrase in the corpus
	 */

	private ContextCountsImpl mapPhraseCountsToClusterCounts(int phrase, Int2IntOpenHashMap phraseToClusterMap,
			ContextCounts phraseContextCounts, int newCluster) {
		Map<Integer, Int2IntOpenHashMap> prevClusterCounts = new HashMap<>();
		Map<Integer, Int2IntOpenHashMap> nextClusterCounts = new HashMap<>();
		addCounts(phraseToClusterMap, phraseContextCounts.getPrevCounts(phrase), prevClusterCounts, nextClusterCounts,
				phrase, true, newCluster);
		addCounts(phraseToClusterMap, phraseContextCounts.getNextCounts(phrase), nextClusterCounts, prevClusterCounts,
				phrase, false, newCluster);
		return new ContextCountsImpl(prevClusterCounts, nextClusterCounts);
	}

	private void addCounts(Int2IntOpenHashMap phraseToClusterMap, Int2IntOpenHashMap phraseContextCounts,
			Map<Integer, Int2IntOpenHashMap> prevClusterCounts, Map<Integer, Int2IntOpenHashMap> nextClusterCounts,
			int phrase, boolean includeIdentityCounts, int newCluster) {
		Int2IntOpenHashMap phrasePrevClusterCounts = prevClusterCounts.get(newCluster);
		if (phrasePrevClusterCounts == null) {
			phrasePrevClusterCounts = ContextCountsUtils.createNewInt2IntMap();
			prevClusterCounts.put(newCluster, phrasePrevClusterCounts);
		}
		for (Int2IntOpenHashMap.Entry otherPhraseEntry : phraseContextCounts.int2IntEntrySet()) {
			int otherPhrase = otherPhraseEntry.getIntKey();
			if (phrase != otherPhrase || includeIdentityCounts) {
				int clusterOtherPhrase = otherPhrase == phrase ? newCluster : phraseToClusterMap.get(otherPhrase);
				phrasePrevClusterCounts.addTo(clusterOtherPhrase, otherPhraseEntry.getIntValue());
				Int2IntOpenHashMap otherPhraseNextCounts = nextClusterCounts.get(clusterOtherPhrase);
				if (otherPhraseNextCounts == null) {
					otherPhraseNextCounts = ContextCountsUtils.createNewInt2IntMap();
					nextClusterCounts.put(clusterOtherPhrase, otherPhraseNextCounts);
				}
				otherPhraseNextCounts.addTo(newCluster, otherPhraseEntry.getValue());
			}
		}
	}

	private void removeMergeCandidates(List<MergeCandidate> mergeCandidates, int smallCluster) {
		mergeCandidates.removeIf(next -> next.getCluster1() == smallCluster || next.getCluster2() == smallCluster);
	}

	/**
	 * Compute the scores of all merge candidates
	 */

	private List<MergeCandidate> computeAllScores(ContextCounts contextCounts) {
		List<MergeCandidate> mergeCandidates = Collections.synchronizedList(new ArrayList<>());
		Set<Integer> allClusters = contextCounts.getAllClusters();
		allClusters.parallelStream().forEach(cluster1 -> {
			double ski = computeSK(cluster1, contextCounts);
			for (Integer cluster2 : allClusters) {
				if (cluster1 < cluster2) {
					double score = computeMergeScore(cluster1, ski, cluster2, contextCounts);
					mergeCandidates.add(new MergeCandidate(cluster1, cluster2, score));
				}
			}
		});
		Collections.sort(mergeCandidates);
		return mergeCandidates;
	}

	private void updateMergeCandidateScores(int cluster2, List<MergeCandidate> mergeCandidates,
			ContextCounts contextCounts) {
		double skj = computeSK(cluster2, contextCounts);
		Utils.fasterParallelStream(mergeCandidates).forEach(mergeCandidate -> {
			if (mergeCandidate.getCluster2() == cluster2) {
				double ski = computeSK(mergeCandidate.getCluster1(), contextCounts);
				mergeCandidate.setScore(computeMergeScore(mergeCandidate.getCluster1(), ski,
						mergeCandidate.getCluster2(), skj, contextCounts));
			}
		});
		Collections.sort(mergeCandidates);
	}

	private Pair<Integer, Double> findBestClusterToMerge(int origCluster, int minCluster, int maxCluster,
			ContextCounts clusterContextCounts) {
		MutableDouble bestScore = new MutableDouble(-Double.MAX_VALUE);
		MutableInt bestCluster = new MutableInt(-1);
		Utils.fasterParallelStream(clusterContextCounts.getAllClusters()).forEach(cluster -> {
			if (cluster >= minCluster && cluster < maxCluster && cluster != origCluster) {
				double score = computeMergeScore(origCluster, 0.0, cluster, clusterContextCounts);
				if (score > bestScore.doubleValue()) {
					synchronized (bestScore) {
						if (score > bestScore.doubleValue()) { // bestScore
																// might have
																// changed while
																// acquiring
																// lock
							bestScore.setValue(score);
							bestCluster.setValue(cluster);
						}
					}
				}
			}
		});
		return new Pair<>(bestCluster.intValue(), bestScore.doubleValue());
	}

	/**
	 * see top of page 7 of [Brown et al.].
	 */

	private double computeMergeScore(int cki, double ski, int ckj, ContextCounts contextCounts) {
		return computeMergeScore(cki, ski, ckj, computeSK(ckj, contextCounts), contextCounts);
	}

	private double computeMergeScore(int cki, double ski, int ckj, double skj, ContextCounts originalCounts) {
		MergedContextCounts mergedCounts = new MergedContextCounts(cki, ckj, originalCounts);
		double result = -ski - skj;
		result += computeSK(ckj, mergedCounts);
		return result;
	}

	private double computeSK(int cluster, ContextCounts contextCounts) {
		double sk = 0;
		double grandTotal = contextCounts.getGrandTotal();
		int prevTotal = contextCounts.getPrevTotal(cluster);
		for (Int2IntOpenHashMap.Entry entry : contextCounts.getPrevCounts(cluster).int2IntEntrySet()) {
			sk += computeQK(entry.getIntValue(), contextCounts.getNextTotal(entry.getIntKey()), prevTotal, grandTotal);
		}
		Int2IntOpenHashMap nextCounts = contextCounts.getNextCounts(cluster);
		int nextTotal = contextCounts.getNextTotal(cluster) - nextCounts.get(cluster);
		for (Int2IntOpenHashMap.Entry entry : nextCounts.int2IntEntrySet()) {
			if (entry.getIntKey() != cluster) {
				sk += computeQK(entry.getIntValue(), nextTotal, contextCounts.getPrevTotal(entry.getIntKey()),
						grandTotal);
			}
		}
		return sk;
	}

	private double computeQK(int jointCounts, int totalCki, int totalCkj, double grandTotal) {
		if (jointCounts > 0) {
			double pklm = jointCounts / grandTotal;
			double plkl = totalCki / grandTotal;
			double prkm = totalCkj / grandTotal;
			if (DO_TESTS) {
				checkProbability(pklm);
				checkProbability(plkl);
				checkProbability(prkm);
				if (plkl == 0 || prkm == 0) {
					throw new RuntimeException("Illegal probabilities!");
				}
			}
			return pklm * Math.log(pklm / (plkl * prkm));
		} else {
			return 0.0;
		}
	}

	private void writeOutput(Map<Integer, String> phraseMap, Int2IntOpenHashMap phraseToClusterMap,
			Map<Integer, ClusterHistoryNode> nodes, Int2IntOpenHashMap phraseFrequencies) throws IOException {
		List<String> outputLines = new ArrayList<>();
		List<String> stanfordOutputLines = new ArrayList<>();
		for (Integer phraseInd : phraseToClusterMap.keySet()) {
			String phrase = phraseMap.get(phraseInd);
			String output = "";
			ClusterHistoryNode node = nodes.get(phraseToClusterMap.get(phraseInd));
			while (node != null) {
				ClusterHistoryNode parent = node.getParent();
				if (parent != null) {
					if (parent.getLeftChild() == node) {
						output = '0' + output;
					} else {
						output = '1' + output;
					}
				}
				node = parent;
			}
			outputLines.add(output + '\t' + phrase + "\t" + phraseFrequencies.get(phraseInd));
		}
		Collections.sort(outputLines);
		BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
		for (String line : outputLines) {
			writer.write(line);
			writer.write('\n');
		}
		writer.close();
	}

	private Map<Integer, ClusterHistoryNode> initializeHistoryNodes(Int2IntOpenHashMap phraseToClusterMap) {
		Map<Integer, ClusterHistoryNode> result = new HashMap<>();
		for (Integer cluster : phraseToClusterMap.values()) {
			result.put(cluster, new ClusterHistoryNode(cluster));
		}
		return result;
	}

	private void updateClusterNodes(Map<Integer, ClusterHistoryNode> nodes, int smallCluster, int largeCluster) {
		ClusterHistoryNode parent = new ClusterHistoryNode(largeCluster);
		parent.setChildren(nodes.remove(smallCluster), nodes.get(largeCluster));
		nodes.put(largeCluster, parent);
	}

	private ContextCountsImpl extractContextCounts(Map<Integer, String> phraseMap) throws IOException {
		Map<String, Integer> invertedPhraseMap = invert(phraseMap); // mapping
																	// of words
																	// to their
																	// index
		Map<Integer, Int2IntOpenHashMap> prevContextCounts = createEmptyCounts(phraseMap.size());
		Map<Integer, Int2IntOpenHashMap> nextContextCounts = createEmptyCounts(phraseMap.size());
		BufferedReader rdr = new BufferedReader(new FileReader(inputFile));
		while (rdr.ready()) {
			String line = rdr.readLine();
			List<String> phrases = splitLineInPhrases(line);
			Integer prevPhrase = null;
			for (String phrase : phrases) {
				Integer currPhrase = invertedPhraseMap.get(phrase);
				if (currPhrase == null) {
					// infrequent phrase
					currPhrase = invertedPhraseMap.get(UNKNOWN_PHRASE);
				}
				if (prevPhrase != null) {
					nextContextCounts.get(prevPhrase).addTo(currPhrase, 1);
					prevContextCounts.get(currPhrase).addTo(prevPhrase, 1);
				}
				prevPhrase = currPhrase;
			}
		}
		rdr.close();
		trimCounts(prevContextCounts);
		trimCounts(nextContextCounts);
		return new ContextCountsImpl(prevContextCounts, nextContextCounts);
	}

	private Map<String, Integer> invert(Map<Integer, String> map) {
		Map<String, Integer> invertedMap = new HashMap<>(map.size());
		for (Map.Entry<Integer, String> entry : map.entrySet()) {
			invertedMap.put(entry.getValue(), entry.getKey());
		}
		return invertedMap;
	}

	private void trimCounts(Map<Integer, Int2IntOpenHashMap> wordCounts) {
		wordCounts.values().stream().forEach(Int2IntOpenHashMap::trim);
	}

	private Map<Integer, Int2IntOpenHashMap> createEmptyCounts(int size) {
		Map<Integer, Int2IntOpenHashMap> result = new HashMap<>();
		for (int i = 0; i < size; i++) {
			result.put(i, ContextCountsUtils.createNewInt2IntMap());
		}
		return result;
	}

	private Pair<Map<Integer, String>, Int2IntOpenHashMap> readPhrases() throws IOException {
		Map<String, Integer> rawPraseCounts = countPhrases();
		Map<Integer, String> phraseToIndexMap = assignWordsToIndexBasedOnFrequency(rawPraseCounts);
		Int2IntOpenHashMap phraseFrequencies = new Int2IntOpenHashMap(phraseToIndexMap.size());
		for (Map.Entry<Integer, String> entry : phraseToIndexMap.entrySet()) {
			phraseFrequencies.put(entry.getKey(), rawPraseCounts.get(entry.getValue()));
		}
		return new Pair<>(phraseToIndexMap, phraseFrequencies);
	}

	private Map<Integer, String> assignWordsToIndexBasedOnFrequency(Map<String, Integer> phraseCounts) {
		List<String> allWords = new ArrayList<>(phraseCounts.keySet());
		Collections.sort(allWords,
				(word1, word2) -> -Integer.compare(phraseCounts.get(word1), phraseCounts.get(word2)));
		Map<Integer, String> wordMapping = new HashMap<>();
		int ind = 0;
		for (String word : allWords) {
			wordMapping.put(ind++, word);
		}
		return wordMapping;
	}

	private Map<String, Integer> countPhrases() throws IOException {
		// Count how often every phrase occurs in the input
		Map<String, Integer> phraseCounts = countAllPhrases(inputFile);
		// Select phrases that occur >= minFrequencyOfPhrase
		int totalDroppedCounts = 0;
		Iterator<Map.Entry<String, Integer>> iterator = phraseCounts.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<String, Integer> entry = iterator.next();
			if (entry.getValue() < minFrequencyOfPhrase) {
				totalDroppedCounts += entry.getValue();
				iterator.remove();
			}
		}
		if (totalDroppedCounts > 0) {
			phraseCounts.put(UNKNOWN_PHRASE, totalDroppedCounts);
		}
		return phraseCounts;
	}

	private Map<String, Integer> countAllPhrases(String textInputFile) throws IOException {
		Object2IntOpenHashMap<String> phraseCounts = new Object2IntOpenHashMap<>();
		BufferedReader rdr = new BufferedReader(new FileReader(textInputFile));
		while (rdr.ready()) {
			String line = rdr.readLine();
			List<String> phrases = splitLineInPhrases(line);
			for (String phrase : phrases) {
				phraseCounts.addTo(phrase, 1);
			}
		}
		rdr.close();
		return phraseCounts;
	}

	/**
	 * Could be adapted to have phrases of more than 1 word (e.g. map
	 * collocations such as 'fast food' or 'prime minister' to a single phrase)
	 */

	private List<String> splitLineInPhrases(String line) {
		String[] words = line.split("\\s");
		List<String> result = new ArrayList<>();
		for (String word : words) {
			if (!word.isEmpty()) {
				result.add(word);
			}
		}
		return result;
	}

	private void checkProbability(double probability) {
		if (probability < 0 || probability > 1 || Double.isNaN(probability)) {
			throw new RuntimeException("Illegal probability " + probability);
		}
	}

	private Int2IntOpenHashMap initializeClusters(int numberOfPhrases) {
		Int2IntOpenHashMap phraseToCluster = ContextCountsUtils.createNewInt2IntMap(numberOfPhrases);
		for (int i = 0; i < numberOfPhrases; i++) {
			phraseToCluster.put(i, i); // assign every word to its own cluster
		}
		return phraseToCluster;
	}

}
