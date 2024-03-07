package nc.ncpf.configuration;
import java.util.ArrayList;
import nc.ncpf.config2.Config;
import nc.ncpf.config2.ConfigList;
import nc.ncpf.configuration.overhaul.OverhaulConfiguration;
public class Configuration{
    public String name;
    public String overhaulVersion;
    public boolean addon;
    public ArrayList<Configuration> addons = new ArrayList<>();
    public Configuration(String name, String version){
        this.name = name;
        this.overhaulVersion = version;
    }
    public OverhaulConfiguration overhaul;
    public Config save(Configuration parent, Config config){
        config.set("partial", false);
        config.set("addon", addon);
        if(overhaul!=null)config.set("overhaul", overhaul.save(parent));
        if(overhaulVersion!=null)config.set("version", overhaulVersion);
        config.set("name", name);
        if(!addons.isEmpty()){
            ConfigList addns = new ConfigList();
            for(Configuration cnfg : addons){
                addns.add(cnfg.save(this, Config.newConfig()));
            }
            config.set("addons", addns);
        }
        return config;
    }
}