package top.lanscarlos.magicite.core.database

import taboolib.module.configuration.Configuration
import taboolib.module.database.getHost
import top.lanscarlos.magicite.core.MagiciteCore
import java.io.File
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

//    fun createWarp(name: String, server: String, loc: Location) {
//        type.table().insert(dataSource, "name", "server", "location") {
//            value(name, server, loc.encode())
//        }
//    }

//    fun getWarp(name: String): WarpHandler.WarpPoint? {
//        return type.table().select(dataSource) {
//            rows("name", "server", "location")
//            where("name" eq name)
//        }.firstOrNull {
//            WarpHandler.WarpPoint(
//                name,
//                getString("server"),
//                getString("location").decodeToLocation()
//            )
//        }
//    }

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