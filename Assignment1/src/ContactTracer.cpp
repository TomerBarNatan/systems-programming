#include "Agent.h"

ContactTracer::ContactTracer():
    Agent()
{}

Agent* ContactTracer::clone() const
{
    return new ContactTracer(*this);
}

void ContactTracer::act(Session& session)
{
    if(!session.is_empty_infected_queue())
    {
        int root_index = session.dequeueInfected();
        Tree* tree = Tree::createTree(session, root_index);

        bfs_tree(tree, root_index, session);
        int node_to_disconnect = tree->traceTree(); // returns index of node to disconnect from graph

        Graph graph = session.graph();
        graph.disconnect_node(node_to_disconnect);
        session.setGraph(graph);
        delete tree;
    }
}

void ContactTracer::bfs_tree(Tree* tree, int start_node, Session& session)
{
    std::queue<int> nodes_queue;
    std::vector<int> visited(session.graph().size());
    std::vector<Tree*> trees = std::vector<Tree*>(session.graph().size());

    nodes_queue.push(start_node);
    visited.at(start_node) = 1;

    trees.at(start_node) = tree;
    int current_node_index;

    while (!nodes_queue.empty())
    {
        Tree* current_tree = trees.at(nodes_queue.front());
        nodes_queue.pop();
        current_node_index = current_tree->get_node();
        for (int i = 0; i < session.graph().size(); i++)
        {
            if (session.graph().is_adjacent_nodes(current_node_index, i) and visited.at(i) == 0)
            {
                visited.at(i) = 1;
                nodes_queue.push(i);
                Tree* tree_to_add = Tree::createTree(session, i);

                current_tree->addChild(*tree_to_add);
                trees.at(i) = current_tree->last_child();
                delete tree_to_add;
            }
            current_node_index = current_tree->get_node();
        }
    }
}

