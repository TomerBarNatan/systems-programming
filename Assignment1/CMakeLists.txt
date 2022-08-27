cmake_minimum_required(VERSION 3.2)
project(cTrace)

set(CMAKE_CXX_STANDARD 11)

add_compile_options(-Wall -Wextra -pedantic -Werror)

add_executable(ctrace
        src/main.cpp
        src/Session.cpp
        src/Graph.cpp
        src/Agent.cpp
        src/Tree.cpp
        src/CycleTree.cpp
        src/MaxRankTree.cpp
        src/RootTree.cpp
        src/Virus.cpp
        src/ContactTracer.cpp
        )


target_include_directories(ctrace
        PRIVATE
        include)
