package com.dyn.achievements;

import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;

import com.dyn.achievements.achievement.AchievementPlus;
import com.dyn.achievements.achievement.Requirements;
import com.dyn.achievements.achievement.Requirements.CraftRequirement;
import com.dyn.achievements.achievement.Requirements.KillRequirement;
import com.dyn.achievements.achievement.Requirements.PickupRequirement;
import com.dyn.achievements.achievement.Requirements.SmeltRequirement;
import com.dyn.achievements.gui.AchHome;
import com.dyn.achievements.handlers.*;
import com.dyn.achievements.proxy.Proxy;
import com.dyn.achievements.reference.Reference;
import com.google.gson.JsonObject;
import com.rabbit.gui.GuiFoundation;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION)
public class AchievementsMod {

	public static KeyBinding guiKey;
	
	@Mod.Instance(Reference.MOD_ID)
	public static AchievementsMod instance;

	@SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.SERVER_PROXY_CLASS)
	public static Proxy proxy;
	
	public static Logger logger;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		logger = event.getModLog();

		Configuration configs = new Configuration(event.getSuggestedConfigurationFile());
		try {
			configs.load();
		} catch (RuntimeException e) {
			logger.warn(e);
		}

		FMLCommonHandler.instance().bus().register(this);

		this.guiKey = new KeyBinding("key.toggle.achievementui", Keyboard.KEY_N, "key.categories.toggle");

		ClientRegistry.registerKeyBinding(this.guiKey);
		
		FMLCommonHandler.instance().bus().register(new EventHandler());
		MinecraftForge.EVENT_BUS.register(new EventHandler());
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		Requirements r = new Requirements();
		
		//Crafting
		CraftRequirement cr2 = r.new CraftRequirement();
		cr2.setFromItemId(Block.getIdFromBlock(Blocks.oak_stairs));
		cr2.setAmountNeeded(1);
		r.addRequirement(cr2);
		
		//Smelting
		SmeltRequirement sr = r.new SmeltRequirement();
		sr.setFromItemId(265); //iron ingot
		sr.setAmountNeeded(1);
		r.addRequirement(sr);
		
		//Pick Up
		PickupRequirement pr = r.new PickupRequirement();
		pr.setFromItemId(344); //egg
		pr.setAmountNeeded(10);
		r.addRequirement(pr);
		
		//Stat - Not Implmented Yet
		/*StatRequirement st = r.new StatRequirement();
		st.eventStat = StatBase
		st.amount = 10;
		r.addRequirement(st);*/
				
		//Kill
		KillRequirement kr = r.new KillRequirement();
		kr.entityType = "Cow";
		kr.setAmountNeeded(1);
		r.addRequirement(kr);
		
		//Spawn - Not Implmented Yet
		/*SpawnRequirement sp = r.new SpawnRequirement();
		sp.entityType = "Chicken";
		sp.amount = 3;
		r.addRequirement(sp);*/
		AchievementPlus test = new AchievementPlus(r, "My Test Achievement", "this is a test to make sure that the achievement system works", 2168);

		for(int i=1;i<100;i++){
			AchievementPlus test2 = new AchievementPlus(r, "Achievement " + i, "this is a test", 2168);
		}
		
		/*JsonObject ache = test.achievementToJson();
		AchievementPlus test3 = new AchievementPlus();
		test3.JsonToAchievement(ache);*/
		
	}
	
	@SubscribeEvent
	public void onKeyInput(InputEvent.KeyInputEvent event) {

		if ((Minecraft.getMinecraft().currentScreen instanceof GuiChat)) {
			return;
		}
		if (this.guiKey.getIsKeyPressed()) {
				GuiFoundation.display(new AchHome());
		}
	}
}
