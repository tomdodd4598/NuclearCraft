package nc.multiblock.fission.tile;

import li.cil.oc.api.machine.*;
import li.cil.oc.api.network.SimpleComponent;
import nc.multiblock.cuboidal.CuboidalPartPositionType;
import nc.multiblock.fission.FissionReactor;
import nc.multiblock.fission.salt.tile.*;
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
	
	/* @Callback
	 * 
	 * @Optional.Method(modid = "opencomputers") public Object[] getNetHeatingRate(Context context, Arguments args) { return new Object[] {isMultiblockAssembled() ? getMultiblock().getNetHeatingRate(false) : 0D}; } */
	
	/* @Callback
	 * 
	 * @Optional.Method(modid = "opencomputers") public Object[] getRawEfficiency(Context context, Arguments args) { return new Object[] {isMultiblockAssembled() ? getMultiblock().rawEfficiency : 0D}; } */
	
	/* @Callback
	 * 
	 * @Optional.Method(modid = "opencomputers") public Object[] getHeatMultiplier(Context context, Arguments args) { return new Object[] {isMultiblockAssembled() ? getMultiblock().heatMult : 0D}; } */
	
	/* @Callback
	 * 
	 * @Optional.Method(modid = "opencomputers") public Object[] getCoolingEfficiency(Context context, Arguments args) { return new Object[] {isMultiblockAssembled() ? getMultiblock().coolingEfficiency : 0D}; } */
	
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
	public Object[] getNumberOfFuelCells(Context context, Arguments args) {
		return new Object[] {isMultiblockAssembled() ? getMultiblock().getPartMap(TileSolidFissionCell.class).size() : 0};
	}
	
	
	//TODO review OC fission cell method
	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] getFuelCellStats(Context context, Arguments args) {
		List<Map> fuelCellData = new ArrayList<Map>();
		if(isMultiblockAssembled()) {
			Collection<TileSolidFissionCell> fuelCells = getMultiblock().getPartMap(TileSolidFissionCell.class).values();
			for(TileSolidFissionCell fuelCell: fuelCells) {
				Map<String, Object> entry = new HashMap<String, Object>();
				entry.put("fuel", new Object[] {fuelCell.getInventoryStacks().get(0).getCount(),fuelCell.getInventoryStacks().get(0).getDisplayName()});
				entry.put("heating", fuelCell.getEffectiveHeating());
				entry.put("heat_mult", fuelCell.getHeatMultiplier());
				entry.put("is_processing", fuelCell.isProcessing);
				entry.put("time", fuelCell.time);
				entry.put("base_process_time", fuelCell.baseProcessTime);
				entry.put("claster_heat_capacity", fuelCell.getCluster().heatBuffer.getHeatCapacity());
				entry.put("claster_heat_stored", fuelCell.getCluster().heatBuffer.getHeatStored());
				entry.put("cooling", fuelCell.getCluster().cooling);
				entry.put("base_process_criticality", fuelCell.baseProcessCriticality);
				entry.put("base_process_efficiency", fuelCell.baseProcessEfficiency);
				entry.put("is_primed", fuelCell.isPrimed());
				entry.put("flux_efficiency", fuelCell.getEfficiency());
				fuelCellData.add(entry);
			}
		}
		return new Object[] {fuelCellData.toArray()};
	}
	
	/* @Callback
	 * 
	 * @Optional.Method(modid = "opencomputers") public Object[] getVesselStats(Context context, Arguments args) { if (isMultiblockAssembled()) { List<Object[]> stats = new ArrayList<>(); for (TileSaltFissionVessel vessel : getMultiblock().getVessels()) { stats.add(new Object[] { new Object[] {vessel.getPos().getX(), vessel.getPos().getY(), vessel.getPos().getZ()}, vessel.isProcessing, vessel.time, vessel.recipeInfo != null ? vessel.baseProcessTime/vessel.getSpeedMultiplier() : 0D, vessel.getProcessHeat(), vessel.getEfficiency(), vessel.getHeatMultiplier() }); } return new Object[] {stats.toArray()}; } else return new Object[] {}; } */
	
	/* @Callback
	 * 
	 * @Optional.Method(modid = "opencomputers") public Object[] getHeaterStats(Context context, Arguments args) { if (isMultiblockAssembled()) { List<Object[]> stats = new ArrayList<>(); for (TileSaltFissionHeater heater : getMultiblock().getHeaters()) { stats.add(new Object[] { new Object[] {heater.getPos().getX(), heater.getPos().getY(), heater.getPos().getZ()}, heater.getCoolantName(), heater.isProcessing, heater.time, heater.recipeInfo != null ? heater.baseProcessTime/heater.getSpeedMultiplier() : 0D, heater.baseProcessCooling }); } return new Object[] {stats.toArray()}; } else return new Object[] {}; } */
	
	// TODO
	/* @Callback
	 * 
	 * @Optional.Method(modid = "opencomputers") public Object[] getModeratorStats(Context context, Arguments args) { if (isMultiblockAssembled()) { List<Object[]> stats = new ArrayList<>(); for (TileSaltFissionModerator moderator : getMultiblock().getModerators()) { stats.add( new Object[] { new Object[] {moderator.getPos().getX(), moderator.getPos().getY(), moderator.getPos().getZ()}, moderator.isInValidPosition, moderator.isInModerationLine }); } return new Object[] {stats.toArray()}; } else return new Object[] {}; } */
	
	/* @Callback
	 * 
	 * @Optional.Method(modid = "opencomputers") public Object[] activate(Context context, Arguments args) { if (isMultiblockAssembled()) { getMultiblock().computerActivated = true; getMultiblock().setIsReactorOn(); } return new Object[] {}; } */
	
	/* @Callback
	 * 
	 * @Optional.Method(modid = "opencomputers") public Object[] deactivate(Context context, Arguments args) { if (isMultiblockAssembled()) { getMultiblock().computerActivated = false; getMultiblock().setIsReactorOn(); } return new Object[] {}; } */
	
	@Callback
	@Optional.Method(modid = "opencomputers")
	public Object[] clearAllMaterial(Context context, Arguments args) {
		if (isMultiblockAssembled()) {
			getMultiblock().clearAllMaterial();
		}
		return new Object[] {};
	}
}
