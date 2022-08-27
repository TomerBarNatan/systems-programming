#include <stdlib.h>
#include <boost/thread.hpp>
#include <thread>
#include <connectionHandler.h>

#include "ServerReadTask.h"
#include "KeyboardReadTask.h"

/**
* This code assumes that the server replies the exact text the client sent it (as opposed to the practical session example)
*/
int main (int argc, char *argv[]) {
    if (argc < 3) {
        std::cerr << "Usage: " << argv[0] << " host port" << std::endl << std::endl;
        return -1;
    }
    std::string host = argv[1];
    short port = atoi(argv[2]);
    
    ConnectionHandler connectionHandler(host, port);
    if (!connectionHandler.connect()) {
        std::cerr << "Cannot connect to " << host << ":" << port << std::endl;
        return 1;
    }
    try
    {
        KeyboardReadTask k(connectionHandler);
        std::thread t1(&KeyboardReadTask::run, &k);

        ServerReadTask s(connectionHandler);
        std::thread t2(&ServerReadTask::run, &s);
        t1.join();
        t2.join();
    }
    catch (std::runtime_error& e)
    {
        std::cout << e.what() << std::endl;
    }
    return 0;
}
