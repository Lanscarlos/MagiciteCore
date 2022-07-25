package top.lanscarlos.magicite.core.database

import taboolib.module.database.*

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
            type(ColumnTypeSQL.VARCHAR, 36) {
                options(ColumnOptionSQL.UNIQUE_KEY)
            }
        }
        add("server") {
            type(ColumnTypeSQL.VARCHAR, 36)
        }
        add("world") {
            type(ColumnTypeSQL.VARCHAR, 36)
        }
        add("x") {
            type(ColumnTypeSQL.DECIMAL, 16, 2)
        }
        add("y") {
            type(ColumnTypeSQL.DECIMAL, 16, 2)
        }
        add("z") {
            type(ColumnTypeSQL.DECIMAL, 16, 2)
        }
        add("yaw") {
            type(ColumnTypeSQL.DECIMAL, 16, 2)
        }
        add("pitch") {
            type(ColumnTypeSQL.DECIMAL, 16, 2)
        }

    }

    override fun host(): Host<*> = host
    override fun table(): Table<*, *> = table
}