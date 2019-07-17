package nc.integration.crafttweaker;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.entity.IEntityLivingBase;
import crafttweaker.api.minecraft.CraftTweakerMC;
import nc.capability.radiation.entity.IEntityRads;
import nc.radiation.RadiationHelper;
import net.minecraft.entity.EntityLivingBase;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenExpansion;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenExpansion("crafttweaker.entity.IEntityLivingBase")
public class EntityExtension {

    @ZenMethod
    public static void addRadiation(IEntityLivingBase entity, double amount) {
        EntityLivingBase actualentity = CraftTweakerMC.getEntityLivingBase(entity);
        if (actualentity != null) {
            IEntityRads rads = RadiationHelper.getEntityRadiation(actualentity);
            if (rads != null) {
                rads.setTotalRads(rads.getTotalRads() + amount, true);
            }
        }
    }

    @ZenMethod
    public static void setRadiation(IEntityLivingBase entity, double amount) {
        EntityLivingBase actualentity = CraftTweakerMC.getEntityLivingBase(entity);
        if (actualentity != null) {
            IEntityRads rads = RadiationHelper.getEntityRadiation(actualentity);
            if (rads != null) {
                rads.setTotalRads(amount, true);
            }
        }
    }

    @ZenMethod
    public static double getRadiation(IEntityLivingBase entity) {
        EntityLivingBase actualentity = CraftTweakerMC.getEntityLivingBase(entity);
        if (actualentity != null) {
            IEntityRads rads = RadiationHelper.getEntityRadiation(actualentity);
            if (rads != null) {
                return rads.getTotalRads();
            }
        }
        return 0.0D;
    }

    @ZenMethod
    public static void addRadawayBuffer(IEntityLivingBase entity, double amount, @Optional boolean slow) {
        EntityLivingBase actualentity = CraftTweakerMC.getEntityLivingBase(entity);
        if (actualentity != null) {
            IEntityRads rads = RadiationHelper.getEntityRadiation(actualentity);
            if (rads != null) {
                rads.setRadawayBuffer(slow, rads.getRadawayBuffer(slow) + amount);
                rads.setRecentRadawayAddition(amount);
            }
        }
    }

    @ZenMethod
    public static void setRadawayBuffer(IEntityLivingBase entity, double amount, @Optional boolean slow) {
        EntityLivingBase actualentity = CraftTweakerMC.getEntityLivingBase(entity);
        if (actualentity != null) {
            IEntityRads rads = RadiationHelper.getEntityRadiation(actualentity);
            if (rads != null) {
                rads.setRadawayBuffer(slow, amount);
                rads.setRecentRadawayAddition(amount);
            }
        }
    }

    @ZenMethod
    public static double getRadawayBuffer(IEntityLivingBase entity, boolean slow) {
        EntityLivingBase actualentity = CraftTweakerMC.getEntityLivingBase(entity);
        if (actualentity != null) {
            IEntityRads rads = RadiationHelper.getEntityRadiation(actualentity);
            if (rads != null) {
                return rads.getRadawayBuffer(slow);
            }
        }
        return 0.0D;
    }

    @ZenMethod
    public static void addPoisonBuffer(IEntityLivingBase entity, double amount) {
        EntityLivingBase actualentity = CraftTweakerMC.getEntityLivingBase(entity);
        if (actualentity != null) {
            IEntityRads rads = RadiationHelper.getEntityRadiation(actualentity);
            if (rads != null) {
                rads.setPoisonBuffer(rads.getPoisonBuffer() + amount);
                rads.setRecentPoisonAddition(amount);
            }
        }
    }

    @ZenMethod
    public static void setPoisonBuffer(IEntityLivingBase entity, double amount) {
        EntityLivingBase actualentity = CraftTweakerMC.getEntityLivingBase(entity);
        if (actualentity != null) {
            IEntityRads rads = RadiationHelper.getEntityRadiation(actualentity);
            if (rads != null) {
                rads.setPoisonBuffer(amount);
                rads.setRecentPoisonAddition(amount);
            }
        }
    }


    @ZenMethod
    public static double getPoisonBuffer(IEntityLivingBase entity) {
        EntityLivingBase actualentity = CraftTweakerMC.getEntityLivingBase(entity);
        if (actualentity != null) {
            IEntityRads rads = RadiationHelper.getEntityRadiation(actualentity);
            if (rads != null) {
                return rads.getPoisonBuffer();
            }
        }
        return 0.0D;
    }

    @ZenMethod
    public static void addRadiationResistance(IEntityLivingBase entity, double amount) {
        EntityLivingBase actualentity = CraftTweakerMC.getEntityLivingBase(entity);
        if (actualentity != null) {
            IEntityRads rads = RadiationHelper.getEntityRadiation(actualentity);
            if (rads != null) {
                rads.setInternalRadiationResistance(rads.getInternalRadiationResistance() + amount);
                rads.setRecentRadXAddition(Math.abs(amount));
            }
        }
    }

    @ZenMethod
    public static void setRadiationResistance(IEntityLivingBase entity, double amount) {
        EntityLivingBase actualentity = CraftTweakerMC.getEntityLivingBase(entity);
        if (actualentity != null) {
            IEntityRads rads = RadiationHelper.getEntityRadiation(actualentity);
            if (rads != null) {
                rads.setInternalRadiationResistance(amount);
                rads.setRecentRadXAddition(Math.abs(amount));
            }
        }
    }

    @ZenMethod
    public static double getRadiationResistance(IEntityLivingBase entity) {
        EntityLivingBase actualentity = CraftTweakerMC.getEntityLivingBase(entity);
        if (actualentity != null) {
            IEntityRads rads = RadiationHelper.getEntityRadiation(actualentity);
            if (rads != null) {
                return rads.getInternalRadiationResistance();
            }
        }
        return 0.0D;
    }
}
