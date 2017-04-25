package nc.container.energy.processor;

import nc.container.SlotProcessorInput;
import nc.container.SlotSpecificInput;
import nc.crafting.processor.AlloyFurnaceRecipes;
import nc.tile.processor.TileEnergyProcessor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnaceOutput;

public class ContainerAlloyFurnace extends ContainerEnergyProcessor {

	public ContainerAlloyFurnace(EntityPlayer player, TileEnergyProcessor tileEntity) {
		super(tileEntity, AlloyFurnaceRecipes.instance());
		
		addSlotToContainer(new SlotProcessorInput(tileEntity, recipes, 0, 46, 35));
		addSlotToContainer(new SlotProcessorInput(tileEntity, recipes, 1, 66, 35));
		
		addSlotToContainer(new SlotFurnaceOutput(player, tileEntity, 2, 126, 35));
		
		addSlotToContainer(new SlotSpecificInput(tileEntity, 3, 132, 64, speedUpgrade));
		addSlotToContainer(new SlotFurnaceOutput(player, tileEntity, 4, 152, 64));
		
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				addSlotToContainer(new Slot(player.inventory, j + 9*i + 9, 8 + 18*j, 84 + 18*i));
			}
		}
		
		for (int i = 0; i < 9; i++) {
			addSlotToContainer(new Slot(player.inventory, i, 8 + 18*i, 142));
		}
	}
}
