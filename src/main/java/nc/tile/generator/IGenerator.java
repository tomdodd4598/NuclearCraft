package nc.tile.generator;

import nc.tile.processor.IProcessor;

public abstract interface IGenerator extends IProcessor {
	
	public int getOtherSlotsSize();
}
