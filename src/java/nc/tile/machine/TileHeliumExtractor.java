package nc.tile.machine;
 
import nc.NuclearCraft;
import nc.block.machine.BlockHeliumExtractor;
import nc.crafting.machine.HeliumExtractorRecipes;

public class TileHeliumExtractor extends TileMachineFluidOut {
	
	public TileHeliumExtractor() {
		super("heliumExtractor", 250000, 16000, 1, 1, false, true, 400, 16000, NuclearCraft.liquidHelium, 1000, NuclearCraft.heliumExtractorSpeed, NuclearCraft.heliumExtractorEfficiency, HeliumExtractorRecipes.instance());
	}
	
	public void updateEntity() {
		super.updateEntity();
		if (flag != flag1) {
			flag1 = flag;
			BlockHeliumExtractor.updateBlockState(flag, worldObj, xCoord, yCoord, zCoord);
		}
		markDirty();
	}
}