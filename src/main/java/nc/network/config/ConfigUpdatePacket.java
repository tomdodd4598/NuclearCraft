package nc.network.config;

import io.netty.buffer.ByteBuf;
import nc.config.NCConfig;
import nc.network.NCPacket;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.*;
import net.minecraftforge.fml.relauncher.Side;

public class ConfigUpdatePacket extends NCPacket {
	
	public boolean radiation_enabled, radiation_horse_armor;
	
	public ConfigUpdatePacket() {
		super();
	}
	
	public ConfigUpdatePacket(boolean radiation_enabled, boolean radiation_horse_armor) {
		super();
		this.radiation_enabled = radiation_enabled;
		this.radiation_horse_armor = radiation_horse_armor;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		super.fromBytes(buf);
		radiation_enabled = buf.readBoolean();
		radiation_horse_armor = buf.readBoolean();
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		super.toBytes(buf);
		buf.writeBoolean(radiation_enabled);
		buf.writeBoolean(radiation_horse_armor);
	}
	
	public static class Handler implements IMessageHandler<ConfigUpdatePacket, IMessage> {
		
		@Override
		public IMessage onMessage(ConfigUpdatePacket message, MessageContext ctx) {
			if (ctx.side == Side.CLIENT) {
				Minecraft.getMinecraft().addScheduledTask(() -> NCConfig.onConfigPacket(message));
			}
			return null;
		}
	}
}
