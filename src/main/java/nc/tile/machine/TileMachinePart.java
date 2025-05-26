package nc.tile.machine;

import nc.capability.radiation.source.IRadiationSource;
import nc.multiblock.cuboidal.*;
import nc.multiblock.machine.*;

import javax.annotation.Nullable;

public abstract class TileMachinePart extends TileCuboidalMultiblockPart<Machine, IMachinePart> implements IMachinePart {
	
	public TileMachinePart(CuboidalPartPositionType positionType) {
		super(Machine.class, IMachinePart.class, positionType);
	}
	
	@Override
	public Machine createNewMultiblock() {
		return new Machine(world);
	}
	
	@Override
	protected @Nullable IRadiationSource getMultiblockRadiationSourceInternal() {
		Machine machine = getMultiblock();
		return machine == null ? null : machine.radiation;
	}
}
