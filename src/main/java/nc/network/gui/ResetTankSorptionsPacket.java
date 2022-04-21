package nc.network.gui;

import io.netty.buffer.ByteBuf;
import nc.tile.fluid.ITileFluid;
import nc.tile.internal.fluid.TankSorption;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.*;
import net.minecraftforge.fml.relauncher.Side;

public class ResetTankSorptionsPacket implements IMessage {
	
	protected BlockPos pos;
	protected int tank;
	protected boolean defaults;
	
	public ResetTankSorptionsPacket() {
		
	}
	
	public ResetTankSorptionsPacket(ITileFluid machine, int tank, boolean defaults) {
		pos = machine.getTilePos();
		this.tank = tank;
		this.defaults = defaults;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
		tank = buf.readInt();
		defaults = buf.readBoolean();
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(pos.getX());
		buf.writeInt(pos.getY());
		buf.writeInt(pos.getZ());
		buf.writeInt(tank);
		buf.writeBoolean(defaults);
	}
	
	public static class Handler implements IMessageHandler<ResetTankSorptionsPacket, IMessage> {
		
		@Override
		public IMessage onMessage(ResetTankSorptionsPacket message, MessageContext ctx) {
			if (ctx.side == Side.SERVER) {
				FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> processMessage(message, ctx));
			}
			return null;
		}
		
		void processMessage(ResetTankSorptionsPacket message, MessageContext ctx) {
			EntityPlayerMP player = ctx.getServerHandler().player;
			World world = player.getServerWorld();
			if (!world.isBlockLoaded(message.pos) || !world.isBlockModifiable(player, message.pos)) {
				return;
			}
			TileEntity tile = world.getTileEntity(message.pos);
			if (tile instanceof ITileFluid) {
				ITileFluid machine = (ITileFluid) tile;
				for (EnumFacing side : EnumFacing.VALUES) {
					if (message.defaults) {
						machine.setTankSorption(side, message.tank, machine.getFluidConnection(side).getDefaultTankSorption(message.tank));
					}
					else {
						machine.setTankSorption(side, message.tank, TankSorption.NON);
					}
				}
				machine.markDirtyAndNotify(true);
			}
		}
	}
}
