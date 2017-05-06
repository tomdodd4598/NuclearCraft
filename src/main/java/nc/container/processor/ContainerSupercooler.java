package nc.container.processor;

import nc.container.SlotSpecificInput;
import nc.crafting.processor.SupercoolerRecipes;
import nc.tile.processor.TileEnergyFluidProcessor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnaceOutput;

public class ContainerSupercooler extends ContainerEnergyFluidProcessor {

	public ContainerSupercooler(EntityPlayer player, TileEnergyFluidProcessor tileEntity) {
		super(tileEntity, SupercoolerRecipes.instance());
		
		addSlotToContainer(new SlotSpecificInput(tileEntity, 0, 132, 64, speedUpgrade));
		addSlotToContainer(new SlotFurnaceOutput(player, tileEntity, 1, 152, 64));
		
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
