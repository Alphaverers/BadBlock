package net.minecraft.src;

public class RecipesWeapons {
	private String[][] recipePatterns = new String[][]{{"X", "X", "#"}};
	private Object[][] recipeItems = new Object[][]{{Block.planks, Block.cobblestone, Item.ingotIron, Item.diamond, Item.ingotGold}, {Item.swordWood, Item.swordStone, Item.swordSteel, Item.swordDiamond, Item.swordGold}};

	public void addRecipes(CraftingManager craftingManager) {
		for(int i2 = 0; i2 < this.recipeItems[0].length; ++i2) {
			Object object3 = this.recipeItems[0][i2];

			for(int i4 = 0; i4 < this.recipeItems.length - 1; ++i4) {
				Item item5 = (Item)this.recipeItems[i4 + 1][i2];
				craftingManager.addRecipe(new ItemStack(item5), new Object[]{this.recipePatterns[i4], '#', Item.stick, 'X', object3});
			}
		}

		craftingManager.addRecipe(new ItemStack(Item.bow, 1), new Object[]{" #X", "# X", " #X", 'X', Item.silk, '#', Item.stick});
		craftingManager.addRecipe(new ItemStack(Item.arrow, 4), new Object[]{"X", "#", "Y", 'Y', Item.feather, 'X', Item.flint, '#', Item.stick});
	}
}
