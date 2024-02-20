package nc.network.gui;

import io.netty.buffer.ByteBuf;
import nc.tile.fluid.ITileFluid;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;

public class ToggleInputTanksSeparatedPacket extends TileGuiPacket {
	
	protected boolean inputTanksSeparated;
	
	public ToggleInputTanksSeparatedPacket() {
		super();
	}
	
	public ToggleInputTanksSeparatedPacket(ITileFluid tile) {
		super(tile);
		inputTanksSeparated = tile.getInputTanksSeparated();
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		super.fromBytes(buf);
		inputTanksSeparated = buf.readBoolean();
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		super.toBytes(buf);
		buf.writeBoolean(inputTanksSeparated);
	}
	
	public static class Handler extends TileGuiPacket.Handler<ToggleInputTanksSeparatedPacket> {
		
		@Override
		protected void onPacket(ToggleInputTanksSeparatedPacket message, EntityPlayerMP player, TileEntity tile) {
			if (tile instanceof ITileFluid machine) {
                machine.setInputTanksSeparated(message.inputTanksSeparated);
				tile.markDirty();
			}
		}
	}
}
