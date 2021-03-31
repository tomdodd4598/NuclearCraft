package nc.advancement;

import java.util.*;

import com.google.gson.*;

import it.unimi.dsi.fastutil.objects.*;
import nc.Global;
import net.minecraft.advancements.*;
import net.minecraft.advancements.critereon.AbstractCriterionInstance;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;

public class NCCriterionTrigger implements ICriterionTrigger<NCCriterionTrigger.NCCriterion> {
	
	private final ResourceLocation id;
	private final Object2ObjectMap<PlayerAdvancements, NCCriterionTrigger.NCCriterionListeners> listenersMap = new Object2ObjectOpenHashMap<>();
	
	public NCCriterionTrigger(String modid, String path) {
		super();
		id = new ResourceLocation(modid, path);
	}
	
	public NCCriterionTrigger(String path) {
		super();
		id = new ResourceLocation(Global.MOD_ID, path);
	}
	
	@Override
	public ResourceLocation getId() {
		return id;
	}
	
	@Override
	public void addListener(PlayerAdvancements playerAdvancements, ICriterionTrigger.Listener<NCCriterion> listener) {
		NCCriterionListeners listeners = listenersMap.get(playerAdvancements);
		
		if (listeners == null) {
			listeners = new NCCriterionListeners(playerAdvancements);
			listenersMap.put(playerAdvancements, listeners);
		}
		
		listeners.add(listener);
	}
	
	@Override
	public void removeListener(PlayerAdvancements playerAdvancements, ICriterionTrigger.Listener<NCCriterion> listener) {
		NCCriterionListeners listeners = listenersMap.get(playerAdvancements);
		
		if (listeners != null) {
			listeners.remove(listener);
			
			if (listeners.isEmpty()) {
				listenersMap.remove(playerAdvancements);
			}
		}
	}
	
	@Override
	public void removeAllListeners(PlayerAdvancements playerAdvancements) {
		listenersMap.remove(playerAdvancements);
	}
	
	@Override
	public NCCriterion deserializeInstance(JsonObject json, JsonDeserializationContext context) {
		return new NCCriterion(id);
	}
	
	public static class NCCriterion extends AbstractCriterionInstance {
		
		public NCCriterion(ResourceLocation path) {
			super(path);
		}
		
		public boolean test() {
			return true;
		}
	}
	
	public void trigger(EntityPlayerMP player) {
		NCCriterionListeners listeners = listenersMap.get(player.getAdvancements());
		
		if (listeners != null) {
			listeners.trigger();
		}
	}
	
	public static class NCCriterionListeners {
		
		private final PlayerAdvancements playerAdvancements;
		private final ObjectSet<ICriterionTrigger.Listener<NCCriterion>> listeners = new ObjectOpenHashSet<>();
		
		public NCCriterionListeners(PlayerAdvancements playerAdvancements) {
			this.playerAdvancements = playerAdvancements;
		}
		
		public boolean isEmpty() {
			return listeners.isEmpty();
		}
		
		public void add(ICriterionTrigger.Listener<NCCriterion> listener) {
			listeners.add(listener);
		}
		
		public void remove(ICriterionTrigger.Listener<NCCriterion> listener) {
			listeners.remove(listener);
		}
		
		public void trigger() {
			List<ICriterionTrigger.Listener<NCCriterion>> list = null;
			
			for (ICriterionTrigger.Listener<NCCriterion> listener : listeners) {
				if (listener.getCriterionInstance().test()) {
					if (list == null) {
						list = new ArrayList<>();
					}
					list.add(listener);
				}
			}
			
			if (list != null) {
				for (ICriterionTrigger.Listener<NCCriterion> listener1 : list) {
					listener1.grantCriterion(playerAdvancements);
				}
			}
		}
	}
}
