package top.lanscarlos.magicite.core.command

import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import taboolib.common.platform.command.CommandBody
import taboolib.common.platform.command.CommandHeader
import taboolib.common.platform.command.subCommand
import taboolib.module.chat.colored
import top.lanscarlos.magicite.core.MagiciteCore
import top.lanscarlos.magicite.core.toInt
import top.lanscarlos.magicite.core.warp.WarpHandler
import top.lanscarlos.magicite.core.warp.WarpHandler.preWarp

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
    val cmd = subCommand {
        dynamic {
            execute<CommandSender> { sender, _, cmd ->
                MagiciteCore.bungeeCommand(cmd)
                sender.sendMessage("&7[&3MagiciteCore&7] &7正在执行跨服命令: &b${cmd} &7!".colored())
            }
        }
    }

    @CommandBody
    val setwarp = subCommand {
        dynamic {
            execute<Player> { sender, _, id ->
                val loc = sender.location
                if (WarpHandler.createWarp(id, loc)) {
                    sender.sendMessage("&7[&3MagiciteCore&7] &7成功创建地标 &b${id} &7!".colored())
                } else {
                    sender.sendMessage("&7[&3MagiciteCore&7] &7创建地标 &b${id} &7失败！请查看控制台报错！".colored())
                }
            }
        }
    }

    @CommandBody
    val delwarp = subCommand {
        dynamic {
            execute<CommandSender> { sender, _, id ->
                if (WarpHandler.deleteWarp(id)) {
                    sender.sendMessage("&7[&3MagiciteCore&7] &7成功删除地标 &b${id} &7!".colored())
                } else {
                    sender.sendMessage("&7[&3MagiciteCore&7] &7无效的地标 &b${id}".colored())
                }
            }
        }
    }

    @CommandBody
    val warp = subCommand {
        dynamic {
            execute<Player> { sender, _, id ->
                if (sender.preWarp(id)) {
                    sender.sendMessage("&7[&3MagiciteCore&7] &7准备传送至地标 &b${id} &7!".colored())
                } else {
                    sender.sendMessage("&7[&3MagiciteCore&7] &7无效的地标 &b${id}".colored())
                }
            }
        }
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
                        var level = player.level
                        var max = getExpAtLevel(level)
                        var exp = (player.exp * max).toInt()
                        while (target > 0) {
                            if (level <= 0) {
                                exp = 0
                                level = 0
                                max = getExpAtLevel(level)
                                break
                            } else if (exp >= target) {
                                exp -= target
                                break
                            } else {
                                target -= exp
                                level -= 1
                                max = getExpAtLevel(level)
                                exp = max
                            }
                        }
                        player.exp = exp.toFloat() / max
                        player.level = level
                        sender.sendMessage("&7[&3MagiciteCore&7] &7成功扣除玩家 &b${player.name} &7的经验 &c-$arg".colored())
                        sender.sendMessage("&7[&3MagiciteCore&7] &7玩家 &b${player.name} &7当前等级 &b${level} &7经验剩余 &b${exp}".colored())
                    }
                }
            }
        }
    }

    fun getExpAtLevel(level: Int): Int {
        if (level <= 15) {
            return (2 * level) + 7
        }
        if ((level >= 16) && (level <= 30)) {
            return (5 * level) - 38
        }
        return (9 * level) - 158
    }
}