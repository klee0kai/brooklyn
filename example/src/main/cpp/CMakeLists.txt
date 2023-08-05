cmake_minimum_required(VERSION 3.18.1)

project(native_lib)
include(../../../build/generated/sources/brooklyn/main/FindBrooklynBridge.cmake)


find_package(JNI REQUIRED)

if (JNI_FOUND)
    message(STATUS "JNI_INCLUDE_DIRS=${JNI_INCLUDE_DIRS}")
    message(STATUS "JNI_LIBRARIES=${JNI_LIBRARIES}")
endif ()



add_library(native_lib
        SHARED
        native-lib.cpp)

target_link_libraries(
        native_lib
        brooklyn
        ${JNI_LIBRARIES}
)

include_directories(${JNI_INCLUDE_DIRS})

