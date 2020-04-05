package nc.multiblock.heatExchanger;

import nc.multiblock.Multiblock;
import nc.multiblock.MultiblockLogic;
import nc.multiblock.heatExchanger.tile.IHeatExchangerPart;
import nc.multiblock.network.HeatExchangerUpdatePacket;
import nc.multiblock.tile.TileBeefAbstract.SyncReason;
import net.minecraft.nbt.NBTTagCompound;

public class HeatExchangerLogic extends MultiblockLogic<HeatExchanger, HeatExchangerLogic, IHeatExchangerPart, HeatExchangerUpdatePacket> {
	
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
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onMachineRestored() {
		// TODO Auto-generated method stub
		
	}
	
	protected void onExchangerFormed() {
		
	}
	
	@Override
	public void onMachinePaused() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onMachineDisassembled() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public boolean isMachineWhole(Multiblock multiblock) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public void onAssimilate(Multiblock assimilated) {
		if (!(assimilated instanceof HeatExchanger)) return;
		onExchangerFormed();
	}
	
	public void onAssimilated(Multiblock assimilator) {}
	
	// Server
	
	// Client
	
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
	public HeatExchangerUpdatePacket getUpdatePacket() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onPacket(HeatExchangerUpdatePacket message) {
		// TODO Auto-generated method stub
		
	}
}
