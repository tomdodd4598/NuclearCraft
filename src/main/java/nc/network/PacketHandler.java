package nc.network;

import nc.multiblock.saltFission.network.SaltFissionUpdatePacket;
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
		instance.registerMessage(SaltFissionUpdatePacket.Handler.class, SaltFissionUpdatePacket.class, nextID(), Side.CLIENT);
		instance.registerMessage(FusionUpdatePacket.Handler.class, FusionUpdatePacket.class, nextID(), Side.CLIENT);
	}
}
