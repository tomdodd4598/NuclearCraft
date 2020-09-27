package nc.worldgen.dimension;

import net.minecraft.world.World;
import net.minecraft.world.gen.ChunkGeneratorOverworld;
import net.minecraftforge.fluids.FluidRegistry;

public class ChunkGeneratorWasteland extends ChunkGeneratorOverworld {
	
	public ChunkGeneratorWasteland(World world) {
		super(world, world.getSeed(), world.getWorldInfo().isMapFeaturesEnabled(), getGeneratorOptions());
		oceanBlock = FluidRegistry.getFluid("corium").getBlock().getDefaultState();
	}
	
	// net.minecraft.client.gui.GuiScreenCustomizePresets
	public static String getGeneratorOptions() {
		return "{ \"useCaves\":false, \"useDungeons\":false, \"dungeonChance\":8, \"useStrongholds\":false, \"useVillages\":false, \"useMineShafts\":false, \"useTemples\":true, \"useRavines\":true, \"useWaterLakes\":false, \"waterLakeChance\":4, \"useLavaLakes\":true, \"lavaLakeChance\":80, \"useLavaOceans\":true, \"seaLevel\":63 }";
	}
}
