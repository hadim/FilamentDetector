package fiji.plugin.filamentdetector.tracking;

import org.scijava.Named;
import org.scijava.plugin.RichPlugin;

import fiji.plugin.filamentdetector.model.Filaments;
import fiji.plugin.filamentdetector.model.TrackedFilaments;

public interface FilamentsTracker extends Named, RichPlugin {

	void setFilaments(Filaments filaments);

	Filaments getFilaments();

	void track();

	TrackedFilaments getTrackedFilaments();

	void setTrackedFilaments(TrackedFilaments trackedFilaments);

	@Override
	String toString();

}