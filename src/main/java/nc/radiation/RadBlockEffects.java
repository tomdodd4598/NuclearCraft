package nc.radiation;

import java.util.ArrayList;
import java.util.List;

import nc.config.NCConfig;
import nc.recipe.NCRecipes;
import nc.recipe.ProcessorRecipeHandler;
import nc.util.RegistryHelper;
import nc.util.StringHelper;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;

public class RadBlockEffects extends ProcessorRecipeHandler {
	
	public static final List<MaterialQuery> MATERIAL_QUERIES = new ArrayList<>();
	public static boolean hasRecipes = false;
	
	public RadBlockEffects() {
		super("rad_block_mutations", 1, 0, 1, 0);
	}
	
	@Override
	public void addRecipes() {}
	
	public static void init() {
		for (String effectInfo : NCConfig.radiation_block_effects) {
			int puncPos = effectInfo.lastIndexOf('_');
			if (puncPos == -1) continue;
			
			String s = effectInfo.substring(0, puncPos);
			int puncPos2 = s.lastIndexOf('_');
			if (puncPos2 == -1) continue;
			
			IBlockState result = RegistryHelper.blockStateFromRegistry(s.substring(0, puncPos2));
			if (result == null) continue;
			effectInfo = effectInfo.substring(puncPos + 1);
			
			MaterialQuery query = new MaterialQuery(result, Double.parseDouble(s.substring(puncPos2 + 1)));
			
			boolean end = false;
			while (!end) {
				for (int i = 0; i < ARGS.length; i++) {
					if (StringHelper.beginsWith(effectInfo, ARGS[i])) {
						effectInfo = effectInfo.substring(ARGS[i].length() + 1);
						puncPos = effectInfo.indexOf(',');
						if (puncPos == -1) {
							puncPos = effectInfo.length();
							end = true;
						}
						String value = effectInfo.substring(0, puncPos);
						if (i == 0) {
							query.isLiquid = new Boolean(Boolean.parseBoolean(value));
						}
						else if (i == 1) {
							query.isSolid = new Boolean(Boolean.parseBoolean(value));
						}
						else if (i == 2) {
							query.blocksLight = new Boolean(Boolean.parseBoolean(value));
						}
						else if (i == 3) {
							query.blocksMovement = new Boolean(Boolean.parseBoolean(value));
						}
						else if (i == 4) {
							query.getCanBurn = new Boolean(Boolean.parseBoolean(value));
						}
						else if (i == 5) {
							query.isReplaceable = new Boolean(Boolean.parseBoolean(value));
						}
						else if (i == 6) {
							query.isOpaque = new Boolean(Boolean.parseBoolean(value));
						}
						else if (i == 7) {
							query.isToolNotRequired = new Boolean(Boolean.parseBoolean(value));
						}
						else if (i == 8) {
							if (value.equalsIgnoreCase("normal")) {
								query.getPushReaction = EnumPushReaction.NORMAL;
							}
							else if (value.equalsIgnoreCase("destroy")) {
								query.getPushReaction = EnumPushReaction.DESTROY;
							}
							else if (value.equalsIgnoreCase("block")) {
								query.getPushReaction = EnumPushReaction.BLOCK;
							}
							else if (value.equalsIgnoreCase("ignore")) {
								query.getPushReaction = EnumPushReaction.IGNORE;
							}
							else if (value.equalsIgnoreCase("pushOnly")) {
								query.getPushReaction = EnumPushReaction.PUSH_ONLY;
							}
						}
						else if (i == 9) {
							query.materialMapColor = new Integer(Integer.parseInt(value));
						}
						if (!end) {
							effectInfo = effectInfo.substring(puncPos + 1);
						}
						break;
					}
				}
			}
			MATERIAL_QUERIES.add(query);
		}
		
		hasRecipes = !NCRecipes.radiation_block_mutations.getRecipeList().isEmpty();
	}
	
	private static final String[] ARGS = new String[] {"isLiquid", "isSolid", "blocksLight", "blocksMovement", "getCanBurn", "isReplaceable", "isOpaque", "isToolNotRequired", "getPushReaction", "materialMapColor"};
	
	public static class MaterialQuery {
		
		public final IBlockState result;
		public final double threshold; 
		public Boolean isLiquid = null, isSolid = null, blocksLight = null, blocksMovement = null, getCanBurn = null, isReplaceable = null, isOpaque = null, isToolNotRequired = null;
		public EnumPushReaction getPushReaction = null;
		public Integer materialMapColor = null;
		
		public MaterialQuery(IBlockState result, double threshold) {
			this.result = result;
			this.threshold = threshold;
		}
		
		public boolean matches(Material target, double radiation) {
			return radiation >= threshold && !(isLiquid != null && isLiquid.booleanValue() != target.isLiquid()) && !(isSolid != null && isSolid.booleanValue() != target.isSolid()) && !(blocksLight != null && blocksLight.booleanValue() != target.blocksLight()) && !(blocksMovement != null && blocksMovement.booleanValue() != target.blocksMovement()) && !(getCanBurn != null && getCanBurn.booleanValue() != target.getCanBurn()) && !(isReplaceable != null && isReplaceable.booleanValue() != target.isReplaceable()) && !(isOpaque != null && isOpaque.booleanValue() != target.isOpaque()) && !(isToolNotRequired != null && isToolNotRequired.booleanValue() != target.isToolNotRequired()) && !(getPushReaction != null && getPushReaction != target.getPushReaction()) && !(materialMapColor != null && materialMapColor.intValue() != target.getMaterialMapColor().colorValue);
		}
	}
}
