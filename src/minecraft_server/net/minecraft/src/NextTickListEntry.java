package net.minecraft.src;

public class NextTickListEntry implements Comparable {
	private static long nextTickEntryID = 0L;
	public int xCoord;
	public int yCoord;
	public int zCoord;
	public int blockID;
	public long scheduledTime;
	private long tickEntryID = nextTickEntryID++;

	public NextTickListEntry(int xCoord, int yCoord, int zCoord, int blockID) {
		this.xCoord = xCoord;
		this.yCoord = yCoord;
		this.zCoord = zCoord;
		this.blockID = blockID;
	}

	public boolean equals(Object object) {
		if(!(object instanceof NextTickListEntry)) {
			return false;
		} else {
			NextTickListEntry nextTickListEntry2 = (NextTickListEntry)object;
			return this.xCoord == nextTickListEntry2.xCoord && this.yCoord == nextTickListEntry2.yCoord && this.zCoord == nextTickListEntry2.zCoord && this.blockID == nextTickListEntry2.blockID;
		}
	}

	public int hashCode() {
		return (this.xCoord * 128 * 1024 + this.zCoord * 128 + this.yCoord) * 256 + this.blockID;
	}

	public NextTickListEntry setScheduledTime(long scheduledTime) {
		this.scheduledTime = scheduledTime;
		return this;
	}

	public int comparer(NextTickListEntry nextTickListEntry1) {
		return this.scheduledTime < nextTickListEntry1.scheduledTime ? -1 : (this.scheduledTime > nextTickListEntry1.scheduledTime ? 1 : (this.tickEntryID < nextTickListEntry1.tickEntryID ? -1 : (this.tickEntryID > nextTickListEntry1.tickEntryID ? 1 : 0)));
	}

	public int compareTo(Object nextTickListEntry) {
		return this.comparer((NextTickListEntry)nextTickListEntry);
	}
}
