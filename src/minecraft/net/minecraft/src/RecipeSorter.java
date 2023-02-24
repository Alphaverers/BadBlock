package net.minecraft.src;

import java.util.Comparator;

class RecipeSorter implements Comparator {
	final CraftingManager craftingManager;

	RecipeSorter(CraftingManager craftingManager) {
		this.craftingManager = craftingManager;
	}

	public int compareRecipes(CraftingRecipe craftingRecipe1, CraftingRecipe craftingRecipe2) {
		return craftingRecipe2.getRecipeSize() < craftingRecipe1.getRecipeSize() ? -1 : (craftingRecipe2.getRecipeSize() > craftingRecipe1.getRecipeSize() ? 1 : 0);
	}

	public int compare(Object object1, Object object2) {
		return this.compareRecipes((CraftingRecipe)object1, (CraftingRecipe)object2);
	}
}
