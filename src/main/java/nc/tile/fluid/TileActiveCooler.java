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
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

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
		super(80*DRAIN_MULT, validFluids(), ITileFluid.fluidConnectionAll(TankSorption.IN));
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
		if (!getFluidConnection(side).canConnect()) {
			return;
		}
		TileEntity tile = getTileWorld().getTileEntity(getTilePos().offset(side));
		if (!(tile instanceof TileActiveCooler)) {
			return;
		}
		IFluidHandler adjStorage = tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side.getOpposite());
		if (adjStorage == null) {
			return;
		}
		for (int i = 0; i < getTanks().size(); i++) {
			if (!getFluidConnection(side).getTankSorption(i).canConnect() || getTanks().get(i).getFluid() == null) {
				continue;
			}
			int maxDrain = getTanks().get(i).getFluidAmount()/2;
			FluidStack stack = adjStorage.getTankProperties()[0].getContents();
			if (stack != null) {
				maxDrain -= stack.amount/2;
			}
			if (maxDrain > 0) {
				getTanks().get(i).drain(adjStorage.fill(getTanks().get(i).drain(maxDrain, false), true), true);
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
