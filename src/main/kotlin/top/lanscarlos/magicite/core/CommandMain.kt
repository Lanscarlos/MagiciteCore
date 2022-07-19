package top.lanscarlos.magicite.core

import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import taboolib.common.platform.command.CommandBody
import taboolib.common.platform.command.CommandHeader
import taboolib.common.platform.command.subCommand
import taboolib.common.platform.function.info
import taboolib.module.chat.colored

/**
 * MagiciteCore
 * top.lanscarlos.magicite.core
 *
 * @author Lanscarlos
 * @since 2022-07-16 15:45
 */
@CommandHeader(name = "magicitecore", aliases = ["magicite"], permission = "sacredlore.command")
object CommandMain {

    fun getExpAtLevel(level: Int): Int {
        if (level <= 15) {
            return (2 * level) + 7
        }
        if ((level >= 16) && (level <= 30)) {
            return (5 * level) - 38
        }
        return (9 * level) - 158
    }

    @CommandBody
    val exp = subCommand {
        literal("take") {
            dynamic(commit = "player") {
                suggestion<CommandSender> { _, _ ->
                    Bukkit.getOnlinePlayers().map { it.name }
                }
                dynamic(commit = "amount") {
                    execute<CommandSender> { sender, context, arg ->
                        val name = context.argument(-1)
                        val player = Bukkit.getPlayerExact(name) ?: error("Player \"$name\" is not exist!")
                        var target = arg.toInt()
                        var max = getExpAtLevel(player.level)
                        var exp = (player.exp * max).toInt()
                        while (target > 0) {
                            if (player.level <= 0) {
                                exp = 0
                                break
                            } else if (exp >= target) {
                                exp -= target
                                break
                            } else {
                                target -= exp
                                player.level -= 1
                                max = getExpAtLevel(player.level)
                                exp = max
                            }
                        }
                        player.exp = exp.toFloat() / max
                        sender.sendMessage("&7[&3MagiciteCore&7] &7成功扣除玩家 &b${player.name} &7的经验 &c-$arg".colored())
                        sender.sendMessage("&7[&3MagiciteCore&7] &7玩家 &b${player.name} &7当前等级 &b${player.level} &7经验剩余 &b${exp}".colored())
                    }
                }
            }
        }
    }
}