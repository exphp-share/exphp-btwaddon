package net.minecraft.src;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class XP_TileEntityMagnet extends TileEntity {
	
	public double beamDepth = 0.0D;
	public static final double maxBeamDepth = 1.0D;
	
	public static double[] beamDepthIndicatorLevels = new double[] {0.15D, 0.95D};
	
	// Maps shifted indices of affected items to constants representing effects.
	// This determines not only what happens to EntityItems in the world, but also armor worn by other entities.
	public static Map<Integer,Byte> itemEffects;
	public static final byte itemEffectFloat = 0;
	public static final byte itemEffectBecomeDangerous = 1;
	
	public static final String nbtTagnameBeamDepth    = "xpLen";
	
	private static XP_BlockMagnet __blockMagnet = mod_XPMagnet.blockMagnet;	
	private static final int __magnetItemDelayBeforeCanPickup = 5;
	private static Random __rand = new Random();
	
	/******************
	 * INITIALIZATION *
	 ******************/
	
	// For the initialize method
	private static boolean __staticMembersInitialized = false;

	// Constructor used in my code; used when creating new TileEntity and obtaining a packet client-side
	public XP_TileEntityMagnet(int i, int j, int k) {
		this();
		this.xCoord = i;
		this.yCoord = j;
		this.zCoord = k;
		
	}
	
	// Constructor used on server when reading a saved TileEntity from NBT
	public XP_TileEntityMagnet() {
		__initializeStaticMembers();
		this.blockType = __blockMagnet;
	}

	private static void __initializeStaticMembers() {
		if (!__staticMembersInitialized) {
			
			__staticMembersInitialized = true;
			
			itemEffects = new HashMap(64);
			itemEffects.put(Block.oreGold.blockID, itemEffectFloat);
			itemEffects.put(Block.oreIron.blockID, itemEffectFloat);
			itemEffects.put(Block.blockGold.blockID, itemEffectFloat);
			itemEffects.put(Block.blockSteel.blockID, itemEffectFloat);
			itemEffects.put(Block.anvil.blockID, itemEffectFloat);
			itemEffects.put(Block.cauldron.blockID, itemEffectFloat);
			itemEffects.put(Block.doorSteel.blockID, itemEffectFloat);
			itemEffects.put(Block.fenceIron.blockID, itemEffectFloat);
			itemEffects.put(Block.rail.blockID, itemEffectFloat);
			itemEffects.put(Block.railDetector.blockID, itemEffectFloat);
			itemEffects.put(Block.railPowered.blockID, itemEffectFloat);
			itemEffects.put(Item.ingotIron.shiftedIndex, itemEffectFloat);
			itemEffects.put(Item.ingotGold.shiftedIndex, itemEffectFloat);
			itemEffects.put(Item.swordSteel.shiftedIndex, itemEffectFloat);
			itemEffects.put(Item.swordGold.shiftedIndex, itemEffectFloat);
			itemEffects.put(Item.pickaxeSteel.shiftedIndex, itemEffectFloat);
			itemEffects.put(Item.pickaxeGold.shiftedIndex, itemEffectFloat);
			itemEffects.put(Item.axeSteel.shiftedIndex, itemEffectFloat);
			itemEffects.put(Item.axeGold.shiftedIndex, itemEffectFloat);
			itemEffects.put(Item.shovelSteel.shiftedIndex, itemEffectFloat);
			itemEffects.put(Item.shovelGold.shiftedIndex, itemEffectFloat);
			itemEffects.put(Item.hoeSteel.shiftedIndex, itemEffectFloat);
			itemEffects.put(Item.hoeGold.shiftedIndex, itemEffectFloat);
			itemEffects.put(Item.helmetSteel.shiftedIndex, itemEffectFloat);
			itemEffects.put(Item.helmetGold.shiftedIndex, itemEffectFloat);
			itemEffects.put(Item.plateSteel.shiftedIndex, itemEffectFloat);
			itemEffects.put(Item.plateGold.shiftedIndex, itemEffectFloat);
			itemEffects.put(Item.legsSteel.shiftedIndex, itemEffectFloat);
			itemEffects.put(Item.legsGold.shiftedIndex, itemEffectFloat);
			itemEffects.put(Item.bootsSteel.shiftedIndex, itemEffectFloat);
			itemEffects.put(Item.bootsGold.shiftedIndex, itemEffectFloat);
			itemEffects.put(Item.minecartEmpty.shiftedIndex, itemEffectFloat);
			itemEffects.put(Item.minecartCrate.shiftedIndex, itemEffectFloat);
			itemEffects.put(Item.minecartPowered.shiftedIndex, itemEffectFloat);
			itemEffects.put(mod_FCBetterThanWolves.fcCauldron.blockID, itemEffectFloat);
			
			itemEffects.put(mod_FCBetterThanWolves.fcSoulforgedSteelBlock.blockID, itemEffectBecomeDangerous);
			itemEffects.put(mod_FCBetterThanWolves.fcSteel.shiftedIndex, itemEffectBecomeDangerous);
			itemEffects.put(mod_FCBetterThanWolves.fcRefinedPickAxe.shiftedIndex, itemEffectBecomeDangerous);
			itemEffects.put(mod_FCBetterThanWolves.fcRefinedShovel.shiftedIndex, itemEffectBecomeDangerous);
			itemEffects.put(mod_FCBetterThanWolves.fcRefinedHoe.shiftedIndex, itemEffectBecomeDangerous);
			itemEffects.put(mod_FCBetterThanWolves.fcBattleAxe.shiftedIndex, itemEffectBecomeDangerous);
			itemEffects.put(mod_FCBetterThanWolves.fcMattock.shiftedIndex, itemEffectBecomeDangerous);
			itemEffects.put(mod_FCBetterThanWolves.fcRefinedAxe.shiftedIndex, itemEffectBecomeDangerous);
			itemEffects.put(mod_FCBetterThanWolves.fcRefinedSword.shiftedIndex, itemEffectBecomeDangerous);
			itemEffects.put(mod_FCBetterThanWolves.fcArmorPlate.shiftedIndex, itemEffectBecomeDangerous);
			itemEffects.put(mod_FCBetterThanWolves.fcPlateHelm.shiftedIndex, itemEffectBecomeDangerous);
			itemEffects.put(mod_FCBetterThanWolves.fcPlateBreastPlate.shiftedIndex, itemEffectBecomeDangerous);
			itemEffects.put(mod_FCBetterThanWolves.fcPlateLeggings.shiftedIndex, itemEffectBecomeDangerous);
			itemEffects.put(mod_FCBetterThanWolves.fcPlateBoots.shiftedIndex, itemEffectBecomeDangerous);
			itemEffects.put(mod_FCBetterThanWolves.fcBroadheadArrowhead.shiftedIndex, itemEffectBecomeDangerous);
			itemEffects.put(mod_FCBetterThanWolves.fcBroadheadArrow.shiftedIndex, itemEffectBecomeDangerous);
		}
	}
	
	/*******************
	 * BEAM DIMENSIONS *
	 *******************/
	
	public double getBeamMinX()    { return (double)this.xCoord;        }
	public double getBeamCenterX() { return (double)this.xCoord + 0.5D; }
	public double getBeamMaxX()    { return (double)this.xCoord + 1.0D; }
	
	public double getBeamMinY(double depth) { return (double)this.yCoord - depth; }
	public double getBeamMinY()             { return this.getBeamMinY(this.beamDepth); }
	public double getBeamMaxY()             { return (double)this.yCoord; }
	
	public double getBeamMinZ()    { return (double)this.zCoord;        }
	public double getBeamCenterZ() { return (double)this.zCoord + 0.5D; }
	public double getBeamMaxZ()    { return (double)this.zCoord + 1.0D; }

	/**********
	 * EVENTS *
	 **********/

	@Override // TileEntity
	public void updateEntity() {
		this.__tickMetalEntities();
	}
	
	@Override // TileEntity
	public void receiveClientEvent(int eventID, int parameter) {
		this.__updateBeamDepth();
		this.__updateBlockPowerLevel();
	}
	
	/*****************
	 * SERIALIZATION *
	 *****************/

	@Override // TileEntity
	public Packet getDescriptionPacket() {
		ByteArrayOutputStream dataBytes = new ByteArrayOutputStream();
		DataOutputStream data = new DataOutputStream(dataBytes);
		
		try {
			data.writeInt(this.xCoord);
			data.writeShort(this.yCoord);
			data.writeInt(this.zCoord);
			data.writeByte(mod_XPMagnet.tileEntIdMagnet);
			this.writeMagnetData(data);
		} catch (java.io.IOException e) {
			e.printStackTrace();
		}
		
		Packet packet = new Packet250CustomPayload(mod_XPMagnet.chanTileEntity, dataBytes.toByteArray()); 
		
		return packet; 
	}

	public void writeMagnetData(DataOutputStream data) throws java.io.IOException {
		data.writeDouble(this.beamDepth);
	}
	
	public void readMagnetData(DataInputStream data) throws java.io.IOException {
		this.beamDepth = data.readDouble();
	}
	
	@Override // TileEntity
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		this.beamDepth = nbt.getDouble(nbtTagnameBeamDepth);
	}
	
	@Override // TileEntity
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setDouble(nbtTagnameBeamDepth, this.beamDepth);
	}
	
	
	/***************
	 * DEPTH GAUGE *
	 ***************/
	
	private int __prevPowerLevel = -1;
	
	private void __updateBlockPowerLevel() {
		int powerLevel = (this.beamDepth < beamDepthIndicatorLevels[0]) ?
		                 0 : (this.beamDepth < beamDepthIndicatorLevels[1]) ?
		                 1 : 2;
		
		if (powerLevel != this.__prevPowerLevel) {
			this.__prevPowerLevel = powerLevel;
			__blockMagnet.setFlagPowerLevel(this.worldObj, this.xCoord, this.yCoord, this.zCoord, powerLevel);
		}
	}
	
	/**************
	 * MECH POWER *
	 **************/
	
	// Reads the mechanical power flag from block metadata
	public boolean getBlockMechState() {
		return __blockMagnet.getFlagMechInput(this.worldObj, this.xCoord, this.yCoord, this.zCoord);
	}
	
	
	/**************
	 * BEAM DEPTH *
	 **************/
	
	// Called to reevaluate the beam's max allowed depth (based on mechanical power and blocks below the magnet)	
	private void __updateBeamDepth() {
		double range = 0.0D;
		
		int i = this.xCoord;
		int j = this.yCoord;
		int k = this.zCoord;
		
		// Beam is only present when mech powered
		if (this.getBlockMechState()) { 
			
			// Check up to two blocks below (in case there's a fence down there)
			for (int c = 0; c < 2; c++) {
				--j;
				int blockId = this.worldObj.getBlockId(i, j, k);
				
				if (blockId > 0) {
					Block block = Block.blocksList[blockId];
					
					// TODO: If there is a more appropriate method of doing this, please find it.
					// Currently, the block collision bounding box is updated and used, even though
					// it seems to be engineered for a different purpose, and it contains absolute
					// coordinates rather than relative
					block.setBlockBoundsBasedOnState(this.worldObj, i, j, k);
					AxisAlignedBB aabb = block.getCollisionBoundingBoxFromPool(this.worldObj, i, j, k);
					
					if (aabb == null) {
						range += 1.0D;
					} else {
						range += 1.0D - aabb.maxY + (double)j;
						break;
					}
					
				} else {
					range += 1.0D;
				}
			}
			
			// We checked further than the beam is allowed to go, so range may be too big.  
			range = Math.min(range, maxBeamDepth);
		}

		this.beamDepth = range;
	}
	
	
	private AxisAlignedBB __getMagnetAABBWithSize(double size) {
		return AxisAlignedBB.getAABBPool().addOrModifyAABBInPool(this.getBeamMinX(), this.getBeamMinY(size), this.getBeamMinZ(),
		                                                         this.getBeamMaxX(), this.getBeamMaxY(),     this.getBeamMaxZ());
	}
	
	/******
	 * FX *
	 ******/
	
	public void emitBeamParticles(Random rand) {
		double x, y, z;
		float scale;
		
		if (this.getBlockMechState()) {
			x = this.getBeamMinX() + rand.nextDouble();
			y = this.getBeamMaxY() - rand.nextDouble() * this.beamDepth;
			z = this.getBeamMinZ() + rand.nextDouble();
			scale = (0.4F * rand.nextFloat()) + 0.3F;
			
			XP_UtilFX.spawnMagBeamParticle(x, y, z, 0.0D, 0.0D, 0.0D, scale);
		}
	}
	
	/**********************
	 * Affecting Entities *
	 **********************/
	
	private static final int[]    __armorSlotsToAffect          = new int[]    {2,3};
	private static final float[]  __wornMetalStripChancesBySlot = new float[]  {-0.0F, -0.0F, 0.036F, 0.012F};
	private static final double[] __wornMetalSlowFactorsByCount = new double[] {1.0D, 0.09D, 0.04D, 0.04D, 0.04D};
	private static final int[]    __wornSFSAttackValuesBySlot   = new int[]    {0, 0, 2, 1};
	private static final int      __sfsArmorDamagePerTick       = 2;
	
	// Search for entities and call the other methods in this section as necessary 
	private void __tickMetalEntities() {
		AxisAlignedBB aabb = this.__getMagnetAABBWithSize(this.beamDepth);
		List<Entity> entities = this.worldObj.getEntitiesWithinAABB(Entity.class, aabb);
		
		for (Entity e : entities) {
			if (e instanceof EntityItem) {
				EntityItem eItem = (EntityItem)e;
				int itemId = eItem.item.itemID;
				
				Byte effect = itemEffects.get(itemId);
				
				if (effect != null) {
					switch(effect) {
					case itemEffectFloat:
						this.__liftItem(eItem);
						break;
					case itemEffectBecomeDangerous:
						this.__makeItemDangerous(eItem);
						break;
					}
				}
				
			} else if (e instanceof EntityMinecart) {
				this.__slowMinecart((EntityMinecart)e);
				
			} else if (e instanceof EntityLiving) {
				this.__affectArmoredEntity((EntityLiving)e);
			}
		}
	}
	
	// Called each tick to lift iron items
	private void __liftItem(EntityItem e) {
		e.delayBeforeCanPickup = __magnetItemDelayBeforeCanPickup;
		
		e.motionX *= 0.60D;
		e.motionY *= 0.75D;
		e.motionZ *= 0.60D;
		
		e.motionX += (this.getBeamCenterX() - e.posX) * 0.02D;
		e.motionY += 0.005D;
		e.motionZ += (this.getBeamCenterZ() - e.posZ) * 0.02D;
		
		// Counteract item gravity
		e.motionY += 0.03999999910593033D;
	}
	
	// Makes SFS items dangerous.  Not yet certain how this will be implemented.
	private void __makeItemDangerous(EntityItem e) {
		// TODO
	}
	
	// Called each tick on a minecart to slow it down/hold it in place
	private void __slowMinecart(EntityMinecart e) {
		// TODO
	}

	// Called each tick on an armored EntityLiving to hold it in place and steal armor or damage it (for SFS armor).
	private void __affectArmoredEntity(EntityLiving e) {
		int damageToCauseEntity = 0;
		int wornMetalItems      = 0; // used to determine how much the player is slowed
		
		for (int slot : __armorSlotsToAffect) {
			
			ItemStack armorItem = e.getCurrentArmor(slot);
			
			if (armorItem != null) {
				
				switch(itemEffects.get(armorItem.itemID)) {
				case itemEffectFloat:
					wornMetalItems++;
					if (!this.worldObj.isRemote){
						if (__rand.nextFloat() < __wornMetalStripChancesBySlot[slot]) {
							this.__stripArmorFromSlot(e, slot);
						}
					}
					break;
					
				case itemEffectBecomeDangerous:
					if (!this.worldObj.isRemote){ 
						armorItem.damageItem(__sfsArmorDamagePerTick, e);
						damageToCauseEntity += __wornSFSAttackValuesBySlot[slot];
					}
					break;
				}
				
			}
		}
		
		if (wornMetalItems > 0) {
			double slowFactor = __wornMetalSlowFactorsByCount[wornMetalItems];
			e.motionX *= slowFactor;
			e.motionY *= slowFactor;
			e.motionZ *= slowFactor;
		}
		
		if (damageToCauseEntity > 0) {
			e.attackEntityFrom(DamageSource.generic, damageToCauseEntity);
		}
	}
	
	private void __stripArmorFromSlot(EntityLiving e, int slot) {
		// Ensure item is spawned inside beam
		double x = Math.max(Math.min(e.posX, this.getBeamMaxX() - 0.001D), this.getBeamMinX() + 0.001D);
		double y = Math.max(Math.min(e.posY, this.getBeamMaxY() - 0.001D), this.getBeamMinY() + 0.001D);
		double z = Math.max(Math.min(e.posZ, this.getBeamMaxZ() - 0.001D), this.getBeamMinZ() + 0.001D);
		
		EntityItem item = new EntityItem(this.worldObj, x, y, z, e.getCurrentArmor(slot));
		item.delayBeforeCanPickup = __magnetItemDelayBeforeCanPickup;
		item.setVelocity(0.0D, 0.0D, 0.0D);
		this.worldObj.spawnEntityInWorld(item);
		
		e.setCurrentItemOrArmor(slot, null);
	}
}
