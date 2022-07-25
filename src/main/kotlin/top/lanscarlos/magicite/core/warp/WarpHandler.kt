package top.lanscarlos.magicite.core.warp

import com.google.common.io.ByteStreams
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerJoinEvent
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common.platform.function.submit
import top.lanscarlos.magicite.core.MagiciteCore
import top.lanscarlos.magicite.core.database.Database

/**
 * MagiciteCore
 * top.lanscarlos.magicite.core
 *
 * @author Lanscarlos
 * @since 2022-07-16 16:15
 */
object WarpHandler {

    private val cache = mutableMapOf<String, Location>()

    fun Player.preWarp(id: String): Boolean {
        val point = Database.getWarp(id) ?: return false
        if (point.server == MagiciteCore.serverName) {
            teleport(point.loc)
            return true
        }
        val output = ByteStreams.newDataOutput().also {
            it.writeUTF("magicite:warp")
            it.writeUTF(point.server)
            it.writeUTF(this.uniqueId.toString())
            it.writeUTF(point.id)
        }
        this.sendPluginMessage(MagiciteCore.plugin, "BungeeCord", output.toByteArray())
        return true
    }

    fun postWarp(uuid: String, id: String): Boolean {
        val point = Database.getWarp(id) ?: error("Cannot find warp: \"$id\"!")
        cache[uuid] = point.loc
        return true
    }

    fun createWarp(name: String, loc: Location): Boolean {
        return Database.createWarp(name, MagiciteCore.serverName, loc)
    }

    fun deleteWarp(name: String): Boolean {
        return Database.deleteWarp(name)
    }

    @SubscribeEvent
    fun e(e: PlayerJoinEvent) {
        submit(delay = 5) {
            if (e.player.uniqueId.toString() !in cache) return@submit
            val loc = cache.remove(e.player.uniqueId.toString())!!
            e.player.teleport(loc)
        }
    }
}