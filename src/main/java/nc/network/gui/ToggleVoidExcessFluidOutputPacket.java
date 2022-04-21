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

public class ToggleVoidExcessFluidOutputPacket implements IMessage {
	
	protected BlockPos pos;
	protected int voidExcessFluidOutput;
	protected int tankNumber;
	
	public ToggleVoidExcessFluidOutputPacket() {
		
	}
	
	public ToggleVoidExcessFluidOutputPacket(ITileFluid machine, int tankNumber) {
		pos = machine.getTilePos();
		voidExcessFluidOutput = machine.getTankOutputSetting(tankNumber).ordinal();
		this.tankNumber = tankNumber;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
		voidExcessFluidOutput = buf.readInt();
		tankNumber = buf.readInt();
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(pos.getX());
		buf.writeInt(pos.getY());
		buf.writeInt(pos.getZ());
		buf.writeInt(voidExcessFluidOutput);
		buf.writeInt(tankNumber);
	}
	
	public static class Handler implements IMessageHandler<ToggleVoidExcessFluidOutputPacket, IMessage> {
		
		@Override
		public IMessage onMessage(ToggleVoidExcessFluidOutputPacket message, MessageContext ctx) {
			if (ctx.side == Side.SERVER) {
				FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> processMessage(message, ctx));
			}
			return null;
		}
		
		void processMessage(ToggleVoidExcessFluidOutputPacket message, MessageContext ctx) {
			EntityPlayerMP player = ctx.getServerHandler().player;
			World world = player.getServerWorld();
			if (!world.isBlockLoaded(message.pos) || !world.isBlockModifiable(player, message.pos)) {
				return;
			}
			TileEntity tile = world.getTileEntity(message.pos);
			if (tile instanceof ITileFluid) {
				ITileFluid machine = (ITileFluid) tile;
				machine.setTankOutputSetting(message.tankNumber, TankOutputSetting.values()[message.voidExcessFluidOutput]);
				tile.markDirty();
			}
		}
	}
}
