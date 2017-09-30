package nc.network;

import io.netty.buffer.ByteBuf;
import nc.tile.fluid.ITileFluid;
import nc.util.NCUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class PacketEmptyTankButton implements IMessage {
	
	boolean messageValid;
	
	BlockPos pos;
	int tankNo;
	
	public PacketEmptyTankButton() {
		messageValid = false;
	}
	
	public PacketEmptyTankButton(ITileFluid machine, int tankNo) {
		pos = machine.getBlockPos();
		this.tankNo = tankNo;
		messageValid = true;
	}
	
	public void fromBytes(ByteBuf buf) {
		try {
			pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
			tankNo = buf.readInt();
		} catch (IndexOutOfBoundsException ioe) {
			NCUtil.getLogger().catching(ioe);
			return;
		}
		messageValid = true;
	}
	
	public void toBytes(ByteBuf buf) {
		if (!messageValid) return;
		buf.writeInt(pos.getX());
		buf.writeInt(pos.getY());
		buf.writeInt(pos.getZ());
		buf.writeInt(tankNo);
	}
	
	public static class Handler implements IMessageHandler<PacketEmptyTankButton, IMessage> {
		
		public IMessage onMessage(PacketEmptyTankButton message, MessageContext ctx) {
			if (!message.messageValid && ctx.side != Side.SERVER) return null;
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> processMessage(message, ctx));
			return null;
		}
		
		void processMessage(PacketEmptyTankButton message, MessageContext ctx) {
			TileEntity tile = ctx.getServerHandler().playerEntity.getServerWorld().getTileEntity(message.pos);
			if (tile == null) return;
			if(tile instanceof ITileFluid) {
				ITileFluid machine = (ITileFluid) tile;
				machine.clearTank(message.tankNo);
				ctx.getServerHandler().playerEntity.getServerWorld().getTileEntity(message.pos).markDirty();
			}
		}
	}
}
