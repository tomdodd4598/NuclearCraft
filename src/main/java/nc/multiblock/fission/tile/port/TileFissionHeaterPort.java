package nc.multiblock.fission.tile.port;

import static nc.init.NCCoolantFluids.COOLANTS;
import static nc.util.FluidStackHelper.INGOT_BLOCK_VOLUME;
import static nc.util.PosHelper.DEFAULT_NON;

import java.util.Set;

import com.google.common.collect.Lists;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import nc.multiblock.fission.salt.tile.TileSaltFissionHeater;
import nc.network.multiblock.FissionHeaterPortUpdatePacket;
import nc.recipe.NCRecipes;
import nc.tile.ITileGui;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileFissionHeaterPort extends TileFissionFluidPort<TileFissionHeaterPort, TileSaltFissionHeater> implements ITileGui<FissionHeaterPortUpdatePacket> {
	
	protected String heaterType, coolantName;
	
	protected final Set<EntityPlayer> updatePacketListeners;
	
	/** Don't use this constructor! */
	public TileFissionHeaterPort() {
		super(TileFissionHeaterPort.class, INGOT_BLOCK_VOLUME, null, NCRecipes.coolant_heater);
		
		updatePacketListeners = new ObjectOpenHashSet<>();
	}
	
	public TileFissionHeaterPort(String heaterType, String coolantName) {
		this();
		this.heaterType = heaterType;
		this.coolantName = coolantName;
		tanks.get(0).setAllowedFluids(Lists.newArrayList(coolantName));
	}
	
	protected static class Meta extends TileFissionHeaterPort {
		
		protected Meta(String heaterType, String coolantName) {
			super(heaterType, coolantName);
		}
		
		protected Meta(int coolantID) {
			super(COOLANTS.get(coolantID), COOLANTS.get(coolantID) + "_nak");
		}
		
		@Override
		public boolean shouldRefresh(World worldIn, BlockPos posIn, IBlockState oldState, IBlockState newState) {
			return oldState.getBlock() != newState.getBlock() || oldState.getBlock().getMetaFromState(oldState) != newState.getBlock().getMetaFromState(newState);
		}
	}
	
	public static class Standard extends Meta {
		
		public Standard() {
			super("standard", "nak");
		}
	}
	
	public static class Iron extends Meta {
		
		public Iron() {
			super(1);
		}
	}
	
	public static class Redstone extends Meta {
		
		public Redstone() {
			super(2);
		}
	}
	
	public static class Quartz extends Meta {
		
		public Quartz() {
			super(3);
		}
	}
	
	public static class Obsidian extends Meta {
		
		public Obsidian() {
			super(4);
		}
	}
	
	public static class NetherBrick extends Meta {
		
		public NetherBrick() {
			super(5);
		}
	}
	
	public static class Glowstone extends Meta {
		
		public Glowstone() {
			super(6);
		}
	}
	
	public static class Lapis extends Meta {
		
		public Lapis() {
			super(7);
		}
	}
	
	public static class Gold extends Meta {
		
		public Gold() {
			super(8);
		}
	}
	
	public static class Prismarine extends Meta {
		
		public Prismarine() {
			super(9);
		}
	}
	
	public static class Slime extends Meta {
		
		public Slime() {
			super(10);
		}
	}
	
	public static class EndStone extends Meta {
		
		public EndStone() {
			super(11);
		}
	}
	
	public static class Purpur extends Meta {
		
		public Purpur() {
			super(12);
		}
	}
	
	public static class Diamond extends Meta {
		
		public Diamond() {
			super(13);
		}
	}
	
	public static class Emerald extends Meta {
		
		public Emerald() {
			super(14);
		}
	}
	
	public static class Copper extends Meta {
		
		public Copper() {
			super(15);
		}
	}
	
	public static class Tin extends Meta {
		
		public Tin() {
			super(16);
		}
	}
	
	public static class Lead extends Meta {
		
		public Lead() {
			super(17);
		}
	}
	
	public static class Boron extends Meta {
		
		public Boron() {
			super(18);
		}
	}
	
	public static class Lithium extends Meta {
		
		public Lithium() {
			super(19);
		}
	}
	
	public static class Magnesium extends Meta {
		
		public Magnesium() {
			super(20);
		}
	}
	
	public static class Manganese extends Meta {
		
		public Manganese() {
			super(21);
		}
	}
	
	public static class Aluminum extends Meta {
		
		public Aluminum() {
			super(22);
		}
	}
	
	public static class Silver extends Meta {
		
		public Silver() {
			super(23);
		}
	}
	
	public static class Fluorite extends Meta {
		
		public Fluorite() {
			super(24);
		}
	}
	
	public static class Villiaumite extends Meta {
		
		public Villiaumite() {
			super(25);
		}
	}
	
	public static class Carobbiite extends Meta {
		
		public Carobbiite() {
			super(26);
		}
	}
	
	public static class Arsenic extends Meta {
		
		public Arsenic() {
			super(27);
		}
	}
	
	public static class LiquidNitrogen extends Meta {
		
		public LiquidNitrogen() {
			super(28);
		}
	}
	
	public static class LiquidHelium extends Meta {
		
		public LiquidHelium() {
			super(29);
		}
	}
	
	public static class Enderium extends Meta {
		
		public Enderium() {
			super(30);
		}
	}
	
	public static class Cryotheum extends Meta {
		
		public Cryotheum() {
			super(31);
		}
	}
	
	@Override
	public int getTankCapacityPerConnection() {
		return 36;
	}
	
	@Override
	public Object getFilterKey() {
		return heaterType;
	}
	
	@Override
	public boolean hasConfigurableFluidConnections() {
		return true;
	}
	
	// Ticking
	
	@Override
	public void update() {
		super.update();
		if (!world.isRemote) {
			sendTileUpdatePacketToListeners();
		}
	}
	
	// ITileGui
	
	@Override
	public int getGuiID() {
		return 303;
	}
	
	@Override
	public Set<EntityPlayer> getTileUpdatePacketListeners() {
		return updatePacketListeners;
	}
	
	@Override
	public FissionHeaterPortUpdatePacket getTileUpdatePacket() {
		return new FissionHeaterPortUpdatePacket(pos, masterPortPos, getTanks(), getFilterTanks());
	}
	
	@Override
	public void onTileUpdatePacket(FissionHeaterPortUpdatePacket message) {
		masterPortPos = message.masterPortPos;
		if (DEFAULT_NON.equals(masterPortPos) ^ masterPort == null) {
			refreshMasterPort();
		}
		for (int i = 0; i < getTanks().size(); ++i) {
			getTanks().get(i).readInfo(message.tanksInfo.get(i));
		}
		for (int i = 0; i < getFilterTanks().size(); ++i) {
			getFilterTanks().get(i).readInfo(message.filterTanksInfo.get(i));
		}
	}
	
	// NBT
	
	@Override
	public NBTTagCompound writeAll(NBTTagCompound nbt) {
		super.writeAll(nbt);
		if (heaterType != null) {
			nbt.setString("heaterName", heaterType);
		}
		nbt.setString("coolantName", coolantName);
		return nbt;
	}
	
	@Override
	public void readAll(NBTTagCompound nbt) {
		super.readAll(nbt);
		if (nbt.hasKey("heaterName")) {
			heaterType = nbt.getString("heaterName");
		}
		if (nbt.hasKey("coolantName")) {
			coolantName = nbt.getString("coolantName");
			tanks.get(0).setAllowedFluids(Lists.newArrayList(coolantName));
		}
	}
}
