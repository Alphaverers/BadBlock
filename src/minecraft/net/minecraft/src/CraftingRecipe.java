package net.minecraft.src;

public class CraftingRecipe {
	private int width;
	private int height;
	private int[] ingredientMap;
	private ItemStack resultStack;
	public final int resultId;

	public CraftingRecipe(int width, int height, int[] ingredientMap, ItemStack resultStack) {
		this.resultId = resultStack.itemID;
		this.width = width;
		this.height = height;
		this.ingredientMap = ingredientMap;
		this.resultStack = resultStack;
	}

	public boolean matches(int[] i1) {
		for(int i2 = 0; i2 <= 3 - this.width; ++i2) {
			for(int i3 = 0; i3 <= 3 - this.height; ++i3) {
				if(this.checkMatch(i1, i2, i3, true)) {
					return true;
				}

				if(this.checkMatch(i1, i2, i3, false)) {
					return true;
				}
			}
		}

		return false;
	}

	private boolean checkMatch(int[] i1, int i2, int i3, boolean z4) {
		for(int i5 = 0; i5 < 3; ++i5) {
			for(int i6 = 0; i6 < 3; ++i6) {
				int i7 = i5 - i2;
				int i8 = i6 - i3;
				int i9 = -1;
				if(i7 >= 0 && i8 >= 0 && i7 < this.width && i8 < this.height) {
					if(z4) {
						i9 = this.ingredientMap[this.width - i7 - 1 + i8 * this.width];
					} else {
						i9 = this.ingredientMap[i7 + i8 * this.width];
					}
				}

				if(i1[i5 + i6 * 3] != i9) {
					return false;
				}
			}
		}

		return true;
	}

	public ItemStack getCraftingResult(int[] i1) {
		return new ItemStack(this.resultStack.itemID, this.resultStack.stackSize);
	}

	public int getRecipeSize() {
		return this.width * this.height;
	}
}
