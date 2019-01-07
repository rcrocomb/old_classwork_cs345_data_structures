#include <map>
#include <stdio.h>
#include <stdint.h>
#include "radix_sort.h"

////////////////////////////////////////////////////////////////////////////////
// Static
////////////////////////////////////////////////////////////////////////////////

const unsigned int radix_sort::RADIX = 10;

////////////////////////////////////////////////////////////////////////////////
// Definitions
////////////////////////////////////////////////////////////////////////////////

// Get the digit @ the "place" place, e.g. if place is 1, then return
// the digit in the "ones" place, if place is 10, then return the
// digit in the "tens" place.
//
// If the value were 4321
//
// Then:
//
// 1 == place(4321, 1);
// 2 == place(4321, 10);
// 3 == place(4321, 100);
// 4 == place(4321, 1000);
//
// Here's the pow() alternative.  Once you go floating point,
// though, speed tanks (on a 1GHz Pentium III).  I didn't run it
// on my other machines.  You could use shift for binary-type radices.
// return (int(value / pow(RADIX,place)) % RADIX);

int
radix_sort::place_digit(const int value, const int place)
{
    return ((value / place) % RADIX);
}

// Does a radix sort of radix radix_sort::RADIX on the vector "v".
// latch to true if sort must continue: note that "place" must be
// contained.  A more general way would be to compare only to
// (place * RADIX - 1), but that becomes a value that doesn't fit in an
// integer for any value > 1E9, and I know that there's no values that
// can't be compared in the billions place and work.  Or you can use pow(),
// but I tried it, and it's less than half as fast.

void
radix_sort::sort(svector &v)
{
    bool continue_sort = false;
    int place = 1;      // becomes "0" with pow()
    unsigned int s;
    unsigned int i, j;

    std::map<unsigned int, svector> bins;
    do {
        bins.clear();

        // put into bins
        s = v.size();
        for(i = 0; i < s; i++)
        {
            bins[place_digit(v[i],place)].push_back(v[i]);  // yeehaw!
        }

        // concatenate back into vector

        v.clear();
        continue_sort = false;  // do we need another loop to continue sort?
        // for each of the bins
        for(i = 0; i < RADIX; i++)
        {
            s = (bins[i]).size();
            // put values back into vector in stable order
            for(j = 0; j < s; j++)
            {
                int x = (bins[i])[j];
                v.push_back(x);
                if( !continue_sort && (x >= place) && (place < 1E9))
                {
                    continue_sort = true;   // latches true
                    place *= RADIX;         // becomes ++place if you use pow()
                }
            }
        }
    } while (continue_sort);


}
