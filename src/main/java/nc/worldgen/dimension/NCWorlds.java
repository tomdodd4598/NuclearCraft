package nc.worldgen.dimension;

import static nc.config.NCConfig.*;

import nc.Global;
import net.minecraft.world.*;
import net.minecraftforge.common.DimensionManager;

public class NCWorlds {
	
	public static final String WASTELAND_DIM_NAME = "nc_wasteland";
	public static final int WASTELAND_DIM_ID = wasteland_dimension;
	public static final DimensionType WASTELAND_DIM_TYPE = DimensionType.register(WASTELAND_DIM_NAME, "_" + WASTELAND_DIM_NAME, WASTELAND_DIM_ID, WorldProviderWasteland.class, false);
	public static final WorldProvider WASTELAND_WORLD_PROVIDER = new WorldProviderWasteland();
	
	public static final void registerDimensions() {
		if (wasteland_dimension_gen) {
			DimensionManager.registerDimension(WASTELAND_DIM_ID, WASTELAND_DIM_TYPE);
		}
	}
}
