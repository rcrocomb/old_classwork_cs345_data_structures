#ifndef QUICKSORT_H
#define QUICKSORT_H

#include "sort.h"

#include <stack>
#include <utility>

class quicksort
{

typedef std::pair<int, int> qpair;
typedef std::stack<qpair> qstack;

private:

    static int median_of_3(svector &v, const int left, const int right);
    static int pivot(svector &v, const int left, const int right);
    static void qsort(svector &v, qstack &s);

public:

    static void sort(svector &v);
    ~quicksort(void);
};

#endif  // QUICKSORT_H
