package nc.init;

import nc.network.config.ConfigUpdatePacket;
import nc.network.gui.*;
import nc.network.multiblock.*;
import nc.network.radiation.PlayerRadsUpdatePacket;
import nc.network.render.BlockHighlightUpdatePacket;
import nc.network.tile.multiblock.*;
import nc.network.tile.multiblock.port.*;
import nc.network.tile.processor.EnergyProcessorUpdatePacket;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class NCPackets {
	
	public static SimpleNetworkWrapper wrapper = null;
	
	public static void registerMessages(String channelName) {
		wrapper = NetworkRegistry.INSTANCE.newSimpleChannel(channelName);
		registerMessages();
	}
	
	public static void registerMessages() {
		// CLIENT -> SERVER
		
		wrapper.registerMessage(ClearTankPacket.Handler.class, ClearTankPacket.class, nextID(), Side.SERVER);
		wrapper.registerMessage(ClearFilterTankPacket.Handler.class, ClearFilterTankPacket.class, nextID(), Side.SERVER);
		
		wrapper.registerMessage(ToggleInputTanksSeparatedPacket.Handler.class, ToggleInputTanksSeparatedPacket.class, nextID(), Side.SERVER);
		wrapper.registerMessage(ToggleVoidUnusableFluidInputPacket.Handler.class, ToggleVoidUnusableFluidInputPacket.class, nextID(), Side.SERVER);
		wrapper.registerMessage(ToggleVoidExcessFluidOutputPacket.Handler.class, ToggleVoidExcessFluidOutputPacket.class, nextID(), Side.SERVER);
		wrapper.registerMessage(ToggleAlternateComparatorPacket.Handler.class, ToggleAlternateComparatorPacket.class, nextID(), Side.SERVER);
		wrapper.registerMessage(ToggleRedstoneControlPacket.Handler.class, ToggleRedstoneControlPacket.class, nextID(), Side.SERVER);
		
		wrapper.registerMessage(OpenGuiPacket.Handler.class, OpenGuiPacket.class, nextID(), Side.SERVER);
		wrapper.registerMessage(OpenTileGuiPacket.Handler.class, OpenTileGuiPacket.class, nextID(), Side.SERVER);
		wrapper.registerMessage(OpenSideConfigGuiPacket.Handler.class, OpenSideConfigGuiPacket.class, nextID(), Side.SERVER);
		wrapper.registerMessage(ToggleItemSorptionPacket.Handler.class, ToggleItemSorptionPacket.class, nextID(), Side.SERVER);
		wrapper.registerMessage(ResetItemSorptionsPacket.Handler.class, ResetItemSorptionsPacket.class, nextID(), Side.SERVER);
		wrapper.registerMessage(ToggleItemOutputSettingPacket.Handler.class, ToggleItemOutputSettingPacket.class, nextID(), Side.SERVER);
		wrapper.registerMessage(ToggleTankSorptionPacket.Handler.class, ToggleTankSorptionPacket.class, nextID(), Side.SERVER);
		wrapper.registerMessage(ResetTankSorptionsPacket.Handler.class, ResetTankSorptionsPacket.class, nextID(), Side.SERVER);
		wrapper.registerMessage(ToggleTankOutputSettingPacket.Handler.class, ToggleTankOutputSettingPacket.class, nextID(), Side.SERVER);
		
		wrapper.registerMessage(ClearAllMaterialPacket.Handler.class, ClearAllMaterialPacket.class, nextID(), Side.SERVER);
		
		// SERVER -> CLIENT
		
		wrapper.registerMessage(ConfigUpdatePacket.Handler.class, ConfigUpdatePacket.class, nextID(), Side.CLIENT);
		
		wrapper.registerMessage(BlockHighlightUpdatePacket.Handler.class, BlockHighlightUpdatePacket.class, nextID(), Side.CLIENT);
		
		wrapper.registerMessage(EnergyProcessorUpdatePacket.Handler.class, EnergyProcessorUpdatePacket.class, nextID(), Side.CLIENT);
		
		wrapper.registerMessage(ElectrolyzerUpdatePacket.Handler.class, ElectrolyzerUpdatePacket.class, nextID(), Side.CLIENT);
		wrapper.registerMessage(ElectrolyzerRenderPacket.Handler.class, ElectrolyzerRenderPacket.class, nextID(), Side.CLIENT);
		wrapper.registerMessage(DistillerUpdatePacket.Handler.class, DistillerUpdatePacket.class, nextID(), Side.CLIENT);
		wrapper.registerMessage(DistillerRenderPacket.Handler.class, DistillerRenderPacket.class, nextID(), Side.CLIENT);
		wrapper.registerMessage(InfiltratorUpdatePacket.Handler.class, InfiltratorUpdatePacket.class, nextID(), Side.CLIENT);
		wrapper.registerMessage(InfiltratorRenderPacket.Handler.class, InfiltratorRenderPacket.class, nextID(), Side.CLIENT);
		
		wrapper.registerMessage(ItemPortUpdatePacket.Handler.class, ItemPortUpdatePacket.class, nextID(), Side.CLIENT);
		wrapper.registerMessage(FluidPortUpdatePacket.Handler.class, FluidPortUpdatePacket.class, nextID(), Side.CLIENT);
		
		wrapper.registerMessage(FissionIrradiatorUpdatePacket.Handler.class, FissionIrradiatorUpdatePacket.class, nextID(), Side.CLIENT);
		wrapper.registerMessage(SolidFissionUpdatePacket.Handler.class, SolidFissionUpdatePacket.class, nextID(), Side.CLIENT);
		wrapper.registerMessage(SolidFissionCellUpdatePacket.Handler.class, SolidFissionCellUpdatePacket.class, nextID(), Side.CLIENT);
		wrapper.registerMessage(SaltFissionUpdatePacket.Handler.class, SaltFissionUpdatePacket.class, nextID(), Side.CLIENT);
		wrapper.registerMessage(SaltFissionVesselUpdatePacket.Handler.class, SaltFissionVesselUpdatePacket.class, nextID(), Side.CLIENT);
		wrapper.registerMessage(SaltFissionHeaterUpdatePacket.Handler.class, SaltFissionHeaterUpdatePacket.class, nextID(), Side.CLIENT);
		
		wrapper.registerMessage(HeatExchangerUpdatePacket.Handler.class, HeatExchangerUpdatePacket.class, nextID(), Side.CLIENT);
		wrapper.registerMessage(HeatExchangerRenderPacket.Handler.class, HeatExchangerRenderPacket.class, nextID(), Side.CLIENT);
		wrapper.registerMessage(CondenserUpdatePacket.Handler.class, CondenserUpdatePacket.class, nextID(), Side.CLIENT);
		wrapper.registerMessage(CondenserRenderPacket.Handler.class, CondenserRenderPacket.class, nextID(), Side.CLIENT);
		
		wrapper.registerMessage(TurbineUpdatePacket.Handler.class, TurbineUpdatePacket.class, nextID(), Side.CLIENT);
		wrapper.registerMessage(TurbineRenderPacket.Handler.class, TurbineRenderPacket.class, nextID(), Side.CLIENT);
		
		wrapper.registerMessage(QuantumComputerQubitRenderPacket.Handler.class, QuantumComputerQubitRenderPacket.class, nextID(), Side.CLIENT);
		
		wrapper.registerMessage(PlayerRadsUpdatePacket.Handler.class, PlayerRadsUpdatePacket.class, nextID(), Side.CLIENT);
	}
	
	private static int packetId = 0;
	
	public static int nextID() {
		return packetId++;
	}
}
