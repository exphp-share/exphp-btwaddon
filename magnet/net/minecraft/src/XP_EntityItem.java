package net.minecraft.src;

public class XP_EntityItem extends EntityItem {
	public XP_EntityItem(World world, double x, double y, double z, ItemStack itemStack) {
		super(world, x, y, z, itemStack);
	}
	
	public XP_EntityItem(EntityItem original) {
		super(original.worldObj, original.posX, original.posY, original.posZ, original.item);
	}
}
