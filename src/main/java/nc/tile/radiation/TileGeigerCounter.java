package nc.tile.radiation;

import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.SimpleComponent;
import nc.Global;
import nc.capability.radiation.source.IRadiationSource;
import nc.radiation.RadiationHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.Optional;

@Optional.Interface(iface = "li.cil.oc.api.network.SimpleComponent", modid = "opencomputers")
public class TileGeigerCounter extends TileEntity implements SimpleComponent {
	
	public double getChunkRadiationLevel() {
		IRadiationSource chunkRadiation = RadiationHelper.getRadiationSource(world.getChunk(pos));
		return chunkRadiation == null ? 0D : chunkRadiation.getRadiationLevel();
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
