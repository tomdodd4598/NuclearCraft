package nc.tile.quantum;

import it.unimi.dsi.fastutil.ints.*;
import it.unimi.dsi.fastutil.objects.*;
import nc.multiblock.quantum.*;
import nc.render.BlockHighlightTracker;
import nc.util.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.*;
import net.minecraft.world.World;

import static nc.multiblock.quantum.QuantumOperationWrapper.intsToString;

public abstract class TileQuantumComputerGate extends TileQuantumComputerPart implements ITickable {
	
	protected final String gateID;
	protected String toolMode = "";
	public boolean pulsed = false;
	
	public static abstract class Basic extends TileQuantumComputerGate {
		
		protected final IntSet targets;
		
		public Basic(String gateID) {
			super(gateID);
			targets = new IntRBTreeSet();
			toolMode = "getTarget";
		}
		
		@Override
		public void sendGateInfo(EntityPlayerMP player) {
			highlightQubits(player, targets);
			player.sendMessage(new TextComponentString(Lang.localize("info.nuclearcraft.multitool.quantum_computer.single_gate_info", getTileBlockDisplayName(), intsToString(targets))));
		}
		
		@Override
		public boolean onUseMultitool(ItemStack multitool, EntityPlayerMP player, World worldIn, EnumFacing facing, float hitX, float hitY, float hitZ) {
			NBTTagCompound nbt = NBTHelper.getStackNBT(multitool, "ncMultitool");
			if (nbt != null) {
				if (toolMode.equals("getTarget") && player.isSneaking()) {
					targets.clear();
					player.sendMessage(new TextComponentString(TextFormatting.ITALIC + Lang.localize("info.nuclearcraft.multitool.quantum_computer.start_target_set", getTileBlockDisplayName())));
					nbt.setString("qComputerQubitMode", "set");
					nbt.setString("qComputerGateMode", "");
					toolMode = "setTarget";
					
					clearMultitoolGateInfo(nbt);
					return true;
				}
				else if (toolMode.equals("setTarget") && !player.isSneaking()) {
					NBTHelper.readIntCollection(nbt, targets, "qubitIDSet");
					highlightQubits(player, targets);
					player.sendMessage(new TextComponentString(TextFormatting.BLUE + Lang.localize("info.nuclearcraft.multitool.quantum_computer.finish_target_set", getTileBlockDisplayName(), intsToString(targets))));
					nbt.setString("qComputerQubitMode", "");
					nbt.setString("qComputerGateMode", "");
					toolMode = "getTarget";
					
					clearMultitoolGateInfo(nbt);
					return true;
				}
			}
			return super.onUseMultitool(multitool, player, worldIn, facing, hitX, hitY, hitZ);
		}
		
		@Override
		public NBTTagCompound writeAll(NBTTagCompound nbt) {
			super.writeAll(nbt);
			NBTHelper.writeIntCollection(nbt, targets, "nQubits");
			return nbt;
		}
		
		@Override
		public void readAll(NBTTagCompound nbt) {
			super.readAll(nbt);
			NBTHelper.readIntCollection(nbt, targets, "nQubits");
		}
	}
	
	public static class X extends Basic {
		
		public X() {
			super("x");
		}
		
		@Override
		protected QuantumOperationWrapper newGate(QuantumComputer qc) {
			return new QuantumOperationWrapper.X(qc, targets.toIntArray());
		}
	}
	
	public static class Y extends Basic {
		
		public Y() {
			super("y");
		}
		
		@Override
		protected QuantumOperationWrapper newGate(QuantumComputer qc) {
			return new QuantumOperationWrapper.Y(qc, targets.toIntArray());
		}
	}
	
	public static class Z extends Basic {
		
		public Z() {
			super("z");
		}
		
		@Override
		protected QuantumOperationWrapper newGate(QuantumComputer qc) {
			return new QuantumOperationWrapper.Z(qc, targets.toIntArray());
		}
	}
	
	public static class H extends Basic {
		
		public H() {
			super("h");
		}
		
		@Override
		protected QuantumOperationWrapper newGate(QuantumComputer qc) {
			return new QuantumOperationWrapper.H(qc, targets.toIntArray());
		}
	}
	
	public static class S extends Basic {
		
		public S() {
			super("s");
		}
		
		@Override
		protected QuantumOperationWrapper newGate(QuantumComputer qc) {
			return new QuantumOperationWrapper.S(qc, targets.toIntArray());
		}
	}
	
	public static class Sdg extends Basic {
		
		public Sdg() {
			super("sdg");
		}
		
		@Override
		protected QuantumOperationWrapper newGate(QuantumComputer qc) {
			return new QuantumOperationWrapper.Sdg(qc, targets.toIntArray());
		}
	}
	
	public static class T extends Basic {
		
		public T() {
			super("t");
		}
		
		@Override
		protected QuantumOperationWrapper newGate(QuantumComputer qc) {
			return new QuantumOperationWrapper.T(qc, targets.toIntArray());
		}
	}
	
	public static class Tdg extends Basic {
		
		public Tdg() {
			super("tdg");
		}
		
		@Override
		protected QuantumOperationWrapper newGate(QuantumComputer qc) {
			return new QuantumOperationWrapper.Tdg(qc, targets.toIntArray());
		}
	}
	
	public static abstract class BasicAngle extends TileQuantumComputerGate {
		
		protected double angle = 0;
		protected final IntSet targets;
		
		public BasicAngle(String gateID) {
			super(gateID);
			targets = new IntRBTreeSet();
		}
		
		@Override
		public void sendGateInfo(EntityPlayerMP player) {
			highlightQubits(player, targets);
			player.sendMessage(new TextComponentString(Lang.localize("info.nuclearcraft.multitool.quantum_computer.single_angle_gate_info", getTileBlockDisplayName(), intsToString(targets), NCMath.decimalPlaces(angle, 5))));
		}
		
		@Override
		public boolean onUseMultitool(ItemStack multitool, EntityPlayerMP player, World worldIn, EnumFacing facing, float hitX, float hitY, float hitZ) {
			NBTTagCompound nbt = NBTHelper.getStackNBT(multitool, "ncMultitool");
			if (nbt != null) {
				if (toolMode.equals("getAngle") && player.isSneaking()) {
					angle = 0D;
					player.sendMessage(new TextComponentString(TextFormatting.ITALIC + Lang.localize("info.nuclearcraft.multitool.quantum_computer.start_angle", getTileBlockDisplayName())));
					nbt.setString("qComputerQubitMode", "");
					nbt.setString("qComputerGateMode", "angle");
					toolMode = "setAngle";
					
					clearMultitoolGateInfo(nbt);
					return true;
				}
				else if (toolMode.equals("setAngle") && !player.isSneaking()) {
					angle = nbt.getDouble("qGateAngle");
					player.sendMessage(new TextComponentString(TextFormatting.GREEN + Lang.localize("info.nuclearcraft.multitool.quantum_computer.finish_angle", getTileBlockDisplayName(), NCMath.decimalPlaces(angle, 5))));
					nbt.setString("qComputerQubitMode", "");
					nbt.setString("qComputerGateMode", "");
					toolMode = "getTarget";
					
					clearMultitoolGateInfo(nbt);
					return true;
				}
				else if (toolMode.equals("getTarget") && player.isSneaking()) {
					targets.clear();
					player.sendMessage(new TextComponentString(TextFormatting.ITALIC + Lang.localize("info.nuclearcraft.multitool.quantum_computer.start_target_set", getTileBlockDisplayName())));
					nbt.setString("qComputerQubitMode", "set");
					nbt.setString("qComputerGateMode", "");
					toolMode = "setTarget";
					
					clearMultitoolGateInfo(nbt);
					return true;
				}
				else if (toolMode.equals("setTarget") && !player.isSneaking()) {
					NBTHelper.readIntCollection(nbt, targets, "qubitIDSet");
					highlightQubits(player, targets);
					player.sendMessage(new TextComponentString(TextFormatting.BLUE + Lang.localize("info.nuclearcraft.multitool.quantum_computer.finish_target_set", getTileBlockDisplayName(), intsToString(targets))));
					nbt.setString("qComputerQubitMode", "");
					nbt.setString("qComputerGateMode", "");
					toolMode = "getAngle";
					
					clearMultitoolGateInfo(nbt);
					return true;
				}
			}
			return super.onUseMultitool(multitool, player, worldIn, facing, hitX, hitY, hitZ);
		}
		
		@Override
		public NBTTagCompound writeAll(NBTTagCompound nbt) {
			super.writeAll(nbt);
			nbt.setDouble("qGateAngle", angle);
			NBTHelper.writeIntCollection(nbt, targets, "nQubits");
			return nbt;
		}
		
		@Override
		public void readAll(NBTTagCompound nbt) {
			super.readAll(nbt);
			angle = nbt.getDouble("qGateAngle");
			NBTHelper.readIntCollection(nbt, targets, "nQubits");
		}
	}
	
	public static class P extends BasicAngle {
		
		public P() {
			super("p");
		}
		
		@Override
		protected QuantumOperationWrapper newGate(QuantumComputer qc) {
			return new QuantumOperationWrapper.P(qc, angle, targets.toIntArray());
		}
	}
	
	public static class RX extends BasicAngle {
		
		public RX() {
			super("rx");
		}
		
		@Override
		protected QuantumOperationWrapper newGate(QuantumComputer qc) {
			return new QuantumOperationWrapper.RX(qc, angle, targets.toIntArray());
		}
	}
	
	public static class RY extends BasicAngle {
		
		public RY() {
			super("ry");
		}
		
		@Override
		protected QuantumOperationWrapper newGate(QuantumComputer qc) {
			return new QuantumOperationWrapper.RY(qc, angle, targets.toIntArray());
		}
	}
	
	public static class RZ extends BasicAngle {
		
		public RZ() {
			super("rz");
		}
		
		@Override
		protected QuantumOperationWrapper newGate(QuantumComputer qc) {
			return new QuantumOperationWrapper.RZ(qc, angle, targets.toIntArray());
		}
	}
	
	public static abstract class Control extends TileQuantumComputerGate {
		
		protected final IntSet controls, targets;
		
		public Control(String gateID) {
			super(gateID);
			controls = new IntRBTreeSet();
			targets = new IntRBTreeSet();
			toolMode = "getControl";
		}
		
		@Override
		public void sendGateInfo(EntityPlayerMP player) {
			highlightQubits(player, controls);
			highlightQubits(player, targets);
			player.sendMessage(new TextComponentString(Lang.localize("info.nuclearcraft.multitool.quantum_computer.control_gate_info", getTileBlockDisplayName(), intsToString(targets), intsToString(controls))));
		}
		
		@Override
		public boolean onUseMultitool(ItemStack multitool, EntityPlayerMP player, World worldIn, EnumFacing facing, float hitX, float hitY, float hitZ) {
			NBTTagCompound nbt = NBTHelper.getStackNBT(multitool, "ncMultitool");
			if (nbt != null) {
				if (toolMode.equals("getControl") && player.isSneaking()) {
					controls.clear();
					player.sendMessage(new TextComponentString(TextFormatting.ITALIC + Lang.localize("info.nuclearcraft.multitool.quantum_computer.start_control_set", getTileBlockDisplayName())));
					nbt.setString("qComputerQubitMode", "set");
					nbt.setString("qComputerGateMode", "");
					toolMode = "setControl";
					
					clearMultitoolGateInfo(nbt);
					return true;
				}
				else if (toolMode.equals("setControl") && !player.isSneaking()) {
					NBTHelper.readIntCollection(nbt, controls, "qubitIDSet");
					highlightQubits(player, controls);
					player.sendMessage(new TextComponentString(TextFormatting.RED + Lang.localize("info.nuclearcraft.multitool.quantum_computer.finish_control_set", getTileBlockDisplayName(), intsToString(controls))));
					nbt.setString("qComputerQubitMode", "");
					nbt.setString("qComputerGateMode", "");
					toolMode = "getTarget";
					
					clearMultitoolGateInfo(nbt);
					return true;
				}
				else if (toolMode.equals("getTarget") && player.isSneaking()) {
					targets.clear();
					player.sendMessage(new TextComponentString(TextFormatting.ITALIC + Lang.localize("info.nuclearcraft.multitool.quantum_computer.start_target_set", getTileBlockDisplayName())));
					nbt.setString("qComputerQubitMode", "set");
					nbt.setString("qComputerGateMode", "");
					toolMode = "setTarget";
					
					clearMultitoolGateInfo(nbt);
					return true;
				}
				else if (toolMode.equals("setTarget") && !player.isSneaking()) {
					NBTHelper.readIntCollection(nbt, targets, "qubitIDSet");
					highlightQubits(player, targets);
					player.sendMessage(new TextComponentString(TextFormatting.BLUE + Lang.localize("info.nuclearcraft.multitool.quantum_computer.finish_target_set", getTileBlockDisplayName(), intsToString(targets))));
					nbt.setString("qComputerQubitMode", "");
					nbt.setString("qComputerGateMode", "");
					toolMode = "getControl";
					
					clearMultitoolGateInfo(nbt);
					return true;
				}
			}
			return super.onUseMultitool(multitool, player, worldIn, facing, hitX, hitY, hitZ);
		}
		
		@Override
		public NBTTagCompound writeAll(NBTTagCompound nbt) {
			super.writeAll(nbt);
			NBTHelper.writeIntCollection(nbt, controls, "cQubits");
			NBTHelper.writeIntCollection(nbt, targets, "tQubits");
			return nbt;
		}
		
		@Override
		public void readAll(NBTTagCompound nbt) {
			super.readAll(nbt);
			NBTHelper.readIntCollection(nbt, controls, "cQubits");
			NBTHelper.readIntCollection(nbt, targets, "tQubits");
		}
	}
	
	public static class CX extends Control {
		
		public CX() {
			super("cx");
		}
		
		@Override
		protected QuantumOperationWrapper newGate(QuantumComputer qc) {
			return new QuantumOperationWrapper.CX(qc, controls.toIntArray(), targets.toIntArray());
		}
	}
	
	public static class CY extends Control {
		
		public CY() {
			super("cy");
		}
		
		@Override
		protected QuantumOperationWrapper newGate(QuantumComputer qc) {
			return new QuantumOperationWrapper.CY(qc, controls.toIntArray(), targets.toIntArray());
		}
	}
	
	public static class CZ extends Control {
		
		public CZ() {
			super("cz");
		}
		
		@Override
		protected QuantumOperationWrapper newGate(QuantumComputer qc) {
			return new QuantumOperationWrapper.CZ(qc, controls.toIntArray(), targets.toIntArray());
		}
	}
	
	public static class CH extends Control {
		
		public CH() {
			super("ch");
		}
		
		@Override
		protected QuantumOperationWrapper newGate(QuantumComputer qc) {
			return new QuantumOperationWrapper.CH(qc, controls.toIntArray(), targets.toIntArray());
		}
	}
	
	public static class CS extends Control {
		
		public CS() {
			super("cs");
		}
		
		@Override
		protected QuantumOperationWrapper newGate(QuantumComputer qc) {
			return new QuantumOperationWrapper.CS(qc, controls.toIntArray(), targets.toIntArray());
		}
	}
	
	public static class CSdg extends Control {
		
		public CSdg() {
			super("csdg");
		}
		
		@Override
		protected QuantumOperationWrapper newGate(QuantumComputer qc) {
			return new QuantumOperationWrapper.CSdg(qc, controls.toIntArray(), targets.toIntArray());
		}
	}
	
	public static class CT extends Control {
		
		public CT() {
			super("ct");
		}
		
		@Override
		protected QuantumOperationWrapper newGate(QuantumComputer qc) {
			return new QuantumOperationWrapper.CT(qc, controls.toIntArray(), targets.toIntArray());
		}
	}
	
	public static class CTdg extends Control {
		
		public CTdg() {
			super("ctdg");
		}
		
		@Override
		protected QuantumOperationWrapper newGate(QuantumComputer qc) {
			return new QuantumOperationWrapper.CTdg(qc, controls.toIntArray(), targets.toIntArray());
		}
	}
	
	public static abstract class ControlAngle extends TileQuantumComputerGate {
		
		protected double angle = 0;
		protected final IntSet controls, targets;
		
		public ControlAngle(String gateID) {
			super(gateID);
			controls = new IntRBTreeSet();
			targets = new IntRBTreeSet();
			toolMode = "getAngle";
		}
		
		@Override
		public void sendGateInfo(EntityPlayerMP player) {
			highlightQubits(player, controls);
			highlightQubits(player, targets);
			player.sendMessage(new TextComponentString(Lang.localize("info.nuclearcraft.multitool.quantum_computer.control_angle_gate_info", getTileBlockDisplayName(), intsToString(targets), NCMath.decimalPlaces(angle, 5), intsToString(controls))));
		}
		
		@Override
		public boolean onUseMultitool(ItemStack multitool, EntityPlayerMP player, World worldIn, EnumFacing facing, float hitX, float hitY, float hitZ) {
			NBTTagCompound nbt = NBTHelper.getStackNBT(multitool, "ncMultitool");
			if (nbt != null) {
				if (toolMode.equals("getAngle") && player.isSneaking()) {
					angle = 0D;
					player.sendMessage(new TextComponentString(TextFormatting.ITALIC + Lang.localize("info.nuclearcraft.multitool.quantum_computer.start_angle", getTileBlockDisplayName())));
					nbt.setString("qComputerQubitMode", "");
					nbt.setString("qComputerGateMode", "angle");
					toolMode = "setAngle";
					
					clearMultitoolGateInfo(nbt);
					return true;
				}
				else if (toolMode.equals("setAngle") && !player.isSneaking()) {
					angle = nbt.getDouble("qGateAngle");
					player.sendMessage(new TextComponentString(TextFormatting.GREEN + Lang.localize("info.nuclearcraft.multitool.quantum_computer.finish_angle", getTileBlockDisplayName(), NCMath.decimalPlaces(angle, 5))));
					nbt.setString("qComputerQubitMode", "");
					nbt.setString("qComputerGateMode", "");
					toolMode = "getControl";
					
					clearMultitoolGateInfo(nbt);
					return true;
				}
				else if (toolMode.equals("getControl") && player.isSneaking()) {
					controls.clear();
					player.sendMessage(new TextComponentString(TextFormatting.ITALIC + Lang.localize("info.nuclearcraft.multitool.quantum_computer.start_control_set", getTileBlockDisplayName())));
					nbt.setString("qComputerQubitMode", "set");
					nbt.setString("qComputerGateMode", "");
					toolMode = "setControl";
					
					clearMultitoolGateInfo(nbt);
					return true;
				}
				else if (toolMode.equals("setControl") && !player.isSneaking()) {
					NBTHelper.readIntCollection(nbt, controls, "qubitIDSet");
					highlightQubits(player, controls);
					player.sendMessage(new TextComponentString(TextFormatting.RED + Lang.localize("info.nuclearcraft.multitool.quantum_computer.finish_control_set", getTileBlockDisplayName(), intsToString(controls))));
					nbt.setString("qComputerQubitMode", "");
					nbt.setString("qComputerGateMode", "");
					toolMode = "getTarget";
					
					clearMultitoolGateInfo(nbt);
					return true;
				}
				else if (toolMode.equals("getTarget") && player.isSneaking()) {
					targets.clear();
					player.sendMessage(new TextComponentString(TextFormatting.ITALIC + Lang.localize("info.nuclearcraft.multitool.quantum_computer.start_target_set", getTileBlockDisplayName())));
					nbt.setString("qComputerQubitMode", "set");
					nbt.setString("qComputerGateMode", "");
					toolMode = "setTarget";
					
					clearMultitoolGateInfo(nbt);
					return true;
				}
				else if (toolMode.equals("setTarget") && !player.isSneaking()) {
					NBTHelper.readIntCollection(nbt, targets, "qubitIDSet");
					highlightQubits(player, targets);
					player.sendMessage(new TextComponentString(TextFormatting.BLUE + Lang.localize("info.nuclearcraft.multitool.quantum_computer.finish_target_set", getTileBlockDisplayName(), intsToString(targets))));
					nbt.setString("qComputerQubitMode", "");
					nbt.setString("qComputerGateMode", "");
					toolMode = "getAngle";
					
					clearMultitoolGateInfo(nbt);
					return true;
				}
			}
			return super.onUseMultitool(multitool, player, worldIn, facing, hitX, hitY, hitZ);
		}
		
		@Override
		public NBTTagCompound writeAll(NBTTagCompound nbt) {
			super.writeAll(nbt);
			nbt.setDouble("qGateAngle", angle);
			NBTHelper.writeIntCollection(nbt, controls, "cQubits");
			NBTHelper.writeIntCollection(nbt, targets, "tQubits");
			return nbt;
		}
		
		@Override
		public void readAll(NBTTagCompound nbt) {
			super.readAll(nbt);
			angle = nbt.getDouble("qGateAngle");
			NBTHelper.readIntCollection(nbt, controls, "cQubits");
			NBTHelper.readIntCollection(nbt, targets, "tQubits");
		}
	}
	
	public static class CP extends ControlAngle {
		
		public CP() {
			super("cp");
		}
		
		@Override
		protected QuantumOperationWrapper newGate(QuantumComputer qc) {
			return new QuantumOperationWrapper.CP(qc, angle, controls.toIntArray(), targets.toIntArray());
		}
	}
	
	public static class CRX extends ControlAngle {
		
		public CRX() {
			super("crx");
		}
		
		@Override
		protected QuantumOperationWrapper newGate(QuantumComputer qc) {
			return new QuantumOperationWrapper.CRX(qc, angle, controls.toIntArray(), targets.toIntArray());
		}
	}
	
	public static class CRY extends ControlAngle {
		
		public CRY() {
			super("cry");
		}
		
		@Override
		protected QuantumOperationWrapper newGate(QuantumComputer qc) {
			return new QuantumOperationWrapper.CRY(qc, angle, controls.toIntArray(), targets.toIntArray());
		}
	}
	
	public static class CRZ extends ControlAngle {
		
		public CRZ() {
			super("crz");
		}
		
		@Override
		protected QuantumOperationWrapper newGate(QuantumComputer qc) {
			return new QuantumOperationWrapper.CRZ(qc, angle, controls.toIntArray(), targets.toIntArray());
		}
	}
	
	public static class Swap extends TileQuantumComputerGate {
		
		protected final IntList from, to;
		
		public Swap() {
			super("swap");
			from = new IntArrayList();
			to = new IntArrayList();
			toolMode = "getFirst";
		}
		
		@Override
		protected QuantumOperationWrapper newGate(QuantumComputer qc) {
			return new QuantumOperationWrapper.Swap(qc, from.toIntArray(), to.toIntArray());
		}
		
		@Override
		public void sendGateInfo(EntityPlayerMP player) {
			highlightQubits(player, from);
			highlightQubits(player, to);
			player.sendMessage(new TextComponentString(Lang.localize("info.nuclearcraft.multitool.quantum_computer.swap_gate_info", getTileBlockDisplayName(), intsToString(from), intsToString(to))));
		}
		
		@Override
		public boolean onUseMultitool(ItemStack multitool, EntityPlayerMP player, World worldIn, EnumFacing facing, float hitX, float hitY, float hitZ) {
			NBTTagCompound nbt = NBTHelper.getStackNBT(multitool, "ncMultitool");
			if (nbt != null) {
				if (toolMode.equals("getFirst") && player.isSneaking()) {
					from.clear();
					player.sendMessage(new TextComponentString(TextFormatting.ITALIC + Lang.localize("info.nuclearcraft.multitool.quantum_computer.start_first_swap_list", getTileBlockDisplayName())));
					nbt.setString("qComputerQubitMode", "list");
					nbt.setString("qComputerGateMode", "");
					toolMode = "setFirst";
					
					clearMultitoolGateInfo(nbt);
					return true;
				}
				else if (toolMode.equals("setFirst") && !player.isSneaking()) {
					NBTHelper.readIntCollection(nbt, from, "qubitIDList");
					highlightQubits(player, from);
					player.sendMessage(new TextComponentString(TextFormatting.GOLD + Lang.localize("info.nuclearcraft.multitool.quantum_computer.finish_first_swap_list", getTileBlockDisplayName(), intsToString(from))));
					nbt.setString("qComputerQubitMode", "");
					nbt.setString("qComputerGateMode", "");
					toolMode = "getSecond";
					
					clearMultitoolGateInfo(nbt);
					return true;
				}
				else if (toolMode.equals("getSecond") && player.isSneaking()) {
					to.clear();
					player.sendMessage(new TextComponentString(TextFormatting.ITALIC + Lang.localize("info.nuclearcraft.multitool.quantum_computer.start_second_swap_list", getTileBlockDisplayName())));
					nbt.setString("qComputerQubitMode", "list");
					nbt.setString("qComputerGateMode", "");
					toolMode = "setSecond";
					
					clearMultitoolGateInfo(nbt);
					return true;
				}
				else if (toolMode.equals("setSecond") && !player.isSneaking()) {
					NBTHelper.readIntCollection(nbt, to, "qubitIDList");
					highlightQubits(player, to);
					player.sendMessage(new TextComponentString(TextFormatting.LIGHT_PURPLE + Lang.localize("info.nuclearcraft.multitool.quantum_computer.finish_second_swap_list", getTileBlockDisplayName(), intsToString(to))));
					nbt.setString("qComputerQubitMode", "");
					nbt.setString("qComputerGateMode", "");
					toolMode = "getFirst";
					
					clearMultitoolGateInfo(nbt);
					return true;
				}
			}
			return super.onUseMultitool(multitool, player, worldIn, facing, hitX, hitY, hitZ);
		}
		
		@Override
		public NBTTagCompound writeAll(NBTTagCompound nbt) {
			super.writeAll(nbt);
			NBTHelper.writeIntCollection(nbt, from, "iQubits");
			NBTHelper.writeIntCollection(nbt, to, "jQubits");
			return nbt;
		}
		
		@Override
		public void readAll(NBTTagCompound nbt) {
			super.readAll(nbt);
			NBTHelper.readIntCollection(nbt, from, "iQubits");
			NBTHelper.readIntCollection(nbt, to, "jQubits");
		}
	}
	
	public static class ControlSwap extends TileQuantumComputerGate {
		
		protected final IntSet controls;
		protected final IntList from, to;
		
		public ControlSwap() {
			super("cswap");
			controls = new IntRBTreeSet();
			from = new IntArrayList();
			to = new IntArrayList();
			toolMode = "getControl";
		}
		
		@Override
		protected QuantumOperationWrapper newGate(QuantumComputer qc) {
			return new QuantumOperationWrapper.ControlSwap(qc, controls.toIntArray(), from.toIntArray(), to.toIntArray());
		}
		
		@Override
		public void sendGateInfo(EntityPlayerMP player) {
			highlightQubits(player, controls);
			highlightQubits(player, from);
			highlightQubits(player, to);
			player.sendMessage(new TextComponentString(Lang.localize("info.nuclearcraft.multitool.quantum_computer.control_swap_gate_info", getTileBlockDisplayName(), intsToString(from), intsToString(to), intsToString(controls))));
		}
		
		@Override
		public boolean onUseMultitool(ItemStack multitool, EntityPlayerMP player, World worldIn, EnumFacing facing, float hitX, float hitY, float hitZ) {
			NBTTagCompound nbt = NBTHelper.getStackNBT(multitool, "ncMultitool");
			if (nbt != null) {
				if (toolMode.equals("getControl") && player.isSneaking()) {
					controls.clear();
					player.sendMessage(new TextComponentString(TextFormatting.ITALIC + Lang.localize("info.nuclearcraft.multitool.quantum_computer.start_control_set", getTileBlockDisplayName())));
					nbt.setString("qComputerQubitMode", "set");
					nbt.setString("qComputerGateMode", "");
					toolMode = "setControl";
					
					clearMultitoolGateInfo(nbt);
					return true;
				}
				else if (toolMode.equals("setControl") && !player.isSneaking()) {
					NBTHelper.readIntCollection(nbt, controls, "qubitIDSet");
					highlightQubits(player, controls);
					player.sendMessage(new TextComponentString(TextFormatting.RED + Lang.localize("info.nuclearcraft.multitool.quantum_computer.finish_control_set", getTileBlockDisplayName(), intsToString(controls))));
					nbt.setString("qComputerQubitMode", "");
					nbt.setString("qComputerGateMode", "");
					toolMode = "getFirst";
					
					clearMultitoolGateInfo(nbt);
					return true;
				}
				else if (toolMode.equals("getFirst") && player.isSneaking()) {
					from.clear();
					player.sendMessage(new TextComponentString(TextFormatting.ITALIC + Lang.localize("info.nuclearcraft.multitool.quantum_computer.start_first_swap_list", getTileBlockDisplayName())));
					nbt.setString("qComputerQubitMode", "list");
					nbt.setString("qComputerGateMode", "");
					toolMode = "setFirst";
					
					clearMultitoolGateInfo(nbt);
					return true;
				}
				else if (toolMode.equals("setFirst") && !player.isSneaking()) {
					NBTHelper.readIntCollection(nbt, from, "qubitIDList");
					highlightQubits(player, from);
					player.sendMessage(new TextComponentString(TextFormatting.GOLD + Lang.localize("info.nuclearcraft.multitool.quantum_computer.finish_first_swap_list", getTileBlockDisplayName(), intsToString(from))));
					nbt.setString("qComputerQubitMode", "");
					nbt.setString("qComputerGateMode", "");
					toolMode = "getSecond";
					
					clearMultitoolGateInfo(nbt);
					return true;
				}
				else if (toolMode.equals("getSecond") && player.isSneaking()) {
					to.clear();
					player.sendMessage(new TextComponentString(TextFormatting.ITALIC + Lang.localize("info.nuclearcraft.multitool.quantum_computer.start_second_swap_list", getTileBlockDisplayName())));
					nbt.setString("qComputerQubitMode", "list");
					nbt.setString("qComputerGateMode", "");
					toolMode = "setSecond";
					
					clearMultitoolGateInfo(nbt);
					return true;
				}
				else if (toolMode.equals("setSecond") && !player.isSneaking()) {
					NBTHelper.readIntCollection(nbt, to, "qubitIDList");
					highlightQubits(player, to);
					player.sendMessage(new TextComponentString(TextFormatting.LIGHT_PURPLE + Lang.localize("info.nuclearcraft.multitool.quantum_computer.finish_second_swap_list", getTileBlockDisplayName(), intsToString(to))));
					nbt.setString("qComputerQubitMode", "");
					nbt.setString("qComputerGateMode", "");
					toolMode = "getControl";
					
					clearMultitoolGateInfo(nbt);
					return true;
				}
			}
			return super.onUseMultitool(multitool, player, worldIn, facing, hitX, hitY, hitZ);
		}
		
		@Override
		public NBTTagCompound writeAll(NBTTagCompound nbt) {
			super.writeAll(nbt);
			NBTHelper.writeIntCollection(nbt, controls, "cQubits");
			NBTHelper.writeIntCollection(nbt, from, "iQubits");
			NBTHelper.writeIntCollection(nbt, to, "jQubits");
			return nbt;
		}
		
		@Override
		public void readAll(NBTTagCompound nbt) {
			super.readAll(nbt);
			NBTHelper.readIntCollection(nbt, controls, "cQubits");
			NBTHelper.readIntCollection(nbt, from, "iQubits");
			NBTHelper.readIntCollection(nbt, to, "jQubits");
		}
	}
	
	public TileQuantumComputerGate(String gateID) {
		super();
		this.gateID = gateID;
	}
	
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
			if (isMultiblockAssembled()) {
				getMultiblock().queue.add(newGate(getMultiblock()));
			}
			pulsed = true;
		}
		else if (pulsed && !getIsRedstonePowered()) {
			pulsed = false;
		}
	}
	
	@Override
	public boolean shouldRefresh(World worldIn, BlockPos posIn, IBlockState oldState, IBlockState newState) {
		return oldState != newState;
	}
	
	protected abstract QuantumOperationWrapper newGate(QuantumComputer qc);
	
	public abstract void sendGateInfo(EntityPlayerMP player);
	
	protected void highlightQubits(EntityPlayerMP player, IntCollection n) {
		QuantumComputer qc = getMultiblock();
		if (qc != null) {
			ObjectSet<BlockPos> posSet = new ObjectOpenHashSet<>();
			for (TileQuantumComputerQubit qubit : qc.getQubits()) {
				if (n.contains(qubit.id)) {
					posSet.add(qubit.getPos());
				}
			}
			BlockHighlightTracker.sendPacket(player, posSet, 5000);
		}
	}
	
	public static void clearMultitoolGateInfo(NBTTagCompound nbt) {
		nbt.setDouble("qGateAngle", 0D);
		NBTHelper.writeIntCollection(nbt, new IntRBTreeSet(), "qubitIDSet");
		NBTHelper.writeIntCollection(nbt, new IntArrayList(), "qubitIDList");
	}
	
	@Override
	public NBTTagCompound writeAll(NBTTagCompound nbt) {
		super.writeAll(nbt);
		nbt.setBoolean("pulsed", pulsed);
		nbt.setString("toolMode", toolMode);
		return nbt;
	}
	
	@Override
	public void readAll(NBTTagCompound nbt) {
		super.readAll(nbt);
		pulsed = nbt.getBoolean("pulsed");
		toolMode = nbt.getString("toolMode");
	}
}
