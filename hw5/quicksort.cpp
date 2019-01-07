//  Author: Robert Crocombe
//  Class: CS345
//  Section Leader: Mark Miles
//
//  Implemented in place qsort using stacks instead of recursion.  In
//  a *real* language.
//
//  Damn well better satisfy requirements of hw #5 problem 5, or it's
//  skull-punching time.

#include <iostream>
using std::cout;
using std::endl;

#include <iomanip>
using std::setw;
using std::setfill;

#include <vector>
#include <stack>

#include <fstream>                      // "operator <<" cleanliness
using std::ostream;

#include <utility>                      // std::pair is in here too
using std::swap;

#include <stdlib.h>                     // drand48()

////////////////////////////////////////////////////////////////////////////////
// Types
////////////////////////////////////////////////////////////////////////////////

typedef std::pair<int,int> qpair;       // for array bounds
typedef std::vector<int> qvector;       // where the data be
typedef std::stack<qpair> qstack;       // stack of bounds for subproblems

////////////////////////////////////////////////////////////////////////////////
// Macro
////////////////////////////////////////////////////////////////////////////////

#define DEBUG 0

const double CHANGE_OF_BASE = log(2);
#define LOG2(a) ( log(a) / CHANGE_OF_BASE )

#define DP(a) {if(DEBUG) {a} }          // makes turning print statements on
                                        // and off a bit easier

////////////////////////////////////////////////////////////////////////////////
// Prototypes
////////////////////////////////////////////////////////////////////////////////

int median_of_3(qvector &v, const int left, const int right);
void quicksort(qvector &v, const int left, const int right);
void qsort(qvector &v, qstack &s);

ostream &operator <<(ostream &o, const qvector &v);

bool monotonic(const qvector &v);

////////////////////////////////////////////////////////////////////////////////
// Definitions
////////////////////////////////////////////////////////////////////////////////

// Find the pivot value using the median-of-3 technique.

int
median_of_3(qvector &v, const int left, const int right)
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

DP(
    cout << "After median of 3: " << v;
    printf("Median for %3d to %3d is at %3d with value %3d\n",
           left, right, center, v[center]);
)
    return center;
}

// Finds a pivot (uses median-of-3 currently), and then partitions the
// vector to put all elements smaller than the pivot to it's left, and
// all elements greater than the pivot to it's right.  Values == pivot
// end up in the right spots, but I don't think it's guaranteed which
// side they end up on, and who cares, anyway?  Not me, that's who.

int
pivot(qvector &v, const int l, const int r)
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

    if ((j <= i) || (j < 0))
    {
        //printf("pivot: crossed to start.\n"); fflush(stdout);
        return i;
    }

    // While not partitioned
    while(1)
    {
        // find values to move to right of pivot index
        while(v[++i] < value)
        {
            if (i == pivot_save)
            {
                // trying to zoom off right end of subarray: done partitioning
                goto out;
            }
        }

        // find values to move to left of pivot index
        while(v[--j] > value)
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

        DP(cout << v;)
    }

out:
    // restore pivot to original location
    swap(v[pivot_save], v[i]);
    DP(cout << "Partitioned: " << v;)
    return i;
}

// Main woonage for quicksort.  Creates the first stack entry for the
// helper "qsort" routine.

void
quicksort(qvector &v)
{
    if (v.size() < 2)
    {
        return;
    }

    qstack s;
    s.push(qpair(0, v.size() - 1));
    qsort(v, s);
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
qsort(qvector &v, qstack &s)
{
    const double log_size = LOG2(v.size());
    qpair p1, p2;
    while (!s.empty())
    {
        qpair p1 = s.top();
        s.pop();

        // Anything to do in this subproblem?
        if( p1.second - p1.first < 1)
        {
            // No.  Discard it and move to next (if any)
            continue;
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

        if( LOG2(s.size()) > log_size)
        {
            // This can never ever happen.  Really.
            cout << "Stack depth has gone shiny side up!\n";
            cout << "Stack depth is " << s.size() << "\n";
            break;
        }
    }
}

// Handy for printing out: is yucky for data > 99 because of setw(2)

ostream &
operator <<(ostream &o, const qvector &v)
{
    for( int i = 0; i < v.size(); i++)
    {
        o << setw(2) << setfill('0') << v[i] << " ";
    }
    o << "\n";
}

// Here's "isSorted" for Problem #6, too.

bool
monotonic(const qvector &v)
{
    int monotonic = v[0];
    const int size = v.size();
    for( int i = 1; i < size; i++)
    {
        if (v[i] >= monotonic)  // >= allows for many of same value,
        {                       // i.e. not "strictly" monotonic
            monotonic = v[i];
        } else {
            return false;
        }
    }
    return true;
}


int
main
(
    int argc,
    char *argv[]
)
{
    qvector v;

    enum tests { MANUAL, AUTOMATIC };

    tests bob = AUTOMATIC;

    switch(bob)
    {
    case MANUAL:
        v.push_back(5);
        v.push_back(3);
        v.push_back(9);

        v.push_back(5);
        v.push_back(2);
        v.push_back(5);

        cout << "Before:\n" << v;
        quicksort(v);
        cout << "After:\n" << v;
        cout << "Monotonic == " << (monotonic(v) ? "true" : "false") << "\n";
        break;

    case AUTOMATIC:
    {
        const int MAX_SIZE = 23;    // to fit 80 columns okay

        int iterations = 10;        // default iterations count
        if( argc > 1)
        {
            iterations = atoi(argv[1]);
        }

        v.reserve(MAX_SIZE);        // remove need for memory allocation
        for(int i = 0; i < iterations; i++)
        {
            v.clear();              // remove any existing data

            // generate a random length for the array
            int length = static_cast<int>(drand48() * MAX_SIZE);
            if (length == 0)
            {
                // drand will give us a 0 length sometimes.  That's boring.
                --i;
                continue;
            }

            DP(printf("Sorting array with length %3d\n", length);)

            // generate random data
            for(int j = 0; j < length; j++)
            {
                v.push_back(static_cast<int>(drand48() * 99));
            }

            cout << i << "\nBefore: " << v;
            quicksort(v);
            cout << "After:  " << v << "\n";

            // test to make sure data is properly sorted: O(n)
            if (!monotonic(v))
            {
                cout << "Monotonicity failed!\n" << endl;
                break;
            }
        }
    }
    break;

    default:
        cout << "Unknown test.\n";
    }

    return 0;
}

