package top.lanscarlos.magicite.core

/**
 * MagiciteCore
 * top.lanscarlos.magicite.core
 *
 * @author Lanscarlos
 * @since 2022-07-16 16:15
 */
object WarpHandler {

//    class WarpPoint(
//        val name: String,
//        val server: String,
//        val loc: Location
//    )
//
//    val cache = mutableMapOf<String, Location>()
//
//    fun Player.warp(point: WarpPoint) {
//        val output = ByteStreams.newDataOutput()
//        output.writeUTF("Connect")
//        output.writeUTF(point.server)
//        this.sendPluginMessage(MagiciteCore.plugin, "BungeeCord", output.toByteArray())
//        this.sendPluginMessage(MagiciteCore.plugin, "BungeeCord", ByteStreams.newDataOutput().also {
//            it.writeUTF("MagiciteCoreWarp")
//            it.writeUTF(this.uniqueId.toString())
//            it.writeUTF(point.loc.encode())
//        }.toByteArray())
//    }
//
//    fun createWarp(name: String, loc: Location) {
//        Database.createWarp(name, MagiciteCore.serverName, loc)
//    }
//
//    fun deleteWarp(name: String): Boolean {
//        return Database.deleteWarp(name)
//    }
//
//    @SubscribeEvent
//    fun e(e: PlayerJoinEvent) {
//        submit(delay = 1) { MagiciteCore.requestServerName() }
//        val uuid = e.player.uniqueId.toString()
//        if (uuid !in cache) return
//        val loc = cache.remove(uuid)!!
//        e.player.teleport(loc)
//    }
}