package net.minecraft.src;

public class RecipesCrafting {
	public void addRecipes(CraftingManager craftingManager) {
		craftingManager.addRecipe(new ItemStack(Block.chest), new Object[]{"###", "# #", "###", '#', Block.planks});
		craftingManager.addRecipe(new ItemStack(Block.stoneOvenIdle), new Object[]{"###", "# #", "###", '#', Block.cobblestone});
		craftingManager.addRecipe(new ItemStack(Block.workbench), new Object[]{"##", "##", '#', Block.planks});
	}
}
