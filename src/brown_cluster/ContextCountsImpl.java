package brown_cluster;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;

/**
 * Created by Koen Deschacht (koendeschacht@gmail.com) on 03/12/14.
 */
public class ContextCountsImpl implements ContextCounts {

	private final Map<Integer, Int2IntOpenHashMap> prevCounts;
	private final Map<Integer, Int2IntOpenHashMap> nextCounts;
	private Int2IntOpenHashMap prevTotals;
	private Int2IntOpenHashMap nextTotals;
	private int grandTotal;

	public ContextCountsImpl(Map<Integer, Int2IntOpenHashMap> prevCounts, Map<Integer, Int2IntOpenHashMap> nextCounts) {
		this.prevCounts = prevCounts;
		this.nextCounts = nextCounts;
		computeTotals();
		if (BrownClustering.DO_TESTS) {
			checkCountsConsistent();
		}
	}

	@Override
	public int getPrevTotal(int cluster) {
		return prevTotals.get(cluster);
	}

	@Override
	public int getNextTotal(int cluster) {
		return nextTotals.get(cluster);
	}

	@Override
	public int getGrandTotal() {
		return grandTotal;
	}

	private void computeTotals() {
		prevTotals = ContextCountsUtils.computeMapTotals(prevCounts);
		nextTotals = ContextCountsUtils.computeMapTotals(nextCounts);
		grandTotal = ContextCountsUtils.getTotal(nextTotals);
	}

	public void mergeClusters(int smallCluster, int largeCluster) {
		mergeCounts(smallCluster, largeCluster, prevCounts);
		mergeCounts(smallCluster, largeCluster, nextCounts);
		prevTotals.put(largeCluster, prevTotals.get(largeCluster) + prevTotals.remove(smallCluster));
		nextTotals.put(largeCluster, nextTotals.get(largeCluster) + nextTotals.remove(smallCluster));
		if (BrownClustering.DO_TESTS) {
			checkCountsConsistent();
		}
	}

	protected void mergeCounts(int smallCluster, int largeCluster, Map<Integer, Int2IntOpenHashMap> counts) {
		// step 1: merge counts from small cluster to large cluster
		Int2IntOpenHashMap countsSmallCluster = counts.remove(smallCluster);
		Int2IntOpenHashMap countsLargeCluster = counts.get(largeCluster);
		if (countsLargeCluster == null) {
			countsLargeCluster = ContextCountsUtils.createNewInt2IntMap();
			counts.put(largeCluster, countsLargeCluster);
		}
		for (Int2IntOpenHashMap.Entry entry : countsSmallCluster.int2IntEntrySet()) {
			countsLargeCluster.addTo(entry.getIntKey(), entry.getIntValue());
		}
		// step 2: update all occurrences of small cluster to the large cluster
		counts.values().parallelStream().forEach(countsForSingleCluster -> {
			int prevCountsSmallCluster = countsForSingleCluster.remove(smallCluster);
			if (prevCountsSmallCluster > 0) {
				countsForSingleCluster.addTo(largeCluster, prevCountsSmallCluster);
			}
		});
	}

	public void removeCounts(ContextCountsImpl contextCounts) {
		int added = addCounts(prevCounts, contextCounts.prevCounts, prevTotals, -1);
		grandTotal -= added;
		addCounts(nextCounts, contextCounts.nextCounts, nextTotals, -1);
		if (BrownClustering.DO_TESTS) {
			checkCountsConsistent();
		}
	}

	public void addCounts(ContextCountsImpl contextCounts) {
		int added = addCounts(prevCounts, contextCounts.prevCounts, prevTotals, 1);
		grandTotal += added;
		addCounts(nextCounts, contextCounts.nextCounts, nextTotals, 1);
		if (BrownClustering.DO_TESTS) {
			checkCountsConsistent();
		}
	}

	private int addCounts(Map<Integer, Int2IntOpenHashMap> counts, Map<Integer, Int2IntOpenHashMap> countsToAdd,
			Int2IntOpenHashMap totals, int sign) {
		int total = 0;
		for (Map.Entry<Integer, Int2IntOpenHashMap> entry : countsToAdd.entrySet()) {
			Int2IntOpenHashMap countsForKey = counts.get(entry.getKey());
			if (countsForKey == null) {
				countsForKey = ContextCountsUtils.createNewInt2IntMap();
				counts.put(entry.getKey(), countsForKey);
			}
			int added = addCountsSingleMap(countsForKey, entry.getValue(), sign);
			totals.addTo(entry.getKey(), added * sign);
			total += added;
		}
		return total;
	}

	private int addCountsSingleMap(Int2IntOpenHashMap counts, Int2IntOpenHashMap countsToAdd, int sign) {
		int totalCounts = 0;
		for (Int2IntOpenHashMap.Entry entry : countsToAdd.int2IntEntrySet()) {
			addCount(counts, entry.getIntKey(), entry.getIntValue() * sign);
			totalCounts += entry.getIntValue();
		}
		return totalCounts;
	}

	public int getNumberOfClusters() {
		return prevCounts.size();
	}

	public int getNumberOfPhrases() {
		return getNumberOfClusters(); // same thing, depending on context
	}

	@Override
	public Set<Integer> getAllClusters() {
		Set<Integer> result = new HashSet<>();
		addNonZero(result, prevTotals);
		addNonZero(result, nextTotals);
		return result;
	}

	protected void addNonZero(Set<Integer> result, Int2IntOpenHashMap totals) {
		for (Int2IntMap.Entry entry : totals.int2IntEntrySet()) {
			if (entry.getIntValue() > 0) {
				result.add(entry.getIntKey());
			}
		}
	}

	public Set<Integer> getAllPhrases() {
		return getAllClusters(); // same thing, depending on context
	}

	@Override
	public ContextCountsImpl clone() {
		return new ContextCountsImpl(deepClone(prevCounts), deepClone(nextCounts));
	}

	private Map<Integer, Int2IntOpenHashMap> deepClone(Map<Integer, Int2IntOpenHashMap> map) {
		Map<Integer, Int2IntOpenHashMap> result = new HashMap<>();
		for (Map.Entry<Integer, Int2IntOpenHashMap> entry : map.entrySet()) {
			result.put(entry.getKey(), entry.getValue().clone());
		}
		return result;
	}

	@Override
	public Int2IntOpenHashMap getPrevCounts(int cluster) {
		Int2IntOpenHashMap result = prevCounts.get(cluster);
		return returnResultOrEmpty(result);
	}

	@Override
	public Int2IntOpenHashMap getNextCounts(int cluster) {
		Int2IntOpenHashMap result = nextCounts.get(cluster);
		return returnResultOrEmpty(result);
	}

	private Int2IntOpenHashMap returnResultOrEmpty(Int2IntOpenHashMap result) {
		if (result == null) {
			return ContextCountsUtils.createNewInt2IntMap();
		} else {
			return result;
		}
	}

	private void addCount(Int2IntOpenHashMap currentCounts, int key, int value) {
		if (value != 0) {
			int oldValue = currentCounts.addTo(key, value);
			if (oldValue + value < 0) {
				throw new RuntimeException("Negative count!");
			}
		}
	}

	private void checkCountsConsistent() {
		if (ContextCountsUtils.getTotal(prevCounts) != ContextCountsUtils.getTotal(prevTotals)
				|| ContextCountsUtils.getTotal(prevTotals) != grandTotal) {
			throw new RuntimeException("Inconsistent prev counts!");
		}
		if (ContextCountsUtils.getTotal(nextCounts) != ContextCountsUtils.getTotal(nextTotals)
				|| ContextCountsUtils.getTotal(nextTotals) != grandTotal) {
			throw new RuntimeException("Inconsistent next counts!");
		}
		checkCountsConsistent(prevCounts, nextCounts);
		checkCountsConsistent(nextCounts, prevCounts);
	}

	private void checkCountsConsistent(Map<Integer, Int2IntOpenHashMap> counts1,
			Map<Integer, Int2IntOpenHashMap> counts2) {
		for (Map.Entry<Integer, Int2IntOpenHashMap> entry : counts1.entrySet()) {
			for (Int2IntOpenHashMap.Entry innerEntry : entry.getValue().int2IntEntrySet()) {
				int count1 = innerEntry.getIntValue();
				Int2IntOpenHashMap countsForKey = counts2.get(innerEntry.getKey());
				if (countsForKey == null) {
					throw new RuntimeException("Inconsistent prev-next counts :" + entry.getKey() + " "
							+ innerEntry.getKey() + " " + count1);
				}
				int count2 = countsForKey.getOrDefault(entry.getKey(), 0);
				if (count1 != count2) {
					throw new RuntimeException("Inconsistent prev-next counts :" + entry.getKey() + " "
							+ innerEntry.getKey() + " " + count1 + " " + count2);
				}
			}
		}
	}

	public ContextCountsImpl mapCluster(int oldCluster, int newCluster) {
		ContextCountsImpl result = clone();
		mapCluster(result.prevCounts, oldCluster, newCluster);
		mapCluster(result.nextCounts, oldCluster, newCluster);
		result.prevTotals.addTo(newCluster, result.prevTotals.remove(oldCluster));
		result.nextTotals.addTo(newCluster, result.nextTotals.remove(oldCluster));
		if (BrownClustering.DO_TESTS) {
			result.checkCountsConsistent();
		}
		return result;
	}

	private void mapCluster(Map<Integer, Int2IntOpenHashMap> counts, int oldCluster, int newCluster) {
		Int2IntOpenHashMap mapOldCluster = counts.remove(oldCluster);
		if (mapOldCluster != null) {
			Int2IntOpenHashMap mapNewCluster = counts.get(newCluster);
			if (mapNewCluster == null) {
				counts.put(newCluster, mapOldCluster);
			} else {
				// merge maps
				for (Map.Entry<Integer, Integer> entry : mapOldCluster.entrySet()) {
					mapNewCluster.addTo(entry.getKey(), entry.getValue());
				}
			}
		}
		for (Int2IntOpenHashMap currMap : counts.values()) {
			currMap.addTo(newCluster, currMap.remove(oldCluster));
		}
	}
}
