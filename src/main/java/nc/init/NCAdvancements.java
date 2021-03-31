package nc.init;

import java.lang.reflect.Method;


import nc.Global;
import nc.multiblock.advancement.MultiblockTrigger;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

@SuppressWarnings("deprecation")
public class NCAdvancements {

	
	public static void init() {

		Logger logger = LogManager.getLogger(Global.MOD_ID);
		
		Method method = ReflectionHelper.findMethod(CriteriaTriggers.class, "register", "func_192118_a", ICriterionTrigger.class);
		method.setAccessible(true);

        for (int i=0; i < MultiblockTrigger.TRIGGER_ARRAY.length; i++)
        {
            try
            {
                method.invoke(null, MultiblockTrigger.TRIGGER_ARRAY[i]);
                logger.log(Level.INFO, "Invoking: "+MultiblockTrigger.TRIGGER_ARRAY[i].getId());
            }
            catch (Exception e)
            {
            	logger.log(Level.ERROR, e.getMessage());
            }
        } 
	}
	
}
