package com.github.klee0kai.brooklyn.model

import kotlinx.serialization.Serializable

const val PROJECT_FINGERPRINT_VERSION = 1

@Serializable
data class ProjectFingerPrint(
    val version: Int = PROJECT_FINGERPRINT_VERSION,
    val supportPojoClasses: List<SupportClassMirror> = emptyList(),
    val supportMirrorClasses: List<SupportClassMirror> = emptyList(),
)


