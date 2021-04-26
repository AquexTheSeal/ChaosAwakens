package io.github.chaosawakens.data;

import io.github.chaosawakens.ChaosAwakens;
import io.github.chaosawakens.registry.ModBlocks;
import io.github.chaosawakens.registry.ModItems;
import net.minecraft.block.Block;
import net.minecraft.data.loot.BlockLootTables;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ModBlockLootTables extends BlockLootTables
{
    @Override
    protected void addTables()
    {
        // ORES
        registerLootTable(ModBlocks.AMETHYST_ORE.get(), (ore) -> {
            return droppingItemWithFortune(ore, ModItems.AMETHYST.get());
        });
        registerLootTable(ModBlocks.RUBY_ORE.get(), (ore) -> {
            return droppingItemWithFortune(ore, ModItems.RUBY.get());
        });
        registerLootTable(ModBlocks.TIGERS_EYE_ORE.get(), (ore) -> {
            return droppingItemWithFortune(ore, ModItems.TIGERS_EYE.get());
        });

        // BLOCKS
        registerDropSelfLootTable(ModBlocks.AMETHYST_BLOCK.get());
        registerDropSelfLootTable(ModBlocks.RUBY_BLOCK.get());
        registerDropSelfLootTable(ModBlocks.TIGERS_EYE_BLOCK.get());
        registerDropSelfLootTable(ModBlocks.URANIUM_ORE.get());
        registerDropSelfLootTable(ModBlocks.URANIUM_BLOCK.get());
        registerDropSelfLootTable(ModBlocks.TITANIUM_ORE.get());
        registerDropSelfLootTable(ModBlocks.TITANIUM_BLOCK.get());
        registerDropSelfLootTable(ModBlocks.ALUMINIUM_ORE.get());
        registerDropSelfLootTable(ModBlocks.ALUMINIUM_BLOCK.get());
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }
}