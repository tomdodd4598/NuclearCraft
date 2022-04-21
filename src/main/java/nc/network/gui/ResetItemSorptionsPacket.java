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

public class ResetItemSorptionsPacket implements IMessage {
	
	protected BlockPos pos;
	protected int slot;
	protected boolean defaults;
	
	public ResetItemSorptionsPacket() {
		
	}
	
	public ResetItemSorptionsPacket(ITileInventory machine, int slot, boolean defaults) {
		pos = machine.getTilePos();
		this.slot = slot;
		this.defaults = defaults;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
		slot = buf.readInt();
		defaults = buf.readBoolean();
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(pos.getX());
		buf.writeInt(pos.getY());
		buf.writeInt(pos.getZ());
		buf.writeInt(slot);
		buf.writeBoolean(defaults);
	}
	
	public static class Handler implements IMessageHandler<ResetItemSorptionsPacket, IMessage> {
		
		@Override
		public IMessage onMessage(ResetItemSorptionsPacket message, MessageContext ctx) {
			if (ctx.side == Side.SERVER) {
				FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> processMessage(message, ctx));
			}
			return null;
		}
		
		void processMessage(ResetItemSorptionsPacket message, MessageContext ctx) {
			EntityPlayerMP player = ctx.getServerHandler().player;
			World world = player.getServerWorld();
			if (!world.isBlockLoaded(message.pos) || !world.isBlockModifiable(player, message.pos)) {
				return;
			}
			TileEntity tile = world.getTileEntity(message.pos);
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
				machine.markDirtyAndNotify(true);
			}
		}
	}
}
