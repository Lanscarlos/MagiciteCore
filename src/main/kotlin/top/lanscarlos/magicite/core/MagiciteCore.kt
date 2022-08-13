package top.lanscarlos.magicite.core

import com.google.common.io.ByteStreams
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.plugin.messaging.PluginMessageListener
import taboolib.common.platform.Plugin
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common.platform.function.info
import taboolib.common.platform.function.submit
import taboolib.module.configuration.Config
import taboolib.module.configuration.Configuration
import taboolib.platform.BukkitPlugin
import top.lanscarlos.magicite.core.database.Database
import top.lanscarlos.magicite.core.warp.WarpHandler.postWarp

object MagiciteCore : Plugin(), PluginMessageListener {

    val plugin by lazy {
        BukkitPlugin.getInstance()
    }

    lateinit var serverName: String

    @Config("config.yml")
    lateinit var config: Configuration
        private set

    override fun onEnable() {
        Database.setup(config)
        plugin.server.messenger.registerOutgoingPluginChannel(plugin, "BungeeCord")
        plugin.server.messenger.registerIncomingPluginChannel(plugin, "BungeeCord", this)
        info("Successfully running MagiciteCore!")
    }

    fun requestServerName(player: Player) {
        val output = ByteStreams.newDataOutput()
        output.writeUTF("GetServer")
        player.sendPluginMessage(plugin, "BungeeCord", output.toByteArray())
    }

    fun bungeeCommand(cmd: String) {
        val output = ByteStreams.newDataOutput()
        output.writeUTF("magicite:cmd")
        output.writeUTF(cmd)
        Bukkit.getOnlinePlayers().firstOrNull()?.sendPluginMessage(plugin, "BungeeCord", output.toByteArray()) ?: error("Cannot find any player！")
    }

    override fun onPluginMessageReceived(channel: String, player: Player, message: ByteArray) {
        if (channel != "BungeeCord") return
        val input = ByteStreams.newDataInput(message)
        val sub = input.readUTF()
        info("检测到来自 BungeeCord 端的信息 -> $sub")
        when (sub) {
            "GetServer" -> {
                serverName = input.readUTF()
            }
            "magicite:cmd" -> {
                val cmd = input.readUTF()
                info("正在执行跨服命令: $cmd")
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd)
            }
            "magicite:warp" -> {
                val uuid = input.readUTF()
                val id = input.readUTF()
                info("检测到来自 BungeeCord 端的跨服信息 {id=$id,uuid=$uuid}")
                postWarp(uuid, id)
            }
        }
    }

    @SubscribeEvent
    fun e(e: PlayerJoinEvent) {
        if (::serverName.isInitialized) return
        submit(delay = 5) { requestServerName(e.player) }
    }
}