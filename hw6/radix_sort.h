#ifndef RADIX_SORT_H
#define RADIX_SORT_H

#include "sort.h"

class radix_sort
{

private:

    static const unsigned int RADIX;

private:

    static int place_digit(const int value, const int place);

public:

    static void sort(svector &v);


};

#endif  // RADIX_SORT_H
