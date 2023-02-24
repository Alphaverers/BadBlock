package net.minecraft.src;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.lwjgl.opengl.GL11;

public class RenderManager {
	private Map entityRenderMap;
	public static RenderManager instance = new RenderManager();
	private FontRenderer fontRenderer;
	public static double renderPosX;
	public static double renderPosY;
	public static double renderPosZ;
	public RenderEngine renderEngine;
	public ItemRenderer itemRenderer;
	public World worldObj;
	public EntityPlayer player;
	public float playerViewY;
	public float playerViewX;
	public GameSettings options;
	public double viewerPosX;
	public double viewerPosY;
	public double viewerPosZ;

	private RenderManager() {
		(this.entityRenderMap = new HashMap()).put(EntitySpider.class, new RenderSpider());
		this.entityRenderMap.put(EntityPig.class, new RenderPig(new ModelPig(), new ModelPig(0.5F), 0.7F));
		this.entityRenderMap.put(EntitySheep.class, new RenderSheep(new ModelSheep(), new ModelSheepFur(), 0.7F));
		this.entityRenderMap.put(EntityCow.class, new RenderCow(new ModelCow(), 0.7F));
		this.entityRenderMap.put(EntityChicken.class, new RenderChicken(new ModelChicken(), 0.3F));
		this.entityRenderMap.put(EntityCreeper.class, new RenderCreeper());
		this.entityRenderMap.put(EntitySkeleton.class, new RenderLiving(new ModelSkeleton(), 0.5F));
		this.entityRenderMap.put(EntityZombie.class, new RenderLiving(new ModelZombie(), 0.5F));
		this.entityRenderMap.put(EntitySlime.class, new RenderSlime(new ModelSlime(16), new ModelSlime(0), 0.25F));
		this.entityRenderMap.put(EntityPlayer.class, new RenderPlayer());
		this.entityRenderMap.put(EntityGiantZombie.class, new RenderGiantZombie(new ModelZombie(), 0.5F, 6.0F));
		this.entityRenderMap.put(EntityLiving.class, new RenderLiving(new ModelBiped(), 0.5F));
		this.entityRenderMap.put(Entity.class, new RenderEntity());
		this.entityRenderMap.put(EntityPainting.class, new RenderPainting());
		this.entityRenderMap.put(EntityArrow.class, new RenderArrow());
		this.entityRenderMap.put(EntitySnowball.class, new RenderSnowball());
		this.entityRenderMap.put(EntityItem.class, new RenderItem());
		this.entityRenderMap.put(EntityTNTPrimed.class, new RenderTNTPrimed());
		this.entityRenderMap.put(EntityFallingSand.class, new RenderFallingSand());
		this.entityRenderMap.put(EntityMinecart.class, new RenderMinecart());
		this.entityRenderMap.put(EntityBoat.class, new RenderBoat());
		this.entityRenderMap.put(MobGiant.class, new RenderGiant(new ModelZombie(), 0.5F, 6.0F));
		Iterator iterator1 = this.entityRenderMap.values().iterator();

		while(iterator1.hasNext()) {
			((Render)iterator1.next()).setRenderManager(this);
		}

	}

	public Render getEntityClassRenderObject(Class entityClass) {
		Render render2 = (Render)this.entityRenderMap.get(entityClass);
		if(render2 == null && entityClass != Entity.class) {
			render2 = this.getEntityClassRenderObject(entityClass.getSuperclass());
			this.entityRenderMap.put(entityClass, render2);
		}

		return render2;
	}

	public Render getEntityRenderObject(Entity entity) {
		return this.getEntityClassRenderObject(entity.getClass());
	}

	public void cacheActiveRenderInfo(World world, RenderEngine renderEngine, FontRenderer fontRenderer, EntityPlayer entityPlayer, GameSettings gameSettings, float renderPartialTick) {
		this.worldObj = world;
		this.renderEngine = renderEngine;
		this.options = gameSettings;
		this.player = entityPlayer;
		this.fontRenderer = fontRenderer;
		this.playerViewY = entityPlayer.prevRotationYaw + (entityPlayer.rotationYaw - entityPlayer.prevRotationYaw) * renderPartialTick;
		this.playerViewX = entityPlayer.prevRotationPitch + (entityPlayer.rotationPitch - entityPlayer.prevRotationPitch) * renderPartialTick;
		this.viewerPosX = entityPlayer.lastTickPosX + (entityPlayer.posX - entityPlayer.lastTickPosX) * (double)renderPartialTick;
		this.viewerPosY = entityPlayer.lastTickPosY + (entityPlayer.posY - entityPlayer.lastTickPosY) * (double)renderPartialTick;
		this.viewerPosZ = entityPlayer.lastTickPosZ + (entityPlayer.posZ - entityPlayer.lastTickPosZ) * (double)renderPartialTick;
	}

	public void renderEntity(Entity entity, float renderPartialTick) {
		double d3 = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)renderPartialTick;
		double d5 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)renderPartialTick;
		double d7 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)renderPartialTick;
		float f9 = entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * renderPartialTick;
		float f10 = entity.getBrightness(renderPartialTick);
		GL11.glColor3f(f10, f10, f10);
		this.renderEntityWithPosYaw(entity, d3 - renderPosX, d5 - renderPosY, d7 - renderPosZ, f9, renderPartialTick);
	}

	public void renderEntityWithPosYaw(Entity entity, double x, double y, double z, float yaw, float pitch) {
		Render render10 = this.getEntityRenderObject(entity);
		if(render10 != null) {
			render10.doRender(entity, x, y, z, yaw, pitch);
			render10.doRenderShadowAndFire(entity, x, y, z, yaw, pitch);
		}

	}

	public void set(World world) {
		this.worldObj = world;
	}

	public double getDistanceToCamera(double x, double y, double z) {
		double d7 = x - this.viewerPosX;
		double d9 = y - this.viewerPosY;
		double d11 = z - this.viewerPosZ;
		return d7 * d7 + d9 * d9 + d11 * d11;
	}

	public FontRenderer getFontRenderer() {
		return this.fontRenderer;
	}
}
