#pragma once

#include "connectionHandler.h"

class ServerReadTask {
public:
    explicit ServerReadTask(ConnectionHandler& connection_handler);
    void run();

private:
    short _read_opcode();
    static bool _contains_optional(short message_opcode);

private:
    ConnectionHandler& _connection_handler;
};


