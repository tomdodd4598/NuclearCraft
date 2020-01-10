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

public class ToggleItemSorptionPacket implements IMessage {
	
	boolean messageValid;
	
	BlockPos pos;
	int side;
	int slot;
	int sorption;
	
	public ToggleItemSorptionPacket() {
		messageValid = false;
	}
	
	public ToggleItemSorptionPacket(ITileInventory machine, EnumFacing side, int slot, ItemSorption sorption) {
		pos = machine.getTilePos();
		this.side = side.getIndex();
		this.slot = slot;
		this.sorption = sorption.ordinal();
		messageValid = true;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		try {
			pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
			side = buf.readInt();
			slot = buf.readInt();
			sorption = buf.readInt();
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
		buf.writeInt(side);
		buf.writeInt(slot);
		buf.writeInt(sorption);
	}
	
	public static class Handler implements IMessageHandler<ToggleItemSorptionPacket, IMessage> {
		
		@Override
		public IMessage onMessage(ToggleItemSorptionPacket message, MessageContext ctx) {
			if (!message.messageValid && ctx.side != Side.SERVER) return null;
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> processMessage(message, ctx));
			return null;
		}
		
		void processMessage(ToggleItemSorptionPacket message, MessageContext ctx) {
			TileEntity tile = ctx.getServerHandler().player.getServerWorld().getTileEntity(message.pos);
			if (tile instanceof ITileInventory) {
				ITileInventory machine = (ITileInventory) tile;
				machine.setItemSorption(EnumFacing.byIndex(message.side), message.slot, ItemSorption.values()[message.sorption]);
				machine.markDirtyAndNotify();
			}
		}
	}
}
