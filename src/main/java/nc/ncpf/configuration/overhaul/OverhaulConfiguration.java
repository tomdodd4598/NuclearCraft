package nc.ncpf.configuration.overhaul;
import nc.ncpf.config2.Config;
import nc.ncpf.configuration.Configuration;
import nc.ncpf.configuration.overhaul.fissionmsr.FissionMSRConfiguration;
import nc.ncpf.configuration.overhaul.fissionsfr.FissionSFRConfiguration;
import nc.ncpf.configuration.overhaul.turbine.TurbineConfiguration;
public class OverhaulConfiguration{
    public FissionSFRConfiguration fissionSFR;
    public FissionMSRConfiguration fissionMSR;
    public TurbineConfiguration turbine;
    public Config save(Configuration parent){
        Config config = Config.newConfig();
        if(fissionSFR!=null)config.set("fissionSFR", fissionSFR.save(parent));
        if(fissionMSR!=null)config.set("fissionMSR", fissionMSR.save(parent));
        if(turbine!=null)config.set("turbine", turbine.save(parent));
        return config;
    }
}