package nc.tile.generator;

import nc.tile.dummy.IInterfaceable;
import nc.tile.energy.*;
import nc.tile.internal.energy.EnergyConnection;
import nc.util.EnergyHelper;
import net.minecraft.util.ITickable;

public abstract class TilePassiveGenerator extends TileEnergy implements ITickable, IInterfaceable {
	
	public final int power;
	
	public TilePassiveGenerator(int maxPowerGen) {
		super(4 * maxPowerGen, 4 * maxPowerGen, ITileEnergy.energyConnectionAll(EnergyConnection.OUT));
		power = maxPowerGen;
	}
	
	@Override
	public void update() {
		if (!world.isRemote) {
			getEnergyStorage().changeEnergyStored(getGenerated());
			pushEnergy();
		}
	}
	
	public abstract int getGenerated();
	
	@Override
	public int getSinkTier() {
		return 10;
	}
	
	@Override
	public int getSourceTier() {
		return EnergyHelper.getEUTier(power);
	}
}
