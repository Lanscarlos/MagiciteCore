package top.lanscarlos.magicite.core.database

import taboolib.common.io.newFile
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
            type(taboolib.module.database.ColumnTypeSQLite.TEXT, 36)
        }
        add("server") {
            type(taboolib.module.database.ColumnTypeSQLite.TEXT, 36)
        }
        add("location") {
            type(taboolib.module.database.ColumnTypeSQLite.TEXT, 36)
        }
    }

    override fun host(): Host<*> = host
    override fun table(): Table<*, *> = table
}