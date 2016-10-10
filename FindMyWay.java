package aisearchalgo;

import java.io.*;
import java.util.*;

public class FindMyWay {

    String algo, startState, goalState;
    int noOfLiveTL, noOfSundayTL;
    Map<String, List<Map<String, Integer>>> liveTrafficParentMap = new HashMap<String, List<Map<String, Integer>>>();
    Map<String, Integer> sundayTrafficParentMap = new HashMap<String, Integer>();

    private void createInputMap(BufferedReader input) throws IOException {
        String line;
        int count = 1;
        int afterLiveTraffic = 1000;

        while ((line = input.readLine()) != null) {
            if ((line.isEmpty() || line.trim().equals("") || line.trim().equals("\n"))) {
                continue;
            }
            if (count == 1) {
                algo = line;
            }
            if (count == 2) {
                startState = line;
            }
            if (count == 3) {
                goalState = line;
            }
            if (count == 4) {
                noOfLiveTL = Integer.parseInt(line);
                createLiveMap(input);
                count += noOfLiveTL;
                afterLiveTraffic = count;
            }
            if (count == afterLiveTraffic) {
                noOfSundayTL = Integer.parseInt(line);
                createSundayMap(input);
                count += noOfSundayTL;
            }
            count++;

        }

    }

    private void createLiveMap(BufferedReader input) throws IOException {
        String line;
        String[] terms = {};
        int lineCount = 0;

        while ((line = input.readLine()) != null && lineCount < noOfLiveTL) {
            if ((line.isEmpty() || line.trim().equals("") || line.trim().equals("\n"))) {
                continue;
            }

            terms = line.split(" ");
            lineCount++;

            if (liveTrafficParentMap.containsKey(terms[0])) {
                Map<String, Integer> childCostPath = new HashMap<String, Integer>();
                List<Map<String, Integer>> children = new ArrayList<Map<String, Integer>>();
                children = (ArrayList) liveTrafficParentMap.get(terms[0]);
                liveTrafficParentMap.remove(terms[0]);

                childCostPath.put(terms[1], Integer.parseInt(terms[2]));
                children.add(childCostPath);
                liveTrafficParentMap.put(terms[0], children);

            } else {
                Map<String, Integer> childCostPath = new HashMap<String, Integer>();
                List<Map<String, Integer>> children = new ArrayList<Map<String, Integer>>();
                childCostPath.put(terms[1], Integer.parseInt(terms[2]));
                children.add(childCostPath);
                liveTrafficParentMap.put(terms[0], children);
            }
        }
    }

    private void createSundayMap(BufferedReader input) throws IOException {
        String line;
        String[] terms = {};
        int lineCount = 0;

        while ((line = input.readLine()) != null && lineCount < noOfSundayTL) {
            if ((line.isEmpty() || line.trim().equals("") || line.trim().equals("\n"))) {
                continue;
            }

            terms = line.split(" ");
            lineCount++;
            sundayTrafficParentMap.put(terms[0], Integer.parseInt(terms[1]));
        }
    }

    private void applyAlgo() throws IOException {
        switch (algo) {
            case "BFS":
                implementSearchAlgo("BFS");
                break;

            case "DFS":
                //implementSearchAlgoDFS();
                implementSearchAlgo("DFS");
                break;

            case "UCS":
                implementSearchAlgo("UCS");
                break;

            case "A*":
                implementSearchAlgo("A*");
                break;
        }
    }

    
    private void implementSearchAlgo(String algo) throws IOException {
        int Fn = 0, Gn = 0, Hn = 0;
        ArrayList<Object> nodeQueue = new ArrayList<Object>();
        LinkedList<ArrayList<Object>> openQueue = new LinkedList<ArrayList<Object>>();
        LinkedList<ArrayList<Object>> closedQueue = new LinkedList<ArrayList<Object>>();

        if (algo.equals("BFS") || algo.equals("DFS") || algo.equals("UCS")) {
            nodeQueue.add(0, startState);
            Gn = 0;
            nodeQueue.add(1, Gn);
            nodeQueue.add(2, "NoParent");
        }

        if (algo.equals("A*")) {
            nodeQueue.add(0, startState);
            Gn = 0;
            nodeQueue.add(1, Gn);
            Hn = sundayTrafficParentMap.get(startState);
            nodeQueue.add(2, Hn);
            Fn = Gn + Hn;
            nodeQueue.add(3, Fn);
            nodeQueue.add(4, "NoParent");
        }

        if (algo.equals("UCS") || algo.equals("A*")) {
            openQueue.add((ArrayList) nodeQueue);
        }

        if (algo.equals("BFS")) {
            openQueue.addLast((ArrayList) nodeQueue);
        }

        if (algo.equals("DFS")) {
            openQueue.push((ArrayList) nodeQueue);
        }

        do {
            List<Object> oldNode = new ArrayList<Object>();
            Map<String, Integer> childCostPath = new HashMap<String, Integer>();
            List<Map<String, Integer>> children = new ArrayList<Map<String, Integer>>();

            if (openQueue.isEmpty()) {
                //System.out.println("Failed!!");
                break;
            }

            oldNode = (ArrayList) openQueue.pop(); //Dequeue

            //Goal Test
            String child = (String) oldNode.get(0);
            int GnParent = (Integer) oldNode.get(1);
            
            //System.out.println(child);
            
            if (child.equals(goalState)) {
                closedQueue.addFirst((ArrayList) oldNode);
                printOutputFile(closedQueue);
                //System.out.println("Success!!");
                break;
            }

            //If not a Goal, enqueue in closedQueue, and enqueue its children in openQueue
            closedQueue.addFirst((ArrayList) oldNode);

            String childBecomesParent = child;

            children = (ArrayList) liveTrafficParentMap.get(childBecomesParent);   //Geting the list of CurrentParent
            if (children == null) {
                continue;
            }

            //Reverse Iteration for DFS
            if (algo.equals("DFS")) {
                ListIterator itChildren = children.listIterator(children.size());
                while (itChildren.hasPrevious()) {
                    List<Object> childNodeQueue = new ArrayList<Object>();
                    childCostPath = (HashMap) itChildren.previous();     //Getting the children from the list
                    Map.Entry<String, Integer> childMap = childCostPath.entrySet().iterator().next();
                    child = (String) childMap.getKey();

                    childNodeQueue.add(0, child);
                    Gn = GnParent + 1;
                    childNodeQueue.add(1, Gn);
                    childNodeQueue.add(2, childBecomesParent);

                    Boolean doesExistInOpenQueue = false;
                    for (int i = 0; i < openQueue.size(); i++) {
                        List<Object> searchQueue = (ArrayList) openQueue.get(i);
                        if (child.equals((String) searchQueue.get(0))) {
                            if (Gn >= (Integer) searchQueue.get(1)) {
                                doesExistInOpenQueue = true;
                            } else {
                                openQueue.remove(i);
                                break;
                            }
                        }
                    }

                    Boolean doesExistInClosedQueue = false;
                    for (int i = 0; i < closedQueue.size(); i++) {
                        List<Object> searchQueue = (ArrayList) closedQueue.get(i);
                        if (child.equals((String) searchQueue.get(0))) {
                            if (Gn >= (Integer) searchQueue.get(1)) {
                                doesExistInClosedQueue = true;
                            } else {
                                closedQueue.remove(i);
                                break;
                            }
                        }
                    }

                    if (!doesExistInOpenQueue && !doesExistInClosedQueue) {
                        openQueue.push((ArrayList) childNodeQueue);
                    }

                }
            }

            //Normal Iteration for the rest
            if (algo.equals("BFS") || algo.equals("UCS") || algo.equals("A*")) {
                Iterator itChildren = children.iterator();
                while (itChildren.hasNext()) {
                    List<Object> childNodeQueue = new ArrayList<Object>();
                    childCostPath = (HashMap) itChildren.next();     //Getting the children from the list
                    Map.Entry<String, Integer> childMap = childCostPath.entrySet().iterator().next();
                    child = (String) childMap.getKey();

                    if (algo.equals("BFS")) {
                        childNodeQueue.add(0, child);
                        Gn = GnParent + 1;
                        childNodeQueue.add(1, Gn);
                        childNodeQueue.add(2, childBecomesParent);
                    }

                    if (algo.equals("UCS")) {
                        childNodeQueue.add(0, child);
                        Gn = GnParent + childMap.getValue();
                        childNodeQueue.add(1, Gn);
                        childNodeQueue.add(2, childBecomesParent);
                    }

                    if (algo.equals("A*")) {
                        childNodeQueue.add(0, child);
                        Gn = GnParent + childMap.getValue();
                        childNodeQueue.add(1, Gn);
                        Hn = sundayTrafficParentMap.get(child);
                        childNodeQueue.add(2, Hn);
                        Fn = Gn + Hn;
                        childNodeQueue.add(3, Fn);
                        childNodeQueue.add(4, childBecomesParent);
                    }

                    Boolean doesExistInOpenQueue = false;
                    for (int i = 0; i < openQueue.size(); i++) {
                        List<Object> searchQueue = (ArrayList) openQueue.get(i);
                        if (child.equals((String) searchQueue.get(0))) {
                            if (Gn >= (Integer) searchQueue.get(1)) {
                                doesExistInOpenQueue = true;
                            } else {
                                openQueue.remove(i);
                                break;
                            }
                        }
                    }

                    Boolean doesExistInClosedQueue = false;
                    for (int i = 0; i < closedQueue.size(); i++) {
                        List<Object> searchQueue = (ArrayList) closedQueue.get(i);
                        if (child.equals((String) searchQueue.get(0))) {
                            if (Gn >= (Integer) searchQueue.get(1)) {
                                doesExistInClosedQueue = true;
                            } else {
                                closedQueue.remove(i);
                                break;
                            }
                        }
                    }

                    if (!doesExistInOpenQueue && !doesExistInClosedQueue) {
                        if (algo.equals("BFS")) {
                            openQueue.addLast((ArrayList) childNodeQueue);
                        }

                        if (algo.equals("UCS") || algo.equals("A*")) {
                            openQueue.add((ArrayList) childNodeQueue);
                        }
                    }
                }

                if (algo.equals("UCS")) {
                    sortArray(1, openQueue);
                }

                if (algo.equals("A*")) {
                    sortArray(3, openQueue);
                }
            }
        } while (true);
    }

    //Sort the openAStarQueue
    private void sortArray(int sortIndex, LinkedList<ArrayList<Object>> openQueue) {
        //In case of AStar SortIndex = 4, Fn in the nodeQueue
        //In case of UCS SortIndex = 2, Gn in the nodeQueue

        List<Object> nodeQueue;
        int value1, value2;

        for (int index1 = 0; index1 < openQueue.size(); index1++) {
            nodeQueue = (ArrayList) openQueue.get(index1);
            value1 = (Integer) nodeQueue.get(sortIndex);
            for (int index2 = index1 + 1; index2 < openQueue.size(); index2++) {
                nodeQueue = (ArrayList) openQueue.get(index2);
                value2 = (Integer) nodeQueue.get(sortIndex);
                if (value1 > value2) {
                    nodeQueue = (ArrayList) openQueue.get(index1);
                    openQueue.set(index1, (ArrayList) openQueue.get(index2));
                    openQueue.set(index2, (ArrayList) nodeQueue);
                }
            }
        }
  /*           for (int i = 0; i < openQueue.size(); i++) {
			System.out.println(openQueue.get(i));
		}
        System.out.println("");
*/
    }

    
    private void printOutputFile(LinkedList<ArrayList<Object>> closedQueue) throws IOException {
        BufferedWriter output = null;
        LinkedList<ArrayList<Object>> outputStack = new LinkedList<ArrayList<Object>>();
        ArrayList<Object> nodeQueue = new ArrayList<Object>();

        
        
        nodeQueue = (ArrayList) closedQueue.pollFirst();

        String goalChildParent = "";

        if (algo.equals("UCS") || algo.equals("BFS") || algo.equals("DFS")) {
            goalChildParent = (String) nodeQueue.get(2);
        }

        if (algo.equals("A*")) {
            goalChildParent = (String) nodeQueue.get(4);
        }

        String prevChildParent = goalChildParent;

        outputStack.push((ArrayList) nodeQueue);

        while (!closedQueue.isEmpty()) {
            nodeQueue = (ArrayList) closedQueue.pollFirst();
            String node = (String) nodeQueue.get(0);
            if (node.equals(prevChildParent)) {
                outputStack.push((ArrayList) nodeQueue);

                if (algo.equals("UCS") || algo.equals("BFS") || algo.equals("DFS")) {
                    prevChildParent = (String) nodeQueue.get(2);
                }

                if (algo.equals("A*")) {
                    prevChildParent = (String) nodeQueue.get(4);
                }
            }

            if (prevChildParent.equals("NoParent")) {
                break;
            }
        }

       /* for (int i = 0; i < outputStack.size(); i++) {
			System.out.println(outputStack.get(i));
		}*/
        try {
            output = new BufferedWriter(new FileWriter("output.txt"));
            Iterator itStack = outputStack.iterator();
            while (itStack.hasNext()) {
                nodeQueue = (ArrayList) itStack.next();
                String node = (String) nodeQueue.get(0);
                int pathCost = (Integer) nodeQueue.get(1);
                output.write(node + " " + pathCost);
                output.newLine();
            }
        } finally {
            output.close();
        }
    }

    public static void main(String[] args) throws FileNotFoundException, IOException {
        BufferedReader input = null;
        FindMyWay ob = new FindMyWay();
        try {
            input = new BufferedReader(new FileReader("input.txt"));
            ob.createInputMap(input);

        } finally {
            input.close();
        }
        ob.applyAlgo();
    }

}
