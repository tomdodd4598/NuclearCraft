package nc.ncpf.element;
import java.util.ArrayList;
public class NCPFRecipe extends NCPFElement{
    
    public String name;
    
    public ArrayList<NCPFElement> inputs = new ArrayList<>();
    public ArrayList<NCPFElement> outputs = new ArrayList<>();
    
    public NCPFRecipe() {
        super("legacy_recipe");
    }
}
