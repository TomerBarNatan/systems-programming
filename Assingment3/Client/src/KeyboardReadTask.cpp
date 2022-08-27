#include <iostream>
#include <stdlib.h>
#include <vector>
#include "KeyboardReadTask.h"

enum command {
    ADMINREG = 1,
    STUDENTREG = 2,
    LOGIN = 3,
    LOGOUT = 4,
    COURSEREG = 5,
    KDAMCHECK = 6,
    COURSESTAT = 7,
    STUDENTSTAT = 8,
    ISREGISTERED = 9,
    UNREGISTER = 10,
    MYCOURSES = 11
};

KeyboardReadTask::KeyboardReadTask(ConnectionHandler& connection_handler):
    _command_to_opcode(_initialize_map()),
    _connection_handler(connection_handler)
{}


void KeyboardReadTask::run()
{
    const short bufsize = 1024;
    char buf[bufsize];
    while(!_connection_handler.terminate())
    {
        if(_connection_handler.tried_to_logout())
        {
            continue;
        }
        std::cin.getline(buf, bufsize);
        std::string line(buf);

        std::vector<std::string> commands = _split_string_to_commands(std::move(line));
        auto opcode = _command_to_opcode.at(commands.at(0));
        _send_short(opcode);
        act_with_opcode(opcode, std::move(commands));
    }
}

std::map<std::string, short> KeyboardReadTask::_initialize_map()
{
    return {
            {"ADMINREG", 1},
            {"STUDENTREG", 2},
            {"LOGIN", 3},
            {"LOGOUT", 4},
            {"COURSEREG", 5},
            {"KDAMCHECK", 6},
            {"COURSESTAT", 7},
            {"STUDENTSTAT", 8},
            {"ISREGISTERED", 9},
            {"UNREGISTER", 10},
            {"MYCOURSES", 11}
    };
}


std::vector<std::string> KeyboardReadTask::_split_string_to_commands(std::string input)
{
    std::vector<std::string> commands;
    std::string delimiter = " ";

    size_t pos = 0;
    std::string token;
    while ((pos = input.find(delimiter)) != std::string::npos) {
        token = input.substr(0, pos);
        commands.push_back(token);
        input.erase(0, pos + delimiter.length());
    }
    if(input.length() != 0){
        commands.push_back(input);
    }
    return commands;
}


void KeyboardReadTask::_send_short(short number)
{
    char* bytes_array = new char[2];
    bytes_array[0] = ((number >> 8) & 0xFF);
    bytes_array[1] = (number & 0xFF);
    _connection_handler.sendBytes(bytes_array, 2);
    delete[] bytes_array;
}

void KeyboardReadTask::act_with_opcode(short opcode, std::vector<std::string> commands)
{
    switch (opcode) {
        case ADMINREG:
        case STUDENTREG:
        case LOGIN: {
            _connection_handler.sendLine(commands.at(1));
            _connection_handler.sendLine(commands.at(2));
            break;
        }
        case MYCOURSES: {
            break;
        }
        case LOGOUT: {
            _connection_handler.set_try_to_logout(true);
            break;
        }
        case COURSEREG:
        case KDAMCHECK:
        case COURSESTAT:
        case ISREGISTERED:
        case UNREGISTER: {
            short course_number = std::atoi(commands.at(1).c_str());
            _send_short(course_number);
            break;
        }
        case STUDENTSTAT: {
            _connection_handler.sendLine(commands.at(1));
            break;
        }
        default:
            break;
    }
}
