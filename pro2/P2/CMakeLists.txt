#PLEASE, DO NOT CHANGE THIS FILE
cmake_minimum_required(VERSION 3.5)
project(P2 C)
set(CMAKE_C_STANDARD 99)

include(config.cmake)

set(CMAKE_VERBOSE_MAKEFILE true)
add_definitions(-DINPUT_FILE="${input}")
set(dependencies types.h party_list.h party_list.c center_list.h center_list.c)
add_executable(main main.c ${dependencies})
target_compile_options(main PRIVATE -Wall -Wextra)
set_target_properties(main PROPERTIES RUNTIME_OUTPUT_DIRECTORY ../bin)


