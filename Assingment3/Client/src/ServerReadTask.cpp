#include "ServerReadTask.h"

enum command {
    LOGOUT = 4,
    KDAMCHECK = 6,
    COURSESTAT = 7,
    STUDENTSTAT = 8,
    ISREGISTERED = 9,
    MYCOURSES = 11,
    ACK = 12,
    ERROR = 13
};

ServerReadTask::ServerReadTask(ConnectionHandler &connection_handler) :
        _connection_handler(connection_handler) {}

void ServerReadTask::run()
{
    while (!_connection_handler.terminate())
    {
        short server_opcode = _read_opcode();
        short message_opcode = _read_opcode();

        if (server_opcode == ERROR)
        {
            if (message_opcode == LOGOUT){
                _connection_handler.set_try_to_logout(false);
            }
            std::cout << "ERROR " << message_opcode << std::endl;
        }
        else //server_opcode == ACK
        {
            std::cout << "ACK " << message_opcode << std::endl;
            if (message_opcode == LOGOUT)
            {
                _connection_handler.close();
                break;
            }
            else if (_contains_optional(message_opcode))
            {
                std::string answer;
                _connection_handler.getLine(answer);
                std::cout << answer << std::endl;
            }
        }
    }
}

short ServerReadTask::_read_opcode()
{
    char *byte_array = new char[2];
    short result;
    _connection_handler.getBytes(byte_array, 2);
    result = (short) ((byte_array[0] & 0xff) << 8);
    result += (short) (byte_array[1] & 0xff);
    delete[] byte_array;
    return result;
}

bool ServerReadTask::_contains_optional(short message_opcode)
{
    return (message_opcode >= KDAMCHECK && message_opcode <=ISREGISTERED) || message_opcode == MYCOURSES;
}
