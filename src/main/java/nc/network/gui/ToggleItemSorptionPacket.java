package nc.network.gui;

import io.netty.buffer.ByteBuf;
import nc.tile.internal.inventory.ItemSorption;
import nc.tile.inventory.ITileInventory;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.*;
import net.minecraftforge.fml.relauncher.Side;

public class ToggleItemSorptionPacket implements IMessage {
	
	protected BlockPos pos;
	protected int side;
	protected int slot;
	protected int sorption;
	
	public ToggleItemSorptionPacket() {
		
	}
	
	public ToggleItemSorptionPacket(ITileInventory machine, EnumFacing side, int slot, ItemSorption sorption) {
		pos = machine.getTilePos();
		this.side = side.getIndex();
		this.slot = slot;
		this.sorption = sorption.ordinal();
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
		side = buf.readInt();
		slot = buf.readInt();
		sorption = buf.readInt();
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
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
			if (ctx.side == Side.SERVER) {
				FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> processMessage(message, ctx));
			}
			return null;
		}
		
		void processMessage(ToggleItemSorptionPacket message, MessageContext ctx) {
			EntityPlayerMP player = ctx.getServerHandler().player;
			World world = player.getServerWorld();
			if (!world.isBlockLoaded(message.pos) || !world.isBlockModifiable(player, message.pos)) {
				return;
			}
			TileEntity tile = world.getTileEntity(message.pos);
			if (tile instanceof ITileInventory) {
				ITileInventory machine = (ITileInventory) tile;
				machine.setItemSorption(EnumFacing.byIndex(message.side), message.slot, ItemSorption.values()[message.sorption]);
				machine.markDirtyAndNotify(true);
			}
		}
	}
}
