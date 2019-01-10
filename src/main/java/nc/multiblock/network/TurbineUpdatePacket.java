package nc.multiblock.network;

import io.netty.buffer.ByteBuf;
import nc.multiblock.turbine.Turbine;
import nc.multiblock.turbine.tile.TileTurbineController;
import net.minecraft.util.math.BlockPos;

public class TurbineUpdatePacket extends MultiblockUpdatePacket {
	
	public boolean isTurbineOn;
	public double power, rawConductivity, totalExpansionLevel, idealTotalExpansionLevel;
	public int recipeRate, shaftWidth, bladeLength, noBladeSets, capacity, energy;
	
	public TurbineUpdatePacket() {
		messageValid = false;
	}
	
	public TurbineUpdatePacket(BlockPos pos, boolean isTurbineOn, double power, double rawConductivity, double totalExpansionLevel, double idealTotalExpansionLevel, int recipeRate, int shaftWidth, int bladeLength, int noBladeSets, int capacity, int energy) {
		this.pos = pos;
		this.isTurbineOn = isTurbineOn;
		this.power = power;
		this.rawConductivity = rawConductivity;
		this.totalExpansionLevel = totalExpansionLevel;
		this.idealTotalExpansionLevel = idealTotalExpansionLevel;
		this.recipeRate = recipeRate;
		this.shaftWidth = shaftWidth;
		this.bladeLength = bladeLength;
		this.noBladeSets = noBladeSets;
		this.capacity = capacity;
		this.energy = energy;
		
		messageValid = true;
	}
	
	@Override
	public void readMessage(ByteBuf buf) {
		pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
		isTurbineOn = buf.readBoolean();
		power = buf.readDouble();
		rawConductivity = buf.readDouble();
		totalExpansionLevel = buf.readDouble();
		idealTotalExpansionLevel = buf.readDouble();
		recipeRate = buf.readInt();
		shaftWidth = buf.readInt();
		bladeLength = buf.readInt();
		noBladeSets = buf.readInt();
		capacity = buf.readInt();
		energy = buf.readInt();
	}
	
	@Override
	public void writeMessage(ByteBuf buf) {
		buf.writeInt(pos.getX());
		buf.writeInt(pos.getY());
		buf.writeInt(pos.getZ());
		buf.writeBoolean(isTurbineOn);
		buf.writeDouble(power);
		buf.writeDouble(rawConductivity);
		buf.writeDouble(totalExpansionLevel);
		buf.writeDouble(idealTotalExpansionLevel);
		buf.writeInt(recipeRate);
		buf.writeInt(shaftWidth);
		buf.writeInt(bladeLength);
		buf.writeInt(noBladeSets);
		buf.writeInt(capacity);
		buf.writeInt(energy);
	}
	
	public static class Handler extends MultiblockUpdatePacket.Handler<TurbineUpdatePacket, Turbine, TileTurbineController> {

		public Handler() {
			super(TileTurbineController.class);
		}
	}
}
