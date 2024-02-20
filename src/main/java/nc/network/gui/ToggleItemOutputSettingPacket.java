package nc.network.gui;

import io.netty.buffer.ByteBuf;
import nc.tile.internal.inventory.ItemOutputSetting;
import nc.tile.inventory.ITileInventory;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class ToggleItemOutputSettingPacket extends TileGuiPacket {
	
	protected int slot;
	protected int setting;
	
	public ToggleItemOutputSettingPacket() {
		super();
	}
	
	public ToggleItemOutputSettingPacket(ITileInventory tile, int slot, ItemOutputSetting setting) {
		super(tile);
		this.slot = slot;
		this.setting = setting.ordinal();
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		super.fromBytes(buf);
		slot = buf.readInt();
		setting = buf.readInt();
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		super.toBytes(buf);
		buf.writeInt(slot);
		buf.writeInt(setting);
	}
	
	public static class Handler extends TileGuiPacket.Handler<ToggleItemOutputSettingPacket> {
		
		@Override
		protected void onPacket(ToggleItemOutputSettingPacket message, EntityPlayerMP player, TileEntity tile) {
			if (tile instanceof ITileInventory machine) {
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
