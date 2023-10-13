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
        native-lib.cpp
        ${BROOKLYN_SRC}
)

target_link_libraries(
        native_lib
        ${JNI_LIBRARIES}
)

target_include_directories(
        native_lib
        PUBLIC
        ${JNI_INCLUDE_DIRS}
        ${BROOKLYN_INCLUDE_DIRS}
        ./../../../build/generated/sources/headers/java/main
)


add_executable(
        term
        native-lib.cpp
        term.cpp
        ${BROOKLYN_SRC}
)


target_link_libraries(term
        ${JNI_LIBRARIES}
)

target_include_directories(
        term
        PUBLIC
        ${JNI_INCLUDE_DIRS}
        ${BROOKLYN_INCLUDE_DIRS}
        ./../../../build/generated/sources/headers/java/main
)


include_directories(${JNI_INCLUDE_DIRS})

