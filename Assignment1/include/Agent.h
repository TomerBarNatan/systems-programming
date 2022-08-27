#pragma once

#include <vector>

#include "Tree.h"
#include "Session.h"

class Agent{
public:
    Agent();

    virtual Agent* clone() const = 0;
    virtual void act(Session& session)=0;
};

class ContactTracer: public Agent{
public:
    ContactTracer();

    virtual Agent* clone() const;
    virtual void act(Session& session);

private:
    void bfs_tree(Tree* tree, int start_node, Session& session);
};

class Virus: public Agent{
public:
    Virus(int nodeInd);

    virtual Agent* clone() const;
    virtual void act(Session& session);

private:
    void enqueue_infected_node(Session& session) const;

private:
    const int nodeInd;
};