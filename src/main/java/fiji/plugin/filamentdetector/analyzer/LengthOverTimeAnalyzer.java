package fiji.plugin.filamentdetector.analyzer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.io.FilenameUtils;
import org.scijava.Context;

import com.opencsv.CSVWriter;

import fiji.plugin.filamentdetector.Calibrations;
import fiji.plugin.filamentdetector.FilamentWorkflow;
import fiji.plugin.filamentdetector.model.TrackedFilament;
import fiji.plugin.filamentdetector.model.TrackedFilaments;
import net.imagej.Dataset;

public class LengthOverTimeAnalyzer extends AbstractAnalyzer implements Analyzer {

	public static String NAME = "Filament Growth Curve";
	public static String DESCRIPTION = "Generate the classic filament length versus time curve.";

	public LengthOverTimeAnalyzer(Context context, FilamentWorkflow filamentWorkflow) {
		super(context, filamentWorkflow);
		setName(NAME);
		setDescription(DESCRIPTION);
	}

	public String getInfo() {
		String out = "";
		out += "Name : " + this.name + "\n";
		out += "Save results : " + this.saveResults + "\n";
		out += "\n";
		return out;
	}

	@Override
	public void analyze() {

		this.resultMessage = "";

		Calibrations calibrations = filamentWorkflow.getCalibrations();
		TrackedFilaments trackedFilaments = filamentWorkflow.getTrackedFilaments();

		List<Integer> filamentIDs = new ArrayList<>();
		List<Double> lengths = new ArrayList<>();
		List<Double> times = new ArrayList<>();

		List<Double> lengthsTrackedFilament;
		List<Double> timesTrackedFilament;

		for (TrackedFilament trackedFilament : trackedFilaments) {

			lengthsTrackedFilament = trackedFilament.getLengths();
			timesTrackedFilament = trackedFilament.getFrames();

			// Convert using calibrations
			lengthsTrackedFilament = lengthsTrackedFilament.stream().map(x -> x * calibrations.getDx())
					.collect(Collectors.toList());
			timesTrackedFilament = timesTrackedFilament.stream().map(x -> x * calibrations.getDt())
					.collect(Collectors.toList());

			filamentIDs.addAll(Collections.nCopies(lengthsTrackedFilament.size(), trackedFilament.getId()));
			lengths.addAll(lengthsTrackedFilament);
			times.addAll(timesTrackedFilament);
		}

		this.results = new HashMap<>();
		this.results.put("ids", filamentIDs);
		this.results.put("lengths", lengths);
		this.results.put("times", times);

		if (saveResults) {

			// Set the name of the file
			Dataset dataset = (Dataset) filamentWorkflow.getSourceImage().getActiveView().getData();
			if (dataset.getSource() != null && dataset.getSource() != "") {
				String filePath = FilenameUtils.removeExtension(dataset.getSource());
				filePath += "-LengthOverTime.csv";

				File file = new File(filePath);
				try {
					CSVWriter writer = new CSVWriter(new FileWriter(file), ';');

					writer.writeNext(Arrays.asList("filament id", "time", "length").stream().toArray(String[]::new));
					List<String[]> data = new ArrayList<>();
					List<String> row;
					for (int i = 0; i < lengths.size(); i++) {
						row = new ArrayList<>();
						row.add(Integer.toString(filamentIDs.get(i)));
						row.add(Double.toString(times.get(i)));
						row.add(Double.toString(lengths.get(i)));
						data.add(row.toArray(new String[0]));
					}
					writer.writeAll(data);

				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			else {
				this.resultMessage += "Can't save the result file !";
			}
		}
	}

}
