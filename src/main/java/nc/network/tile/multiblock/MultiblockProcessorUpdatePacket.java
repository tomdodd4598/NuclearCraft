package nc.network.tile.multiblock;

import java.util.List;

import io.netty.buffer.ByteBuf;
import nc.network.tile.processor.ProcessorUpdatePacket;
import nc.tile.internal.fluid.Tank;
import net.minecraft.util.math.BlockPos;

public abstract class MultiblockProcessorUpdatePacket extends ProcessorUpdatePacket {
	
	public MultiblockProcessorUpdatePacket() {
		super();
	}
	
	public MultiblockProcessorUpdatePacket(BlockPos pos, boolean isProcessing, double time, double baseProcessTime, List<Tank> tanks) {
		super(pos, isProcessing, time, baseProcessTime, tanks);
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		super.fromBytes(buf);
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		super.toBytes(buf);
	}
}
