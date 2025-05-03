package nc.multiblock.machine;

import it.unimi.dsi.fastutil.objects.*;
import nc.Global;
import nc.multiblock.*;
import nc.multiblock.cuboidal.CuboidalMultiblock;
import nc.network.multiblock.*;
import nc.tile.machine.*;
import nc.tile.multiblock.TilePartAbstract.SyncReason;
import net.minecraft.client.audio.ISound;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.*;

import javax.annotation.Nonnull;
import java.util.Set;
import java.util.function.UnaryOperator;

public class Machine extends CuboidalMultiblock<Machine, IMachinePart> implements ILogicMultiblock<Machine, MachineLogic, IMachinePart>, IPacketMultiblock<Machine, IMachinePart, MachineUpdatePacket> {
	
	public static final ObjectSet<Class<? extends IMachinePart>> PART_CLASSES = new ObjectOpenHashSet<>();
	public static final Object2ObjectMap<String, UnaryOperator<MachineLogic>> LOGIC_MAP = new Object2ObjectOpenHashMap<>();
	
	protected @Nonnull MachineLogic logic = new MachineLogic(this);
	
	protected final PartSuperMap<Machine, IMachinePart> partSuperMap = new PartSuperMap<>();
	
	protected IMachineController<?> controller;
	
	public boolean isMachineOn, fullHalt;
	
	@SideOnly(Side.CLIENT)
	protected Object2ObjectMap<BlockPos, ISound> soundMap;
	public boolean refreshSounds = true;
	
	protected final Set<EntityPlayer> updatePacketListeners = new ObjectOpenHashSet<>();
	
	public Machine(World world) {
		super(world, Machine.class, IMachinePart.class);
		for (Class<? extends IMachinePart> clazz : PART_CLASSES) {
			partSuperMap.equip(clazz);
		}
	}
	
	@Override
	public @Nonnull MachineLogic getLogic() {
		return logic;
	}
	
	@Override
	public void setLogic(String logicID) {
		if (logicID.equals(logic.getID())) {
			return;
		}
		logic = getNewLogic(LOGIC_MAP.get(logicID));
	}
	
	@Override
	public PartSuperMap<Machine, IMachinePart> getPartSuperMap() {
		return partSuperMap;
	}
	
	// Multiblock Size Limits
	
	@Override
	protected int getMinimumInteriorLength() {
		return logic.getMinimumInteriorLength();
	}
	
	@Override
	protected int getMaximumInteriorLength() {
		return logic.getMaximumInteriorLength();
	}
	
	// Multiblock Methods
	
	@Override
	public void onAttachedPartWithMultiblockData(IMachinePart part, NBTTagCompound data) {
		logic.onAttachedPartWithMultiblockData(part, data);
		syncDataFrom(data, SyncReason.FullSync);
	}
	
	@Override
	protected void onBlockAdded(IMachinePart newPart) {
		onPartAdded(newPart);
		logic.onBlockAdded(newPart);
	}
	
	@Override
	protected void onBlockRemoved(IMachinePart oldPart) {
		onPartRemoved(oldPart);
		logic.onBlockRemoved(oldPart);
	}
	
	@Override
	protected void onMachineAssembled() {
		logic.onMachineAssembled();
	}
	
	@Override
	protected void onMachineRestored() {
		logic.onMachineRestored();
	}
	
	@Override
	protected void onMachinePaused() {
		logic.onMachinePaused();
	}
	
	@Override
	protected void onMachineDisassembled() {
		logic.onMachineDisassembled();
	}
	
	@Override
	protected boolean isMachineWhole() {
		return setLogic(this) && super.isMachineWhole() && logic.isMachineWhole();
	}
	
	public boolean setLogic(Machine multiblock) {
		if (getPartMap(IMachineController.class).isEmpty()) {
			multiblock.setLastError(Global.MOD_ID + ".multiblock_validation.no_controller", null);
			return false;
		}
		if (getPartCount(IMachineController.class) > 1) {
			multiblock.setLastError(Global.MOD_ID + ".multiblock_validation.too_many_controllers", null);
			return false;
		}
		
		for (IMachineController<?> contr : getParts(IMachineController.class)) {
			controller = contr;
			break;
		}
		
		setLogic(controller.getLogicID());
		
		return true;
	}
	
	@Override
	protected void onAssimilate(Machine assimilated) {
		logic.onAssimilate(assimilated);
	}
	
	@Override
	protected void onAssimilated(Machine assimilator) {
		logic.onAssimilated(assimilator);
	}
	
	// Server
	
	@Override
	protected boolean updateServer() {
		boolean flag = false;
		if (logic.onUpdateServer()) {
			flag = true;
		}
		return flag;
	}
	
	// Client
	
	@Override
	protected void updateClient() {
		logic.onUpdateClient();
	}
	
	// NBT
	
	@Override
	public void syncDataTo(NBTTagCompound data, SyncReason syncReason) {
		data.setBoolean("isMachineOn", isMachineOn);
		data.setBoolean("fullHalt", fullHalt);
		
		writeLogicNBT(data, syncReason);
	}
	
	@Override
	public void syncDataFrom(NBTTagCompound data, SyncReason syncReason) {
		isMachineOn = data.getBoolean("isMachineOn");
		fullHalt = data.getBoolean("fullHalt");
		
		readLogicNBT(data, syncReason);
	}
	
	// Packets
	
	@Override
	public Set<EntityPlayer> getMultiblockUpdatePacketListeners() {
		return updatePacketListeners;
	}
	
	@Override
	public MachineUpdatePacket getMultiblockUpdatePacket() {
		return logic.getMultiblockUpdatePacket();
	}
	
	@Override
	public void onMultiblockUpdatePacket(MachineUpdatePacket message) {
		isMachineOn = message.isMachineOn;
		logic.onMultiblockUpdatePacket(message);
	}
	
	protected MachineRenderPacket getRenderPacket() {
		return logic.getRenderPacket();
	}
	
	public void onRenderPacket(MachineRenderPacket message) {
		isMachineOn = message.isMachineOn;
		logic.onRenderPacket(message);
	}
	
	public void sendRenderPacketToPlayer(EntityPlayer player) {
		if (WORLD.isRemote) {
			return;
		}
		MachineRenderPacket packet = getRenderPacket();
		if (packet == null) {
			return;
		}
		packet.sendTo(player);
	}
	
	public void sendRenderPacketToAll() {
		if (WORLD.isRemote) {
			return;
		}
		MachineRenderPacket packet = getRenderPacket();
		if (packet == null) {
			return;
		}
		packet.sendToAll();
	}
	
	// Multiblock Validators
	
	@Override
	protected boolean isBlockGoodForInterior(World world, BlockPos pos) {
		return logic.isBlockGoodForInterior(world, pos);
	}
	
	// Clear Material
	
	@Override
	public void clearAllMaterial() {
		logic.clearAllMaterial();
		super.clearAllMaterial();
	}
}
