package io.github.chaosawakens.client;

import io.github.chaosawakens.ChaosAwakens;
import io.github.chaosawakens.common.registry.CABlocks;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.item.BlockItem;
import net.minecraft.world.FoliageColors;
import net.minecraft.world.GrassColors;
import net.minecraft.world.biome.BiomeColors;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ChaosAwakens.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class CABlockItemColors {
	
	public static final IBlockColor GRASS_BLOCK_COLOR = (state, reader, pos, color) -> reader != null && pos != null ? BiomeColors.getAverageGrassColor(reader, pos) : GrassColors.get(0.5, 1);
	public static final IBlockColor LEAVES_BLOCK_COLOR = (state, reader, pos, color) -> reader != null && pos != null ? BiomeColors.getAverageFoliageColor(reader, pos) : FoliageColors.get(0.5, 1);
	
	@SubscribeEvent
	public static void registerBlockColors(ColorHandlerEvent.Block event) {
		event.getBlockColors().register(GRASS_BLOCK_COLOR,
			CABlocks.RED_ANT_NEST.get(), CABlocks.BROWN_ANT_NEST.get(), CABlocks.RAINBOW_ANT_NEST.get(), CABlocks.UNSTABLE_ANT_NEST.get(), CABlocks.TERMITE_NEST.get());
		event.getBlockColors().register(LEAVES_BLOCK_COLOR,
			CABlocks.APPLE_LEAVES.get());
	}
	
	@SubscribeEvent
	public static void registerItemColors(ColorHandlerEvent.Item event) {
		event.getItemColors().register((stack, color) -> event.getBlockColors().getColor(((BlockItem) stack.getItem()).getBlock().defaultBlockState(), null, null, color),
			CABlocks.RED_ANT_NEST.get(), CABlocks.BROWN_ANT_NEST.get(), CABlocks.RAINBOW_ANT_NEST.get(), CABlocks.UNSTABLE_ANT_NEST.get(), CABlocks.TERMITE_NEST.get(),
			CABlocks.APPLE_LEAVES.get());
	}
}
