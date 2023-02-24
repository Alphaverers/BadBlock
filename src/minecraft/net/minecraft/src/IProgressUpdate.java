package net.minecraft.src;

public interface IProgressUpdate {
	void displayProgressMessage(String string1);

	void displayLoadingString(String string1);

	void setLoadingProgress(int i1);
}
