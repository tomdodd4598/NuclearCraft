package nc.util;

public class FluidHelper {
	
	public static final int BUCKET_VOLUME = 1000;
	
	public static final int ORE_VOLUME = 324;
	public static final int INGOT_VOLUME = 144;
	public static final int NUGGET_VOLUME = INGOT_VOLUME/9;
	public static final int INGOT_BLOCK_VOLUME = INGOT_VOLUME*9;
	
	public static final int FRAGMENT_VOLUME = INGOT_VOLUME/4;
	public static final int SHARD_VOLUME = INGOT_VOLUME/2;
	public static final int GEM_VOLUME = 666;
	
	public static final int GLASS_VOLUME = BUCKET_VOLUME;
	public static final int GLASS_PANE_VOLUME = (GLASS_VOLUME*6)/16;
	public static final int BRICK_VOLUME = INGOT_VOLUME;
	public static final int BRICK_BLOCK_VOLUME = BRICK_VOLUME*4;
	
	public static final int SEARED_BLOCK_VOLUME = INGOT_VOLUME*2;
	public static final int SEARED_MATERIAL_VOLUME = INGOT_VOLUME/2;
	
	public static final int SLIMEBALL_VOLUME = 250;
	
	public static final int REDSTONE_DUST_VOLUME = 100;
	public static final int REDSTONE_BLOCK_VOLUME = REDSTONE_DUST_VOLUME*9;
	public static final int GLOWSTONE_DUST_VOLUME = 250;
	public static final int GLOWSTONE_BLOCK_VOLUME = GLOWSTONE_DUST_VOLUME*4;
	public static final int COAL_DUST_VOLUME = 100;
	public static final int ENDER_PEARL_VOLUME = 250;
	
	public static final int EUM_DUST_VOLUME = 250;
	
	public static final int OXIDIZING_VOLUME = 400;
	
	public static final int PARTICLE_VOLUME = 10;
}
