package fiji.plugin.filamentdetector.preprocessing;

import org.scijava.Context;

import net.imagej.Dataset;
import net.imagej.ops.convert.RealTypeConverter;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.RealType;

public class NormalizeIntensitiesPreprocessor extends AbstractImagePreprocessor {

	private static boolean DEFAULT_DO_PREPROCESS = true;

	public NormalizeIntensitiesPreprocessor(Context context) {
		super(context);
		setDoPreprocess(DEFAULT_DO_PREPROCESS);
	}

	@Override
	public <T extends RealType<T>> void preprocess() {
		if (isDoPreprocess()) {
			Dataset dataset = getInput().duplicate();

			// Normalize intensity
			Img<T> out = (Img<T>) ops.create().img(dataset.getImgPlus());
			RealTypeConverter op = (RealTypeConverter) ops.op("convert.normalizeScale", out.firstElement(),
					dataset.getImgPlus().firstElement());
			ops.convert().imageType(out, (Img<T>) dataset.getImgPlus(), op);

			this.output = matchRAIToDataset(out, dataset);
		} else {
			this.output = getInput();
		}
	}

}