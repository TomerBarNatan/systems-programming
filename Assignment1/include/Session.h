#pragma once

#include <queue>
#include <vector>

#include "Graph.h"
#include "json.hpp"

class Agent;

using json = nlohmann::json;

enum TreeType{
  Cycle,
  MaxRank,
  Root,
};

class Session
{
public:
    Session(const std::string& path);
    ~Session();

    Session(const Session& other);
    Session& operator=(const Session& other);

    Session(Session&& other);
    Session& operator=(Session&& other);

    void swap(Session& other);

    void simulate();
    void addAgent(const Agent& agent);
    void setGraph(const Graph& graph);
    void enqueueInfected(int);
    int dequeueInfected();
    TreeType getTreeType() const;

    Graph graph();
    bool is_node_with_virus(int node_index);
    bool is_empty_infected_queue();
    void set_virus_on_node_histogram(int node_index);
    int cycle_num() const;

private:
    static json _read_json(const std::string& path);
    static TreeType _initialize_tree_type(json json_file);

private:
    std::vector<Agent*> _initialize_agents(json json_file);
    bool check_ending_condition();
    void _write_json();
    void delete_agents();

private:
    std::queue<int> _infected_queue;
    Graph g;
    std::vector<int> _viruses_on_nodes_histogram;
    TreeType treeType;
    std::vector<Agent*> agents;
    int _cycle_num;
};