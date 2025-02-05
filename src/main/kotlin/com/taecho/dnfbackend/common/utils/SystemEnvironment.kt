package com.taecho.dnfbackend.common.utils

object SystemEnvironment {
    private val SYSTEM_OS = System.getProperty("os.name").lowercase()
    fun isWindows(): Boolean = SYSTEM_OS.contains("win")
    fun isLinux(): Boolean = SYSTEM_OS.contains("linux")
}
