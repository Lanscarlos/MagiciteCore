package top.lanscarlos.magicite.core.warp

import com.google.common.io.ByteStreams
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerJoinEvent
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common.platform.function.info
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
        info("正在尝试构建玩家 $name 的跨服信息...")
        val point = Database.getWarp(id) ?: return false
        info("跨服坐标查询成功 -> $point")
        if (point.server == MagiciteCore.serverName) {
            info("检测到跨服坐标在本服，直接传送...")
            teleport(point.loc)
            return true
        }
        val output = ByteStreams.newDataOutput().also {
            it.writeUTF("magicite:warp")
            it.writeUTF(point.server)
            it.writeUTF(this.uniqueId.toString())
            it.writeUTF(point.id)
        }.toByteArray()
        info("跨服信息构建完毕 -> ${String(output)}")
        this.sendPluginMessage(MagiciteCore.plugin, "BungeeCord", output)
        info("已向 BungeeCord 发送跨服信息")
        return true
    }

    fun postWarp(uuid: String, id: String): Boolean {
        val point = Database.getWarp(id) ?: error("Cannot find warp: \"$id\"!")
        info("成功解析跨服坐标 $point")
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
        submit(delay = 10) {
            if (e.player.uniqueId.toString() !in cache) return@submit
            info("检测到目标玩家 ${e.player.name} 进入服务器...")
            val loc = cache.remove(e.player.uniqueId.toString())!!
            info("正在尝试将玩家 ${e.player.name} 传送至坐标 $loc")
            e.player.teleport(loc)
        }
    }
}