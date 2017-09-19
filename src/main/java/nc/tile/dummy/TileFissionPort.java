package nc.tile.dummy;

import nc.ModCheck;
import nc.block.tile.dummy.BlockFissionPort;
import nc.block.tile.generator.BlockFissionController;
import nc.block.tile.passive.BlockBuffer;
import nc.config.NCConfig;
import nc.energy.EnumStorage.EnergyConnection;
import nc.init.NCBlocks;
import nc.tile.generator.TileFissionController;
import net.darkhax.tesla.capability.TeslaCapabilities;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.energy.CapabilityEnergy;

public class TileFissionPort extends TileDummy {
	
	public int tickCount;

	public TileFissionPort() {
		super("fission_port", EnergyConnection.OUT, NCConfig.fission_update_rate);
	}
	
	public void update() {
		super.update();
		if(!world.isRemote) {
			pushEnergy();
			pushFluid();
		}
	}
	
	// Finding Blocks
	
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
	
	private boolean findCasingPort(int x, int y, int z) {
		if (world.getBlockState(position(x, y, z)) == NCBlocks.fission_block.getStateFromMeta(0)) return true;
		if (world.getBlockState(position(x, y, z)).getBlock() instanceof BlockFissionPort) return true;
		if (world.getBlockState(position(x, y, z)).getBlock() instanceof BlockBuffer) return true;
		return false;
	}
	
	private boolean findController(int x, int y, int z) {
		return world.getBlockState(position(x, y, z)).getBlock() instanceof BlockFissionController;
	}
	
	private boolean findCasingControllerPort(int x, int y, int z) {
		if (world.getBlockState(position(x, y, z)) == NCBlocks.fission_block.getStateFromMeta(0)) return true;
		if (world.getBlockState(position(x, y, z)).getBlock() instanceof BlockFissionController) return true;
		if (world.getBlockState(position(x, y, z)).getBlock() instanceof BlockFissionPort) return true;
		if (world.getBlockState(position(x, y, z)).getBlock() instanceof BlockBuffer) return true;
		return false;
	}
	
	// Find Master
	
	protected void findMaster() {
		int l = NCConfig.fission_max_size + 2;
		boolean f = false;
		int rz = 0;
		int z0 = 0;
		int x0 = 0;
		int y0 = 0;
		int z1 = 0;
		int x1 = 0;
		int y1 = 0;
		for (int z = 0; z <= l; z++) {
			if ((findCasingPort(0, 1, 0) || findCasingPort(0, -1, 0)) || ((findCasingPort(1, 1, 0) || findCasingPort(1, -1, 0)) && findCasingPort(1, 0, 0)) || ((findCasingPort(1, 1, 0) && !findCasingPort(1, -1, 0)) && !findCasingPort(1, 0, 0)) || ((!findCasingPort(1, 1, 0) && findCasingPort(1, -1, 0)) && !findCasingPort(1, 0, 0))) {
				if (!findCasingPort(0, 1, -z) && !findCasingPort(0, -1, -z) && (findCasingControllerPort(0, 0, -z + 1) || findCasingControllerPort(0, 1, -z + 1) || findCasingControllerPort(0, -1, -z + 1))) {
					rz = l - z;
					z0 = -z;
					f = true;
					break;
				}
			} else if (!findCasingPort(0, 0, -z) && !findCasingPort(1, 1, -z) && !findCasingPort(1, -1, -z) && findCasingControllerPort(0, 0, -z + 1) && findCasingPort(1, 0, -z) && findCasingPort(1, 1, -z + 1) && findCasingPort(1, -1, -z + 1)) {
				rz = l - z;
				z0 = -z;
				f = true;
				break;
			}
		}
		if (!f) {
			masterPosition = null;
			return;
		}
		f = false;
		for (int y = 0; y <= l; y++) {
			if (!findCasingPort(x0, -y + 1, z0) && !findCasingPort(x0 + 1, -y, z0) && !findCasingPort(x0, -y, z0 + 1) && findCasingControllerPort(x0 + 1, -y, z0 + 1) && findCasingControllerPort(x0, -y + 1, z0 + 1) && findCasingControllerPort(x0 + 1, -y + 1, z0)) {
				y0 = -y;
				f = true;
				break;
			}
		}
		if (!f) {
			masterPosition = null;
			return;
		}
		f = false;
		for (int z = 0; z <= rz; z++) {
			if (!findCasingPort(x0, y0 + 1, z) && !findCasingPort(x0 + 1, y0, z) && !findCasingPort(x0, y0, z - 1) && findCasingControllerPort(x0 + 1, y0, z - 1) && findCasingControllerPort(x0, y0 + 1, z - 1) && findCasingControllerPort(x0 + 1, y0 + 1, z)) {
				z1 = z;
				f = true;
				break;
			}
		}
		if (!f) {
			masterPosition = null;
			return;
		}
		f = false;
		for (int x = 0; x <= l; x++) {
			if (!findCasingPort(x0 + x, y0 + 1, z0) && !findCasingPort(x0 + x - 1, y0, z0) && !findCasingPort(x0 + x, y0, z0 + 1) && findCasingControllerPort(x0 + x - 1, y0, z0 + 1) && findCasingControllerPort(x0 + x, y0 + 1, z0 + 1) && findCasingControllerPort(x0 + x - 1, y0 + 1, z0)) {
				x1 = x0 + x;
				f = true;
				break;
			}
		}
		if (!f) {
			masterPosition = null;
			return;
		}
		f = false;
		for (int y = 0; y <= l; y++) {
			if (!findCasingPort(x0, y0 + y - 1, z0) && !findCasingPort(x0 + 1, y0 + y, z0) && !findCasingPort(x0, y0 + y, z0 + 1) && findCasingControllerPort(x0 + 1, y0 + y, z0 + 1) && findCasingControllerPort(x0, y0 + y - 1, z0 + 1) && findCasingControllerPort(x0 + 1, y0 + y - 1, z0)) {
				y1 = y0 + y;
				f = true;
				break;
			}
		}
		if (!f) {
			masterPosition = null;
			return;
		}
		f = false;
		if ((x0 > 0 || x1 < 0) || (y0 > 0 || y1 < 0) || (z0 > 0 || z1 < 0) || x1 - x0 < 1 || y1 - y0 < 1 || z1 - z0 < 1) {
			masterPosition = null;
			return;
		}
		for (int y = y0; y <= y1; y++) {
			for (int z = z0; z <= z1; z++) {
				for (int x : new int[] {x0, x1}) {
					if(world.getTileEntity(position(x, y, z)) != null) {
						if(isMaster(position(x, y, z))) {
							masterPosition = position(x, y, z);
							return;
						}
					}
				}
			}
			for (int z : new int[] {z0, z1}) {
				for (int x = x0; x <= x1; x++) {
					if(world.getTileEntity(position(x, y, z)) != null) {
						if(isMaster(position(x, y, z))) {
							masterPosition = position(x, y, z);
							return;
						}
					}
				}
			}
		}
		masterPosition = null;
	}
	
	public boolean isMaster(BlockPos pos) {
		return world.getTileEntity(pos) instanceof TileFissionController;
	}
	
	// Capability
	
	net.minecraftforge.items.IItemHandler handlerTop = new net.minecraftforge.items.wrapper.SidedInvWrapper(this, net.minecraft.util.EnumFacing.UP);
	net.minecraftforge.items.IItemHandler handlerBottom = new net.minecraftforge.items.wrapper.SidedInvWrapper(this, net.minecraft.util.EnumFacing.DOWN);
	net.minecraftforge.items.IItemHandler handlerSide = new net.minecraftforge.items.wrapper.SidedInvWrapper(this, net.minecraft.util.EnumFacing.WEST);
	
	@SuppressWarnings("unchecked")
	public <T> T getCapability(net.minecraftforge.common.capabilities.Capability<T> capability, @javax.annotation.Nullable net.minecraft.util.EnumFacing facing) {
		if (CapabilityEnergy.ENERGY == capability && connection.canConnect()) {
			return (T) getStorage();
		}
		if (connection != null && ModCheck.teslaLoaded && connection.canConnect()) {
			if ((capability == TeslaCapabilities.CAPABILITY_CONSUMER && connection.canReceive()) || (capability == TeslaCapabilities.CAPABILITY_PRODUCER && connection.canExtract()) || capability == TeslaCapabilities.CAPABILITY_HOLDER)
				return (T) getStorage();
		}
		if (facing != null && capability == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			if (facing == EnumFacing.DOWN) {
				return (T) handlerBottom;
			} else if (facing == EnumFacing.UP) {
				return (T) handlerTop;
			} else {
				return (T) handlerSide;
			}
		}
		return null;
	}
}
