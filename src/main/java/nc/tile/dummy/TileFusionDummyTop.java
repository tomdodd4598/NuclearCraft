package nc.tile.dummy;

import nc.ModCheck;
import nc.block.tile.generator.BlockFusionCore;
import nc.config.NCConfig;
import nc.tile.generator.TileFusionCore;
import net.darkhax.tesla.capability.TeslaCapabilities;
import net.minecraft.block.BlockRedstoneComparator;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

public class TileFusionDummyTop extends TileDummy {
	
	private static final String[] FUSION_FUELS = new String[] {"hydrogen", "deuterium", "tritium", "helium3", "lithium6", "lithium7", "boron11"};
	
	private static final String[][] ALLOWED_FUELS = new String[][] {FUSION_FUELS, FUSION_FUELS, {}, {}, {}, {}, {}, {}};
	
	public TileFusionDummyTop() {
		super("fusion_dummy_top", NCConfig.fusion_update_rate, ALLOWED_FUELS);
	}
	
	public void update() {
		super.update();
		if(!worldObj.isRemote) {
			pushEnergy();
			pushFluid();
		}
		if (findAdjacentComparator() && shouldCheck()) markDirty();
	}
	
	public void tick() {
		if (tickCount > NCConfig.fusion_update_rate) {
			tickCount = 0;
		} else {
			tickCount++;
		}
	}
	
	public boolean shouldCheck() {
		return tickCount > NCConfig.fusion_update_rate;
	}
	
	// Redstone Flux
	
	public boolean canExtract() {
		if (getMaster() != null) {
			if (isMaster(masterPosition)) return ((TileFusionCore) getMaster()).isHotEnough();
		}
		return false;
	}

	public boolean canReceive() {
		if (getMaster() != null) {
			if (isMaster(masterPosition)) return !((TileFusionCore) getMaster()).isHotEnough();
		}
		return false;
	}
	
	// Finding Blocks
	
	private BlockPos getPos(int x, int y, int z) {
		return new BlockPos(pos.getX() + x, pos.getY() + y, pos.getZ() + z);
	}
	
	private boolean findCore(int x, int y, int z) {
		return worldObj.getBlockState(getPos(x, y, z)).getBlock() instanceof BlockFusionCore;
	}
	
	private BlockPos position(int x, int y, int z) {
		int xCheck = getPos().getX();
		int yCheck = getPos().getY() + y;
		int zCheck = getPos().getZ();
		
		if (getBlockMetadata() == 4) {
			return new BlockPos(xCheck + x, yCheck, zCheck + z);
		}
		if (getBlockMetadata() == 2) {
			return new BlockPos(xCheck - z, yCheck, zCheck + x);
		}
		if (getBlockMetadata() == 5) {
			return new BlockPos(xCheck - x, yCheck, zCheck - z);
		}
		if (getBlockMetadata() == 3) {
			return new BlockPos(xCheck + z, yCheck, zCheck - x);
		}
		else return new BlockPos(xCheck + x, yCheck, zCheck + z);
	}
	
	public boolean findAdjacentComparator() {
		if (worldObj.getBlockState(position(1, 0, 0)).getBlock() instanceof BlockRedstoneComparator) return true;
		if (worldObj.getBlockState(position(-1, 0, 0)).getBlock() instanceof BlockRedstoneComparator) return true;
		if (worldObj.getBlockState(position(0, 1, 0)).getBlock() instanceof BlockRedstoneComparator) return true;
		if (worldObj.getBlockState(position(0, -1, 0)).getBlock() instanceof BlockRedstoneComparator) return true;
		if (worldObj.getBlockState(position(0, 0, 1)).getBlock() instanceof BlockRedstoneComparator) return true;
		if (worldObj.getBlockState(position(0, 0, -1)).getBlock() instanceof BlockRedstoneComparator) return true;
		return false;
	}
	
	// Find Master
	
	protected void findMaster() {
		if (findCore(0, -2, 0)) masterPosition = getPos(0, -2, 0);
		else if (findCore(1, -2, 0)) masterPosition = getPos(1, -2, 0);
		else if (findCore(1, -2, 1)) masterPosition = getPos(1, -2, 1);
		else if (findCore(0, -2, 1)) masterPosition = getPos(0, -2, 1);
		else if (findCore(-1, -2, 1)) masterPosition = getPos(-1, -2, 1);
		else if (findCore(-1, -2, 0)) masterPosition = getPos(-1, -2, 0);
		else if (findCore(-1, -2, -1)) masterPosition = getPos(-1, -2, -1);
		else if (findCore(0, -2, -1)) masterPosition = getPos(0, -2, -1);
		else if (findCore(1, -2, -1)) masterPosition = getPos(1, -2, -1);
		else masterPosition = null;
	}
	
	public boolean isMaster(BlockPos pos) {
		return worldObj.getTileEntity(pos) instanceof TileFusionCore;
	}
	
	// Capability
	
	@SuppressWarnings("unchecked")
	public <T> T getCapability(net.minecraftforge.common.capabilities.Capability<T> capability, @javax.annotation.Nullable net.minecraft.util.EnumFacing facing) {
		if (CapabilityEnergy.ENERGY == capability && connection.canConnect()) {
			return (T) getStorage();
		}
		if (connection != null && ModCheck.teslaLoaded && connection.canConnect()) {
			if ((capability == TeslaCapabilities.CAPABILITY_CONSUMER && connection.canReceive()) || (capability == TeslaCapabilities.CAPABILITY_PRODUCER && connection.canExtract()) || capability == TeslaCapabilities.CAPABILITY_HOLDER)
				return (T) getStorage();
		}
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(this);
		}
		return super.getCapability(capability, facing);
	}
}
