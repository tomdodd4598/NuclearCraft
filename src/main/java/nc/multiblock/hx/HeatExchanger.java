package nc.multiblock.hx;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.objects.*;
import nc.Global;
import nc.multiblock.*;
import nc.multiblock.cuboidal.CuboidalMultiblock;
import nc.network.multiblock.*;
import nc.recipe.*;
import nc.tile.hx.*;
import nc.tile.internal.fluid.Tank;
import nc.tile.multiblock.TilePartAbstract.SyncReason;
import nc.util.PosHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.*;
import java.util.*;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

public class HeatExchanger extends CuboidalMultiblock<HeatExchanger, IHeatExchangerPart> implements ILogicMultiblock<HeatExchanger, HeatExchangerLogic, IHeatExchangerPart>, IPacketMultiblock<HeatExchanger, IHeatExchangerPart, HeatExchangerUpdatePacket> {
	
	public static final ObjectSet<Class<? extends IHeatExchangerPart>> PART_CLASSES = new ObjectOpenHashSet<>();
	public static final Object2ObjectMap<String, UnaryOperator<HeatExchangerLogic>> LOGIC_MAP = new Object2ObjectOpenHashMap<>();
	
	protected @Nonnull HeatExchangerLogic logic = new HeatExchangerLogic(this);
	
	protected final PartSuperMap<HeatExchanger, IHeatExchangerPart> partSuperMap = new PartSuperMap<>();
	
	protected IHeatExchangerController<?> controller;
	
	public boolean refreshFlag = false;
	public int packetFlag = 0;
	
	public double shellSpeedMultiplier = 0D;
	
	public @Nullable TileHeatExchangerInlet masterShellInlet;
	
	public final ObjectSet<HeatExchangerTubeNetwork> networks = new ObjectOpenHashSet<>();
	
	public static final int BASE_MAX_INPUT = 4000, BASE_MAX_OUTPUT = 16000;
	
	public final @Nonnull List<Tank> shellTanks = Lists.newArrayList(new Tank(BASE_MAX_INPUT, NCRecipes.getValidFluids("heat_exchanger").get(0)), new Tank(BASE_MAX_OUTPUT, null));
	
	public RecipeInfo<BasicRecipe> shellRecipe;
	
	public boolean isExchangerOn, computerActivated;
	
	public int totalNetworkCount = 0, activeNetworkCount = 0;
	public int activeTubeCount = 0, activeContactCount = 0;
	public double tubeInputRate = 0D, tubeInputRateFP = 0D;
	public double shellInputRate = 0D, shellInputRateFP = 0D;
	public double heatTransferRate = 0D, heatTransferRateFP = 0D;
	public double heatDissipationRate = 0D, heatDissipationRateFP = 0D;
	public double totalTempDiff = 0D;
	
	protected final Set<EntityPlayer> updatePacketListeners = new ObjectOpenHashSet<>();
	
	public HeatExchanger(World world) {
		super(world, HeatExchanger.class, IHeatExchangerPart.class);
		for (Class<? extends IHeatExchangerPart> clazz : PART_CLASSES) {
			partSuperMap.equip(clazz);
		}
	}
	
	@Override
	public @Nonnull HeatExchangerLogic getLogic() {
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
	public PartSuperMap<HeatExchanger, IHeatExchangerPart> getPartSuperMap() {
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
	public void onAttachedPartWithMultiblockData(IHeatExchangerPart part, NBTTagCompound data) {
		logic.onAttachedPartWithMultiblockData(part, data);
		syncDataFrom(data, SyncReason.FullSync);
	}
	
	@Override
	protected void onBlockAdded(IHeatExchangerPart newPart) {
		onPartAdded(newPart);
		logic.onBlockAdded(newPart);
	}
	
	@Override
	protected void onBlockRemoved(IHeatExchangerPart oldPart) {
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
	
	public boolean setLogic(HeatExchanger multiblock) {
		if (getPartMap(IHeatExchangerController.class).isEmpty()) {
			multiblock.setLastError(Global.MOD_ID + ".multiblock_validation.no_controller", null);
			return false;
		}
		if (getPartCount(IHeatExchangerController.class) > 1) {
			multiblock.setLastError(Global.MOD_ID + ".multiblock_validation.too_many_controllers", null);
			return false;
		}
		
		for (IHeatExchangerController<?> contr : getParts(IHeatExchangerController.class)) {
			controller = contr;
			break;
		}
		
		setLogic(controller.getLogicID());
		
		return true;
	}
	
	@Override
	protected void onAssimilate(HeatExchanger assimilated) {
		logic.onAssimilate(assimilated);
	}
	
	@Override
	protected void onAssimilated(HeatExchanger assimilator) {
		logic.onAssimilated(assimilator);
	}
	
	// Server
	
	@Override
	protected boolean updateServer() {
		return logic.onUpdateServer();
	}
	
	public BlockPos getMasterShellInletPos() {
		return masterShellInlet == null ? PosHelper.DEFAULT_NON : masterShellInlet.getPos();
	}
	
	public Stream<TileHeatExchangerInlet> getMasterInlets() {
		return Stream.concat(networks.stream().map(x -> x.masterInlet), Stream.of(masterShellInlet)).filter(Objects::nonNull);
	}
	
	// Client
	
	@Override
	protected void updateClient() {
		logic.onUpdateClient();
	}
	
	// NBT
	
	@Override
	public void syncDataTo(NBTTagCompound data, SyncReason syncReason) {
		data.setBoolean("isExchangerOn", isExchangerOn);
		data.setBoolean("computerActivated", computerActivated);
		
		writeTanks(shellTanks, data, "shellTanks");
		
		writeLogicNBT(data, syncReason);
	}
	
	@Override
	public void syncDataFrom(NBTTagCompound data, SyncReason syncReason) {
		isExchangerOn = data.getBoolean("isExchangerOn");
		computerActivated = data.getBoolean("computerActivated");
		
		readTanks(shellTanks, data, "shellTanks");
		
		readLogicNBT(data, syncReason);
	}
	
	// Packets
	
	@Override
	public Set<EntityPlayer> getMultiblockUpdatePacketListeners() {
		return updatePacketListeners;
	}
	
	@Override
	public HeatExchangerUpdatePacket getMultiblockUpdatePacket() {
		return logic.getMultiblockUpdatePacket();
	}
	
	@Override
	public void onMultiblockUpdatePacket(HeatExchangerUpdatePacket message) {
		logic.onMultiblockUpdatePacket(message);
	}
	
	protected HeatExchangerRenderPacket getRenderPacket() {
		return logic.getRenderPacket();
	}
	
	public void onRenderPacket(HeatExchangerRenderPacket message) {
		logic.onRenderPacket(message);
	}
	
	public void sendRenderPacketToPlayer(EntityPlayer player) {
		if (WORLD.isRemote) {
			return;
		}
		HeatExchangerRenderPacket packet = getRenderPacket();
		if (packet == null) {
			return;
		}
		packet.sendTo(player);
	}
	
	public void sendRenderPacketToAll() {
		if (WORLD.isRemote) {
			return;
		}
		HeatExchangerRenderPacket packet = getRenderPacket();
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
		
		for (Tank tank : shellTanks) {
			tank.setFluidStored(null);
		}
	}
}
