package nc.block.plant;

import nc.init.NCBlocks;
import net.minecraft.block.material.MapColor;

public class BlockHugeGlowingMushroom extends NCBlockHugeMushroom {
	
	public BlockHugeGlowingMushroom() {
		super(MapColor.GOLD, NCBlocks.glowing_mushroom, 1);
		setLightLevel(1F);
	}
}
