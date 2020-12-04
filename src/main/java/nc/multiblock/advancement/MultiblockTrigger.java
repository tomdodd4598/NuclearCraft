package nc.multiblock.advancement;

import nc.Global;

public class MultiblockTrigger 
{
    public static final CustomTrigger FISSION_REACTOR_FORMED = new CustomTrigger("fission_reactor_complete");
    public static final CustomTrigger MOLTENSALT_REACTOR_FORMED = new CustomTrigger("moltensalt_reactor_complete");
    public static final CustomTrigger HEATEXCHANGER_FORMED = new CustomTrigger("heatexchanger_complete");
    public static final CustomTrigger TURBINE_FORMED = new CustomTrigger("turbine_complete");
    
    public static final CustomTrigger[] TRIGGER_ARRAY = new CustomTrigger[] {
    		FISSION_REACTOR_FORMED,
    		MOLTENSALT_REACTOR_FORMED,
    		HEATEXCHANGER_FORMED,
    		TURBINE_FORMED
            };
}
