package net.minecraft.src;

public class BiomeGenHell extends BiomeGenBase {
	public BiomeGenHell(int var1) {
		super(var1);
		this.spawnableMonsterList.clear();
		this.spawnableCreatureList.clear();
		this.spawnableWaterCreatureList.clear();
		this.spawnableMonsterList.add(new SpawnListEntry(EntityGhast::new, 50, 4, 4));
		this.spawnableMonsterList.add(new SpawnListEntry(EntityPigZombie::new, 100, 4, 4));
		this.spawnableMonsterList.add(new SpawnListEntry(EntityMagmaCube::new, 1, 4, 4));
	}
}
