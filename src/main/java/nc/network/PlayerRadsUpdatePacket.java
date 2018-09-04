package nc.network;

import io.netty.buffer.ByteBuf;
import nc.capability.radiation.IEntityRads;
import nc.util.NCUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class PlayerRadsUpdatePacket implements IMessage {
	
	boolean messageValid;
	
	protected double totalRads;
	protected double radiationLevel;
	protected double radiationResistance;
	protected boolean radXWoreOff;
	protected double radawayBuffer;
	protected boolean consumed;
	
	public PlayerRadsUpdatePacket() {
		messageValid = false;
	}
	
	public PlayerRadsUpdatePacket(IEntityRads playerRads) {
		this.totalRads = playerRads.getTotalRads();
		this.radiationLevel = playerRads.getRadiationLevel();
		this.radiationResistance = playerRads.getRadiationResistance();
		this.radXWoreOff = playerRads.getRadXWoreOff();
		this.radawayBuffer = playerRads.getRadawayBuffer();
		this.consumed = playerRads.getConsumedMedicine();
		
		messageValid = true;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		try {
			totalRads = buf.readDouble();
			radiationLevel = buf.readDouble();
			radiationResistance = buf.readDouble();
			radXWoreOff = buf.readBoolean();
			radawayBuffer = buf.readDouble();
			consumed = buf.readBoolean();
		} catch (IndexOutOfBoundsException ioe) {
			NCUtil.getLogger().catching(ioe);
			return;
		}
		messageValid = true;
	}

	@Override
	public void toBytes(ByteBuf buf) {
		if (!messageValid) return;
		
		buf.writeDouble(totalRads);
		buf.writeDouble(radiationLevel);
		buf.writeDouble(radiationResistance);
		buf.writeBoolean(radXWoreOff);
		buf.writeDouble(radawayBuffer);
		buf.writeBoolean(consumed);
	}
	
	public static class Handler implements IMessageHandler<PlayerRadsUpdatePacket, IMessage> {

		@Override
		public IMessage onMessage(PlayerRadsUpdatePacket message, MessageContext ctx) {
			if (!message.messageValid && ctx.side != Side.CLIENT) return null;
			Minecraft.getMinecraft().addScheduledTask(() -> processMessage(message));
			return null;
		}
		
		void processMessage(PlayerRadsUpdatePacket message) {
			EntityPlayerSP player = Minecraft.getMinecraft().player;
			if (!player.hasCapability(IEntityRads.CAPABILITY_ENTITY_RADS, null)) return;
			IEntityRads playerRads = player.getCapability(IEntityRads.CAPABILITY_ENTITY_RADS, null);
			playerRads.setTotalRads(message.totalRads);
			playerRads.setRadiationLevel(message.radiationLevel);
			playerRads.setRadiationResistance(message.radiationResistance);
			playerRads.setRadXWoreOff(message.radXWoreOff);
			playerRads.setRadawayBuffer(message.radawayBuffer);
			playerRads.setConsumedMedicine(message.consumed);
		}
	}
}
