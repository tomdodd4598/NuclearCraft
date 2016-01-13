package cofh.api.world;

import org.apache.logging.log4j.Logger;

import com.google.gson.JsonObject;

public interface IFeatureParser {

	/**
	 * Parse a {@link JsonObject} for registration with an with an {@link IFeatureHandler}.
	 * 
	 * @param featureName
	 *            The name of the feature to register.
	 * @param genObject
	 *            The JsonObject to parse.
	 * @param log
	 *            The {@link Logger} to log debug/error/etc. messages to.
	 * @return The {@link IFeatureGenerator} to be registered with an IFeatureHandler
	 */
	public IFeatureGenerator parseFeature(String featureName, JsonObject genObject, Logger log);

}
