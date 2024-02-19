package nc.network.gui;

import io.netty.buffer.ByteBuf;
import nc.tile.fluid.ITileFluid;
import nc.tile.internal.fluid.TankOutputSetting;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;

public class ToggleTankOutputSettingPacket extends TileGuiPacket {
	
	protected int tank;
	protected int setting;
	
	public ToggleTankOutputSettingPacket() {
		super();
	}
	
	public ToggleTankOutputSettingPacket(ITileFluid tile, int tank, TankOutputSetting setting) {
		super(tile);
		this.tank = tank;
		this.setting = setting.ordinal();
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		super.fromBytes(buf);
		tank = buf.readInt();
		setting = buf.readInt();
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		super.toBytes(buf);
		buf.writeInt(tank);
		buf.writeInt(setting);
	}
	
	public static class Handler extends TileGuiPacket.Handler<ToggleTankOutputSettingPacket> {
		
		@Override
		protected void onPacket(ToggleTankOutputSettingPacket message, EntityPlayerMP player, TileEntity tile) {
			if (tile instanceof ITileFluid) {
				ITileFluid machine = (ITileFluid) tile;
				TankOutputSetting setting = TankOutputSetting.values()[message.setting];
				machine.setTankOutputSetting(message.tank, setting);
				if (setting == TankOutputSetting.VOID) {
					machine.clearTank(message.tank);
				}
				machine.markDirtyAndNotify(true);
			}
		}
	}
}
