// Judy Li
// Lecture Sec 01
import java.util.*;
import java.io.*;
import java.awt.geom.Line2D;

class Main {
  public static void main(String[] args) {
    int numLines = 0;
    int order = 0;
    //get the number of lines in unput
    try{
      Scanner read = new Scanner(new File("input.txt"));
      order = read.nextInt(); 
      while(read.hasNext()) {
        numLines++;
        String trash = read.nextLine();
      }
    } catch(FileNotFoundException fnf) {
      System.out.println("File was not found.");
    }
    //store input values into line
    double[][] line = new double[numLines-1][4];
    try{
      Scanner read = new Scanner(new File("input.txt"));
      order = read.nextInt(); 
      int i = 0;
      while(read.hasNext()) {
        double x1 = read.nextDouble();
        double y1 = read.nextDouble();
        double x2 = read.nextDouble();
        double y2 = read.nextDouble();
        line[i] = new double[]{x1, y1, x2, y2};
        i++;
      }
    } catch(FileNotFoundException fnf) {
      System.out.println("File was not found.");
    }
    //Compare sierpinski segment(3 lines, 4pts) to every every line in input
    List<Integer> pat = pattern(order);
    double[] segment;
    double[] beg = new double[]{0,0};
    boolean[] tf = new boolean[numLines-1];
    //build sierpinski segment
    for(int i = 0; i < pat.size(); i++) {
      if (pat.get(i) == 1) {
        segment = up(beg);
      } else if (pat.get(i) == 2) {
        segment = right(beg);
      } else if (pat.get(i) == 3){
        segment = left(beg);
      } else if (pat.get(i) == 4) {
        segment = left2(beg);
      } else if (pat.get(i) == 5){
        segment = right2(beg);
      } else {
        segment = up2(beg);
      }
      beg[0] = segment[6];
      beg[1] = segment[7];
      //compare segment to every line until true or reached end
      for (int j = 0; j < numLines-1; j++) {
        if (intersect(line[j], segment)) {
          tf[j] = true;
        }
      }
    }
    //Output 0 or 1 into output.txt
    try{
      PrintWriter writer = new PrintWriter("output.txt");
      for (int i = 0; i < numLines - 1; i++) {
        if (tf[i]) {
          if(i == numLines -2){
            writer.print(1);
          } else {
            writer.println(1);
          }
        } else {
          if (i == numLines -2){
            writer.print(0);
          } else {
            writer.println(0);
          }
        }
      }
      writer.close();
    } catch(FileNotFoundException fnf){
      System.out.println("File was not found.");
    }
  }


  //returns true if given line and segment intersects
  public static boolean intersect(double[] line, double[] tri) {
    Line2D line1 = new Line2D.Double(line[0], line[1], line[2], line[3]);
    boolean result = line1.intersectsLine(tri[0], tri[1], tri[2], tri[3]);
    if (result) {
      return true;
    }
    result = line1.intersectsLine(tri[2], tri[3], tri[4], tri[5]);
    if (result) {
      return true;
    }
    result = line1.intersectsLine(tri[4], tri[5], tri[6], tri[7]);
    if (result) {
      return true;
    }
    return false; 
  }


  //Returns list of numbers that represent the orientation of each segment
  public static List<Integer> pattern(int order) {
    //base case
    if (order == 1){
      return new ArrayList<Integer>(1);
    }

    List<Integer> mid = new ArrayList<Integer>();
    List<Integer> pat = new ArrayList<Integer>();
    //1-up, 2-right, 3-left, 4-left2, 5-right2
    mid.add(1);
    for(int i = 2; i <= order; i++) {
      // first triangle
      List<Integer> first = new ArrayList<Integer>(mid);
      List<Integer> index2 = new ArrayList<Integer>();
      List<Integer> index4 = new ArrayList<Integer>();
      List<Integer> index6 = new ArrayList<Integer>();
      //storing all index that have a 2 and 4
      for (int j = 0; j < first.size(); j++) {
        if (first.get(j) == 2) {
          index2.add(j);
        }
        else if (first.get(j) == 4) {
          index4.add(j);
        }
        else if (first.get(j) == 6) {
          index6.add(j);
        }
      }
      Collections.replaceAll(first, 1, 2);
      Collections.replaceAll(first, 3, 4);
      Collections.replaceAll(first, 5, 6);
      //convert 2 to 1
      for (int j = 0; j < index2.size(); j++) {
        int ind1 = index2.get(j);
        first.set(ind1, 1);
      }
      //convert all 4 to 3
      for (int j = 0; j < index4.size(); j++) {
        int ind3 = index4.get(j);
        first.set(ind3, 3);
      }
      //convert
      for (int j = 0; j < index6.size(); j++) {
        int ind6 = index6.get(j);
        first.set(ind6, 5);
      }
      //last triangle
      List<Integer> last = new ArrayList<Integer>(mid);
      List<Integer> index1 = new ArrayList<Integer>();
      index2.clear();
      index6.clear();
      for (int j = 0; j < last.size(); j++) {
        if (last.get(j) == 1) {
          index1.add(j);
        }
        else if (last.get(j) == 2) {
          index2.add(j);
        }
        else if (last.get(j) == 6) {
          index6.add(j);
        }
      }
      Collections.replaceAll(last, 4, 6);
      Collections.replaceAll(last, 3, 1);
      Collections.replaceAll(last, 5, 2);
      //1->3
      for (int j = 0; j < index1.size(); j++) {
        int ind = index1.get(j);
        last.set(ind, 3);
      }
      //2->5
      for (int j = 0; j < index2.size(); j++) {
        int ind = index2.get(j);
        last.set(ind, 5);
      }
      //6->4
      for (int j = 0; j < index6.size(); j++) {
        int ind = index6.get(j);
        last.set(ind, 4);
      }
      //store into pat
      pat.clear();
      pat.addAll(first);
      pat.addAll(mid);
      pat.addAll(last);
      //middle now has the entire pattern of the previous order
      mid = new ArrayList<Integer>(pat);
    }
    return pat;
  }

  //3 lines, facing to the right
  public static double[] right(double[] beg) {
    double[] arr = new double[8];
    //first pt
    arr[0] = beg[0];
    arr[1] = beg[1];
    //second 
    arr[2] = arr[0] + 1;
    arr[3] = arr[1];
    for(int i = 2; i < 4; i++) {
      if (arr[i] < 2.220446049250313E-10) {
        arr[i] = 0;
      }
    }
    //third
    arr[4] = arr[2] + Math.cos(60*(Math.PI/180));
    arr[5] = arr[3] + Math.sin(60*(Math.PI/180));
    for(int i = 4; i < 6; i++) {
      if (arr[i] < 2.220446049250313E-10) {
        arr[i] = 0;
      }
    }
    //fourth
    arr[6] = arr[2];
    arr[7] = arr[5] + Math.sin(60*(Math.PI/180));
    for(int i = 6; i < arr.length; i++) {
      if (arr[i] < 2.220446049250313E-10) {
        arr[i] = 0;
      }
    }
    return arr;
  }

  public static double[] right2(double[] beg) {
    double[] arr = new double[8];
    //first pt
    arr[0] = beg[0];
    arr[1] = beg[1];
    //second
    arr[2] = arr[0] + Math.cos(60*(Math.PI/180));
    arr[3] = arr[1] - Math.sin(60*(Math.PI/180));
    for(int i = 2; i < 4; i++) {
      if (arr[i] < 2.220446049250313E-10) {
        arr[i] = 0;
      }
    }
    //third 
    arr[4] = arr[2] - Math.cos(60*(Math.PI/180));
    arr[5] = arr[1] - (2*Math.sin(60*(Math.PI/180)));
    for(int i = 4; i < 6; i++) {
      if (arr[i] < 2.220446049250313E-10) {
        arr[i] = 0;
      }
    }
    //fourth 
    arr[6] = arr[4] - 1;
    arr[7] = arr[5];
    for(int i = 6; i < arr.length; i++) {
      if (arr[i] < 2.220446049250313E-10) {
        arr[i] = 0;
      }
    }
    return arr;
  }

  //3 lines, facing upwards
  public static double[] up(double[] beg) {
    double[] arr = new double[8];
    //first pt
    arr[0] = beg[0];
    arr[1] = beg[1];
    //second
    arr[2] = arr[0] + Math.cos(60*(Math.PI/180));
    arr[3] = arr[1] + Math.sin(60*(Math.PI/180));
    for(int i = 2; i < 4; i++) {
      if (arr[i] < 2.220446049250313E-10) {
        arr[i] = 0;
      }
    }
    //third 
    arr[4] = arr[2] + 1;
    arr[5] = arr[3];
    for(int i = 4; i < 6; i++) {
      if (arr[i] < 2.220446049250313E-10) {
        arr[i] = 0;
      }
    }
    //fourth 
    arr[6] = arr[4] + Math.cos(60*(Math.PI/180));
    arr[7] = arr[5] - Math.sin(60*(Math.PI/180));

    for(int i = 6; i < arr.length; i++) {
      if (arr[i] < 2.220446049250313E-10) {
        arr[i] = 0;
      }
    }
    return arr;
  }

  public static double[] up2(double[] beg) {
    double[] arr = new double[8];
    //first pt
    arr[0] = beg[0];
    arr[1] = beg[1];
    //second
    arr[2] = arr[0] - Math.cos(60*(Math.PI/180));
    arr[3] = arr[1] + Math.sin(60*(Math.PI/180));
    for(int i = 2; i < 4; i++) {
      if (arr[i] < 2.220446049250313E-10) {
        arr[i] = 0;
      }
    }
    //third 
    arr[4] = arr[2] - 1;
    arr[5] = arr[3];
    for(int i = 4; i < 6; i++) {
      if (arr[i] < 2.220446049250313E-10) {
        arr[i] = 0;
      }
    }
    //fourth 
    arr[6] = arr[4] - Math.cos(60*(Math.PI/180));
    arr[7] = arr[5] - Math.sin(60*(Math.PI/180));

    for(int i = 6; i < arr.length; i++) {
      if (arr[i] < 2.220446049250313E-10) {
        arr[i] = 0;
      }
    }
    return arr;
  }

  //3 lines facing left
  public static double[] left(double[] beg) {
    double[] arr = new double[8];
    //first pt
    arr[0] = beg[0];
    arr[1] = beg[1];
    //second
    arr[2] = arr[0] - Math.cos(60*(Math.PI/180));
    arr[3] = arr[1] - Math.sin(60*(Math.PI/180));
    for(int i = 2; i < 4; i++) {
      if (arr[i] < 2.220446049250313E-10) {
        arr[i] = 0;
      }
    }
    //third 
    arr[4] = arr[2] + Math.cos(60*(Math.PI/180));
    arr[5] = arr[3] - Math.sin(60*(Math.PI/180));
    for(int i = 4; i < 6; i++) {
      if (arr[i] < 2.220446049250313E-10) {
        arr[i] = 0;
      }
    }
    //fourth
    arr[6] = arr[4] + 1;
    arr[7] = arr[5];
    for(int i = 6; i < arr.length; i++) {
      if (arr[i] < 2.220446049250313E-10) {
        arr[i] = 0;
      }
    }
    return arr;
  }

  public static double[] left2(double[] beg) {
    double[] arr = new double[8];
    //first pt
    arr[0] = beg[0];
    arr[1] = beg[1];
    //second
    arr[2] = arr[0] - 1;
    arr[3] = arr[1];
    for(int i = 2; i < 4; i++) {
      if (arr[i] < 2.220446049250313E-10) {
        arr[i] = 0;
      }
    }
    //third
    arr[4] = arr[2] - Math.cos(60*(Math.PI/180));
    arr[5] = arr[3] + Math.sin(60*(Math.PI/180));
    for(int i = 4; i < 6; i++) {
      if (arr[i] < 2.220446049250313E-10) {
        arr[i] = 0;
      }
    }
    //fourth
    arr[6] = arr[4] + Math.cos(60*(Math.PI/180));
    arr[7] = arr[5] + Math.sin(60*(Math.PI/180));

    for(int i = 6; i < arr.length; i++) {
      if (arr[i] < 2.220446049250313E-10) {
        arr[i] = 0;
      }
    }
    return arr;
  }
}