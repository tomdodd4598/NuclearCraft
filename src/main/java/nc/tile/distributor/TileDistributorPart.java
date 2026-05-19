package nc.tile.distributor;

import nc.multiblock.distributor.Distributor;
import nc.tile.multiblock.TileMultiblockPart;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.Optional;

@Optional.InterfaceList({@Optional.Interface(iface = "ic2.api.energy.tile.IEnergySink", modid = "ic2"), @Optional.Interface(iface = "ic2.api.energy.tile.IEnergySource", modid = "ic2")})
public abstract class TileDistributorPart extends TileMultiblockPart<Distributor, IDistributorPart> implements IDistributorPart {
	
	public TileDistributorPart() {
		super(Distributor.class, IDistributorPart.class);
	}
	
	@Override
	public Distributor createNewMultiblock() {
		return new Distributor(world);
	}
	
	// NBT
	
	@Override
	public NBTTagCompound writeAll(NBTTagCompound nbt) {
		super.writeAll(nbt);
		return nbt;
	}
	
	@Override
	public void readAll(NBTTagCompound nbt) {
		super.readAll(nbt);
	}
}
