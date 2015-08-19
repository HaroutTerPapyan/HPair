/*Harout Ter-Papyan
 * HPair.java
 */

import java.util.Scanner;
import java.io.*;


public class HPair {
	
	public static void main(String[] args) throws IOException {
	      String source, destination;
	      Map aMap = new Map();
	      boolean found;
	      int from, to;
	      Scanner s = new Scanner(new File("requestFile.txt"));
	    
	      int count = s.nextInt();  
	      String eoln = s.nextLine();
	   
	      for (int i = 0; i < count; i ++)
	      {
	         source = s.nextLine();
	         destination = s.nextLine();
	      
	         from = aMap.getCityNumber(source);
	         to   = aMap.getCityNumber(destination);
	         if (from != -1 && to != -1)
	         {
	            found = aMap.isPath(from, to);
	            aMap.unvisitAll();
	            if (found)
	               System.out.println("HPAir flies from " 
	                   + source + " to " + destination);
	            else
	               System.out.println("Sorry. HPAir does not serve " 
	                   + source + " to " + destination);
	         }
	         else
	               System.out.println("Sorry. HPAir does not serve " 
	                   + source + " to " + destination);      } 
	   }
	
}




class LinkedNode {

   private Object data;     
   private LinkedNode next; 

   public LinkedNode() {
      this( null, null );
   }

   public LinkedNode( Object newData, LinkedNode newNext ) {
      data = newData;
      next = newNext;
   }

   public Object getData() {
      return data;
   }

   public LinkedNode getNext() {
      return next;
   }

   public void setData( Object newData ) {
      data = newData;
   }

   public void setNext( LinkedNode newNext ) {
      next = newNext;
   }

} 



class LinkedStack implements Stack {

   private LinkedNode top; 

   public LinkedStack() {
      top = null;  
   }

   public void push( Object data ) {
      LinkedNode newTop = new LinkedNode( data, top ); 
      top = newTop;
   }

   public void pop() {
      top = top.getNext();
   }
  
   public Object top()  {
      return top.getData();
   }

   public boolean empty() {
       
      return top == null;
   }

   public boolean full() {
       
      return false;
   }
} 



class Map {
   private String[]   cityNames;
   private String[][] flightRoutes;
   private String[][] customerRequests;
   private boolean[]  visited;

   public Map() 
   {
      readCityNames();
      readFlightRoutes();
      visited = new boolean[cityNames.length];
      for (int i = 0; i < visited.length; i ++)
         visited[i] = false;
   }

   public void readCityNames()
   {
      try
      {
         Scanner s = new Scanner(new File("cityFile.txt"));
         int size = s.nextInt();
         String eoln = s.nextLine();
      
         cityNames = new String[size];
         for (int i = 0; i < size; i ++)
            cityNames[i] = s.nextLine();
      }
      catch (IOException e)
      {
         System.err.println("IO error with cityFile.txt");
         System.exit(1);
      }
   }

   public void readFlightRoutes()
   {
      try
      {
         Scanner s = new Scanner(new File("flightFile.txt"));
         int size = s.nextInt();  
         String eoln = s.nextLine();
      
         flightRoutes = new String[size][2];
      
         String aCityName;
      
         for (int i = 0; i < size; i ++)
         {
            aCityName = s.nextLine();
            flightRoutes[i][0] = aCityName;  
            aCityName = s.nextLine();
            flightRoutes[i][1] = aCityName;  
         }
      }
      catch (IOException e)
      {
         System.err.println("IO error with flightFile.txt");
         System.exit(1);
      }
   
   }

   public void unvisitAll()
   {
      for (int i = 0; i < visited.length; i ++)
         visited[i] = false;
   }

   public void markVisited(int inCity)
   {
      visited[inCity] = true;
   }


   public boolean getNextCity(int inFromCity, NextCity outNextCity)
   {
      for (int i = 0; i < flightRoutes.length; i ++)
      {
         if ((flightRoutes[i][0]).compareTo
          (getCityName(inFromCity)) == 0)
         {
            int c = getCityNumber(flightRoutes[i][1]);
            if (visited[c] == false)
            {
               outNextCity.setCity(c);
               return true;
            }
         }
      }
   
      return false;
   }

   public int getCityNumber(String inName)
   {
      for (int i = 0; i < cityNames.length; i ++)
         if (inName.compareTo(cityNames[i]) == 0)
            return i;
      return -1;  
   }
   
   public String getCityName(int inNumber)
   {
      for (int i = 0; i < cityNames.length; i ++)
         if (i == inNumber)
            return cityNames[i];
      return "";  
   }

   public boolean isPath(int inOrig, int inDestiny)
   {
      LinkedStack aStack = new LinkedStack();
      int topCity, nextCity;
      Integer myTop;
      boolean success;
   
      unvisitAll();
    
      aStack.push(inOrig);
      markVisited(inOrig);
   
      myTop = (Integer)aStack.top();
      topCity = myTop.intValue();
   
      while (!aStack.empty() && topCity != inDestiny)
      {
      
         NextCity nextTemp = new NextCity();
         success = getNextCity(topCity, nextTemp);
      
         nextCity = nextTemp.getCity();
         if (!success)
            aStack.pop();
         else
         {
            aStack.push(new Integer(nextCity));
            markVisited(nextCity);
         }
      
         if (!aStack.empty())
         {
            myTop = (Integer)aStack.top();
            topCity = myTop.intValue();
         }
      }  
   
      if (aStack.empty())
         return false;
      else
         return true;
   }

   public boolean isPathR(int inOrig, int inDestiny)
   {
      int nextCity;
      boolean success, done;
   
      markVisited(inOrig);
    
      if (inOrig == inDestiny)
         return true;
      else
      {
         done = false;
         NextCity nextTemp = new NextCity();
         success = getNextCity(inOrig, nextTemp);
         nextCity = nextTemp.getCity();
      
         while (success && !done)
         {
            done = isPathR(nextCity, inDestiny);
            if (!done)
            {
               success = getNextCity(inOrig, nextTemp);
               nextCity = nextTemp.getCity();
            }
         }  
      }
    
      return done;
   }
}

class NextCity {

   private int city;

   public NextCity() {
   
      city = -1;
   }

   public NextCity(int cityIndex) {
   
      city = cityIndex;
   }

   public void setCity(int cityIndex) {
   
      city = cityIndex;
   }

   public int getCity() {
   
      return city;
   }

}

interface Stack {
   public void push( Object data );

   public void pop();
   
   public Object top();

   public boolean empty();

   public boolean full();

}