package fiji.plugin.filamentdetector.tracking;

public class FilteringTrackedFilamentsParameters {

	private double maxSize = Double.POSITIVE_INFINITY;
	private double minSize = 1;

	private boolean disableFiltering = false;

	public double getMaxSize() {
		return maxSize;
	}

	public void setMaxSize(double maxSize) {
		this.maxSize = maxSize;
	}

	public double getMinSize() {
		return minSize;
	}

	public void setMinSize(double minSize) {
		this.minSize = minSize;
	}

	public boolean isDisableFiltering() {
		return disableFiltering;
	}

	public void setDisableFiltering(boolean disableFiltering) {
		this.disableFiltering = disableFiltering;
	}

	@Override
	public String toString() {
		String out = "";

		out += "maxSize = " + maxSize + "\n";
		out += "minSize = " + minSize;

		return out;
	}

}
