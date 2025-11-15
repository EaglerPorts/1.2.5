package net.minecraft.src;

import net.peyton.eagler.minecraft.suppliers.EntitySupplier;

public class SpawnListEntry extends WeightedRandomChoice {
	public EntitySupplier entityClass;
	public int minGroupCount;
	public int maxGroupCount;

	public SpawnListEntry(EntitySupplier var1, int var2, int var3, int var4) {
		super(var2);
		this.entityClass = var1;
		this.minGroupCount = var3;
		this.maxGroupCount = var4;
	}
}
