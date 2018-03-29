package nc.worldgen.dimension;

import nc.worldgen.biome.NCBiomes;
import net.minecraft.world.biome.BiomeProviderSingle;

public class BiomeProviderWasteland extends BiomeProviderSingle {
	
	public BiomeProviderWasteland() {
		super(NCBiomes.NUCLEAR_WASTELAND);
	}
}
