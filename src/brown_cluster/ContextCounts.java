package brown_cluster;

import java.util.Set;

import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;

/**
 * Created by Koen Deschacht (koendeschacht@gmail.com) on 03/12/14.
 */
public interface ContextCounts {

	int getPrevTotal(int cluster);

	int getNextTotal(int cluster);

	int getGrandTotal();

	Set<Integer> getAllClusters();

	Int2IntOpenHashMap getPrevCounts(int cluster);

	Int2IntOpenHashMap getNextCounts(int cluster);
}
