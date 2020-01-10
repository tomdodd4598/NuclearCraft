package nc.network.gui;

import io.netty.buffer.ByteBuf;
import nc.tile.internal.inventory.ItemOutputSetting;
import nc.tile.inventory.ITileInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class ToggleItemOutputSettingPacket implements IMessage {
	
	boolean messageValid;
	
	BlockPos pos;
	int slot;
	int setting;
	
	public ToggleItemOutputSettingPacket() {
		messageValid = false;
	}
	
	public ToggleItemOutputSettingPacket(ITileInventory machine, int slot, ItemOutputSetting setting) {
		pos = machine.getTilePos();
		this.slot = slot;
		this.setting = setting.ordinal();
		messageValid = true;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		try {
			pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
			slot = buf.readInt();
			setting = buf.readInt();
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
		buf.writeInt(setting);
	}
	
	public static class Handler implements IMessageHandler<ToggleItemOutputSettingPacket, IMessage> {
		
		@Override
		public IMessage onMessage(ToggleItemOutputSettingPacket message, MessageContext ctx) {
			if (!message.messageValid && ctx.side != Side.SERVER) return null;
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> processMessage(message, ctx));
			return null;
		}
		
		void processMessage(ToggleItemOutputSettingPacket message, MessageContext ctx) {
			TileEntity tile = ctx.getServerHandler().player.getServerWorld().getTileEntity(message.pos);
			if (tile instanceof ITileInventory) {
				ITileInventory machine = (ITileInventory) tile;
				ItemOutputSetting setting = ItemOutputSetting.values()[message.setting];
				machine.setItemOutputSetting(message.slot, setting);
				if (setting == ItemOutputSetting.VOID) machine.getInventoryStacks().set(message.slot, ItemStack.EMPTY);
				machine.markDirtyAndNotify();
			}
		}
	}
}
