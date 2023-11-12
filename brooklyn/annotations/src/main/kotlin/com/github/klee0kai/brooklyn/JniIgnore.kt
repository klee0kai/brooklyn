package com.github.klee0kai.brooklyn

/**
 * serializable class
 */
@MustBeDocumented
@Target(
    allowedTargets = [
        AnnotationTarget.PROPERTY,
        AnnotationTarget.FUNCTION,
        AnnotationTarget.CONSTRUCTOR,
    ]
)
@Retention(value = AnnotationRetention.SOURCE)
annotation class JniIgnore

