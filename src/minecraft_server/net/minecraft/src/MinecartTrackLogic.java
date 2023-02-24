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
	final BlockMinecartTrack minecartTrack;

	public MinecartTrackLogic(BlockMinecartTrack minecartTrack, World world, int trackX, int trackY, int trackZ) {
		this.minecartTrack = minecartTrack;
		this.connectedTracks = new ArrayList();
		this.worldObj = world;
		this.trackX = trackX;
		this.trackY = trackY;
		this.trackZ = trackZ;
		this.trackMetadata = world.getBlockMetadata(trackX, trackY, trackZ);
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
		return this.worldObj.getBlockId(x, y, z) == this.minecartTrack.blockID ? true : (this.worldObj.getBlockId(x, y + 1, z) == this.minecartTrack.blockID ? true : this.worldObj.getBlockId(x, y - 1, z) == this.minecartTrack.blockID);
	}

	private MinecartTrackLogic getMinecartTrackLogic(ChunkPosition chunkPos) {
		return this.worldObj.getBlockId(chunkPos.x, chunkPos.y, chunkPos.z) == this.minecartTrack.blockID ? new MinecartTrackLogic(this.minecartTrack, this.worldObj, chunkPos.x, chunkPos.y, chunkPos.z) : (this.worldObj.getBlockId(chunkPos.x, chunkPos.y + 1, chunkPos.z) == this.minecartTrack.blockID ? new MinecartTrackLogic(this.minecartTrack, this.worldObj, chunkPos.x, chunkPos.y + 1, chunkPos.z) : (this.worldObj.getBlockId(chunkPos.x, chunkPos.y - 1, chunkPos.z) == this.minecartTrack.blockID ? new MinecartTrackLogic(this.minecartTrack, this.worldObj, chunkPos.x, chunkPos.y - 1, chunkPos.z) : null));
	}

	private boolean isConnectedTo(MinecartTrackLogic trackLogic) {
		for(int i2 = 0; i2 < this.connectedTracks.size(); ++i2) {
			ChunkPosition chunkPosition3 = (ChunkPosition)this.connectedTracks.get(i2);
			if(chunkPosition3.x == trackLogic.trackX && chunkPosition3.z == trackLogic.trackZ) {
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

	private int getAdjacentTracks() {
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

	private boolean handleKeyPress(MinecartTrackLogic trackLogic) {
		if(this.isConnectedTo(trackLogic)) {
			return true;
		} else if(this.connectedTracks.size() == 2) {
			return false;
		} else if(this.connectedTracks.size() == 0) {
			return true;
		} else {
			ChunkPosition chunkPosition2 = (ChunkPosition)this.connectedTracks.get(0);
			return trackLogic.trackY == this.trackY && chunkPosition2.y == this.trackY ? true : true;
		}
	}

	private void connectToNeighbor(MinecartTrackLogic trackLogic) {
		this.connectedTracks.add(new ChunkPosition(trackLogic.trackX, trackLogic.trackY, trackLogic.trackZ));
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
			if(this.worldObj.getBlockId(this.trackX, this.trackY + 1, this.trackZ - 1) == this.minecartTrack.blockID) {
				b6 = 4;
			}

			if(this.worldObj.getBlockId(this.trackX, this.trackY + 1, this.trackZ + 1) == this.minecartTrack.blockID) {
				b6 = 5;
			}
		}

		if(b6 == 1) {
			if(this.worldObj.getBlockId(this.trackX + 1, this.trackY + 1, this.trackZ) == this.minecartTrack.blockID) {
				b6 = 2;
			}

			if(this.worldObj.getBlockId(this.trackX - 1, this.trackY + 1, this.trackZ) == this.minecartTrack.blockID) {
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
			return minecartTrackLogic4.handleKeyPress(this);
		}
	}

	public void place(boolean flag) {
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

			if(flag) {
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
			if(this.worldObj.getBlockId(this.trackX, this.trackY + 1, this.trackZ - 1) == this.minecartTrack.blockID) {
				b6 = 4;
			}

			if(this.worldObj.getBlockId(this.trackX, this.trackY + 1, this.trackZ + 1) == this.minecartTrack.blockID) {
				b6 = 5;
			}
		}

		if(b6 == 1) {
			if(this.worldObj.getBlockId(this.trackX + 1, this.trackY + 1, this.trackZ) == this.minecartTrack.blockID) {
				b6 = 2;
			}

			if(this.worldObj.getBlockId(this.trackX - 1, this.trackY + 1, this.trackZ) == this.minecartTrack.blockID) {
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
				if(minecartTrackLogic8.handleKeyPress(this)) {
					minecartTrackLogic8.connectToNeighbor(this);
				}
			}
		}

	}

	static int getAdjacentTracks(MinecartTrackLogic trackLogic) {
		return trackLogic.getAdjacentTracks();
	}
}
