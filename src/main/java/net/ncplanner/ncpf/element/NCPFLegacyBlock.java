package net.ncplanner.ncpf.element;
import java.util.Map;
public class NCPFLegacyBlock extends NCPFElement{
    public NCPFLegacyBlock(){
        super("legacy_block");
    }
    public String name;
    public Integer metadata;
    public Map<String, Object> blockstate;
    public String nbt;
}
