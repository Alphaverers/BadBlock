package net.minecraft.src;

import adaptive.ds5java.DS5Connection;
import adaptive.ds5java.DS5InstrPlayerUpdate;
import adaptive.ds5java.DS5InstrRGBUpdate;
import adaptive.ds5java.DS5InstrTriggerUpdate;
import adaptive.ds5java.DS5Packet;

public class AdaptiveHandler extends Thread {
	public DS5Connection ds5 = new DS5Connection();
	public boolean DS5enabled = false;
	public DS5Packet ds5_conf_default = new DS5Packet();
	public DS5Packet ds5_conf_ingame_aimblock = new DS5Packet();
	public DS5Packet ds5_conf_ingame_aimentity = new DS5Packet();
	public DS5Packet ds5_conf_ingame_aimnone = new DS5Packet();
	public long lastPacketSent = 0L;
	public int lastPacketType = -1;

	public void ConstructDefaultPacket() {
		this.ds5_conf_default.AddInstruction(new DS5InstrRGBUpdate(0, 0, 0, 255));
		this.ds5_conf_default.AddInstruction(new DS5InstrPlayerUpdate(0, false, false, true, false, false));
		this.ds5_conf_default.AddInstruction(new DS5InstrTriggerUpdate(0, DS5InstrTriggerUpdate.TriggerIndex.Left, DS5InstrTriggerUpdate.TriggerMode.Normal, new int[]{0}));
		this.ds5_conf_default.AddInstruction(new DS5InstrTriggerUpdate(0, DS5InstrTriggerUpdate.TriggerIndex.Right, DS5InstrTriggerUpdate.TriggerMode.Normal, new int[]{0}));
		this.ds5_conf_ingame_aimblock.AddInstruction(new DS5InstrRGBUpdate(0, 0, 0, 0));
		this.ds5_conf_ingame_aimblock.AddInstruction(new DS5InstrPlayerUpdate(0, false, false, true, false, false));
		this.ds5_conf_ingame_aimblock.AddInstruction(new DS5InstrTriggerUpdate(0, DS5InstrTriggerUpdate.TriggerIndex.Left, DS5InstrTriggerUpdate.TriggerMode.VibrateTrigger, new int[]{3}));
		this.ds5_conf_ingame_aimblock.AddInstruction(new DS5InstrTriggerUpdate(0, DS5InstrTriggerUpdate.TriggerIndex.Right, DS5InstrTriggerUpdate.TriggerMode.AutomaticGun, new int[]{4, 8, 5}));
		this.ds5_conf_ingame_aimentity.AddInstruction(new DS5InstrRGBUpdate(0, 0, 0, 0));
		this.ds5_conf_ingame_aimentity.AddInstruction(new DS5InstrPlayerUpdate(0, false, false, true, false, false));
		this.ds5_conf_ingame_aimentity.AddInstruction(new DS5InstrTriggerUpdate(0, DS5InstrTriggerUpdate.TriggerIndex.Left, DS5InstrTriggerUpdate.TriggerMode.Normal, new int[]{0}));
		this.ds5_conf_ingame_aimentity.AddInstruction(new DS5InstrTriggerUpdate(0, DS5InstrTriggerUpdate.TriggerIndex.Right, DS5InstrTriggerUpdate.TriggerMode.Bow, new int[]{3, 6, 1, 1}));
		this.ds5_conf_ingame_aimnone.AddInstruction(new DS5InstrRGBUpdate(0, 0, 0, 0));
		this.ds5_conf_ingame_aimnone.AddInstruction(new DS5InstrPlayerUpdate(0, false, false, true, false, false));
		this.ds5_conf_ingame_aimnone.AddInstruction(new DS5InstrTriggerUpdate(0, DS5InstrTriggerUpdate.TriggerIndex.Left, DS5InstrTriggerUpdate.TriggerMode.Normal, new int[]{0}));
		this.ds5_conf_ingame_aimnone.AddInstruction(new DS5InstrTriggerUpdate(0, DS5InstrTriggerUpdate.TriggerIndex.Right, DS5InstrTriggerUpdate.TriggerMode.Normal, new int[]{0}));
	}

	public void UpdatePLEDBasedOnDiff(DS5Packet dS5Packet1) {
		DS5InstrPlayerUpdate dS5InstrPlayerUpdate2 = (DS5InstrPlayerUpdate)dS5Packet1.ds5Instructions.get(1);
		boolean z3;
		boolean z4;
		boolean z5;
		boolean z6;
		boolean z7;
		switch(InputHandler.mc.options.difficulty) {
		case 0:
			z7 = false;
			z6 = false;
			z4 = false;
			z3 = false;
			z5 = true;
			break;
		case 1:
			z7 = false;
			z5 = false;
			z3 = false;
			z6 = true;
			z4 = true;
			break;
		case 2:
			z6 = false;
			z4 = false;
			z7 = true;
			z5 = true;
			z3 = true;
			break;
		case 3:
			z5 = false;
			z7 = true;
			z6 = true;
			z4 = true;
			z3 = true;
			break;
		case 4:
			z7 = true;
			z6 = true;
			z5 = true;
			z4 = true;
			z3 = true;
			break;
		default:
			z7 = false;
			z6 = false;
			z5 = false;
			z4 = false;
			z3 = false;
		}

		dS5InstrPlayerUpdate2.p1 = z3;
		dS5InstrPlayerUpdate2.p2 = z4;
		dS5InstrPlayerUpdate2.p3 = z5;
		dS5InstrPlayerUpdate2.p4 = z6;
		dS5InstrPlayerUpdate2.p5 = z7;
	}

	public void UpdateIngamePacket(DS5Packet dS5Packet1) {
		DS5InstrRGBUpdate dS5InstrRGBUpdate2 = (DS5InstrRGBUpdate)dS5Packet1.ds5Instructions.get(0);
		this.UpdatePLEDBasedOnDiff(dS5Packet1);
		if(InputHandler.mc.options.difficulty == 4 && (float)InputHandler.mc.thePlayer.health != 0.0F) {
			float f6 = (float)(System.currentTimeMillis() % 2000L) / 1000.0F;
			float f4 = 0.5F + (f6 <= 1.0F ? 0.5F * f6 : (1.0F - (f6 - 1.0F)) * 0.5F);
			int i5 = (int)(255.0F * f4);
			dS5InstrRGBUpdate2.r = dS5InstrRGBUpdate2.g = dS5InstrRGBUpdate2.b = i5;
		} else if((float)InputHandler.mc.thePlayer.health <= 4.0F && System.currentTimeMillis() % 2000L >= 1000L) {
			dS5InstrRGBUpdate2.r = dS5InstrRGBUpdate2.g = dS5InstrRGBUpdate2.b = 0;
		} else {
			float[] f3 = ColorUtil.BlendColorA((float)InputHandler.mc.thePlayer.health / 20.0F, 1.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F);
			dS5InstrRGBUpdate2.r = (int)(f3[0] * 255.0F);
			dS5InstrRGBUpdate2.g = (int)(f3[1] * 255.0F);
			dS5InstrRGBUpdate2.b = (int)(f3[2] * 255.0F);
		}
	}

	public void SendPacket() {
		if(System.currentTimeMillis() - this.lastPacketSent > 16L) {
			this.lastPacketSent = System.currentTimeMillis();
			if((InputHandler.mc == null || InputHandler.mc.thePlayer == null) && this.lastPacketType != 0) {
				this.ds5.Send(this.ds5_conf_default);
				this.lastPacketType = 0;
			} else if(InputHandler.mc.thePlayer != null) {
				this.lastPacketType = 1;
				DS5Packet dS5Packet1 = null;
				if(InputHandler.mc.objectMouseOver != null) {
					switch(InputHandler.mc.objectMouseOver.typeOfHit) {
					case 0:
						dS5Packet1 = this.ds5_conf_ingame_aimblock;
						break;
					case 1:
						dS5Packet1 = this.ds5_conf_ingame_aimentity;
					}
				} else {
					dS5Packet1 = this.ds5_conf_ingame_aimnone;
				}

				this.UpdateIngamePacket(dS5Packet1);
				this.ds5.Send(dS5Packet1);
			}
		}

	}

	public AdaptiveHandler() {
		this.ConstructDefaultPacket();
	}

	public void run() {
		System.out.println("");
		if(this.ds5.Connect()) {
			while(true) {
				this.SendPacket();

				try {
					Thread.sleep(64L);
				} catch (InterruptedException interruptedException2) {
				}
			}
		}

	}
}
