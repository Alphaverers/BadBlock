package net.minecraft.src;

class MCHashEntry2 {
	final long hashEntry;
	Object valueEntry;
	MCHashEntry2 nextEntry;
	final int slotHash;

	MCHashEntry2(int slotHash, long hashEntry, Object valueEntry, MCHashEntry2 nextEntry) {
		this.valueEntry = valueEntry;
		this.nextEntry = nextEntry;
		this.hashEntry = hashEntry;
		this.slotHash = slotHash;
	}

	public final long getHash() {
		return this.hashEntry;
	}

	public final Object getValue() {
		return this.valueEntry;
	}

	public final boolean equals(Object object) {
		if(!(object instanceof MCHashEntry2)) {
			return false;
		} else {
			MCHashEntry2 mCHashEntry22 = (MCHashEntry2)object;
			Long long3 = this.getHash();
			Long long4 = mCHashEntry22.getHash();
			if(long3 == long4 || long3 != null && long3.equals(long4)) {
				Object object5 = this.getValue();
				Object object6 = mCHashEntry22.getValue();
				if(object5 == object6 || object5 != null && object5.equals(object6)) {
					return true;
				}
			}

			return false;
		}
	}

	public final int hashCode() {
		return MCHashTable2.getHash(this.hashEntry);
	}

	public final String toString() {
		return this.getHash() + "=" + this.getValue();
	}
}
