package nc.tile.fluid;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import nc.config.NCConfig;
import nc.enumm.MetaEnums.CoolerType;
import nc.tile.dummy.IInterfaceable;
import nc.tile.energyFluid.IBufferable;
import nc.tile.internal.fluid.TankSorption;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

public class TileActiveCooler extends TileFluid implements IInterfaceable, IBufferable, IFluidSpread {
	
	protected int drainCount;
	
	public boolean isActive = false;
	
	private static List<String> validFluids() {
		List<String> validFluids = new ArrayList<String>();
		for (int i = 1; i < CoolerType.values().length; i++) validFluids.add(CoolerType.values()[i].getFluidName());
		return validFluids;
	}
	
	private static final int DRAIN_MULT = Math.max(1, NCConfig.machine_update_rate*NCConfig.active_cooler_max_rate/20);
	
	public TileActiveCooler() {
		super(100*DRAIN_MULT, validFluids(), ITileFluid.fluidConnectionAll(TankSorption.IN));
	}
	
	@Override
	public void update() {
		super.update();
		if (!world.isRemote) {
			if (drainCount == 0) {
				spreadFluid();
				if (isActive) getTanks().get(0).drain(DRAIN_MULT, true);
			}
			tickDrain();
		}
	}
	
	public void tickDrain() {
		drainCount++; drainCount %= NCConfig.machine_update_rate;
	}
	
	@Override
	public void spreadFluidToSide(@Nonnull EnumFacing side) {
		TileEntity tile = getTileWorld().getTileEntity(getTilePos().offset(side));
		
		if (tile instanceof TileActiveCooler) {
			TileActiveCooler other = (TileActiveCooler) tile;
			
			int diff = getTanks().get(0).getFluidAmount() - other.getTanks().get(0).getFluidAmount();
			if (diff > 1) {
				getTanks().get(0).drain(other.getTanks().get(0).fillInternal(getTanks().get(0).drain(diff/2, false), true), true);
			}
		}
	}
	
	@Override
	public NBTTagCompound writeAll(NBTTagCompound nbt) {
		super.writeAll(nbt);
		nbt.setBoolean("isActive", isActive);
		return nbt;
		
	}
	
	@Override
	public void readAll(NBTTagCompound nbt) {
		super.readAll(nbt);
		isActive = nbt.getBoolean("isActive");
	}
}
