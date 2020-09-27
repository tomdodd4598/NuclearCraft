package nc.multiblock.qComputer.tile;

import nc.config.NCConfig;
import nc.multiblock.qComputer.*;
import nc.util.Lang;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class TileQuantumComputerController extends TileQuantumComputerPart implements ITickable {
	
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
			getMultiblock().getGateQueue().add(new QuantumGate.Reset(getMultiblock()));
		}
	}
	
	@Override
	public boolean onUseMultitool(ItemStack multitoolStack, EntityPlayer player, World world, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (player.isSneaking()) {
			QuantumComputer qc = getMultiblock();
			if (qc != null) {
				if (qc.qasmWrite) {
					qc.qasmPrint(player);
				}
				else if (qc.qubitCount() <= NCConfig.quantum_max_qubits_qasm) {
					qc.qasmStart = true;
					player.sendMessage(new TextComponentString(Lang.localise("info.nuclearcraft.multitool.quantum_computer.controller.qasm_start")));
				}
				else {
					player.sendMessage(new TextComponentString(Lang.localise("info.nuclearcraft.multitool.quantum_computer.controller.qasm_too_many_qubits")));
				}
				return true;
			}
		}
		return super.onUseMultitool(multitoolStack, player, world, facing, hitX, hitY, hitZ);
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
