#loader nc_preinit

import mods.nuclearcraft.ProcessorBuilderHelper.standardSlot;
import mods.nuclearcraft.ProcessorBuilderHelper.bigSlot;

mods.nuclearcraft.UpgradableEnergyProcessorBuilder("glowstone_aggregator")
	.setParticles(["reddust", "depthsuspend"])
	.setDefaultProcessTime(600.0)
	.setDefaultProcessPower(100.0)
	.setItemInputSlots([standardSlot(56, 35)])
	.setItemOutputSlots([bigSlot(112, 31)])
	.buildAndRegister();
