package brown_cluster;

import java.io.IOException;

/**
 * Created by Koen Deschacht (koendeschacht@gmail.com) on 11/12/14.
 */
public class Main {

	public static void main(String[] args) throws IOException {
		String inputFile = "data/distsim/Wikipedia - Tagalog - Moses.txt";
		String outputFile = "data/distsim/distsim-Tagalog-Moses.txt";
		String stanfordOutputFile = "data/distsim/distsim-Stanford-Tagalog-Moses.txt";
		int minFrequencyOfPhrase = 10;
		int maxNumberOfClusters = 1000;
		boolean onlySwapMostFrequentWords = true;
		long start = System.currentTimeMillis();
		// new BrownClustering(inputFile, outputFile, minFrequencyOfPhrase,
		// maxNumberOfClusters, onlySwapMostFrequentWords)
		// .run();
		new BrownClusterOutputFormatter().toStanfordFormat(outputFile, stanfordOutputFile);
		long end = System.currentTimeMillis();
		System.out.println("Took " + (end - start) + " ms.");
	}
}
