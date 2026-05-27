package nc.worldgen.structure.vault;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;

import java.util.Random;

public enum VaultSectorType {
	
	STANDARD,
	DEEP;
	
	private Block getConcrete(Random randomIn) {
		return randomIn.nextInt(this == VaultSectorType.DEEP ? 4 : 8) == 0 ? Blocks.STAINED_HARDENED_CLAY : Blocks.CONCRETE;
	}
	
	public IBlockState getEntranceShaftShellState(Random randomIn) {
		return switch (this) {
			case STANDARD, DEEP -> Blocks.OBSIDIAN.getDefaultState();
		};
	}
	
	public IBlockState getEntranceHatchState(Random randomIn) {
		return switch (this) {
			case STANDARD, DEEP -> Blocks.STONEBRICK.getDefaultState();
		};
	}
	
	public IBlockState getCorridorWallState(Random randomIn) {
		return switch (this) {
			case STANDARD -> getConcrete(randomIn).getStateFromMeta(0);
			case DEEP -> getConcrete(randomIn).getStateFromMeta(8);
		};
	}
	
	public IBlockState getCorridorFloorState(Random randomIn) {
		return switch (this) {
			case STANDARD -> getConcrete(randomIn).getStateFromMeta(7);
			case DEEP -> getConcrete(randomIn).getStateFromMeta(15);
		};
	}
	
	public IBlockState getCorridorLightState(Random randomIn) {
		return switch (this) {
			case STANDARD -> Blocks.SEA_LANTERN.getDefaultState();
			case DEEP -> Blocks.REDSTONE_LAMP.getDefaultState();
		};
	}
	
	public IBlockState getLadderShaftShellState(Random randomIn) {
		return switch (this) {
			case STANDARD, DEEP -> Blocks.OBSIDIAN.getDefaultState();
		};
	}
	
	public IBlockState getStairsShellState(Random randomIn) {
		return switch (this) {
			case STANDARD, DEEP -> Blocks.OBSIDIAN.getDefaultState();
		};
	}
	
	public IBlockState getStairsStepState(Random randomIn) {
		return switch (this) {
			case STANDARD -> Blocks.QUARTZ_STAIRS.getDefaultState();
			case DEEP -> Blocks.STONE_BRICK_STAIRS.getDefaultState();
		};
	}
}
