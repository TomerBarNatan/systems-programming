#include "Agent.h"

Virus::Virus(int nodeInd):
        Agent(),
        nodeInd(nodeInd)
{}

Agent* Virus::clone() const
{
    return new Virus(*this);
}

void Virus::act(Session& session)
{
    enqueue_infected_node(session);

    for (int i = 0; i < session.graph().size(); i++ )
    {
        if(session.graph().is_adjacent_nodes(nodeInd, i) and !session.is_node_with_virus(i))
        {
            Virus *new_virus = new Virus(i);
            session.addAgent(*new_virus);

            session.set_virus_on_node_histogram(i);
            break;
        }
    }
}

void Virus::enqueue_infected_node(Session& session) const
{
    Graph graph = session.graph();
    if(!graph.isInfected(nodeInd))
    {
        graph.infectNode(nodeInd);
        session.setGraph(graph);
        session.enqueueInfected(nodeInd);
    }
}
