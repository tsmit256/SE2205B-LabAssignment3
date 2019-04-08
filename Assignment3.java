//COMPLETED WITH PARTNER
//tsmit256 (250950741) && zmascian (250949836)


public class Assignment3 {
    //By declaring parent and verticesArray outside of the individual functions,
    // it allows them to be altered in path search function & also accessed in the max flow function
    int parent[];
    Vertex<V> verticesArray[];

    public int breadthFirstPathSearch(Graph FN, int s, int d){
        //Array to track parent of each node in path
        parent = new int[FN.numVertices()];

        //Array to store all vertices in FN
        verticesArray = new Vertex<V>[FN.numVertices()];
        for(Vertex<V> u : FN.vertices()){ //initializes verticesArray to include all vertices of graph
            verticesArray[u.getLabel()] = u;
        }

        //Array to keep track of which nodes have been visited
        int visitedNodes[] = new int[FN.numVertices()];
        for(int i=0; i< visitedNodes.length; i++){ //Initialize all elements to 0
            visitedNodes[i] = 0;
        }

        //Queue to keep track of vertex being analyzed
        LinkedListQueue<Vertex<V>> LLQ = new LinkedListQueue<Vertex<V>>();
        LLQ.enqueue(verticesArray[s]); //enqueue the first Vertex
        parent[s] = -1; //start node does not have parent so, make it -1

        Vertex<V> v_i;
        //While loop until queue is empty
        while(!LLQ.isEmpty()){
            v_i = LLQ.dequeue();

            //Sets the current node to be visited
            visitedNodes[v_i.getLabel()] = 1;

            //looks at each outgoing edge of the current Vertex
            for(Edge e : FN.outgoingEdges(v_i)){

                //if the child node(vertex) hasn't been visited and potential for increase flow
                if((visitedNodes[FN.opposite(v_i,e).getLabel()] == 0) && (e.flowCap - e.flow > 0)){
                    parent[FN.opposite(v_i,e).getLabel()] = v_i.getLabel(); //sets the parent of adj node to be v_i
                    LLQ.enqueue(FN.opposite(v_i,e)); //enqueues adj (child) node
                }
            } //end of for loop
        }//end of while loop

        if(visitedNodes[d] == 1) return 1; //returns 1 if d has been visited (complete path)
        else{return 0;} //returns 0 if no un-full path exists from start to finish
    } //end of function

    public void maximizeFlowNetwork(Graph fN, int s, int t){

        //loop repeats as long as there is a path from start to finish that can have increased flow
        while(breadthFirstPathSearch(fN, s, t) == 1){
            Vertex<V> currVertex = verticesArray[parent[t]]; //currVertex is parent vertex of final vertex
            Edge e = fN.getEdge(currVertex,verticesArray[t]); //edge between t and parent of t
            int minFlowChange = e.flowCap - e.flow; //min flow difference initialized to be difference in last edge of path

            //loop which iterates through all edges in path, starting from end of path
            //Finds the largest amount of flow that can be added to suit requirements of all edge caps
            currVertex = verticesArray[parent[currVertex.getLabel()]];
            while(parent[currVertex.getLabel()] != -1){ //loop will stop once parent of s is called
                 
                e = fN.getEdge(verticesArray[parent[currVertex.getLabel()]],currVertex); //edge between currVertex and parent
                if(e.flowCap - e.flow < minFlowChange) //update min if flow difference of this edge is smaller
                    minFlowChange = e.flowCap - e.flow;
                
                //next iteration: set currVertex to be the parent of the past currVertex
                currVertex = verticesArray[parent[currVertex.getLabel()]]; 
            }//end of while loop

            currVertex = verticesArray[t]; //start currVertex at end of path again
            //loop which iterates through all edges in path, starting from end of path
            //Adds calculated flow to all edges in path
            while(parent[currVertex.getLabel()] != -1){
                e = fN.getEdge(verticesArray[parent[currVertex.getLabel()]],currVertex); //gets edge between currVertex and parent
                e.flow += minFlowChange; //this increments edge in path by minimally accepted flow which was determined above
            }
        } //end of outer while loop
    }//end of function
}//end of class
