package nc.network.gui;

import io.netty.buffer.ByteBuf;
import nc.tile.fluid.ITileFluid;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.*;
import net.minecraftforge.fml.relauncher.Side;

public class ToggleInputTanksSeparatedPacket implements IMessage {
	
	protected BlockPos pos;
	protected boolean inputTanksSeparated;
	
	public ToggleInputTanksSeparatedPacket() {
		
	}
	
	public ToggleInputTanksSeparatedPacket(ITileFluid machine) {
		pos = machine.getTilePos();
		inputTanksSeparated = machine.getInputTanksSeparated();
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
		inputTanksSeparated = buf.readBoolean();
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(pos.getX());
		buf.writeInt(pos.getY());
		buf.writeInt(pos.getZ());
		buf.writeBoolean(inputTanksSeparated);
	}
	
	public static class Handler implements IMessageHandler<ToggleInputTanksSeparatedPacket, IMessage> {
		
		@Override
		public IMessage onMessage(ToggleInputTanksSeparatedPacket message, MessageContext ctx) {
			if (ctx.side == Side.SERVER) {
				FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> processMessage(message, ctx));
			}
			return null;
		}
		
		void processMessage(ToggleInputTanksSeparatedPacket message, MessageContext ctx) {
			EntityPlayerMP player = ctx.getServerHandler().player;
			World world = player.getServerWorld();
			if (!world.isBlockLoaded(message.pos) || !world.isBlockModifiable(player, message.pos)) {
				return;
			}
			TileEntity tile = world.getTileEntity(message.pos);
			if (tile instanceof ITileFluid) {
				ITileFluid machine = (ITileFluid) tile;
				machine.setInputTanksSeparated(message.inputTanksSeparated);
				tile.markDirty();
			}
		}
	}
}
