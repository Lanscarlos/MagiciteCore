package top.lanscarlos.magicite.core

import com.google.common.io.ByteStreams
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.plugin.messaging.PluginMessageListener
import taboolib.common.platform.Plugin
import taboolib.common.platform.function.console
import taboolib.common.platform.function.info
import taboolib.common.platform.function.submit
import taboolib.common5.Demand
import taboolib.module.configuration.Config
import taboolib.module.configuration.Configuration
import taboolib.platform.BukkitPlugin
import top.lanscarlos.magicite.core.database.Database

object MagiciteCore : Plugin() {

    val plugin by lazy {
        BukkitPlugin.getInstance()
    }

//    @Config("config.yml")
//    lateinit var config: Configuration
//        private set

    override fun onEnable() {
//        Database.setup(config)
        info("Successfully running ExamplePlugin!")
    }
}