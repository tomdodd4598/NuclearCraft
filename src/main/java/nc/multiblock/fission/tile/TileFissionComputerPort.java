package nc.multiblock.fission.tile;

import java.util.*;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.objects.*;
import li.cil.oc.api.machine.*;
import li.cil.oc.api.network.SimpleComponent;
import nc.multiblock.cuboidal.CuboidalPartPositionType;
import nc.multiblock.fission.*;
import nc.multiblock.fission.salt.tile.*;
import nc.multiblock.fission.solid.tile.*;
import nc.util.StackHelper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Optional;

@Optional.Interface(iface = "li.cil.oc.api.network.SimpleComponent", modid = "opencomputers")
public class TileFissionComputerPort extends TileFissionPart implements SimpleComponent {
	
	public TileFissionComputerPort() {
		super(CuboidalPartPositionType.WALL);
	}
	
	@Override
	public void onMachineAssembled(FissionReactor controller) {
		doStandardNullControllerResponse(controller);
		super.onMachineAssembled(controller);
	}
	
	@Override
	public void onMachineBroken() {
		super.onMachineBroken();
	}
	
	// OpenComputers
	
	@Override
	@Optional.Method(modid = "opencomputers")
	public String getComponentName() {
		return "nc_salt_fission_reactor";
	}
	
	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] isComplete(Context context, Arguments args) {
		return new Object[] {isMultiblockAssembled()};
	}
	
	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] isReactorOn(Context context, Arguments args) {
		return new Object[] {isMultiblockAssembled() ? getMultiblock().isReactorOn : false};
	}
	
	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] getLengthX(Context context, Arguments args) {
		return new Object[] {isMultiblockAssembled() ? getMultiblock().getInteriorLengthX() : 0};
	}
	
	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] getLengthY(Context context, Arguments args) {
		return new Object[] {isMultiblockAssembled() ? getMultiblock().getInteriorLengthY() : 0};
	}
	
	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] getLengthZ(Context context, Arguments args) {
		return new Object[] {isMultiblockAssembled() ? getMultiblock().getInteriorLengthZ() : 0};
	}
	
	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] getHeatStored(Context context, Arguments args) {
		return new Object[] {isMultiblockAssembled() ? getMultiblock().getLogic().heatBuffer.getHeatStored() : 0L};
	}
	
	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] getHeatCapacity(Context context, Arguments args) {
		return new Object[] {isMultiblockAssembled() ? getMultiblock().getLogic().heatBuffer.getHeatCapacity() : 0L};
	}
	
	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] getCoolingRate(Context context, Arguments args) {
		return new Object[] {isMultiblockAssembled() ? getMultiblock().cooling : 0D};
	}
	
	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] getRawHeatingRate(Context context, Arguments args) {
		return new Object[] {isMultiblockAssembled() ? getMultiblock().rawHeating : 0D};
	}
	
	/*@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] getNetHeatingRate(Context context, Arguments args) {
		return new Object[] {isMultiblockAssembled() ? getMultiblock().getNetHeatingRate(false) : 0D};
	}*/
	
	/*@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] getRawEfficiency(Context context, Arguments args) {
		return new Object[] {isMultiblockAssembled() ? getMultiblock().rawEfficiency : 0D};
	}*/
	
	/*@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] getHeatMultiplier(Context context, Arguments args) {
		return new Object[] {isMultiblockAssembled() ? getMultiblock().heatMult : 0D};
	}*/
	
	/*@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] getCoolingEfficiency(Context context, Arguments args) {
		return new Object[] {isMultiblockAssembled() ? getMultiblock().coolingEfficiency : 0D};
	}*/
	
	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] getNumberOfCells(Context context, Arguments args) {
		return new Object[] {isMultiblockAssembled() ? getMultiblock().getPartMap(TileSolidFissionCell.class).size() : 0};
	}
	
	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] getNumberOfVessels(Context context, Arguments args) {
		return new Object[] {isMultiblockAssembled() ? getMultiblock().getPartMap(TileSaltFissionVessel.class).size() : 0};
	}
	
	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] getNumberOfHeaters(Context context, Arguments args) {
		return new Object[] {isMultiblockAssembled() ? getMultiblock().getPartMap(TileSaltFissionHeater.class).size() : 0};
	}
	
	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] getNumberOfShields(Context context, Arguments args) {
		return new Object[] {isMultiblockAssembled() ? getMultiblock().getPartMap(TileFissionShield.class).size() : 0};
	}
	
	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] getNumberOfClusters(Context context, Arguments args) {
		return new Object[] {isMultiblockAssembled() ? getMultiblock().clusterCount : 0};
	}
	
	/*@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] getVesselStats(Context context, Arguments args) {
		if (isMultiblockAssembled()) {
			List<Object[]> stats = new ArrayList<>();
			for (TileSaltFissionVessel vessel : getMultiblock().getVessels()) {
				stats.add(new Object[] {new Object[] {vessel.getPos().getX(), vessel.getPos().getY(), vessel.getPos().getZ()}, vessel.isProcessing, vessel.time, vessel.recipeInfo != null ? vessel.baseProcessTime / vessel.getSpeedMultiplier() : 0D, vessel.getProcessHeat(), vessel.getEfficiency(), vessel.getHeatMultiplier()});
			}
			return new Object[] {stats.toArray()};
		}
		else
			return new Object[] {};
	}*/
	
	/*@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] getHeaterStats(Context context, Arguments args) {
		if (isMultiblockAssembled()) {
			List<Object[]> stats = new ArrayList<>();
			for (TileSaltFissionHeater heater : getMultiblock().getHeaters()) {
				stats.add(new Object[] {new Object[] {heater.getPos().getX(), heater.getPos().getY(), heater.getPos().getZ()}, heater.getCoolantName(), heater.isProcessing, heater.time, heater.recipeInfo != null ? heater.baseProcessTime / heater.getSpeedMultiplier() : 0D, heater.baseProcessCooling});
			}
			return new Object[] {stats.toArray()};
		}
		else
			return new Object[] {};
	}*/
	
	/*@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] getModeratorStats(Context context, Arguments args) {
		if (isMultiblockAssembled()) {
			List<Object[]> stats = new ArrayList<>();
			for (TileSaltFissionModerator moderator : getMultiblock().getModerators()) {
				stats.add(new Object[] {new Object[] {moderator.getPos().getX(), moderator.getPos().getY(), moderator.getPos().getZ()}, moderator.isInValidPosition, moderator.isInModerationLine});
			}
			return new Object[] {stats.toArray()};
		}
		else
			return new Object[] {};
	}*/
	
	/*@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] activate(Context context, Arguments args) {
		if (isMultiblockAssembled()) {
			getMultiblock().computerActivated = true;
			getMultiblock().setIsReactorOn();
		}
		return new Object[] {};
	}*/
	
	/*@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] deactivate(Context context, Arguments args) {
		if (isMultiblockAssembled()) {
			getMultiblock().computerActivated = false;
			getMultiblock().setIsReactorOn();
		}
		return new Object[] {};
	}*/
	
	// TODO
	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] getCellStats(Context context, Arguments args) {
		List<Map<String, Object>> cellData = new ArrayList<>();
		if (isMultiblockAssembled()) {
			Collection<TileSolidFissionCell> cells = getMultiblock().getParts(TileSolidFissionCell.class);
			for (TileSolidFissionCell cell : cells) {
				Object2ObjectMap<String, Object> entry = new Object2ObjectLinkedOpenHashMap<>();
				ItemStack fuel = cell.getInventoryStacks().get(0);
				if (fuel.isEmpty()) {
					entry.put("fuel", new Object[] {0, "null"});
				}
				else {
					entry.put("fuel", new Object[] {fuel.getCount(), StackHelper.stackName(fuel)});
				}
				entry.put("effective_heating", cell.getEffectiveHeating());
				entry.put("heat_multiplier", cell.getHeatMultiplier());
				entry.put("is_processing", cell.isProcessing);
				entry.put("time", cell.time);
				entry.put("base_process_time", cell.baseProcessTime);
				FissionCluster cluster = cell.getCluster();
				entry.put("cluster_heat_capacity", cluster == null ? 0 : cluster.heatBuffer.getHeatCapacity());
				entry.put("cluster_heat_stored", cluster == null ? 0 : cluster.heatBuffer.getHeatStored());
				entry.put("cluster_cooling", cluster == null ? 0 : cluster.cooling);
				entry.put("base_process_criticality", cell.baseProcessCriticality);
				entry.put("base_process_efficiency", cell.baseProcessEfficiency);
				entry.put("is_primed", cell.isPrimed());
				entry.put("efficiency", cell.getEfficiency());
				cellData.add(entry);
			}
		}
		return new Object[] {cellData.toArray()};
	}
	
	// TODO - currently broken!
	@Optional.Method(modid = "opencomputers")
	public Object[] updateShieldState(Context context, Arguments args) {
		boolean activated = false;
		if (isMultiblockAssembled()) {
			Long2ObjectMap<TileFissionShield> shieldMap = getMultiblock().getPartMap(TileFissionShield.class);
			if (shieldMap.size() == 0) {
				return new Object[] {"No neutron shields found!"};
			}
			int shieldId = args.checkInteger(0);
			boolean shieldState = args.checkBoolean(1);
			if (shieldId >= shieldMap.size()) {
				return new Object[] {"Incorrect neutron shield ID!"};
			}
			int i = 0;
			for (TileFissionShield shield : shieldMap.values()) {
				if (shieldId == i) {
					shield.isShielding = shieldState;
					shield.setActivity(shield.isShielding);
					shield.resetStats();
					getLogic().onShieldUpdated(shield);
					markDirty();
					getMultiblock().refreshFlag = true;
					activated = shield.isShielding;
					break;
				}
			}
		}
		return new Object[] {activated};
	}
	
	// TODO
	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] getClusterComponents(Context context, Arguments args) {
		List<Map<String, Object>> componentsData = new ArrayList<>();
		if (isMultiblockAssembled()) {
			if (getMultiblock().clusterCount == 0) {
				return new Object[] {"No clusters found!"};
			}
			int clusterID = args.checkInteger(0);
			if (clusterID >= getMultiblock().clusterCount) {
				return new Object[] {"Invalid cluster ID!"};
			}
			FissionCluster cluster = getMultiblock().getClusterMap().get(clusterID);
			for (IFissionComponent component : cluster.getComponentMap().values()) {
				Object2ObjectMap<String, Object> componentMap = new Object2ObjectOpenHashMap<>();
				if (component instanceof TileSolidFissionSink) {
					TileSolidFissionSink sink = (TileSolidFissionSink) component;
					componentMap.put(sink.ruleID, new Object[] {sink.getHeatStored(), sink.coolingRate});
				}
				if (component instanceof TileFissionShield) {
					TileFissionShield shield = (TileFissionShield) component;
					componentMap.put(shield.getClass().getName().toString(), new Object[] {shield.flux, shield.heatPerFlux, shield.isShielding, shield.heat, shield.efficiency});
				}
				componentsData.add(componentMap);
			}
		}
		return new Object[] {componentsData.toArray()};
	}
	
	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] clearAllMaterial(Context context, Arguments args) {
		if (isMultiblockAssembled()) {
			getMultiblock().clearAllMaterial();
		}
		return new Object[] {};
	}
}
