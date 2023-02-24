package net.minecraft.src;

public class MCHashTable2 {
	private transient MCHashEntry2[] slots = new MCHashEntry2[16];
	private transient int count;
	private int threshold = 12;
	private final float growFactor = 0.75F;
	private transient volatile int versionStamp;

	private static int computeHash(long hash) {
		return computeHash((int)(hash ^ hash >>> 32));
	}

	private static int computeHash(int i0) {
		i0 ^= i0 >>> 20 ^ i0 >>> 12;
		return i0 ^ i0 >>> 7 ^ i0 >>> 4;
	}

	private static int getSlotIndex(int i0, int i1) {
		return i0 & i1 - 1;
	}

	public Object lookup(long j1) {
		int i3 = computeHash(j1);

		for(MCHashEntry2 mCHashEntry24 = this.slots[getSlotIndex(i3, this.slots.length)]; mCHashEntry24 != null; mCHashEntry24 = mCHashEntry24.nextEntry) {
			if(mCHashEntry24.hashEntry == j1) {
				return mCHashEntry24.valueEntry;
			}
		}

		return null;
	}

	public void addKey(long j1, Object object3) {
		int i4 = computeHash(j1);
		int i5 = getSlotIndex(i4, this.slots.length);

		for(MCHashEntry2 mCHashEntry26 = this.slots[i5]; mCHashEntry26 != null; mCHashEntry26 = mCHashEntry26.nextEntry) {
			if(mCHashEntry26.hashEntry == j1) {
				mCHashEntry26.valueEntry = object3;
			}
		}

		++this.versionStamp;
		this.insert(i4, j1, object3, i5);
	}

	private void grow(int i1) {
		MCHashEntry2[] mCHashEntry22 = this.slots;
		int i3 = mCHashEntry22.length;
		if(i3 == 1073741824) {
			this.threshold = Integer.MAX_VALUE;
		} else {
			MCHashEntry2[] mCHashEntry24 = new MCHashEntry2[i1];
			this.copyTo(mCHashEntry24);
			this.slots = mCHashEntry24;
			this.threshold = (int)((float)i1 * this.growFactor);
		}
	}

	private void copyTo(MCHashEntry2[] mCHashEntry21) {
		MCHashEntry2[] mCHashEntry22 = this.slots;
		int i3 = mCHashEntry21.length;

		for(int i4 = 0; i4 < mCHashEntry22.length; ++i4) {
			MCHashEntry2 mCHashEntry25 = mCHashEntry22[i4];
			if(mCHashEntry25 != null) {
				mCHashEntry22[i4] = null;

				MCHashEntry2 mCHashEntry26;
				do {
					mCHashEntry26 = mCHashEntry25.nextEntry;
					int i7 = getSlotIndex(mCHashEntry25.slotHash, i3);
					mCHashEntry25.nextEntry = mCHashEntry21[i7];
					mCHashEntry21[i7] = mCHashEntry25;
					mCHashEntry25 = mCHashEntry26;
				} while(mCHashEntry26 != null);
			}
		}

	}

	public Object removeObject(long j1) {
		MCHashEntry2 mCHashEntry23 = this.removeEntry(j1);
		return mCHashEntry23 == null ? null : mCHashEntry23.valueEntry;
	}

	final MCHashEntry2 removeEntry(long j1) {
		int i3 = computeHash(j1);
		int i4 = getSlotIndex(i3, this.slots.length);
		MCHashEntry2 mCHashEntry25 = this.slots[i4];

		MCHashEntry2 mCHashEntry26;
		MCHashEntry2 mCHashEntry27;
		for(mCHashEntry26 = mCHashEntry25; mCHashEntry26 != null; mCHashEntry26 = mCHashEntry27) {
			mCHashEntry27 = mCHashEntry26.nextEntry;
			if(mCHashEntry26.hashEntry == j1) {
				++this.versionStamp;
				--this.count;
				if(mCHashEntry25 == mCHashEntry26) {
					this.slots[i4] = mCHashEntry27;
				} else {
					mCHashEntry25.nextEntry = mCHashEntry27;
				}

				return mCHashEntry26;
			}

			mCHashEntry25 = mCHashEntry26;
		}

		return mCHashEntry26;
	}

	private void insert(int i1, long j2, Object object4, int i5) {
		MCHashEntry2 mCHashEntry26 = this.slots[i5];
		this.slots[i5] = new MCHashEntry2(i1, j2, object4, mCHashEntry26);
		if(this.count++ >= this.threshold) {
			this.grow(2 * this.slots.length);
		}

	}

	static int getHash(long j0) {
		return computeHash(j0);
	}
}
