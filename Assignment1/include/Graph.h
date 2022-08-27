#pragma once

#include <vector>

enum NODE_STATE: int {
    NOT_INFECTED = 0,
    INFECTED = 1
};

enum CONNECTIVITY: int {
    NOT_CONNECTED = 0,
    CONNECTED = 1
};

class Graph{
public:
    Graph(std::vector<std::vector<int>> matrix);
    
    void infectNode(int nodeInd);
    bool isInfected(int nodeInd);

    int size();
    std::vector<std::vector<int>> get_edges() const;
    bool is_adjacent_nodes(int first_node_index, int second_node_index) const;
    void disconnect_node(int node_index);

private:
    std::vector<std::vector<int>> edges;
    std::vector<int> _infected_histogram;
};