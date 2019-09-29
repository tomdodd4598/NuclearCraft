package nc.worldgen.dimension;

import nc.Global;
import nc.config.NCConfig;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.minecraftforge.common.DimensionManager;

public class NCWorlds {
	
	public static final String WASTELAND_DIM_NAME = Global.MOD_SHORT_ID + "_wasteland";
	public static final int WASTELAND_DIM_ID = NCConfig.wasteland_dimension;
	public static final DimensionType WASTELAND_DIM_TYPE = DimensionType.register(WASTELAND_DIM_NAME, "_" + WASTELAND_DIM_NAME, WASTELAND_DIM_ID, WorldProviderWasteland.class, false);
	public static final WorldProvider WASTELAND_WORLD_PROVIDER = new WorldProviderWasteland();
	
	public static final void registerDimensions() {
		if (NCConfig.wasteland_dimension_gen) DimensionManager.registerDimension(WASTELAND_DIM_ID, WASTELAND_DIM_TYPE);
	}
}
