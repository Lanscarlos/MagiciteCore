package top.lanscarlos.magicite.core.database

import taboolib.common.io.newFile
import taboolib.module.database.ColumnOptionSQLite
import taboolib.module.database.Host
import taboolib.module.database.Table
import taboolib.module.database.getHost
import java.io.File

/**
 * MagiciteCore
 * top.lanscarlos.magicite.core.database
 *
 * @author Lanscarlos
 * @since 2022-07-16 17:03
 */
class TypeSQLite(file: File, prefix: String) : Type {
    private val host = newFile(file).getHost()
    private val table = Table("${prefix}_warp", host) {
        add { id() }
        add("name") {
            type(taboolib.module.database.ColumnTypeSQLite.TEXT, 36) {
                options(ColumnOptionSQLite.UNIQUE)
            }
        }
        add("server") {
            type(taboolib.module.database.ColumnTypeSQLite.TEXT, 36)
        }
        add("world") {
            type(taboolib.module.database.ColumnTypeSQLite.TEXT, 36)
        }
        add("x") {
            type(taboolib.module.database.ColumnTypeSQLite.REAL, 36)
        }
        add("y") {
            type(taboolib.module.database.ColumnTypeSQLite.REAL, 36)
        }
        add("z") {
            type(taboolib.module.database.ColumnTypeSQLite.REAL, 36)
        }
        add("yaw") {
            type(taboolib.module.database.ColumnTypeSQLite.REAL, 36)
        }
        add("pitch") {
            type(taboolib.module.database.ColumnTypeSQLite.REAL, 36)
        }
    }

    override fun host(): Host<*> = host
    override fun table(): Table<*, *> = table
}