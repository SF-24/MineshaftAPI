


# MineshaftAPI
An spigot plugin for Minecraft servers. Build with maven, documentation coming soon. Designed for Minecraft 1.20.6

## Table of Contents
- [Copyright notice](#Important-Copyright-Information)
- [Documentation introduction](#Documentation)
- [Item creation](#Item-Creation )
	- [Example](#Example)
	- [List of parameters](#List-of-parameters)
		- [Item category list](#item-category-list)
		- [Item rarity list](#item-rarity-list)
		- [Item stats and attributes](#item-stats-and-attributes)
		- [List of item attributes](#list-of-item-attributes)
		-  [Food](#food)
		 - [List of food stats](#list-of-food-stats)
		 - [Potion effects](#potion-effects)
		 - [Potion effect parameters](#potion-effect-parameters)
		 - [Item events](#item-events)
		 - [Item event parameters](#item-event-parameters)
	- [Obtaining custom items](#obtaining-custom-items)
- [Event creation](#event-creation)
  - [Event example](#event-example)
  - [List of event parameters](#list-of-event-parameters)
- [Plugin reloading](#plugin-reloading)

# Important Copyright Information
**This project is under the GPU GPL v3 license**
Terms of use can be found here: https://www.gnu.org/licenses/gpl-3.0.en.html

# Documentation
Currently, the only functionality of this plugin are custom item creation, event creation and **Vault** server economy.

Events can be triggered as **MythicMobs** mechanics via the integration.

A work in progress sidebar (disabled by default) is displayed to all players on the server. Customisation is not yet available. 

Images representing examples of content included in the plugin may not be entirely accurate, as these were taken by a user with client mods and a resource pack altering the look of the game.

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

| Parameter    | Description | Data Type | Note
|:---------|:-----------:|-----------:|--:|
| `parent` | The name of the parent item. All values unspecified in the file will be set to those of the parent. A parent item may also have its own parent item.| String||
| `name` | The custom name for the item | String||
| `subcategory` | The displayed category of the item |String|Only affects the item description. Use any value you want |
| `material` | The Minecraft material for the item| Enum | [1.21 Material List](https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Material.html)|
| `rarity` | Rarity of the item |Enum|[Rarity List](#Item-Rarity-List)|
| `item_category` | Category of the item | Enum|[Category List](#Item-Category-List)|
| `custom_model_data` | Custom Model Data | Integer||
| `durability` | The amount of times an item may be used before it breaks | Integer||
| `stack_size` | The maximum stack size of any given item | Integer|Value between 1 and 99|
| `hide_attributes` | Whether the default Minecraft attribute description will be hidden | boolean|Default is true. Only set to false for debugging.|
| `enchantment_glint` | WIP: do not set | boolean||
| `stats` | List of item attributes | HashMap||
|`id`|A unique id assigned by the plugin. This is used for the plugin to recognize the item.|UUID|Do not change or set manually. Two items having the same id will result in **MAJOR** bugs. Changing an item id results in previous items breaking.

### Item Category List

| Value | Description |
|:---------|:-----------|
| `weapon_melee` | Melee weapon |
| `weapon_ranged` | Ranged weapon |
| `armour_helmet` | Helmet (Equip in head slot) |
| `armour_chestplate` | Chestplate (Equip in chest slot) |
| `armour_leggings` | Leggings (Equip in leg slot) |
| `armour_boots` | Boots (Equip in foot slot) |
| `tool_axe` | Axe |
| `tool_pickaxe` | Pickaxe |
| `tool_shovel` | Shovel |
| `tool_hoe` | Hoe |
| `item_consumable` | Consumable item (One time use) |
| `item_generic` | Generic item (no category) |
| `other` | Other (Unspecified) |

### Item Rarity List

| Value | Rarity | Text Colour | Description |
|:---------|:-----------:|:--:|-|
| `standard` |~~No Description~~||Standard items with no rarity|
| `common` |<font color="white">Common</font>|white|
| `uncommon` |<font color="lime">Uncommon</font>|green|
| `rare` |<font color="blue">Rare</font>|blue|
| `exotic` |<font color="magenta">Exotic</font>|magenta/purple|
| `legendary` |<font color="ORANGE">Legendary</font>|orange/gold|

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
  defence: 32
  health: 17
```

### List of item attributes

| Value | Stat | Description | Notes
|:---------|-|:-----------|-:|
| `damage` |Attack damage|  Increases damage dealt via ranged and melee | WIP -> extremely glitchy when using ranged weapons |
| `speed` | Health| Increases maximum health when equipped||
| `defence` | Defence| Reduces damage taken (excluding certain sources like drowning) | Defence scalability will be modified|
| `speed` | Speed | Will increase movement speed when implemented | Stat is applied to items, however functionality has not yet been implemented |
| `reach` | Reach | Affects block and entity interaction reach distance | | `mining_reach` | Block Interaction Reach | Affects block interaction reach distance |
| `attack_reach` | Entity Interaction Reach | Affects entity interaction reach distance | 
| `attack_speed` | Attack Speed | The attack speed of an item. | Work in progress. Do not use on armour | 


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
  eat_seconds: 0.2
  potion_effects:
    speed:
      duration: 200
      amplifier: 1
      ambient: true 
      particles: false
      icon: true
```

### List of food stats

| Value | Stat | Description |Data Type|Notes
|:---------|-|:-----------|-:|-:|
| `nutrition`|Nutrition|The amount of health icons healed by the food|Integer
| `saturation`|Saturation|Saturation healed by the food|Float|
| `eat_seconds`|Eat seconds|Time in seconds it takes to consume an item|Float|
| `always_edible`|Always Edible|Whether an item can be eaten when your health bar is full|Boolean|
| `potion effects`|Potion effects|Potion effects given to the player when the item is consumed|Complex|

### Potion effects
This section controls the potion effect which are applied when the item is consumed

```yaml
potion_effects:
  potion-effect-name:
    duration: 200
    amplifier: 1
    ambient: true 
    particles: false
    icon: true
```

### Potion effect parameters
| Value | Stat | Description |Data Type|Notes
|:---------|-|:-----------|-:|-:|
| `potion-effect-name`|Potion effect name|Replace with the potion effect type name|Enum|[1.21 effect list](https://hub.spigotmc.org/javadocs/spigot/org/bukkit/potion/PotionEffectType.html)
| `duration`|Duration|Duration in ticks of the effect|Integer|
| `amplifier`|Amplifier|Level of the effect - starting with 0|Integer|
| `ambient`|Ambient|Whether the displayed particles are ambient|Boolean|
| `particles`|Show particles|Whether the effect particles are displayed|Boolean|
| `icon`|Show icon|Whether the effect icon is displayed|Boolean|

### Item events
This section controls what events are triggered when the player executes a certain action. This includes the item being right or left-clicked.

```yaml
action:
  right_click:
  - blaster-shot
```

### Item event parameters

| Value | Description |Data Type
|:---------|-|:-----------|
| `right_click`|A list of events triggered when the player right-clicks with the item in their hand|String List|
| `left_click`|A list of events triggered when the player left-clicks with the item in their hand|String list|

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
    damage: 8
```

This event will fire a red coloured beam. If it hits a mob, it will damage it for 8 damage. 

![An image of the vent being triggered by a player](https://raw.githubusercontent.com/SF-24/images/main/event2.png)The event shown above being triggered by a player

## List of event parameters
### ***Coming soon....***

# Configuration

Functionality of the plugin can be modified via the `config.yml` file in the plugin folder. The currency name parameter does not yet work.

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
```

# Plugin reloading
The plugin can be reloaded via the `/mineshaft` command, which is automatically available to operators.

Usage:
`/mineshaft reload [all|items|events|configs]`

Examples:
`/mineshaft reload` and `/mineshaft reload all` reloads the whole plugin
`/mineshaft reload item` and `/mineshaft reload items` reloads custom items
`/mineshaft reload event` and `/mineshaft reload events` reloads events
`/mineshaft reload config` and `/mineshaft reload configs` reloads the configuration
