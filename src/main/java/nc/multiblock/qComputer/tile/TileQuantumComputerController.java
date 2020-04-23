package nc.multiblock.qComputer.tile;

import nc.multiblock.qComputer.QuantumComputer;
import nc.multiblock.qComputer.QuantumComputerGate;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileQuantumComputerController extends TileQuantumComputerPart {
	
	public boolean pulsed = false;
	
	public TileQuantumComputerController() {
		super();
	}
	
	@Override
	public void onMachineAssembled(QuantumComputer multiblock) {
		doStandardNullControllerResponse(multiblock);
	}
	
	@Override
	public void onMachineBroken() {}
	
	@Override
	public int[] weakSidesToCheck(World world, BlockPos pos) {
		return new int[] {2, 3, 4, 5};
	}
	
	@Override
	public void update() {
		super.update();
		if (!pulsed && getIsRedstonePowered()) {
			queueReset();
			pulsed = true;
		}
		else if (pulsed && !getIsRedstonePowered()) {
			pulsed = false;
		}
	}
	
	public final void queueReset() {
		if (isMultiblockAssembled()) {
			getMultiblock().getGateQueue().add(new QuantumComputerGate.Reset(getMultiblock()));
		}
	}
	
	@Override
	public NBTTagCompound writeAll(NBTTagCompound nbt) {
		super.writeAll(nbt);
		nbt.setBoolean("pulsed", pulsed);
		return nbt;
	}
	
	@Override
	public void readAll(NBTTagCompound nbt) {
		super.readAll(nbt);
		pulsed = nbt.getBoolean("pulsed");
	}
}
