#include "Graph.h"

Graph::Graph(std::vector<std::vector<int>> matrix):
    edges(matrix),
    _infected_histogram(std::vector<int>(matrix.size()))
{}

void Graph::infectNode(int nodeInd)
{
    _infected_histogram.at(nodeInd) = INFECTED;
}

bool Graph::isInfected(int nodeInd)
{
    return _infected_histogram.at(nodeInd) == INFECTED;
}

int Graph::size()
{
    return edges.size();
}

std::vector<std::vector<int>> Graph::get_edges() const
{
    return edges;
}

bool Graph::is_adjacent_nodes(int first_node_index, int second_node_index) const
{
    return edges.at(first_node_index).at(second_node_index) == CONNECTED;
}

void Graph::disconnect_node(int node_index)
{
    for(int i = 0; i < size(); i++)
    {
        edges.at(i).at(node_index) = NOT_CONNECTED;
        edges.at(node_index).at(i) = NOT_CONNECTED;
    }
}