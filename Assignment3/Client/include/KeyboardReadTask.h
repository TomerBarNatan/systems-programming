#pragma once

#include "connectionHandler.h"
#include <map>


class KeyboardReadTask {
public:
    explicit KeyboardReadTask(ConnectionHandler& connection_handler);
    void run();

private:
    std::map<std::string, short> _command_to_opcode;
    ConnectionHandler& _connection_handler;


private:
    static std::map<std::string, short> _initialize_map();
    static std::vector<std::string> _split_string_to_commands(std::string input);

    void _send_short(short number);
    void act_with_opcode(short opcode, std::vector<std::string> commands);
};


