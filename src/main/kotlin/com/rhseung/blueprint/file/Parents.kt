package com.rhseung.blueprint.render.model

import com.rhseung.blueprint.file.Path

object Parents {
    val item = Path("item")
    val block = Path("block")

    val EMPTY = item/""
    val GENERATED = item/"generated"
    val HANDHELD = item/"handheld"
    val HANDHELD_ROD = item/"handheld_rod"
    val TEMPLATE_SHULKER_BOX = item/"template_shulker_box"
    val TEMPLATE_BED = item/"template_bed"
    val TEMPLATE_BANNER = item/"template_banner"
    val TEMPLATE_SKULL = item/"template_skull"

    val CUBE = block/"cube"
    val CUBE_DIRECTIONAL = block/"cube_directional"
    val CUBE_ALL = block/"cube_all"
    val CUBE_MIRRORED_ALL = block/"cube_mirrored_all"
    val CUBE_NORTH_WEST_MIRRORED_ALL = block/"cube_north_west_mirrored_all"
    val CUBE_COLUMN_UV_LOCKED_X = block/"cube_column_uv_locked_x"
    val CUBE_COLUMN_UV_LOCKED_Y = block/"cube_column_uv_locked_y"
    val CUBE_COLUMN_UV_LOCKED_Z = block/"cube_column_uv_locked_z"
    val CUBE_COLUMN = block/"cube_column"
    val CUBE_COLUMN_HORIZONTAL = block/"cube_column_horizontal"
    val CUBE_COLUMN_MIRRORED = block/"cube_column_mirrored"
    val CUBE_TOP = block/"cube_top"
    val CUBE_BOTTOM_TOP = block/"cube_bottom_top"
    val ORIENTABLE = block/"orientable"
    val ORIENTABLE_WITH_BOTTOM = block/"orientable_with_bottom"
    val ORIENTABLE_VERTICAL = block/"orientable_vertical"
    val BUTTON = block/"button"
    val BUTTON_PRESSED = block/"button_pressed"
    val BUTTON_INVENTORY = block/"button_inventory"
    val DOOR_BOTTOM_LEFT = block/"door_bottom_left"
    val DOOR_BOTTOM_LEFT_OPEN = block/"door_bottom_left_open"
    val DOOR_BOTTOM_RIGHT = block/"door_bottom_right"
    val DOOR_BOTTOM_RIGHT_OPEN = block/"door_bottom_right_open"
    val DOOR_TOP_LEFT = block/"door_top_left"
    val DOOR_TOP_LEFT_OPEN = block/"door_top_left_open"
    val DOOR_TOP_RIGHT = block/"door_top_right"
    val DOOR_TOP_RIGHT_OPEN = block/"door_top_right_open"
    val CUSTOM_FENCE_POST = block/"custom_fence_post"
    val CUSTOM_FENCE_SIDE_NORTH = block/"custom_fence_side_north"
    val CUSTOM_FENCE_SIDE_EAST = block/"custom_fence_side_east"
    val CUSTOM_FENCE_SIDE_SOUTH = block/"custom_fence_side_south"
    val CUSTOM_FENCE_SIDE_WEST = block/"custom_fence_side_west"
    val CUSTOM_FENCE_INVENTORY = block/"custom_fence_inventory"
    val FENCE_POST = block/"fence_post"
    val FENCE_SIDE = block/"fence_side"
    val FENCE_INVENTORY = block/"fence_inventory"
    val TEMPLATE_WALL_POST = block/"template_wall_post"
    val TEMPLATE_WALL_SIDE = block/"template_wall_side"
    val TEMPLATE_WALL_SIDE_TALL = block/"template_wall_side_tall"
    val WALL_INVENTORY = block/"wall_inventory"
    val TEMPLATE_CUSTOM_FENCE_GATE = block/"template_custom_fence_gate"
    val TEMPLATE_CUSTOM_FENCE_GATE_OPEN = block/"template_custom_fence_gate_open"
    val TEMPLATE_CUSTOM_FENCE_GATE_WALL = block/"template_custom_fence_gate_wall"
    val TEMPLATE_CUSTOM_FENCE_GATE_WALL_OPEN = block/"template_custom_fence_gate_wall_open"
    val TEMPLATE_FENCE_GATE = block/"template_fence_gate"
    val TEMPLATE_FENCE_GATE_OPEN = block/"template_fence_gate_open"
    val TEMPLATE_FENCE_GATE_WALL = block/"template_fence_gate_wall"
    val TEMPLATE_FENCE_GATE_WALL_OPEN = block/"template_fence_gate_wall_open"
    val PRESSURE_PLATE_UP = block/"pressure_plate_up"
    val PRESSURE_PLATE_DOWN = block/"pressure_plate_down"
    val SLAB = block/"slab"
    val SLAB_TOP = block/"slab_top"
    val LEAVES = block/"leaves"
    val STAIRS = block/"stairs"
    val INNER_STAIRS = block/"inner_stairs"
    val OUTER_STAIRS = block/"outer_stairs"
    val TEMPLATE_TRAPDOOR_TOP = block/"template_trapdoor_top"
    val TEMPLATE_TRAPDOOR_BOTTOM = block/"template_trapdoor_bottom"
    val TEMPLATE_TRAPDOOR_OPEN = block/"template_trapdoor_open"
    val TEMPLATE_ORIENTABLE_TRAPDOOR_TOP = block/"template_orientable_trapdoor_top"
    val TEMPLATE_ORIENTABLE_TRAPDOOR_BOTTOM = block/"template_orientable_trapdoor_bottom"
    val TEMPLATE_ORIENTABLE_TRAPDOOR_OPEN = block/"template_orientable_trapdoor_open"
    val POINTED_DRIPSTONE = block/"pointed_dripstone"
    val CROSS = block/"cross"
    val TINTED_CROSS = block/"tinted_cross"
    val FLOWER_POT_CROSS = block/"flower_pot_cross"
    val TINTED_FLOWER_POT_CROSS = block/"tinted_flower_pot_cross"
    val RAIL_FLAT = block/"rail_flat"
    val RAIL_CURVED = block/"rail_curved"
    val TEMPLATE_RAIL_RAISED_NE = block/"template_rail_raised_ne"
    val TEMPLATE_RAIL_RAISED_SW = block/"template_rail_raised_sw"
    val CARPET = block/"carpet"
    val CORAL_FAN = block/"coral_fan"
    val CORAL_WALL_FAN = block/"coral_wall_fan"
    val TEMPLATE_GLAZED_TERRACOTTA = block/"template_glazed_terracotta"
    val TEMPLATE_CHORUS_FLOWER = block/"template_chorus_flower"
    val TEMPLATE_DAYLIGHT_DETECTOR = block/"template_daylight_detector"
    val TEMPLATE_GLASS_PANE_NOSIDE = block/"template_glass_pane_noside"
    val TEMPLATE_GLASS_PANE_NOSIDE_ALT = block/"template_glass_pane_noside_alt"
    val TEMPLATE_GLASS_PANE_POST = block/"template_glass_pane_post"
    val TEMPLATE_GLASS_PANE_SIDE = block/"template_glass_pane_side"
    val TEMPLATE_GLASS_PANE_SIDE_ALT = block/"template_glass_pane_side_alt"
    val TEMPLATE_COMMAND_BLOCK = block/"template_command_block"
    val TEMPLATE_CHISELED_BOOKSHELF_SLOT_TOP_LEFT = block/"template_chiseled_bookshelf_slot_top_left"
    val TEMPLATE_CHISELED_BOOKSHELF_SLOT_TOP_MID = block/"template_chiseled_bookshelf_slot_top_mid"
    val TEMPLATE_CHISELED_BOOKSHELF_SLOT_TOP_RIGHT = block/"template_chiseled_bookshelf_slot_top_right"
    val TEMPLATE_CHISELED_BOOKSHELF_SLOT_BOTTOM_LEFT =
        block/"template_chiseled_bookshelf_slot_bottom_left"
    val TEMPLATE_CHISELED_BOOKSHELF_SLOT_BOTTOM_MID =
        block/"template_chiseled_bookshelf_slot_bottom_mid"
    val TEMPLATE_CHISELED_BOOKSHELF_SLOT_BOTTOM_RIGHT =
        block/"template_chiseled_bookshelf_slot_bottom_right"
    val TEMPLATE_ANVIL = block/"template_anvil"
    val STEM_FRUIT = block/"stem_fruit"
//    fun STEM_GROWTH(n: Int) = block/"stem_growth$n"
    val STEM_GROWTH_0 = block/"stem_growth_0"
    val STEM_GROWTH_1 = block/"stem_growth_1"
    val STEM_GROWTH_2 = block/"stem_growth_2"
    val STEM_GROWTH_3 = block/"stem_growth_3"
    val STEM_GROWTH_4 = block/"stem_growth_4"
    val STEM_GROWTH_5 = block/"stem_growth_5"
    val STEM_GROWTH_6 = block/"stem_growth_6"
    val STEM_GROWTH_7 = block/"stem_growth_7"
    val CROP = block/"crop"
    val TEMPLATE_FARMLAND = block/"template_farmland"
    val TEMPLATE_FIRE_FLOOR = block/"template_fire_floor"
    val TEMPLATE_FIRE_SIDE = block/"template_fire_side"
    val TEMPLATE_FIRE_SIDE_ALT = block/"template_fire_side_alt"
    val TEMPLATE_FIRE_UP = block/"template_fire_up"
    val TEMPLATE_FIRE_UP_ALT = block/"template_fire_up_alt"
    val TEMPLATE_CAMPFIRE = block/"template_campfire"
    val TEMPLATE_LANTERN = block/"template_lantern"
    val TEMPLATE_HANGING_LANTERN = block/"template_hanging_lantern"
    val TEMPLATE_TORCH = block/"template_torch"
    val TEMPLATE_TORCH_WALL = block/"template_torch_wall"
    val TEMPLATE_PISTON = block/"template_piston"
    val TEMPLATE_PISTON_HEAD = block/"template_piston_head"
    val TEMPLATE_PISTON_HEAD_SHORT = block/"template_piston_head_short"
    val TEMPLATE_SEAGRASS = block/"template_seagrass"
    val TEMPLATE_TURTLE_EGG = block/"template_turtle_egg"
    val TEMPLATE_TWO_TURTLE_EGGS = block/"template_two_turtle_eggs"
    val TEMPLATE_THREE_TURTLE_EGGS = block/"template_three_turtle_eggs"
    val TEMPLATE_FOUR_TURTLE_EGGS = block/"template_four_turtle_eggs"
    val TEMPLATE_SINGLE_FACE = block/"template_single_face"
    val TEMPLATE_CAULDRON_LEVEL1 = block/"template_cauldron_level1"
    val TEMPLATE_CAULDRON_LEVEL2 = block/"template_cauldron_level2"
    val TEMPLATE_CAULDRON_FULL = block/"template_cauldron_full"
    val TEMPLATE_AZALEA = block/"template_azalea"
    val TEMPLATE_POTTED_AZALEA_BUSH = block/"template_potted_azalea_bush"
    val TEMPLATE_CANDLE = block/"template_candle"
    val TEMPLATE_TWO_CANDLES = block/"template_two_candles"
    val TEMPLATE_THREE_CANDLES = block/"template_three_candles"
    val TEMPLATE_FOUR_CANDLES = block/"template_four_candles"
    val TEMPLATE_CAKE_WITH_CANDLE = block/"template_cake_with_candle"
    val TEMPLATE_SCULK_SHRIEKER = block/"template_sculk_shrieker"
    val FLOWERBED_1 = block/"flowerbed_1"
    val FLOWERBED_2 = block/"flowerbed_2"
    val FLOWERBED_3 = block/"flowerbed_3"
    val FLOWERBED_4 = block/"flowerbed_4"
}