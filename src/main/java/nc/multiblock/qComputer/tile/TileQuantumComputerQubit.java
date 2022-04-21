package nc.multiblock.qComputer.tile;

import it.unimi.dsi.fastutil.ints.*;
import nc.multiblock.qComputer.*;
import nc.network.multiblock.QuantumComputerQubitRenderPacket;
import nc.tile.ITilePacket;
import nc.util.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class TileQuantumComputerQubit extends TileQuantumComputerPart implements ITickable, ITilePacket<QuantumComputerQubitRenderPacket> {
	
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
	public int[] weakSidesToCheck(World worldIn, BlockPos posIn) {
		return new int[] {2, 3, 4, 5};
	}
	
	@Override
	public void update() {
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
			getMultiblock().getGateQueue().add(new QuantumGate.Measurement(getMultiblock(), new IntOpenHashSet(new int[] {id})));
		}
	}
	
	@Override
	public boolean onUseMultitool(ItemStack multitool, EntityPlayer player, World worldIn, EnumFacing facing, float hitX, float hitY, float hitZ) {
		NBTTagCompound nbt = NBTHelper.getStackNBT(multitool);
		if (nbt != null) {
			String mode = nbt.getString("qubitMode");
			boolean s = mode.equals("set"), l = mode.equals("list");
			if (s || l) {
				IntCollection idColl = s ? new IntOpenHashSet() : new IntArrayList();
				if (s) {
					NBTHelper.readIntCollection(nbt, idColl, "qubitIDSet");
					NBTHelper.writeIntCollection(nbt, new IntArrayList(), "qubitIDList");
				}
				else {
					NBTHelper.readIntCollection(nbt, idColl, "qubitIDList");
					NBTHelper.writeIntCollection(nbt, new IntOpenHashSet(), "qubitIDSet");
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
					NBTHelper.writeIntCollection(nbt, idColl, "qubitIDSet");
				}
				else {
					NBTHelper.writeIntCollection(nbt, idColl, "qubitIDList");
				}
				return true;
			}
		}
		return super.onUseMultitool(multitool, player, worldIn, facing, hitX, hitY, hitZ);
	}
	
	@Override
	public QuantumComputerQubitRenderPacket getTileUpdatePacket() {
		return new QuantumComputerQubitRenderPacket(pos, measureColor);
	}
	
	@Override
	public void onTileUpdatePacket(QuantumComputerQubitRenderPacket message) {
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
