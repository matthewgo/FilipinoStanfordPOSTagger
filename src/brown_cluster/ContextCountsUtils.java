package brown_cluster;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;

/**
 * Created by Koen Deschacht (koendeschacht@gmail.com) on 04/12/14.
 */
public class ContextCountsUtils {

	public static ContextCountsImpl computeContextCounts(ContextCountsImpl phraseContextCounts,
			Int2IntOpenHashMap phraseToClusterMap) {
		Map<Integer, Int2IntOpenHashMap> prevCounts = new HashMap<>();
		Map<Integer, Int2IntOpenHashMap> nextCounts = new HashMap<>();
		for (Integer phrase : phraseContextCounts.getAllPhrases()) {
			Integer cluster = phraseToClusterMap.get(phrase);
			Int2IntOpenHashMap prevCountsForPhrase = phraseContextCounts.getPrevCounts(phrase);
			addCounts(phraseToClusterMap, prevCounts, cluster, prevCountsForPhrase);
			Int2IntOpenHashMap nextCountsForPhrase = phraseContextCounts.getNextCounts(phrase);
			addCounts(phraseToClusterMap, nextCounts, cluster, nextCountsForPhrase);
		}
		return new ContextCountsImpl(prevCounts, nextCounts);
	}

	private static void addCounts(Int2IntOpenHashMap phraseToClusterMap, Map<Integer, Int2IntOpenHashMap> clusterCounts,
			Integer cluster, Int2IntOpenHashMap phraseCounts) {
		Int2IntOpenHashMap clusterCountsForCluster = clusterCounts.get(cluster);
		if (clusterCountsForCluster == null) {
			clusterCountsForCluster = ContextCountsUtils.createNewInt2IntMap();
			clusterCounts.put(cluster, clusterCountsForCluster);
		}
		for (Map.Entry<Integer, Integer> entry : phraseCounts.entrySet()) {
			Integer cluster2 = phraseToClusterMap.get(entry.getKey());
			clusterCountsForCluster.addTo(cluster2, entry.getValue());
		}
	}

	public static int getTotal(Int2IntOpenHashMap count) {
		if (count == null) {
			return 0;
		} else {
			return count.values().stream().collect(Collectors.summingInt(v -> v));
		}
	}

	public static Int2IntOpenHashMap createNewInt2IntMap(int initialSize) {
		Int2IntOpenHashMap result = new Int2IntOpenHashMap(initialSize);
		result.defaultReturnValue(0);
		return result;
	}

	public static Int2IntOpenHashMap createNewInt2IntMap() {
		return createNewInt2IntMap(0);
	}

	public static Int2IntOpenHashMap computeMapTotals(Map<Integer, Int2IntOpenHashMap> countMaps) {
		Int2IntOpenHashMap totals = createNewInt2IntMap(countMaps.size());
		for (Map.Entry<Integer, Int2IntOpenHashMap> entry : countMaps.entrySet()) {
			int total = entry.getValue().values().stream().collect(Collectors.summingInt(i -> i));
			totals.put(entry.getKey().intValue(), total);
		}
		return totals;
	}

	public static int getTotal(Map<Integer, Int2IntOpenHashMap> counts) {
		return counts.values().stream().flatMap(map -> map.values().stream()).collect(Collectors.summingInt(i -> i));
	}

	public static void checkCounts(ContextCountsImpl clusterContextCounts, Int2IntOpenHashMap phraseToClusterMap,
			ContextCountsImpl phraseContextCounts) {
		ContextCounts computedContextCounts = ContextCountsUtils.computeContextCounts(phraseContextCounts,
				phraseToClusterMap);
		for (Integer cluster : clusterContextCounts.getAllClusters()) {
			compare(clusterContextCounts.getPrevCounts(cluster), computedContextCounts.getPrevCounts(cluster));
			compare(clusterContextCounts.getNextCounts(cluster), computedContextCounts.getNextCounts(cluster));
		}
	}

	private static void compare(Int2IntOpenHashMap counts1, Int2IntOpenHashMap counts2) {
		for (Int2IntMap.Entry entry : counts1.int2IntEntrySet()) {
			if (counts2.get(entry.getIntKey()) != entry.getIntValue()) {
				throw new RuntimeException("Incorrect counts!");
			}
		}
		for (Int2IntMap.Entry entry : counts2.int2IntEntrySet()) {
			if (counts1.get(entry.getIntKey()) != entry.getIntValue()) {
				throw new RuntimeException("Incorrect counts!");
			}
		}
	}
}
