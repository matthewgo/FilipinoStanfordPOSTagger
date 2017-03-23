package brown_cluster;

/**
 * Created by Koen Deschacht (koendeschacht@gmail.com) on 03/12/14.
 */
class MergeCandidate implements Comparable<MergeCandidate> {

	private int cluster1;
	private int cluster2;
	private double score;

	MergeCandidate(int cluster1, int cluster2, double score) {
		this.cluster1 = cluster1;
		this.cluster2 = cluster2;
		this.score = score;
	}

	public int getCluster1() {
		return cluster1;
	}

	public int getCluster2() {
		return cluster2;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	@Override
	public int compareTo(MergeCandidate o) {
		return Double.compare(getScore(), o.getScore());
	}

}
