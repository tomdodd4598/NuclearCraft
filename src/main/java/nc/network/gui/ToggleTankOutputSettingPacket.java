package nc.network.gui;

import io.netty.buffer.ByteBuf;
import nc.tile.fluid.ITileFluid;
import nc.tile.internal.fluid.TankOutputSetting;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class ToggleTankOutputSettingPacket implements IMessage {
	
	boolean messageValid;
	
	BlockPos pos;
	int tank;
	int setting;
	
	public ToggleTankOutputSettingPacket() {
		messageValid = false;
	}
	
	public ToggleTankOutputSettingPacket(ITileFluid machine, int tank, TankOutputSetting setting) {
		pos = machine.getTilePos();
		this.tank = tank;
		this.setting = setting.ordinal();
		messageValid = true;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		try {
			pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
			tank = buf.readInt();
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
		buf.writeInt(tank);
		buf.writeInt(setting);
	}
	
	public static class Handler implements IMessageHandler<ToggleTankOutputSettingPacket, IMessage> {
		
		@Override
		public IMessage onMessage(ToggleTankOutputSettingPacket message, MessageContext ctx) {
			if (!message.messageValid && ctx.side != Side.SERVER) return null;
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> processMessage(message, ctx));
			return null;
		}
		
		void processMessage(ToggleTankOutputSettingPacket message, MessageContext ctx) {
			TileEntity tile = ctx.getServerHandler().player.getServerWorld().getTileEntity(message.pos);
			if (tile instanceof ITileFluid) {
				ITileFluid machine = (ITileFluid) tile;
				TankOutputSetting setting = TankOutputSetting.values()[message.setting];
				machine.setTankOutputSetting(message.tank, setting);
				if (setting == TankOutputSetting.VOID) machine.clearTank(message.tank);
				machine.markDirtyAndNotify();
			}
		}
	}
}
