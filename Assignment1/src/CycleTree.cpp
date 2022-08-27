#include "Tree.h"

CycleTree::CycleTree(int rootLabel, int currCycle):
        Tree(rootLabel),
        currCycle(currCycle)
{}

Tree * CycleTree::clone() const
{
    return new CycleTree(*this);
}

int CycleTree::traceTree()
{
    return Tree::rec_trace_tree_cycle(this, currCycle);
}
