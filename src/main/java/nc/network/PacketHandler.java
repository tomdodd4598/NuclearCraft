package nc.network;

import nc.multiblock.network.CondenserUpdatePacket;
import nc.multiblock.network.HeatExchangerUpdatePacket;
import nc.multiblock.network.SaltFissionUpdatePacket;
import nc.multiblock.network.TurbineUpdatePacket;
import nc.network.gui.EmptyTankButtonPacket;
import nc.network.gui.GetFluidInTankPacket;
import nc.network.gui.ReturnFluidInTankPacket;
import nc.network.gui.ToggleAlternateComparatorButtonPacket;
import nc.network.gui.ToggleTanksEmptyUnusableButtonPacket;
import nc.network.gui.ToggleTanksSharedButtonPacket;
import nc.network.gui.ToggleVoidExcessOutputsButtonPacket;
import nc.network.radiation.PlayerRadsUpdatePacket;
import nc.network.tile.FissionUpdatePacket;
import nc.network.tile.FusionUpdatePacket;
import nc.network.tile.ProcessorUpdatePacket;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class PacketHandler {
	
	private static int packetId = 0;

	public static SimpleNetworkWrapper instance = null;

	public PacketHandler() {}

	public static int nextID() {
		return packetId++;
	}

	public static void registerMessages(String channelName) {
		instance = NetworkRegistry.INSTANCE.newSimpleChannel(channelName);
		registerMessages();
	}

	public static void registerMessages() {
		// SERVER
		instance.registerMessage(GetFluidInTankPacket.Handler.class, GetFluidInTankPacket.class, nextID(), Side.SERVER);
		instance.registerMessage(ToggleTanksSharedButtonPacket.Handler.class, ToggleTanksSharedButtonPacket.class, nextID(), Side.SERVER);
		instance.registerMessage(EmptyTankButtonPacket.Handler.class, EmptyTankButtonPacket.class, nextID(), Side.SERVER);
		instance.registerMessage(ToggleTanksEmptyUnusableButtonPacket.Handler.class, ToggleTanksEmptyUnusableButtonPacket.class, nextID(), Side.SERVER);
		instance.registerMessage(ToggleVoidExcessOutputsButtonPacket.Handler.class, ToggleVoidExcessOutputsButtonPacket.class, nextID(), Side.SERVER);
		instance.registerMessage(ToggleAlternateComparatorButtonPacket.Handler.class, ToggleAlternateComparatorButtonPacket.class, nextID(), Side.SERVER);
		
		// CLIENT
		instance.registerMessage(ReturnFluidInTankPacket.Handler.class, ReturnFluidInTankPacket.class, nextID(), Side.CLIENT);
		instance.registerMessage(ProcessorUpdatePacket.Handler.class, ProcessorUpdatePacket.class, nextID(), Side.CLIENT);
		instance.registerMessage(FissionUpdatePacket.Handler.class, FissionUpdatePacket.class, nextID(), Side.CLIENT);
		instance.registerMessage(FusionUpdatePacket.Handler.class, FusionUpdatePacket.class, nextID(), Side.CLIENT);
		instance.registerMessage(SaltFissionUpdatePacket.Handler.class, SaltFissionUpdatePacket.class, nextID(), Side.CLIENT);
		instance.registerMessage(HeatExchangerUpdatePacket.Handler.class, HeatExchangerUpdatePacket.class, nextID(), Side.CLIENT);
		instance.registerMessage(TurbineUpdatePacket.Handler.class, TurbineUpdatePacket.class, nextID(), Side.CLIENT);
		instance.registerMessage(CondenserUpdatePacket.Handler.class, CondenserUpdatePacket.class, nextID(), Side.CLIENT);
		instance.registerMessage(PlayerRadsUpdatePacket.Handler.class, PlayerRadsUpdatePacket.class, nextID(), Side.CLIENT);
	}
}
