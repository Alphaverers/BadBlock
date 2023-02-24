package net.minecraft.src;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class CraftingManager {
	private static final CraftingManager instance = new CraftingManager();
	private List recipes = new ArrayList();

	public static final CraftingManager getInstance() {
		return instance;
	}

	private CraftingManager() {
		(new RecipesTools()).addRecipes(this);
		(new RecipesWeapons()).addRecipes(this);
		(new RecipesIngots()).addRecipes(this);
		(new RecipesFood()).addRecipes(this);
		(new RecipesCrafting()).addRecipes(this);
		(new RecipesArmor()).addRecipes(this);
		this.addRecipe(new ItemStack(Item.paper, 3), new Object[]{"###", '#', Item.reed});
		this.addRecipe(new ItemStack(Item.book, 1), new Object[]{"#", "#", "#", '#', Item.paper});
		this.addRecipe(new ItemStack(Block.fence, 2), new Object[]{"###", "###", '#', Item.stick});
		this.addRecipe(new ItemStack(Block.jukebox, 1), new Object[]{"###", "#X#", "###", '#', Block.planks, 'X', Item.diamond});
		this.addRecipe(new ItemStack(Block.bookshelf, 1), new Object[]{"###", "XXX", "###", '#', Block.planks, 'X', Item.book});
		this.addRecipe(new ItemStack(Block.blockSnow, 1), new Object[]{"##", "##", '#', Item.snowball});
		this.addRecipe(new ItemStack(Block.blockClay, 1), new Object[]{"##", "##", '#', Item.clay});
		this.addRecipe(new ItemStack(Block.brick, 1), new Object[]{"##", "##", '#', Item.brick});
		this.addRecipe(new ItemStack(Block.cloth, 1), new Object[]{"###", "###", "###", '#', Item.silk});
		this.addRecipe(new ItemStack(Block.tnt, 1), new Object[]{"X#X", "#X#", "X#X", 'X', Item.gunpowder, '#', Block.sand});
		this.addRecipe(new ItemStack(Block.stairSingle, 3), new Object[]{"###", '#', Block.cobblestone});
		this.addRecipe(new ItemStack(Block.ladder, 1), new Object[]{"# #", "###", "# #", '#', Item.stick});
		this.addRecipe(new ItemStack(Item.doorWood, 1), new Object[]{"##", "##", "##", '#', Block.planks});
		this.addRecipe(new ItemStack(Item.doorSteel, 1), new Object[]{"##", "##", "##", '#', Item.ingotIron});
		this.addRecipe(new ItemStack(Item.sign, 1), new Object[]{"###", "###", " X ", '#', Block.planks, 'X', Item.stick});
		this.addRecipe(new ItemStack(Block.planks, 4), new Object[]{"#", '#', Block.wood});
		this.addRecipe(new ItemStack(Item.stick, 4), new Object[]{"#", "#", '#', Block.planks});
		this.addRecipe(new ItemStack(Block.torch, 4), new Object[]{"X", "#", 'X', Item.coal, '#', Item.stick});
		this.addRecipe(new ItemStack(Item.bowlEmpty, 4), new Object[]{"# #", " # ", '#', Block.planks});
		this.addRecipe(new ItemStack(Block.minecartTrack, 16), new Object[]{"X X", "X#X", "X X", 'X', Item.ingotIron, '#', Item.stick});
		this.addRecipe(new ItemStack(Item.minecartEmpty, 1), new Object[]{"# #", "###", '#', Item.ingotIron});
		this.addRecipe(new ItemStack(Item.minecartBox, 1), new Object[]{"A", "B", 'A', Block.chest, 'B', Item.minecartEmpty});
		this.addRecipe(new ItemStack(Item.minecartEngine, 1), new Object[]{"A", "B", 'A', Block.stoneOvenIdle, 'B', Item.minecartEmpty});
		this.addRecipe(new ItemStack(Item.boat, 1), new Object[]{"# #", "###", '#', Block.planks});
		this.addRecipe(new ItemStack(Item.bucketEmpty, 1), new Object[]{"# #", " # ", '#', Item.ingotIron});
		this.addRecipe(new ItemStack(Item.striker, 1), new Object[]{"A ", " B", 'A', Item.ingotIron, 'B', Item.flint});
		this.addRecipe(new ItemStack(Item.bread, 1), new Object[]{"###", '#', Item.wheat});
		this.addRecipe(new ItemStack(Block.stairCompactWood, 4), new Object[]{"#  ", "## ", "###", '#', Block.planks});
		this.addRecipe(new ItemStack(Block.stairCompactStone, 4), new Object[]{"#  ", "## ", "###", '#', Block.cobblestone});
		this.addRecipe(new ItemStack(Item.painting, 1), new Object[]{"###", "#X#", "###", '#', Item.stick, 'X', Block.cloth});
		this.addRecipe(new ItemStack(Item.appleGold, 1), new Object[]{"###", "#X#", "###", '#', Block.blockGold, 'X', Item.appleRed});
		this.addRecipe(new ItemStack(Block.lever, 1), new Object[]{"X", "#", '#', Block.cobblestone, 'X', Item.stick});
		this.addRecipe(new ItemStack(Block.torchRedstoneActive, 1), new Object[]{"X", "#", '#', Item.stick, 'X', Item.redstone});
		this.addRecipe(new ItemStack(Item.compass, 1), new Object[]{" # ", "#X#", " # ", '#', Item.ingotIron, 'X', Item.redstone});
		this.addRecipe(new ItemStack(Block.button, 1), new Object[]{"#", "#", '#', Block.stone});
		this.addRecipe(new ItemStack(Block.pressurePlateStone, 1), new Object[]{"###", '#', Block.stone});
		this.addRecipe(new ItemStack(Block.pressurePlateWood, 1), new Object[]{"###", '#', Block.planks});
		this.addRecipe(new ItemStack(Block.PillarBlock, 4), new Object[]{"#X#", "X X", "#X#", '#', Block.planks, 'X', Item.stick});
		this.addRecipe(new ItemStack(Block.QuadWindowGlassBlock, 1), new Object[]{" # ", "#X#", " # ", '#', Block.cobblestone, 'X', Block.glass});
		this.addRecipe(new ItemStack(Item.obsidianSword), new Object[]{"#", "#", "$", '#', Item.obsidianIngot, '$', Item.stick});
		this.addRecipe(new ItemStack(Item.obsidianShovel), new Object[]{"#", "$", "$", '#', Item.obsidianIngot, '$', Item.stick});
		this.addRecipe(new ItemStack(Item.obsidianPick), new Object[]{"###", " $ ", " $ ", '#', Item.obsidianIngot, '$', Item.stick});
		this.addRecipe(new ItemStack(Item.obsidianHoe), new Object[]{"##", " $", " $", '#', Item.obsidianIngot, '$', Item.stick});
		this.addRecipe(new ItemStack(Item.obsidianHoe), new Object[]{"##", "$ ", "$ ", '#', Item.obsidianIngot, '$', Item.stick});
		this.addRecipe(new ItemStack(Item.obsidianAxe), new Object[]{"##", "#$", " $", '#', Item.obsidianIngot, '$', Item.stick});
		this.addRecipe(new ItemStack(Item.obsidianAxe), new Object[]{"##", "$ ", "$ ", '#', Item.obsidianIngot, '$', Item.stick});
		this.addRecipe(new ItemStack(Item.obsidianArmor0), new Object[]{"###", "# #", '#', Item.obsidianIngot});
		this.addRecipe(new ItemStack(Item.obsidianArmor2), new Object[]{"###", "# #", "# #", '#', Item.obsidianIngot});
		this.addRecipe(new ItemStack(Item.obsidianArmor3), new Object[]{"# #", "# #", '#', Item.obsidianIngot});
		this.addRecipe(new ItemStack(Item.obsidianArmor1), new Object[]{"# #", "###", "###", '#', Item.obsidianIngot});
		this.addRecipe(new ItemStack(Block.woolPink), new Object[]{"#X", '#', Block.cloth, 'X', Item.pinkDye});
		this.addRecipe(new ItemStack(Block.woolBlue), new Object[]{"#X", '#', Block.cloth, 'X', Item.blueDye});
		this.addRecipe(new ItemStack(Block.woolGreen), new Object[]{"#X", '#', Block.cloth, 'X', Item.greenDye});
		this.addRecipe(new ItemStack(Block.woolBlack), new Object[]{"#X", '#', Block.cloth, 'X', Item.blackDye});
		this.addRecipe(new ItemStack(Item.edibleFire), new Object[]{"#", '#', Block.blueFireIdk});
		Collections.sort(this.recipes, new RecipeSorter(this));
		System.out.println(this.recipes.size() + " recipes");
	}

	void addRecipe(ItemStack itemStack1, Object... object2) {
		String string3 = "";
		int i4 = 0;
		int i5 = 0;
		int i6 = 0;
		if(object2[i4] instanceof String[]) {
			String[] string11 = (String[])((String[])object2[i4++]);

			for(int i8 = 0; i8 < string11.length; ++i8) {
				String string9 = string11[i8];
				++i6;
				i5 = string9.length();
				string3 = string3 + string9;
			}
		} else {
			while(object2[i4] instanceof String) {
				String string7 = (String)object2[i4++];
				++i6;
				i5 = string7.length();
				string3 = string3 + string7;
			}
		}

		HashMap hashMap12;
		int i15;
		for(hashMap12 = new HashMap(); i4 < object2.length; i4 += 2) {
			Character character13 = (Character)object2[i4];
			i15 = 0;
			if(object2[i4 + 1] instanceof Item) {
				i15 = ((Item)object2[i4 + 1]).shiftedIndex;
			} else if(object2[i4 + 1] instanceof Block) {
				i15 = ((Block)object2[i4 + 1]).blockID;
			}

			hashMap12.put(character13, i15);
		}

		int[] i14 = new int[i5 * i6];

		for(i15 = 0; i15 < i5 * i6; ++i15) {
			char c10 = string3.charAt(i15);
			if(hashMap12.containsKey(c10)) {
				i14[i15] = ((Integer)hashMap12.get(c10)).intValue();
			} else {
				i14[i15] = -1;
			}
		}

		this.recipes.add(new CraftingRecipe(i5, i6, i14, itemStack1));
	}

	public ItemStack findMatchingRecipe(int[] i1) {
		for(int i2 = 0; i2 < this.recipes.size(); ++i2) {
			CraftingRecipe craftingRecipe3 = (CraftingRecipe)this.recipes.get(i2);
			if(craftingRecipe3.matches(i1)) {
				return craftingRecipe3.getCraftingResult(i1);
			}
		}

		return null;
	}
}
