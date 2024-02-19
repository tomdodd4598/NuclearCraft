package nc.network.gui;

import io.netty.buffer.ByteBuf;
import nc.tile.fluid.ITileFluid;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;

public class ToggleVoidUnusableFluidInputPacket extends TileGuiPacket {
	
	protected boolean voidUnusableFluidInput;
	protected int tankNumber;
	
	public ToggleVoidUnusableFluidInputPacket() {
		super();
	}
	
	public ToggleVoidUnusableFluidInputPacket(ITileFluid tile, int tankNumber) {
		super(tile);
		voidUnusableFluidInput = tile.getVoidUnusableFluidInput(tankNumber);
		this.tankNumber = tankNumber;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		super.fromBytes(buf);
		voidUnusableFluidInput = buf.readBoolean();
		tankNumber = buf.readInt();
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		super.toBytes(buf);
		buf.writeBoolean(voidUnusableFluidInput);
		buf.writeInt(tankNumber);
	}
	
	public static class Handler extends TileGuiPacket.Handler<ToggleVoidUnusableFluidInputPacket> {
		
		@Override
		protected void onPacket(ToggleVoidUnusableFluidInputPacket message, EntityPlayerMP player, TileEntity tile) {
			if (tile instanceof ITileFluid) {
				ITileFluid machine = (ITileFluid) tile;
				machine.setVoidUnusableFluidInput(message.tankNumber, message.voidUnusableFluidInput);
				tile.markDirty();
			}
		}
	}
}
