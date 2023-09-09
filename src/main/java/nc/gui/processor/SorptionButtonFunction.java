package nc.gui.processor;

import nc.gui.element.NCButton.SorptionConfig;

@FunctionalInterface
public interface SorptionButtonFunction {
	
	SorptionConfig apply(int id, int x, int y, int width, int height);
}
