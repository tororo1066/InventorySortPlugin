package inventorysortplugin.inventorysortplugin

import inventorysortplugin.inventorysortplugin.InventorySort.invSort
import inventorysortplugin.inventorysortplugin.InventorySort.playerInvSort
import inventorysortplugin.inventorysortplugin.InventorySortPlugin.Companion.wand
import org.bukkit.Material
import org.bukkit.block.Barrel
import org.bukkit.block.Chest
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot

object InventorySortEvent : Listener {

    @EventHandler
    fun chestClick(e : PlayerInteractEvent){
        if (e.hand == EquipmentSlot.OFF_HAND)return
        if (e.action != Action.RIGHT_CLICK_BLOCK)return
        if (!e.hasItem() || !e.hasBlock())return

        val block = e.clickedBlock!!
        val item = e.item!!

        if (item != wand)return
        if (block.type != Material.BARREL && block.type != Material.CHEST)return

        e.isCancelled = true

        if (!e.player.hasPermission("msort.user")){
            e.player.sendMessage("§4You don't have permission!")
            return
        }

        if (block.type == Material.BARREL){
            val inv = block.state as Barrel

            if (inv.isLocked || inv.isOpen){
                e.player.sendMessage("§4この樽はロックがかけられている、または開かれています")
                return
            }
            inv.inventory.contents = invSort(inv.inventory).toTypedArray()
            e.player.sendMessage("§a樽の中身をソートしました")
            return
        }else{
            val inv = block.state as Chest

            if (inv.isLocked || inv.isOpen){
                e.player.sendMessage("§4このチェストはロックがかけられている、または開かれています")
                return
            }
            inv.inventory.contents = invSort(inv.inventory).toTypedArray()
            e.player.sendMessage("§aチェストの中身をソートしました")
            return
        }

    }

    @EventHandler
    fun airClick(e : PlayerInteractEvent){
        if (e.hand == EquipmentSlot.OFF_HAND)return
        if (e.action != Action.RIGHT_CLICK_AIR)return
        if (!e.hasItem())return

        val item = e.item!!
        if (item != wand)return

        e.isCancelled = true

        if (!e.player.hasPermission("msort.user")){
            e.player.sendMessage("§4You don't have permission!")
            return
        }
        playerInvSort(e.player)
    }
}