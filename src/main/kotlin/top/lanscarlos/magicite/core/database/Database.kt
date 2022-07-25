package top.lanscarlos.magicite.core.database

import org.bukkit.Bukkit
import org.bukkit.Location
import taboolib.common5.Demand
import taboolib.module.configuration.Configuration
import taboolib.module.database.getHost
import top.lanscarlos.magicite.core.MagiciteCore
import top.lanscarlos.magicite.core.asDouble
import top.lanscarlos.magicite.core.asFloat
import top.lanscarlos.magicite.core.warp.WarpHandler
import top.lanscarlos.magicite.core.warp.WarpPoint
import java.io.File
import java.lang.StringBuilder
import javax.sql.DataSource

/**
 * MagiciteCore
 * top.lanscarlos.magicite.core.database
 *
 * @author Lanscarlos
 * @since 2022-07-16 17:00
 */
object Database {

    private lateinit var type: Type
    private lateinit var dataSource: DataSource

    fun createWarp(name: String, server: String, loc: Location): Boolean {
        return try {
            type.table().insert(dataSource, "name", "server", "world", "x", "y", "z", "yaw", "pitch") {
                value(name, server, loc.world?.name ?: "world", loc.x, loc.y, loc.z, loc.yaw, loc.pitch)
            }
            true
        } catch (e: Exception) {
            false
        }
    }

    fun getWarp(name: String): WarpPoint? {
        return type.table().select(dataSource) {
            rows("name", "server", "world", "x", "y", "z", "yaw", "pitch")
            where("name" eq name)
        }.firstOrNull {
            WarpPoint(
                name,
                getString("server"),
                Location(
                    Bukkit.getWorld(getString("world")),
                    getDouble("x"),
                    getDouble("y"),
                    getDouble("z"),
                    getFloat("yaw"),
                    getFloat("pitch")
                )
            )
        }
    }

    fun deleteWarp(name: String): Boolean {
        return type.table().delete(dataSource) {
            where("name" eq name)
        } > 0
    }

    fun setup(config: Configuration) {
        if (::dataSource.isInitialized) return
        val prefix = config.getString("database-setting.table-prefix") ?: "magicite_core"
        type = if (config.getBoolean("database-setting.enable")) {
            TypeMySQL(config.getHost("database-setting"), prefix)
        } else {
            TypeSQLite(File(MagiciteCore.plugin.dataFolder, "data.db"), prefix)
        }
        dataSource = type.host().createDataSource()
        type.table().createTable(dataSource)
    }
}