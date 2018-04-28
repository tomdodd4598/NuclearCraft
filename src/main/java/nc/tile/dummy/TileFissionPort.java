package nc.tile.dummy;

import ic2.api.energy.tile.IEnergySink;
import nc.ModCheck;
import nc.config.NCConfig;
import nc.init.NCBlocks;
import nc.tile.energy.ITileEnergy;
import nc.tile.generator.TileFissionController;
import nc.tile.internal.energy.EnergyConnection;
import nc.tile.passive.ITilePassive;
import nc.util.BlockFinder;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class TileFissionPort extends TileDummy implements IInterfaceable {
	
	private BlockFinder finder;

	public TileFissionPort() {
		super("fission_port", energyConnectionAll(EnergyConnection.OUT), NCConfig.machine_update_rate);
	}
	
	@Override
	public void update() {
		super.update();
		if(!world.isRemote) {
			pushEnergy();
			pushFluid();
		}
	}
	
	@Override
	public void onAdded() {
		finder = new BlockFinder(pos, world, getBlockMetadata());
		super.onAdded();
	}
	
	private int getNumberOfPorts() {
		if (getMaster() != null) {
			if (getMaster() instanceof TileFissionController) return ((TileFissionController)getMaster()).ports;
		}
		return 1;
	}
	
	private int getCurrentEnergyStored() {
		if (getMaster() != null) {
			if (getMaster() instanceof TileFissionController) return ((TileFissionController)getMaster()).currentEnergyStored;
		}
		return 1;
	}
	
	// Energy Pushing - Account for multiple ports
	
	@Override
	public void pushEnergyToSide(EnumFacing side) {
		if (getEnergyStorage().getEnergyStored() <= 0 || !getEnergyConnection(side).canExtract()) return;
		
		TileEntity tile = world.getTileEntity(getPos().offset(side));
		
		if (tile instanceof ITileEnergy) if (!((ITileEnergy) tile).getEnergyConnection(side.getOpposite()).canReceive()) return;
		if (tile instanceof ITilePassive) if (!((ITilePassive) tile).canPushEnergyTo()) return;
		
		IEnergyStorage adjStorage = tile == null ? null : tile.getCapability(CapabilityEnergy.ENERGY, side.getOpposite());
		
		if (adjStorage != null && getEnergyStorage().canExtract()) {
			getEnergyStorage().extractEnergy(adjStorage.receiveEnergy(getEnergyStorage().extractEnergy(getCurrentEnergyStored()/getNumberOfPorts(), true), false), false);
		}
		else if (ModCheck.ic2Loaded()) {
			if (tile instanceof IEnergySink) {
				getEnergyStorage().extractEnergy((int) Math.round(((IEnergySink) tile).injectEnergy(side.getOpposite(), getEnergyStorage().extractEnergy(getCurrentEnergyStored()/getNumberOfPorts(), true) / NCConfig.rf_per_eu, getSourceTier())), false);
			}
		}
	}
	
	// Finding Blocks
	
	private boolean findCasing(int x, int y, int z) {
		return finder.find(x, y, z, NCBlocks.fission_block.getStateFromMeta(0), NCBlocks.reactor_casing_transparent, NCBlocks.fission_port, NCBlocks.buffer, NCBlocks.reactor_door, NCBlocks.reactor_trapdoor);
	}
	
	private boolean findController(int x, int y, int z) {
		return finder.find(x, y, z, NCBlocks.fission_controller_idle, NCBlocks.fission_controller_active, NCBlocks.fission_controller_new_idle, NCBlocks.fission_controller_new_active);
	}
	
	private boolean findCasingAll(int x, int y, int z) {
		return findCasing(x, y, z) || findController(x, y, z);
	}
	
	// Find Master
	
	@Override
	public void findMaster() {
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
			if ((findCasing(0, 1, 0) || findCasing(0, -1, 0)) || ((findCasing(1, 1, 0) || findCasing(1, -1, 0)) && findCasing(1, 0, 0)) || ((findCasing(1, 1, 0) && !findCasing(1, -1, 0)) && !findCasing(1, 0, 0)) || ((!findCasing(1, 1, 0) && findCasing(1, -1, 0)) && !findCasing(1, 0, 0))) {
				if (!findCasing(0, 1, -z) && !findCasing(0, -1, -z) && (findCasingAll(0, 0, -z + 1) || findCasingAll(0, 1, -z + 1) || findCasingAll(0, -1, -z + 1))) {
					rz = l - z;
					z0 = -z;
					f = true;
					break;
				}
			} else if (!findCasing(0, 0, -z) && !findCasing(1, 1, -z) && !findCasing(1, -1, -z) && findCasingAll(0, 0, -z + 1) && findCasing(1, 0, -z) && findCasing(1, 1, -z + 1) && findCasing(1, -1, -z + 1)) {
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
			if (!findCasing(x0, -y + 1, z0) && !findCasing(x0 + 1, -y, z0) && !findCasing(x0, -y, z0 + 1) && findCasingAll(x0 + 1, -y, z0 + 1) && findCasingAll(x0, -y + 1, z0 + 1) && findCasingAll(x0 + 1, -y + 1, z0)) {
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
			if (!findCasing(x0, y0 + 1, z) && !findCasing(x0 + 1, y0, z) && !findCasing(x0, y0, z - 1) && findCasingAll(x0 + 1, y0, z - 1) && findCasingAll(x0, y0 + 1, z - 1) && findCasingAll(x0 + 1, y0 + 1, z)) {
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
			if (!findCasing(x0 + x, y0 + 1, z0) && !findCasing(x0 + x - 1, y0, z0) && !findCasing(x0 + x, y0, z0 + 1) && findCasingAll(x0 + x - 1, y0, z0 + 1) && findCasingAll(x0 + x, y0 + 1, z0 + 1) && findCasingAll(x0 + x - 1, y0 + 1, z0)) {
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
			if (!findCasing(x0, y0 + y - 1, z0) && !findCasing(x0 + 1, y0 + y, z0) && !findCasing(x0, y0 + y, z0 + 1) && findCasingAll(x0 + 1, y0 + y, z0 + 1) && findCasingAll(x0, y0 + y - 1, z0 + 1) && findCasingAll(x0 + 1, y0 + y - 1, z0)) {
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
					if(world.getTileEntity(finder.position(x, y, z)) != null) {
						if(isMaster(finder.position(x, y, z))) {
							masterPosition = finder.position(x, y, z);
							return;
						}
					}
				}
			}
			for (int z : new int[] {z0, z1}) {
				for (int x = x0; x <= x1; x++) {
					if(world.getTileEntity(finder.position(x, y, z)) != null) {
						if(isMaster(finder.position(x, y, z))) {
							masterPosition = finder.position(x, y, z);
							return;
						}
					}
				}
			}
		}
		masterPosition = null;
	}
	
	@Override
	public boolean isMaster(BlockPos pos) {
		return world.getTileEntity(pos) instanceof TileFissionController;
	}
}
