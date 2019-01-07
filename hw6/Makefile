
DEPENDS = .depend

CXX = g++
CXXFLAGS = -O2 -march=pentium3 -Wall -MMD
#CXXFLAGS = -g -Wall -MMD

# What we're generating

SOURCE = main.cpp quicksort.cpp insertion_sort.cpp radix_sort.cpp

MAINFILE = main

.SUFFIEXES: .cpp .o

.cpp.o:
	$(CXX) $(CXXFLAGS) -c $<

# the stuff

OBJECTS = $(SOURCE:.cpp=.o)

$(MAINFILE):	$(OBJECTS)
		$(CXX) $(CXXFLAGS) $(OBJECTS) -o $@ $(LIBRARIES)

.PHONY: clean
clean:
	rm -f $(OBJECTS) \
	touch $(DEPENDS)

.PHONY: distclean
distclean:
	rm -f $(OBJECTS) $(MAINFILE) \
	touch $(DEPENDS)
