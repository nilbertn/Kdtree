Programming Assignment 5: K-d Trees


    /* *****************************************************************************
     *  Describe the Node data type you used to implement the
     *  2d-tree data structure.
     **************************************************************************** */
We use the point coordinates to refer to the 2D space using x and why coordinates
We use left and right nodes pointers to refer to the left and right child nodes
in the tree. The children nodes may contain children themselves to result in a tree structure
The Axis which is not stored expicity but is implied in the depth of the tree
in the 2d-tree, the axis alternates between x and y down the level of the tree
starting with the root. This i sused when deciding whether to insert left or right in the
tree.


    /* *****************************************************************************
     *  Describe your method for range search in a k-d tree.
     **************************************************************************** */
Start at the root, and recursively visit nodes of the tree.
At each node, check if the point lies within the query rectangle.
If it does, include it in the result set.
Decide which subtrees to visit: If the splitting line defined by the node's coordinate
and axis intersects the query rectangle, both subtrees must be visited since points
within the range can lie in both halves. If the splitting line does not intersect the
query rectangle, only one subtree (the one on the same side as the rectangle) needs
to be visited.
Recursively apply these steps to each node visited until the entire relevant
portion of the tree has been searched.


    /* *****************************************************************************
     *  Describe your method for nearest neighbor search in a k-d tree.
     **************************************************************************** */
Traverse the tree to find the leaf node that would contain the query point if the
query point were to be inserted into the tree. Keep track of the closest point found along the way.
Backtrack through the tree: On the way back up the tree, check each node's distance
to the query point. If the distance is less than the current best distance, update the closest point.
Prune subtrees: Before traversing a subtree, check if the distance between the query
point and the splitting line of the current node is less than the current best distance.
If it is not, there is no need to visit that subtree because it cannot contain a closer point.
Continue the search until the entire tree has been traversed as necessary, applying the
pruning rule to avoid unnecessary calculations.

/* *****************************************************************************
 *  How many nearest-neighbor calculations can your PointST implementation
 *  perform per second for input1M.txt (1 million points), where the query
 *  points are random points in the unit square?
 *
 *  Fill in the table below, rounding each value to use one digit after
 *  the decimal point. Use at least 1 second of CPU time. Do not use -Xint.
 *  (Do not count the time to read the points or to build the 2d-tree.)
 *  (See the checklist for information on how to do this)
 *
 *  Repeat the same question but with your KdTreeST implementation.
 *
 **************************************************************************** */


                 # calls to         /   CPU time     =   # calls to nearest()
                 client nearest()       (seconds)        per second
                ------------------------------------------------------
PointST:        100                    4.2         =       23.8

KdTreeST:       2,000,000             5.4         =       370,370.37

Note: more calls per second indicates better performance.

/* *****************************************************************************
 *  Suppose you wanted to add a method numberInRange(RectHV rect) to your
 *  KdTreeST, which should return the number of points that are inside rect
 *  (or on the boundary), i.e. the number of points in the iterable returned by
 *  calling range(rect).
 *
 *  Describe a pruning rule that would make this more efficient than the
 *  range() method. Also, briefly describe how you would implement it.
 *
 *  Hint: consider a range search. What can you do when the query rectangle
 *  completely contains the rectangle corresponding to a node?
 **************************************************************************** */
When the query rectangle completely contains the rectangle corresponding to a node,
we can ignore the points in the node and subsequent subtrees as as it lies within
the query rectangle. Therefore, you can return the number of points in the subtree
of the node as a result.
We can use an extra variable in the Node class to store the number of points in the
subtree, and update it for each point. This will result in thenumberInRange(RectHV rect)
method that will be able to implimented like range with the alteration
of the pruning rule.

/* *****************************************************************************
 *  Known bugs / limitations.
 **************************************************************************** */
Failed Test 11; and cannot implement the put method correctly to pass the case
where the min is greater than the max


/* *****************************************************************************
 *  Describe any serious problems you encountered.
 **************************************************************************** */
fixing the bug


/* *****************************************************************************
 *  List any other comments here. Feel free to provide any feedback
 *  on  how helpful the class meeting was and on how much you learned
 * from doing the assignment, and whether you enjoyed doing it.
 **************************************************************************** */
This assignment was really hard and time consuming. We went to multiple lab TA's for
help and none could solve passing test 11 (Stefan Gjaja, Austin Li, Sofia Michaelides).
Thus, our assignment passes all tests and works with the kd tree visualizer as well as
nearest neighbour visualizer and all other clients provided, though still does not
pass test 11. I hope this does not impact our grade!
