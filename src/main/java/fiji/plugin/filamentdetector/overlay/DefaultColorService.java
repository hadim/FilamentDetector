package fiji.plugin.filamentdetector.overlay;

import java.awt.Color;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.scijava.log.LogService;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
import org.scijava.service.AbstractService;
import org.scijava.service.Service;

import net.imagej.lut.LUTService;
import net.imglib2.display.ColorTable;

@Plugin(type = Service.class)
public class DefaultColorService extends AbstractService implements ColorService {

	private static String DEFAULT_LUT = "glasbey";
	private static Map<String, URL> customLUTs;
	static {
		Map<String, URL> aMap = new HashMap<>();
		aMap.put("glasbey", DefaultColorService.class.getResource("/luts/glasbey.lut"));
		customLUTs = Collections.unmodifiableMap(aMap);
	}

	@Parameter
	private LUTService lutService;

	@Parameter
	private LogService log;

	private String lut;
	private ColorTable colorTable;

	@Override
	public int getLength() {
		return this.colorTable.getLength();
	}

	@Override
	public String getLut() {
		return lut;
	}

	@Override
	public void initialize() {
		initialize(DEFAULT_LUT);
	}

	@Override
	public void initialize(String lut) {
		try {
			// Look first in custom LUTs
			if (customLUTs.containsKey(lut)) {
				this.lut = lut;
				this.colorTable = lutService.loadLUT(customLUTs.get(lut));

				// Then look in LUTService
			} else if (lutService.findLUTs().containsKey(lut)) {
				this.lut = lut;
				this.colorTable = lutService.loadLUT(lutService.findLUTs().get(lut));

			} else {
				log.error("Can't load LUT called '" + lut + "'");
			}
		} catch (IOException e) {
			log.error(e);
		}
	}

	@Override
	public Color getColor(int colorCounter) {

		if (colorCounter >= getLength()) {
			colorCounter = colorCounter % getLength();
		}

		Color color = null;
		if (this.colorTable.getComponentCount() == 3) {
			color = new Color(this.colorTable.get(0, colorCounter), this.colorTable.get(1, colorCounter),
					this.colorTable.get(2, colorCounter));

		} else if (this.colorTable.getComponentCount() == 4) {
			color = new Color(this.colorTable.get(0, colorCounter), this.colorTable.get(1, colorCounter),
					this.colorTable.get(2, colorCounter), this.colorTable.get(3, colorCounter));
		} else {
			log.error("Invalid color component.");
		}
		return color;
	}

}
