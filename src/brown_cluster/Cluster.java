package brown_cluster;

/**
 * Created by Koen Deschacht (koendeschacht@gmail.com) on 10/12/14.
 */
public class Cluster {

	private final int ind;
	private double cachedSk;

	public Cluster(int ind) {
		this.ind = ind;
	}

	public int getInd() {
		return ind;
	}

	public double getCachedSk() {
		return cachedSk;
	}

	public void setCachedSk(double cachedSk) {
		this.cachedSk = cachedSk;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Cluster) {
			return getInd() == ((Cluster) obj).getInd();
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return ind;
	}
}
