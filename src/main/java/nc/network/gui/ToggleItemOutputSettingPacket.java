package nc.network.gui;

import io.netty.buffer.ByteBuf;
import nc.tile.internal.inventory.ItemOutputSetting;
import nc.tile.inventory.ITileInventory;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.*;
import net.minecraftforge.fml.relauncher.Side;

public class ToggleItemOutputSettingPacket implements IMessage {
	
	protected BlockPos pos;
	protected int slot;
	protected int setting;
	
	public ToggleItemOutputSettingPacket() {
		
	}
	
	public ToggleItemOutputSettingPacket(ITileInventory machine, int slot, ItemOutputSetting setting) {
		pos = machine.getTilePos();
		this.slot = slot;
		this.setting = setting.ordinal();
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
		slot = buf.readInt();
		setting = buf.readInt();
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(pos.getX());
		buf.writeInt(pos.getY());
		buf.writeInt(pos.getZ());
		buf.writeInt(slot);
		buf.writeInt(setting);
	}
	
	public static class Handler implements IMessageHandler<ToggleItemOutputSettingPacket, IMessage> {
		
		@Override
		public IMessage onMessage(ToggleItemOutputSettingPacket message, MessageContext ctx) {
			if (ctx.side == Side.SERVER) {
				FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> processMessage(message, ctx));
			}
			return null;
		}
		
		void processMessage(ToggleItemOutputSettingPacket message, MessageContext ctx) {
			EntityPlayerMP player = ctx.getServerHandler().player;
			World world = player.getServerWorld();
			if (!world.isBlockLoaded(message.pos) || !world.isBlockModifiable(player, message.pos)) {
				return;
			}
			TileEntity tile = world.getTileEntity(message.pos);
			if (tile instanceof ITileInventory) {
				ITileInventory machine = (ITileInventory) tile;
				ItemOutputSetting setting = ItemOutputSetting.values()[message.setting];
				machine.setItemOutputSetting(message.slot, setting);
				if (setting == ItemOutputSetting.VOID) {
					machine.getInventoryStacks().set(message.slot, ItemStack.EMPTY);
				}
				machine.markDirtyAndNotify(true);
			}
		}
	}
}
