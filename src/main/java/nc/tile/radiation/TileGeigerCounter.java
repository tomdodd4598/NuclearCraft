package nc.tile.radiation;

import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.SimpleComponent;
import nc.Global;
import nc.capability.radiation.source.IRadiationSource;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.common.Optional;

@Optional.Interface(iface = "li.cil.oc.api.network.SimpleComponent", modid = "opencomputers")
public class TileGeigerCounter extends TileEntity implements SimpleComponent {
	
	public double getChunkRadiationLevel() {
		Chunk chunk = world.getChunkFromBlockCoords(pos);
		if (chunk == null || !chunk.hasCapability(IRadiationSource.CAPABILITY_RADIATION_SOURCE, null)) return 0D;
		IRadiationSource chunkRadiation = chunk.getCapability(IRadiationSource.CAPABILITY_RADIATION_SOURCE, null);
		if (chunkRadiation == null) return 0D;
		
		return chunkRadiation.getRadiationLevel();
	}
	
	@Override
	@Optional.Method(modid = "opencomputers")
	public String getComponentName() {
		return Global.MOD_SHORT_ID + "_geiger_counter";
	}
	
	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] getChunkRadiationLevel(Context context, Arguments args) {
		return new Object[] {getChunkRadiationLevel()};
	}
}
