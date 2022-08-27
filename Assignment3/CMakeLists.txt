cmake_minimum_required(VERSION 3.10)
project(spl_assingment3)

set(CMAKE_CXX_STANDARD 11)

include_directories(.)
include_directories(Client)
include_directories(Client/include)

add_definitions(-DBOOST_ERROR_CODE_HEADER_ONLY)
add_definitions(-DBOOST_SYSTEM_NO_DEPRECATED)
SET(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -pthread")
add_executable(spl_assingment3
        Client/src/connectionHandler.cpp
        Client/src/BGRSclient.cpp Client/src/KeyboardReadTask.cpp Client/src/ServerReadTask.cpp)
