//  Author: Robert Crocombe
//  Class: CS345
//  Section Leader: Mark Miles
//

#include "quicksort.h"
#include "insertion_sort.h"

#include <vector>
using std::vector;
#include <stack>

#include <utility>                      // std::pair is in here too
using std::swap;

int
quicksort::median_of_3(svector &v, const int left, const int right)
{
    const int center = (left + right) / 2;

    if (v[right] < v[center])
    {
        swap(v[right], v[center]);
    }

    if (v[center] < v[left])
    {
        swap(v[center], v[left]);
    }

    if (v[right] < v[center])
    {
        swap(v[right], v[center]);
    }

    return center;
}

// Finds a pivot and then partitions the
// vector to put all elements smaller than the pivot to it's left, and
// all elements greater than the pivot to it's right.  Values == pivot
// end up in the right spots, but I don't think it's guaranteed which
// side they end up on, and who cares, anyway?  Not me, that's who.

int
quicksort::pivot(svector &v, const int l, const int r)
{
    // find the index of the value on which we will pivot
    const int pivot = median_of_3(v, l, r);

    // get this pivot value
    const int value = v[pivot];

    // put pivot in a safe spot
    const int pivot_save = r - 1;
    swap(v[pivot], v[pivot_save]);

    // setup scanning variables (i scans right, j scans left)
    int i = l;
    int j = pivot_save;

    if (j <= i)
    {
        //printf("pivot: crossed to start.\n"); fflush(stdout);
        return i;
    }

    // While not partitioned
    while (1)
    {
        // find values to move to right of pivot index
        while (v[++i] < value)
        {
            if (i == pivot_save)
            {
                // trying to zoom off right end of subarray: done partitioning
                goto out;
            }
        }

        // find values to move to left of pivot index
        while (v[--j] > value)
        {
            if (j == l)
            {
                // trying to zoom off left end of subarray: done partitioning
                goto out;
            }
        }

        if (j < i)
        {
            // j and i have crossed.  Partitioning complete
            break;
        }

        // didn't cross: perform swap to put smaller to pivot left,
        // larger to pivot right
        swap(v[i], v[j]);
    }

out:
    // restore pivot to original location
    swap(v[pivot_save], v[i]);
    return i;
}

// In-place quicksort using a stack instead of recursive calls.
//
// From p 512.
//
// "The main idea is to design a nonrecursive version of in-place
// quick-sort using an explicit stack to iteratively process
// subproblems (each of which can be represented with a pair of
// indices marking subarray boundaries), with each iteration involves (sic)
// popping the top subproblem, splitting it in two (if it is big
// enough), and pushing the two new subproblems.  The trick is that
// when pushing the new subproblems, we should first push the larger
// subproblem and then the smaller one.  In this way, the sizes of the
// subproblems will at least double as we go down the stack; hence the
// stack can have depth at most O(log n)."

void
quicksort::qsort(svector &v, qstack &s)
{
    qpair p1, p2;
    while (!s.empty())
    {
        p1 = s.top();
        s.pop();

        // Anything to do in this subproblem?
        const int size = p1.second - p1.first;
        if( size < 1)
        {
            // No.  Discard it and move to next (if any)
            continue;
        } else if ( size < 10)
        {
            insertion_sort::sort(v, p1.first, p1.second);
        }

        DP(printf("\nCalling pivot on %3d to %3d\n", p1.first, p1.second);)

        // Yup: cut problem into two.
        int p = pivot(v, p1.first, p1.second);
        // p1.first is already set from pop(), above.
        p2.second = p1.second;  // rightmost boundary
        p1.second = p - 1;      // pivot @ p is in right spot
        p2.first = p + 1;       // ditto

        // Push biggest subproblem first.
        if ((p2.second - p2.first) > (p1.second - p1.first))
        {
            // p2 is bigger.  Push it first.
            s.push(p2);
            s.push(p1);
        } else {
            // p1 is bigger.  Push it first.
            s.push(p1);
            s.push(p2);
        }
    }
}

// Main woonage for quicksort.  Creates the first stack entry for the
// helper "qsort" routine.

void
quicksort::sort(svector &v)
{
    if (v.size() < 2)
    {
        return;
    }

    qstack s;
    s.push(qpair(0, v.size() - 1));
    qsort(v, s);
}

