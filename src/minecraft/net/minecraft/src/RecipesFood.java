package net.minecraft.src;

public class RecipesFood {
	public void addRecipes(CraftingManager craftingManager) {
		craftingManager.addRecipe(new ItemStack(Item.bowlSoup), new Object[]{"Y", "X", "#", 'X', Block.mushroomBrown, 'Y', Block.mushroomRed, '#', Item.bowlEmpty});
		craftingManager.addRecipe(new ItemStack(Item.bowlSoup), new Object[]{"Y", "X", "#", 'X', Block.mushroomRed, 'Y', Block.mushroomBrown, '#', Item.bowlEmpty});
	}
}
