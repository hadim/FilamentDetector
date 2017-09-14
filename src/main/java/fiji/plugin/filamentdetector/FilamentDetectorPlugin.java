package fiji.plugin.filamentdetector;

import java.util.Arrays;
import java.util.List;

import org.scijava.ItemIO;
import org.scijava.command.Command;
import org.scijava.convert.ConvertService;
import org.scijava.log.LogService;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import de.biomedical_imaging.ij.steger.Line;
import fiji.plugin.filamentdetector.ui.OverlayFactory;
import ij.gui.Roi;
import net.imagej.Dataset;
import net.imagej.ImageJ;
import net.imglib2.type.numeric.integer.UnsignedByteType;

@Plugin(type = Command.class, menuPath = "Plugins>Tracking>FilamentDetector")
public class FilamentDetectorPlugin implements Command {

	@Parameter
	private ImageJ ij;

	@Parameter
	private LogService log;

	@Parameter
	private ConvertService convert;

	@Parameter(type = ItemIO.INPUT)
	private Dataset image;

	public static final String PLUGIN_NAME = "FilamentDetector";
	public static final String VERSION = version();

	private static String version() {
		String version = null;
		final Package pack = FilamentDetectorPlugin.class.getPackage();
		if (pack != null) {
			version = pack.getImplementationVersion();
		}
		return version == null ? "DEVELOPMENT" : version;
	}

	@Override
	public void run() {

		log.info("Running " + PLUGIN_NAME + " version " + VERSION);

		// Check image exist on disk
		if (image.getSource() == "") {
			log.error("Please save the image on disk before processing it.");
			return;
		}

		// Check image is 8-bit
		if (image.getType().getClass() != UnsignedByteType.class) {
			log.error("Please convert the image to 8-bit first.");
			return;
		}

		// Get physical pixel sizes (um) and duration between frames (s)
		Calibrations cals = new Calibrations(image);
		log.info(cals.getDx());
		log.info(cals.getDt());

		// Setup parameters
		DetectionParameters params = new DetectionParameters();
		params.setSigma(1.51);

		// Detect filaments
		Detector detector = new Detector(ij.context(), image, params);
		detector.detect();
		Filaments filaments = detector.getFilaments();
		
		// Simplify filaments by reducing the number of points
		double toleranceDistance = 10;
		filaments = filaments.simplify(toleranceDistance);

		// Filter lines by length and sinuosity
		double maxLength = 500;
		double minLength = 5;
		double maxSinuosity = 1.2;
		double minSinuosity = 0;

		Filaments filteredFilaments = filaments.stream().filter(filament -> filament.getLength() < maxLength)
				.filter(filament -> filament.getLength() > minLength)
				.filter(filament -> filament.getSinuosity() < maxSinuosity)
				.filter(filament -> filament.getSinuosity() > minSinuosity)
				.collect(Filaments::new, Filaments::add, Filaments::addAll);

		log.info(filteredFilaments.info());

		log.info("Size before filters : " + filaments.size());
		log.info("Size after filters : " + filteredFilaments.size());
		log.info("\n");

		// Show lines as ROIs
		List<Roi> rois = OverlayFactory.createROIs(filteredFilaments);
		OverlayFactory.displayInROIManager(rois);
		
		// Build GUI to do live fine parameters tuning
		// Track filaments
		// Export data
		// Build kymographs automatically

	}

}
