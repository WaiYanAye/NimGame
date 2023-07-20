import java.util.ArrayList;
import java.io.File;
import java.io.FileWriter;
import java.util.Collections;
import java.util.Scanner;
import java.io.IOException;

public class NimGame {
  private static int stickLeft;
  private static int maxRemove;

  public static int learn(int stickLeft,ArrayList<ArrayList<Integer>> data) //learrning the matrix column
   {
    ArrayList<Integer> col = new ArrayList<Integer>();
    for(int i=1;i<4;i++){                             
      col.add(data.get(stickLeft).get(i)); 
    }
    Integer max = Collections.max(col);         
    int index = col.indexOf(max)+1;
    return index;
  }

  public static String buildData(ArrayList<ArrayList<Integer>> data) {    //learning the matrix row
    String result = "";
    for (int i = 0; i < 4; i++) {
      for (ArrayList<Integer> j : data) {
        result += Integer.toString(j.get(i));
        if (j.get(i) < 0 || j.get(i) > 9) result += " ";
        else result += "  ";
      }
      result += "\n";
    }
    return result;
  }

  public static void main(String[] args) {
    
    ArrayList<ArrayList<Integer>> data = new ArrayList<ArrayList<Integer>>(); 
    try {
      File file = new File("data.txt");
      if (file.createNewFile()) {
        ArrayList<ArrayList<Integer>> oriData = new ArrayList<ArrayList<Integer>>();  
        ArrayList<Integer> col = new ArrayList<Integer>();
        for (int i = 0; i < 4; i++) {
          col.add(i);                       
        }
        oriData.add(col);
        for (int i = 1; i <= 10; i++) {
          col = new ArrayList<Integer>();
          col.add(i);
          for (int j = 0; j < 3; j++) {
            col.add(0);
          }
          oriData.add(col);
        }
        FileWriter writer = new FileWriter(file);
        writer.write(buildData(oriData));
        writer.close();
        data = oriData;
      } else {
        Scanner sc = new Scanner(file);
        for (int i = 0; i <= 10; i++) {
          ArrayList<Integer> col = new ArrayList<Integer>();
          data.add(col);
        }
        int i = 0;
        int j;
        while (sc.hasNext()) {
          if (i == 11) {
            i = 0;
          }
          j = sc.nextInt();
          data.get(i).add(j);
          i++;
        }
        sc.close();
      }
          
      System.out.println("Welcome! You are training computer to learn Nimgame");
      System.out.println("There are 10 sticks. You will complete againts the computer.");
      System.out.println("Each turn, either player must take between 1 and 3.");
      System.out.println("The one who take the last stick win. The computer goes first.");

      boolean restart = true;
      Scanner sc = new Scanner(System.in);
      while (restart) {
        stickLeft = 10;
        maxRemove = 3;
        System.out.println("-----------------------------\n"+"Game Begin!\n -----------------------------");
        System.out.println("There are (" + stickLeft + ") sticks avaliable.");

        ArrayList<Integer> computer = new ArrayList<>();
        ArrayList<Integer> human = new ArrayList<>();
        ArrayList<Integer> sticks = new ArrayList<>();
      

        boolean result = true;
        while (stickLeft > 0) {
          sticks.add(stickLeft);
          computer.add(learn(stickLeft, data));
          stickLeft -= learn(stickLeft,data);
          if (stickLeft == 0) {
            System.out.println("You Lose the game...");
            result = true;
            break;
          }

          System.out.println("Computer picked (" + computer.get(computer.size()-1)+ ") , there are (" + stickLeft + ") sticks left.");
          System.out.println("Remember to choose between (1 to 3)");
          System.out.print("Your turn: ");
          int input = sc.nextInt();

          while (input > stickLeft || input > maxRemove || input <= 0) {
            System.out.println("Try agian!");
            input = sc.nextInt();
          }

     
          sticks.add(stickLeft);
          human.add(input);
          stickLeft -= input;
          if (stickLeft == 0) {
            System.out.println("Congratulation! You win!!");
            result = false;
            break;
          }
          System.out.println(" There are " + stickLeft + " sticks left.");
        }

        int n = 0;
        for (int i = 0; i < sticks.size(); i++) {
          if (i % 2 == 0) {
            if(result){
              // -> array form: data[computer[i][sticks[i]]++;
              data.get(sticks.get(i)).set(computer.get(i-n), data.get(sticks.get(i)).get(computer.get(i-n))+1);
            }else{
              data.get(sticks.get(i)).set(computer.get(i-n), data.get(sticks.get(i)).get(computer.get(i-n))-1);
            }
          } else {
            if(result){
              data.get(sticks.get(i)).set(human.get(i-n), data.get(sticks.get(i)).get(human.get(i-n))-1);
            }else{
              data.get(sticks.get(i)).set(human.get(i-n), data.get(sticks.get(i)).get(human.get(i-n))+1);
            }
          }
          n++;
        }
                
        file.delete();
        file.createNewFile();
        FileWriter writer = new FileWriter(file);
        writer.write(buildData(data));
        writer.close();
        System.out.println("Restart again?(Type 'y' or 'n')");
        String reset = sc.next();
        if(!reset.equalsIgnoreCase("y")){
          restart = false;
        }
      }

      sc.close();
    } catch (IOException e) {
      System.out.println("An error occured during runtime");
      e.printStackTrace();
    }
  }
}
