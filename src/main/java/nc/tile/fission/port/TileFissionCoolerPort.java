package nc.tile.fission.port;

import nc.recipe.NCRecipes;
import nc.tile.fission.TilePebbleFissionCooler;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Collections;

import static nc.util.FluidStackHelper.INGOT_BLOCK_VOLUME;

public class TileFissionCoolerPort extends TileFissionFluidPort<TileFissionCoolerPort, TilePebbleFissionCooler> {
	
	protected String coolerType, coolantName;
	
	/**
	 * Don't use this constructor!
	 */
	public TileFissionCoolerPort() {
		super("fission_cooler_port", TileFissionCoolerPort.class, INGOT_BLOCK_VOLUME, null, NCRecipes.gas_cooler);
	}
	
	public TileFissionCoolerPort(String coolerType, String coolantName) {
		this();
		this.coolerType = coolerType;
		this.coolantName = coolantName;
		tanks.get(0).setAllowedFluids(Collections.singleton(coolantName));
	}
	
	public static class Meta extends TileFissionCoolerPort {
		
		protected Meta(String coolantName) {
			super(coolantName, coolantName);
		}
		
		@Override
		public boolean shouldRefresh(World worldIn, BlockPos posIn, IBlockState oldState, IBlockState newState) {
			return oldState.getBlock() != newState.getBlock() || oldState.getBlock().getMetaFromState(oldState) != newState.getBlock().getMetaFromState(newState);
		}
	}
	
	public static class Oxygen extends Meta {
		
		public Oxygen() {
			super("oxygen");
		}
	}
	
	public static class Hydrogen extends Meta {
		
		public Hydrogen() {
			super("hydrogen");
		}
	}
	
	public static class Helium extends Meta {
		
		public Helium() {
			super("helium");
		}
	}
	
	public static class Nitrogen extends Meta {
		
		public Nitrogen() {
			super("nitrogen");
		}
	}
	
	public static class Fluorine extends Meta {
		
		public Fluorine() {
			super("fluorine");
		}
	}
	
	public static class Methane extends Meta {
		
		public Methane() {
			super("methane");
		}
	}
	
	public static class CarbonDioxide extends Meta {
		
		public CarbonDioxide() {
			super("carbon_dioxide");
		}
	}
	
	public static class CarbonMonoxide extends Meta {
		
		public CarbonMonoxide() {
			super("carbon_monoxide");
		}
	}
	
	public static class Ethene extends Meta {
		
		public Ethene() {
			super("ethene");
		}
	}
	
	public static class Ethyne extends Meta {
		
		public Ethyne() {
			super("ethyne");
		}
	}
	
	public static class Fluoromethane extends Meta {
		
		public Fluoromethane() {
			super("fluoromethane");
		}
	}
	
	public static class Ammonia extends Meta {
		
		public Ammonia() {
			super("ammonia");
		}
	}
	
	public static class Diborane extends Meta {
		
		public Diborane() {
			super("diborane");
		}
	}
	
	public static class SulfurDioxide extends Meta {
		
		public SulfurDioxide() {
			super("sulfur_dioxide");
		}
	}
	
	public static class SulfurTrioxide extends Meta {
		
		public SulfurTrioxide() {
			super("sulfur_trioxide");
		}
	}
	
	public static class SulfurHexafluoride extends Meta {
		
		public SulfurHexafluoride() {
			super("sulfur_hexafluoride");
		}
	}
	
	@Override
	public Object getFilterKey() {
		return coolerType;
	}
	
	// NBT
	
	@Override
	public NBTTagCompound writeAll(NBTTagCompound nbt) {
		super.writeAll(nbt);
		if (coolerType != null) {
			nbt.setString("coolerType", coolerType);
		}
		return nbt;
	}
	
	@Override
	public void readAll(NBTTagCompound nbt) {
		super.readAll(nbt);
		if (nbt.hasKey("coolerType")) {
			coolerType = nbt.getString("coolerType");
		}
		if (TilePebbleFissionCooler.DYN_COOLANT_NAME_MAP.containsKey(coolerType)) {
			coolantName = TilePebbleFissionCooler.DYN_COOLANT_NAME_MAP.get(coolerType);
			tanks.get(0).setAllowedFluids(Collections.singleton(coolantName));
		}
	}
}
