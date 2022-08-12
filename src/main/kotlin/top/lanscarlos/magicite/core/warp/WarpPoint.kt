package top.lanscarlos.magicite.core.warp

import org.bukkit.Location

/**
 * MagiciteCore
 * top.lanscarlos.magicite.core.warp
 *
 * @author Lanscarlos
 * @since 2022-07-24 14:42
 */
class WarpPoint(
    val id: String,
    val server: String,
    val loc: Location
) {
    override fun toString(): String {
        return "WarpPoint(id='$id', server='$server', loc=$loc)"
    }
}