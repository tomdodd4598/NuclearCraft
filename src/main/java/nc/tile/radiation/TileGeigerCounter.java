package nc.tile.radiation;

import static nc.config.NCConfig.radiation_geiger_block_redstone;

import li.cil.oc.api.machine.*;
import li.cil.oc.api.network.SimpleComponent;
import nc.Global;
import nc.capability.radiation.source.IRadiationSource;
import nc.radiation.RadiationHelper;
import nc.tile.NCTile;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.Optional;

@Optional.Interface(iface = "li.cil.oc.api.network.SimpleComponent", modid = "opencomputers")
public class TileGeigerCounter extends NCTile implements SimpleComponent {
	
	public int comparatorStrength = 0;
	
	@Override
	public void update() {
		super.update();
		if (!world.isRemote) {
			boolean shouldUpdate = false;
			int compStrength = getComparatorStrength();
			if (comparatorStrength != compStrength) {
				shouldUpdate = true;
			}
			comparatorStrength = compStrength;
			if (shouldUpdate) {
				markDirty();
			}
		}
	}
	
	public double getChunkRadiationLevel() {
		IRadiationSource chunkRadiation = RadiationHelper.getRadiationSource(world.getChunk(pos));
		return chunkRadiation == null ? 0D : chunkRadiation.getRadiationLevel();
	}
	
	public int getComparatorStrength() {
		double radiation = getChunkRadiationLevel();
		return radiation <= 0D ? 0 : Math.max(0, (int) (15D + Math.log10(radiation) - radiation_geiger_block_redstone));
	}
	
	// NBT
	
	@Override
	public NBTTagCompound writeAll(NBTTagCompound nbt) {
		super.writeAll(nbt);
		nbt.setInteger("comparatorStrength", comparatorStrength);
		return nbt;
	}
	
	@Override
	public void readAll(NBTTagCompound nbt) {
		super.readAll(nbt);
		comparatorStrength = nbt.getInteger("comparatorStrength");
	}
	
	// OpenComputers
	
	@Override
	@Optional.Method(modid = "opencomputers")
	public String getComponentName() {
		return "nc_geiger_counter";
	}
	
	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] getChunkRadiationLevel(Context context, Arguments args) {
		return new Object[] {getChunkRadiationLevel()};
	}
}
