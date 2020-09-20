package nc.multiblock.fission.tile.port;

import static nc.init.NCCoolantFluids.COOLANTS;
import static nc.util.FluidStackHelper.INGOT_BLOCK_VOLUME;
import static nc.util.PosHelper.DEFAULT_NON;

import java.util.Set;

import com.google.common.collect.Lists;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import nc.multiblock.fission.salt.tile.TileSaltFissionHeater;
import nc.multiblock.network.FissionHeaterPortUpdatePacket;
import nc.recipe.NCRecipes;
import nc.tile.ITileGui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class TileFissionHeaterPort extends TileFissionFluidPort<TileFissionHeaterPort, TileSaltFissionHeater> implements ITileGui<FissionHeaterPortUpdatePacket> {
	
	protected String coolantName;
	
	protected final Set<EntityPlayer> playersToUpdate;
	
	/** Don't use this constructor! */
	public TileFissionHeaterPort() {
		super(TileFissionHeaterPort.class, INGOT_BLOCK_VOLUME, null, NCRecipes.coolant_heater);
		
		playersToUpdate = new ObjectOpenHashSet<>();
	}
	
	public TileFissionHeaterPort(String coolantName) {
		this();
		this.coolantName = coolantName;
		tanks.get(0).setAllowedFluids(Lists.newArrayList(coolantName));
	}
	
	protected TileFissionHeaterPort(int coolant) {
		this(COOLANTS.get(coolant) + "nak");
	}
	
	public static class Standard extends TileFissionHeaterPort {
		
		public Standard() {
			super(0);
		}
	}
	
	public static class Iron extends TileFissionHeaterPort {
		
		public Iron() {
			super(1);
		}
	}
	
	public static class Redstone extends TileFissionHeaterPort {
		
		public Redstone() {
			super(2);
		}
	}
	
	public static class Quartz extends TileFissionHeaterPort {
		
		public Quartz() {
			super(3);
		}
	}
	
	public static class Obsidian extends TileFissionHeaterPort {
		
		public Obsidian() {
			super(4);
		}
	}
	
	public static class NetherBrick extends TileFissionHeaterPort {
		
		public NetherBrick() {
			super(5);
		}
	}
	
	public static class Glowstone extends TileFissionHeaterPort {
		
		public Glowstone() {
			super(6);
		}
	}
	
	public static class Lapis extends TileFissionHeaterPort {
		
		public Lapis() {
			super(7);
		}
	}
	
	public static class Gold extends TileFissionHeaterPort {
		
		public Gold() {
			super(8);
		}
	}
	
	public static class Prismarine extends TileFissionHeaterPort {
		
		public Prismarine() {
			super(9);
		}
	}
	
	public static class Slime extends TileFissionHeaterPort {
		
		public Slime() {
			super(10);
		}
	}
	
	public static class EndStone extends TileFissionHeaterPort {
		
		public EndStone() {
			super(11);
		}
	}
	
	public static class Purpur extends TileFissionHeaterPort {
		
		public Purpur() {
			super(12);
		}
	}
	
	public static class Diamond extends TileFissionHeaterPort {
		
		public Diamond() {
			super(13);
		}
	}
	
	public static class Emerald extends TileFissionHeaterPort {
		
		public Emerald() {
			super(14);
		}
	}
	
	public static class Copper extends TileFissionHeaterPort {
		
		public Copper() {
			super(15);
		}
	}
	
	public static class Tin extends TileFissionHeaterPort {
		
		public Tin() {
			super(16);
		}
	}
	
	public static class Lead extends TileFissionHeaterPort {
		
		public Lead() {
			super(17);
		}
	}
	
	public static class Boron extends TileFissionHeaterPort {
		
		public Boron() {
			super(18);
		}
	}
	
	public static class Lithium extends TileFissionHeaterPort {
		
		public Lithium() {
			super(19);
		}
	}
	
	public static class Magnesium extends TileFissionHeaterPort {
		
		public Magnesium() {
			super(20);
		}
	}
	
	public static class Manganese extends TileFissionHeaterPort {
		
		public Manganese() {
			super(21);
		}
	}
	
	public static class Aluminum extends TileFissionHeaterPort {
		
		public Aluminum() {
			super(22);
		}
	}
	
	public static class Silver extends TileFissionHeaterPort {
		
		public Silver() {
			super(23);
		}
	}
	
	public static class Fluorite extends TileFissionHeaterPort {
		
		public Fluorite() {
			super(24);
		}
	}
	
	public static class Villiaumite extends TileFissionHeaterPort {
		
		public Villiaumite() {
			super(25);
		}
	}
	
	public static class Carobbiite extends TileFissionHeaterPort {
		
		public Carobbiite() {
			super(26);
		}
	}
	
	public static class Arsenic extends TileFissionHeaterPort {
		
		public Arsenic() {
			super(27);
		}
	}
	
	public static class LiquidNitrogen extends TileFissionHeaterPort {
		
		public LiquidNitrogen() {
			super(28);
		}
	}
	
	public static class LiquidHelium extends TileFissionHeaterPort {
		
		public LiquidHelium() {
			super(29);
		}
	}
	
	public static class Enderium extends TileFissionHeaterPort {
		
		public Enderium() {
			super(30);
		}
	}
	
	public static class Cryotheum extends TileFissionHeaterPort {
		
		public Cryotheum() {
			super(31);
		}
	}
	
	@Override
	public int getTankCapacityPerConnection() {
		return 36;
	}
	
	@Override
	public int getFilterID() {
		return coolantName.hashCode();
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
			sendUpdateToListeningPlayers();
		}
	}
	
	// ITileGui
	
	@Override
	public int getGuiID() {
		return 303;
	}
	
	@Override
	public Set<EntityPlayer> getPlayersToUpdate() {
		return playersToUpdate;
	}
	
	@Override
	public FissionHeaterPortUpdatePacket getGuiUpdatePacket() {
		return new FissionHeaterPortUpdatePacket(pos, masterPortPos, getTanks(), getFilterTanks());
	}
	
	@Override
	public void onGuiPacket(FissionHeaterPortUpdatePacket message) {
		masterPortPos = message.masterPortPos;
		if (DEFAULT_NON.equals(masterPortPos) ^ masterPort == null) {
			refreshMasterPort();
		}
		for (int i = 0; i < getTanks().size(); i++) {
			getTanks().get(i).readInfo(message.tanksInfo.get(i));
		}
		for (int i = 0; i < getFilterTanks().size(); i++) {
			getFilterTanks().get(i).readInfo(message.filterTanksInfo.get(i));
		}
	}
	
	// NBT
	
	@Override
	public NBTTagCompound writeAll(NBTTagCompound nbt) {
		super.writeAll(nbt);
		nbt.setString("coolantName", coolantName);
		return nbt;
	}
	
	@Override
	public void readAll(NBTTagCompound nbt) {
		super.readAll(nbt);
		if (nbt.hasKey("coolantName")) {
			coolantName = nbt.getString("coolantName");
			tanks.get(0).setAllowedFluids(Lists.newArrayList(coolantName));
		}
	}
}
