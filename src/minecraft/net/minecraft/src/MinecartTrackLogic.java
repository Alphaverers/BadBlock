package net.minecraft.src;

import java.util.ArrayList;
import java.util.List;

class MinecartTrackLogic {
	private World worldObj;
	private int trackX;
	private int trackY;
	private int trackZ;
	private int trackMetadata;
	private List connectedTracks;
	final ClassIf a;

	public MinecartTrackLogic(ClassIf classIf1, World world2, int i3, int i4, int i5) {
		this.a = classIf1;
		this.connectedTracks = new ArrayList();
		this.worldObj = world2;
		this.trackX = i3;
		this.trackY = i4;
		this.trackZ = i5;
		this.trackMetadata = world2.getBlockMetadata(i3, i4, i5);
		this.calculateConnectedTracks();
	}

	private void calculateConnectedTracks() {
		this.connectedTracks.clear();
		if(this.trackMetadata == 0) {
			this.connectedTracks.add(new ChunkPosition(this.trackX, this.trackY, this.trackZ - 1));
			this.connectedTracks.add(new ChunkPosition(this.trackX, this.trackY, this.trackZ + 1));
		} else if(this.trackMetadata == 1) {
			this.connectedTracks.add(new ChunkPosition(this.trackX - 1, this.trackY, this.trackZ));
			this.connectedTracks.add(new ChunkPosition(this.trackX + 1, this.trackY, this.trackZ));
		} else if(this.trackMetadata == 2) {
			this.connectedTracks.add(new ChunkPosition(this.trackX - 1, this.trackY, this.trackZ));
			this.connectedTracks.add(new ChunkPosition(this.trackX + 1, this.trackY + 1, this.trackZ));
		} else if(this.trackMetadata == 3) {
			this.connectedTracks.add(new ChunkPosition(this.trackX - 1, this.trackY + 1, this.trackZ));
			this.connectedTracks.add(new ChunkPosition(this.trackX + 1, this.trackY, this.trackZ));
		} else if(this.trackMetadata == 4) {
			this.connectedTracks.add(new ChunkPosition(this.trackX, this.trackY + 1, this.trackZ - 1));
			this.connectedTracks.add(new ChunkPosition(this.trackX, this.trackY, this.trackZ + 1));
		} else if(this.trackMetadata == 5) {
			this.connectedTracks.add(new ChunkPosition(this.trackX, this.trackY, this.trackZ - 1));
			this.connectedTracks.add(new ChunkPosition(this.trackX, this.trackY + 1, this.trackZ + 1));
		} else if(this.trackMetadata == 6) {
			this.connectedTracks.add(new ChunkPosition(this.trackX + 1, this.trackY, this.trackZ));
			this.connectedTracks.add(new ChunkPosition(this.trackX, this.trackY, this.trackZ + 1));
		} else if(this.trackMetadata == 7) {
			this.connectedTracks.add(new ChunkPosition(this.trackX - 1, this.trackY, this.trackZ));
			this.connectedTracks.add(new ChunkPosition(this.trackX, this.trackY, this.trackZ + 1));
		} else if(this.trackMetadata == 8) {
			this.connectedTracks.add(new ChunkPosition(this.trackX - 1, this.trackY, this.trackZ));
			this.connectedTracks.add(new ChunkPosition(this.trackX, this.trackY, this.trackZ - 1));
		} else if(this.trackMetadata == 9) {
			this.connectedTracks.add(new ChunkPosition(this.trackX + 1, this.trackY, this.trackZ));
			this.connectedTracks.add(new ChunkPosition(this.trackX, this.trackY, this.trackZ - 1));
		}

	}

	private void refreshConnectedTracks() {
		for(int i1 = 0; i1 < this.connectedTracks.size(); ++i1) {
			MinecartTrackLogic minecartTrackLogic2 = this.getMinecartTrackLogic((ChunkPosition)this.connectedTracks.get(i1));
			if(minecartTrackLogic2 != null && minecartTrackLogic2.isConnectedTo(this)) {
				this.connectedTracks.set(i1, new ChunkPosition(minecartTrackLogic2.trackX, minecartTrackLogic2.trackY, minecartTrackLogic2.trackZ));
			} else {
				this.connectedTracks.remove(i1--);
			}
		}

	}

	private boolean isMinecartTrack(int x, int y, int z) {
		return this.worldObj.getBlockId(x, y, z) == this.a.blockID || this.worldObj.getBlockId(x, y + 1, z) == this.a.blockID || this.worldObj.getBlockId(x, y - 1, z) == this.a.blockID;
	}

	private MinecartTrackLogic getMinecartTrackLogic(ChunkPosition chunkPos) {
		return this.worldObj.getBlockId(chunkPos.x, chunkPos.y, chunkPos.z) == this.a.blockID ? new MinecartTrackLogic(this.a, this.worldObj, chunkPos.x, chunkPos.y, chunkPos.z) : (this.worldObj.getBlockId(chunkPos.x, chunkPos.y + 1, chunkPos.z) == this.a.blockID ? new MinecartTrackLogic(this.a, this.worldObj, chunkPos.x, chunkPos.y + 1, chunkPos.z) : (this.worldObj.getBlockId(chunkPos.x, chunkPos.y - 1, chunkPos.z) == this.a.blockID ? new MinecartTrackLogic(this.a, this.worldObj, chunkPos.x, chunkPos.y - 1, chunkPos.z) : null));
	}

	private boolean isConnectedTo(MinecartTrackLogic minecartTrackLogic) {
		for(int i2 = 0; i2 < this.connectedTracks.size(); ++i2) {
			ChunkPosition chunkPosition3 = (ChunkPosition)this.connectedTracks.get(i2);
			if(chunkPosition3.x == minecartTrackLogic.trackX && chunkPosition3.z == minecartTrackLogic.trackZ) {
				return true;
			}
		}

		return false;
	}

	private boolean isInTrack(int x, int y, int z) {
		for(int i4 = 0; i4 < this.connectedTracks.size(); ++i4) {
			ChunkPosition chunkPosition5 = (ChunkPosition)this.connectedTracks.get(i4);
			if(chunkPosition5.x == x && chunkPosition5.z == z) {
				return true;
			}
		}

		return false;
	}

	public int getAdjacentTracks() {
		int i1 = 0;
		if(this.isMinecartTrack(this.trackX, this.trackY, this.trackZ - 1)) {
			++i1;
		}

		if(this.isMinecartTrack(this.trackX, this.trackY, this.trackZ + 1)) {
			++i1;
		}

		if(this.isMinecartTrack(this.trackX - 1, this.trackY, this.trackZ)) {
			++i1;
		}

		if(this.isMinecartTrack(this.trackX + 1, this.trackY, this.trackZ)) {
			++i1;
		}

		return i1;
	}

	private boolean canConnectTo(MinecartTrackLogic minecartTrackLogic) {
		if(this.isConnectedTo(minecartTrackLogic)) {
			return true;
		} else if(this.connectedTracks.size() == 2) {
			return false;
		} else if(this.connectedTracks.size() == 0) {
			return true;
		} else {
			ChunkPosition chunkPosition2 = (ChunkPosition)this.connectedTracks.get(0);
			if(minecartTrackLogic.trackY == this.trackY && chunkPosition2.y == this.trackY) {
				;
			}

			return true;
		}
	}

	private void connectToNeighbor(MinecartTrackLogic minecartTrackLogic) {
		this.connectedTracks.add(new ChunkPosition(minecartTrackLogic.trackX, minecartTrackLogic.trackY, minecartTrackLogic.trackZ));
		boolean z2 = this.isInTrack(this.trackX, this.trackY, this.trackZ - 1);
		boolean z3 = this.isInTrack(this.trackX, this.trackY, this.trackZ + 1);
		boolean z4 = this.isInTrack(this.trackX - 1, this.trackY, this.trackZ);
		boolean z5 = this.isInTrack(this.trackX + 1, this.trackY, this.trackZ);
		byte b6 = -1;
		if(z2 || z3) {
			b6 = 0;
		}

		if(z4 || z5) {
			b6 = 1;
		}

		if(z3 && z5 && !z2 && !z4) {
			b6 = 6;
		}

		if(z3 && z4 && !z2 && !z5) {
			b6 = 7;
		}

		if(z2 && z4 && !z3 && !z5) {
			b6 = 8;
		}

		if(z2 && z5 && !z3 && !z4) {
			b6 = 9;
		}

		if(b6 == 0) {
			if(this.worldObj.getBlockId(this.trackX, this.trackY + 1, this.trackZ - 1) == this.a.blockID) {
				b6 = 4;
			}

			if(this.worldObj.getBlockId(this.trackX, this.trackY + 1, this.trackZ + 1) == this.a.blockID) {
				b6 = 5;
			}
		}

		if(b6 == 1) {
			if(this.worldObj.getBlockId(this.trackX + 1, this.trackY + 1, this.trackZ) == this.a.blockID) {
				b6 = 2;
			}

			if(this.worldObj.getBlockId(this.trackX - 1, this.trackY + 1, this.trackZ) == this.a.blockID) {
				b6 = 3;
			}
		}

		if(b6 < 0) {
			b6 = 0;
		}

		this.worldObj.setBlockMetadataWithNotify(this.trackX, this.trackY, this.trackZ, b6);
	}

	private boolean canConnectFrom(int x, int y, int z) {
		MinecartTrackLogic minecartTrackLogic4 = this.getMinecartTrackLogic(new ChunkPosition(x, y, z));
		if(minecartTrackLogic4 == null) {
			return false;
		} else {
			minecartTrackLogic4.refreshConnectedTracks();
			return minecartTrackLogic4.canConnectTo(this);
		}
	}

	public void place(boolean powered) {
		boolean z2 = this.canConnectFrom(this.trackX, this.trackY, this.trackZ - 1);
		boolean z3 = this.canConnectFrom(this.trackX, this.trackY, this.trackZ + 1);
		boolean z4 = this.canConnectFrom(this.trackX - 1, this.trackY, this.trackZ);
		boolean z5 = this.canConnectFrom(this.trackX + 1, this.trackY, this.trackZ);
		byte b6 = -1;
		if((z2 || z3) && !z4 && !z5) {
			b6 = 0;
		}

		if((z4 || z5) && !z2 && !z3) {
			b6 = 1;
		}

		if(z3 && z5 && !z2 && !z4) {
			b6 = 6;
		}

		if(z3 && z4 && !z2 && !z5) {
			b6 = 7;
		}

		if(z2 && z4 && !z3 && !z5) {
			b6 = 8;
		}

		if(z2 && z5 && !z3 && !z4) {
			b6 = 9;
		}

		if(b6 == -1) {
			if(z2 || z3) {
				b6 = 0;
			}

			if(z4 || z5) {
				b6 = 1;
			}

			if(powered) {
				if(z3 && z5) {
					b6 = 6;
				}

				if(z4 && z3) {
					b6 = 7;
				}

				if(z5 && z2) {
					b6 = 9;
				}

				if(z2 && z4) {
					b6 = 8;
				}
			} else {
				if(z2 && z4) {
					b6 = 8;
				}

				if(z5 && z2) {
					b6 = 9;
				}

				if(z4 && z3) {
					b6 = 7;
				}

				if(z3 && z5) {
					b6 = 6;
				}
			}
		}

		if(b6 == 0) {
			if(this.worldObj.getBlockId(this.trackX, this.trackY + 1, this.trackZ - 1) == this.a.blockID) {
				b6 = 4;
			}

			if(this.worldObj.getBlockId(this.trackX, this.trackY + 1, this.trackZ + 1) == this.a.blockID) {
				b6 = 5;
			}
		}

		if(b6 == 1) {
			if(this.worldObj.getBlockId(this.trackX + 1, this.trackY + 1, this.trackZ) == this.a.blockID) {
				b6 = 2;
			}

			if(this.worldObj.getBlockId(this.trackX - 1, this.trackY + 1, this.trackZ) == this.a.blockID) {
				b6 = 3;
			}
		}

		if(b6 < 0) {
			b6 = 0;
		}

		this.trackMetadata = b6;
		this.calculateConnectedTracks();
		this.worldObj.setBlockMetadataWithNotify(this.trackX, this.trackY, this.trackZ, b6);

		for(int i7 = 0; i7 < this.connectedTracks.size(); ++i7) {
			MinecartTrackLogic minecartTrackLogic8 = this.getMinecartTrackLogic((ChunkPosition)this.connectedTracks.get(i7));
			if(minecartTrackLogic8 != null) {
				minecartTrackLogic8.refreshConnectedTracks();
				if(minecartTrackLogic8.canConnectTo(this)) {
					minecartTrackLogic8.connectToNeighbor(this);
				}
			}
		}

	}
}
