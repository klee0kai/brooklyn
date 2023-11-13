package com.github.klee0kai.brooklyn


/**
 * Available from jni
 */
@MustBeDocumented
@Target(
    allowedTargets = [AnnotationTarget.CLASS]
)
@Retention(value = AnnotationRetention.SOURCE)
annotation class JniMirror

