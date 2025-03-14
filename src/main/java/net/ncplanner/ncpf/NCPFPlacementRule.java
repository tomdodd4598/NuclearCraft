package net.ncplanner.ncpf;
import java.util.ArrayList;
import java.util.List;
import net.ncplanner.ncpf.element.NCPFElement;
public class NCPFPlacementRule{
    public RuleType type;
    public NCPFElement block;//block or module reference
    public int min;
    public int max;
    public List<NCPFPlacementRule> rules = new ArrayList<>();
    public enum RuleType{
        between,
        axial,
        vertex,
        edge,
        or,
        and;
    }
}
