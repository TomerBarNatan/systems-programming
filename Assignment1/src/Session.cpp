#include <fstream>

#include "json.hpp"
#include "Agent.h"
#include "Session.h"

using json = nlohmann::json;

Session::Session(const std::string& path):
    _infected_queue(),
    g(_read_json(path).at("graph")),
    _viruses_on_nodes_histogram(std::vector<int>(g.size())),
    treeType(_initialize_tree_type(_read_json(path))),
    agents(_initialize_agents(_read_json(path))),
    _cycle_num(0)
{}

Session::~Session()
{
    delete_agents();
}

Session::Session(const Session &other):
    _infected_queue(other._infected_queue),
    g(other.g),
    _viruses_on_nodes_histogram(other._viruses_on_nodes_histogram),
    treeType(other.treeType),
    agents(),
    _cycle_num(other._cycle_num)
{
    for(const auto& agent : other.agents)
    {
        Agent* new_agent = agent->clone();
        agents.push_back(new_agent);
    }
}

Session &Session::operator=(const Session &other)
{
    if(this == &other)
    {
        return *this;
    }
    Session tmp(other);
    swap(tmp);

    return *this;
}

Session::Session(Session &&other):
    _infected_queue(std::move(other._infected_queue)),
    g(std::move(other.g)),
    _viruses_on_nodes_histogram(std::move(other._viruses_on_nodes_histogram)),
    treeType(other.treeType),
    agents(std::move(other.agents)),
    _cycle_num(other._cycle_num)
{
    other.agents.clear();
}

Session &Session::operator=(Session &&other)
{
    if(this == &other)
    {
        return *this;
    }
    Session tmp(std::move(other));
    swap(tmp);
    return *this;
}

void Session::swap(Session &other)
{
    std::swap(_infected_queue, other._infected_queue);
    std::swap(g, other.g);
    std::swap(_viruses_on_nodes_histogram, other._viruses_on_nodes_histogram);
    std::swap(treeType, other.treeType);
    std::swap(agents, other.agents);
    std::swap(_cycle_num, other._cycle_num);
}

void Session::simulate()
{
    bool ending_condition = false;
    while (!ending_condition)
    {
        std::vector<Agent*> current_cycle_agents = agents;
        for (const auto& agent : current_cycle_agents)
        {
            agent->act(*this);
        }
        _cycle_num++;
        ending_condition = check_ending_condition();
    }
    _write_json();
}

void Session::addAgent(const Agent& agent)
{
    auto new_agent = const_cast<Agent *>(&agent);
    agents.push_back(new_agent);
}

void Session::setGraph(const Graph& graph)
{
    g = graph;
}

void Session::enqueueInfected(int infected_index)
{
   _infected_queue.push(infected_index);
}

int Session::dequeueInfected()
{
    int next_infected = _infected_queue.front();
    _infected_queue.pop();

    return next_infected;
}

TreeType Session::getTreeType() const
{
    return treeType;
}

Graph Session::graph()
{
    return g;
}

bool Session::is_node_with_virus(int node_index)
{
    return _viruses_on_nodes_histogram.at(node_index) == 1;
}

bool Session::is_empty_infected_queue()
{
    return _infected_queue.empty();
}

void Session::set_virus_on_node_histogram(int node_index)
{
    _viruses_on_nodes_histogram.at(node_index) = 1;
}

int Session::cycle_num() const
{
    return _cycle_num;
}

json Session::_read_json(const std::string &path)
{
    std::ifstream json_stream(path);
    json json_file;
    json_stream >> json_file;
    return json_file;
}

TreeType Session::_initialize_tree_type(json json_file)
{
    std::map<std::string, TreeType> tree_type_map = {
            {"C", Cycle},
            {"M", MaxRank},
            {"R", Root}
    };
    return tree_type_map.at(json_file.at("tree"));
}

std::vector<Agent*> Session::_initialize_agents(json json_file)
{
    std::vector<Agent*> _agents;
    for(const auto & agent: json_file.at("agents"))
    {
        if(agent.at(0) == "V")
        {
            _agents.push_back(new Virus(agent.at(1)));
            _viruses_on_nodes_histogram.at(agent.at(1)) = INFECTED;
        }
        else
        {
            _agents.push_back(new ContactTracer());
        }
    }
    return _agents;
}

bool Session::check_ending_condition()
{
    for(int i = 0; i < g.size(); i++)
    {
        if(_viruses_on_nodes_histogram.at(i) == 1 and !g.isInfected(i))
        {
            return false;
        }

        for(int j = i + 1; j < g.size(); j++)
        {
            if(g.is_adjacent_nodes(i, j) and g.isInfected(i) != g.isInfected(j))
            {
                return false;
            }
        }
    }
    return true;
}

void Session::_write_json()
{
    json json_dump;

    json_dump["graph"] = g.get_edges();
    std::vector<int> infected;
    for(int i = 0; i < g.size(); i++)
    {
        if(g.isInfected(i))
        {
            infected.push_back(i);
        }
    }
    json_dump["infected"] = infected;

    std::ofstream output_file("./output.json");
    output_file << json_dump;
}

void Session::delete_agents()
{
    for(auto& agent: agents)
    {
        delete agent;
    }
}