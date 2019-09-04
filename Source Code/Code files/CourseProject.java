
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.TimeUnit;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Mesam
 */
public class CourseProject {

    public static void computeConnectedComponents(Map<Integer, List<Integer>> adjecencyList, HashMap<Integer, Boolean> assignedComponent, PrintWriter writer){
        List<Integer> neighbours;        
        HashMap<Integer, HashMap<Integer, Boolean>> connectedComponents;
        connectedComponents = new HashMap<Integer, HashMap<Integer, Boolean>>();
        HashMap<Integer, Boolean> connectedComponent;

        Set setOfKeys = adjecencyList.keySet();
        Iterator allverticeIterator = setOfKeys.iterator();
        Integer node;
        Integer neighborNode;
        Stack underConsideration = new Stack();
        while(allverticeIterator.hasNext()){   
            node = (Integer) allverticeIterator.next();
            if(!assignedComponent.get(node)){
                connectedComponent = new HashMap<Integer, Boolean>();
                connectedComponent.put(node,true);
                assignedComponent.put(node,true);
                underConsideration.push(node);
                Integer stackNode;
                while(!underConsideration.empty()){
                    stackNode =(Integer) underConsideration.pop();
                    neighbours = adjecencyList.get(stackNode);
                    if(neighbours != null){
                        for(int i = 0; i<neighbours.size();i++ ){
                            neighborNode = neighbours.get(i);
                            if(!assignedComponent.get(neighborNode)){
                                connectedComponent.put(neighborNode,true);
                                assignedComponent.put(neighborNode,true);
                                underConsideration.push(neighborNode);
                            }
                        }
                    }
                }

                Set componentKeys = connectedComponent.keySet();
                Iterator componentIterator = componentKeys.iterator();
                Integer keyNode;
                keyNode = (Integer) componentIterator.next();
                connectedComponents.put(keyNode,connectedComponent);
            }
        }

        writer.printf("***Graph Components***\n");
        Set componentsKeys = connectedComponents.keySet();
        Iterator componentsIterator = componentsKeys.iterator();
        Integer keyNode;
        while(componentsIterator.hasNext()){   
            keyNode = (Integer) componentsIterator.next();
            connectedComponent = new HashMap<Integer, Boolean>();
            connectedComponent = connectedComponents.get(keyNode);
            Set componentKeys = connectedComponent.keySet();
            Iterator componentIterator = componentKeys.iterator();
            //System.out.print("[");
            writer.printf("[%s",componentIterator.next());
            while(componentIterator.hasNext()){   
                //System.out.printf(",%d",(Integer) componentIterator.next());
                writer.printf(",%d",(Integer) componentIterator.next());
            }
            writer.printf("]\n");
            //System.out.print("]\n");
        }
    }
    public static void computeClosenessCentrality(HashMap<Integer, HashMap<Integer, Integer>> adjecencyList2, Map<Integer, List<Integer>> adjecencyList, HashMap<Integer, Boolean> assignedCloseness, HashMap<Integer, HashMap<Integer, ArrayList<HashMap>>> allPaths, PrintWriter writer, Double alpha){
        //this first portion is for betweeness
        HashMap<Integer, ArrayList<HashMap>> allPathsOfaNode;
        ArrayList<HashMap> pathsWithNode;
        HashMap<Integer, Boolean> path;
        
        Queue<Integer> queue = new LinkedList<Integer>();
        HashMap<Integer, HashMap<Integer, Double>> distancesMap;
        distancesMap = new HashMap<Integer, HashMap<Integer, Double>>();
        HashMap<Integer, Double> D;
        List<Integer> neighbours;
        HashMap<Integer, Integer> neighbours2;
                
                
        Set setOfKeys = adjecencyList2.keySet();
        Iterator allverticeIterator = setOfKeys.iterator();
        Integer node;
        Integer neighborNode;
        HashMap<Integer, Double> closenessCentrality;
        while(allverticeIterator.hasNext()){   
            node = (Integer) allverticeIterator.next();
            if(!assignedCloseness.get(node)){
                D = new HashMap<Integer, Double>();
                D.put(node,0.0);
                queue.add(node);               
                Integer parentNode;
                Double parentD, neighborD;
                
                //some calculations  for betweeness
                allPathsOfaNode = new HashMap<Integer, ArrayList<HashMap>>();
                pathsWithNode = new ArrayList<HashMap>();
                path = new HashMap<Integer, Boolean>();
                path.put(node,true);
                pathsWithNode.add(path);
                allPathsOfaNode.put(node, pathsWithNode);
                
                while(!queue.isEmpty()){
                    parentNode =(Integer) queue.poll();
                    parentD = D.get(parentNode); 
                    neighbours = adjecencyList.get(parentNode);
                    neighbours2 = adjecencyList2.get(parentNode);
                    
                
                    /*
                    //some calculations for betweeness
                    pathsWithNode = new ArrayList<HashMap>();
                    pathsWithNode = allPathsOfaNode.get(parentNode);
                    ArrayList<HashMap> pathsWithNeighborNode;
                    */
                    
                    if(neighbours2 != null){
                        Set setOfKeysOfDestinations = neighbours2.keySet();
                        Iterator destinationsIterator = setOfKeysOfDestinations.iterator();
                        while(destinationsIterator.hasNext()){   
                            neighborNode = (Integer) destinationsIterator.next(); 
                            neighborD = D.get(neighborNode);
                            
                            if(neighborD == null || (parentD + Math.pow(neighbours2.get(neighborNode), alpha) < neighborD)){
                                D.put(neighborNode, parentD + Math.pow(neighbours2.get(neighborNode), alpha));
                                queue.add(neighborNode);
                            }
                            
                            /*
                            //some calculations for betweeness
                            if(parentD + 1 == D.get(neighborNode)){
                                pathsWithNeighborNode = allPathsOfaNode.get(neighborNode);
                                if(pathsWithNeighborNode == null)
                                    pathsWithNeighborNode = new ArrayList<HashMap>();
                                for(int j=0; j < pathsWithNode.size(); j++){
                                    path = new HashMap<Integer, Boolean>();
                                    path= (HashMap<Integer, Boolean>) pathsWithNode.get(j).clone();
                                    path.put(neighborNode, true);
                                    pathsWithNeighborNode.add(path);
                                }
                                allPathsOfaNode.put(neighborNode, pathsWithNeighborNode);
                            }
                            */
                            //preActionsForBetweeness(allPaths,D,parentNode,neighborNode);
                        }
                    }
                }
                D.remove(node);
                distancesMap.put(node, D);
                assignedCloseness.put(node,true);
                
                //some calculation for betweeness
                //allPaths.put(node, allPathsOfaNode);
            }
        }
        
        closenessCentrality = new HashMap<Integer, Double>();
        //printing Results
        Set distancesKeys = distancesMap.keySet();
        Integer numberOfVertices = distancesMap.size();
        //numberOfVertices = 13;
        //System.out.printf("Number Of vertices = %s\n",numberOfVertices);
        Iterator distancesIterator = distancesKeys.iterator();
        Integer keyNode;
        Double closenessOfNode, sumation;
        writer.printf("\n\n\n***Graph Closeness Centrality***\n\n");
        while(distancesIterator.hasNext()){ 
            closenessOfNode = 0.0;
            sumation = 0.0;
            keyNode = (Integer) distancesIterator.next();
            D = new HashMap<Integer, Double>();
            D = distancesMap.get(keyNode);
            Set otherNodesKeys = D.keySet();
            Iterator otherNodes = otherNodesKeys.iterator();
            //System.out.printf("keyNode = %s   Distances---->",keyNode);          
            Integer otherNode;
            
            while(otherNodes.hasNext()){
                otherNode = (Integer) otherNodes.next();
                //System.out.printf("       %s = %d", otherNode,D.get(otherNode));
                sumation += (float)1 / D.get(otherNode);              
            }
            closenessOfNode = ((float)1/(numberOfVertices - 1)) * sumation;
            closenessCentrality.put(keyNode, closenessOfNode);
            //System.out.printf("      Closeness Centrality = %f\n", closenessOfNode);
            //writer.printf("[%d]\t%f\t[%f]\n", keyNode, closenessOfNode, sumation);
            //System.out.printf("For Values:   Sumation = %f,  1/(n-1) = %f\n", sumation, (float)1/(numberOfVertices - 1));
        }
        //System.out.print(numberOfVertices);

        //sorting
        List<Integer> mapKeys = new ArrayList<>(closenessCentrality.keySet());
        List<Double> mapValues = new ArrayList<>(closenessCentrality.values());
        Collections.sort(mapValues);
        Collections.sort(mapKeys);

        LinkedHashMap<Integer, Double> sortedMap = new LinkedHashMap<>();

        Iterator<Double> valueIt = mapValues.iterator();
        while (valueIt.hasNext()) {
            Double val = valueIt.next();
            Iterator<Integer> keyIt = mapKeys.iterator();

            while (keyIt.hasNext()) {
                Integer key = keyIt.next();
                Double comp1 = closenessCentrality.get(key);
                Double comp2 = val;

                if (comp1.equals(comp2)) {
                    keyIt.remove();
                    sortedMap.put(key, val);
                    break;
                }
            }
        }
        
        Set setOfAirportIds = sortedMap.keySet();
        Iterator<Integer> airportIdsIterator = setOfAirportIds.iterator();
        while (airportIdsIterator.hasNext()) {
            Integer airportId = airportIdsIterator.next();
            writer.printf("[%d]\t%f\n", airportId, sortedMap.get(airportId));
        }
    }
    public static void computeBetweenessCentrality(HashMap<Integer, HashMap<Integer, ArrayList<HashMap>>> allPaths, PrintWriter writer){
        HashMap<Integer,Double> betweenessCentrality = new HashMap<Integer,Double>();
        HashMap<Integer, ArrayList<HashMap>> allPathsOfaNode;
        ArrayList<HashMap> pathsWithNode;
        HashMap<Integer, Boolean> path;
        
        /*
        Set allNodeKeys = allPaths.keySet();
        //System.out.printf("Number Of vertices = %s\n",numberOfVertices);
        Iterator allNodesIterator = allNodeKeys.iterator();
        Integer Node;
        while(allNodesIterator.hasNext()){ 
            Node = (Integer) allNodesIterator.next();
            allPathsOfaNode = new HashMap<Integer, ArrayList<HashMap>>();
            allPathsOfaNode = allPaths.get(Node);
            System.out.printf("\n\nAll Paths of NODE : %d\n", Node);
            
            
            Set otherKeys = allPathsOfaNode.keySet();
            Iterator otherNodesIterator = otherKeys.iterator();
            Integer otherNode;
            while(otherNodesIterator.hasNext()){ 
                otherNode = (Integer) otherNodesIterator.next();
                
                pathsWithNode = new ArrayList<HashMap>();
                pathsWithNode = allPathsOfaNode.get(otherNode);
                System.out.printf("\nAll Paths of NODE : %d with NODE: %d", Node, otherNode);
                
                for(int i = 0; i < pathsWithNode.size(); i++)
                {
                    path = new HashMap<Integer, Boolean>();
                    path = pathsWithNode.get(i);
                    System.out.print("\nPath:");
                    System.out.print(path.keySet());
                }                
            }
        }
        */
        ArrayList<HashMap> pathsBetweenSandT;
        Set allNodeKeys = allPaths.keySet();
        Iterator allNodesIterator = allNodeKeys.iterator();
        Integer node;
        Double betweenessOfNode;
        Integer numberOfPathsNodeIn = 0;
        Integer totalNumberOfShortestPaths;
        writer.printf("\n\n\n***Graph Betweeness Centrality***\n\n");
        while(allNodesIterator.hasNext()){ 
            node = (Integer) allNodesIterator.next();
            betweenessOfNode=0.0;
            
            Iterator allNodesIterator2 = allNodeKeys.iterator();
            Integer S;
            while(allNodesIterator2.hasNext()){ 
                S = (Integer) allNodesIterator2.next();
                
                Iterator allNodesIterator3 = allNodeKeys.iterator();
                Integer T;
                while(allNodesIterator3.hasNext()){ 
                    T = (Integer) allNodesIterator3.next();
                    numberOfPathsNodeIn = 0;
                    pathsBetweenSandT = allPaths.get(S).get(T);
                    if(pathsBetweenSandT != null){
                        totalNumberOfShortestPaths = pathsBetweenSandT.size();
                        for(int i = 0; i < totalNumberOfShortestPaths; i++){
                            path = new HashMap<Integer, Boolean>();
                            path = pathsBetweenSandT.get(i);
                            if(path.containsKey(node)){
                                numberOfPathsNodeIn++;
                            }
                        }
                        betweenessOfNode += (float)numberOfPathsNodeIn/totalNumberOfShortestPaths;
                    }
                }
            }
            betweenessCentrality.put(node, betweenessOfNode);
            //System.out.printf("Node: %d  Betweeness = %f\n", node, betweenessOfNode);
            writer.printf("[%d]\t%f\n", node, betweenessOfNode);
        }
        
        /*System.out.print("\n\n************CUSTOM TEST Path:");
        System.out.print(allPaths.get(1).get(1).get(0).keySet());
        System.out.print("*******\n\n");
        */

    }
    public static void readFile(String filename, HashMap<Integer, HashMap<Integer, Integer>> adjecencyList2, Map<Integer, List<Integer>> adjecencyList, HashMap<Integer, Boolean> assignedComponent, HashMap<Integer, Boolean> assignedCloseness){
        List<Integer> neighbours;
        BufferedReader reader = null;
        HashMap<Integer, Integer> neighbours2;
        
        try {
            File file = new File(filename);
            reader = new BufferedReader(new FileReader(file));
            Integer firstNode = 0, secondNode=0, token1= 0;
            Integer maxNode = 0;
            String line;           
            while((line = reader.readLine()) != null) {
                String[] tokens = line.split("\t");

                if(Integer.parseInt(tokens[0]) >= 0){
                    //origin airport
                    firstNode = Integer.parseInt(tokens[2]);
                    //destination airport
                    secondNode = Integer.parseInt(tokens[3]);
                    if(adjecencyList.containsKey(firstNode))
                        neighbours = adjecencyList.get(firstNode);
                    else
                        neighbours = new ArrayList<Integer>();
                    neighbours.add(secondNode);
                    adjecencyList.put(firstNode, neighbours);
                    
                    if(adjecencyList2.containsKey(firstNode))
                        neighbours2 = adjecencyList2.get(firstNode);
                    else
                        neighbours2 = new HashMap<Integer, Integer>();
                     
                    neighbours2.put(secondNode, Integer.parseInt(tokens[1]));
                    adjecencyList2.put(firstNode, neighbours2);
                   
                    /* if(adjecencyList.containsKey(secondNode))
                        neighbours = adjecencyList.get(secondNode);
                    else
                        neighbours = new ArrayList<Integer>();
                    neighbours.add(firstNode);
                    adjecencyList.put(secondNode, neighbours);
                    */

                    assignedComponent.put(firstNode, false);
                    assignedComponent.put(secondNode, false);
                    assignedCloseness.put(firstNode, false);
                    //assignedCloseness.put(secondNode, false);
                    
                    token1 = Integer.parseInt(tokens[1]);
                }
            } 
            //System.out.print("\n\n");
            //System.out.print(token1);
            //System.out.print(secondNode);
            
            //System.out.print(adjecencyList.entrySet());
            //System.out.printf("Maximum Vertex Value: %d\n\n", maxNode);
   
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static void computeDegreeCentrality(HashMap<Integer, HashMap<Integer, Integer>> adjecencyList2, PrintWriter writer, Double alpha){
        Set setOfKeys = adjecencyList2.keySet();
        Iterator allverticeIterator = setOfKeys.iterator();
        Integer node=0;
        Integer neighborNode;
        Integer outWeights = 0;
        Integer numberOfNeighbors = 0;
        Double nodeOutDegreeCentrality = 0.0;
        HashMap<Integer, Double> outDegreeCentralities;
        outDegreeCentralities = new HashMap<Integer, Double>();
        while(allverticeIterator.hasNext()){   
            node = (Integer) allverticeIterator.next();
            outWeights = 0;
            numberOfNeighbors = 0;
            HashMap<Integer, Integer> neighbours2 = adjecencyList2.get(node);   
            if(neighbours2 != null){
                Set setOfKeysOfDestinations = neighbours2.keySet();
                numberOfNeighbors = setOfKeysOfDestinations.size();
                Iterator destinationsIterator = setOfKeysOfDestinations.iterator();
                while(destinationsIterator.hasNext()){   
                    neighborNode = (Integer) destinationsIterator.next();
                    outWeights += neighbours2.get(neighborNode);
                }
            }
            //nodeOutDegreeCentrality = (float)((float)outWeights/(float)numberOfNeighbors);
            nodeOutDegreeCentrality = new Double((float)numberOfNeighbors * Math.pow(new Double((float)outWeights/numberOfNeighbors), alpha));
            outDegreeCentralities.put(node, nodeOutDegreeCentrality);
        }
        
        /*
        System.out.print("\n");
        System.out.print(node);
        System.out.print("\n");
        System.out.print(numberOfNeighbors);
        System.out.print("\n");
        System.out.print(new Double((float)outWeights/numberOfNeighbors));
        System.out.print("\n");
        System.out.print(Math.pow(new Double((float)outWeights/numberOfNeighbors), alpha));
        System.out.print("\n");
        */
        //sorting
        List<Integer> mapKeys = new ArrayList<>(outDegreeCentralities.keySet());
        List<Double> mapValues = new ArrayList<>(outDegreeCentralities.values());
        Collections.sort(mapValues);
        Collections.sort(mapKeys);

        LinkedHashMap<Integer, Double> sortedMap = new LinkedHashMap<>();

        Iterator<Double> valueIt = mapValues.iterator();
        while (valueIt.hasNext()) {
            Double val = valueIt.next();
            Iterator<Integer> keyIt = mapKeys.iterator();

            while (keyIt.hasNext()) {
                Integer key = keyIt.next();
                Double comp1 = outDegreeCentralities.get(key);
                Double comp2 = val;

                if (comp1.equals(comp2)) {
                    keyIt.remove();
                    sortedMap.put(key, val);
                    break;
                }
            }
        }
        
        Set setOfAirportIds = sortedMap.keySet();
        Iterator<Integer> airportIdsIterator = setOfAirportIds.iterator();
        while (airportIdsIterator.hasNext()) {
            Integer airportId = airportIdsIterator.next();
            writer.printf("[%d]\t%f\n", airportId, sortedMap.get(airportId));
        }
        
        //System.out.print(setOfAirportIds.size());
    }
    public static void readFileForOutDegreeCentrality(String filename, HashMap<Integer, HashMap<Integer, Integer>> adjecencyList2, Map<Integer, List<Integer>> adjecencyList, HashMap<Integer, Boolean> assignedComponent, HashMap<Integer, Boolean> assignedCloseness){
        List<Integer> neighbours;
        BufferedReader reader = null;
        HashMap<Integer, Integer> neighbours2;
        
        try {
            File file = new File(filename);
            reader = new BufferedReader(new FileReader(file));
            Integer firstNode = 0, secondNode=0;
            Integer maxNode = 0, numberOfPassengers=0;
            String line;           
            while((line = reader.readLine()) != null) {
                String[] tokens = line.split("\t");

                if(Integer.parseInt(tokens[0]) >= 0){
                    //origin airport
                    firstNode = Integer.parseInt(tokens[2]);
                    //destination airport
                    secondNode = Integer.parseInt(tokens[3]);
                    numberOfPassengers = 0;
                    if(adjecencyList2.containsKey(firstNode))
                    {
                        neighbours2 = adjecencyList2.get(firstNode);
                        if(neighbours2.containsKey(secondNode))
                            numberOfPassengers = neighbours2.get(secondNode);
                    }
                    else
                        neighbours2 = new HashMap<Integer, Integer>();
                    numberOfPassengers += Integer.parseInt(tokens[0]);
                    
                    neighbours2.put(secondNode, numberOfPassengers);
                    adjecencyList2.put(firstNode, neighbours2);
                   
                    /* if(adjecencyList.containsKey(secondNode))
                        neighbours = adjecencyList.get(secondNode);
                    else
                        neighbours = new ArrayList<Integer>();
                    neighbours.add(firstNode);
                    adjecencyList.put(secondNode, neighbours);
                    */

                    assignedComponent.put(firstNode, false);
                    assignedComponent.put(secondNode, false);
                    assignedCloseness.put(firstNode, false);
                    //assignedCloseness.put(secondNode, false);
                }
            } 
            //System.out.print(firstNode);
            //System.out.print(secondNode);
            
            //System.out.print(adjecencyList.entrySet());
            //System.out.printf("Maximum Vertex Value: %d\n\n", maxNode);
   
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static void readFileForInDegreeCentrality(String filename, HashMap<Integer, HashMap<Integer, Integer>> adjecencyList2, Map<Integer, List<Integer>> adjecencyList, HashMap<Integer, Boolean> assignedComponent, HashMap<Integer, Boolean> assignedCloseness){
        
        BufferedReader reader = null;
        HashMap<Integer, Integer> neighbours2;
        
        try {
            File file = new File(filename);
            reader = new BufferedReader(new FileReader(file));
            Integer firstNode = 0, secondNode=0;
            Integer numberOfPassengers=0;
            String line;           
            while((line = reader.readLine()) != null) {
                String[] tokens = line.split("\t");

                if(Integer.parseInt(tokens[0]) >= 0){
                    //destination airport
                    firstNode = Integer.parseInt(tokens[3]);
                    //origin airport
                    secondNode = Integer.parseInt(tokens[2]);
                    numberOfPassengers = 0;
                    if(adjecencyList2.containsKey(firstNode))
                    {
                        neighbours2 = adjecencyList2.get(firstNode);
                        if(neighbours2.containsKey(secondNode))
                            numberOfPassengers = neighbours2.get(secondNode);
                    }
                    else
                        neighbours2 = new HashMap<Integer, Integer>();
                    numberOfPassengers += Integer.parseInt(tokens[0]);
                    
                    neighbours2.put(secondNode, numberOfPassengers);
                    adjecencyList2.put(firstNode, neighbours2);
                }
            } 
            //System.out.print(firstNode);
            //System.out.print(secondNode);
            
            //System.out.print(adjecencyList.entrySet());
            //System.out.printf("Maximum Vertex Value: %d\n\n", maxNode);
   
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static void readFileForMonthlyDegreeCentrality(String filename, HashMap<Integer, HashMap<Integer, Integer>> adjecencyList2, Map<Integer, List<Integer>> adjecencyList, HashMap<Integer, Boolean> assignedComponent, HashMap<Integer, Boolean> assignedCloseness, Integer month){
        
        BufferedReader reader = null;
        HashMap<Integer, Integer> neighbours2;
        
        try {
            File file = new File(filename);
            reader = new BufferedReader(new FileReader(file));
            Integer firstNode = 0, secondNode=0;
            Integer numberOfPassengers=0;
            String line;           
            while((line = reader.readLine()) != null) {
                String[] tokens = line.split("\t");

                if(Integer.parseInt(tokens[4]) == month){
                    //destination airport
                    firstNode = Integer.parseInt(tokens[3]);
                    //origin airport
                    secondNode = Integer.parseInt(tokens[2]);
                    numberOfPassengers = 0;
                    if(adjecencyList2.containsKey(firstNode))
                    {
                        neighbours2 = adjecencyList2.get(firstNode);
                        if(neighbours2.containsKey(secondNode))
                            numberOfPassengers = neighbours2.get(secondNode);
                    }
                    else
                        neighbours2 = new HashMap<Integer, Integer>();
                    numberOfPassengers += Integer.parseInt(tokens[0]);
                    
                    neighbours2.put(secondNode, numberOfPassengers);
                    adjecencyList2.put(firstNode, neighbours2);
                }
            } 
            //System.out.print(firstNode);
            //System.out.print(secondNode);
            
            //System.out.print(adjecencyList.entrySet());
            //System.out.printf("Maximum Vertex Value: %d\n\n", maxNode);
   
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static void readFileForCountingNumberOfUSAirports(String filename, HashMap<Integer, HashMap<Integer, Integer>> adjecencyList2, Map<Integer, List<Integer>> adjecencyList, HashMap<Integer, Boolean> assignedComponent, HashMap<Integer, Boolean> assignedCloseness){
        List<Integer> neighbours;
        BufferedReader reader = null;
        HashMap<Integer, Integer> neighbours2;
        
        try {
            File file = new File(filename);
            reader = new BufferedReader(new FileReader(file));
            Integer firstNode = 0, secondNode=0;
            Integer maxNode = 0, numberOfPassengers=0;
            String line;           
            while((line = reader.readLine()) != null) {
               String[] tokens = line.split("\t");
              // System.out.print(tokens[1]);
              // System.out.print("\n");
               if(tokens[1].equals("United States")){
                    //origin airport
                    firstNode = Integer.parseInt(tokens[0]);
                    //destination airport
                    secondNode = Integer.parseInt(tokens[2]);
                    numberOfPassengers = 0;
                    if(adjecencyList2.containsKey(firstNode))
                    {
                        neighbours2 = adjecencyList2.get(firstNode);
                        if(neighbours2.containsKey(secondNode))
                            numberOfPassengers = neighbours2.get(secondNode);
                    }
                    else
                        neighbours2 = new HashMap<Integer, Integer>();
                    numberOfPassengers += 0;

                    neighbours2.put(secondNode, numberOfPassengers);
                    adjecencyList2.put(firstNode, neighbours2);


                    if(tokens[3].equals("United States")){
                        if(adjecencyList2.containsKey(secondNode))
                        {
                            neighbours2 = adjecencyList2.get(secondNode);
                        }
                        else
                            neighbours2 = new HashMap<Integer, Integer>();
                        numberOfPassengers += 0;
                        adjecencyList2.put(secondNode, neighbours2);
                    }



                    /* if(adjecencyList.containsKey(secondNode))
                        neighbours = adjecencyList.get(secondNode);
                    else
                        neighbours = new ArrayList<Integer>();
                    neighbours.add(firstNode);
                    adjecencyList.put(secondNode, neighbours);
                    */

                    assignedComponent.put(firstNode, false);
                    assignedComponent.put(secondNode, false);
                    assignedCloseness.put(firstNode, false);
                    //assignedCloseness.put(secondNode, false);
               }
            } 
            
            
            //System.out.print(firstNode);
            //System.out.print("\n");
            //System.out.print(secondNode);
            
            System.out.print(adjecencyList2.keySet().size());
            System.out.print("\n");
            System.out.print(adjecencyList2.keySet());
            //System.out.printf("Maximum Vertex Value: %d\n\n", maxNode);
   
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InterruptedException {
        BufferedReader reader = null;
        Map<Integer, List<Integer>> adjecencyList = new HashMap<Integer, List<Integer>>();    
        HashMap<Integer, Boolean> assignedComponent = new HashMap<Integer, Boolean>();
        HashMap<Integer, Boolean> assignedCloseness = new HashMap<Integer, Boolean>();
        HashMap<Integer, HashMap<Integer, ArrayList<HashMap>>> allPaths = new HashMap<Integer, HashMap<Integer, ArrayList<HashMap>>>();
        HashMap<Integer, HashMap<Integer, Integer>> adjecencyList2;
        adjecencyList2 = new HashMap<Integer, HashMap<Integer, Integer>>();
        String option = "-1";
        String input, filename;
        
        try{
            PrintWriter writer = new PrintWriter("output.txt", "UTF-8");
            Double alpha = 1.0;
            
            Scanner in = new Scanner(System.in); 
            //while(option != "4"){
                //System.out.println(option);
                System.out.flush();
                System.out.println("0- Closeness Centralities.\n");
                System.out.println("1- In Degree Centralities.\n");
                System.out.println("2- Out Degree Centralities.\n");
                System.out.println("3- Monthly Centralities.\n");
                System.out.println("4- Exit.\n");

                System.out.println("Enter the option [0-4] you want results for..\n");
                option = in.nextLine();

                if(option.equals("0")){
                    System.out.flush();
                    System.out.println("Enter the file name you want to read for computing Closeness Centralities.\n");
                    filename = in.nextLine();
                    System.out.println("Enter the Alpha value.\n");
                    System.out.println("Format should be like 0.0\n");
                    input = in.nextLine();
                    alpha =  Double.parseDouble(input);
                    readFile(filename, adjecencyList2, adjecencyList, assignedComponent, assignedCloseness);
                    //Calling algorithm for finding connected components
                    //computeConnectedComponents(adjecencyList, assignedComponent, writer);
                    //Calling algorithm for computing closeness centralities
                    computeClosenessCentrality(adjecencyList2, adjecencyList, assignedCloseness, allPaths, writer, alpha);
                    System.out.print("\n**Results Generated in Output.txt**");
                }
                if(option.equals("1")){
                    System.out.flush();
                    System.out.println("Enter the file name you want to read for computing In Degree Centralities.\n");
                    filename = in.nextLine();
                    System.out.println("Enter the Alpha value.\n");
                    System.out.println("Format should be like 0.0\n");
                    input = in.nextLine();
                    alpha =  Double.parseDouble(input);

                    readFileForInDegreeCentrality(filename, adjecencyList2, adjecencyList, assignedComponent, assignedCloseness);
                    computeDegreeCentrality(adjecencyList2, writer, alpha);
                    System.out.print("\n**Results Generated in Output.txt**");
                }
                if(option.equals("2")){
                    System.out.flush();
                    System.out.println("Enter the file name you want to read for computing Out Degree Centralities.\n");
                    filename = in.nextLine();
                    System.out.println("Enter the Alpha value.\n");
                    System.out.println("Format should be like 0.0\n");
                    input = in.nextLine();
                    alpha =  Double.parseDouble(input);

                    readFileForOutDegreeCentrality(filename, adjecencyList2, adjecencyList, assignedComponent, assignedCloseness);
                    computeDegreeCentrality(adjecencyList2, writer, alpha);
                    System.out.print("\n**Results Generated in Output.txt**");
                }
                if(option.equals("3")){
                    System.out.flush();
                    System.out.println("Enter the file name you want to read for computing Monthly Centralities.\n");
                    filename = in.nextLine();
                    System.out.println("Enter the Month[1-12] you want results for..\n");
                    input = in.nextLine();
                    Integer month = Integer.parseInt(input);
                    readFileForMonthlyDegreeCentrality(filename, adjecencyList2, adjecencyList, assignedComponent, assignedCloseness, month);               
                    computeDegreeCentrality(adjecencyList2, writer, alpha);
                    System.out.print("\n**Results Generated in Output.txt**");
                }
                if(option.equals("4")){
                    System.out.flush();
                    System.out.println("Exiting system..");
                    TimeUnit.SECONDS.sleep(2);
                    System.exit(0);
                }
                
                TimeUnit.SECONDS.sleep(2);
            //}            
            //readFileForCountingNumberOfUSAirports("countUsAirports.txt", adjecencyList2, adjecencyList, assignedComponent, assignedCloseness);

            //Calling algorithm for computing betweeness centralities
            //computeBetweenessCentrality(allPaths, writer);
            //System.out.print("\n**Success**");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }    
}
