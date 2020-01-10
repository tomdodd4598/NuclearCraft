package nc.multiblock.heatExchanger;

import nc.multiblock.Multiblock;
import nc.multiblock.MultiblockLogic;
import nc.multiblock.TileBeefBase.SyncReason;
import nc.multiblock.heatExchanger.tile.IHeatExchangerPart;
import nc.multiblock.network.HeatExchangerUpdatePacket;
import net.minecraft.nbt.NBTTagCompound;

public class HeatExchangerLogic extends MultiblockLogic<HeatExchanger, IHeatExchangerPart, HeatExchangerUpdatePacket> {
	
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
