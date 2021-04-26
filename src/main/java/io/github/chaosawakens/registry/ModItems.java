package io.github.chaosawakens.registry;

import io.github.chaosawakens.enums.ArmorMaterials;
import io.github.chaosawakens.ChaosAwakens;
import io.github.chaosawakens.enums.ToolMaterials;
import io.github.chaosawakens.items.ThunderStaffItem;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = ChaosAwakens.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModItems {

    // FOOD
    public static final Food FOOD_RAW_CORNDOG = new Food.Builder().hunger(4).saturation(0.6F).build();
    public static final Food FOOD_COOKED_CORNDOG = new Food.Builder().hunger(14).saturation(1.5F).build();
    public static final Food FOOD_RAW_BACON = new Food.Builder().hunger(8).saturation(1.5F).build();
    public static final Food FOOD_COOKED_BACON = new Food.Builder().hunger(14).saturation(1.0F).build();
    public static final Food FOOD_CORN = new Food.Builder().hunger(6).saturation(0.75F).build();
    public static final Food FOOD_TOMATO = new Food.Builder().hunger(4).saturation(0.55F).build();
    public static final Food FOOD_LETTUCE = new Food.Builder().hunger(3).saturation(0.45F).build();
    public static final Food FOOD_CHEESE = new Food.Builder().hunger(4).saturation(0.5F).build();
    public static final Food FOOD_GARDEN_SALAD = new Food.Builder().hunger(10).saturation(0.95F).build();
    public static final Food FOOD_BLT = new Food.Builder().hunger(12).saturation(0.95F).build();

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ChaosAwakens.MODID);

    // FOODS
    public static final RegistryObject<Item> RAW_CORNDOG = ITEMS.register("raw_corndog", () -> new Item(new Item.Properties().food(ModItems.FOOD_RAW_CORNDOG).group(ItemGroup.FOOD)));
    public static final RegistryObject<Item> COOKED_CORNDOG = ITEMS.register("cooked_corndog", () -> new Item(new Item.Properties().food(ModItems.FOOD_COOKED_CORNDOG).group(ItemGroup.FOOD)));
    public static final RegistryObject<Item> RAW_BACON = ITEMS.register("raw_bacon", () -> new Item(new Item.Properties().food(ModItems.FOOD_RAW_BACON).group(ItemGroup.FOOD)));
    public static final RegistryObject<Item> COOKED_BACON = ITEMS.register("cooked_bacon", () -> new Item(new Item.Properties().food(ModItems.FOOD_COOKED_BACON).group(ItemGroup.FOOD)));
    public static final RegistryObject<Item> CORN = ITEMS.register("corn", () -> new Item(new Item.Properties().food(ModItems.FOOD_CORN).group(ItemGroup.FOOD)));
    public static final RegistryObject<Item> TOMATO = ITEMS.register("tomato", () -> new Item(new Item.Properties().food(ModItems.FOOD_TOMATO).group(ItemGroup.FOOD)));
    public static final RegistryObject<Item> LETTUCE = ITEMS.register("lettuce", () -> new Item(new Item.Properties().food(ModItems.FOOD_LETTUCE).group(ItemGroup.FOOD)));
    public static final RegistryObject<Item> CHEESE = ITEMS.register("cheese", () -> new Item(new Item.Properties().food(ModItems.FOOD_CHEESE).group(ItemGroup.FOOD)));
    public static final RegistryObject<Item> GARDEN_SALAD = ITEMS.register("garden_salad", () -> new SoupItem(new Item.Properties().food(ModItems.FOOD_GARDEN_SALAD).maxStackSize(1).group(ItemGroup.FOOD)));
    public static final RegistryObject<Item> BLT = ITEMS.register("blt", () -> new Item(new Item.Properties().food(ModItems.FOOD_BLT).group(ItemGroup.FOOD)));

    // GEMST0NES
    public static final RegistryObject<Item> AMETHYST = ITEMS.register("amethyst", () -> new Item(new Item.Properties().group(ItemGroup.MISC)));
    public static final RegistryObject<Item> RUBY = ITEMS.register("ruby", () -> new Item(new Item.Properties().group(ItemGroup.MISC)));
    public static final RegistryObject<Item> TIGERS_EYE = ITEMS.register("tigers_eye", () -> new Item(new Item.Properties().group(ItemGroup.MISC)));
    public static final RegistryObject<Item> TITANIUM_INGOT = ITEMS.register("titanium_ingot", () -> new Item(new Item.Properties().group(ItemGroup.MISC)));
    public static final RegistryObject<Item> TITANIUM_NUGGET = ITEMS.register("titanium_nugget", () -> new Item(new Item.Properties().group(ItemGroup.MISC)));
    public static final RegistryObject<Item> URANIUM_INGOT = ITEMS.register("uranium_ingot", () -> new Item(new Item.Properties().group(ItemGroup.MISC)));
    public static final RegistryObject<Item> URANIUM_NUGGET = ITEMS.register("uranium_nugget", () -> new Item(new Item.Properties().group(ItemGroup.MISC)));
    public static final RegistryObject<Item> ALUMINIUM_INGOT = ITEMS.register("aluminium_ingot", () -> new Item(new Item.Properties().group(ItemGroup.MISC)));

    // EMERALD TOOLS
    public static final RegistryObject<SwordItem> EMERALD_SWORD = ITEMS.register("emerald_sword", () -> new SwordItem(ToolMaterials.TOOL_EMERALD, 0, -2.4F, new Item.Properties().group(ItemGroup.COMBAT)));
    public static final RegistryObject<PickaxeItem> EMERALD_PICKAXE = ITEMS.register("emerald_pickaxe", () -> new PickaxeItem(ToolMaterials.TOOL_EMERALD, -2, -2.8F, new Item.Properties().group(ItemGroup.TOOLS)));
    public static final RegistryObject<ShovelItem> EMERALD_SHOVEL = ITEMS.register("emerald_shovel", () -> new ShovelItem(ToolMaterials.TOOL_EMERALD, -3, -3, new Item.Properties().group(ItemGroup.TOOLS)));
    public static final RegistryObject<AxeItem> EMERALD_AXE = ITEMS.register("emerald_axe", () -> new AxeItem(ToolMaterials.TOOL_EMERALD, -1, -3, new Item.Properties().group(ItemGroup.TOOLS)));
    public static final RegistryObject<HoeItem> EMERALD_HOE = ITEMS.register("emerald_hoe", () -> new HoeItem(ToolMaterials.TOOL_EMERALD, -9, 0.0F, new Item.Properties().group(ItemGroup.TOOLS)));

    // EMERALD ARMOR
    public static final RegistryObject<ArmorItem> EMERALD_HELMET = ITEMS.register("emerald_helmet", () -> new ArmorItem(ArmorMaterials.EMERALD, EquipmentSlotType.HEAD, new Item.Properties().group(ItemGroup.COMBAT)));
    public static final RegistryObject<ArmorItem> EMERALD_CHESTPLATE = ITEMS.register("emerald_chestplate", () -> new ArmorItem(ArmorMaterials.EMERALD, EquipmentSlotType.CHEST, new Item.Properties().group(ItemGroup.COMBAT)));
    public static final RegistryObject<ArmorItem> EMERALD_LEGGINGS = ITEMS.register("emerald_leggings", () -> new ArmorItem(ArmorMaterials.EMERALD, EquipmentSlotType.LEGS, new Item.Properties().group(ItemGroup.COMBAT)));
    public static final RegistryObject<ArmorItem> EMERALD_BOOTS = ITEMS.register("emerald_boots", () -> new ArmorItem(ArmorMaterials.EMERALD, EquipmentSlotType.FEET, new Item.Properties().group(ItemGroup.COMBAT)));

    // AMETHYST TOOLS
    public static final RegistryObject<SwordItem> AMETHYST_SWORD = ITEMS.register("amethyst_sword", () -> new SwordItem(ToolMaterials.TOOL_AMETHYST, -4, -2.4F, new Item.Properties().group(ItemGroup.COMBAT)));
    public static final RegistryObject<PickaxeItem> AMETHYST_PICKAXE = ITEMS.register("amethyst_pickaxe", () -> new PickaxeItem(ToolMaterials.TOOL_AMETHYST, -6, -2.8F, new Item.Properties().group(ItemGroup.TOOLS)));
    public static final RegistryObject<ShovelItem> AMETHYST_SHOVEL = ITEMS.register("amethyst_shovel", () -> new ShovelItem(ToolMaterials.TOOL_AMETHYST, -7, -3, new Item.Properties().group(ItemGroup.TOOLS)));
    public static final RegistryObject<AxeItem> AMETHYST_AXE = ITEMS.register("amethyst_axe", () -> new AxeItem(ToolMaterials.TOOL_AMETHYST, -5, -3, new Item.Properties().group(ItemGroup.TOOLS)));
    public static final RegistryObject<HoeItem> AMETHYST_HOE = ITEMS.register("amethyst_hoe", () -> new HoeItem(ToolMaterials.TOOL_AMETHYST, -18, 0.0F, new Item.Properties().group(ItemGroup.TOOLS)));

    // AMETHYST ARMOR
    public static final RegistryObject<ArmorItem> AMETHYST_HELMET = ITEMS.register("amethyst_helmet", () -> new ArmorItem(ArmorMaterials.AMETHYST, EquipmentSlotType.HEAD, new Item.Properties().group(ItemGroup.COMBAT)));
    public static final RegistryObject<ArmorItem> AMETHYST_CHESTPLATE = ITEMS.register("amethyst_chestplate", () -> new ArmorItem(ArmorMaterials.AMETHYST, EquipmentSlotType.CHEST, new Item.Properties().group(ItemGroup.COMBAT)));
    public static final RegistryObject<ArmorItem> AMETHYST_LEGGINGS = ITEMS.register("amethyst_leggings", () -> new ArmorItem(ArmorMaterials.AMETHYST, EquipmentSlotType.LEGS, new Item.Properties().group(ItemGroup.COMBAT)));
    public static final RegistryObject<ArmorItem> AMETHYST_BOOTS = ITEMS.register("amethyst_boots", () -> new ArmorItem(ArmorMaterials.AMETHYST, EquipmentSlotType.FEET, new Item.Properties().group(ItemGroup.COMBAT)));

    // RUBY TOOLS
    public static final RegistryObject<SwordItem> RUBY_SWORD = ITEMS.register("ruby_sword", () -> new SwordItem(ToolMaterials.TOOL_RUBY, -11, -2.4F, new Item.Properties().group(ItemGroup.COMBAT)));
    public static final RegistryObject<PickaxeItem> RUBY_PICKAXE = ITEMS.register("ruby_pickaxe", () -> new PickaxeItem(ToolMaterials.TOOL_RUBY, -13, -2.8F, new Item.Properties().group(ItemGroup.TOOLS)));
    public static final RegistryObject<ShovelItem> RUBY_SHOVEL = ITEMS.register("ruby_shovel", () -> new ShovelItem(ToolMaterials.TOOL_RUBY, -14, -3, new Item.Properties().group(ItemGroup.TOOLS)));
    public static final RegistryObject<AxeItem> RUBY_AXE = ITEMS.register("ruby_axe", () -> new AxeItem(ToolMaterials.TOOL_RUBY, -12, -3, new Item.Properties().group(ItemGroup.TOOLS)));
    public static final RegistryObject<HoeItem> RUBY_HOE = ITEMS.register("ruby_hoe", () -> new HoeItem(ToolMaterials.TOOL_RUBY, -30, 0.0F, new Item.Properties().group(ItemGroup.TOOLS)));

    // RUBY ARMOR
    public static final RegistryObject<ArmorItem> RUBY_HELMET = ITEMS.register("ruby_helmet", () -> new ArmorItem(ArmorMaterials.RUBY, EquipmentSlotType.HEAD, new Item.Properties().group(ItemGroup.COMBAT)));
    public static final RegistryObject<ArmorItem> RUBY_CHESTPLATE = ITEMS.register("ruby_chestplate", () -> new ArmorItem(ArmorMaterials.RUBY, EquipmentSlotType.CHEST, new Item.Properties().group(ItemGroup.COMBAT)));
    public static final RegistryObject<ArmorItem> RUBY_LEGGINGS = ITEMS.register("ruby_leggings", () -> new ArmorItem(ArmorMaterials.RUBY, EquipmentSlotType.LEGS, new Item.Properties().group(ItemGroup.COMBAT)));
    public static final RegistryObject<ArmorItem> RUBY_BOOTS = ITEMS.register("ruby_boots", () -> new ArmorItem(ArmorMaterials.RUBY, EquipmentSlotType.FEET, new Item.Properties().group(ItemGroup.COMBAT)));

    // TIGERS EYE TOOLS
    public static final RegistryObject<SwordItem> TIGERS_EYE_SWORD = ITEMS.register("tigers_eye_sword", () -> new SwordItem(ToolMaterials.TOOL_TIGERS_EYE, 1, -2.4F, new Item.Properties().group(ItemGroup.COMBAT)));
    public static final RegistryObject<PickaxeItem> TIGERS_EYE_PICKAXE = ITEMS.register("tigers_eye_pickaxe", () -> new PickaxeItem(ToolMaterials.TOOL_TIGERS_EYE, -1, -2.8F, new Item.Properties().group(ItemGroup.TOOLS)));
    public static final RegistryObject<ShovelItem> TIGERS_EYE_SHOVEL = ITEMS.register("tigers_eye_shovel", () -> new ShovelItem(ToolMaterials.TOOL_TIGERS_EYE, -2, -3, new Item.Properties().group(ItemGroup.TOOLS)));
    public static final RegistryObject<AxeItem> TIGERS_EYE_AXE = ITEMS.register("tigers_eye_axe", () -> new AxeItem(ToolMaterials.TOOL_TIGERS_EYE, 0, -3, new Item.Properties().group(ItemGroup.TOOLS)));
    public static final RegistryObject<HoeItem> TIGERS_EYE_HOE = ITEMS.register("tigers_eye_hoe", () -> new HoeItem(ToolMaterials.TOOL_TIGERS_EYE, -10, 0.0F, new Item.Properties().group(ItemGroup.TOOLS)));

    // TIGERS EYE ARMOR
    public static final RegistryObject<ArmorItem> TIGERS_EYE_HELMET = ITEMS.register("tigers_eye_helmet", () -> new ArmorItem(ArmorMaterials.TIGERS_EYE, EquipmentSlotType.HEAD, new Item.Properties().group(ItemGroup.COMBAT)));
    public static final RegistryObject<ArmorItem> TIGERS_EYE_CHESTPLATE = ITEMS.register("tigers_eye_chestplate", () -> new ArmorItem(ArmorMaterials.TIGERS_EYE, EquipmentSlotType.CHEST, new Item.Properties().group(ItemGroup.COMBAT)));
    public static final RegistryObject<ArmorItem> TIGERS_EYE_LEGGINGS = ITEMS.register("tigers_eye_leggings", () -> new ArmorItem(ArmorMaterials.TIGERS_EYE, EquipmentSlotType.LEGS, new Item.Properties().group(ItemGroup.COMBAT)));
    public static final RegistryObject<ArmorItem> TIGERS_EYE_BOOTS = ITEMS.register("tigers_eye_boots", () -> new ArmorItem(ArmorMaterials.TIGERS_EYE, EquipmentSlotType.FEET, new Item.Properties().group(ItemGroup.COMBAT)));

    // ULTIMATE TOOLS
    public static final RegistryObject<SwordItem> ULTIMATE_SWORD = ITEMS.register("ultimate_sword", () -> new SwordItem(ToolMaterials.TOOL_ULTIMATE, 31, -2.4F, new Item.Properties().group(ItemGroup.COMBAT)));
    public static final RegistryObject<PickaxeItem> ULTIMATE_PICKAXE = ITEMS.register("ultimate_pickaxe", () -> new PickaxeItem(ToolMaterials.TOOL_ULTIMATE, 29, -2.8F, new Item.Properties().group(ItemGroup.TOOLS)));
    public static final RegistryObject<ShovelItem> ULTIMATE_SHOVEL = ITEMS.register("ultimate_shovel", () -> new ShovelItem(ToolMaterials.TOOL_ULTIMATE, 28, -3, new Item.Properties().group(ItemGroup.TOOLS)));
    public static final RegistryObject<AxeItem> ULTIMATE_AXE = ITEMS.register("ultimate_axe", () -> new AxeItem(ToolMaterials.TOOL_ULTIMATE, 30, -3, new Item.Properties().group(ItemGroup.TOOLS)));
    public static final RegistryObject<HoeItem> ULTIMATE_HOE = ITEMS.register("ultimate_hoe", () -> new HoeItem(ToolMaterials.TOOL_ULTIMATE, -8, 0.0F, new Item.Properties().group(ItemGroup.TOOLS)));

    // ULTIMATE ARMOR
    public static final RegistryObject<ArmorItem> ULTIMATE_HELMET = ITEMS.register("ultimate_helmet", () -> new ArmorItem(ArmorMaterials.ULTIMATE, EquipmentSlotType.HEAD, new Item.Properties().group(ItemGroup.COMBAT)));
    public static final RegistryObject<ArmorItem> ULTIMATE_CHESTPLATE = ITEMS.register("ultimate_chestplate", () -> new ArmorItem(ArmorMaterials.ULTIMATE, EquipmentSlotType.CHEST, new Item.Properties().group(ItemGroup.COMBAT)));
    public static final RegistryObject<ArmorItem> ULTIMATE_LEGGINGS = ITEMS.register("ultimate_leggings", () -> new ArmorItem(ArmorMaterials.ULTIMATE, EquipmentSlotType.LEGS, new Item.Properties().group(ItemGroup.COMBAT)));
    public static final RegistryObject<ArmorItem> ULTIMATE_BOOTS = ITEMS.register("ultimate_boots", () -> new ArmorItem(ArmorMaterials.ULTIMATE, EquipmentSlotType.FEET, new Item.Properties().group(ItemGroup.COMBAT)));

    // STAFFS
    public static final RegistryObject<ThunderStaffItem> THUNDER_STAFF = ITEMS.register("thunder_staff", () -> new ThunderStaffItem(new Item.Properties().group(ItemGroup.COMBAT).maxDamage(50)));
}