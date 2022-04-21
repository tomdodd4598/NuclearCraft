package nc.network.multiblock;

import io.netty.buffer.ByteBuf;
import nc.multiblock.Multiblock;
import nc.multiblock.tile.ITileMultiblockPart;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.*;
import net.minecraftforge.fml.relauncher.Side;

public class ClearAllMaterialPacket implements IMessage {
	
	protected BlockPos pos;
	
	public ClearAllMaterialPacket() {
		
	}
	
	public ClearAllMaterialPacket(BlockPos pos) {
		this.pos = pos;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(pos.getX());
		buf.writeInt(pos.getY());
		buf.writeInt(pos.getZ());
	}
	
	public static class Handler implements IMessageHandler<ClearAllMaterialPacket, IMessage> {
		
		@Override
		public IMessage onMessage(ClearAllMaterialPacket message, MessageContext ctx) {
			if (ctx.side == Side.SERVER) {
				FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> processMessage(message, ctx));
			}
			return null;
		}
		
		void processMessage(ClearAllMaterialPacket message, MessageContext ctx) {
			EntityPlayerMP player = ctx.getServerHandler().player;
			World world = player.getServerWorld();
			if (!world.isBlockLoaded(message.pos) || !world.isBlockModifiable(player, message.pos)) {
				return;
			}
			TileEntity tile = world.getTileEntity(message.pos);
			if (tile instanceof ITileMultiblockPart) {
				Multiblock<?, ?> multiblock = ((ITileMultiblockPart<?, ?>) tile).getMultiblock();
				multiblock.clearAllMaterial();
			}
		}
	}
}
