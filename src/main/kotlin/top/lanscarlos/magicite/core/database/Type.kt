package top.lanscarlos.magicite.core.database

import taboolib.module.database.Host
import taboolib.module.database.Table

/**
 * MagiciteCore
 * top.lanscarlos.magicite.core.database
 *
 * @author Lanscarlos
 * @since 2022-07-16 17:01
 */
interface Type {
    fun host(): Host<*>
    fun table(): Table<*, *>
}