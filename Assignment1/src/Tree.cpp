#include "Session.h"
#include "Tree.h"

Tree::Tree(int rootLabel):
        children(),
        node(rootLabel)
{}

Tree::~Tree()
{
    for(auto& child : children)
    {
        delete child;
    }
}

Tree::Tree(const Tree& other):
    children(),
    node(other.node)
{
    for(auto& tree : other.children)
    {
        Tree* new_tree = tree->clone();
        children.push_back(new_tree);
    }
}

Tree& Tree::operator=(const Tree& other)
{
    if (this == &other)
    {
        return *this;
    }

    for(const auto& child : children)
    {
        delete child;
    }
    children.clear();

    for(const auto& other_child : other.children)
    {
        Tree* child = other_child->clone();
        children.push_back(child);
    }
    node = other.node;

    return *this;
}

Tree::Tree(Tree&& other):
        children(std::move(other.children)),
        node(other.node)
{}

Tree& Tree::operator=(Tree&& other)
{
    children = std::move(other.children);
    node =  other.node;
    return *this;
}

void Tree::addChild(const Tree& child)
{
    Tree* new_child = child.clone();
    children.push_back(new_child);
}

Tree* Tree::createTree(const Session& session, int rootLabel)
{
    switch (session.getTreeType())
    {
        case Cycle:
            return new CycleTree(rootLabel, session.cycle_num());
        case MaxRank:
            return new MaxRankTree(rootLabel);
        case Root:
            return new RootTree(rootLabel);
        default:
            return nullptr;
    }
}

Tree* Tree::get_child(int node_index)
{
    for(const auto& child : children)
    {
        if(child->node == node_index)
        {
            return child;
        }
    }
    return nullptr;
}

int Tree::get_node() const
{
    return node;
}

int Tree::children_size()
{
    return children.size();
}

RecMaxTree Tree::rec_trace_tree_max(Tree* tree, int depth)
{
    RecMaxTree output = {tree->children_size(), tree->get_node(), depth};
    for (const auto& child: children)
    {
        RecMaxTree child_output = child->rec_trace_tree_max(child, depth + 1);
        if(_is_bigger_rank(child_output, output)
                or _is_bigger_depth(child_output, output)
                or _is_bigger_node(child_output, output))
        {
            output = child_output;
        }
    }
    return output;
}

int Tree::rec_trace_tree_cycle(Tree* tree, int depth)
{
    if(tree->children_size() == 0 or depth == 0)
    {
        return tree->get_node();
    }
    return rec_trace_tree_cycle(tree->children.at(0), depth - 1);
}

Tree* Tree::last_child()
{
    return children.at(children_size() - 1);
}

bool Tree::_is_bigger_rank(RecMaxTree first, RecMaxTree second)
{
    return first.rank > second.rank;
}

bool Tree::_is_bigger_depth(RecMaxTree first, RecMaxTree second)
{
    return first.rank == second.rank and first.depth < second.depth;
}

bool Tree::_is_bigger_node(RecMaxTree first, RecMaxTree second)
{
    return first.rank == second.rank and first.depth == second.depth and first.node_label < second.node_label;
}
