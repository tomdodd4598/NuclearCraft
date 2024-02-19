package nc.network.multiblock;

import io.netty.buffer.ByteBuf;
import nc.multiblock.Multiblock;
import nc.network.NCPacket;
import nc.tile.multiblock.ITileMultiblockPart;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.*;
import net.minecraftforge.fml.relauncher.Side;

public class ClearAllMaterialPacket extends NCPacket {
	
	protected BlockPos pos;
	
	public ClearAllMaterialPacket() {
		super();
	}
	
	public ClearAllMaterialPacket(BlockPos pos) {
		super();
		this.pos = pos;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		super.fromBytes(buf);
		pos = readPos(buf);
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		super.toBytes(buf);
		writePos(buf, pos);
	}
	
	public static class Handler implements IMessageHandler<ClearAllMaterialPacket, IMessage> {
		
		@Override
		public IMessage onMessage(ClearAllMaterialPacket message, MessageContext ctx) {
			if (ctx.side == Side.SERVER) {
				FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
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
				});
			}
			return null;
		}
	}
}
