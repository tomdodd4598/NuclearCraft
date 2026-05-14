package nc.advancement;

import java.util.*;

public class NCCriterions {
	
	public static final NCCriterionTrigger ELECTROLYZER_ASSEMBLED = new NCCriterionTrigger("electrolyzer_assembled");
	public static final NCCriterionTrigger DISTILLER_ASSEMBLED = new NCCriterionTrigger("distiller_assembled");
	public static final NCCriterionTrigger INFILTRATOR_ASSEMBLED = new NCCriterionTrigger("infiltrator_assembled");
	public static final NCCriterionTrigger PEBBLE_FISSION_ASSEMBLED = new NCCriterionTrigger("pebble_fission_reactor_assembled");
	public static final NCCriterionTrigger SOLID_FISSION_ASSEMBLED = new NCCriterionTrigger("solid_fission_reactor_assembled");
	public static final NCCriterionTrigger SALT_FISSION_ASSEMBLED = new NCCriterionTrigger("salt_fission_reactor_assembled");
	public static final NCCriterionTrigger HEAT_EXCHANGER_ASSEMBLED = new NCCriterionTrigger("heat_exchanger_assembled");
	public static final NCCriterionTrigger CONDENSER_ASSEMBLED = new NCCriterionTrigger("condenser_assembled");
	public static final NCCriterionTrigger TURBINE_ASSEMBLED = new NCCriterionTrigger("turbine_assembled");
	
	public static final List<NCCriterionTrigger> CRITERION_TRIGGERS = Arrays.asList(ELECTROLYZER_ASSEMBLED, DISTILLER_ASSEMBLED, INFILTRATOR_ASSEMBLED, PEBBLE_FISSION_ASSEMBLED, SOLID_FISSION_ASSEMBLED, SALT_FISSION_ASSEMBLED, HEAT_EXCHANGER_ASSEMBLED, CONDENSER_ASSEMBLED, TURBINE_ASSEMBLED);
}
