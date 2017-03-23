package brown_cluster;

import java.util.Set;

import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;

/**
 * Created by Koen Deschacht (koendeschacht@gmail.com) on 03/12/14.
 * <p/>
 * This class represents context counts where one phrase was mapped to the
 * cluster DUMMY_CLUSTER.
 */
public class SwapWordContextCounts implements ContextCounts {

	public static final int DUMMY_CLUSTER = -1;

	private final ContextCounts origContextCounts;
	private final ContextCounts phraseContextCounts;
	private final int currClusterOfPhrase;

	public SwapWordContextCounts(ContextCounts origContextCounts, ContextCounts phraseContextCounts,
			int currClusterOfPhrase) {
		this.origContextCounts = origContextCounts;
		this.phraseContextCounts = phraseContextCounts;
		this.currClusterOfPhrase = currClusterOfPhrase;
	}

	@Override
	public int getPrevTotal(int cluster) {
		if (cluster == DUMMY_CLUSTER) {
			// DUMMY_CLUSTER only occurs in phraseContextCounts
			return phraseContextCounts.getPrevTotal(DUMMY_CLUSTER);
		} else if (cluster == currClusterOfPhrase) {
			// reduce the original context counts with the counts that have been
			// assigned to DUMMY_CLUSTER
			return origContextCounts.getPrevTotal(currClusterOfPhrase)
					- phraseContextCounts.getPrevTotal(DUMMY_CLUSTER);
		} else {
			// the total counts of this cluster are not affected
			return origContextCounts.getPrevTotal(cluster);
		}
	}

	@Override
	public int getNextTotal(int cluster) {
		if (cluster == DUMMY_CLUSTER) {
			// DUMMY_CLUSTER only occurs in phraseContextCounts
			return phraseContextCounts.getNextTotal(DUMMY_CLUSTER);
		} else if (cluster == currClusterOfPhrase) {
			// reduce the original context counts with the counts that have been
			// assigned to DUMMY_CLUSTER
			return origContextCounts.getNextTotal(currClusterOfPhrase)
					- phraseContextCounts.getNextTotal(DUMMY_CLUSTER);
		} else {
			// the total counts of this cluster are not affected
			return origContextCounts.getNextTotal(cluster);
		}
	}

	@Override
	public int getGrandTotal() {
		return origContextCounts.getGrandTotal();
	}

	@Override
	public Set<Integer> getAllClusters() {
		Set<Integer> result = origContextCounts.getAllClusters();
		result.add(DUMMY_CLUSTER);
		return result;
	}

	@Override
	public Int2IntOpenHashMap getPrevCounts(int cluster) {
		if (cluster == DUMMY_CLUSTER) {
			// DUMMY_CLUSTER only occurs in phraseContextCounts
			return phraseContextCounts.getPrevCounts(cluster);
		} else if (cluster == currClusterOfPhrase) {
			// first reduce counts with counts that have been mapped to
			// DUMMY_CLUSTER
			Int2IntOpenHashMap reducedCounts = reduceCounts(origContextCounts.getPrevCounts(cluster),
					phraseContextCounts.getPrevCounts(DUMMY_CLUSTER));
			// then swap counts that previously mapped to currClusterOfPhrase to
			// DUMMY_CLUSTER
			return swapCounts(reducedCounts, phraseContextCounts.getPrevCounts(cluster));
		} else {
			// swap counts that previously mapped to currClusterOfPhrase to
			// DUMMY_CLUSTER
			return swapCounts(origContextCounts.getPrevCounts(cluster), phraseContextCounts.getPrevCounts(cluster));
		}
	}

	@Override
	public Int2IntOpenHashMap getNextCounts(int cluster) {
		if (cluster == DUMMY_CLUSTER) {
			// DUMMY_CLUSTER only occurs in phraseContextCounts
			return phraseContextCounts.getNextCounts(cluster);
		} else if (cluster == currClusterOfPhrase) {
			// first reduce counts with counts that have been mapped to
			// DUMMY_CLUSTER
			Int2IntOpenHashMap reducedCounts = reduceCounts(origContextCounts.getNextCounts(cluster),
					phraseContextCounts.getNextCounts(DUMMY_CLUSTER));
			// then swap counts that previously mapped to currClusterOfPhrase to
			// DUMMY_CLUSTER
			return swapCounts(reducedCounts, phraseContextCounts.getNextCounts(cluster));
		} else {
			// swap counts that previously mapped to currClusterOfPhrase to
			// DUMMY_CLUSTER
			return swapCounts(origContextCounts.getNextCounts(cluster), phraseContextCounts.getNextCounts(cluster));
		}
	}

	private Int2IntOpenHashMap reduceCounts(Int2IntOpenHashMap origCounts, Int2IntOpenHashMap countsToReduce) {
		if (countsToReduce != null) {
			origCounts = origCounts.clone();
			for (Int2IntMap.Entry entry : countsToReduce.int2IntEntrySet()) {
				int cluster = entry.getIntKey();
				if (cluster == DUMMY_CLUSTER) {
					reduceValue(origCounts, currClusterOfPhrase, entry.getIntValue());
				} else {
					reduceValue(origCounts, cluster, entry.getIntValue());
				}
			}
		}
		return origCounts;
	}

	private Int2IntOpenHashMap swapCounts(Int2IntOpenHashMap origCounts, Int2IntOpenHashMap countsToSwap) {
		if (countsToSwap != null) {
			if (countsToSwap.size() > 1) {
				throw new RuntimeException("Unexpected counts!");
			}
			int countToSwap = countsToSwap.get(DUMMY_CLUSTER);
			if (countToSwap > 0) {
				origCounts = origCounts.clone();
				origCounts.addTo(DUMMY_CLUSTER, countToSwap);
				reduceValue(origCounts, currClusterOfPhrase, countToSwap);
			}
		}
		return origCounts;
	}

	private void reduceValue(Int2IntOpenHashMap origCounts, int key, int countToReduce) {
		int oldValue = origCounts.addTo(key, -countToReduce);
		if (oldValue < countToReduce) {
			throw new RuntimeException("Unexpected count " + oldValue);
		}
	}

}
