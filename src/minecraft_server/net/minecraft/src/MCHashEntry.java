package net.minecraft.src;

class MCHashEntry {
	final int hashEntry;
	Object valueEntry;
	MCHashEntry nextEntry;
	final int slotHash;

	MCHashEntry(int slotHash, int hashEntry, Object valueEntry, MCHashEntry nextEntry) {
		this.valueEntry = valueEntry;
		this.nextEntry = nextEntry;
		this.hashEntry = hashEntry;
		this.slotHash = slotHash;
	}

	public final int getHash() {
		return this.hashEntry;
	}

	public final Object getValue() {
		return this.valueEntry;
	}

	public final boolean equals(Object object) {
		if(!(object instanceof MCHashEntry)) {
			return false;
		} else {
			MCHashEntry mCHashEntry2 = (MCHashEntry)object;
			Integer integer3 = this.getHash();
			Integer integer4 = mCHashEntry2.getHash();
			if(integer3 == integer4 || integer3 != null && integer3.equals(integer4)) {
				Object object5 = this.getValue();
				Object object6 = mCHashEntry2.getValue();
				if(object5 == object6 || object5 != null && object5.equals(object6)) {
					return true;
				}
			}

			return false;
		}
	}

	public final int hashCode() {
		return MCHashTable.getHash(this.hashEntry);
	}

	public final String toString() {
		return this.getHash() + "=" + this.getValue();
	}
}
