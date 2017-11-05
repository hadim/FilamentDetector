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
package fiji.plugin.filamentdetector;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import ij.gui.Plot;
import net.imagej.Dataset;
import net.imagej.axis.Axes;
import net.imglib2.RealPoint;
import net.imglib2.RealRandomAccess;
import net.imglib2.img.Img;
import net.imglib2.interpolation.InterpolatorFactory;
import net.imglib2.interpolation.randomaccess.NLinearInterpolatorFactory;
import net.imglib2.type.numeric.RealType;
import net.imglib2.view.Views;

public class GeometryUtils {

	public static RealPoint add(RealPoint point1, RealPoint point2) {
		RealPoint point = new RealPoint();
		for (int d = 0; d < point1.numDimensions(); d++) {
			point.setPosition(point1.getDoublePosition(d) + point2.getDoublePosition(d), d);
		}
		return point;
	}

	public static RealPoint subtract(RealPoint point1, RealPoint point2) {
		RealPoint point = new RealPoint();
		for (int d = 0; d < point1.numDimensions(); d++) {
			point.setPosition(point1.getDoublePosition(d) - point2.getDoublePosition(d), d);
		}
		return point;
	}

	public static double distance(RealPoint point1, RealPoint point2) {
		double sum = 0;
		for (int d = 0; d < point1.numDimensions(); d++) {
			sum += Math.pow(point1.getDoublePosition(d) - point2.getDoublePosition(d), 2);
		}
		return Math.sqrt(sum);
	}

	public static <T> INDArray getIntensities(List<RealPoint> line, Dataset dataset, int frame, int channel, int z) {
		InterpolatorFactory<? extends RealType<?>, Img<? extends RealType<?>>> interpolator = new NLinearInterpolatorFactory();
		return getIntensities(line, dataset, interpolator, frame, channel, z);
	}

	public static <T> INDArray getIntensities(List<RealPoint> line, Dataset dataset,
			InterpolatorFactory<? extends RealType<?>, Img<? extends RealType<?>>> interpolator, int frame, int channel,
			int z) {

		Img<? extends RealType<?>> img = dataset.getImgPlus().getImg();
		RealRandomAccess<? extends RealType<?>> interpolated = Views.interpolate(img, interpolator).realRandomAccess();

		double[] intensities = new double[line.size()];
		int xIndex = dataset.dimensionIndex(Axes.X);
		int yIndex = dataset.dimensionIndex(Axes.Y);
		int zIndex = dataset.dimensionIndex(Axes.Z);
		int timeIndex = dataset.dimensionIndex(Axes.TIME);
		int channelIndex = dataset.dimensionIndex(Axes.CHANNEL);

		for (int i = 0; i < line.size(); i++) {

			interpolated.setPosition(line.get(i).getDoublePosition(0), xIndex);
			interpolated.setPosition(line.get(i).getDoublePosition(1), yIndex);

			if (zIndex >= 0) {
				interpolated.setPosition(z, zIndex);
			}
			if (timeIndex >= 0) {
				interpolated.setPosition(frame, timeIndex);
			}
			if (channelIndex >= 0) {
				interpolated.setPosition(channel, channelIndex);
			}

			try {
				intensities[i] = interpolated.get().getRealDouble();
			} catch (Exception err) {
				intensities[i] = 0;
			}
		}

		return Nd4j.create(intensities);
	}

	public static List<RealPoint> getLinePointsFromNumberOfPoints(RealPoint start, RealPoint end, int numPts) {
		List<RealPoint> line = new ArrayList<RealPoint>();
		double x;
		double y;
		for (int i = 0; i < numPts; i++) {
			x = i * (end.getDoublePosition(0) - start.getDoublePosition(0)) / (numPts - 1) + start.getDoublePosition(0);
			y = i * (end.getDoublePosition(1) - start.getDoublePosition(1)) / (numPts - 1) + start.getDoublePosition(1);
			line.add(new RealPoint(x, y));
		}
		return line;
	}

	public static List<RealPoint> getLinePointsFromSpacing(RealPoint start, RealPoint end, double spacing) {
		List<RealPoint> line = new ArrayList<RealPoint>();
		double dist = distance(start, end);
		long nPoints = (long) (dist / spacing);
		line.add(start);
		for (long i = 1; i <= nPoints; i++) {
			line.add(getPointOnVectorFromDistance(start, end, spacing * i));
		}
		// line.add(end);
		return line;
	}

	public static RealPoint getPointOnVectorFromDistance(RealPoint start, RealPoint end, double distance) {
		double distRatio = distance / Math.sqrt(Math.pow(start.getDoublePosition(0) - end.getDoublePosition(0), 2)
				+ Math.pow(start.getDoublePosition(1) - end.getDoublePosition(1), 2));
		double x = ((1 - distRatio) * start.getDoublePosition(0) + distRatio * end.getDoublePosition(0));
		double y = ((1 - distRatio) * start.getDoublePosition(1) + distRatio * end.getDoublePosition(1));
		return new RealPoint(x, y);
	}

	public static float[] getPointOnVectorFromDistance(float[] start, float[] end, double distance) {
		float distRatio = (float) (distance
				/ Math.sqrt(Math.pow(start[0] - end[0], 2) + Math.pow(start[1] - end[1], 2)));
		float x = (1 - distRatio) * start[0] + distRatio * end[0];
		float y = (1 - distRatio) * start[1] + distRatio * end[1];
		return new float[] { x, y };
	}

	public static double[] getPointOnVectorFromDistance(double[] start, double[] end, double distance) {
		double distRatio = distance / Math.sqrt(Math.pow(start[0] - end[0], 2) + Math.pow(start[1] - end[1], 2));
		double x = (1 - distRatio) * start[0] + distRatio * end[0];
		double y = (1 - distRatio) * start[1] + distRatio * end[1];
		return new double[] { x, y };
	}

	public static Plot plotPoints(double[] y) {
		double[] x = IntStream.range(0, y.length).mapToDouble(i -> i).toArray();
		return plotPoints(x, y);
	}

	public static Plot plotPoints(double[] x, double[] y) {
		List<RealPoint> points = IntStream.range(0, x.length).mapToObj(i -> new RealPoint(x[i], y[i]))
				.collect(Collectors.toList());
		return plotPoints(points);
	}

	public static Plot plotPoints(List<RealPoint> points) {

		double[] x = points.stream().mapToDouble(p -> p.getDoublePosition(0)).toArray();
		double[] y = points.stream().mapToDouble(p -> p.getDoublePosition(1)).toArray();

		Plot plot = new Plot("", "x", "y", x, y);
		plot.show();

		return plot;
	}

	public static Plot plotPoints(INDArray y) {
		INDArray x = Nd4j.arange(0, y.size(1));
		return plotPoints(x, y);
	}

	public static Plot plotPoints(INDArray x, INDArray y) {

		double[] xPoints = x.data().asDouble();
		double[] yPoints = y.data().asDouble();

		Plot plot = new Plot("", "x", "y", xPoints, yPoints);
		plot.addPoints(xPoints, yPoints, 0);
		plot.show();

		return plot;
	}

}
