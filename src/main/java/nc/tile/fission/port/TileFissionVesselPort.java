package nc.tile.fission.port;

import static nc.util.FluidStackHelper.INGOT_BLOCK_VOLUME;
import static nc.util.PosHelper.DEFAULT_NON;

import java.util.Set;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import nc.container.ContainerFunction;
import nc.gui.*;
import nc.handler.TileInfoHandler;
import nc.network.tile.multiblock.port.FluidPortUpdatePacket;
import nc.recipe.NCRecipes;
import nc.tile.*;
import nc.tile.fission.TileSaltFissionVessel;
import nc.tile.fission.port.TileFissionVesselPort.FissionVesselPortContainerInfo;
import nc.tile.internal.fluid.Tank.TankInfo;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class TileFissionVesselPort extends TileFissionFluidPort<TileFissionVesselPort, TileSaltFissionVessel> implements ITileGui<TileFissionVesselPort, FluidPortUpdatePacket, FissionVesselPortContainerInfo> {
	
	public static class FissionVesselPortContainerInfo extends TileContainerInfo<TileFissionVesselPort> {
		
		public FissionVesselPortContainerInfo(String modId, String name, Class<? extends Container> containerClass, ContainerFunction<TileFissionVesselPort> containerFunction, Class<? extends GuiContainer> guiClass, GuiInfoTileFunction<TileFissionVesselPort> guiFunction) {
			super(modId, name, containerClass, containerFunction, guiClass, GuiFunction.of(modId, name, containerFunction, guiFunction));
		}
	}
	
	protected final FissionVesselPortContainerInfo info = TileInfoHandler.getTileContainerInfo("fission_vessel_port");
	
	protected final Set<EntityPlayer> updatePacketListeners = new ObjectOpenHashSet<>();
	
	public TileFissionVesselPort() {
		super(TileFissionVesselPort.class, INGOT_BLOCK_VOLUME, NCRecipes.salt_fission.validFluids.get(0), NCRecipes.salt_fission);
	}
	
	@Override
	public FissionVesselPortContainerInfo getContainerInfo() {
		return info;
	}
	
	@Override
	public int getTankCapacityPerConnection() {
		return 36;
	}
	
	@Override
	public Object getFilterKey() {
		return getFilterTanks().get(0).getFluidName();
	}
	
	// Ticking
	
	@Override
	public void update() {
		super.update();
		if (!world.isRemote) {
			sendTileUpdatePacketToListeners();
		}
	}
	
	// ITileGui
	
	@Override
	public Set<EntityPlayer> getTileUpdatePacketListeners() {
		return updatePacketListeners;
	}
	
	@Override
	public FluidPortUpdatePacket getTileUpdatePacket() {
		return new FluidPortUpdatePacket(pos, masterPortPos, getTanks(), getFilterTanks());
	}
	
	@Override
	public void onTileUpdatePacket(FluidPortUpdatePacket message) {
		masterPortPos = message.masterPortPos;
		if (DEFAULT_NON.equals(masterPortPos) ^ masterPort == null) {
			refreshMasterPort();
		}
		TankInfo.readInfoList(message.tankInfos, getTanks());
		TankInfo.readInfoList(message.filterTankInfos, getFilterTanks());
	}
}
