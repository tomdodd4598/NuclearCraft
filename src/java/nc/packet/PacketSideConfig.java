package nc.packet;

import io.netty.buffer.ByteBuf;
import nc.NuclearCraft;
import net.minecraft.tileentity.TileEntity;
import cofh.api.tileentity.IReconfigurableSides;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class PacketSideConfig implements IMessage {
	
	private int x;
	private int y;
	private int z;

	public int side, value;

	public PacketSideConfig() {}

	public PacketSideConfig(int xc, int yc, int zc, int side, int value) {
		x = xc;
		y = yc;
		z = zc;
		this.side = side;
		this.value = value;
	}

	public void fromBytes(ByteBuf buf) {
		x = buf.readInt();
		y = buf.readInt();
		z = buf.readInt();
		side = buf.readInt();
		value = buf.readInt();
	}

	public void toBytes(ByteBuf buf) {
		buf.writeInt(x);
		buf.writeInt(y);
		buf.writeInt(z);
		buf.writeInt(side);
		buf.writeInt(value);
	}
	
	public static class Handler implements IMessageHandler<PacketSideConfig, IMessage> {
		public IMessage onMessage(PacketSideConfig message, MessageContext ctx) {
			TileEntity te = NuclearCraft.NCProxy.getPlayerEntity(ctx).worldObj.getTileEntity(message.x, message.y, message.z);
			if (te.getWorldObj().isRemote && te instanceof IReconfigurableSides) {
				IReconfigurableSides sides = (IReconfigurableSides) te;
				sides.setSide(message.side, message.value);
			}
			return null;
		}
	}
}