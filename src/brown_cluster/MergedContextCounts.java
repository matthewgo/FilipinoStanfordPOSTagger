package brown_cluster;

import java.util.Set;

import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;

/**
 * Created by Koen Deschacht (koendeschacht@gmail.com) on 03/12/14.
 * <p/>
 * This class represents context counts where smallCluster was merged into
 * largeCluster
 */
public class MergedContextCounts implements ContextCounts {

	private final int smallCluster;
	private final int largeCluster;
	private final ContextCounts contextCounts;

	public MergedContextCounts(int smallCluster, int largeCluster, ContextCounts contextCounts) {
		this.smallCluster = smallCluster;
		this.largeCluster = largeCluster;
		this.contextCounts = contextCounts;
	}

	@Override
	public int getPrevTotal(int cluster) {
		if (cluster == smallCluster) {
			return 0; // smallCluster does not exist anymore
		} else if (cluster == largeCluster) {
			return contextCounts.getPrevTotal(smallCluster) + contextCounts.getPrevTotal(largeCluster); // largeCluster
																										// has
																										// all
																										// counts
																										// of
																										// smallCluster
																										// and
																										// of
																										// largeCluster
		} else {
			return contextCounts.getPrevTotal(cluster); // the total of this
														// cluster is not
														// affected
		}
	}

	@Override
	public int getNextTotal(int cluster) {
		if (cluster == smallCluster) {
			return 0; // smallCluster does not exist anymore
		} else if (cluster == largeCluster) {
			return contextCounts.getNextTotal(smallCluster) + contextCounts.getNextTotal(largeCluster); // largeCluster
																										// has
																										// all
																										// counts
																										// of
																										// smallCluster
																										// and
																										// of
																										// largeCluster
		} else {
			return contextCounts.getNextTotal(cluster); // the total of this
														// cluster is not
														// affected
		}
	}

	@Override
	public int getGrandTotal() {
		return contextCounts.getGrandTotal();
	}

	@Override
	public Set<Integer> getAllClusters() {
		Set<Integer> result = contextCounts.getAllClusters();
		result.remove(smallCluster);
		return result;
	}

	@Override
	public Int2IntOpenHashMap getPrevCounts(int cluster) {
		Int2IntOpenHashMap result;
		if (cluster == smallCluster) {
			return ContextCountsUtils.createNewInt2IntMap(); // smallCluster has
																// no context
																// counts
		} else if (cluster == largeCluster) {
			result = merge(contextCounts.getPrevCounts(smallCluster), contextCounts.getPrevCounts(largeCluster)); // largeCluster
																													// has
																													// original
																													// context
																													// counts
																													// merged
																													// with
																													// context
																													// counts
																													// of
																													// small
																													// cluster
		} else {
			result = contextCounts.getPrevCounts(cluster);
		}
		result = replace(result, smallCluster, largeCluster); // map all counts
																// that refer to
																// smallCluster
																// to
																// largeCluster
		return result;
	}

	@Override
	public Int2IntOpenHashMap getNextCounts(int cluster) {
		Int2IntOpenHashMap result;
		if (cluster == smallCluster) {
			return ContextCountsUtils.createNewInt2IntMap(); // smallCluster has
																// no context
																// counts
		} else if (cluster == largeCluster) {
			result = merge(contextCounts.getNextCounts(smallCluster), contextCounts.getNextCounts(largeCluster)); // largeCluster
																													// has
																													// original
																													// context
																													// counts
																													// merged
																													// with
																													// context
																													// counts
																													// of
																													// small
																													// cluster
		} else {
			result = contextCounts.getNextCounts(cluster);
		}
		result = replace(result, smallCluster, largeCluster); // map all counts
																// that refer to
																// smallCluster
																// to
																// largeCluster
		return result;
	}

	private Int2IntOpenHashMap replace(Int2IntOpenHashMap result, int smallCluster, int largeCluster) {
		int countsSmallCluster = result.get(smallCluster);
		if (countsSmallCluster > 0) {
			result = result.clone();
			result.remove(smallCluster);
			result.addTo(largeCluster, countsSmallCluster);
		}
		return result;
	}

	private Int2IntOpenHashMap merge(Int2IntOpenHashMap counts1, Int2IntOpenHashMap counts2) {
		Int2IntOpenHashMap large = counts1.size() > counts2.size() ? counts1 : counts2;
		Int2IntOpenHashMap small = counts1.size() > counts2.size() ? counts2 : counts1;
		Int2IntOpenHashMap result = large.clone();
		for (Int2IntOpenHashMap.Entry entry : small.int2IntEntrySet()) {
			result.addTo(entry.getIntKey(), entry.getIntValue());
		}
		return result;
	}

}
