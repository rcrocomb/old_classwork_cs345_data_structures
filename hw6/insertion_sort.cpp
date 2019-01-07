#include "insertion_sort.h"

void
insertion_sort::sort(svector &v, const int left, const int right)
{
    int t;
    int j;
    for( int i = left+1; i <= right; i++)
    {
        t = v[i];
        j = i;
        while ((j > left) && (v[j-1] > t))
        //while ((j > 0) && (v[j-1] > t))
        {
            v[j] = v[j-1];
            --j;
        }
        v[j] = t;
    }
}
