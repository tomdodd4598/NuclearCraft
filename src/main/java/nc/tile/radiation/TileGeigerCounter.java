package nc.tile.radiation;

import li.cil.oc.api.machine.*;
import li.cil.oc.api.network.SimpleComponent;
import nc.capability.radiation.source.IRadiationSource;
import nc.radiation.RadiationHelper;
import nc.tile.NCTile;
import nc.util.NCMath;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraftforge.fml.common.Optional;

import static nc.config.NCConfig.radiation_geiger_block_redstone;

@Optional.Interface(iface = "li.cil.oc.api.network.SimpleComponent", modid = "opencomputers")
public class TileGeigerCounter extends NCTile implements ITickable, SimpleComponent {
	
	public int comparatorStrength = 0;
	
	@Override
	public void update() {
		if (!world.isRemote) {
			boolean shouldUpdate = false;
			int compStrength = getComparatorStrength();
			if (comparatorStrength != compStrength) {
				shouldUpdate = true;
			}
			comparatorStrength = compStrength;
			if (shouldUpdate) {
				markDirty();
				updateComparatorOutputLevel();
			}
		}
	}
	
	public double getChunkRadiationLevel() {
		IRadiationSource chunkRadiation = RadiationHelper.getRadiationSource(world.getChunk(pos));
		return chunkRadiation == null ? 0D : chunkRadiation.getRadiationLevel();
	}
	
	public int getComparatorStrength() {
		double radiation = getChunkRadiationLevel();
		return radiation <= 0D ? 0 : Math.max(0, NCMath.toInt(15D + Math.log10(radiation) - radiation_geiger_block_redstone));
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
	
	@Callback(direct = true)
	@Optional.Method(modid = "opencomputers")
	public Object[] getChunkRadiationLevel(Context context, Arguments args) {
		return new Object[] {getChunkRadiationLevel()};
	}
}
