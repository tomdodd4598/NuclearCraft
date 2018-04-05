package nc.network;

import nc.multiblock.fission.moltensalt.network.SaltFissionHeatBufferUpdatePacket;
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
		instance.registerMessage(PacketGetFluidInTank.Handler.class, PacketGetFluidInTank.class, nextID(), Side.SERVER);
		instance.registerMessage(PacketToggleTanksSharedButton.Handler.class, PacketToggleTanksSharedButton.class, nextID(), Side.SERVER);
		instance.registerMessage(PacketEmptyTankButton.Handler.class, PacketEmptyTankButton.class, nextID(), Side.SERVER);
		
		// CLIENT
		instance.registerMessage(PacketReturnFluidInTank.Handler.class, PacketReturnFluidInTank.class, nextID(), Side.CLIENT);
		instance.registerMessage(SaltFissionHeatBufferUpdatePacket.class, SaltFissionHeatBufferUpdatePacket.class, nextID(), Side.CLIENT);
	}
}
