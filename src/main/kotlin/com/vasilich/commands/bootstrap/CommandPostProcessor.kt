package com.vasilich.commands

import org.springframework.beans.factory.config.BeanPostProcessor
import org.slf4j.LoggerFactory
import com.vasilich.config.CommandConfigResolver

public class CommandPostProcessor (private val cfgProvider: CommandConfigResolver,
                                   private val wrapper: (Command, CommandCfg) -> Command): BeanPostProcessor {

    val logger = LoggerFactory.getLogger(javaClass<CommandPostProcessor>())!!;

    override fun postProcessBeforeInitialization(bean: Any?, beanName: String?): Any? {
        return bean
    }
    override fun postProcessAfterInitialization(bean: Any?, beanName: String?): Any? {
        return when(bean) {
            is Command -> {
                return init(bean)
            }
            else -> bean
        }
    }

    private fun init(bean: Command): Command {
        val cfg = cfgProvider.config(bean.javaClass.getSimpleName().toLowerCase().trimTrailing("command"))
        return if(cfg == null) NoopCommand else wrapper(bean, cfg)
    }
}
