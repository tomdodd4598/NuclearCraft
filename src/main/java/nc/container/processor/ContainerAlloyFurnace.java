package nc.container.processor;

import nc.container.slot.*;
import nc.recipe.NCRecipes;
import nc.tile.processor.TileBasicProcessor.AlloyFurnace;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerAlloyFurnace extends ContainerBasicUpgradeProcessor<AlloyFurnace> {
	
	public ContainerAlloyFurnace(EntityPlayer player, AlloyFurnace tile) {
		super(player, tile, NCRecipes.alloy_furnace);
		
		addSlotToContainer(new SlotProcessorInput(tile, recipeHandler, 0, 46, 35));
		addSlotToContainer(new SlotProcessorInput(tile, recipeHandler, 1, 66, 35));
		
		addSlotToContainer(new SlotFurnace(player, tile, 2, 126, 35));
		
		addSlotToContainer(new SlotSpecificInput(tile, 3, 132 + info.playerInventoryX, 64 + info.playerInventoryY, speedUpgrade()));
		addSlotToContainer(new SlotSpecificInput(tile, 4, 152 + info.playerInventoryX, 64 + info.playerInventoryY, energyUpgrade()));
	}
}
