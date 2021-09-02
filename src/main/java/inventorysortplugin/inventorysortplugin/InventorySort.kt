package inventorysortplugin.inventorysortplugin

import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

object InventorySort {

    fun invSort(inv : Inventory): List<ItemStack> {
        val normalItemList = ArrayList<ItemStack>()
        val elseItemList = ArrayList<ItemStack>()
        val nbtItemList = ArrayList<ItemStack>()


        for ((index,content) in inv.contents.withIndex()){
            if (inv.type == InventoryType.PLAYER){
                if (index < 9)continue
                if (index in 36..39)continue
            }
            if (content == null || content.type == Material.AIR)continue
            if (content.hasItemMeta()) {
                if (!content.itemMeta.persistentDataContainer.isEmpty)nbtItemList.add(content)
                else elseItemList.add(content)
            }
            else normalItemList.add(content)
            inv.clear(index)
        }
        val sortedMaterial = sortByMaterial(normalItemList)
        val sortedNBT = sortByNBT(nbtItemList)
        val sortedDisplay = sortByDisplayName(elseItemList)

        return arrayListOf(sortedNBT,sortedDisplay,sortedMaterial).flatten()
    }

    fun playerInvSort(p : Player){
        val sortContents = invSort(p.inventory)
        var index = 9
        for (content in sortContents){
            p.inventory.setItem(index,content)
            index++
        }
        p.sendMessage("§aインベントリをソートしました")
        return
    }

    private fun sortByMaterial(contents : ArrayList<ItemStack>): ArrayList<ItemStack> {
        val list = stackItems(contents)
        list.sortBy { it.type }
        return list
    }

    private fun sortByNBT(contents: ArrayList<ItemStack>): ArrayList<ItemStack> {
        val list = stackItems(contents)
        list.sortBy { it.itemMeta.persistentDataContainer.keys.first().key }
        return list
    }

    private fun sortByDisplayName(contents: ArrayList<ItemStack>): ArrayList<ItemStack> {
        val list = stackItems(contents)
        list.sortBy { it.itemMeta.displayName }
        return list
    }

    private fun stackItems(contents: ArrayList<ItemStack>): ArrayList<ItemStack> {
        val inv = Bukkit.createInventory(null,54, Component.text("sort"))
        val list = ArrayList<ItemStack>()

        for (content in contents){
            inv.addItem(content)
        }

        for (content in inv.contents){
            if (content == null || content.type == Material.AIR)break
            list.add(content)
        }

        return list
    }


}