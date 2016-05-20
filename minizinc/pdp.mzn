% 
% Pickup and Delivery Problem. Code Reworked from 
% "Traveling Salesman Problem, integer programming model in MiniZinc."
% 
% From GLPK:s example tsp.mod
% """
% TSP, Traveling Salesman Problem
%
% Written in GNU MathProg by Andrew Makhorin <mao@mai2.rcnet.ru> */
%
% The Traveling Salesman Problem (TSP) is stated as follows.
% Let a directed graph G = (V, E) be given, where V = {1, ..., n} is
% a set of nodes, E <= V x V is a set of arcs. Let also each arc
% e = (i,j) be assigned a number c[i,j], which is the length of the
% arc e. The problem is to find a closed path of minimal length going
% through each node of G exactly once.
% """

% Pickup And Delivery problem stands from TSP decsribed above, but with so-called "pairing constraint".
% Let p be the number of node pairs (therefore total number of nodes is n = 2*p). Nodes have numbers 1..n.
% Pairing constraint means that every node with number i < p, is visited before it's pair-node with number i+p.
% 
% This MiniZinc model for TSP was created by Hakan Kjellerstrand, hakank@bonetmail.com
% Reworked to Pickup and delivery probled by Viacheslav Shalamov.
%


% number of nodes 
int: n;

% number of pairs
int: p;

% empirical upper bound for solution
int: bound;

% set of arcs 
% 1..num_edges is the ordinary number of arc, 
% 1..2 means is this origin or target of the edge.
int: num_edges;
array[1..num_edges, 1..2] of 1..n: E;

% distance from node i to node j 
array[1..num_edges] of int: c;

% x[k] = 1 means that the salesman takes the k-th edge and goes from node i=E[k,1] to node j=E[k,2]. 
array[1..num_edges] of var 0..1: x;


% if x[k]=0, then y[k]=0. otherwise, 
% y[k] is the number of cars, which the salesman has after leaving
% node i=E[k,1] and before entering node j=E[k,2];
array[1..num_edges] of var int: y;

% the objective is to make the path length as small as possible 
var int: total = sum(i in 1..num_edges) (c[i] * x[i]);

solve :: int_search(
    [x[i] | i in 1..num_edges] ++
    [y[i] | i in 1..num_edges] ++
    [total],
   first_fail, % "occurrence",
   indomain_max,
   complete,
   )
   satisfy;

constraint

   %some bound
   total <= bound
   
   /\
   

   % the salesman visits node i before node p+i
   % going through all edges, 
   % if this two edges are in not the route, then skip
   % if origins of this edges arent a pair (i and i+p), the skip
   % otherwise check if first one is vivited before the second.
   forall(t in 1..num_edges) (
      forall(r in 1..num_edges) (
		   (x[t]!=1 \/ x[r]!=1 \/ E[t,1]+p != E[r,1] ) \/ y[t] >= y[r]
	  )
   )

   /\

   % the salesman leaves each node i exactly once 
   forall(i in 1..n) (
        sum(k in 1..num_edges where E[k,1] = i) (x[k]) = 1
   )

   /\

   % the salesman enters each node j exactly once 
   forall(j in 1..n) (
        sum(k in 1..num_edges where E[k,2] = j) (x[k]) = 1
   )

   /\

   % """
   % Constraints above are not sufficient to describe valid tours, so we
   % need to add constraints to eliminate subtours, i.e. tours which have
   % disconnected components. Although there are many known ways to do
   % that, I invented yet another way. The general idea is the following.
   % Let the salesman sells, say, cars, starting the travel from node 1,
   % where he has n cars. If we require the salesman to sell exactly one
   % car in each node, he will need to go through all nodes to satisfy
   % this requirement, thus, all subtours will be eliminated. 
   % 

   % if arc (i,j) does not belong to the salesman's tour, its capacity
   % must be zero; it is obvious that on leaving a node, it is sufficient
   % to have not more than n-1 cars 
   % """
   forall(k in 1..num_edges) (
      y[k] >= 0
      /\
      y[k] <= (n-1) * x[k]
   )

   /\
   % node[i] is a conservation constraint for node i 
   forall(i in 1..n) (
      % summary flow into node i through all ingoing arcs 
      (
      sum(k in 1..num_edges where E[k,2] = i) (y[k])
      % plus n cars which the salesman has at starting node 
      + (if i = 1 then n else 0 endif)
      )
      = % must be equal to 
      % summary flow from node i through all outgoing arcs 
      (
      sum(k in 1..num_edges where E[k,1] = i) (y[k])
      % plus one car which the salesman sells at node i 
      + 1
      )
   )

;

output [
   "total: " ++ show(total) ++ "\n" ++
   "x: " ++ show(x) ++ "\n" ++
   "y: " ++ show(y) ++ "\n"

];


