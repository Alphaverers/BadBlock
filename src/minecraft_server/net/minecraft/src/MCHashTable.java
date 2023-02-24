package net.minecraft.src;

public class MCHashTable {
	private transient MCHashEntry[] slots = new MCHashEntry[16];
	private transient int count;
	private int threshold = 12;
	private final float growFactor = 0.75F;
	private transient volatile int versionStamp;

	private static int computeHash(int hash) {
		hash ^= hash >>> 20 ^ hash >>> 12;
		return hash ^ hash >>> 7 ^ hash >>> 4;
	}

	private static int getSlotIndex(int i0, int i1) {
		return i0 & i1 - 1;
	}

	public Object lookup(int i1) {
		int i2 = computeHash(i1);

		for(MCHashEntry mCHashEntry3 = this.slots[getSlotIndex(i2, this.slots.length)]; mCHashEntry3 != null; mCHashEntry3 = mCHashEntry3.nextEntry) {
			if(mCHashEntry3.hashEntry == i1) {
				return mCHashEntry3.valueEntry;
			}
		}

		return null;
	}

	public boolean containsItem(int i1) {
		return this.lookupEntry(i1) != null;
	}

	final MCHashEntry lookupEntry(int i1) {
		int i2 = computeHash(i1);

		for(MCHashEntry mCHashEntry3 = this.slots[getSlotIndex(i2, this.slots.length)]; mCHashEntry3 != null; mCHashEntry3 = mCHashEntry3.nextEntry) {
			if(mCHashEntry3.hashEntry == i1) {
				return mCHashEntry3;
			}
		}

		return null;
	}

	public void addKey(int i1, Object object2) {
		int i3 = computeHash(i1);
		int i4 = getSlotIndex(i3, this.slots.length);

		for(MCHashEntry mCHashEntry5 = this.slots[i4]; mCHashEntry5 != null; mCHashEntry5 = mCHashEntry5.nextEntry) {
			if(mCHashEntry5.hashEntry == i1) {
				mCHashEntry5.valueEntry = object2;
			}
		}

		++this.versionStamp;
		this.insert(i3, i1, object2, i4);
	}

	private void grow(int i1) {
		MCHashEntry[] mCHashEntry2 = this.slots;
		int i3 = mCHashEntry2.length;
		if(i3 == 1073741824) {
			this.threshold = Integer.MAX_VALUE;
		} else {
			MCHashEntry[] mCHashEntry4 = new MCHashEntry[i1];
			this.copyTo(mCHashEntry4);
			this.slots = mCHashEntry4;
			this.threshold = (int)((float)i1 * this.growFactor);
		}
	}

	private void copyTo(MCHashEntry[] mCHashEntry1) {
		MCHashEntry[] mCHashEntry2 = this.slots;
		int i3 = mCHashEntry1.length;

		for(int i4 = 0; i4 < mCHashEntry2.length; ++i4) {
			MCHashEntry mCHashEntry5 = mCHashEntry2[i4];
			if(mCHashEntry5 != null) {
				mCHashEntry2[i4] = null;

				MCHashEntry mCHashEntry6;
				do {
					mCHashEntry6 = mCHashEntry5.nextEntry;
					int i7 = getSlotIndex(mCHashEntry5.slotHash, i3);
					mCHashEntry5.nextEntry = mCHashEntry1[i7];
					mCHashEntry1[i7] = mCHashEntry5;
					mCHashEntry5 = mCHashEntry6;
				} while(mCHashEntry6 != null);
			}
		}

	}

	public Object removeObject(int i1) {
		MCHashEntry mCHashEntry2 = this.removeEntry(i1);
		return mCHashEntry2 == null ? null : mCHashEntry2.valueEntry;
	}

	final MCHashEntry removeEntry(int i1) {
		int i2 = computeHash(i1);
		int i3 = getSlotIndex(i2, this.slots.length);
		MCHashEntry mCHashEntry4 = this.slots[i3];

		MCHashEntry mCHashEntry5;
		MCHashEntry mCHashEntry6;
		for(mCHashEntry5 = mCHashEntry4; mCHashEntry5 != null; mCHashEntry5 = mCHashEntry6) {
			mCHashEntry6 = mCHashEntry5.nextEntry;
			if(mCHashEntry5.hashEntry == i1) {
				++this.versionStamp;
				--this.count;
				if(mCHashEntry4 == mCHashEntry5) {
					this.slots[i3] = mCHashEntry6;
				} else {
					mCHashEntry4.nextEntry = mCHashEntry6;
				}

				return mCHashEntry5;
			}

			mCHashEntry4 = mCHashEntry5;
		}

		return mCHashEntry5;
	}

	public void clearMap() {
		++this.versionStamp;
		MCHashEntry[] mCHashEntry1 = this.slots;

		for(int i2 = 0; i2 < mCHashEntry1.length; ++i2) {
			mCHashEntry1[i2] = null;
		}

		this.count = 0;
	}

	private void insert(int i1, int i2, Object object3, int i4) {
		MCHashEntry mCHashEntry5 = this.slots[i4];
		this.slots[i4] = new MCHashEntry(i1, i2, object3, mCHashEntry5);
		if(this.count++ >= this.threshold) {
			this.grow(2 * this.slots.length);
		}

	}

	static int getHash(int i0) {
		return computeHash(i0);
	}
}
