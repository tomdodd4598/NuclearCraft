package nc.worldgen.decoration;

import static nc.config.NCConfig.*;

import net.minecraft.block.state.IBlockState;

public class MushroomGenerator extends BushGenerator {
	
	public MushroomGenerator(IBlockState bush) {
		super(bush, mushroom_gen_size, mushroom_gen_rate);
	}
	
	@Override
	public boolean shouldGenerate() {
		return mushroom_gen && super.shouldGenerate();
	}
}
