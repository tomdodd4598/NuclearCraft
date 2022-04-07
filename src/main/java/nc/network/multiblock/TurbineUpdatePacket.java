package nc.network.multiblock;

import io.netty.buffer.ByteBuf;
import nc.multiblock.turbine.Turbine;
import nc.multiblock.turbine.tile.*;
import nc.tile.internal.energy.EnergyStorage;
import net.minecraft.util.math.BlockPos;

public class TurbineUpdatePacket extends MultiblockUpdatePacket {
	
	public boolean isTurbineOn;
	public double power, rawPower, conductivity, rotorEfficiency, powerBonus, totalExpansionLevel, idealTotalExpansionLevel, bearingTension;
	public int energy, capacity, shaftWidth, bladeLength, noBladeSets, dynamoCoilCount, dynamoCoilCountOpposite;
	
	public TurbineUpdatePacket() {
		
	}
	
	public TurbineUpdatePacket(BlockPos pos, boolean isTurbineOn, EnergyStorage energyStorage, double power, double rawPower, double conductivity, double rotorEfficiency, double powerBonus, double totalExpansionLevel, double idealTotalExpansionLevel, int shaftWidth, int bladeLength, int noBladeSets, int dynamoCoilCount, int dynamoCoilCountOpposite, double bearingTension) {
		this.pos = pos;
		this.isTurbineOn = isTurbineOn;
		energy = energyStorage.getEnergyStored();
		capacity = energyStorage.getMaxEnergyStored();
		this.power = power;
		this.rawPower = rawPower;
		this.conductivity = conductivity;
		this.rotorEfficiency = rotorEfficiency;
		this.powerBonus = powerBonus;
		this.totalExpansionLevel = totalExpansionLevel;
		this.idealTotalExpansionLevel = idealTotalExpansionLevel;
		this.shaftWidth = shaftWidth;
		this.bladeLength = bladeLength;
		this.noBladeSets = noBladeSets;
		this.dynamoCoilCount = dynamoCoilCount;
		this.dynamoCoilCountOpposite = dynamoCoilCountOpposite;
		this.bearingTension = bearingTension;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
		isTurbineOn = buf.readBoolean();
		energy = buf.readInt();
		capacity = buf.readInt();
		power = buf.readDouble();
		rawPower = buf.readDouble();
		conductivity = buf.readDouble();
		rotorEfficiency = buf.readDouble();
		powerBonus = buf.readDouble();
		totalExpansionLevel = buf.readDouble();
		idealTotalExpansionLevel = buf.readDouble();
		shaftWidth = buf.readInt();
		bladeLength = buf.readInt();
		noBladeSets = buf.readInt();
		dynamoCoilCount = buf.readInt();
		dynamoCoilCountOpposite = buf.readInt();
		bearingTension = buf.readDouble();
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(pos.getX());
		buf.writeInt(pos.getY());
		buf.writeInt(pos.getZ());
		buf.writeBoolean(isTurbineOn);
		buf.writeInt(energy);
		buf.writeInt(capacity);
		buf.writeDouble(power);
		buf.writeDouble(rawPower);
		buf.writeDouble(conductivity);
		buf.writeDouble(rotorEfficiency);
		buf.writeDouble(powerBonus);
		buf.writeDouble(totalExpansionLevel);
		buf.writeDouble(idealTotalExpansionLevel);
		buf.writeInt(shaftWidth);
		buf.writeInt(bladeLength);
		buf.writeInt(noBladeSets);
		buf.writeInt(dynamoCoilCount);
		buf.writeInt(dynamoCoilCountOpposite);
		buf.writeDouble(bearingTension);
	}
	
	public static class Handler extends MultiblockUpdatePacket.Handler<Turbine, ITurbinePart, TurbineUpdatePacket, TileTurbineController, TurbineUpdatePacket> {
		
		public Handler() {
			super(TileTurbineController.class);
		}
		
		@Override
		protected void onPacket(TurbineUpdatePacket message, Turbine multiblock) {
			multiblock.onMultiblockUpdatePacket(message);
		}
	}
}
