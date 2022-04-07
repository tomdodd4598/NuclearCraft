package nc.network.gui;

import io.netty.buffer.ByteBuf;
import nc.tile.fluid.ITileFluid;
import nc.tile.internal.fluid.TankOutputSetting;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.*;
import net.minecraftforge.fml.relauncher.Side;

public class ToggleTankOutputSettingPacket implements IMessage {
	
	protected BlockPos pos;
	protected int tank;
	protected int setting;
	
	public ToggleTankOutputSettingPacket() {
		
	}
	
	public ToggleTankOutputSettingPacket(ITileFluid machine, int tank, TankOutputSetting setting) {
		pos = machine.getTilePos();
		this.tank = tank;
		this.setting = setting.ordinal();
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
		tank = buf.readInt();
		setting = buf.readInt();
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(pos.getX());
		buf.writeInt(pos.getY());
		buf.writeInt(pos.getZ());
		buf.writeInt(tank);
		buf.writeInt(setting);
	}
	
	public static class Handler implements IMessageHandler<ToggleTankOutputSettingPacket, IMessage> {
		
		@Override
		public IMessage onMessage(ToggleTankOutputSettingPacket message, MessageContext ctx) {
			if (ctx.side == Side.SERVER) {
				FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> processMessage(message, ctx));
			}
			return null;
		}
		
		void processMessage(ToggleTankOutputSettingPacket message, MessageContext ctx) {
			EntityPlayerMP player = ctx.getServerHandler().player;
			World world = player.getServerWorld();
			if (!world.isBlockLoaded(message.pos) || !world.isBlockModifiable(player, message.pos)) {
				return;
			}
			TileEntity tile = world.getTileEntity(message.pos);
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
