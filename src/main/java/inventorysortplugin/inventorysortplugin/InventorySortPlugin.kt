package inventorysortplugin.inventorysortplugin

import inventorysortplugin.inventorysortplugin.InventorySort.playerInvSort
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin

class InventorySortPlugin : JavaPlugin() {

    companion object{
        lateinit var wand : ItemStack
    }

    override fun onEnable() {
        getCommand("msort")?.setExecutor(this)
        server.pluginManager.registerEvents(InventorySortEvent,this)
        wand = generateWand()
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player)return true
        if (args.isEmpty()){
            playerInvSort(sender)
            return true
        }
        when(args[0]){

            "give"->{
                if (!sender.hasPermission("msort.op")){
                    sender.sendMessage("§4You don't have permission!")
                    return true
                }
                sender.inventory.setItemInMainHand(wand)
                return true
            }
        }
        return true
    }

    private fun generateWand(): ItemStack {
        val item = ItemStack(Material.STICK)
        val meta = item.itemMeta
        meta.displayName(Component.text("§e§lSortWand"))
        meta.lore(mutableListOf(Component.text("§a右クリックでソートする杖").asComponent(), Component.text("§a===ソートできるもの==="),
            Component.text("§b自身のインベントリ"),Component.text("§bチェスト"),Component.text("§b樽"), Component.text("§a========================")))
        item.itemMeta = meta
        return item
    }
}