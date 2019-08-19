package nc.tile.dummy;

import li.cil.oc.api.Network;
import li.cil.oc.api.network.Environment;
import li.cil.oc.api.network.Message;
import li.cil.oc.api.network.Node;
import li.cil.oc.api.network.Visibility;
import nc.ModCheck;
import nc.config.NCConfig;
import nc.init.NCBlocks;
import nc.recipe.NCRecipes;
import nc.tile.energyFluid.IBufferable;
import nc.tile.fluid.ITileFluid;
import nc.tile.generator.TileFusionCore;
import nc.tile.internal.fluid.TankSorption;
import nc.util.BlockFinder;
import nc.util.BlockPosHelper;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.Optional;

@Optional.Interface(iface = "li.cil.oc.api.network.Environment", modid = "opencomputers")
public abstract class TileFusionDummy extends TileDummy<TileFusionCore> implements IBufferable, Environment {
	
	// Type can't be specified here as OC may not be loaded
	Object oc_node;
	
	public static class Side extends TileFusionDummy {
		public Side() {
			super("fusion_dummy_side");
		}
		
		@Override
		public void findMaster() {
			BlockPosHelper helper = new BlockPosHelper(pos);
			for (BlockPos pos : helper.cuboid(-1, -1, -1, 1, 0, 1)) if (findCore(pos)) {
				masterPosition = pos;
				return;
			}
			masterPosition = null;
		}
	}
	
	public static class Top extends TileFusionDummy {
		public Top() {
			super("fusion_dummy_top");
		}
		
		@Override
		public void findMaster() {
			BlockPosHelper helper = new BlockPosHelper(pos);
			for (BlockPos pos : helper.cuboid(-1, -2, -1, 1, -2, 1)) if (findCore(pos)) {
				masterPosition = pos;
				return;
			}
			masterPosition = null;
		}
	}
	
	private BlockFinder finder;
	
	public TileFusionDummy(String name) {
		super(TileFusionCore.class, name, NCConfig.machine_update_rate, NCRecipes.fusion_valid_fluids.get(0), ITileFluid.fluidConnectionAll(TankSorption.BOTH));
	}
	
	@Override
	public void onAdded() {
		finder = new BlockFinder(pos, world);
		super.onAdded();
	}
	
	@Override
	public void update() {
		super.update();
		if(!world.isRemote) {
			if (ModCheck.openComputersLoaded()) {
				refreshNode();
			}
			pushEnergy();
			if (checkCount == 0 && findAdjacentComparator()) {
				markDirty();
			}
		}
	}
	
	@Override
	public void onChunkUnload() {
		super.onChunkUnload();
		if (ModCheck.openComputersLoaded()) removeNode();
	}
	
	@Override
	public void invalidate() {
		super.invalidate();
		if (ModCheck.openComputersLoaded()) removeNode();
	}
	
	// Redstone Flux

	@Override
	public boolean canReceiveEnergy(EnumFacing side) {
		if (hasMaster()) return !getMaster().isHotEnough();
		return false;
	}
	
	@Override
	public boolean canExtractEnergy(EnumFacing side) {
		if (hasMaster()) return getMaster().isHotEnough();
		return false;
	}
	
	// Finding Blocks
	
	protected boolean findCore(BlockPos pos) {
		return finder.find(pos, NCBlocks.fusion_core);
	}
	
	public boolean findAdjacentComparator() {
		return finder.horizontalYCount(pos, 1, Blocks.UNPOWERED_COMPARATOR, Blocks.POWERED_COMPARATOR) > 0;
	}
	
	// NBT
	
	@Override
	public NBTTagCompound writeAll(NBTTagCompound nbt) {
		super.writeAll(nbt);
		if (hasMaster()) {
			nbt.setDouble("processPower", getMaster().processPower);
			nbt.setInteger("size", getMaster().size);
		}
		return nbt;
	}
	
	// OpenComputers
	
	@Optional.Method(modid = "opencomputers")
	public void removeNode() {
		if (oc_node instanceof Node) ((Node)oc_node).remove();
	}
	
	@Optional.Method(modid = "opencomputers")
	public void refreshNode() {
		if (node() == null) oc_node = Network.newNode(this, Visibility.None).create();
		if (node() != null && node().network() == null) Network.joinOrCreateNetwork(this);
	}
	
	@Override
	@Optional.Method(modid = "opencomputers")
	public Node node() {
		return (Node)oc_node;
	}
	
	@Override
	@Optional.Method(modid = "opencomputers")
	public void onConnect(Node node) {
		
	}
	
	@Override
	@Optional.Method(modid = "opencomputers")
	public void onDisconnect(Node node) {
		
	}
	
	@Override
	@Optional.Method(modid = "opencomputers")
	public void onMessage(Message message) {
		
	}
}
