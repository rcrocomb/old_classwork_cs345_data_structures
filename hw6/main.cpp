#include "sort.h"
#include "insertion_sort.h"
#include "quicksort.h"
#include "radix_sort.h"

#include <values.h>

#include <iostream>
using std::cout;
using std::endl;

#include <iosfwd>

#include <vector>
using std::vector;

#include <string>
using std::string;

#include <iomanip>
using std::setw;
using std::setfill;
using std::setprecision;

#include <errno.h>
#include <sys/time.h>

////////////////////////////////////////////////////////////////////////////////
// Globals
////////////////////////////////////////////////////////////////////////////////

extern int errno;

////////////////////////////////////////////////////////////////////////////////
// Prototypes
////////////////////////////////////////////////////////////////////////////////

double get_time(void);
vector<int> get_data(const string &filename);
bool monotonic(const svector &v);
std::ostream & operator <<(std::ostream &o, const svector &v);


////////////////////////////////////////////////////////////////////////////////
// Definitions
////////////////////////////////////////////////////////////////////////////////

double
get_time(void)
{
    static struct timeval t;
    const static double MICROS_PER_SEC = 1E-6f;

    int ret = gettimeofday(&t, 0);
    if( ret != 0) { cout << "Err: gettimeofday: " << strerror(errno) << endl;}

    return t.tv_sec + (MICROS_PER_SEC * t.tv_usec);
}

vector<int>
get_data(const string &filename)
{
    vector<int> v;
    int count = 0;
    const int LIMIT = int(1E6); // For testing

    std::ifstream in(filename.c_str());
    if (!in)
    {
        cout << "Error opening file "  << filename << endl;
        throw new int(0);
    }

    while( count < LIMIT )
    {
        int a;
        in >> a;
        // This works a little different than I expected.  It only
        // returns true *after* you've hit EOF (logical, I suppose).
        // So we need to check it after we read but before we insert
        // to see if we just hit EOF.
        if( in.eof())
        {
            break;
        }
        v.push_back(a);
        ++count;
    }

    return v;
}

bool
monotonic(const svector &v)
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

std::ostream &
operator <<(std::ostream &o, const svector &v)
{
    using std::setw;
    using std::setfill;
    using std::dec;
    for( unsigned int i = 0; i < v.size(); i++)
    {
        o << dec << setw(3) << setfill('0') << v[i] << " ";
    }
    o << "\n";
    return o;
}

int
main
(
    int argc,
    char *argv[]
)
{
    enum tests { MANUAL, AUTOMATIC, DATA_FILE };

    tests bob = DATA_FILE;

    switch(bob)
    {
    case MANUAL:
    {
        svector v;
        v.push_back(5);
        v.push_back(3);
        v.push_back(9);

        v.push_back(5);
        v.push_back(2);
        v.push_back(5);

        cout << "Before:\n" << v;
        quicksort::sort(v);
        cout << "After:\n" << v;
        cout << "Monotonic == " << (monotonic(v) ? "true" : "false") << "\n";
    }
    break;

    case AUTOMATIC:
    {
        svector v;
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
            //double start = get_time();
            quicksort::sort(v);
            //double stop = get_time();
            //cout << "Time: " << setprecision(8) << stop - start << endl;
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

    case DATA_FILE:
    {
        vector<int> up = get_data("mid-large_ascending.txt");
        vector<int> down = get_data("mid-large_descending.txt");
        vector<int> random = get_data("mid-large_random.txt");

        double ustart = get_time();
        quicksort::sort(up);
        double ustop = get_time();

        double dstart = get_time();
        quicksort::sort(down);
        double dstop = get_time();

        double rstart = get_time();
        quicksort::sort(random);
        double rstop = get_time();

        cout << "Ascending\tDescending\tRandom\n"
             << ustop - ustart << "\t"
             << dstop - dstart << "\t"
             << rstop - rstart << "\n\n\n";
    }
    break;

    default:
        cout << "Unknown test.\n";
    }

    return 0;
}

