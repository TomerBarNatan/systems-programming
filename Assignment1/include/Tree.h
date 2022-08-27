#pragma once //TODO: ask if okay to use

#include <vector>
#include <memory>

class Session;

struct RecMaxTree
{
    int rank;
    int node_label;
    int depth;
};

class Tree{
public:
    Tree(int rootLabel);
    virtual ~Tree();

    Tree(const Tree& other);
    Tree& operator=(const Tree& other);

    Tree(Tree&& other);
    Tree& operator=(Tree&& other);

    virtual Tree* clone() const = 0;

    void addChild(const Tree& child);
    static Tree* createTree(const Session& session, int rootLabel);

    Tree* get_child(int node_index);
    int get_node() const;
    int children_size();

    RecMaxTree rec_trace_tree_max(Tree* tree, int depth);
    int rec_trace_tree_cycle(Tree* tree, int depth);
    Tree* last_child();
    virtual int traceTree()=0;

protected:
    std::vector<Tree*> children;

private:
    static bool _is_bigger_rank(RecMaxTree first, RecMaxTree second);
    static bool _is_bigger_depth(RecMaxTree first, RecMaxTree second);
    static bool _is_bigger_node(RecMaxTree first, RecMaxTree second);

private:
    int node;
};

class CycleTree: public Tree{
public:
    CycleTree(int rootLabel, int currCycle);

    virtual Tree* clone() const;
    virtual int traceTree();


private:
    int currCycle;
};

class MaxRankTree: public Tree{
public:
    MaxRankTree(int rootLabel);

    virtual Tree* clone() const;
    virtual int traceTree();

};

class RootTree: public Tree{
public:
    RootTree(int rootLabel);

    virtual Tree* clone() const;
    virtual int traceTree();

};

