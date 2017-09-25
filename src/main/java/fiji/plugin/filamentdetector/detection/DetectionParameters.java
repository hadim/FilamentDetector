package fiji.plugin.filamentdetector.detection;

import de.biomedical_imaging.ij.steger.OverlapOption;

public class DetectionParameters {

	private double sigma = 1.51;
	private double upperThresh = 7.99;
	private double lowerThresh = 3.06;
	private double minLength = 0;
	private double maxLength = 0;
	private boolean isDarkLine = false;
	private boolean doCorrectPosition = true;
	private boolean doEstimateWidth = true;
	private boolean doExtendLine = true;
	private boolean simplifyFilaments = true;
	private double simplifyToleranceDistance = 5;

	private OverlapOption overlapOption = OverlapOption.NONE;

	private boolean detectOnlyCurrentFrame = false;

	public double getSigma() {
		return sigma;
	}

	public void setSigma(double sigma) {
		this.sigma = sigma;
	}

	public double getUpperThresh() {
		return upperThresh;
	}

	public void setUpperThresh(double upperThresh) {
		this.upperThresh = upperThresh;
	}

	public double getLowerThresh() {
		return lowerThresh;
	}

	public void setLowerThresh(double lowerThresh) {
		this.lowerThresh = lowerThresh;
	}

	public double getMinLength() {
		return minLength;
	}

	public void setMinLength(double minLength) {
		this.minLength = minLength;
	}

	public double getMaxLength() {
		return maxLength;
	}

	public void setMaxLength(double maxLength) {
		this.maxLength = maxLength;
	}

	public boolean isDarkLine() {
		return isDarkLine;
	}

	public void setDarkLine(boolean isDarkLine) {
		this.isDarkLine = isDarkLine;
	}

	public boolean isDoCorrectPosition() {
		return doCorrectPosition;
	}

	public void setDoCorrectPosition(boolean doCorrectPosition) {
		this.doCorrectPosition = doCorrectPosition;
	}

	public boolean isDoEstimateWidth() {
		return doEstimateWidth;
	}

	public void setDoEstimateWidth(boolean doEstimateWidth) {
		this.doEstimateWidth = doEstimateWidth;
	}

	public boolean isDoExtendLine() {
		return doExtendLine;
	}

	public void setDoExtendLine(boolean doExtendLine) {
		this.doExtendLine = doExtendLine;
	}

	public OverlapOption getOverlapOption() {
		return overlapOption;
	}

	public void setOverlapOption(OverlapOption overlapOption) {
		this.overlapOption = overlapOption;
	}

	public boolean isDetectOnlyCurrentFrame() {
		return detectOnlyCurrentFrame;
	}

	public void setDetectOnlyCurrentFrame(boolean detectOnlyCurrentFrame) {
		this.detectOnlyCurrentFrame = detectOnlyCurrentFrame;
	}

	public boolean isSimplifyFilaments() {
		return simplifyFilaments;
	}

	public void setSimplifyFilaments(boolean simplifyFilaments) {
		this.simplifyFilaments = simplifyFilaments;
	}

	public double getSimplifyToleranceDistance() {
		return simplifyToleranceDistance;
	}

	public void setSimplifyToleranceDistance(double simplifyToleranceDistance) {
		this.simplifyToleranceDistance = simplifyToleranceDistance;
	}

	@Override
	public String toString() {
		String out = "";

		out += "Sigma = " + sigma + "\n";
		out += "Lower Threshold = " + lowerThresh + "\n";
		out += "Upper Threshold = " + upperThresh + "\n";
		out += "Detect Only Current Frame = " + detectOnlyCurrentFrame + "\n";
		out += "Simplify Filaments = " + simplifyFilaments + "\n";
		out += "Simplify Tolerance Distance = " + simplifyToleranceDistance;

		return out;
	}

}
