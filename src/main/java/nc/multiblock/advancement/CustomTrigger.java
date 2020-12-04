package nc.multiblock.advancement;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;

import nc.Global;
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.advancements.critereon.AbstractCriterionInstance;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;

public class CustomTrigger implements ICriterionTrigger<CustomTrigger.Instance> {
    private final ResourceLocation ID;
    private final Map<PlayerAdvancements, CustomTrigger.Listeners> listeners = Maps.<PlayerAdvancements, CustomTrigger.Listeners>newHashMap();

    //entry for addons
    public CustomTrigger(String MOD_ID, String parString)
    {
        super();
        ID = new ResourceLocation(MOD_ID, parString);
    }
    
    public CustomTrigger(String parString)
    {
        super();
        ID = new ResourceLocation(Global.MOD_ID, parString);
    }
    
	@Override
	public ResourceLocation getId() {
		return ID;
	}

	@Override
	public void addListener(PlayerAdvancements playerAdvancementsIn, ICriterionTrigger.Listener<CustomTrigger.Instance> listener)
    {
		CustomTrigger.Listeners customtrigger$listeners = this.listeners.get(playerAdvancementsIn);

        if (customtrigger$listeners == null)
        {
        	customtrigger$listeners = new CustomTrigger.Listeners(playerAdvancementsIn);
            this.listeners.put(playerAdvancementsIn, customtrigger$listeners);
        }

        customtrigger$listeners.add(listener);
    }

	@Override
    public void removeListener(PlayerAdvancements playerAdvancementsIn, ICriterionTrigger.Listener<CustomTrigger.Instance> listener)
    {
		CustomTrigger.Listeners consumeitemtrigger$listeners = this.listeners.get(playerAdvancementsIn);

        if (consumeitemtrigger$listeners != null)
        {
            consumeitemtrigger$listeners.remove(listener);

            if (consumeitemtrigger$listeners.isEmpty())
            {
                this.listeners.remove(playerAdvancementsIn);
            }
        }
    }

	@Override
    public void removeAllListeners(PlayerAdvancements playerAdvancementsIn)
    {
        this.listeners.remove(playerAdvancementsIn);
    }

	@Override
	public CustomTrigger.Instance deserializeInstance(JsonObject json, JsonDeserializationContext context) {
		return new CustomTrigger.Instance(getId());
	}
	
	public static class Instance extends AbstractCriterionInstance
    {
        
        /**
         * Instantiates a new instance.
         */
        public Instance(ResourceLocation parRL)
        {
            super(parRL);
        }

        /**
         * Test.
         *
         * @return true, if successful
         */
        public boolean test()
        {
            return true;
        }
    }
	
	public void trigger(EntityPlayerMP player) {
		CustomTrigger.Listeners enterblocktrigger$listeners = this.listeners.get(player.getAdvancements());

        if (enterblocktrigger$listeners != null)
        {
            enterblocktrigger$listeners.trigger();
        }
	}
	
	static class Listeners
    {
        private final PlayerAdvancements playerAdvancements;
        private final Set<ICriterionTrigger.Listener<CustomTrigger.Instance>> listeners = Sets.<ICriterionTrigger.Listener<CustomTrigger.Instance>>newHashSet();

        public Listeners(PlayerAdvancements playerAdvancementsIn)
        {
            this.playerAdvancements = playerAdvancementsIn;
        }

        public boolean isEmpty()
        {
            return this.listeners.isEmpty();
        }

        public void add(ICriterionTrigger.Listener<CustomTrigger.Instance> listener)
        {
            this.listeners.add(listener);
        }

        public void remove(ICriterionTrigger.Listener<CustomTrigger.Instance> listener)
        {
            this.listeners.remove(listener);
        }

        public void trigger()
        {
            List<ICriterionTrigger.Listener<CustomTrigger.Instance>> list = null;

            for (ICriterionTrigger.Listener<CustomTrigger.Instance> listener : this.listeners)
            {
                if (((CustomTrigger.Instance)listener.getCriterionInstance()).test())
                {
                    if (list == null)
                    {
                        list = Lists.<ICriterionTrigger.Listener<CustomTrigger.Instance>>newArrayList();
                    }

                    list.add(listener);
                }
            }

            if (list != null)
            {
                for (ICriterionTrigger.Listener<CustomTrigger.Instance> listener1 : list)
                {
                    listener1.grantCriterion(this.playerAdvancements);
                }
            }
        }
    }
}