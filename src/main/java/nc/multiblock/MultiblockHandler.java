package nc.multiblock;

import nc.multiblock.battery.BatteryMultiblock;
import nc.multiblock.battery.tile.TileBattery;
import nc.multiblock.fission.FissionReactor;
import nc.multiblock.fission.salt.tile.TileSaltFissionHeater;
import nc.multiblock.fission.salt.tile.TileSaltFissionVessel;
import nc.multiblock.fission.solid.tile.TileSolidFissionCell;
import nc.multiblock.fission.solid.tile.TileSolidFissionSink;
import nc.multiblock.fission.tile.IFissionComponent;
import nc.multiblock.fission.tile.IFissionController;
import nc.multiblock.fission.tile.IFissionSpecialComponent;
import nc.multiblock.fission.tile.TileFissionConductor;
import nc.multiblock.fission.tile.TileFissionIrradiator;
import nc.multiblock.fission.tile.TileFissionMonitor;
import nc.multiblock.fission.tile.TileFissionShield;
import nc.multiblock.fission.tile.TileFissionSource;
import nc.multiblock.fission.tile.TileFissionVent;
import nc.multiblock.fission.tile.manager.TileFissionShieldManager;
import nc.multiblock.fission.tile.port.TileFissionCellPort;
import nc.multiblock.fission.tile.port.TileFissionHeaterPort;
import nc.multiblock.fission.tile.port.TileFissionIrradiatorPort;
import nc.multiblock.fission.tile.port.TileFissionVesselPort;
import nc.multiblock.heatExchanger.HeatExchanger;
import nc.multiblock.heatExchanger.tile.IHeatExchangerController;
import nc.multiblock.heatExchanger.tile.TileCondenserTube;
import nc.multiblock.heatExchanger.tile.TileHeatExchangerTube;
import nc.multiblock.heatExchanger.tile.TileHeatExchangerVent;
import nc.multiblock.qComputer.QuantumComputer;
import nc.multiblock.qComputer.tile.TileQuantumComputerController;
import nc.multiblock.qComputer.tile.TileQuantumComputerQubit;
import nc.multiblock.rtg.RTGMultiblock;
import nc.multiblock.rtg.tile.TileRTG;
import nc.multiblock.turbine.Turbine;
import nc.multiblock.turbine.tile.ITurbineController;
import nc.multiblock.turbine.tile.TileTurbineDynamoPart;
import nc.multiblock.turbine.tile.TileTurbineInlet;
import nc.multiblock.turbine.tile.TileTurbineOutlet;
import nc.multiblock.turbine.tile.TileTurbineRotorBearing;
import nc.multiblock.turbine.tile.TileTurbineRotorBlade;
import nc.multiblock.turbine.tile.TileTurbineRotorShaft;
import nc.multiblock.turbine.tile.TileTurbineRotorStator;
import net.minecraft.client.Minecraft;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SuppressWarnings("static-method")
public final class MultiblockHandler {
	
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public void onChunkLoad(final ChunkEvent.Load loadEvent) {
		Chunk chunk = loadEvent.getChunk();
		MultiblockRegistry.INSTANCE.onChunkLoaded(loadEvent.getWorld(), chunk.x, chunk.z);
	}
	
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public void onWorldUnload(final WorldEvent.Unload unloadWorldEvent) {
		MultiblockRegistry.INSTANCE.onWorldUnloaded(unloadWorldEvent.getWorld());
	}
	
	@SubscribeEvent
	public void onWorldTick(final TickEvent.WorldTickEvent event) {
		if (TickEvent.Phase.START == event.phase) {
			MultiblockRegistry.INSTANCE.tickStart(event.world);
		}
	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onClientTick(final TickEvent.ClientTickEvent event) {
		if (TickEvent.Phase.START == event.phase) {
			MultiblockRegistry.INSTANCE.tickStart(Minecraft.getMinecraft().world);
		}
	}
	
	public static void init() {
		RTGMultiblock.PART_CLASSES.add(TileRTG.class);
		
		BatteryMultiblock.PART_CLASSES.add(TileBattery.class);
		
		FissionReactor.PART_CLASSES.add(IFissionController.class);
		FissionReactor.PART_CLASSES.add(IFissionComponent.class);
		FissionReactor.PART_CLASSES.add(IFissionSpecialComponent.class);
		FissionReactor.PART_CLASSES.add(TileFissionConductor.class);
		FissionReactor.PART_CLASSES.add(TileFissionMonitor.class);
		FissionReactor.PART_CLASSES.add(TileFissionVent.class);
		FissionReactor.PART_CLASSES.add(TileFissionIrradiatorPort.class);
		FissionReactor.PART_CLASSES.add(TileFissionCellPort.class);
		FissionReactor.PART_CLASSES.add(TileFissionVesselPort.class);
		FissionReactor.PART_CLASSES.add(TileFissionHeaterPort.class);
		FissionReactor.PART_CLASSES.add(TileFissionShieldManager.class);
		FissionReactor.PART_CLASSES.add(TileFissionIrradiator.class);
		FissionReactor.PART_CLASSES.add(TileFissionSource.class);
		FissionReactor.PART_CLASSES.add(TileFissionShield.class);
		FissionReactor.PART_CLASSES.add(TileSolidFissionCell.class);
		FissionReactor.PART_CLASSES.add(TileSolidFissionSink.class);
		FissionReactor.PART_CLASSES.add(TileSaltFissionVessel.class);
		FissionReactor.PART_CLASSES.add(TileSaltFissionHeater.class);
		
		HeatExchanger.PART_CLASSES.add(IHeatExchangerController.class);
		HeatExchanger.PART_CLASSES.add(TileHeatExchangerVent.class);
		HeatExchanger.PART_CLASSES.add(TileHeatExchangerTube.class);
		HeatExchanger.PART_CLASSES.add(TileCondenserTube.class);
		
		Turbine.PART_CLASSES.add(ITurbineController.class);
		Turbine.PART_CLASSES.add(TileTurbineDynamoPart.class);
		Turbine.PART_CLASSES.add(TileTurbineRotorShaft.class);
		Turbine.PART_CLASSES.add(TileTurbineRotorBlade.class);
		Turbine.PART_CLASSES.add(TileTurbineRotorStator.class);
		Turbine.PART_CLASSES.add(TileTurbineRotorBearing.class);
		Turbine.PART_CLASSES.add(TileTurbineInlet.class);
		Turbine.PART_CLASSES.add(TileTurbineOutlet.class);
		
		QuantumComputer.PART_CLASSES.add(TileQuantumComputerController.class);
		QuantumComputer.PART_CLASSES.add(TileQuantumComputerQubit.class);
	}
}
