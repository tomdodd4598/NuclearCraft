package nc.multiblock.qComputer.tile;

import static nc.config.NCConfig.quantum_max_qubits_code;

import nc.multiblock.qComputer.QuantumComputer;
import nc.util.Lang;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public abstract class TileQuantumComputerCodeGenerator extends TileQuantumComputerPart {
	
	protected final int codeType;
	
	protected TileQuantumComputerCodeGenerator(int codeType) {
		super();
		this.codeType = codeType;
	}
	
	public static class Qasm extends TileQuantumComputerCodeGenerator {
		
		public Qasm() {
			super(0);
		}
		
		@Override
		protected String getUnlocalizedCodeStartMessage() {
			return "info.nuclearcraft.multitool.quantum_computer.controller.code_qasm_start";
		}
	}
	
	public static class Qiskit extends TileQuantumComputerCodeGenerator {
		
		public Qiskit() {
			super(1);
		}
		
		@Override
		protected String getUnlocalizedCodeStartMessage() {
			return "info.nuclearcraft.multitool.quantum_computer.controller.code_qiskit_start";
		}
	}
	
	@Override
	public void onMachineAssembled(QuantumComputer multiblock) {
		doStandardNullControllerResponse(multiblock);
	}
	
	@Override
	public void onMachineBroken() {}
	
	@Override
	public boolean shouldRefresh(World worldIn, BlockPos posIn, IBlockState oldState, IBlockState newState) {
		return oldState != newState;
	}
	
	@Override
	public boolean onUseMultitool(ItemStack multitool, EntityPlayer player, World worldIn, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (!player.isSneaking()) {
			QuantumComputer qc = getMultiblock();
			if (qc != null && qc.isAssembled()) {
				if (qc.codeType >= 0) {
					qc.printCode(player);
				}
				else if (qc.qubitCount() <= quantum_max_qubits_code) {
					qc.codeStart = codeType;
					player.sendMessage(new TextComponentString(Lang.localise(getUnlocalizedCodeStartMessage())));
				}
				else {
					player.sendMessage(new TextComponentString(Lang.localise("info.nuclearcraft.multitool.quantum_computer.controller.code_too_many_qubits")));
				}
				return true;
			}
		}
		return super.onUseMultitool(multitool, player, worldIn, facing, hitX, hitY, hitZ);
	}
	
	protected abstract String getUnlocalizedCodeStartMessage();
}
