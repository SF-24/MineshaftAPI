



# MineshaftAPI
A paper (formerly spigot) plugin for Minecraft servers. Build with maven, documentation coming soon. Designed for Minecraft 1.21.4. _(formerly 1.20.6)_
<br><br>
This _README_ file is valid only for the 1.21.4 version. For older versions, please view the commit history.

## Table of Contents
- [Copyright notice](#Important-Copyright-Information)
- [Documentation introduction](#Documentation)
- [Upcoming features](#Upcoming-and-planned-features)
- [Item creation](#Item-Creation )
	- [Example](#Example)
	- [List of parameters](#List-of-parameters)
		- [Item category list](#item-category-list)
		- [Item rarity list](#item-rarity-list)
		- [Item stats and attributes](#item-stats-and-attributes)
		    - [List of item attributes](#list-of-item-attributes)
		    - [List of ranged-stat item attributes](#list-of-ranged-stat-item-attributes)
		- [Ammunition](#ammunition)
        - [Food](#food)
            - [List of food stats](#list-of-food-stats)
		- [Consumable](#consumable)
		- [Potion effects](#potion-effects)
            - [Potion effect parameters](#potion-effect-parameters)
		-  [Armour](#armour)
		-  [Tools](#tool-component)
            - [Default Mining Speed Values](#default-mining-speed-values)
            - [Block Rules](#block-rules)
      - [Crafting Mechanics](#crafting-mechanics)
        - [Item Repair](#item-repair) 
        - [Crafting](#crafting) 
        - [Hardcoded Recipe Types](#hardcoded-recipe-types) 
        - [Deconstruction Recipes](#deconstruction-recipes) 
      - [Item events](#item-events)
        - [Hardcoded events](#hardcoded-events) 
        - [Item event parameters](#item-event-parameters)
	- [Obtaining custom items](#obtaining-custom-items)
- [Event creation](#event-creation)
  - [Event example](#event-example)
  - [List of event parameters](#list-of-event-parameters)
    - [Beam event type](#beam-event-type)
      - [On-hit local events](#on-hit-local-events)
    - [Vector player event type](#vector-player-event-type)
    - [Strong entity attack event type](#strong-attack-event-type)
    - [Entity damage event type](#entity-damage-event-type)
    - [Entity disarm event type](#entity-disarm-event-type)
    - [Entity knockback event type](#entity-knockback-event-type)
- [Configuration](#configuration)
- [Integrations](#integrations)
    - [Mythic Mobs](#mythic-mobs)
        - [Mineshaft event skill](#mineshaft-event)
        - [Targeted Mineshaft event skill](#targeted-mineshaft-event)
        - [Set armour class](#setting-armour-class)
    - [BetonQuest](#BetonQuest)
    - [PlaceholderAPI](#PlaceholderAPI)
- [Dependents](#dependents)
    - [MineshaftRpg](#mineshaftrpg) 
- [Plugin reloading](#plugin-reloading)

# Important Copyright Information
**This project is under the GPU Affero GPL v3 license**<br>
Terms of use can be found [here](https://www.gnu.org/licenses/agpl-3.0.en.html)

# Documentation
Currently, the only functionality of this plugin are custom item creation, event creation and **Vault** server economy.


This plugin contains many other API features used in other plugins, including loading and saving player data under multiple profiles, mathematical calculations and more.

Events can be triggered as **MythicMobs** mechanics via the integration.

~~A work in progress sidebar (disabled by default) is displayed to all players on the server. Customisation is not yet available.~~ (Shelved idea)

Images representing examples of content included in the plugin may not be entirely accurate, as these were taken by a user with client mods and/or a resource pack altering the look of the game. 
Some of these were taken with older versions of the plugin.

## Undocumented features
The following features are not yet explained in the documentation

- MythicMobs compatibility (WIP)
- Currency command (requires permission)
- Permissions
- Heal command (OP only)
- PlayerSkills API (via MineshaftRpg) and Data API
- Placeholders (Placeholder API integration) (partially)
- Weapon cooldown
- Item subtypes (partially explained. WIP)
- Item stat priority list (descending)

# Upcoming and planned features
**Not all features in this list may be added**<br>
*This is just a to-do list featuring my current ideas*<br>
*List elements in italic will definitely be added*

- *[important!] Completing the README file and adding all documentation*
- ~~*[important!] Working armour support*~~ (mostly implemented)
- *More configuration: item rarities, etc.*
- *Extendable weapons (lightsaber creation, etc.)*
- *Customisable weapon cooldown*
- ~~*Player attributes: Strength, intelligence, etc.*~~ (Part of MineshaftRpg plugin)
- ~~Ammunition feature for ranged weapons (with reloading)~~
- Customisable sidebar (Shelved for now)
- More Placeholders (Partially implemented)
- Custom menus and shops
- BetonQuest integration (Partially implemented)
- BetonQuest quest tracker
- More MythicMobs features
- ~~FreeMinecraftModels integration with MythicMobs compatibility~~ **Use BetterModels for now instead**
- ~~Custom crafting system~~ (Partially implemented.)
- Add more customizability to the custom crafting system 
- ~~Skills~~ (Look at MineshaftRpg instead)
- Item customisation (replacing parts, etc.)
- In-built mob support (similar to MythicMobs or EliteMobs)
- Dungeons
- More events (Working on it)
- ~~Replacing local events with events~~ (Added, but untested)
- NPCs
- Better API and JavaDocs

# Item Creation 

Items are created via custom YAML files. These are placed in the `Mineshaft/Items` folder in the plugin directory of your server.

Items can have a multitude of different parameters, which are used to customize the item.

If your `Items` folder is empty, an example file `example-item.yml` will be placed in this folder.

## Example

```yaml
name: Cleaver
rarity: exotic
item_category: weapon_melee
material: GOLDEN_SWORD
id: 1eeeb42e-250b-4466-8e03-9c6cccae0388
custom_model_data: 0
parent: 'null'
durability: 250
stack_size: 1
enchantment_glint: false
hide_attributes: true
subcategory: sword
stats:
  damage: 5
```
![An image of the example item](https://raw.githubusercontent.com/SF-24/images/main/example.png?token=GHSAT0AAAAAACVH5VYE6TIIM3WQV535ATUKZVCIRKQ)

## List of parameters
Not all parameters are required for the item to work

| Parameter           |                                                                     Description                                                                      | Data Type |                                                                                                                                                                                        Note |
|:--------------------|:----------------------------------------------------------------------------------------------------------------------------------------------------:|----------:|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------:|
| `parent`            | The name of the parent item. All values unspecified in the file will be set to those of the parent. A parent item may also have its own parent item. |    String |                                                                                                                                                                                             |
| `name`              |                                                             The custom name for the item                                                             |    String |                                                                                                                                                                                             |
| `subcategory`       |                                                          The displayed category of the item                                                          |    String |                                                                                                                                   Only affects the item description. Use any value you want |
| `material`          |                                                         The Minecraft material for the item                                                          |      Enum |                                                                                                     [1.21 Material List](https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Material.html) |
| `rarity`            |                                                                  Rarity of the item                                                                  |      Enum |                                                                                                                                                            [Rarity List](#Item-Rarity-List) |
| `item_category`     |                                                                 Category of the item                                                                 |      Enum |                                                                                                                                                        [Category List](#Item-Category-List) |
| `custom_model_data` |                                                                  Custom Model Data                                                                   |   Integer |                                                                                                                                                                                             |
| `durability`        |                                               The amount of times an item may be used before it breaks                                               |   Integer |                                                                                                                                                                                             |
| `stack_size`        |                                                       The maximum stack size of any given item                                                       |   Integer |                                                                                                                                                                      Value between 1 and 99 |
| `hide_attributes`   |                                          Whether the default Minecraft attribute description will be hidden                                          |   boolean |                                                                                                                                           Default is true. Only set to false for debugging. |
| `enchantment_glint` |                                                                   WIP: do not set                                                                    |   boolean |                                                                                                                                                                                             |
| `stats`             |                                                               List of item attributes                                                                |   HashMap |                                                                                                                                                                                             |
| `id`                |                                A unique id assigned by the plugin. This is used for the plugin to recognize the item.                                |      UUID |                                          Do not change or set manually. Two items having the same id will result in **MAJOR** bugs. Changing an item id results in previous items breaking. |
| `item_model`        |                                                            The custom model for the item                                                             |    String |                                                                                                                                                                                             |
| `tooltip`           |                                                     Allows for setting the custom tooltip style.                                                     |    String | Textures loaded from: `/assets/<namespace>/textures/gui/sprites/tooltip/<id>_background` and `/assets/<namespace>/textures/gui/sprites/tooltip/<id>_frame` where id is the specified style. |

### Item Category List

| Value               | Description                      |
|:--------------------|:---------------------------------|
| `weapon_melee`      | Melee weapon                     |
| `weapon_ranged`     | Ranged weapon                    |
| `armour_helmet`     | Helmet (Equip in head slot)      |
| `armour_chestplate` | Chestplate (Equip in chest slot) |
| `armour_leggings`   | Leggings (Equip in leg slot)     |
| `armour_boots`      | Boots (Equip in foot slot)       |
| `tool_axe`          | Axe                              |
| `tool_pickaxe`      | Pickaxe                          |
| `tool_shovel`       | Shovel                           |
| `tool_hoe`          | Hoe                              |
| `item_consumable`   | Consumable item (One time use)   |
| `item_generic`      | Generic item (no category)       |
| `other`             | Other (Unspecified)              |

### Item Rarity List

| Value       |                Rarity                 |  Text Colour   | Description                   |
|:------------|:-------------------------------------:|:--------------:|-------------------------------|
| `standard`  |          ~~No Description~~           |                | Standard items with no rarity |
| `common`    |   <font color="white">Common</font>   |     white      |
| `uncommon`  |  <font color="lime">Uncommon</font>   |     green      |
| `rare`      |    <font color="blue">Rare</font>     |      blue      |
| `exotic`    |  <font color="magenta">Exotic</font>  | magenta/purple |
| `legendary` | <font color="ORANGE">Legendary</font> |  orange/gold   |

### Item stats and attributes

The `stat` property is used to add custom attributes to an item. 
These attributes are applied to the player when the item is equipped

See below for a list of custom attributes

**Example**
*Below is an example of a stat section of am item*
*For a full example of an item YAML file [click here](#Example)*
```yaml
stats:
  damage: 5
  speed: 10
  armour: 32
  health: 17
```

### List of item attributes

| Value                        | Stat                             | Description                                                             |                                                                                              Notes |
|:-----------------------------|----------------------------------|:------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------:|
| `damage`                     | Attack damage                    | Increases damage dealt via ranged and melee                             |                                                 WIP -> extremely glitchy when using ranged weapons |
| `ranged_damage`              | Ranged attack damage             | Increases ranged damage                                                 |                           WIP -> currently only applies to beam events triggered using this weapon |
| `health`                     | Health                           | Increases maximum health when equipped                                  |                                                                                                    |
| `speed`                      | Speed                            | Will increase movement speed when implemented                           |                       Stat is applied to items, however functionality has not yet been implemented |
| `reach`                      | Reach                            | Affects block and entity interaction reach distance                     |                                                                                                    | 
| `mining_reach`               | Block Interaction Reach          | Affects block interaction reach distance                                |
| `attack_reach`               | Entity Interaction Reach         | Affects entity interaction reach distance                               |                                                                                                    |
| `attack_speed`               | Attack Speed                     | The attack speed of an item.                                            |                                                             Work in progress. Do not use on armour | 
| `armour`                     | Armour                           | The default Minecraft armour stat                                       |                                                                                                    |
| `armour_class`               | Armour Class (WIP)               | Reduces damage taken (excluding certain sources like drowning)          |                                                          Armour Class scalability will be modified |
| `maximum_added_dex_modifier` | Maximum added dexterity modifier | Modifies the maximum MineshaftRpg dexterity bonus added to armour class | Set to -1 for infinite. Only works in conjunction with MineshaftRpg (WIP). Does nothing on its own |


### List of ranged-stat item attributes

Placed under `ranged_stats:` in the YAML file, e.g.:
<br>
```yaml
ranged_stats:
  firing_speed_custom: 2.5
```

| Value                 | Stat                             | Description | Notes |
|:----------------------|----------------------------------|:------------|------:|
| `firing_speed_custom` | Override beam event firing speed |             |       |
| `firing_range_custom` | Override beam event firing range |             |       |

### Ammunition

Can only be used by ranged weapons.
<br>`shots` specifies the number of shots that can be made per ammunition item used.
<br>`ammunition_types` specifies the names of the items used as ammunition.

```yaml
ammunition:
  shots: 16
  ammunition_types:
  - ammunition_power_cell
```

### Food

The `food` property is used only for consumables. 
If an item has a food property, it may be eaten by a player.

**Example**
*Below is an example of a food section of an item*
*For a full example of an item YAML file [click here](#Example)*

```yaml
food:
  nutrition: 5
  saturation: 0.5
  always_edible: true
  potion_effects:
    speed:
      duration: 200
      amplifier: 1
      ambient: true 
      particles: false
      icon: true
```

### List of food stats

| Value                | Stat                                          | Description                                                  | Data Type | Notes |
|:---------------------|-----------------------------------------------|:-------------------------------------------------------------|----------:|------:|
| `nutrition`          | Nutrition                                     | The amount of health icons healed by the food                |   Integer |       |
| `saturation`         | Saturation                                    | Saturation healed by the food                                |    Double |       |
| `saturation`         | Saturation                                    | Saturation healed by the food                                |    Double |       |
| `always_edible`      | Always Edible                                 | Whether an item can be eaten when your health bar is full    |   Boolean |       |
| ~~`potion effects`~~ | __REMOVED IN 1.21.4__ <br> ~~Potion effects~~ | Potion effects given to the player when the item is consumed |   Complex |       |
| ~~`eat_seconds`~~    | __REMOVED IN 1.21.4__<br> ~~Eat seconds~~     | Time in seconds it takes to consume an item                  |    Double |       |
| ~~`eat_seconds`~~    | __REMOVED IN 1.21.4__<br> ~~Eat seconds~~     | Time in seconds it takes to consume an item                  |    Double |       |

### Consumable

New in the version for paper 1.21.4

```yaml
consumable:
  consume_seconds: 1.0
  animation: EAT
  has_consume_particles: true
  potion_effects:
    potion-effect-name:
      duration: 200
      amplifier: 1
      ambient: true
      particles: true
      icon: true
      probability: 0.5
```

| Value                   | Stat                  | Description                                                  | Data Type |                                                                                                                     Notes |
|:------------------------|-----------------------|:-------------------------------------------------------------|----------:|--------------------------------------------------------------------------------------------------------------------------:|
| `consume_seconds`       | Consumption Time      | The time in seconds taken to consume the item                |    Double |                                                                                                                           |
| `consume_seconds`       | Consumption Time      | The time in seconds taken to consume the item                |    Double |                                                                                                                           |
| `animation`             | Animation             | The animation played during consumption                      |      Enum | [Animation list](https://jd.papermc.io/paper/1.21.4/io/papermc/paper/datacomponent/item/consumable/ItemUseAnimation.html) |
| `has_consume_particles` | Consumption Particles | Whether particles are played during consumption              |   Boolean |                                                                                                                           |
| `potion_effects`        | Potion effects        | Potion effects given to the player when the item is consumed |   Complex |                                                                                                                           |


### Potion effect parameters
| Value                | Stat               | Description                                 | Data Type |                                                                                                Notes |
|:---------------------|--------------------|:--------------------------------------------|----------:|-----------------------------------------------------------------------------------------------------:|
| `potion-effect-name` | Potion effect name | Replace with the potion effect type name    |      Enum | [1.21 effect list](https://hub.spigotmc.org/javadocs/spigot/org/bukkit/potion/PotionEffectType.html) |
| `duration`           | Duration           | Duration in ticks of the effect             |   Integer |                                                                                                      |
| `amplifier`          | Amplifier          | Level of the effect - starting with 0       |   Integer |                                                                                                      |
| `ambient`            | Ambient            | Whether the displayed particles are ambient |   Boolean |                                                                                                      |
| `particles`          | Show particles     | Whether the effect particles are displayed  |   Boolean |                                                                                                      |
| `icon`               | Show icon          | Whether the effect icon is displayed        |   Boolean |                                                                                                      |

### Potion effects
This section controls the potion effect which are applied when the item is consumed
Adding "clear" as seen below clears the potion effects

The probability parameter is the chance of the effect being applied when the item is consumed.

```yaml

potion_effects:
  potion-effect-name:
    duration: 200
    amplifier: 1
    ambient: true 
    particles: false
    icon: true
    probability: 0.5
```

```yaml
potion_effects:
  clear:
```

### Armour

```yaml
armour:
  type: MEDIUM_ARMOUR
  colour:
    r: 255
    g: 255
    b: 255
```

| Value             | Name            | Description                                                           |     Data Type |                    Notes |
|:------------------|-----------------|:----------------------------------------------------------------------|--------------:|-------------------------:|
| `colour`          | Colour          | The colour of the armour (leather only) in RGB format                 | Complex (RGB) |                          |
| `type`            | Armour Type     | The armour type                                                       |          Enum | Applied as a description |
| `equip_sound`     | Equip Sound     | The equipping sound                                                   |        String |                          |
| `model`           | Model           | The armour model                                                      |        String |                          |
| `damage_on_hurt`  | Damage on hurt  | Whether the armour durability is lowered when the player takes damage |       Boolean |                          |
| `cold_protection` | Cold Protection | Coming soon...                                                        |       Boolean |                          |


#### Armour Types (WIP)

Optional value. Works in conjunction with MineshaftRpg. Does nothing on its own

| Type            | Name           |
|:----------------|----------------|
| `NONE`          | None (default) |
| `CLOTHES`       | Clothes        |
| `LIGHT_ARMOUR`  | Light Armour   |
| `MEDIUM_ARMOUR` | Medium Armour  |
| `HEAVY_ARMOUR`  | Heavy Armour   |

### Tool Component

Allows to modify tool properties or set any other item as a tool

```yaml
tool:
  damage_per_block: 1
  mining_speed: 2
  block_rules:
    incorrect_for_wooden_tool:
    correct_for_drops: false
  mineable_pickaxe:
    correct_for_drops: true
    mining_speed: 2
```
| Value              | Description                                 | Data Type |
|:-------------------|---------------------------------------------|:----------|
| `damage_per_block` | The damage done to the tool per block mined | Integer   |
| `mining_speed`     | The default mining speed                    | Double    |
| `block_rules`      | A list of specific block mining rules       | Complex   |

#### Default Mining Speed Values

| Tool Type | Value |
|:----------|-------|
| Wooden    | 2     | 
| Stone     | 4     | 
| Iron      | 6     | 
| Diamond   | 8     | 
| Gold      | 10    | 

#### Block Rules

Block rules allow to define which blocks can be mined

```yaml
block_rules:
  block_tag:
    correct_for_drops: true
    mining_speed: 3
```

| Value               | Description                                                               | Data Type | Notes                                                                            |
|:--------------------|---------------------------------------------------------------------------|:----------|----------------------------------------------------------------------------------|
| `block_tag`         | The name of the block tag containing the blocks corresponding to the rule | Integer   | [Vanilla Tag List](https://hub.spigotmc.org/javadocs/spigot/org/bukkit/Tag.html) |
| `correct_for_drops` | Whether item drops will be obtained from blocks in this list              | Boolean   |                                                                                  |
| `mining_speed`      | The mining speed of blocks in this list                                   | Double    |                                                                                  |

### Crafting Mechanics

#### Item repair

Allows for items to be repaired on an anvil, regaining the specified durability.

```yaml
repair-materials:
  iron_ingot: 115
```

#### Crafting

*Work in progress. Documentation coming soon...*

Allows for items to be crafted using a hardcoded recipe and specified materials.

Examples: 
```yaml
craft-materials:
  c1: iron_ingot
  c2: stick
hardcoded-recipe: shortsword
```

#### Hardcoded recipe types:

| Event               | Description                                     | Pattern                                                                                                                                                                                                                                                                                    |
|:--------------------|-------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `sword`             | The minecraft sword recipe. Alias: `shortsword` | <table><tr><td></td><td>c1</td><td></td></tr><tr><td></td><td>c1</td></tr><tr><td></td><td>c2</td></tr></tbody></table>                                                                                                                                                                    |
| `longsword`         | A custom sword recipe                           | <table><tr><td></td><td>c1</td><td></td></tr><tr><td></td><td>c1</td></tr><tr><td>c1</td><td>c2</td><td>c1</td></tr></tbody></table>                                                                                                                                                       |
| `pickaxe`           |                                                 | <table><tr><td>c1</td><td>c1</td><td>c1</td></tr><tr><td></td><td>c2</td><td></td></tr><tr><td></td><td>c2</td><td></td></tr></tbody></table>                                                                                                                                              |
| `axe`               | Has two interchangeable patterns                | <table><tr><td>c1</td><td>c1</td><td></td></tr><tr><td>c1</td><td>c2</td><td></td></tr><tr><td></td><td>c2</td><td></td></tr></tbody></table><table><tr><td></td><td>c1</td><td>c1</td></tr><tr><td></td><td>c2</td><td>c1</td></tr><tr><td></td><td>c2</td><td></td></tr></tbody></table> |
| `shovel`            |                                                 | <table><tr><td></td><td>c1</td><td></td></tr><tr><td></td><td>c2</td><td></td></tr><tr><td></td><td>c2</td><td></td></tr></tbody></table>                                                                                                                                                  |
| `hoe`               | Has two interchangeable patterns                | <table><tr><td>c1</td><td>c1</td><td></td></tr><tr><td></td><td>c2</td><td></td></tr><tr><td></td><td>c2</td><td></td></tr></tbody></table> <table><tr><td></td><td>c1</td><td>c1</td></tr><tr><td></td><td>c2</td><td></td></tr><tr><td></td><td>c2</td><td></td></tr></tbody></table>    |
| `helmet_simple`     |                                                 | <table><tr><td>c1</td><td>c1</td><td>c1</td></tr><tr><td>c1</td><td></td><td>c1</td></tr><tr><td></td><td></td><td></td></tr></tbody></table>                                                                                                                                              |
| `chestplate_simple` |                                                 | <table><tr><td>c1</td><td></td><td>c1</td></tr><tr><td>c1</td><td>c1</td><td>c1</td></tr><tr><td>c1</td><td>c1</td><td>c1</td></tr></tbody></table>                                                                                                                                        |
| `leggings_simple`   |                                                 | <table><tr><td>c1</td><td>c1</td><td>c1</td></tr><tr><td>c1</td><td></td><td>c1</td></tr><tr><td>c1</td><td></td><td>c1</td></tr></tbody></table>                                                                                                                                          |
| `boots_simple`      |                                                 | <table><tr><td></td><td></td><td></td></tr><tr><td>c1</td><td></td><td>c1</td></tr><tr><td>c1</td><td></td><td>c1</td></tr></tbody></table>                                                                                                                                                |

#### Deconstruction recipes

***Currently, do not work! Items yield nothing when smelted.***
<br>*Work in progress coming soon...*

```yaml
craft-materials:
  melting: diamond
hardcoded-recipe: helmet_simple
```

When melting an item, the obtained number of items is based upon the durability, with the maximum equal to the number of `c1` items used in the hardcoded recipe.
<br><br>
The type of item obtained is based upon the `craft-material` with the `melting` tag.

### Item events
This section controls what events are triggered when the player executes a certain action. This includes the item being right or left-clicked.

```yaml
action:
  right_click:
  - blaster-shot
```

### Hardcoded events

Custom events must not be named using any of the following names. These are reserved for hardcoded features.

| Event                       | Description                                                                                                         | 
|:----------------------------|---------------------------------------------------------------------------------------------------------------------|
| `parry`                     | Implements weapon blocking similar to sword blocking. Works not just for swords. Works only for right click actions |
| `power_attack`              | Charges up the weapon for a strong attack                                                                           |
| `reload`                    | Reloads the weapon, if it is a ranged weapon                                                                        |
| `wand`                      | Opens the spell casting menu                                                                                        |
| `use_ammo`,`use_ammunition` | Consumes the item's ammunition (e.g. on shot)                                                                       |


### Item event parameters

| Value         | Description                                                                         | Data Type   |
|:--------------|-------------------------------------------------------------------------------------|:------------|
| `right_click` | A list of events triggered when the player right-clicks with the item in their hand | String List |
| `left_click`  | A list of events triggered when the player left-clicks with the item in their hand  | String list |

The specific event filename (excluding the `.yml` or `.yaml` extension) is used to access the event.

## Obtaining custom items

Custom items can be obtained using the `/getitem` command. This command is by default accessible to players with operator permissions.

Usage: `/getitem <item name>`

The item name is replaced by the name of the file the item is saved in (excluding the file extension), not the custom name of the item.

Examples:
`/getitem example-item` grants the player the item with the name `example-item`.
This may be saved in the file `example-item.yml` or `example-item.yaml`

`/getitem diamond-sword` grants the player the item with the name `diamond-sword`

# Event creation
Custom events are created via YAML configuration files, similarly to custom items. These can be executed using a command or bound to an item, using the item configuration file. These files are placed in the `Mineshaft/Events` folder in the server plugin directory.

They can have many different parameters, which control what happens when the item is executed.

If your `Events` folder is empty, an example file called `example-event.yml` will be created.

Each event is placed in a separate YAML file. The event can be bound to an item (in the [item's YAML file](#item-events)) or executed in game for debugging purposes.

## Event example

```yaml
parent: 'null'
event_type: BEAM
colour: '208010010'
fly_distance: 40
speed: 5
particle_type: DUST
particle_size: 1
offset:
  x: 0
  y: 1.5
  z: 0
on_hit:
  entity:
    1:
      event_type: ENTITY_DAMAGE
      damage: 10
      damage_type: FREEZE
```

This event will fire a red coloured beam. If it hits a mob, it will damage it for 8 damage. 

![An image of the vent being triggered by a player](https://raw.githubusercontent.com/SF-24/images/main/event2.png)The event shown above being triggered by a player

## List of event parameters
The following parameters are available for all events. Each event type has unique parameters only available for its type.

| Value        | Description                                                                                 | Data Type |
|:-------------|---------------------------------------------------------------------------------------------|:----------|
| `parent`     | The parent event. Currently WIP                                                             | String    |
| `event_type` | The event type. Specifies what happens when the event is triggered.                         | Enum      |
| `offset`     | The offset of the event. These values are added to the location where the event is executed | Complex   |
| `sound`      | The sound which plays when the event triggers                                               | String    |

| Event Type                     | Description                                            |
|:-------------------------------|--------------------------------------------------------|
| `BEAM`                         | Shoots a beam from the target location                 | 
| `PLAYER_VECTOR_DASH`           | Makes the player dash forwards                         | 
| `PLAYER_VECTOR_LEAP`           | Makes the player leap in the direction they are facing | 
| `ENTITY_PREPARE_STRONG_ATTACK` | Makes the next attack stronger                         | 
| `ENTITY_EXECUTE_STRONG_ATTACK` | Executes a stronger attack                             | 
| `ENTITY_DAMAGE`                | Inflicts damage upon an entity or player               | 
| `ENTITY_DISARM`                | Disarms the target entity                              | 
| `ENTITY_KNOCKBACK`             | Knocks back the target entity                          | 


### Beam event type

The beam event fires off a beam in the direction in which the entity that executed it is facing. It supports multiple unique parameters. Not all parameters have to be filled in for the event to work

| Value           | Description                                                              | Data Type |
|:----------------|--------------------------------------------------------------------------|:----------|
| `colour`        | The colour of the beam particles in RGB format (format: RRRGGGBBB)       | String    |
| `particle_type` | The particle making up the beam                                          | Enum      |
| `particle_size` | The size of the beam particles. Only applies to certain particles        | Integer   |
| `fly_distance`  | The maximum distance the beam can fly. Only applies to certain particles | Integer   |
| `speed`         | The speed of the beam in blocks per tick                                 | Integer   |
| `offset`        | The offset from the cast location as a 3D vector                         | Vector    |
| `on_hit`        | The local event, which occurs after a collision                          | Complex   |

#### On hit local events

***Completely redone to support inline event declaration***

*Work in progress. Not yet tested*

On hit example:
```yaml
on_hit:
  player:
    1:
      event_type: PLAYER_VECTOR_LEAP
  entity:
    2:
      event_type: ENTITY_DAMAGE
      damage: 10
      damage_type: FREEZE
  block:
```

### Vector player event type

Includes the `PLAYER_VECTOR_DASH` and `PLAYER_VECTOR_LEAP`.

```yaml
parent: 'null'
event_type: PLAYER_VECTOR_DASH
vector_bounds:
  y_min: 0
  y_max: 0.25
  planar_min: -1
  planar_max: -1
```

```yaml
parent: 'null'
event_type: PLAYER_VECTOR_LEAP
vector_bounds:
  y_min: 5
  y_max: 0.9
  planar_min: 0.0
  planar_max: 2.0
```

Takes bounds parameter. Not all bounds have to be defined. Both minimum and maximum of a bound have to be defined for them to be taken into account.
<br>
Making a value negative leads to it being ignored and treats it as undefinded.
<br><br>
Optionally, the `legacy: "true"` or `"is_legacy_event: "true"` flag can be used to create automatic bounds. However, this is _deprecated_ and ___may eventually be removed___.
Using this is __not recommended__. If you do this, you are doing it ___at your own risk___.

```yaml
parent: 'null'
event_type: PLAYER_VECTOR_DASH
legacy: true
```

### Strong attack event type

*Work in progress. Has not been tested.*

The next attack dealt after this attack is modified according to the parameters.<br>
The particles are shown and the sound is played when the attack is triggered.

```yaml
parent: 'null'
event_type: ENTITY_PREPARE_STRONG_ATTACK
damage_multiplier: 2.0
knockback_power: 1.2
attack_sound: block.stone.hit
particles: false
```

The `EXECUTE_STRONG_ATTACK` event type has the same definition and the strong attack has the same execution, but the player attacks immediately and if the attack is not successful,
the attack is not triggered the next time the player attacks

```yaml
parent: 'null'
event_type: ENTITY_EXECUTE_STRONG_ATTACK
damage_multiplier: 3.0
knockback_power: 1.5
attack_sound: block.stone.hit
particles: true
```

### Entity damage event type

`Damage type is the [damage type](https://jd.papermc.io/paper/1.21.5/org/bukkit/damage/DamageType.html).<br>
Damage is the amount of damage inflicted

```yaml
parent: 'null'
event_type: ENTITY_DAMAGE
damage: 5
damage_type: FREEZE
```

### Entity disarm event type

*Work in progress. Has not been tested.*

```yaml
parent: 'null'
event_type: ENTITY_DISARM
use_disarm_roll: true
use_knockback: false
use_facing_item_vector: true
base_strength: 1
```

`use_knockback` determines whether the entity is knocked back or not. Only works if the event was executed by a caster.<br>
`use_facing_item_vector` determines whether the entity is disarmed based upon the casters directions. If there is no caster this has no effect.<br>
`use_disarm_roll` determines whether a randomised method is used to check if the target is disarmed.<br>
`base_strength` affects the initial roll if the `use_disarm_roll` is used.


### Entity knockback event type

*Work in progress. Has not been tested.*

Damage type is the [damage type](https://jd.papermc.io/paper/1.21.5/org/bukkit/damage/DamageType.html).<br>
Damage is the amount of damage inflicted

```yaml
parent: 'null'
event_type: ENTITY_KNOCKBACK
knockback_multiplier: 1.0
limit_vertical_knockback: true
```

`knockback_multiplier` determines how far the target is knocked back.<br>
`limit_vertical_knockback` determines the vertical knockback is limited so that the target does not fly upwards.<br>

### ***TODO: more coming soon***

# Configuration

Functionality of the plugin can be modified via the `config.yml` file in the plugin folder. The currency name parameter does not yet work.
<br><br>
The `materials` section does not yet do anything.

```yaml
# Whether vault compatibility is enabled
enable-vault: true

# Whether items have a cooldown animation
enable-cooldown-animation: true
# Whether the sidebar is enabled
enable-sidebar: false

# Whether the item rarity description will be italic
italic-item-rarity: true
# Singular and plural form of the currency name
currency-singular: Credit
currency-plural: Credits

# Controls what crafting stations the un-smelting recipes are added to
# Un-smelting recipes are specified in item creation
add-unsmelting-recipes-to-furnace: false
add-unsmelting-recipes-to-smoker: false
add-unsmelting-recipes-to-blast-furnace: true
add-unsmelting-recipes-to-campfire: false

# Whether nuggets are used in cases where a whole ingot would not be given
use-smelting-nuggets: true

# Default unsmelting time in ticks
default-unsmelting-time: 200;

# Material Registry
# Used alongside use-smelting-nuggets
# Usage: <item>:
#          <material>: <number>
#
# Where item is the main material name,
# the number represents how many of the material constitute to the item
# and the material is the material
materials:
  iron_ingot:
    iron_nugget: 9
  gold_ingot:
    gold_nugget: 9
```

# Integrations

## Mythic Mobs

The MythicMobs integration allows you to trigger events *directly* from a MythicMobs file in the form of MythicMobs skills.

The section can be directly appended to the `Skills` section of a MythicMob or the `Skills` section of a MythicMob skill. More information on the MythicMobs API is available on their [wiki](https://git.mythiccraft.io/mythiccraft/MythicMobs/-/wikis/home)

Below is an example of a MythicMobs skill, which containing MineshaftAPI event. 
```yaml
BlasterBeam:
  Cooldown: 1.5
  Skills:
  - targetedmineshaftevent{e=blaster-shot;d=4;o=13} @Target
  - sound{s=minecraft:blaster.blasterrifle;v=5.0} @self
```
The @Target parameter after the skill just specifies the target and is an inbuilt MythicMobs function. 
For entity targeters, please refer to the MythicMobs [wiki page](https://git.mythiccraft.io/mythiccraft/MythicMobs/-/wikis/Skills/Targeters)
<br>
Currently there are two types of MythicMobs events: `mineshaftevent` and `targetedmineshaftevent`
<br><br>
Another feature is setting the armour class of enemies.

### Mineshaft event
The mineshaft event takes a location as a target the entity is aiming at. This allows the entity to aim at a block rather than a player or entity.

Syntax:
`mineshaftevent{e=event-name;d=damage}` or
`mineshaftevent{event=event-name;damage=damage}`

Where `damage` is the amount of damage dealth if the event damages an entity (this only works with certain events) and `event-name` is the name of the event.

Examples:
`mineshaftevent{event=loud-bang}`
`mineshaftevent{e=laser-shot;d=10} @TargetLocation`
`mineshaftevent{event=smoke-bomb;d=1} @self`

### Targeted Mineshaft event
The targeted mineshaft event is incredibly similar, however instead of a location, it takes the target as an entity.
<br>
With events fired in a direction, like the beam event, this fires them in the direction of the target.
<br>
To stop the MythicMobs having too accurate aim, an optional parameter `offset` has been added. The offset (in degrees on an axis, distributed uniformly (WIP)) determines the maximum amount of degrees the beam is rotated away from the target, allowing the mob to miss its shot (by a maximum margin of your choice).
<br>
Syntax:
`targetedmineshaftevent{e=event-name;d=damage;o=offset}` or
`targetedmineshaftevent{event=event-name;damage=damage;offset=offset}`

Where `damage` is the amount of damage dealt if the event damages an entity (this only works with certain events, which do this), `event-name` is the name of the event and `offset` is the offset (explained above).  

Examples:
`mineshaftevent{event=loud-bang}`
`mineshaftevent{e=direct-shot;d=22} @lastAttacker`
`mineshaftevent{e=laser-shot;d=10;offset=2} @Target`
`mineshaftevent{event=grenade-throw;o=25} @NearestPlayer`

#### Setting armour class

```yaml
SetArmourClass:
Skills:
  - mineshaftarmourclass{v=10} @Self
  - mineshaftarmourclass{value=10} @Self
```
The `v` (or `value`) parameter sets the armour class value. Leaving it unset sets it to 0.
<br>
The aliases `mineshaftarmourclass`, `mythic_armour_class`, `mineshaftarmour` and `mineshaft_armour` can be used for this event.
<br><br>
Regardless of the target, this event is triggered on the caster.

## BetonQuest

Description coming soon...

## PlaceholderAPI

| Placeholder    | Description                                     | Data Type |
|:---------------|-------------------------------------------------|:----------|
| `profile_name` | The current profile name                        | String    |
| `coins`        | Number of coins owned                           | Double    |
| `test`         | A debug placeholder                             | String    |

Some placeholders require the presence of MineshaftRpg to fully work:

| Placeholder | Description                                     | Data Type |
|:------------|-------------------------------------------------|:----------|
| `level`     | Current level ('1' unless MineshaftRpg is used) | Integer   |
| `xp`        | Current xp ('0' unless MineshaftRpg is used)    | Integer   |
| `culture`   | Current culture                                 | String    |


# Dependents
## MineshaftRpg

A plugin based off the old RpgPlugin. Contains multiple features like character creation, ability scores, skills, levelling etc.

# Plugin reloading
The plugin can be reloaded via the `/mineshaft` command, which is automatically available to operators.

Usage:
`/mineshaft reload [all|items|events|configs]`

Examples:
`/mineshaft reload` and `/mineshaft reload all` reloads the whole plugin
`/mineshaft reload item` and `/mineshaft reload items` reloads custom items
`/mineshaft reload event` and `/mineshaft reload events` reloads events
`/mineshaft reload config` and `/mineshaft reload configs` reloads the configuration
