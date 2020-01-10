package nc.network.gui;

import io.netty.buffer.ByteBuf;
import nc.tile.internal.inventory.ItemSorption;
import nc.tile.inventory.ITileInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class ResetItemSorptionsPacket implements IMessage {
	
	boolean messageValid;
	
	BlockPos pos;
	int slot;
	boolean defaults;
	
	public ResetItemSorptionsPacket() {
		messageValid = false;
	}
	
	public ResetItemSorptionsPacket(ITileInventory machine, int slot, boolean defaults) {
		pos = machine.getTilePos();
		this.slot = slot;
		this.defaults = defaults;
		messageValid = true;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		try {
			pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
			slot = buf.readInt();
			defaults = buf.readBoolean();
		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
			return;
		}
		messageValid = true;
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		if (!messageValid) return;
		buf.writeInt(pos.getX());
		buf.writeInt(pos.getY());
		buf.writeInt(pos.getZ());
		buf.writeInt(slot);
		buf.writeBoolean(defaults);
	}
	
	public static class Handler implements IMessageHandler<ResetItemSorptionsPacket, IMessage> {
		
		@Override
		public IMessage onMessage(ResetItemSorptionsPacket message, MessageContext ctx) {
			if (!message.messageValid && ctx.side != Side.SERVER) return null;
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> processMessage(message, ctx));
			return null;
		}
		
		void processMessage(ResetItemSorptionsPacket message, MessageContext ctx) {
			TileEntity tile = ctx.getServerHandler().player.getServerWorld().getTileEntity(message.pos);
			if (tile instanceof ITileInventory) {
				ITileInventory machine = (ITileInventory) tile;
				for (EnumFacing side : EnumFacing.VALUES) {
					if (message.defaults) {
						machine.setItemSorption(side, message.slot, machine.getInventoryConnection(side).getDefaultItemSorption(message.slot));
					}
					else {
						machine.setItemSorption(side, message.slot, ItemSorption.NON);
					}
				}
				machine.markDirtyAndNotify();
			}
		}
	}
}
