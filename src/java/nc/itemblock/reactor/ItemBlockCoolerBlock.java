package nc.itemblock.reactor;

import nc.NuclearCraft;
import nc.block.NCBlocks;
import nc.itemblock.ItemBlockNC;
import net.minecraft.block.Block;

public class ItemBlockCoolerBlock extends ItemBlockNC {
	
	Block block;

	public ItemBlockCoolerBlock(Block b) {
		super(b, coolerInfo(b));
		block = b;
	}
	
	public static String[] coolerInfo(Block block) {
		String[] inf = {
			"Removes heat from Fission Reactors.",
			"Each block removes " + coolerHeat(block) + " H/t",
			"",
			extraInfo(block)[0],
			extraInfo(block)[1]
		};
		return inf;
	}
	
	public static int coolerHeat(Block block) {
		if (block == NCBlocks.coolerBlock) return NuclearCraft.standardCool;
		else if (block == NCBlocks.waterCoolerBlock) return NuclearCraft.waterCool;
		else if (block == NCBlocks.cryotheumCoolerBlock) return NuclearCraft.cryotheumCool;
		else if (block == NCBlocks.redstoneCoolerBlock) return NuclearCraft.redstoneCool;
		else if (block == NCBlocks.enderiumCoolerBlock) return NuclearCraft.enderiumCool;
		else if (block == NCBlocks.glowstoneCoolerBlock) return NuclearCraft.glowstoneCool;
		else if (block == NCBlocks.heliumCoolerBlock) return NuclearCraft.heliumCool;
		else if (block == NCBlocks.coolantCoolerBlock) return NuclearCraft.coolantCool;
		else return 0;
	}
	
	public static String[] extraInfo(Block block) {
		if (block == NCBlocks.coolerBlock) return new String[] {
			"Doubly effective when adjacent to",
			"another Standard Reactor Cooler."
		};
		else if (block == NCBlocks.waterCoolerBlock) return new String[] {
			"Doubly effective when adjacent to at",
			"least one Reactor Casing."
		};
		else if (block == NCBlocks.cryotheumCoolerBlock) return new String[] {
			"Doubly effective when not adjacent to",
			"any other Cryotheum Reactor Coolers."
		};
		else if (block == NCBlocks.redstoneCoolerBlock) return new String[] {
			"Doubly effective when adjacent to at",
			"least one Fuel Cell Compartment."
		};
		else if (block == NCBlocks.enderiumCoolerBlock) return new String[] {
			"Doubly effective when adjacent to at",
			"least one Graphite block."
		};
		else if (block == NCBlocks.glowstoneCoolerBlock) return new String[] {
			"Quadrupally effective when adjacent",
			"to Graphite blocks on all six sides."
		};
		else if (block == NCBlocks.heliumCoolerBlock) return new String[] {
			"Not affected by its position in the",
			"Fission Reactor's structure."
		};
		else if (block == NCBlocks.coolantCoolerBlock) return new String[] {
			"Doubly effective when adjacent to at",
			"least one Water Reactor Cooler."
		};
		else return new String[] {
				"",
				""
			};
	}
}
