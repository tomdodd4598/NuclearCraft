package nc.multiblock.qComputer.tile;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntCollection;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import nc.multiblock.network.QuantumComputerQubitRenderPacket;
import nc.multiblock.qComputer.QuantumComputer;
import nc.multiblock.qComputer.QuantumComputerGate;
import nc.util.Lang;
import nc.util.NBTHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class TileQuantumComputerQubit extends TileQuantumComputerPart {
	
	public int id = -1;
	public boolean redstone = false, pulsed = false;
	public float measureColor = 0F;
	
	public TileQuantumComputerQubit() {}
	
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
			queueMeasurement();
			pulsed = true;
		}
		else if (pulsed && !getIsRedstonePowered()) {
			pulsed = false;
		}
	}
	
	public final void queueMeasurement() {
		if (isMultiblockAssembled()) {
			getMultiblock().getGateQueue().add(new QuantumComputerGate.Measurement(getMultiblock(), new IntOpenHashSet(new int[] {id})));
		}
	}
	
	@Override
	public boolean onUseMultitool(ItemStack multitoolStack, EntityPlayer player, World world, EnumFacing facing, float hitX, float hitY, float hitZ) {
		NBTTagCompound nbt = multitoolStack.getTagCompound();
		String mode = nbt.getString("qubitMode");
		boolean s = mode.equals("set"), l = mode.equals("list");
		if (s || l) {
			IntCollection idColl = s ? new IntOpenHashSet() : new IntArrayList();
			if (s) {
				NBTHelper.loadIntCollection(nbt, idColl, "qubitIDSet");
				NBTHelper.saveIntCollection(nbt, new IntArrayList(), "qubitIDList");
			}
			else {
				NBTHelper.loadIntCollection(nbt, idColl, "qubitIDList");
				NBTHelper.saveIntCollection(nbt, new IntOpenHashSet(), "qubitIDSet");
			}
			
			if (!player.isSneaking()) {
				boolean wasEmpty = idColl.isEmpty();
				if (idColl.add(id)) {
					if (wasEmpty) {
						player.sendMessage(new TextComponentString(Lang.localise("info.nuclearcraft.multitool.quantum_computer.start_qubit_" + mode, id)));
					}
					else {
						player.sendMessage(new TextComponentString(Lang.localise("info.nuclearcraft.multitool.quantum_computer.add_qubit", id)));
					}
				}
			}
			
			if (s) {
				NBTHelper.saveIntCollection(nbt, idColl, "qubitIDSet");
			}
			else {
				NBTHelper.saveIntCollection(nbt, idColl, "qubitIDList");
			}
			return true;
		}
		
		return super.onUseMultitool(multitoolStack, player, world, facing, hitX, hitY, hitZ);
	}
	
	public void onRenderPacket(QuantumComputerQubitRenderPacket message) {
		measureColor = message.measureColor;
	}
	
	@Override
	public NBTTagCompound writeAll(NBTTagCompound nbt) {
		super.writeAll(nbt);
		nbt.setInteger("qubitID", id);
		nbt.setBoolean("qubitRedstone", redstone);
		nbt.setBoolean("qubitPulsed", pulsed);
		nbt.setFloat("qubitMeasureColor", measureColor);
		return nbt;
	}
	
	@Override
	public void readAll(NBTTagCompound nbt) {
		super.readAll(nbt);
		id = nbt.getInteger("qubitID");
		redstone = nbt.getBoolean("qubitRedstone");
		pulsed = nbt.getBoolean("qubitPulsed");
		measureColor = nbt.getFloat("qubitMeasureColor");
	}
}
