package nc.multiblock.qComputer.tile;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import nc.multiblock.qComputer.QuantumComputer;
import nc.multiblock.qComputer.QuantumComputerGate;
import nc.util.Lang;
import nc.util.NBTHelper;
import nc.util.NCMath;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public abstract class TileQuantumComputerGate extends TileQuantumComputerPart {
	
	protected final String gateID;
	protected String toolMode = "";
	public boolean pulsed = false;
	
	public static abstract class Single extends TileQuantumComputerGate {
		
		protected final IntSet n;
		
		public Single(String gateID) {
			super(gateID);
			n = new IntOpenHashSet();
			toolMode = "getTarget";
		}
		
		@Override
		public boolean onUseMultitool(ItemStack multitoolStack, EntityPlayer player, World world, EnumFacing facing, float hitX, float hitY, float hitZ) {
			NBTTagCompound nbt = multitoolStack.getTagCompound();
			if (!player.isSneaking()) {
				if (toolMode.equals("getTarget")) {
					n.clear();
					player.sendMessage(new TextComponentString(Lang.localise("info.nuclearcraft.multitool.quantum_computer.start_target_set")));
					nbt.setString("qubitMode", "set");
					nbt.setString("gateMode", "");
					toolMode = "setTarget";
				}
				else if (toolMode.equals("setTarget")) {
					NBTHelper.loadIntCollection(nbt, n, "qubitIDSet");
					player.sendMessage(new TextComponentString(Lang.localise("info.nuclearcraft.multitool.quantum_computer.finish_target_set", QuantumComputer.intSetToString(n))));
					nbt.setString("qubitMode", "");
					nbt.setString("gateMode", "");
					toolMode = "getTarget";
				}
				clearMultitoolGateInfo(nbt);
			}
			else {
				player.sendMessage(new TextComponentString(Lang.localise("info.nuclearcraft.multitool.quantum_computer.single_gate_info", QuantumComputer.intSetToString(n))));
			}
			return true;
		}
		
		@Override
		public NBTTagCompound writeAll(NBTTagCompound nbt) {
			super.writeAll(nbt);
			NBTHelper.saveIntCollection(nbt, n, "nQubits");
			return nbt;
		}
		
		@Override
		public void readAll(NBTTagCompound nbt) {
			super.readAll(nbt);
			NBTHelper.loadIntCollection(nbt, n, "nQubits");
		}
	}
	
	public static class X extends Single {
		
		public X() {
			super("x");
		}
		
		@Override
		protected QuantumComputerGate newGate(QuantumComputer qc) {
			return new QuantumComputerGate.X(qc, n);
		}
	}
	
	public static class Y extends Single {
		
		public Y() {
			super("y");
		}
		
		@Override
		protected QuantumComputerGate newGate(QuantumComputer qc) {
			return new QuantumComputerGate.Y(qc, n);
		}
	}
	
	public static class Z extends Single {
		
		public Z() {
			super("z");
		}
		
		@Override
		protected QuantumComputerGate newGate(QuantumComputer qc) {
			return new QuantumComputerGate.Z(qc, n);
		}
	}
	
	public static class H extends Single {
		
		public H() {
			super("h");
		}
		
		@Override
		protected QuantumComputerGate newGate(QuantumComputer qc) {
			return new QuantumComputerGate.H(qc, n);
		}
	}
	
	public static class S extends Single {
		
		public S() {
			super("s");
		}
		
		@Override
		protected QuantumComputerGate newGate(QuantumComputer qc) {
			return new QuantumComputerGate.S(qc, n);
		}
	}
	
	public static class Sdg extends Single {
		
		public Sdg() {
			super("sdg");
		}
		
		@Override
		protected QuantumComputerGate newGate(QuantumComputer qc) {
			return new QuantumComputerGate.Sdg(qc, n);
		}
	}
	
	public static class T extends Single {
		
		public T() {
			super("t");
		}
		
		@Override
		protected QuantumComputerGate newGate(QuantumComputer qc) {
			return new QuantumComputerGate.T(qc, n);
		}
	}
	
	public static class Tdg extends Single {
		
		public Tdg() {
			super("tdg");
		}
		
		@Override
		protected QuantumComputerGate newGate(QuantumComputer qc) {
			return new QuantumComputerGate.Tdg(qc, n);
		}
	}
	
	public static abstract class SingleAngle extends Single {
		
		protected double angle = 0;
		
		public SingleAngle(String gateID) {
			super(gateID);
			toolMode = "getAngle";
		}
		
		@Override
		public boolean onUseMultitool(ItemStack multitoolStack, EntityPlayer player, World world, EnumFacing facing, float hitX, float hitY, float hitZ) {
			NBTTagCompound nbt = multitoolStack.getTagCompound();
			if (!player.isSneaking()) {
				if (toolMode.equals("getAngle")) {
					angle = 0D;
					player.sendMessage(new TextComponentString(Lang.localise("info.nuclearcraft.multitool.quantum_computer.start_angle")));
					nbt.setString("qubitMode", "");
					nbt.setString("gateMode", "angle");
					toolMode = "setAngle";
				}
				else if (toolMode.equals("setAngle")) {
					angle = nbt.getDouble("gateAngle");
					player.sendMessage(new TextComponentString(Lang.localise("info.nuclearcraft.multitool.quantum_computer.finish_angle", angle)));
					nbt.setString("qubitMode", "");
					nbt.setString("gateMode", "");
					toolMode = "getTarget";
				}
				else if (toolMode.equals("getTarget")) {
					n.clear();
					player.sendMessage(new TextComponentString(Lang.localise("info.nuclearcraft.multitool.quantum_computer.start_target_set")));
					nbt.setString("qubitMode", "set");
					nbt.setString("gateMode", "");
					toolMode = "setTarget";
				}
				else if (toolMode.equals("setTarget")) {
					NBTHelper.loadIntCollection(nbt, n, "qubitIDSet");
					player.sendMessage(new TextComponentString(Lang.localise("info.nuclearcraft.multitool.quantum_computer.finish_target_set", QuantumComputer.intSetToString(n))));
					nbt.setString("qubitMode", "");
					nbt.setString("gateMode", "");
					toolMode = "getAngle";
				}
				clearMultitoolGateInfo(nbt);
			}
			else {
				player.sendMessage(new TextComponentString(Lang.localise("info.nuclearcraft.multitool.quantum_computer.single_angle_gate_info", QuantumComputer.intSetToString(n), NCMath.decimalPlaces(angle, 5))));
			}
			return true;
		}
		
		@Override
		public NBTTagCompound writeAll(NBTTagCompound nbt) {
			super.writeAll(nbt);
			nbt.setDouble("gateAngle", angle);
			return nbt;
		}
		
		@Override
		public void readAll(NBTTagCompound nbt) {
			super.readAll(nbt);
			angle = nbt.getDouble("gateAngle");
		}
	}
	
	public static class P extends SingleAngle {
		
		public P() {
			super("p");
		}
		
		@Override
		protected QuantumComputerGate newGate(QuantumComputer qc) {
			return new QuantumComputerGate.P(qc, angle, n);
		}
	}
	
	public static class RX extends SingleAngle {
		
		public RX() {
			super("rx");
		}
		
		@Override
		protected QuantumComputerGate newGate(QuantumComputer qc) {
			return new QuantumComputerGate.RX(qc, angle, n);
		}
	}
	
	public static class RY extends SingleAngle {
		
		public RY() {
			super("ry");
		}
		
		@Override
		protected QuantumComputerGate newGate(QuantumComputer qc) {
			return new QuantumComputerGate.RY(qc, angle, n);
		}
	}
	
	public static class RZ extends SingleAngle {
		
		public RZ() {
			super("rz");
		}
		
		@Override
		protected QuantumComputerGate newGate(QuantumComputer qc) {
			return new QuantumComputerGate.RZ(qc, angle, n);
		}
	}
	
	public static abstract class Control extends TileQuantumComputerGate {
		
		protected final IntSet c, t;
		
		public Control(String gateID) {
			super(gateID);
			c = new IntOpenHashSet();
			t = new IntOpenHashSet();
			toolMode = "getControl";
		}
		
		@Override
		public boolean onUseMultitool(ItemStack multitoolStack, EntityPlayer player, World world, EnumFacing facing, float hitX, float hitY, float hitZ) {
			NBTTagCompound nbt = multitoolStack.getTagCompound();
			if (!player.isSneaking()) {
				if (toolMode.equals("getControl")) {
					c.clear();
					player.sendMessage(new TextComponentString(Lang.localise("info.nuclearcraft.multitool.quantum_computer.start_control_set")));
					nbt.setString("qubitMode", "set");
					nbt.setString("gateMode", "");
					toolMode = "setControl";
				}
				else if (toolMode.equals("setControl")) {
					NBTHelper.loadIntCollection(nbt, c, "qubitIDSet");
					player.sendMessage(new TextComponentString(Lang.localise("info.nuclearcraft.multitool.quantum_computer.finish_control_set", QuantumComputer.intSetToString(c))));
					nbt.setString("qubitMode", "");
					nbt.setString("gateMode", "");
					toolMode = "getTarget";
				}
				else if (toolMode.equals("getTarget")) {
					t.clear();
					player.sendMessage(new TextComponentString(Lang.localise("info.nuclearcraft.multitool.quantum_computer.start_target_set")));
					nbt.setString("qubitMode", "set");
					nbt.setString("gateMode", "");
					toolMode = "setTarget";
				}
				else if (toolMode.equals("setTarget")) {
					NBTHelper.loadIntCollection(nbt, t, "qubitIDSet");
					player.sendMessage(new TextComponentString(Lang.localise("info.nuclearcraft.multitool.quantum_computer.finish_target_set", QuantumComputer.intSetToString(t))));
					nbt.setString("qubitMode", "");
					nbt.setString("gateMode", "");
					toolMode = "getControl";
				}
				clearMultitoolGateInfo(nbt);
			}
			else {
				player.sendMessage(new TextComponentString(Lang.localise("info.nuclearcraft.multitool.quantum_computer.control_gate_info", QuantumComputer.intSetToString(t), QuantumComputer.intSetToString(c))));
			}
			return true;
		}
		
		@Override
		public NBTTagCompound writeAll(NBTTagCompound nbt) {
			super.writeAll(nbt);
			NBTHelper.saveIntCollection(nbt, c, "cQubits");
			NBTHelper.saveIntCollection(nbt, t, "tQubits");
			return nbt;
		}
		
		@Override
		public void readAll(NBTTagCompound nbt) {
			super.readAll(nbt);
			NBTHelper.loadIntCollection(nbt, c, "cQubits");
			NBTHelper.loadIntCollection(nbt, t, "tQubits");
		}
	}
	
	public static class CX extends Control {
		
		public CX() {
			super("cx");
		}
		
		@Override
		protected QuantumComputerGate newGate(QuantumComputer qc) {
			return new QuantumComputerGate.CX(qc, c, t);
		}
	}
	
	public static class CY extends Control {
		
		public CY() {
			super("cy");
		}
		
		@Override
		protected QuantumComputerGate newGate(QuantumComputer qc) {
			return new QuantumComputerGate.CY(qc, c, t);
		}
	}
	
	public static class CZ extends Control {
		
		public CZ() {
			super("cz");
		}
		
		@Override
		protected QuantumComputerGate newGate(QuantumComputer qc) {
			return new QuantumComputerGate.CZ(qc, c, t);
		}
	}
	
	public static class CH extends Control {
		
		public CH() {
			super("ch");
		}
		
		@Override
		protected QuantumComputerGate newGate(QuantumComputer qc) {
			return new QuantumComputerGate.CH(qc, c, t);
		}
	}
	
	public static class CS extends Control {
		
		public CS() {
			super("cs");
		}
		
		@Override
		protected QuantumComputerGate newGate(QuantumComputer qc) {
			return new QuantumComputerGate.CS(qc, c, t);
		}
	}
	
	public static class CSdg extends Control {
		
		public CSdg() {
			super("csdg");
		}
		
		@Override
		protected QuantumComputerGate newGate(QuantumComputer qc) {
			return new QuantumComputerGate.CSdg(qc, c, t);
		}
	}
	
	public static class CT extends Control {
		
		public CT() {
			super("ct");
		}
		
		@Override
		protected QuantumComputerGate newGate(QuantumComputer qc) {
			return new QuantumComputerGate.CT(qc, c, t);
		}
	}
	
	public static class CTdg extends Control {
		
		public CTdg() {
			super("ctdg");
		}
		
		@Override
		protected QuantumComputerGate newGate(QuantumComputer qc) {
			return new QuantumComputerGate.CTdg(qc, c, t);
		}
	}
	
	public static abstract class ControlAngle extends Control {
		
		protected double angle = 0;
		
		public ControlAngle(String gateID) {
			super(gateID);
			toolMode = "getAngle";
		}
		
		@Override
		public boolean onUseMultitool(ItemStack multitoolStack, EntityPlayer player, World world, EnumFacing facing, float hitX, float hitY, float hitZ) {
			NBTTagCompound nbt = multitoolStack.getTagCompound();
			if (!player.isSneaking()) {
				if (toolMode.equals("getAngle")) {
					angle = 0D;
					player.sendMessage(new TextComponentString(Lang.localise("info.nuclearcraft.multitool.quantum_computer.start_angle")));
					nbt.setString("qubitMode", "");
					nbt.setString("gateMode", "angle");
					toolMode = "setAngle";
				}
				else if (toolMode.equals("setAngle")) {
					angle = nbt.getDouble("gateAngle");
					player.sendMessage(new TextComponentString(Lang.localise("info.nuclearcraft.multitool.quantum_computer.finish_angle", angle)));
					nbt.setString("qubitMode", "");
					nbt.setString("gateMode", "");
					toolMode = "getControl";
				}
				else if (toolMode.equals("getControl")) {
					c.clear();
					player.sendMessage(new TextComponentString(Lang.localise("info.nuclearcraft.multitool.quantum_computer.start_control_set")));
					nbt.setString("qubitMode", "set");
					nbt.setString("gateMode", "");
					toolMode = "setControl";
				}
				else if (toolMode.equals("setControl")) {
					NBTHelper.loadIntCollection(nbt, c, "qubitIDSet");
					player.sendMessage(new TextComponentString(Lang.localise("info.nuclearcraft.multitool.quantum_computer.finish_control_set", QuantumComputer.intSetToString(c))));
					nbt.setString("qubitMode", "");
					nbt.setString("gateMode", "");
					toolMode = "getTarget";
				}
				else if (toolMode.equals("getTarget")) {
					t.clear();
					player.sendMessage(new TextComponentString(Lang.localise("info.nuclearcraft.multitool.quantum_computer.start_target_set")));
					nbt.setString("qubitMode", "set");
					nbt.setString("gateMode", "");
					toolMode = "setTarget";
				}
				else if (toolMode.equals("setTarget")) {
					NBTHelper.loadIntCollection(nbt, t, "qubitIDSet");
					player.sendMessage(new TextComponentString(Lang.localise("info.nuclearcraft.multitool.quantum_computer.finish_target_set", QuantumComputer.intSetToString(t))));
					nbt.setString("qubitMode", "");
					nbt.setString("gateMode", "");
					toolMode = "getAngle";
				}
				clearMultitoolGateInfo(nbt);
			}
			else {
				player.sendMessage(new TextComponentString(Lang.localise("info.nuclearcraft.multitool.quantum_computer.control_angle_gate_info", QuantumComputer.intSetToString(t), NCMath.decimalPlaces(angle, 5), QuantumComputer.intSetToString(c))));
			}
			return true;
		}
		
		@Override
		public NBTTagCompound writeAll(NBTTagCompound nbt) {
			super.writeAll(nbt);
			nbt.setDouble("gateAngle", angle);
			return nbt;
		}
		
		@Override
		public void readAll(NBTTagCompound nbt) {
			super.readAll(nbt);
			angle = nbt.getDouble("gateAngle");
		}
	}
	
	public static class CP extends ControlAngle {
		
		public CP() {
			super("cp");
		}
		
		@Override
		protected QuantumComputerGate newGate(QuantumComputer qc) {
			return new QuantumComputerGate.CP(qc, angle, c, t);
		}
	}
	
	public static class CRX extends ControlAngle {
		
		public CRX() {
			super("crx");
		}
		
		@Override
		protected QuantumComputerGate newGate(QuantumComputer qc) {
			return new QuantumComputerGate.CRX(qc, angle, c, t);
		}
	}
	
	public static class CRY extends ControlAngle {
		
		public CRY() {
			super("cry");
		}
		
		@Override
		protected QuantumComputerGate newGate(QuantumComputer qc) {
			return new QuantumComputerGate.CRY(qc, angle, c, t);
		}
	}
	
	public static class CRZ extends ControlAngle {
		
		public CRZ() {
			super("crz");
		}
		
		@Override
		protected QuantumComputerGate newGate(QuantumComputer qc) {
			return new QuantumComputerGate.CRZ(qc, angle, c, t);
		}
	}
	
	public static class Swap extends TileQuantumComputerGate {
		
		protected final IntList i, j;
		
		public Swap() {
			super("swap");
			i = new IntArrayList();
			j = new IntArrayList();
			toolMode = "getFirst";
		}
		
		@Override
		protected QuantumComputerGate newGate(QuantumComputer qc) {
			return new QuantumComputerGate.Swap(qc, i, j);
		}
		
		@Override
		public boolean onUseMultitool(ItemStack multitoolStack, EntityPlayer player, World world, EnumFacing facing, float hitX, float hitY, float hitZ) {
			NBTTagCompound nbt = multitoolStack.getTagCompound();
			if (!player.isSneaking()) {
				if (toolMode.equals("getFirst")) {
					i.clear();
					player.sendMessage(new TextComponentString(Lang.localise("info.nuclearcraft.multitool.quantum_computer.start_first_swap_list")));
					nbt.setString("qubitMode", "list");
					nbt.setString("gateMode", "");
					toolMode = "setFirst";
				}
				else if (toolMode.equals("setFirst")) {
					NBTHelper.loadIntCollection(nbt, i, "qubitIDList");
					player.sendMessage(new TextComponentString(Lang.localise("info.nuclearcraft.multitool.quantum_computer.finish_first_swap_list", QuantumComputer.intListToString(i))));
					nbt.setString("qubitMode", "");
					nbt.setString("gateMode", "");
					toolMode = "getSecond";
				}
				else if (toolMode.equals("getSecond")) {
					j.clear();
					player.sendMessage(new TextComponentString(Lang.localise("info.nuclearcraft.multitool.quantum_computer.start_second_swap_list")));
					nbt.setString("qubitMode", "list");
					nbt.setString("gateMode", "");
					toolMode = "setSecond";
				}
				else if (toolMode.equals("setSecond")) {
					NBTHelper.loadIntCollection(nbt, j, "qubitIDList");
					player.sendMessage(new TextComponentString(Lang.localise("info.nuclearcraft.multitool.quantum_computer.finish_second_swap_list", QuantumComputer.intListToString(j))));
					nbt.setString("qubitMode", "");
					nbt.setString("gateMode", "");
					toolMode = "getFirst";
				}
				clearMultitoolGateInfo(nbt);
			}
			else {
				player.sendMessage(new TextComponentString(Lang.localise("info.nuclearcraft.multitool.quantum_computer.swap_gate_info", QuantumComputer.intListToString(i), QuantumComputer.intListToString(j))));
			}
			return true;
		}
		
		@Override
		public NBTTagCompound writeAll(NBTTagCompound nbt) {
			super.writeAll(nbt);
			NBTHelper.saveIntCollection(nbt, i, "iQubits");
			NBTHelper.saveIntCollection(nbt, j, "jQubits");
			return nbt;
		}
		
		@Override
		public void readAll(NBTTagCompound nbt) {
			super.readAll(nbt);
			NBTHelper.loadIntCollection(nbt, i, "iQubits");
			NBTHelper.loadIntCollection(nbt, j, "jQubits");
		}
	}
	
	public static class ControlSwap extends TileQuantumComputerGate {
		
		protected final IntSet c;
		protected final IntList i, j;
		
		public ControlSwap() {
			super("cswap");
			c = new IntOpenHashSet();
			i = new IntArrayList();
			j = new IntArrayList();
			toolMode = "getControl";
		}
		
		@Override
		protected QuantumComputerGate newGate(QuantumComputer qc) {
			return new QuantumComputerGate.ControlSwap(qc, c, i, j);
		}
		
		@Override
		public boolean onUseMultitool(ItemStack multitoolStack, EntityPlayer player, World world, EnumFacing facing, float hitX, float hitY, float hitZ) {
			NBTTagCompound nbt = multitoolStack.getTagCompound();
			if (!player.isSneaking()) {
				if (toolMode.equals("getControl")) {
					c.clear();
					player.sendMessage(new TextComponentString(Lang.localise("info.nuclearcraft.multitool.quantum_computer.start_control_set")));
					nbt.setString("qubitMode", "set");
					nbt.setString("gateMode", "");
					toolMode = "setControl";
				}
				else if (toolMode.equals("setControl")) {
					NBTHelper.loadIntCollection(nbt, c, "qubitIDSet");
					player.sendMessage(new TextComponentString(Lang.localise("info.nuclearcraft.multitool.quantum_computer.finish_control_set", QuantumComputer.intSetToString(c))));
					nbt.setString("qubitMode", "");
					nbt.setString("gateMode", "");
					toolMode = "getFirst";
				}
				else if (toolMode.equals("getFirst")) {
					i.clear();
					player.sendMessage(new TextComponentString(Lang.localise("info.nuclearcraft.multitool.quantum_computer.start_first_swap_list")));
					nbt.setString("qubitMode", "list");
					nbt.setString("gateMode", "");
					toolMode = "setFirst";
				}
				else if (toolMode.equals("setFirst")) {
					NBTHelper.loadIntCollection(nbt, i, "qubitIDList");
					player.sendMessage(new TextComponentString(Lang.localise("info.nuclearcraft.multitool.quantum_computer.finish_first_swap_list", QuantumComputer.intListToString(i))));
					nbt.setString("qubitMode", "");
					nbt.setString("gateMode", "");
					toolMode = "getSecond";
				}
				else if (toolMode.equals("getSecond")) {
					j.clear();
					player.sendMessage(new TextComponentString(Lang.localise("info.nuclearcraft.multitool.quantum_computer.start_second_swap_list")));
					nbt.setString("qubitMode", "list");
					nbt.setString("gateMode", "");
					toolMode = "setSecond";
				}
				else if (toolMode.equals("setSecond")) {
					NBTHelper.loadIntCollection(nbt, j, "qubitIDList");
					player.sendMessage(new TextComponentString(Lang.localise("info.nuclearcraft.multitool.quantum_computer.finish_second_swap_list", QuantumComputer.intListToString(j))));
					nbt.setString("qubitMode", "");
					nbt.setString("gateMode", "");
					toolMode = "getControl";
				}
				clearMultitoolGateInfo(nbt);
			}
			else {
				player.sendMessage(new TextComponentString(Lang.localise("info.nuclearcraft.multitool.quantum_computer.control_swap_gate_info", QuantumComputer.intListToString(i), QuantumComputer.intListToString(j), QuantumComputer.intSetToString(c))));
			}
			return true;
		}
		
		@Override
		public NBTTagCompound writeAll(NBTTagCompound nbt) {
			super.writeAll(nbt);
			NBTHelper.saveIntCollection(nbt, c, "cQubits");
			NBTHelper.saveIntCollection(nbt, i, "iQubits");
			NBTHelper.saveIntCollection(nbt, j, "jQubits");
			return nbt;
		}
		
		@Override
		public void readAll(NBTTagCompound nbt) {
			super.readAll(nbt);
			NBTHelper.loadIntCollection(nbt, c, "cQubits");
			NBTHelper.loadIntCollection(nbt, i, "iQubits");
			NBTHelper.loadIntCollection(nbt, j, "jQubits");
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
	public void update() {
		super.update();
		if (!pulsed && getIsRedstonePowered()) {
			if (isMultiblockAssembled()) {
				getMultiblock().getGateQueue().add(newGate(getMultiblock()));
			}
			pulsed = true;
		}
		else if (pulsed && !getIsRedstonePowered()) {
			pulsed = false;
		}
	}
	
	protected abstract QuantumComputerGate newGate(QuantumComputer qc);
	
	public static void clearMultitoolGateInfo(NBTTagCompound nbt) {
		nbt.setDouble("gateAngle", 0D);
		NBTHelper.saveIntCollection(nbt, new IntOpenHashSet(), "qubitIDSet");
		NBTHelper.saveIntCollection(nbt, new IntArrayList(), "qubitIDList");
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
