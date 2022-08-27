#include "Tree.h"

RootTree::RootTree(int rootLabel):
        Tree(rootLabel)
{}

Tree * RootTree::clone() const
{
    return new RootTree(*this);
}

int RootTree::traceTree()
{
    return get_node();
}

