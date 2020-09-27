package nc.worldgen.dimension;

import static nc.config.NCConfig.*;

import net.minecraft.world.DimensionType;
import net.minecraftforge.common.DimensionManager;

public class NCWorlds {
	
	public static DimensionType wastelandDimType;
	
	public static final void registerDimensions() {
		if (wasteland_dimension_gen) {
			wastelandDimType = DimensionType.register("nc_wasteland", "_nc_wasteland", wasteland_dimension, WorldProviderWasteland.class, false);
			DimensionManager.registerDimension(wasteland_dimension, wastelandDimType);
		}
	}
}
