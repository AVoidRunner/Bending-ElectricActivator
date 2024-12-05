package me.Mastervrunner.DischargeEffect;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Dropper;
import org.bukkit.block.data.AnaloguePowerable;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Powerable;
import org.bukkit.block.data.type.DaylightDetector;
import org.bukkit.block.data.type.Dispenser;
import org.bukkit.block.data.type.Door;
import org.bukkit.block.data.type.Gate;
import org.bukkit.block.data.type.NoteBlock;
import org.bukkit.block.data.type.Piston;
import org.bukkit.block.data.type.RedstoneRail;
import org.bukkit.block.data.type.RedstoneWallTorch;
import org.bukkit.block.data.type.TNT;
import org.bukkit.block.data.type.TrapDoor;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.Validate;
//import org.bukkit.block.data.type.RedstoneWire;
import org.bukkit.craftbukkit.v1_16_R1.block.CraftBlock;
//import org.bukkit.block.data.type.RedstoneWire;
import org.bukkit.craftbukkit.v1_16_R1.block.impl.CraftRedstoneWire;
//import org.bukkit.craftbukkit.v1_16_R1.block.data.type.CraftRedstoneWire;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
//import org.bukkit.material.RedstoneWire;
import org.bukkit.util.Vector;

import com.griefcraft.lwc.LWC;
import com.griefcraft.lwc.LWCPlugin;
import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.ProjectKorra;
import com.projectkorra.projectkorra.ability.AddonAbility;
import com.projectkorra.projectkorra.ability.AirAbility;
import com.projectkorra.projectkorra.ability.FireAbility;
import com.projectkorra.projectkorra.configuration.ConfigManager;

public class DischargeEffect extends AirAbility implements AddonAbility {

	private long chargeTime;
	private int abilityState;
	private double radius;
	
	private long initStateOTime = 0;
	
	private double space;
	
	private double particleSize;
	
	private int particleAmount;
	
	LWC lwc;
	Plugin lwcp;
	
	
	public DischargeEffect(Player player) {
		super(player);
		
		setField();
		start();
	}

	public void setField() {
		
		Plugin lwcp = Bukkit.getPluginManager().getPlugin("LWC");
		lwc = ((LWCPlugin)lwcp).getLWC();
		
		chargeTime = 2000;
		abilityState = 0;
		
		radius = ConfigManager.getConfig().getDouble("ExtraAbilities.Mastervrunner.DischargeEffect.Radius");
		
		space = ConfigManager.getConfig().getDouble("ExtraAbilities.Mastervrunner.DischargeEffect.Space");
		particleSize = ConfigManager.getConfig().getDouble("ExtraAbilities.Mastervrunner.DischargeEffect.ParticleSize");
		
		particleAmount = ConfigManager.getConfig().getInt("ExtraAbilities.Mastervrunner.DischargeEffect.ParticleAmount");
		
	}
	
	public void drawLine(Location point1, Location point2, double space) {
	    World world = point1.getWorld();
	    Validate.isTrue(point2.getWorld().equals(world), "Lines cannot be in different worlds!");
	    double distance = point1.distance(point2);
	    Vector p1 = point1.toVector();
	    Vector p2 = point2.toVector();
	    Vector vector = p2.clone().subtract(p1).normalize().multiply(space);
	    double length = 0;
	    for (; length < distance; p1.add(vector)) {
	    	DustOptions dustOptions = new DustOptions(Color.fromRGB(133, 194, 255), (float) particleSize);
	    	player.getWorld().spawnParticle(Particle.REDSTONE, p1.toLocation(player.getWorld()), particleAmount, dustOptions);
	        length += space;
	    }
	}
	
	public void activateBlock(Location location) {
		List<Block> blocks = GeneralMethods.getBlocksAroundPoint(location, radius);
		for(Block block: blocks) {
			BlockData data = block.getBlockData();
			
			if(!lwc.canAccessProtection(player, block)) {
				return;
			}
			
		 
			if(data instanceof NoteBlock) {
				NoteBlock noteBlock = (NoteBlock) data;
				
				noteBlock.setPowered(true);
				
				block.setBlockData(data);
				block.getState().update();
				
				
			} else if(data instanceof Piston) {
				Piston piston = (Piston) data;
				
				block.setBlockData(data);
				block.getState().update();
				
			} else if(data instanceof TNT) {
				TNT tnt = (TNT) data;
				
				tnt.setUnstable(true);
				
				block.setBlockData(data);
				block.getState().update();
				
			} else if(data instanceof RedstoneWallTorch) {
				RedstoneWallTorch redstoneWallTorch = (RedstoneWallTorch) data;
				
				redstoneWallTorch.setLit(true);
				
				block.setBlockData(data);
				block.getState().update();
				
				drawLine(location, block.getLocation(), space);
				
			} else if(data instanceof RedstoneRail) {
				RedstoneRail redstoneRail = (RedstoneRail) data;
				
				redstoneRail.setPowered(true);
				block.setBlockData(data);
				block.getState().update();
			}
			
			else if(data instanceof DaylightDetector) {
				DaylightDetector daylightDetector = (DaylightDetector) data;
				
				daylightDetector.setPower(daylightDetector.getMaximumPower());
				
				block.setBlockData(data);
				block.getState().update();
				
			} else if(data instanceof Dispenser) {
				
				Dispenser dispenser = (Dispenser) data;
				dispenser.setTriggered(true);
				
				block.setBlockData(data);
				block.getState().update();
				
			}			 
			 else if(data instanceof Door) {
				Door door = (Door) data;
				
				door.setOpen(true);
				
				block.setBlockData(data);
				block.getState().update();
				
				drawLine(location, block.getLocation(), space);
				
			} else if(data instanceof Gate) {
				
				Gate door = (Gate) data;
				
				door.setOpen(true);
				block.setBlockData(data);
				
				block.getState().update();
				
				drawLine(location, block.getLocation(), space);
				
			} else if(data instanceof TrapDoor) {
				
				TrapDoor door = (TrapDoor) data;
				
				door.setOpen(true);
				block.setBlockData(data);
				block.getState().update();
				
				drawLine(location, block.getLocation(), space);
				
			} else if (data instanceof Powerable) {
				Powerable redstoneWire = (Powerable) data;
				
				redstoneWire.setPowered(true);
				
	            block.setBlockData(data);

	            block.getState().update();
	            
	            drawLine(location, block.getLocation(), space);
	            
			} else if (data instanceof CraftRedstoneWire) {
                CraftRedstoneWire redstoneWire = (CraftRedstoneWire) data;
                redstoneWire.setPower(15);
                
                block.setBlockData(data);
                
                block.getState().update();
                
                drawLine(location, block.getLocation(), space);
                
			} else if (data instanceof AnaloguePowerable) {
			     AnaloguePowerable powerable = (AnaloguePowerable) data;
			     powerable.setPower(15); // Setting it to full power, because why not :smiley: 
			     block.setBlockData(powerable);
			     
			     block.getState().update();
			     
			     drawLine(location, block.getLocation(), space);
		     }
			
		
		}
	}
	
	@Override
	public void progress() {
		
		/* WRITE YOUR CODE HERE */
		//Controls that will remove the move goes here.
		//(Region protection check, ability range check, ability duration check etc.)
		/* WRITE YOUR CODE HERE */
		
		if(initStateOTime == 0 && abilityState == 0) {
			//abilityState == 0
			initStateOTime = System.currentTimeMillis();
		}
		
		if (abilityState == 0) { //State 0 means ability has started but it is not charged yet.
			if (!player.isSneaking()) {
				remove(); //Don't add cooldown to the ability if it is removed from here.
				return; 
			} //else if (System.currentTimeMillis() > getStartTime() + chargeTime) {
			//	abilityState++;
			//} 
			  else {
				/* WRITE YOUR CODE HERE */
				//Not charged particle codes goes here.
				/* WRITE YOUR CODE HERE */
				
				Location location = GeneralMethods.getMainHandLocation(player);
				
				DustOptions dustOptions = new DustOptions(Color.fromRGB(0, 127, 255), 1);
				player.getWorld().spawnParticle(Particle.REDSTONE, location, 50, dustOptions);
				
				if (System.currentTimeMillis() > initStateOTime + 1000) {
						activateBlock(location);
						initStateOTime = System.currentTimeMillis();
					} else {

					}
			}
		} else if (abilityState == 1) { //State 1 means ability is charged but not released yet.
			if (!player.isSneaking()) {
				abilityState++;
			} else {
				/* WRITE YOUR CODE HERE */
				//Charged particle codes goes here.
				/* WRITE YOUR CODE HERE */
				
				Location location = GeneralMethods.getMainHandLocation(player);
			}
		} else if (abilityState == 2) { //State 2 means ability is launched.
			/* WRITE YOUR CODE HERE */
			//All other codes goes here. (Logic of the launch part.)
			/* WRITE YOUR CODE HERE */
			
			remove(); 
			return; 
		}
	}
	
	@Override
	public long getCooldown() {
		// TODO Auto-generated method stub
		return 100;
	}

	@Override
	public Location getLocation() {
		// TODO Auto-generated method stub
		return player.getLocation();
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "DischargeEffect";
	}

	@Override
	public boolean isHarmlessAbility() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSneakAbility() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String getAuthor() {
		// TODO Auto-generated method stub
		return "Mastervrunner";
	}

	@Override
	public String getVersion() {
		// TODO Auto-generated method stub
		return "1.0";
	}

	@Override
	public void load() {
		// TODO Auto-generated method stub
		ProjectKorra.plugin.getServer().getPluginManager().registerEvents(new DischargeEffectListener(), ProjectKorra.plugin);
		
		
		ConfigManager.getConfig().addDefault("ExtraAbilities.Mastervrunner.DischargeEffect.Radius", 10);
		ConfigManager.getConfig().addDefault("ExtraAbilities.Mastervrunner.DischargeEffect.Space", 1);
		ConfigManager.getConfig().addDefault("ExtraAbilities.Mastervrunner.DischargeEffect.ParticleSize", 0.2);		
		ConfigManager.getConfig().addDefault("ExtraAbilities.Mastervrunner.DischargeEffect.ParticleAmount", 50);
		
		
		ConfigManager.defaultConfig.save();
		ProjectKorra.log.info("Successfully enabled " + getName() + " by " + getAuthor());
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
		super.remove();
		
	}

}
