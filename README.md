# MineshaftAPI
An spigot plugin for Minecraft servers. Build with maven, documentation coming soon. Designed for Minecraft 1.20.6

## Documentation
Currently, the only functionality of this plugin is custom item creation and **Vault** server economy.

A work in progress sidebar is displayed to all players on the server. Customization is not yet available

## Item Creation 

Items are created via custom YAML files. These are placed in the `Mineshaft/Data/Items` folder in the plugin directory of your server.

Items can have a multitude of different parameters, which are used to customize the item.

If your `Items` folder is empty, an example file `example-item.yml` will be placed in this folder.

### Example



### List of parameters

## Plugin reloading
The plugin can be reloaded via the `/mineshaft` command, which is automatically available to operators.

Usage:
`/mineshaft reload [all|items|configs]`

Examples:
`/mineshaft reload` and `/mineshaft reload all` reloads the whole plugin
`/mineshaft reload item` and `/mineshaft reload items` reloads custom items
`/mineshaft reload config` and `/mineshaft reload configs` will reload the configuration files. However, this functionality is yet to be implemented
