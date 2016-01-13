package cofh.api.world;

import java.util.List;

import net.minecraft.world.gen.feature.WorldGenerator;

import org.apache.logging.log4j.Logger;

import cofh.lib.util.WeightedRandomBlock;

import com.google.gson.JsonObject;

public interface IGeneratorParser {

	/**
	 * Parse a {@link JsonObject} for registration with an with an {@link IFeatureGenerator}.
	 *
	 * @param generatorName
	 *            The name of the generator to register.
	 * @param genObject
	 *            The JsonObject to parse.
	 * @param log
	 *            The {@link Logger} to log debug/error/etc. messages to.
	 * @param resList
	 *            The processed list of resources to generate
	 * @param clusterSize
	 *            The processed size of the cluster
	 * @param matList
	 *            The processed list of materials to generate in
	 * @return The {@link WorldGenerator} to be registered with an IFeatureGenerator
	 */
	public WorldGenerator parseGenerator(String generatorName, JsonObject genObject, Logger log,
			List<WeightedRandomBlock> resList, int clusterSize, List<WeightedRandomBlock> matList);

}
