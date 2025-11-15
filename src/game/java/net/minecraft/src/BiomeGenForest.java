package net.minecraft.src;

import net.lax1dude.eaglercraft.Random;

public class BiomeGenForest extends BiomeGenBase {
	public BiomeGenForest(int var1) {
		super(var1);
		this.spawnableCreatureList.add(new SpawnListEntry(EntityWolf::new, 5, 4, 4));
		this.biomeDecorator.treesPerChunk = 10;
		this.biomeDecorator.grassPerChunk = 2;
	}

	public WorldGenerator getRandomWorldGenForTrees(Random var1) {
		return (WorldGenerator) (var1.nextInt(5) == 0 ? this.worldGenForest
				: (var1.nextInt(10) == 0 ? this.worldGenBigTree : this.worldGenTrees));
	}
}
