#ifndef INSERTION_SORT_H
#define INSERTION_SORT_H

#include "sort.h"

class insertion_sort
{

public:

    static void sort(svector &v) { sort(v, 0, v.size() - 1); }
    static void sort(svector &v, const int l, const int r);

};

#endif  // INSERTION_SORT_H
