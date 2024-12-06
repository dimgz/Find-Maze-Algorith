// Dimitrios Gazos 4035
// Thomas Papachristos 4277
// Georgios Mpogris 4123


import java.util.Scanner;
import java.util.Random;
import java.util.Arrays;
import java.util.Collections;
import java.util.ArrayList;
import java.io.*;


class Uniform
{

    //private ArrayList<Integer> keepX = new ArrayList<Integer>();
    //private ArrayList<Integer> keepY = new ArrayList<Integer>();
    
    private String n_printer;
    private String Parent;
    private Node currentN, testing;
    private Node NextNode;
    private ArrayList<Node> visitedNode = new ArrayList<Node>();
    private ArrayList<Node> Childs = new ArrayList<Node>();
    private int Num;
    private Double ParentCost, TotalCostPath;
    private int P_X,P_Y;
    private int G1_X, G1_Y,G2_X,G2_Y;
    private boolean FinalC = false;
    private double Pcost = 0;
    private int testX,testY;
    private int S_X,S_Y,Sou_X,Sou_Y;
    private String maze_matrix[][];
    


    public Uniform(int Num) 
    {
        this.Num = Num;
        this.maze_matrix = new String[Num+1][Num+1]; //this.maze_matrix = new String[Num][Num];
    }

    // calculate cost of node
    public int CalcCost(int S_Y, int S_X, int P_Y, int P_X)
    {
        this.S_X = S_X;
        this.S_Y = S_Y;
        this.P_X = P_X;
        this.P_Y = P_Y;


        int valueS = 0;
        int valuePath = 0;
        int D = 1;
        
        // when Starting and Ending cost is really low to find the cheapest path 

        
        
        if (maze_matrix[S_Y-1][S_X-1] == "S"){
            
            D = 0;
        }
        else{

            if (maze_matrix[S_Y-1][S_X-1] == "G1" || maze_matrix[S_Y-1][S_X-1] == "G2"){
                D=0;
            }
            else
                valueS = Integer.parseInt(maze_matrix[S_Y-1][S_X-1]);
            
        }

        

        if (maze_matrix[P_Y][P_X] == "S" || maze_matrix[P_Y][P_X] == "W")
        {
            ;
        }
        else
        {
        
            if (maze_matrix[P_Y][P_X] == "G1" || maze_matrix[P_Y][P_X] == "G2")
            {
            
                D = 0;
            }
        else{
            valuePath = Integer.parseInt(maze_matrix[P_Y][P_X]);
        }

        }

        if (D != 0)
            D = Math.abs(valueS - valuePath); 
        //System.out.println(D+" !");
        
        return D;
    }


    public void UCS(String maze_matrix[][], int S_X, int S_Y, int G1_X, int G1_Y, int G2_X, int G2_Y)
    {
        
        this.S_X = S_X;
        this.S_Y = S_Y;
        this.G1_X = G1_X;
        this.G1_Y = G1_Y;
        this.G2_X = G2_X;
        this.G2_Y = G2_Y;

        

        for ( int sourceX = 0; sourceX < Num; sourceX++ )
        {
            for ( int sourceY = 0; sourceY < Num; sourceY++ )
            {
                this.maze_matrix[sourceY][sourceX] = maze_matrix[sourceY][sourceX];
            }
        }

       

        TotalCostPath = 0.0;
        int times1 = 0;
        
        int Sou_X = S_X;
        int Sou_Y = S_Y;
        Node start = new Node(TotalCostPath,"A",Sou_X,Sou_Y,0,0,maze_matrix[S_X-1][S_Y-1]);
        visitedNode.add(start);
        currentN = start;

        
      
        
        while ( FinalC == false )
        {
            times1++;
            NextNode = null;
            findNext(currentN.x_place,currentN.y_place,currentN.parent,TotalCostPath);
            visitedNode.add(NextNode);
            TotalCostPath = currentN.mainCost;
            //System.out.println("NODE MAIN    " + currentN.no + " " + currentN.parent + " " + currentN.x_place + " " + currentN.y_place);
            currentN = NextNode;

            
        }


        // print path 
        Node you;
        ArrayList<String> pathList = new ArrayList<String>();
        Collections.reverse(visitedNode);
        visitedNode.remove(0);
        you = visitedNode.get(0);
        pathList.add(you.currentPlace);
        pathList.add(you.parent);
        int testX = you.parentX;
        int testY = you.parentY;
        System.out.println("List of path nodes!");
        for (int i =0; i <  visitedNode.size(); i++)
        {
            you = visitedNode.get(i);
            
            
            if (you.x_place == testX && you.y_place == testY)  
            {
                testX = you.parentX;
                testY = you.parentY; 
                System.out.println(you.currentPlace + " ( " + you.x_place + " , " + you.y_place + " )");
                pathList.add(you.parent);
                you.x_place = testX;
                you.y_place = testY;
                
            }
            
            
           
        }

       
        Collections.reverse(pathList);

        pathList.remove(0);
        
        System.out.print("Path:   ");

        StringBuilder sb = new StringBuilder();

        for ( int p = 0; p<pathList.size(); p++)
        {
            
            sb.append(pathList.get(p) + "  -> ");
            //int priX = keepX.get(p);
            //int priY = keepX.get(p);

        }
        n_printer = sb.toString();
        n_printer = n_printer.substring(0, n_printer.length()-3);
        System.out.println(n_printer);

        
        System.out.println("Total Path cost:    " + TotalCostPath);

        System.out.println("Total Branches:    " + times1);

    }
    // choose the next node
    public Node findNext(int S_X, int S_Y, String Parent,Double ParentCost)
    {
        this.S_X = S_X;
        this.S_Y = S_Y;
        this.Parent = Parent;
        this.ParentCost= ParentCost;

        if (maze_matrix[S_Y-1][S_X-1] == "G1" ||    maze_matrix[S_Y-1][S_X-1]  == "G2")
                FinalC = true;
        

        Pcost = 0;
        Parent = maze_matrix[S_Y-1][S_X-1];
        ParentCost = currentN.mainCost;
        
        
        
        // Clock priority! 1->8
           
        // Right (1)
        if (S_Y-1 < 0 || S_X < 0 || S_Y-1 > Num-1 || S_X > Num-1 || maze_matrix[S_Y-1][S_X] == "W" || maze_matrix[S_Y-1][S_X] == null)
            ;
        else{

            

            
            Pcost = CalcCost(S_Y,S_X,S_Y-1,S_X) + 1.0;
            Double C = Pcost + ParentCost; 
            Node PotentialNode = new Node(C,Parent,S_X+1,S_Y,S_X,S_Y,maze_matrix[S_Y-1][S_X]);
            Childs.add(PotentialNode);
        
            int checkingR = checkDuplicate(PotentialNode);
            
            if (checkingR == 1)
            {
                Childs.remove(PotentialNode);
            }
            
            int checking2R = checkIfAlreadyVisited(PotentialNode);
            
            if (checking2R == 1)
            {
                Childs.remove(PotentialNode);   
            }

            
        }

        // DIAG Right Down (2) *
        if (S_Y < 0 || S_X < 0 || S_Y > Num - 1|| S_X > Num - 1 || maze_matrix[S_Y][S_X] == "W" || maze_matrix[S_Y][S_X] == null)
            ;
        else{

    
            Pcost = CalcCost(S_Y,S_X,S_Y,S_X) + 0.5;
            Double C = Pcost + ParentCost; 
            Node PotentialNode = new Node(C,Parent,S_X+1,S_Y+1,S_X,S_Y,maze_matrix[S_Y][S_X]);
            Childs.add(PotentialNode);
            
            
            int checkingRD = checkDuplicate(PotentialNode);
            if (checkingRD == 1)
            {
                Childs.remove(PotentialNode);
                
            }
            
            int checking2RD = checkIfAlreadyVisited(PotentialNode);
            
            if (checking2RD == 1)
            {
                Childs.remove(PotentialNode);
               
            }
            
        }
        // Down (3)
        if (S_Y < 0 || S_X-1 < 0 || S_Y > Num -1 || S_X-1 > Num - 1|| maze_matrix[S_Y][S_X-1] == "W" || maze_matrix[S_Y][S_X-1] == null)
            ;
        else{

            
            
            Pcost = CalcCost(S_Y,S_X,S_Y,S_X-1) + 1.0;
            Double C = Pcost + ParentCost; 
            Node PotentialNode = new Node(C,Parent,S_X,S_Y+1,S_X,S_Y,maze_matrix[S_Y][S_X-1]);
            Childs.add(PotentialNode);
            
            
            int checkingD = checkDuplicate(PotentialNode);
            if (checkingD == 1)
            {
                Childs.remove(PotentialNode);
                
            }
            
            int checking2D = checkIfAlreadyVisited(PotentialNode);
            if (checking2D == 1)
            {
                Childs.remove(PotentialNode);
                
            }
            
        }
        // DIAG Left Down (4) *
        if (S_Y < 0 || S_X-2 < 0 || S_Y > Num -1 || S_X-2 > Num - 1|| maze_matrix[S_Y][S_X-2] == "W" || maze_matrix[S_Y][S_X-2] == null)
            ;
        else{

          
            
            Pcost = CalcCost(S_Y,S_X,S_Y,S_X-2) + 0.5;
            Double C = Pcost + ParentCost; 
            Node PotentialNode = new Node(C,Parent,S_X-1,S_Y+1,S_X,S_Y,maze_matrix[S_Y][S_X-2]);
            Childs.add(PotentialNode);
            
            
            int checking1 = checkDuplicate(PotentialNode);
            if (checking1 == 1)
            {
                
                Childs.remove(PotentialNode);
                checking1 = 0;
            }
            
            int checking2 = checkIfAlreadyVisited(PotentialNode);
            if (checking2 == 1)
            {
                Childs.remove(PotentialNode);
                checking2 = 0;
            }
            
        }
        // Left (5)
        if (S_Y-1 < 0 || S_X-2 < 0 || S_Y-1 > Num -1 || S_X-2 > Num -1|| maze_matrix[S_Y-1][S_X-2] == "W" || maze_matrix[S_Y-1][S_X-2] == null)
            ;
        else{
            
            
            Pcost = CalcCost(S_Y,S_X,S_Y-1,S_X-2) + 1.0;
            Double C = Pcost + ParentCost; 
            Node PotentialNode = new Node(C,Parent,S_X-1,S_Y,S_X,S_Y,maze_matrix[S_Y-1][S_X-2]);
            Childs.add(PotentialNode);
            
            
            int checking1 = checkDuplicate(PotentialNode);
            if (checking1 == 1)
            {
                
                Childs.remove(PotentialNode);
                checking1 = 0;
            }
            
            int checking2 = checkIfAlreadyVisited(PotentialNode);
            if (checking2 == 1)
            {
                Childs.remove(PotentialNode);
                checking2 = 0;
            }

            
        }
        // DIAG Left Up (6) *
        if (S_Y-2 < 0 || S_X-2 < 0 || S_Y-2 > Num -1|| S_X-2 > Num -1|| maze_matrix[S_Y-2][S_X-2] == "W" || maze_matrix[S_Y-2][S_X-2] == null)
            ;
        else{
            
            
            
            Pcost = CalcCost(S_Y,S_X,S_Y-2,S_X-2) + 0.5;
            Double C = Pcost + ParentCost; 
            Node PotentialNode = new Node(C,Parent,S_X-1,S_Y-1,S_X,S_Y,maze_matrix[S_Y-2][S_X-2]);
            Childs.add(PotentialNode);
            
            
            int checking1 = checkDuplicate(PotentialNode);
            if (checking1 == 1)
            {
                
                Childs.remove(PotentialNode);
                checking1 = 0;
            }
            
            int checking2 = checkIfAlreadyVisited(PotentialNode);
            if (checking2 == 1)
            {
                Childs.remove(PotentialNode);
                checking2 = 0;
            }
           
        }
        // Up   (7)
        if (S_Y-2 < 0 || S_X-1 < 0 || S_Y-2 > Num -1|| S_X-1 > Num -1|| maze_matrix[S_Y-2][S_X-1] == "W" || maze_matrix[S_Y-2][S_X-1] == null)
            ;
        else{
            
            
            
            Pcost = CalcCost(S_Y,S_X,S_Y-2,S_X-1) + 1.0;
            Double C = Pcost + ParentCost; 
            Node PotentialNode = new Node(C,Parent,S_X,S_Y-1,S_X,S_Y,maze_matrix[S_Y-2][S_X-1]);
            Childs.add(PotentialNode);
            
            
            int checking1 = checkDuplicate(PotentialNode);
            if (checking1 == 1)
            {
                Childs.remove(PotentialNode);
                checking1 = 0;
            }
            
            int checking2 = checkIfAlreadyVisited(PotentialNode);
            if (checking2 == 1)
            {
                Childs.remove(PotentialNode);
                checking2 = 0;
            }

        }
        // DIAG Right Up (8) *
        if (S_Y-2 < 0 || S_X < 0 || S_Y-2 > Num -1|| S_X > Num -1|| maze_matrix[S_Y-2][S_X] == "W" || maze_matrix[S_Y-2][S_X] == null)
            ;
        else{

           
            
            Pcost = CalcCost(S_Y,S_X,S_Y-2,S_X) + 0.5;
            Double C = Pcost + ParentCost; 
            Node PotentialNode = new Node(C,Parent,S_X+1,S_Y-1,S_X,S_Y,maze_matrix[S_Y-2][S_X]);
            Childs.add(PotentialNode);
            
            
            int checking1 = checkDuplicate(PotentialNode);
            if (checking1 == 1)
            {
                Childs.remove(PotentialNode);
                checking1 = 0;
            }
            
            int checking2 = checkIfAlreadyVisited(PotentialNode);
            if (checking2 == 1)
            {
                Childs.remove(PotentialNode);
                checking2 = 0;
            }
        }
        

        
        double min = 1000000.0;

        if (maze_matrix[S_Y-1][S_X-1] != "G1" || maze_matrix[S_Y-1][S_X-1] != "G2"){    
            
            // cheapest path
            
            for (int b = 0; b < Childs.size(); b++)
            {
                
                //Node NextNode = null;
                Node test = null;
                test = Childs.get(b);

                if (test.mainCost < min)
                {
                    min = test.mainCost;
                    NextNode = test;
                }
            }
            Childs.remove(NextNode);
        }      
        
        return NextNode;
    }
    // Check if potential node is visited again and pick the smallest one
    public int checkDuplicate(Node PotentialNode)
    {
        Node check;
        for (int z=0; z < Childs.size(); z++)
        {
            check = Childs.get(z);
            if (check.x_place == PotentialNode.x_place)
            {
                if (check.y_place == PotentialNode.y_place)
                {
                    if (PotentialNode.mainCost > check.mainCost)
                    {
                        return 1;
                    }
                }
            } 
            
        }
        return 0;
    }
    // Check if node is already visited
    public int checkIfAlreadyVisited(Node PotentialNode)
    {
        
        for (int i=0;i<visitedNode.size();i++)
        {
            testing = visitedNode.get(i);
            if (testing.x_place == PotentialNode.x_place && testing.y_place == PotentialNode.y_place)
            {
                
                return 1;
            } else ;
        }
        return 0; 
    }

}












class Astar
{

    
    private String n_printer;
    private String Parent;
    private Node currentN, testing;
    private Node NextNode;
    private ArrayList<Node> visitedNode = new ArrayList<Node>();
    private ArrayList<Node> Childs = new ArrayList<Node>();
    private int Num;
    private Double ParentCost, TotalCostPath;
    private int P_X,P_Y;
    private int G1_X, G1_Y,G2_X,G2_Y;
    private boolean FinalC = false;
    private double Pcost = 0;
    private int testX,testY;

    private int X,Y,FX,FY;

    private int S_X,S_Y,Sou_X,Sou_Y;
    private String maze_matrix[][];
    




    public Astar(int Num) 
    {
        this.Num = Num;
        this.maze_matrix = new String[Num+1][Num+1]; //this.maze_matrix = new String[Num][Num]; 
    }

    // caclulate cost of node
    public int CalcCostA(int S_Y, int S_X, int P_Y, int P_X)
    {
        this.S_X = S_X;
        this.S_Y = S_Y;
        this.P_X = P_X;
        this.P_Y = P_Y;


        int valueS = 0;
        int valuePath = 0;
        int D = 1;
        
        // when Starting and Ending cost is really low to find the cheapest path 

        
        
        if (maze_matrix[S_Y-1][S_X-1] == "S"){
            
            D = 0;
        }
        else{

            if (maze_matrix[S_Y-1][S_X-1] == "G1" || maze_matrix[S_Y-1][S_X-1] == "G2"){
                D = 0;
            }
            else
                valueS = Integer.parseInt(maze_matrix[S_Y-1][S_X-1]);
            
        }

        

        if (maze_matrix[P_Y][P_X] == "S" || maze_matrix[P_Y][P_X] == "W")
        {
            ;
        }
        else
        {
        
            if (maze_matrix[P_Y][P_X] == "G1" || maze_matrix[P_Y][P_X] == "G2")
            {
            
                D = 0;
            }
        else{
            valuePath = Integer.parseInt(maze_matrix[P_Y][P_X]);
        }

        }

        if (D != 0)
            D = Math.abs(valueS - valuePath); 
        //System.out.println(D+" !");
        
        return D;
    }


    public void AstarSearch(String maze_matrix[][], int S_X, int S_Y, int G1_X, int G1_Y, int G2_X, int G2_Y)
    {
        
        this.S_X = S_X;
        this.S_Y = S_Y;
        this.G1_X = G1_X;
        this.G1_Y = G1_Y;
        this.G2_X = G2_X;
        this.G2_Y = G2_Y;

        //2 this.maze_matrix = maze_matrix;

        for ( int sourceX = 0; sourceX < Num; sourceX++ )
        {
            for ( int sourceY = 0; sourceY < Num; sourceY++ )
            {
                this.maze_matrix[sourceY][sourceX] = maze_matrix[sourceY][sourceX];
            }
        }

       // System.out.println(S_X + " SX ");

        TotalCostPath = 0.0;
        
        
        Sou_X = S_X;
        
        //System.out.println(S_X + " MID ");
        Sou_Y = S_Y;
        Node start = new Node(TotalCostPath,"A",Sou_X,Sou_Y,0,0,maze_matrix[S_X-1][S_Y-1],findHeuristicValueA(S_X-1, S_Y-1));
        visitedNode.add(start);
        currentN = start;
        
        int times = 0;
        
        while ( FinalC == false )
        {
            times++;
            NextNode = null;
            findNextA(currentN.x_place,currentN.y_place,currentN.parent,TotalCostPath);
            visitedNode.add(NextNode);
            TotalCostPath = currentN.mainCost;
            //System.out.println("NODE MAIN    " + currentN.no + " " + currentN.parent + " " + currentN.x_place + " " + currentN.y_place);
            currentN = NextNode;

            
        }
        // print path 
        Node you;
        ArrayList<String> pathList = new ArrayList<String>();
        Collections.reverse(visitedNode);
        visitedNode.remove(0);
        you = visitedNode.get(0);
        pathList.add(you.currentPlace);
        pathList.add(you.parent);
        int testX = you.parentX;
        int testY = you.parentY;
        System.out.println("List of path nodes!");
        for (int i =0; i <  visitedNode.size(); i++)
        {
            you = visitedNode.get(i);
            
            if (you.x_place == testX && you.y_place == testY)  
            {
                testX = you.parentX;
                testY = you.parentY; 
                System.out.println(you.currentPlace + " ( " + you.x_place + " , " + you.y_place + " )");
                pathList.add(you.parent);
                you.x_place = testX;
                you.y_place = testY;
                
            }
           
        }

       
        Collections.reverse(pathList);

        pathList.remove(0);
        System.out.print("A* Path:   ");

        StringBuilder sb = new StringBuilder();

        for ( int p = 0; p<pathList.size(); p++)
        {
            
            sb.append(pathList.get(p) + "  -> ");
            //int priX = keepX.get(p);
            //int priY = keepX.get(p);

        }
        n_printer = sb.toString();
        n_printer = n_printer.substring(0, n_printer.length()-3);
        System.out.println(n_printer);


        System.out.println("A* Total Path cost:    " + TotalCostPath);

        System.out.println("A* Total Branches:    " + times);

    }
    // Choose the next node
    public Node findNextA(int S_X, int S_Y, String Parent,Double ParentCost)
    {
        this.S_X = S_X;
        this.S_Y = S_Y;
        this.Parent = Parent;
        this.ParentCost= ParentCost;

        if (maze_matrix[S_Y-1][S_X-1] == "G1" ||    maze_matrix[S_Y-1][S_X-1]  == "G2")
                FinalC = true;
        

        Pcost = 0;
        Parent = maze_matrix[S_Y-1][S_X-1];
        ParentCost = currentN.mainCost;
        
        
        
        // Clock priority! 1->8
           
        // Right (1)
        


        if (S_Y-1 < 0 || S_X < 0 || S_Y-1 > Num-1 || S_X > Num-1 || maze_matrix[S_Y-1][S_X] == "W" || maze_matrix[S_Y-1][S_X] == null)
            ;
        else{

            

            
            Pcost = CalcCostA(S_Y,S_X,S_Y-1,S_X) + 1.0;
            Double C = Pcost + ParentCost; 
            Node PotentialNode = new Node(C,Parent,S_X+1,S_Y,S_X,S_Y,maze_matrix[S_Y-1][S_X],findHeuristicValueA(S_X, S_Y-1));
            Childs.add(PotentialNode);
            
            int checkingR = checkDuplicateA(PotentialNode);
            if (checkingR == 1)
            {
                Childs.remove(PotentialNode);
            }
            
            int checking2R = checkIfAlreadyVisitedA(PotentialNode);
            if (checking2R == 1)
            {
                Childs.remove(PotentialNode);   
            }

            
        }
        // DIAG Right Down (2) *
        if (S_Y < 0 || S_X < 0 || S_Y > Num - 1|| S_X > Num - 1 || maze_matrix[S_Y][S_X] == "W" || maze_matrix[S_Y][S_X] == null)
            ;
        else{

    
            Pcost = CalcCostA(S_Y,S_X,S_Y,S_X) + 0.5;
            Double C = Pcost + ParentCost; 
            Node PotentialNode = new Node(C,Parent,S_X+1,S_Y+1,S_X,S_Y,maze_matrix[S_Y][S_X],findHeuristicValueA(S_X, S_Y));
            Childs.add(PotentialNode);
            
            
            int checkingRD = checkDuplicateA(PotentialNode);
            if (checkingRD == 1)
            {
                Childs.remove(PotentialNode);
                
            }
            
            int checking2RD = checkIfAlreadyVisitedA(PotentialNode);
            if (checking2RD == 1)
            {
                Childs.remove(PotentialNode);
               
            }
            
        }
        // Down (3)
        if (S_Y < 0 || S_X-1 < 0 || S_Y > Num -1 || S_X-1 > Num - 1|| maze_matrix[S_Y][S_X-1] == "W" || maze_matrix[S_Y][S_X-1] == null)
            ;
        else{

            
            
            Pcost = CalcCostA(S_Y,S_X,S_Y,S_X-1) + 1.0;
            Double C = Pcost + ParentCost; 
            Node PotentialNode = new Node(C,Parent,S_X,S_Y+1,S_X,S_Y,maze_matrix[S_Y][S_X-1],findHeuristicValueA(S_X-1, S_Y));
            Childs.add(PotentialNode);
            
            
            int checkingD = checkDuplicateA(PotentialNode);
            if (checkingD == 1)
            {
                Childs.remove(PotentialNode);
                
            }
            
            int checking2D = checkIfAlreadyVisitedA(PotentialNode);
            if (checking2D == 1)
            {
                Childs.remove(PotentialNode);
                
            }
            
        }
        // DIAG Left Down (4) *
        if (S_Y < 0 || S_X-2 < 0 || S_Y > Num -1 || S_X-2 > Num - 1|| maze_matrix[S_Y][S_X-2] == "W" || maze_matrix[S_Y][S_X-2] == null)
            ;
        else{

          
            
            Pcost = CalcCostA(S_Y,S_X,S_Y,S_X-2) + 0.5;
            Double C = Pcost + ParentCost; 
            Node PotentialNode = new Node(C,Parent,S_X-1,S_Y+1,S_X,S_Y,maze_matrix[S_Y][S_X-2],findHeuristicValueA(S_X-2, S_Y));
            Childs.add(PotentialNode);
            
            int checking1LD = checkDuplicateA(PotentialNode);
            if (checking1LD == 1)
            {
                
                Childs.remove(PotentialNode);
                checking1LD = 0;
            }
            
            int checking2LD = checkIfAlreadyVisitedA(PotentialNode);
            if (checking2LD == 1)
            {
                Childs.remove(PotentialNode);
                checking2LD = 0;
            }
            
        }
        // Left (5)
        if (S_Y-1 < 0 || S_X-2 < 0 || S_Y-1 > Num -1 || S_X-2 > Num -1|| maze_matrix[S_Y-1][S_X-2] == "W" || maze_matrix[S_Y-1][S_X-2] == null)
            ;
        else{
            
            
            Pcost = CalcCostA(S_Y,S_X,S_Y-1,S_X-2) + 1.0;
            Double C = Pcost + ParentCost; 
            Node PotentialNode = new Node(C,Parent,S_X-1,S_Y,S_X,S_Y,maze_matrix[S_Y-1][S_X-2],findHeuristicValueA(S_X-2, S_Y-1));
            Childs.add(PotentialNode);
            
            int checking1L = checkDuplicateA(PotentialNode);
            if (checking1L == 1)
            {
                
                Childs.remove(PotentialNode);
                checking1L = 0;
            }
            
            int checking2L = checkIfAlreadyVisitedA(PotentialNode);
            if (checking2L == 1)
            {
                Childs.remove(PotentialNode);
                checking2L = 0;
            }

            
        }
        // DIAG Left Up (6) *
        if (S_Y-2 < 0 || S_X-2 < 0 || S_Y-2 > Num -1|| S_X-2 > Num -1|| maze_matrix[S_Y-2][S_X-2] == "W" || maze_matrix[S_Y-2][S_X-2] == null)
            ;
        else{
            
            
            
            Pcost = CalcCostA(S_Y,S_X,S_Y-2,S_X-2) + 0.5;
            Double C = Pcost + ParentCost; 
            Node PotentialNode = new Node(C,Parent,S_X-1,S_Y-1,S_X,S_Y,maze_matrix[S_Y-2][S_X-2],findHeuristicValueA(S_X-2, S_Y-2));
            Childs.add(PotentialNode);
           
            
            int checking1LU = checkDuplicateA(PotentialNode);
            if (checking1LU == 1)
            {
                
                Childs.remove(PotentialNode);
                checking1LU = 0;
            }
            
            int checking2LU = checkIfAlreadyVisitedA(PotentialNode);
            if (checking2LU == 1)
            {
                Childs.remove(PotentialNode);
                checking2LU = 0;
            }
           
        }
        // Up   (7)
        if (S_Y-2 < 0 || S_X-1 < 0 || S_Y-2 > Num -1|| S_X-1 > Num -1|| maze_matrix[S_Y-2][S_X-1] == "W" || maze_matrix[S_Y-2][S_X-1] == null)
            ;
        else{
            
            
            
            Pcost = CalcCostA(S_Y,S_X,S_Y-2,S_X-1) + 1.0;
            Double C = Pcost + ParentCost; 
            Node PotentialNode = new Node(C,Parent,S_X,S_Y-1,S_X,S_Y,maze_matrix[S_Y-2][S_X-1],findHeuristicValueA(S_X-1, S_Y-2));
            Childs.add(PotentialNode);
            
            
            int checking1U = checkDuplicateA(PotentialNode);
            if (checking1U == 1)
            {
                Childs.remove(PotentialNode);
                checking1U = 0;
            }
            
            int checking2U = checkIfAlreadyVisitedA(PotentialNode);
            if (checking2U == 1)
            {
                Childs.remove(PotentialNode);
                checking2U = 0;
            }

        }
        // DIAG Right Up (8) *
        if (S_Y-2 < 0 || S_X < 0 || S_Y-2 > Num -1|| S_X > Num -1|| maze_matrix[S_Y-2][S_X] == "W" || maze_matrix[S_Y-2][S_X] == null)
            ;
        else{

           
            
            Pcost = CalcCostA(S_Y,S_X,S_Y-2,S_X) + 0.5;
            Double C = Pcost + ParentCost; 
            Node PotentialNode = new Node(C,Parent,S_X+1,S_Y-1,S_X,S_Y,maze_matrix[S_Y-2][S_X],findHeuristicValueA(S_X, S_Y-2));
            Childs.add(PotentialNode);
            
            int checking1RU = checkDuplicateA(PotentialNode);
            if (checking1RU == 1)
            {
                Childs.remove(PotentialNode);
                checking1RU = 0;
            }
            
            int checking2RU = checkIfAlreadyVisitedA(PotentialNode);
            if (checking2RU == 1)
            {
                Childs.remove(PotentialNode);
                checking2RU = 0;
            }
        }
        

        
        double min = 1000000.0;

        if (maze_matrix[S_Y-1][S_X-1] != "G1" || maze_matrix[S_Y-1][S_X-1] != "G2"){    
            // cheapest path
            
            for (int b = 0; b < Childs.size(); b++)
            {
                
                //Node NextNode = null;
                Node test = null;
                test = Childs.get(b);
                if (test.mainCost + test.H_value < min)
                {
                    min = test.mainCost + test.H_value;
                    NextNode = test;
                }
            }
            Childs.remove(NextNode);
        }      
        
      
        return NextNode;
    }
    // Check if potential node is visited again and pick the smallest one
    public int checkDuplicateA(Node PotentialNode)
    {
        Node check;
        for (int z=0; z < Childs.size(); z++)
        {
            check = Childs.get(z);
            if (check.x_place == PotentialNode.x_place)
            {
                if (check.y_place == PotentialNode.y_place)
                {
                    if (PotentialNode.mainCost + PotentialNode.H_value > check.mainCost + check.H_value )
                    {
                        
                        return 1;
                    }
                }
            } 
            
        }
        return 0;
    }
    // Check if node is already visited
    public int checkIfAlreadyVisitedA(Node PotentialNode)
    {
        
        for (int i=0;i<visitedNode.size();i++)
        {
            testing = visitedNode.get(i);
            if (testing.x_place == PotentialNode.x_place && testing.y_place == PotentialNode.y_place)
            {
                
                return 1;
            } else ;
        }
        return 0; 
    }

    public double findHeuristicValueA(int X, int Y)
    {
        this.X = X;
        this.Y = Y;
 
        // Manhatan heuristic value 

        Double testH1 = Math.abs(X - (G1_X-1)) + Math.abs(Y - (G1_Y-1)) + (0.5-2) * Math.min(Math.abs(X - (G1_X-1)), Math.abs(Y - (G1_Y-1))); 
        Double testH2 = Math.abs(X - (G2_X-1)) + Math.abs(Y - (G2_Y-1)) + (0.5-2) * Math.min(Math.abs(X - (G2_X-1)), Math.abs(Y - (G2_Y-1)));

        // EUCLIDEAN ALGORITM

        //Double testH1 = Math.sqrt(Math.abs(X - (G1_X-1)) * Math.abs(X - (G1_X-1)) + Math.abs(Y - (G1_Y-1)) * Math.abs(Y - (G1_Y-1)));
        //Double testH2 = Math.sqrt(Math.abs(X - (G2_X-1)) * Math.abs(X - (G2_X-1)) + Math.abs(Y - (G2_Y-1)) * Math.abs(Y - (G2_Y-1)));
        
        // find closest end
        if (testH1 > testH2)
            return testH2;
        else
            return testH1;
        
            
    }

}

public class FindMaze
{

    private static String S,G1,G2;
    private static int N,p;
    
    

    public static void main(String[] args) {
        System.out.println("==================================================");            
        System.out.println("*                                                *");
        System.out.println("*                   Maze Finder                  *");
        System.out.println("*                                                *");
        System.out.println("*                      v 1.00                    *");
        System.out.println("*   created by: Dimitrios Gazos AM:4035          *");
        System.out.println("*               Thomas Papachristos AM:4277      *");
        System.out.println("*               Georgios Mpogris AM:4123         *");
        System.out.println("*                                                *");
        System.out.println("==================================================");

        String maze_matrix[][];    // our maze 
        N = 0;      // number of cells
        p = 0;      // possibility
        
        String [] start;
        String [] end1;
        String [] end2;
        
        
        // objects
        
        Scanner scan = new Scanner(System.in);      
        Random rand = new Random();
        
        // Read Inputs from user
        try{
            System.out.println("Start position: enter X Y (seperated by space) EXAMPLE: 1 2");
            S = scan.nextLine();
            start = S.split(" ");
        
            System.out.println("Ending 1 position: enter X Y (seperated by space)");
            G1 = scan.nextLine();
            end1 = G1.split(" ");
        
            System.out.println("Ending 2 position: enter X Y (seperated by space)");
            G2 = scan.nextLine();
            end2 = G2.split(" ");
            
            System.out.println("Enter number of rows");
            N = scan.nextInt();
            //System.out.println(N);

            System.out.println("Enter possibility of creating walls (input is 1/input)");
            System.out.println("EXAMPLE: 1 -> all walls, 10 -> 1/10 to create a wall");
            p = scan.nextInt();
            //System.out.println(p);
            
            
            
            // convert strings to integer

            int S_X = Integer.parseInt(start[0]);
            int S_Y = Integer.parseInt(start[1]);

            int G1_X = Integer.parseInt(end1[0]);
            int G1_Y = Integer.parseInt(end1[1]);

            int G2_X = Integer.parseInt(end2[0]);
            int G2_Y = Integer.parseInt(end2[1]);
            
            // Main maze matrix
            maze_matrix = new String[N][N];

            for (int i = 0; i < N; i++)
            {
                for (int j = 0; j < N; j++)
                {

                    int flip_sw = 0;    // check for S
                    int flip_g1 = 0;    // check for G1
                    int flip_g2 = 0;    // check for G2
                    boolean wall = rand.nextInt(p)==0;  //create random walls with user input

                    // Check if input is right
                    if (S_X - 1 == j && S_Y - 1 == i)
                    {
                    flip_sw = 1;
                    }

                    if (G1_X - 1 == j && G1_Y - 1 == i)
                    {
                    flip_g1 = 1;
                    if (flip_sw == flip_g1)
                    {
                            System.out.println("error: S same as G1");
                            System.exit(0);
                    }
                    }

                    if (G2_X - 1 == j && G2_Y - 1 == i)
                    {
                    flip_g2 = 1;
                    if (flip_sw == flip_g2)
                    {
                            System.out.println("error: S same as G2");
                            System.exit(0);
                    }
                    if (flip_g1 == flip_g2)
                    {
                            System.out.println("error: G1 same as G2");
                            System.exit(0);
                    }
                    }
                    
                    if (flip_sw == 0 && flip_g1 == 0 && flip_g2 == 0)
                    {
                
                        if (wall == true)
                        {
                            maze_matrix[i][j] = "W";          // 0 = wall 
                            System.out.print(maze_matrix[i][j]+"    ");
                        }
                        else
                        {
                            int val = rand.nextInt(4) + 1;
                            String val_s = String.valueOf(val);
                            maze_matrix[i][j] = val_s;
                            System.out.print(maze_matrix[i][j]+"    ");
                        }
                    }
                    // Place S,G1,G2
                    if (flip_sw == 1 && flip_g1 == 0 && flip_g2 == 0)
                    {
                        maze_matrix[i][j] = "S";
                        System.out.print(maze_matrix[i][j]+"    ");
                    }
                    if (flip_sw == 0 && flip_g1 == 1 && flip_g2 == 0)
                    {
                        maze_matrix[i][j] = "G1";
                        System.out.print(maze_matrix[i][j]+"   ");
                    }
                    if (flip_sw == 0 && flip_g1 == 0 && flip_g2 == 1)
                    {
                        maze_matrix[i][j] = "G2";
                        System.out.print(maze_matrix[i][j]+"   ");
                    }
                    
                }
                System.out.println();
                
            }
            System.out.println("===================================================================================");
            Uniform maingame = new Uniform(N);
            //maingame.FindChilds(maze_matrix,S_X,S_Y);
            maingame.UCS(maze_matrix,S_X,S_Y,G1_X,G1_Y,G2_X,G2_Y);

            Astar maingame2 = new Astar(N);
            maingame2.AstarSearch(maze_matrix, S_X, S_Y, G1_X, G1_Y, G2_X, G2_Y);
            
            scan.close();
        } catch (Exception e){
            System.out.println("Wrong input!");
        }
    }
}


class Node
{

    
    public Double mainCost;
    public String parent;
    public int x_place;
    public int y_place;
    public int parentX;
    public int parentY;
    public String currentPlace;
    public Double H_value;

    // Node Object for UCS 
    public Node (Double mainCost,String parent,int x_place, int y_place, int parentX, int parentY, String currentPlace) 
    {
        
        this.mainCost = mainCost;
        this.parent = parent;
        this.x_place = x_place;
        this.y_place = y_place;
        this.parentX = parentX;
        this.parentY = parentY;
        this.currentPlace = currentPlace;
    }

    // Node Object for A*
    public Node (Double mainCost,String parent,int x_place, int y_place, int parentX, int parentY, String currentPlace, Double H_value) 
    {
        
        this.mainCost = mainCost;
        this.parent = parent;
        this.x_place = x_place;
        this.y_place = y_place;
        this.parentX = parentX;
        this.parentY = parentY;
        this.currentPlace = currentPlace;
        this.H_value = H_value;
    }

}
