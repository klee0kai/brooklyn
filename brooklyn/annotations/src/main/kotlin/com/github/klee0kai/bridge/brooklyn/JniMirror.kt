package com.github.klee0kai.bridge.brooklyn


/**
 * Available from jni
 */
@MustBeDocumented
@Target(
    allowedTargets = [AnnotationTarget.CLASS]
)
@Retention(value = AnnotationRetention.SOURCE)
annotation class JniMirror

