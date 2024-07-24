
# MineshaftAPI
An spigot plugin for Minecraft servers. Build with maven, documentation coming soon. Designed for Minecraft 1.20.6

## Important Copyright Information
**This project is under the GPU GPL v3 license**
Terms of use can be found here: https://www.gnu.org/licenses/gpl-3.0.en.html

## Documentation
Currently, the only functionality of this plugin is custom item creation and **Vault** server economy.

A work in progress sidebar is displayed to all players on the server. Customization is not yet available

## Item Creation 

Items are created via custom YAML files. These are placed in the `Mineshaft/Data/Items` folder in the plugin directory of your server.

Items can have a multitude of different parameters, which are used to customize the item.

If your `Items` folder is empty, an example file `example-item.yml` will be placed in this folder.

### Example

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

### List of parameters

| Parameter    | Description | Usage |
|--------------|:-----------:|-----------:|
| `material`   |  1.99 |        739 |
| Bananas      |  1.89 |          6 |

### Obtaining custom items

Custom items can be obtained using the `/getitem` command. This command is by default accessible to players with operator permissions.

Usage: `/getitem <item name>`

The item name is replaced by the name of the file the item is saved in (excluding the file extension), not the custom name of the item.

Examples:
`/getitem example-item` grants the player the item with the name `example-item`.
This may be saved in the file `example-item.yml` or `example-item.yaml`

`/getitem diamond-sword` grants the player the item with the name `diamond-sword`

## Plugin reloading
The plugin can be reloaded via the `/mineshaft` command, which is automatically available to operators.

Usage:
`/mineshaft reload [all|items|configs]`

Examples:
`/mineshaft reload` and `/mineshaft reload all` reloads the whole plugin
`/mineshaft reload item` and `/mineshaft reload items` reloads custom items
`/mineshaft reload config` and `/mineshaft reload configs` will reload the configuration files. However, this functionality is yet to be implemented
