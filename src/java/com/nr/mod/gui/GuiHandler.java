package com.nr.mod.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.nr.mod.NuclearRelativistics;
import com.nr.mod.blocks.tileentities.TileEntityCollector;
import com.nr.mod.blocks.tileentities.TileEntityCooler;
import com.nr.mod.blocks.tileentities.TileEntityCrusher;
import com.nr.mod.blocks.tileentities.TileEntityElectricCrusher;
import com.nr.mod.blocks.tileentities.TileEntityElectricFurnace;
import com.nr.mod.blocks.tileentities.TileEntityElectrolyser;
import com.nr.mod.blocks.tileentities.TileEntityFactory;
import com.nr.mod.blocks.tileentities.TileEntityFissionReactorGraphite;
import com.nr.mod.blocks.tileentities.TileEntityFurnace;
import com.nr.mod.blocks.tileentities.TileEntityFusionReactor;
import com.nr.mod.blocks.tileentities.TileEntityHastener;
import com.nr.mod.blocks.tileentities.TileEntityIoniser;
import com.nr.mod.blocks.tileentities.TileEntityIrradiator;
import com.nr.mod.blocks.tileentities.TileEntityNuclearFurnace;
import com.nr.mod.blocks.tileentities.TileEntityNuclearWorkspace;
import com.nr.mod.blocks.tileentities.TileEntityOxidiser;
import com.nr.mod.blocks.tileentities.TileEntityReactionGenerator;
import com.nr.mod.blocks.tileentities.TileEntitySeparator;
import com.nr.mod.container.ContainerCollector;
import com.nr.mod.container.ContainerCooler;
import com.nr.mod.container.ContainerCrusher;
import com.nr.mod.container.ContainerElectricCrusher;
import com.nr.mod.container.ContainerElectricFurnace;
import com.nr.mod.container.ContainerElectrolyser;
import com.nr.mod.container.ContainerFactory;
import com.nr.mod.container.ContainerFissionReactorGraphite;
import com.nr.mod.container.ContainerFurnace;
import com.nr.mod.container.ContainerFusionReactor;
import com.nr.mod.container.ContainerHastener;
import com.nr.mod.container.ContainerIoniser;
import com.nr.mod.container.ContainerIrradiator;
import com.nr.mod.container.ContainerNuclearFurnace;
import com.nr.mod.container.ContainerNuclearWorkspace;
import com.nr.mod.container.ContainerOxidiser;
import com.nr.mod.container.ContainerReactionGenerator;
import com.nr.mod.container.ContainerSeparator;

import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.NetworkRegistry;

public class GuiHandler implements IGuiHandler {
	
	public GuiHandler() {
		NetworkRegistry.INSTANCE.registerGuiHandler(NuclearRelativistics.instance, this);
	}

	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity entity = world.getTileEntity(x, y, z);
		
		if (entity != null) {
			switch(ID) {
				case 0:
					if(entity instanceof TileEntityNuclearFurnace)
					{
						return new ContainerNuclearFurnace(player.inventory, (TileEntityNuclearFurnace) entity);
					}
					return null;
				case 1:
					if(entity instanceof TileEntityFurnace)
					{
						return new ContainerFurnace(player.inventory, (TileEntityFurnace) entity);
					}
					return null;
				case 2:
					if(entity instanceof TileEntityCrusher)
					{
						return new ContainerCrusher(player.inventory, (TileEntityCrusher) entity);
					}
					return null;
				case 3:
					if(entity instanceof TileEntityElectricCrusher)
					{
						return new ContainerElectricCrusher(player.inventory, (TileEntityElectricCrusher) entity);
					}
					return null;
				case 4:
					if(entity instanceof TileEntityElectricFurnace)
					{
						return new ContainerElectricFurnace(player.inventory, (TileEntityElectricFurnace) entity);
					}
					return null;
				case 5:
					if(entity instanceof TileEntityReactionGenerator)
					{
						return new ContainerReactionGenerator(player.inventory, (TileEntityReactionGenerator) entity);
					}
					return null;
				case 6:
					if(entity instanceof TileEntitySeparator)
					{
						return new ContainerSeparator(player.inventory, (TileEntitySeparator) entity);
					}
					return null;
				case 7:
					if(entity instanceof TileEntityHastener)
					{
						return new ContainerHastener(player.inventory, (TileEntityHastener) entity);
					}
					return null;
				case 8:
					if(entity instanceof TileEntityFissionReactorGraphite)
					{
						return new ContainerFissionReactorGraphite(player.inventory, (TileEntityFissionReactorGraphite) entity);
					}
					return null;
				case 9:
					if(ID == NuclearRelativistics.guiIdNuclearWorkspace) {
						return new ContainerNuclearWorkspace(player.inventory, (TileEntityNuclearWorkspace) entity);
					}
					return null;
				case 10:
					if(entity instanceof TileEntityCollector)
					{
						return new ContainerCollector(player.inventory, (TileEntityCollector) entity);
					}
					return null;
				case 11:
					if(entity instanceof TileEntityFusionReactor)
					{
						return new ContainerFusionReactor(player.inventory, (TileEntityFusionReactor) entity);
					}
					return null;
				case 12:
					if(entity instanceof TileEntityElectrolyser)
					{
						return new ContainerElectrolyser(player.inventory, (TileEntityElectrolyser) entity);
					}
					return null;
				case 13:
					if(entity instanceof TileEntityOxidiser)
					{
						return new ContainerOxidiser(player.inventory, (TileEntityOxidiser) entity);
					}
					return null;
				case 14:
					if(entity instanceof TileEntityIoniser)
					{
						return new ContainerIoniser(player.inventory, (TileEntityIoniser) entity);
					}
					return null;
				case 15:
					if(entity instanceof TileEntityIrradiator)
					{
						return new ContainerIrradiator(player.inventory, (TileEntityIrradiator) entity);
					}
					return null;
				case 16:
					if(entity instanceof TileEntityCooler)
					{
						return new ContainerCooler(player.inventory, (TileEntityCooler) entity);
					}
					return null;
				case 17:
					if(entity instanceof TileEntityFactory)
					{
						return new ContainerFactory(player.inventory, (TileEntityFactory) entity);
					}
					return null;
			}
		}
		
		return null;
	}

	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity entity = world.getTileEntity(x, y, z);
		
		if (entity != null)
		{
			switch(ID)
			{
				case 0:
					if(entity instanceof TileEntityNuclearFurnace)
					{
						return new GuiNuclearFurnace(player.inventory, (TileEntityNuclearFurnace) entity);
					}
					return null;
				case 1:
					if(entity instanceof TileEntityFurnace)
					{
						return new GuiFurnace(player.inventory, (TileEntityFurnace) entity);
					}
					return null;
				case 2:
					if(entity instanceof TileEntityCrusher)
					{
						return new GuiCrusher(player.inventory, (TileEntityCrusher) entity);
					}
					return null;
				case 3:
					if(entity instanceof TileEntityElectricCrusher)
					{
						return new GuiElectricCrusher(player.inventory, (TileEntityElectricCrusher) entity);
					}
					return null;
				case 4:
					if(entity instanceof TileEntityElectricFurnace)
					{
						return new GuiElectricFurnace(player.inventory, (TileEntityElectricFurnace) entity);
					}
					return null;
				case 5:
					if(entity instanceof TileEntityReactionGenerator)
					{
						return new GuiReactionGenerator(player.inventory, (TileEntityReactionGenerator) entity);
					}
					return null;
				case 6:
					if(entity instanceof TileEntitySeparator)
					{
						return new GuiSeparator(player.inventory, (TileEntitySeparator) entity);
					}
					return null;
				case 7:
					if(entity instanceof TileEntityHastener)
					{
						return new GuiHastener(player.inventory, (TileEntityHastener) entity);
					}
					return null;
				case 8:
					if(entity instanceof TileEntityFissionReactorGraphite)
					{
							return new GuiFissionReactorGraphite(player.inventory, (TileEntityFissionReactorGraphite) entity);
					}
					return null;
				case 9:
					if(entity instanceof TileEntityNuclearWorkspace)
					{
							return new GuiNuclearWorkspace(player.inventory, (TileEntityNuclearWorkspace) entity);
					}
					return null;
				case 10:
					if(entity instanceof TileEntityCollector)
					{
							return new GuiCollector(player.inventory, (TileEntityCollector) entity);
					}
					return null;
				case 11:
					if(entity instanceof TileEntityFusionReactor)
					{
							return new GuiFusionReactor(player.inventory, (TileEntityFusionReactor) entity);
					}
					return null;
				case 12:
					if(entity instanceof TileEntityElectrolyser)
					{
							return new GuiElectrolyser(player.inventory, (TileEntityElectrolyser) entity);
					}
					return null;
				case 13:
					if(entity instanceof TileEntityOxidiser)
					{
							return new GuiOxidiser(player.inventory, (TileEntityOxidiser) entity);
					}
					return null;
				case 14:
					if(entity instanceof TileEntityIoniser)
					{
							return new GuiIoniser(player.inventory, (TileEntityIoniser) entity);
					}
					return null;
				case 15:
					if(entity instanceof TileEntityIrradiator)
					{
							return new GuiIrradiator(player.inventory, (TileEntityIrradiator) entity);
					}
					return null;
				case 16:
					if(entity instanceof TileEntityCooler)
					{
							return new GuiCooler(player.inventory, (TileEntityCooler) entity);
					}
					return null;
				case 17:
					if(entity instanceof TileEntityFactory)
					{
							return new GuiFactory(player.inventory, (TileEntityFactory) entity);
					}
					return null;
			}
		}
		
		return null;
	}

}