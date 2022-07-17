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
                        val amount = arg.toInt()
                        var target = arg.toInt()
                        var max = player.expToLevel
                        var exp = (player.exp * max).toInt()
                        while (target > 0) {
                            if (exp > target) {
                                exp -= target
                                player.exp = exp.toFloat() / max
                            } else if (player.level <= 0) {
                                player.exp = 0f
                                break
                            } else {
                                player.level -= 1
                                max = player.expToLevel
                                exp = max
                                target -= exp
                            }
                        }
                        player.totalExperience -= amount
                        sender.sendMessage("&7[&3MagiciteCore&7] &7成功扣除玩家 &b${player.name} &7的经验 &c-$arg".colored())
                        sender.sendMessage("&7[&3MagiciteCore&7] &7玩家 &b${player.name} &7经验剩余 &b${exp}".colored())
                    }
                }
            }
        }
    }
}