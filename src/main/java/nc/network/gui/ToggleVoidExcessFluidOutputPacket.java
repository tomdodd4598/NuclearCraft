package nc.network.gui;

import io.netty.buffer.ByteBuf;
import nc.tile.fluid.ITileFluid;
import nc.tile.internal.fluid.TankOutputSetting;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;

public class ToggleVoidExcessFluidOutputPacket extends TileGuiPacket {
	
	protected int voidExcessFluidOutput;
	protected int tankNumber;
	
	public ToggleVoidExcessFluidOutputPacket() {
		super();
	}
	
	public ToggleVoidExcessFluidOutputPacket(ITileFluid tile, int tankNumber) {
		super(tile);
		voidExcessFluidOutput = tile.getTankOutputSetting(tankNumber).ordinal();
		this.tankNumber = tankNumber;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		super.fromBytes(buf);
		voidExcessFluidOutput = buf.readInt();
		tankNumber = buf.readInt();
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		super.toBytes(buf);
		buf.writeInt(voidExcessFluidOutput);
		buf.writeInt(tankNumber);
	}
	
	public static class Handler extends TileGuiPacket.Handler<ToggleVoidExcessFluidOutputPacket> {
		
		@Override
		protected void onPacket(ToggleVoidExcessFluidOutputPacket message, EntityPlayerMP player, TileEntity tile) {
			if (tile instanceof ITileFluid machine) {
                machine.setTankOutputSetting(message.tankNumber, TankOutputSetting.values()[message.voidExcessFluidOutput]);
				tile.markDirty();
			}
		}
	}
}
