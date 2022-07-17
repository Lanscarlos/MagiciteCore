package top.lanscarlos.magicite.core.database

import taboolib.module.database.ColumnTypeSQL
import taboolib.module.database.Host
import taboolib.module.database.HostSQL
import taboolib.module.database.Table

/**
 * MagiciteCore
 * top.lanscarlos.magicite.core.database
 *
 * @author Lanscarlos
 * @since 2022-07-16 17:04
 */
class TypeMySQL(private val host: HostSQL, prefix: String) : Type {

    private val table = Table("${prefix}_warp", host) {
        add { id() }
        add("name") {
            type(ColumnTypeSQL.VARCHAR, 36)
        }
        add("server") {
            type(ColumnTypeSQL.VARCHAR, 36)
        }
        add("location") {
            type(ColumnTypeSQL.VARCHAR, 36)
        }

    }

    override fun host(): Host<*> = host
    override fun table(): Table<*, *> = table
}