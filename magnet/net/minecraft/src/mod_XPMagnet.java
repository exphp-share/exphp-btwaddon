package net.minecraft.src;

import net.minecraft.client.Minecraft;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class mod_XPMagnet extends BaseMod {
	
	// Blocks
	public static XP_BlockMagnet blockMagnet = new XP_BlockMagnet(180);
	
	// Channels
	public static final String chanSpawnEntity = "XP^S";
	public static final String chanTileEntity = "XP^T";
	
	// Tile Entity Modes
	public static final int tileEntIdMagnet = 1;
	
	// Image resources and indices of graphics therein
	public static final String magTerrainFilename = "/exphp/magnet-terrain.png";
	public static final int magTerrainMagnetTop      = 0;
	public static final int magTerrainMagnetSide     = 1; // and 2, 3
	public static final int magTerrainMagnetBottom   = 4; // and 5, 6
	
	// Render/Model IDs
	public static int rendIdMagnet;
	
	
	public mod_XPMagnet() {
		this.nameModStuff();
	}
	
	public void nameModStuff() {
		blockMagnet.setBlockName("Magnet");
	}

	@Override // BaseMod
	public String getVersion() {
		return "0.01";
	}
	
	@Override // BaseMod
	public void load() {
		Minecraft mc = ModLoader.getMinecraftInstance();
		
		ModLoader.registerBlock(blockMagnet);
		
		ModLoader.registerTileEntity(XP_TileEntityMagnet.class,"ExpHP: Magnet");
		
		mc.renderEngine.getTexture(this.magTerrainFilename);

		rendIdMagnet = ModLoader.getUniqueBlockModelID(this, false);
		
		ModLoader.registerPacketChannel(this, this.chanSpawnEntity);
		ModLoader.registerPacketChannel(this, this.chanTileEntity);
	}
	
	@Override // BaseMod
	public void addRenderer(Map renderers) {
		
	}
	
	@Override // BaseMod
	public void modsLoaded(){
		Minecraft mc = ModLoader.getMinecraftInstance();
		
		FCRecipes.AddAnvilRecipe(new ItemStack(blockMagnet), new Object[] {"S**S", "====", "SiiS", "S@@S",
		                                                                     'S', Block.stone,
		                                                                     '*', mod_FCBetterThanWolves.fcGear,
		                                                                     '=', mod_FCBetterThanWolves.fcSteel,
		                                                                     'i', Block.torchRedstoneActive,
		                                                                     '@', mod_FCBetterThanWolves.fcPolishedLapis});
	}
	
	@Override // BaseMod
	public boolean renderWorldBlock(RenderBlocks renderer, IBlockAccess world, int i, int j, int k, Block block, int renderType) {
		if (renderType == this.rendIdMagnet) {
			return blockMagnet.renderMagnetIn3D(renderer, world, i, j, k);
		} else {
			return false;
		}
	}
	
	@Override // BaseMod
	public void clientCustomPayload(NetClientHandler client, Packet250CustomPayload packet) {
		try {
			if (packet.channel.equals(this.chanSpawnEntity)) {
				this.__handleSpawnEntityPacket(client, packet);
			} else if (packet.channel.equals(this.chanTileEntity)) {
				this.__handleTileEntityPacket(client, packet);
			}
		} catch (IOException var23) {
			var23.printStackTrace();
		}
	}
	
	private void __handleSpawnEntityPacket(NetClientHandler client, Packet250CustomPayload packet) throws IOException {
		return;
	}
	
	private void __handleTileEntityPacket(NetClientHandler client, Packet250CustomPayload packet) throws IOException {
		WorldClient world = ModLoader.getMinecraftInstance().theWorld;
		DataInputStream data = new DataInputStream(new ByteArrayInputStream(packet.data));
		
		int i = data.readInt();
		int j = data.readShort();
		int k = data.readInt();
		int id = data.readByte();

		TileEntity tileEnt = null;
		TileEntity oldTileEnt = world.getBlockTileEntity(i, j, k);
		
		if (id == this.tileEntIdMagnet) {
			XP_TileEntityMagnet tileEntMagnet;
			// If the tile entity is already there, keep it there. Else, create it.
			tileEntMagnet = (oldTileEnt instanceof XP_TileEntityMagnet) ? (XP_TileEntityMagnet)oldTileEnt
			                                                            : new XP_TileEntityMagnet(i, j, k);
			// In any case, update the tile entity with the magnet state data.
			tileEntMagnet.readMagnetData(data);
			tileEnt = tileEntMagnet;
		} else {
			// TODO: Unrecognized id
		}
		
		if (tileEnt != null) {
			world.setBlockTileEntity(i, j, k, tileEnt);
		}
		
	}
}
