#include "Tree.h"

MaxRankTree::MaxRankTree(int rootLabel):
    Tree(rootLabel)
{}

Tree * MaxRankTree::clone() const
{
    return new MaxRankTree(*this);
}

int MaxRankTree::traceTree()
{
    return Tree::rec_trace_tree_max(this, 0).node_label;
}

