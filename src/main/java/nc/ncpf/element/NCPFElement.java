package nc.ncpf.element;

import nc.ncpf.NCPFObject;

public class NCPFElement extends NCPFObject {
	
	public final String type;
    public Integer amount;
	
	public NCPFElement(String type) {
		this.type = type;
	}
}
