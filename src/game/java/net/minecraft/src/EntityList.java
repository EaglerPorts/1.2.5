package net.minecraft.src;

import java.util.HashMap;
import java.util.Map;

import net.peyton.eagler.minecraft.suppliers.EntitySupplier;

public class EntityList {
	private static Map stringToClassMapping = new HashMap();
	private static Map classToStringMapping = new HashMap();
	private static Map IDtoClassMapping = new HashMap();
	private static Map classToIDMapping = new HashMap();
	private static Map stringToIDMapping = new HashMap();
	public static HashMap entityEggs = new HashMap();

	private static void addMapping(Class var0, EntitySupplier var1, String var2, int var3) {
		stringToClassMapping.put(var2, var1);
		classToStringMapping.put(var0, var2);
		IDtoClassMapping.put(Integer.valueOf(var3), var1);
		classToIDMapping.put(var0, Integer.valueOf(var3));
		stringToIDMapping.put(var2, Integer.valueOf(var3));
	}

	private static void addMapping(Class var0, EntitySupplier var1, String var2, int var3, int var4, int var5) {
		addMapping(var0, var1, var2, var3);
		entityEggs.put(Integer.valueOf(var3), new EntityEggInfo(var3, var4, var5));
	}

	public static Entity createEntityByName(String var0, World var1) {
		Entity var2 = null;

		try {
			EntitySupplier var3 = (EntitySupplier)stringToClassMapping.get(var0.toLowerCase());
			if(var3 != null) {
				var2 = (Entity)var3.createEntity(var1);
			}
		} catch (Exception var4) {
			var4.printStackTrace();
		}

		return var2;
	}

	public static Entity createEntityFromNBT(NBTTagCompound var0, World var1) {
		Entity var2 = null;

		try {
			EntitySupplier var3 = (EntitySupplier)stringToClassMapping.get(var0.getString("id").toLowerCase());
			if(var3 != null) {
				var2 = (Entity)var3.createEntity(var1);
			}
		} catch (Exception var4) {
			var4.printStackTrace();
		}

		if(var2 != null) {
			var2.readFromNBT(var0);
		} else {
			System.out.println("Skipping Entity with id " + var0.getString("id"));
		}

		return var2;
	}

	public static Entity createEntityByID(int var0, World var1) {
		Entity var2 = null;

		try {
			EntitySupplier var3 = (EntitySupplier)IDtoClassMapping.get(Integer.valueOf(var0));
			if(var3 != null) {
				var2 = (Entity)var3.createEntity(var1);
			}
		} catch (Exception var4) {
			var4.printStackTrace();
		}

		if(var2 == null) {
			System.out.println("Skipping Entity with id " + var0);
		}

		return var2;
	}

	public static int getEntityID(Entity var0) {
		return ((Integer)classToIDMapping.get(var0.getClass())).intValue();
	}

	public static String getEntityString(Entity var0) {
		return (String)classToStringMapping.get(var0.getClass());
	}

	public static String getStringFromID(int var0) {
		Class var1 = (Class)IDtoClassMapping.get(Integer.valueOf(var0));
		return var1 != null ? (String)classToStringMapping.get(var1) : null;
	}

	public static int getEntityID(String var0) {
		return ((Integer)stringToIDMapping.get(var0.toLowerCase()));
	}

	static {
		addMapping(EntityItem.class, EntityItem::new, "Item", 1);
		addMapping(EntityXPOrb.class, EntityXPOrb::new, "XPOrb", 2);
		addMapping(EntityPainting.class, EntityPainting::new, "Painting", 9);
		addMapping(EntityArrow.class, EntityArrow::new, "Arrow", 10);
		addMapping(EntitySnowball.class, EntitySnowball::new, "Snowball", 11);
		addMapping(EntityFireball.class, EntityFireball::new, "Fireball", 12);
		addMapping(EntitySmallFireball.class, EntitySmallFireball::new, "SmallFireball", 13);
		addMapping(EntityEnderPearl.class, EntityEnderPearl::new, "ThrownEnderpearl", 14);
		addMapping(EntityEnderEye.class, EntityEnderEye::new, "EyeOfEnderSignal", 15);
		addMapping(EntityPotion.class, EntityPotion::new, "ThrownPotion", 16);
		addMapping(EntityExpBottle.class, EntityExpBottle::new, "ThrownExpBottle", 17);
		addMapping(EntityTNTPrimed.class, EntityTNTPrimed::new, "PrimedTnt", 20);
		addMapping(EntityFallingSand.class, EntityFallingSand::new, "FallingSand", 21);
		addMapping(EntityMinecart.class, EntityMinecart::new, "Minecart", 40);
		addMapping(EntityBoat.class, EntityBoat::new, "Boat", 41);
		addMapping(EntityLiving.class, EntityLiving::new, "Mob", 48);
		addMapping(EntityMob.class, EntityMob::new, "Monster", 49);
		addMapping(EntityCreeper.class, EntityCreeper::new, "Creeper", 50, 894731, 0);
		addMapping(EntitySkeleton.class, EntitySkeleton::new, "Skeleton", 51, 12698049, 4802889);
		addMapping(EntitySpider.class, EntitySpider::new, "Spider", 52, 3419431, 11013646);
		addMapping(EntityGiantZombie.class, EntityGiantZombie::new, "Giant", 53);
		addMapping(EntityZombie.class, EntityZombie::new, "Zombie", 54, '\uafaf', 7969893);
		addMapping(EntitySlime.class, EntitySlime::new, "Slime", 55, 5349438, 8306542);
		addMapping(EntityGhast.class, EntityGhast::new, "Ghast", 56, 16382457, 12369084);
		addMapping(EntityPigZombie.class, EntityPigZombie::new, "PigZombie", 57, 15373203, 5009705);
		addMapping(EntityEnderman.class, EntityEnderman::new, "Enderman", 58, 1447446, 0);
		addMapping(EntityCaveSpider.class, EntityCaveSpider::new, "CaveSpider", 59, 803406, 11013646);
		addMapping(EntitySilverfish.class, EntitySilverfish::new, "Silverfish", 60, 7237230, 3158064);
		addMapping(EntityBlaze.class, EntityBlaze::new, "Blaze", 61, 16167425, 16775294);
		addMapping(EntityMagmaCube.class, EntityMagmaCube::new, "LavaSlime", 62, 3407872, 16579584);
		addMapping(EntityDragon.class, EntityDragon::new, "EnderDragon", 63);
		addMapping(EntityPig.class, EntityPig::new, "Pig", 90, 15771042, 14377823);
		addMapping(EntitySheep.class, EntitySheep::new, "Sheep", 91, 15198183, 16758197);
		addMapping(EntityCow.class, EntityCow::new, "Cow", 92, 4470310, 10592673);
		addMapping(EntityChicken.class, EntityChicken::new, "Chicken", 93, 10592673, 16711680);
		addMapping(EntitySquid.class, EntitySquid::new, "Squid", 94, 2243405, 7375001);
		addMapping(EntityWolf.class, EntityWolf::new, "Wolf", 95, 14144467, 13545366);
		addMapping(EntityMooshroom.class, EntityMooshroom::new, "MushroomCow", 96, 10489616, 12040119);
		addMapping(EntitySnowman.class, EntitySnowman::new, "SnowMan", 97);
		addMapping(EntityOcelot.class, EntityOcelot::new, "Ozelot", 98, 15720061, 5653556);
		addMapping(EntityIronGolem.class, EntityIronGolem::new, "VillagerGolem", 99);
		addMapping(EntityVillager.class, EntityVillager::new, "Villager", 120, 5651507, 12422002);
		addMapping(EntityEnderCrystal.class, EntityEnderCrystal::new, "EnderCrystal", 200);
	}
}
