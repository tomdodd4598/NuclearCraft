package net.ncplanner.ncpf;
import java.util.HashMap;
public class NCPFConfigurationContainer extends HashMap<String, Object>{
    public NCPFModuleList modules = new NCPFModuleList();
    {
        put("modules", modules);
    }
}
