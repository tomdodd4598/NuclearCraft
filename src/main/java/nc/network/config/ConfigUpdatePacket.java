package nc.network.config;

import static nc.config.NCConfig.onConfigPacket;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.*;
import net.minecraftforge.fml.relauncher.Side;

public class ConfigUpdatePacket implements IMessage {
	
	public boolean radiation_enabled, radiation_horse_armor;
	
	public ConfigUpdatePacket() {
		
	}
	
	public ConfigUpdatePacket(boolean radiation_enabled, boolean radiation_horse_armor) {
		this.radiation_enabled = radiation_enabled;
		this.radiation_horse_armor = radiation_horse_armor;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		radiation_enabled = buf.readBoolean();
		radiation_horse_armor = buf.readBoolean();
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeBoolean(radiation_enabled);
		buf.writeBoolean(radiation_horse_armor);
	}
	
	public static class Handler implements IMessageHandler<ConfigUpdatePacket, IMessage> {
		
		@Override
		public IMessage onMessage(ConfigUpdatePacket message, MessageContext ctx) {
			if (ctx.side == Side.CLIENT) {
				Minecraft.getMinecraft().addScheduledTask(() -> processMessage(message));
			}
			return null;
		}
		
		protected void processMessage(ConfigUpdatePacket message) {
			onConfigPacket(message);
		}
	}
}
