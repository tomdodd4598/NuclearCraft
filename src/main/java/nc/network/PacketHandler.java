package nc.network;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class PacketHandler {
	
	private static int packetId = 0;

	public static SimpleNetworkWrapper INSTANCE = null;

	public PacketHandler() {}

	public static int nextID() {
		return packetId++;
	}

	public static void registerMessages(String channelName) {
		INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(channelName);
		registerMessages();
	}

	public static void registerMessages() {
		// SERVER
		INSTANCE.registerMessage(PacketGetFluidInTank.Handler.class, PacketGetFluidInTank.class, nextID(), Side.SERVER);
		INSTANCE.registerMessage(PacketToggleTanksSharedButton.Handler.class, PacketToggleTanksSharedButton.class, nextID(), Side.SERVER);
		INSTANCE.registerMessage(PacketEmptyTankButton.Handler.class, PacketEmptyTankButton.class, nextID(), Side.SERVER);
		
		// CLIENT
		INSTANCE.registerMessage(PacketReturnFluidInTank.Handler.class, PacketReturnFluidInTank.class, nextID(), Side.CLIENT);
	}
}
