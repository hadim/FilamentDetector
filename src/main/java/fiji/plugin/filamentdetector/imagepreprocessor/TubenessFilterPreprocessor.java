/*-
 * #%L
 * A Fiji plugin that allow easy, fast and accurate detection and tracking of biological filaments.
 * %%
 * Copyright (C) 2016 - 2017 Hadrien Mary
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */
package fiji.plugin.filamentdetector.imagepreprocessor;

import org.scijava.Priority;
import org.scijava.plugin.Plugin;

import net.imagej.Dataset;
import net.imagej.axis.Axes;
import net.imagej.ops.special.computer.UnaryComputerOp;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.RealType;

@Plugin(type = ImagePreprocessor.class, priority = Priority.HIGH)
public class TubenessFilterPreprocessor extends AbstractImagePreprocessor {

	private static boolean DEFAULT_DO_PREPROCESS = false;
	private static double DEFAULT_SIGMA = 5;

	private double sigma = DEFAULT_SIGMA;
	private double[] calibrations = new double[] { 1, 1 };

	public TubenessFilterPreprocessor() {
		super();
		setDoPreprocess(DEFAULT_DO_PREPROCESS);
	}

	@Override
	public <T extends RealType<T>> void preprocess() {
		if (isDoPreprocess()) {
			Dataset dataset = getInput().duplicate();

			int[] fixedAxisIndices = new int[] { dataset.dimensionIndex(Axes.X), dataset.dimensionIndex(Axes.Y) };

			// Apply filter
			Img<T> out = (Img<T>) ops.create().img(dataset.getImgPlus().getImg());
			UnaryComputerOp op = (UnaryComputerOp) ops.op("filter.tubeness", out, dataset.getImgPlus(), sigma,
					calibrations);

			ops.slice(out, dataset.getImgPlus(), op, fixedAxisIndices);

			this.output = matchRAIToDataset(out, dataset);
		} else {
			this.output = getInput();
		}
	}

	public double getSigma() {
		return sigma;
	}

	public void setSigma(double sigma) {
		this.sigma = sigma;
	}

	public double[] getCalibrations() {
		return calibrations;
	}

	public void setCalibrations(double[] calibrations) {
		this.calibrations = calibrations;
	}

}
