# FindMyWay - AISearchAlgorithms

The algorithm implements Breadth-First search, Depth-First Search, Uniform-cost
Search, and A* Search separately.


Full specification for input.txt:

<..ALGO..>

<...START STATE...>

<...GOAL STATE...>

<...NUMBER OF LIVE TRAFFIC LINES...>

<...LIVE TRAFFIC LINES...>

<...NUMBER OF SUNDAY TRAFFIC LINES...>

<...SUNDAY TRAFFIC LINES...>


where:

<...ALGO....> is the algorithm to use and will be one of: “BFS”, “DFS”, “UCS”, “A*”.

<...START STATE...> is a string with the name of the start location (e.g., JordanHome).

<...GOAL STATE...> is a string with the name of the goal location (e.g., StaplesCenter).

<...NUMBER OF LIVE TRAFFIC LINES...> is the number of lines of live traffic information which is an arbitrarily large
list of current traveling times between intersections/locations.

<...LIVE TRAFFIC LINES...> are lines of live traffic information in the format described above,
i.e., <.STATE1.> <.STATE2.> <.TRAVEL TIME FROM STATE1 TO STATE2.>

<...NUMBER OF SUNDAY TRAFFIC LINES...> is the number of lines of Sunday traffic estimates of traveling time from each location listed in the file to the specified goal location

<...SUNDAY TRAFFIC LINES...> are lines of sunday traffic information in the format described
above, i.e., <.STATE.> <.ESTIMATED TIME FROM STATE TO GOAL.>



Full specification for output.txt:
Any number of lines with the following format for each:

<...STATE...> <...ACCUMULATED TRAVEL TIME FROM START TO HERE...>
