package nc.network;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

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
		//INSTANCE.registerMessage(PacketClass.Handler.class, PacketClass.class, nextID(), Side.SERVER);
	}
}
