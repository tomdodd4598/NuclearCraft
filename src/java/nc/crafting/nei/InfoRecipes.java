package nc.crafting.nei;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import nc.NuclearCraft;
import nc.block.NCBlocks;
import nc.item.NCItems;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public class InfoRecipes {
	
	private static final InfoRecipes infoBase = new InfoRecipes();
	@SuppressWarnings("rawtypes")
	private Map info = new HashMap();
	@SuppressWarnings("rawtypes")
	private Map infoTypes = new HashMap();

	public static InfoRecipes info() {
		return infoBase;
	}
	
	/*private String text(String s) {
		return StatCollector.translateToLocal(s);
	}*/
	
	private InfoRecipes() {
		
		/*addRecipe(new ItemStack(NCBlocks.fissionReactorGraphiteIdle), "-Nuclear fuel types below-Check uses in NEI-for more info:--"+text("gui.LEU")+"     "+text("gui.HEU")+"     "+text("gui.LEUOx")+"     "+text("gui.HEUOx")+"-"+text("gui.LEP")+"     "+text("gui.HEP")+"     "+text("gui.LEPOx")+"     "+text("gui.HEPOx")+"-"+text("gui.MOX")+"     "+text("gui.TBU")+"--Requires fission multiblock-structure to function and-outputs RF");
		addRecipe(new ItemStack(NCBlocks.fissionReactorSteamIdle), "-Nuclear fuel types below-Check uses in NEI-for more info:--"+text("gui.LEU")+"     "+text("gui.HEU")+"     "+text("gui.LEUOx")+"     "+text("gui.HEUOx")+"-"+text("gui.LEP")+"     "+text("gui.HEP")+"     "+text("gui.LEPOx")+"     "+text("gui.HEPOx")+"-"+text("gui.MOX")+"     "+text("gui.TBU")+"--Requires fission multiblock-structure to function and-outputs steam");
		addRecipe(new ItemStack(NCBlocks.reactionGeneratorIdle), "-Uses nuclear fuel-and Universal Reactant-as the catalyst to-generate " + (TileReactionGenerator.power) + " RF/t");
		addRecipe(new ItemStack(NCBlocks.nuclearFurnaceIdle), "-Smelts items very fast-and uses simple nuclear fuels");
		addRecipe(new ItemStack(NCBlocks.furnaceIdle), "-A more efficient furnace");
		addRecipe(new ItemStack(NCBlocks.crusherIdle), "-Uses burnable fuels to-crush items");
		addRecipe(new ItemStack(NCBlocks.electricCrusherIdle), "-Uses RF to crush items--Can accept speed and-efficiency upgrades");
		addRecipe(new ItemStack(NCBlocks.electricFurnaceIdle), "-Uses RF to smelt items-efficiently--Can accept speed and-efficiency upgrades");
		addRecipe(new ItemStack(NCBlocks.separatorIdle), "-Uses RF to separate-materials into their different-isotopes--Can accept speed and-efficiency upgrades");
		addRecipe(new ItemStack(NCBlocks.hastenerIdle), "-Uses RF to cause-radioactive materials to-decay quickly--Can accept speed and-efficiency upgrades");
		addRecipe(new ItemStack(NCBlocks.cellBlock), "-Used in the construction-of Fission Reactors--Adjacent blocks will generate-extra power and heat--If adjacent to n other Cell-Compartments, it will generate-n+1 times the power and-(n+1)(n+2)/2 times the heat");
		addRecipe(new ItemStack(NCBlocks.reactorBlock), "-Simple block that makes up-the exterior of Fission-Reactors");
		addRecipe(new ItemStack(NCBlocks.machineBlock), "-Used in the construction of-many machines");
		addRecipe(new ItemStack(NCBlocks.blastBlock), "-Three times as blast-resistant as obsidian--Can resist nuke explosions");
		addRecipe(new ItemStack(NCBlocks.nuclearWorkspace), "-An advanced 5x5-crafting table used to-make many things in-NuclearCraft" + (!NuclearCraft.workspace ? "--Currently disabled" : ""));
		addRecipe(new ItemStack(NCBlocks.fusionReactor), "-An advanced RF generator-that fuses Hydrogen, Deuterium,-Tritium, Helium3, Lithium6,-Lithium7 and Boron11 to-generate a very large-amount of RF--Requires a ring of powered-electromagnets to function");
		addRecipe(new ItemStack(NCBlocks.tubing1), "-Purely decorative block--Looks nice at the edges of-Fission Reactors");
		addRecipe(new ItemStack(NCBlocks.tubing2), "-Purely decorative block--Looks nice at the edges of-Fission Reactors");
		addRecipe(new ItemStack(NCBlocks.RTG), "-Generates a constant-stream of " + NuclearCraft.RTGRF + " RF/t");
		addRecipe(new ItemStack(NCBlocks.WRTG), "-Generates a constant-stream of " + NuclearCraft.WRTGRF + " RF/t");
		addRecipe(new ItemStack(NCBlocks.steamGenerator), "-Generates RF from steam-at a maximum rate of-2000 mB/t to 4000 RF/t-(2 RF per 1 mB of Steam)");
		addRecipe(new ItemStack(NCBlocks.steamDecompressor), "-Takes in dense steam-and decompresses it to-form steam at a maximum-rate of 2 mB/t of dense-steam to 2000 mB/t of steam");
		addRecipe(new ItemStack(NCBlocks.denseSteamDecompressor), "-Takes in superdense steam-and decompresses it to-form dense steam at a-maximum rate of 2 mB/t of-superdense steam to-2000 mB/t of dense steam");
		addRecipe(new ItemStack(NCBlocks.solarPanel), "-Generates a constant-stream of " + NuclearCraft.solarRF + " RF/t during-the day");
		addRecipe(new ItemStack(NCBlocks.collectorIdle), "-Slowly generates Helium4-from the Thorium inside it");
		addRecipe(new ItemStack(NCBlocks.nuke), "-A cruel joke, a fun time,-or just a big hole--Nuff said");
		addRecipe(new ItemStack(NCBlocks.antimatterBomb), "-Show the world what-would have happened if-Ewan McGregor did not-have a helicopter at-his disposal...");
		addRecipe(new ItemStack(NCBlocks.electrolyserIdle), "-Uses RF to separate-water into Oxygen, Hydrogen-and Deuterium--Can accept speed and-efficiency upgrades");
		addRecipe(new ItemStack(NCBlocks.oxidiserIdle), "-Uses RF to oxidise materials--Makes nuclear fuels more-efficient--Can accept speed and-efficiency upgrades");
		addRecipe(new ItemStack(NCBlocks.ioniserIdle), "-Uses RF to ionise atoms--Can accept speed and-efficiency upgrades");
		addRecipe(new ItemStack(NCBlocks.coolerIdle), "-Uses RF to cool Helium4-drastically to temperatures-of around 3 Kelvin-above absolute zero,-causing it to liquify--Can accept speed and-efficiency upgrades");
		addRecipe(new ItemStack(NCBlocks.irradiatorIdle), "-Uses RF to bathe-materials in neutron-radiation, causing changes in-nuclear structure--Can accept speed and-efficiency upgrades");
		addRecipe(new ItemStack(NCBlocks.factoryIdle), "-A very useful machine-that uses RF to create-machine parts and-efficiently process ores--Can accept speed and-efficiency upgrades");
		addRecipe(new ItemStack(NCBlocks.assemblerIdle), "-A very useful machine-that uses RF to automate-Nuclear Workspace recipes--Use sticks to block slots--Will not leave empty slots--Can accept speed and-efficiency upgrades");
		addRecipe(new ItemStack(NCBlocks.heliumExtractorIdle), "-Uses RF to carefully-extract Liquid Helium-from its cells, so that it-can be transferred as a fluid");
		addRecipe(new ItemStack(NCBlocks.superElectromagnetIdle), "-Used to control the-beams in particle-accelerators--Requires " + NuclearCraft.superElectromagnetRF + " RF/t to run-continuously");
		addRecipe(new ItemStack(NCBlocks.electromagnetIdle), "-Used to control a-Fusion Reactor's-superhot plasma--Requires " + NuclearCraft.electromagnetRF + " RF/t to run-continuously");
		addRecipe(new ItemStack(NCBlocks.supercoolerIdle), "-Used in the construction-of particle accelerators-to cool the superconducting-electromagnets--Requires " + NuclearCraft.electromagnetHe + " mB of Liquid-Helium per second to run-continuously");
		addRecipe(new ItemStack(NCBlocks.synchrotronIdle), "-Place at the corner of a-particle accelerator ring--Takes in electron cells and-fires them into the accelerator");
		addRecipe(new ItemStack(NCBlocks.simpleQuantumUp), "-A block that mimics the-probabilistic quantum-mechanical physics of a spin-1/2 particle, such as an-electron or a neutron");
		addRecipe(new ItemStack(NCBlocks.simpleQuantumDown), "-A block that mimics the-probabilistic quantum-mechanical physics of a spin-1/2 particle, such as an-electron or a neutron");
		addRecipe(new ItemStack(NCBlocks.blockHelium), "-Created by extraction-from supercooled Helium4-cells--Required for supercoolers-to cool down electromagnets-in a particle accelerator");
		addRecipe(new ItemStack(NCBlocks.blockFusionPlasma), "-Created in the Fusion-Reactor due to the incredibly-high temperatures--Will leak from electromagnets-which are not powered");
		addRecipe(new ItemStack(NCBlocks.blockSteam), "-Can be used in any-machine or generator-that uses Steam");
		
		addRecipe(new ItemStack(NCItems.toughBow), "-A better version of the Bow--Does more damage and has a-higher durability--Can be repaired in an Anvil-using Tough Alloy");
		addRecipe(new ItemStack(NCItems.pistol), "-Uses DU bullets--Deals big damage");
		addRecipe(new ItemStack(NCItems.dUBullet), "-Pistol ammo");
		addRecipe(new ItemStack(NCItems.toughAlloyPaxel), "-A multitool--Can be repaired in an Anvil-using Tough Alloy");
		addRecipe(new ItemStack(NCItems.dUPaxel), "-A multitool--Can be repaired in an Anvil-using Depleted Uranium Plating");
		addRecipe(new ItemStack(NCItems.upgradeSpeed), "-Used to increase the-speed of machines--Stacked upgrades increase the-speed exponentially");
		addRecipe(new ItemStack(NCItems.upgradeEnergy), "-Used to increase the-energy efficiency of machines--Stacked upgrades increase the-efficiency exponentially");
		addRecipe(new ItemStack(NCItems.nuclearGrenade), "-A VERY deadly bomb--It is highly recommended-that this is kept off your-hotbar while not about-to be used");
		addRecipe(new ItemStack(NCItems.portableEnderChest), "-Portable access to your-vanilla Ender Chest inventory");
		
		addRecipe(new ItemStack(NCItems.recordArea51), "-Jimmy, with his newly-aquired map, must make-his way to the mines of-Area 51 to recover his-invisibility technology...");
		addRecipe(new ItemStack(NCItems.recordNeighborhood), "-Jimmy's hometown: a quiet-and green place with-roads to many great-places such as Retroland-and Downtown...");
		addRecipe(new ItemStack(NCItems.recordPractice), "-Whenever Jimmy has some-new discoveries to test-out, his virtual practice-labs are the best places-to see what's possible...");
		*/
		fissionFuelInfo(11, NuclearCraft.baseRFLEU, NuclearCraft.baseFuelLEU, NuclearCraft.baseHeatLEU);
		fissionFuelInfo(17, NuclearCraft.baseRFLEU, NuclearCraft.baseFuelLEU, NuclearCraft.baseHeatLEU);
		fissionFuelInfo(12, NuclearCraft.baseRFHEU, NuclearCraft.baseFuelHEU, NuclearCraft.baseHeatHEU);
		fissionFuelInfo(18, NuclearCraft.baseRFHEU, NuclearCraft.baseFuelHEU, NuclearCraft.baseHeatHEU);
		fissionFuelInfo(13, NuclearCraft.baseRFLEP, NuclearCraft.baseFuelLEP, NuclearCraft.baseHeatLEP);
		fissionFuelInfo(19, NuclearCraft.baseRFLEP, NuclearCraft.baseFuelLEP, NuclearCraft.baseHeatLEP);
		fissionFuelInfo(14, NuclearCraft.baseRFHEP, NuclearCraft.baseFuelHEP, NuclearCraft.baseHeatHEP);
		fissionFuelInfo(20, NuclearCraft.baseRFHEP, NuclearCraft.baseFuelHEP, NuclearCraft.baseHeatHEP);
		fissionFuelInfo(15, NuclearCraft.baseRFMOX, NuclearCraft.baseFuelMOX, NuclearCraft.baseHeatMOX);
		fissionFuelInfo(21, NuclearCraft.baseRFMOX, NuclearCraft.baseFuelMOX, NuclearCraft.baseHeatMOX);
		fissionFuelInfo(16, NuclearCraft.baseRFTBU, NuclearCraft.baseFuelTBU, NuclearCraft.baseHeatTBU);
		
		fissionFuelInfo(59, NuclearCraft.baseRFLEUOx, NuclearCraft.baseFuelLEUOx, NuclearCraft.baseHeatLEUOx);
		fissionFuelInfo(63, NuclearCraft.baseRFLEUOx, NuclearCraft.baseFuelLEUOx, NuclearCraft.baseHeatLEUOx);
		fissionFuelInfo(60, NuclearCraft.baseRFHEUOx, NuclearCraft.baseFuelHEUOx, NuclearCraft.baseHeatHEUOx);
		fissionFuelInfo(64, NuclearCraft.baseRFHEUOx, NuclearCraft.baseFuelHEUOx, NuclearCraft.baseHeatHEUOx);
		fissionFuelInfo(61, NuclearCraft.baseRFLEPOx, NuclearCraft.baseFuelLEPOx, NuclearCraft.baseHeatLEPOx);
		fissionFuelInfo(65, NuclearCraft.baseRFLEPOx, NuclearCraft.baseFuelLEPOx, NuclearCraft.baseHeatLEPOx);
		fissionFuelInfo(62, NuclearCraft.baseRFHEPOx, NuclearCraft.baseFuelHEPOx, NuclearCraft.baseHeatHEPOx);
		fissionFuelInfo(66, NuclearCraft.baseRFHEPOx, NuclearCraft.baseFuelHEPOx, NuclearCraft.baseHeatHEPOx);
		
		addRecipe(new ItemStack(NCBlocks.graphiteBlock), "-Generates additional power-and heat in Fission Reactors");
		
		addRecipe(new ItemStack(NCBlocks.speedBlock), "-Causes nuclear fuels to-deplete faster in Fission-Reactors");
		
		coolerInfo(NCBlocks.coolerBlock, NuclearCraft.standardCool, "Doubly effective when adjacent-to another Standard Reactor-Cooler");
		coolerInfo(NCBlocks.waterCoolerBlock, NuclearCraft.waterCool, "Doubly effective when adjacent-to Reactor Casing");
		coolerInfo(NCBlocks.cryotheumCoolerBlock, NuclearCraft.cryotheumCool, "Doubly effective when not-adjacent to any other-Cryotheum Reactor Coolers");
		coolerInfo(NCBlocks.redstoneCoolerBlock, NuclearCraft.redstoneCool, "Doubly effective when adjacent-to a Fuel Cell Compartment");
		coolerInfo(NCBlocks.enderiumCoolerBlock, NuclearCraft.enderiumCool, "Doubly effective when adjacent-to Graphite");
		coolerInfo(NCBlocks.glowstoneCoolerBlock, NuclearCraft.glowstoneCool, "Quadrupally effective when-adjacent to Graphite on-all six sides");
		coolerInfo(NCBlocks.heliumCoolerBlock, NuclearCraft.heliumCool, "Not affected by its-position in the structure");
		coolerInfo(NCBlocks.coolantCoolerBlock, NuclearCraft.coolantCool, "Doubly effective when adjacent-to a Water Reactor Cooler");
		/*
		addRecipe(new ItemStack(NCItems.dominoes), "-Paul's Favourite: He'll-follow anyone he sees-carrying this in their hand...--Restores 16 hunger");
		addRecipe(new ItemStack(NCItems.boiledEgg), "-Restores 5 hunger");
		addRecipe(new ItemStack(NCItems.fuel, 1, 34), "-Right click on a water-source block with an-empty fluid cell to obtain");
		addRecipe(new ItemStack(NCItems.fuel, 1, 45), "-Right click on a water-source block to-obtain a water cell");
		
		addRecipe(new ItemStack(NCItems.ricecake), "-Healthy meal,-especially with fish--Restores 4 hunger");
		addRecipe(new ItemStack(NCItems.fishAndRicecake), "-At 8 in the morning he'll have-fish and a ricecake, at 10 he'll-have fish, at 12 he'll have fish-and a ricecake, at 2 he'll-have fish, at 4, just before he-trains, he'll have fish and a-ricecake, he'll train, he'll have-his fish, he'll come home and-have some more fish with a-ricecake and then have some-fish before he goes to bed");
		*/
		addRecipe(new ItemStack(NCItems.fuel, 1, 35), "-Used to oxidise and-improve fission fuels");
		addRecipe(new ItemStack(NCItems.fuel, 1, 36), "-Fusion fuel--Best combined with Boron11-or Lithium7");
		addRecipe(new ItemStack(NCItems.fuel, 1, 37), "-Fusion fuel--Best combined with Deuterium,-Tritium, Helium3 or Lithium6");
		addRecipe(new ItemStack(NCItems.fuel, 1, 38), "-Fusion fuel--Best combined with Deuterium-or Tritium");
		addRecipe(new ItemStack(NCItems.fuel, 1, 39), "-Fusion fuel--Best combined with Helium3-or Lithium6");
		addRecipe(new ItemStack(NCItems.fuel, 1, 41), "-Fusion fuel--Best combined with Deuterium-or Helium3");
		addRecipe(new ItemStack(NCItems.fuel, 1, 42), "-Fusion fuel--Best combined with Hydrogen");
		addRecipe(new ItemStack(NCItems.fuel, 1, 44), "-Fusion fuel--Best combined with Hydrogen");
	}

	public void fissionFuelInfo(int meta, int power, int time, int heat) {
		addRecipe(new ItemStack(NCItems.fuel, 1, meta), "Base Power = " + power*NuclearCraft.fissionRF/100 + " RF/t-Base Lifetime = " + (10000000/(time*20))*NuclearCraft.fissionEfficiency + " s-Base Heat = " + heat + " H/t-For a n*m*k Reactor with-c cells and efficiency e:-Multiply Base Power by:-c*(e/100)-Multiply Base Lifetime by:-1/c-Heat produced is determined-by the positions of cells");
	}
	
	public void coolerInfo(Block cooler, int heat, String extra) {
		addRecipe(new ItemStack(cooler), "-Removes heat from-Fission Reactors--Each block removes " + heat + " H/t--" + extra);
	}
		
	@SuppressWarnings("unchecked")
	public void addRecipe(ItemStack input, String info) {
		this.info.put(input, info);
	}

	@SuppressWarnings("unchecked")
	public void addRecipe(String type, ItemStack input, String info) {
		this.info.put(input, info);
		this.infoTypes.put(input, type);
	}

	@SuppressWarnings("rawtypes")
	public String getInfo(ItemStack stack) {
		Iterator iterator = this.info.entrySet().iterator();
		Map.Entry entry;
		do {
			if (!iterator.hasNext()) {
				return null;
			}
			entry = (Map.Entry)iterator.next();
		}
		while (!func_151397_a(stack, (ItemStack)entry.getKey()));
		return (String)entry.getValue();
	}

	private boolean func_151397_a(ItemStack p_151397_1_, ItemStack p_151397_2_) {
		return (p_151397_2_.getItem() == p_151397_1_.getItem()) && ((p_151397_2_.getItemDamage() == 32767) || (p_151397_2_.getItemDamage() == p_151397_1_.getItemDamage()));
	}
	
	@SuppressWarnings("rawtypes")
	public Map getInfoList() {
		return this.info;
	}

	@SuppressWarnings("rawtypes")
	public Map getInfoType() {
		return this.infoTypes;
	}
}