package io.github.chaosawakens;

import java.util.Locale;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mojang.serialization.Codec;
import io.github.chaosawakens.config.CAConfig;
import io.github.chaosawakens.data.ModItemModelGenerator;
import io.github.chaosawakens.data.ModLootTableProvider;
import io.github.chaosawakens.entity.AppleCowEntity;
import io.github.chaosawakens.entity.BeaverEntity;
import io.github.chaosawakens.entity.BrownAntEntity;
import io.github.chaosawakens.entity.EmeraldGatorEntity;
import io.github.chaosawakens.entity.EntEntity;
import io.github.chaosawakens.entity.GoldenAppleCowEntity;
import io.github.chaosawakens.entity.HerculesBeetleEntity;
import io.github.chaosawakens.entity.RainbowAntEntity;
import io.github.chaosawakens.entity.RedAntEntity;
import io.github.chaosawakens.entity.RoboSniperEntity;
import io.github.chaosawakens.entity.RubyBugEntity;
import io.github.chaosawakens.entity.TermiteEntity;
import io.github.chaosawakens.entity.UnstableAntEntity;
import io.github.chaosawakens.network.PacketHandler;
import io.github.chaosawakens.registry.*;
import io.github.chaosawakens.worldgen.ConfiguredStructures;
import io.github.chaosawakens.worldgen.EventBiomeLoading;
import io.github.chaosawakens.worldgen.ModBiomeFeatures;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.FlatChunkGenerator;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.DistExecutor.SafeRunnable;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import software.bernie.geckolib3.GeckoLib;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Mod(ChaosAwakens.MODID)
public class ChaosAwakens {
	
	public static final String MODID = "chaosawakens";
	public static final String MODNAME = "Chaos Awakens";
	
	public static ChaosAwakens INSTANCE;
	
	public static final Logger LOGGER = LogManager.getLogger();
	
	public static ResourceLocation locate(String name) {
		return new ResourceLocation(MODID, name);
	}
	
	public static String find(String name) {
		return MODID + ":" + name;
	}
	
	public ChaosAwakens() {
		INSTANCE = this;
		GeckoLib.initialize();
		
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
		
		IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
		eventBus.addListener(this::gatherData);
		ModBiomes.BIOMES.register(eventBus);
		ModItems.ITEMS.register(eventBus);
		ModBlocks.ITEMS.register(eventBus);
		ModBlocks.BLOCKS.register(eventBus);
		
		ModEntityTypes.ENTITY_TYPES.register(eventBus);
		ModStructures.STRUCTURES.register(eventBus);
		ModAttributes.ATTRIBUTES.register(eventBus);
		
		DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> new SafeRunnable() {@Override public void run() { eventBus.register(new BlockItemColors()); } } );
		
		MinecraftForge.EVENT_BUS.addListener(EventPriority.NORMAL, this::addDimensionalSpacing);
		MinecraftForge.EVENT_BUS.addListener(EventPriority.HIGH, EventBiomeLoading::onBiomeLoading);
		MinecraftForge.EVENT_BUS.register(this);
		
		MinecraftForge.EVENT_BUS.register(GameEvents.class);
		
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, CAConfig.COMMON_SPEC);
	}
	
	private static Method GETCODEC_METHOD;
	
	public void addDimensionalSpacing(final WorldEvent.Load event) {
		if (event.getWorld() instanceof ServerWorld) {
			ServerWorld serverWorld = (ServerWorld) event.getWorld();
			try {
				if (GETCODEC_METHOD == null)
					GETCODEC_METHOD = ObfuscationReflectionHelper.findMethod(ChunkGenerator.class, "codec");
				
				ResourceLocation cgRL = Registry.CHUNK_GENERATOR_CODEC.getKey((Codec<? extends ChunkGenerator>) GETCODEC_METHOD.invoke(serverWorld.getChunkProvider().generator));
				if (cgRL != null && cgRL.getNamespace().equals("terraforged"))
					return;
				
			} catch (Exception e) {
				ChaosAwakens.LOGGER.error(String.format("%s: Was unable to check if %s is using Terraforged's ChunkGenerator.", e.getCause(), serverWorld.getDimensionKey().getLocation()) );
			}
			
			if (serverWorld.getChunkProvider().getChunkGenerator() instanceof FlatChunkGenerator && serverWorld.getDimensionKey().equals(World.OVERWORLD)) {
				return;
			}
			
			Map<Structure<?>, StructureSeparationSettings> tempMap = new HashMap<>( serverWorld.getChunkProvider().generator.func_235957_b_().func_236195_a_());
			
			tempMap.putIfAbsent(ModStructures.ENT_DUNGEON.get(), DimensionStructuresSettings.field_236191_b_.get(ModStructures.ENT_DUNGEON.get()));
			
			serverWorld.getChunkProvider().generator.func_235957_b_().field_236193_d_ = tempMap;
		}
	}
	
	private void setup(FMLCommonSetupEvent event) {
		PacketHandler.init();
		
		event.enqueueWork(() -> {
			ModStructures.setupStructures();
			ConfiguredStructures.registerConfiguredStructures();
		});
		
		DeferredWorkQueue.runLater(() -> {
			GlobalEntityTypeAttributes.put(ModEntityTypes.ENT.get(), EntEntity.setCustomAttributes().create());
			GlobalEntityTypeAttributes.put(ModEntityTypes.RED_ANT.get(), RedAntEntity.setCustomAttributes().create());
			GlobalEntityTypeAttributes.put(ModEntityTypes.BROWN_ANT.get(), BrownAntEntity.setCustomAttributes().create());
			GlobalEntityTypeAttributes.put(ModEntityTypes.RAINBOW_ANT.get(), RainbowAntEntity.setCustomAttributes().create());
			GlobalEntityTypeAttributes.put(ModEntityTypes.UNSTABLE_ANT.get(), UnstableAntEntity.setCustomAttributes().create());
			GlobalEntityTypeAttributes.put(ModEntityTypes.TERMITE.get(), TermiteEntity.setCustomAttributes().create());
			GlobalEntityTypeAttributes.put(ModEntityTypes.HERCULES_BEETLE.get(), HerculesBeetleEntity.setCustomAttributes().create());
			GlobalEntityTypeAttributes.put(ModEntityTypes.RUBY_BUG.get(), RubyBugEntity.setCustomAttributes().create());
			GlobalEntityTypeAttributes.put(ModEntityTypes.APPLE_COW.get(), AppleCowEntity.setCustomAttributes().create());
			GlobalEntityTypeAttributes.put(ModEntityTypes.GOLDEN_APPLE_COW.get(), GoldenAppleCowEntity.setCustomAttributes().create());
			GlobalEntityTypeAttributes.put(ModEntityTypes.BEAVER.get(), BeaverEntity.setCustomAttributes().create());
			GlobalEntityTypeAttributes.put(ModEntityTypes.EMERALD_GATOR.get(), EmeraldGatorEntity.setCustomAttributes().create());
			GlobalEntityTypeAttributes.put(ModEntityTypes.ROBO_SNIPER.get(), RoboSniperEntity.setCustomAttributes().create());
		});
	}
	
	@SubscribeEvent(priority = EventPriority.HIGH)
	public void biomeLoadingAdd(final BiomeLoadingEvent event) {
		ModBiomeFeatures.addMobSpawns(event);
		//ModBiomeFeatures.addStructureSpawns(event);
	}
	
	private void gatherData(final GatherDataEvent event) {
		DataGenerator dataGenerator = event.getGenerator();
		final ExistingFileHelper existing = event.getExistingFileHelper();
		
		if (event.includeServer()) {
			dataGenerator.addProvider(new ModLootTableProvider(dataGenerator));
			dataGenerator.addProvider(new ModItemModelGenerator(dataGenerator, existing));
		}
	}
	
	public static ResourceLocation prefix(String name) {
		return new ResourceLocation(MODID, name.toLowerCase(Locale.ROOT));
	}
}