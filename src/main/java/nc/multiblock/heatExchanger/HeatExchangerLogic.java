package nc.multiblock.heatExchanger;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.Lists;

import nc.Global;
import nc.multiblock.*;
import nc.multiblock.heatExchanger.tile.*;
import nc.multiblock.tile.TileBeefAbstract.SyncReason;
import nc.network.multiblock.HeatExchangerUpdatePacket;
import net.minecraft.nbt.NBTTagCompound;

public class HeatExchangerLogic extends MultiblockLogic<HeatExchanger, HeatExchangerLogic, IHeatExchangerPart> implements IPacketMultiblockLogic<HeatExchanger, HeatExchangerLogic, IHeatExchangerPart, HeatExchangerUpdatePacket> {
	
	public HeatExchangerLogic(HeatExchanger exchanger) {
		super(exchanger);
	}
	
	public HeatExchangerLogic(HeatExchangerLogic oldLogic) {
		super(oldLogic);
	}
	
	@Override
	public String getID() {
		return "heat_exchanger";
	}
	
	protected HeatExchanger getExchanger() {
		return multiblock;
	}
	
	// Multiblock Size Limits
	
	@Override
	public int getMinimumInteriorLength() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public int getMaximumInteriorLength() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	// Multiblock Methods
	
	@Override
	public void onMachineAssembled() {
		onExchangerFormed();
	}
	
	@Override
	public void onMachineRestored() {
		onExchangerFormed();
	}
	
	protected void onExchangerFormed() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onMachinePaused() {
		onExchangerBroken();
	}
	
	@Override
	public void onMachineDisassembled() {
		onExchangerBroken();
	}
	
	public void onExchangerBroken() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public boolean isMachineWhole() {
		return !containsBlacklistedPart();
	}
	
	public static final List<Pair<Class<? extends IHeatExchangerPart>, String>> EXCHANGER_PART_BLACKLIST = Lists.newArrayList(Pair.of(TileCondenserTube.class, Global.MOD_ID + ".multiblock_validation.heat_exchanger.prohibit_condenser_tubes"));
	
	@Override
	public List<Pair<Class<? extends IHeatExchangerPart>, String>> getPartBlacklist() {
		return EXCHANGER_PART_BLACKLIST;
	}
	
	@Override
	public void onAssimilate(HeatExchanger assimilated) {
		/*if (getExchanger().isAssembled()) {
			onExchangerFormed();
		}
		else {
			onExchangerBroken();
		}*/
	}
	
	@Override
	public void onAssimilated(HeatExchanger assimilator) {}
	
	// Server
	
	@Override
	public boolean onUpdateServer() {
		return false;
	}
	
	// Client
	
	@Override
	public void onUpdateClient() {}
	
	// NBT
	
	@Override
	public void writeToLogicTag(NBTTagCompound data, SyncReason syncReason) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void readFromLogicTag(NBTTagCompound data, SyncReason syncReason) {
		// TODO Auto-generated method stub
		
	}
	
	// Packets
	
	@Override
	public HeatExchangerUpdatePacket getMultiblockUpdatePacket() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onMultiblockUpdatePacket(HeatExchangerUpdatePacket message) {
		// TODO Auto-generated method stub
		
	}
	
	// Clear Material
	
	@Override
	public void clearAllMaterial() {
		// TODO Auto-generated method stub
		
	}
}
