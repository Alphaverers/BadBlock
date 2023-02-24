package net.minecraft.src;

class NetworkWriterThread extends Thread {
	final NetworkManager netManager;

	NetworkWriterThread(NetworkManager netManager, String threadName) {
		super(threadName);
		this.netManager = netManager;
	}

	public void run() {
		Object object1 = NetworkManager.threadSyncObject;
		synchronized(NetworkManager.threadSyncObject) {
			++NetworkManager.numWriteThreads;
		}

		while(true) {
			boolean z11 = false;

			try {
				z11 = true;
				if(!NetworkManager.isRunning(this.netManager)) {
					z11 = false;
					break;
				}

				NetworkManager.sendNetworkPacket(this.netManager);
			} finally {
				if(z11) {
					Object object5 = NetworkManager.threadSyncObject;
					synchronized(NetworkManager.threadSyncObject) {
						--NetworkManager.numWriteThreads;
					}
				}
			}
		}

		object1 = NetworkManager.threadSyncObject;
		synchronized(NetworkManager.threadSyncObject) {
			--NetworkManager.numWriteThreads;
		}
	}
}
