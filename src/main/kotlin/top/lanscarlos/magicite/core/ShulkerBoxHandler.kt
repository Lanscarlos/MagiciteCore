package top.lanscarlos.magicite.core

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.block.ShulkerBox
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.inventory.CraftingInventory
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.BlockStateMeta
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common.platform.function.info
import taboolib.common.platform.function.submit
import taboolib.common5.Baffle
import taboolib.module.chat.colored
import taboolib.module.configuration.util.getStringColored
import taboolib.module.nms.ItemTagData
import taboolib.module.nms.getItemTag
import taboolib.module.nms.setItemTag
import taboolib.platform.util.buildItem
import taboolib.platform.util.hasName
import taboolib.platform.util.isAir
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * MagiciteCore
 * top.lanscarlos.magicite.core
 *
 * @author Lanscarlos
 * @since 2022-07-16 15:43
 */
object ShulkerBoxHandler {

    val baffle by lazy {
        Baffle.of(MagiciteCore.config.getLong("ShulkerBox-setting.cooldown.on-open", 5), TimeUnit.SECONDS)
    }

    val message by lazy {
        MagiciteCore.config.getStringColored("ShulkerBox-setting.cooldown.message")
    }

    class LockedHolder : InventoryHolder {
        override fun getInventory(): Inventory = Optional.empty<Inventory>().get()
    }

    fun ItemStack.isSkullBox(): Boolean {
        if (this.isAir()) return false
        return this.type.name.endsWith("SHULKER_BOX") && this.itemMeta?.hasDisplayName() == true
    }

    @SubscribeEvent
    fun e(e: PlayerInteractEvent) {
        if (!e.player.isSneaking) return
        if (!e.action.name.contains("RIGHT", true)) return
        val item = e.player.equipment?.itemInMainHand ?: return
        if (!item.isSkullBox()) return

        e.isCancelled = true

        // 检查冷却
        if (!baffle.hasNext(e.player.uniqueId.toString())) {
            message?.let { e.player.sendMessage(it) }
            e.player.closeInventory()
            e.player.playSound(e.player.location, Sound.BLOCK_SHULKER_BOX_OPEN, 1f, 1f)
            return
        }

        val inv = Bukkit.createInventory(LockedHolder(), 27, item.itemMeta!!.displayName)
        inv.contents = ((item.itemMeta as BlockStateMeta).blockState as ShulkerBox).inventory.contents
        e.player.openInventory(inv)
        e.player.playSound(e.player.location, Sound.BLOCK_SHULKER_BOX_OPEN, 1f, 1f)
    }

    @SubscribeEvent
    fun e(e: InventoryClickEvent) {
        val player = e.whoClicked as? Player ?: return
        if (player.openInventory.topInventory.holder !is LockedHolder) return
        if (e.currentItem?.isSkullBox() == true || ((e.click == ClickType.NUMBER_KEY) && (player.inventory.getItem(e.hotbarButton)?.isSkullBox() == true))) {
            e.isCancelled = true
            player.playSound(player.location, Sound.BLOCK_SHULKER_BOX_OPEN, 1f, 1f)
            return
        }
        if (player.equipment?.itemInMainHand?.isSkullBox() != true) {
            e.isCancelled = true
            player.closeInventory()
            return
        }

        submit(delay = 1) {
            val item = player.equipment?.itemInMainHand!!
            val meta = item.itemMeta as BlockStateMeta
            val state = meta.blockState as ShulkerBox
            state.inventory.contents = e.inventory.contents
            meta.blockState = state
            item.itemMeta = meta
            player.equipment!!.setItemInMainHand(item.formatNBT())
        }
//        player.playSound(player.location, Sound.BLOCK_SHULKER_BOX_CLOSE, 1f, 1f)
    }

    @SubscribeEvent
    fun e(e: InventoryCloseEvent) {
        val player = e.player as? Player ?: return
        if (player.openInventory.topInventory.holder !is LockedHolder || player.equipment?.itemInMainHand?.isSkullBox() != true) return
        val item = player.equipment?.itemInMainHand!!
        val meta = item.itemMeta as BlockStateMeta
        val state = meta.blockState as ShulkerBox
        state.inventory.contents = e.inventory.contents
        meta.blockState = state
        item.itemMeta = meta
        player.equipment!!.setItemInMainHand(item.formatNBT())
        player.playSound(player.location, Sound.BLOCK_SHULKER_BOX_CLOSE, 1f, 1f)
    }

    @SubscribeEvent
    fun e(e: PlayerQuitEvent) {
        baffle.reset(e.player.uniqueId.toString())
    }

    fun ItemStack.formatNBT(): ItemStack {
        val item = this.clone()
        val tag = this.getItemTag()
        if (!tag.containsKey("BlockEntityTag")) return item
        val compound = tag.getDeep("BlockEntityTag").asCompound()
        compound.remove("x")
        compound.remove("y")
        compound.remove("z")
        compound.remove("Lock")
        compound.remove("id")
        tag["BlockEntityTag"] = compound
        return item.setItemTag(tag)
    }
}