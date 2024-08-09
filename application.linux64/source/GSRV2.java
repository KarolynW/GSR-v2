import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import grafica.*; 
import controlP5.*; 
import processing.serial.*; 
import lord_of_galaxy.timing_utils.*; 
import static javax.swing.JOptionPane.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class GSRV2 extends PApplet {

/*
Created by:
  Karolyn Webb
On Date:
  11/05/2020 - Days of the Corona Lockdown 
Purpose:
  This program will graph GSR, sent from the
  microcontroller, through Serial with an On and Off button.

Function:

Plan:
*/

//~~~~~~~~
//~~~~~~~~~~~~~~~~
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~


//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Libraries
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~


 //import ControlP5 library





//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Variables and Objects
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Serial
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
Serial myPort;

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// CSV
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

Table table;

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Arduino Control
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
ControlP5 cp5; //create ControlP5 object

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Timer Variable
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
Stopwatch s;

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Data
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
PFont font;
PFont titleFont;
int lf = 10;
String val;
String value;
String valString;
int valInt;
int x, y; 
String z;
String Pp;
String filename;
int eventCounter;
String eventName;

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Graph
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
GPlot plot1;

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Setup
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

public void setup(){ 
 
  //Set Participant Number to None
  Pp = "Not Set";
    
  eventCounter=0;
  eventName ="None";
  
  //window size, (width, height)
  
 
  //Creating a stopwatch to keep time
  s = new Stopwatch(this);
  
 table= new Table();
 table.addColumn("Time in Seconds");
 table.addColumn("GSR (Resistance in Ohms)");

  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // Initialize Serial Port
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // List all the available serial ports
  
    String COMx, COMlist = "";
    final boolean debug = true;
/*
  Let the user select the com port.
*/
  try {
    if(debug) printArray(Serial.list());
    int i = Serial.list().length;
    if (i != 0) {
      if (i >= 2) {
        // need to check which port the inst uses -
        // for now we'll just let the user decide
        for (int j = 0; j < i;) {
          COMlist += PApplet.parseChar(j+'a') + " = " + Serial.list()[j];
          if (++j < i) COMlist += ",  ";
        }
        COMx = showInputDialog("Which COM port is correct? (a,b,..):\n"+COMlist);
        if (COMx == null) exit();
        if (COMx.isEmpty()) exit();
        i = PApplet.parseInt(COMx.toLowerCase().charAt(0) - 'a') + 1;
      }
      String portName = Serial.list()[i-1];
      if(debug) println(portName);
      myPort = new Serial(this, portName, 9600); // change baud rate to your liking
      myPort.bufferUntil('\n'); // buffer until CR/LF appears, but not required..
    }
    else {
      showMessageDialog(frame,"Device is not connected to the PC");
      exit();
    }
  }
  catch (Exception e)
  { //Print the type of error
    showMessageDialog(frame,"COM port is not available (may\nbe in use by another program)");
    println("Error:", e);
    exit();
  }

 
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // Create Buttons
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  //lets add buton to empty window
  
  cp5 = new ControlP5(this);
  titleFont = createFont("calibri", 34);
  font = createFont("calibri", 20);    // custom fonts for buttons and title
  
  cp5.addButton("on")      //"on" is the name of button
    .setPosition(50, 150)  //x and y coordinates of upper left corner of button
    .setSize(100, 70)      //(width, height)
    .setFont(font)
  ;   

  cp5.addButton("off")     //"off" is the name of button
    .setPosition(50, 225)  //x and y coordinates of upper left corner of button
    .setSize(100, 70)      //(width, height)
    .setFont(font)
  ;
  
   cp5.addButton("Participant")   //"Participant" is the name of button
    .setPosition(380, 150)  //x and y coordinates of upper left corner of button
    .setSize(150, 70)      //(width, height)
    .setFont(font)
  ;
cp5.addButton("AddEvent")   //"Participant" is the name of button
    .setPosition(625, 150)  //x and y coordinates of upper left corner of button
    .setSize(150, 70)      //(width, height)
    .setFont(font)
  ;
  
cp5.addButton("EventName")   //"Participant" is the name of button
    .setPosition(800, 150)  //x and y coordinates of upper left corner of button
    .setSize(150, 70)      //(width, height)
    .setFont(font)
  ;
  
cp5.addButton("SaveAndExit")   //"Participant" is the name of button
    .setPosition(625, 300)  //x and y coordinates of upper left corner of button
    .setSize(150, 70)      //(width, height)
    .setFont(font)
  ;

  valString="Off";        //Set Val to off before input.

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // Set Up Plot
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  plot1 = new GPlot(this);
  plot1.setPos(100,400);
  plot1.setDim(600, 400);
  plot1.getTitle().setText("GSR Vs. Time");
  plot1.getXAxis().getAxisLabel().setText("Time in Seconds");
  plot1.getYAxis().getAxisLabel().setText("GSR - Resistance in Ohms");
  plot1.setPointColor(color(255,0,0,255));
  plot1.setPointSize(2);
  plot1.activatePanning();
}

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Draw
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

public void draw(){

  background(255, 255 , 255); // background color of window (r, g, b) or (0 to 255)
  
  //lets give title to our window
  fill(0, 0, 0);               //text color (r, g, b)
  textFont(titleFont);
  text("GSR", 500, 30);  // ("text", x coordinate, y coordinat)

  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // Read Port
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // Is there a string in the port to read?
  
  while (myPort.available() > 0) {
    valString = myPort.readStringUntil(lf);
    if (valString != null) {
      try {
        valInt = Integer.parseInt(valString.trim());
        println(valInt);
      } catch (NumberFormatException npe){
       // Not an integer so forget it
      }
    } 

  x = (Integer.parseInt(str(s.time()))/1000);
  y = ((1024+2*valInt)*10000)/(512-valInt);
  
    {
      //add a new row for each value
      TableRow newRow = table.addRow();
      //place the new row and value under the "Data" column
      newRow.setInt("Time in Seconds", x);
      //place the new row and value under the "Data" column
      newRow.setInt("GSR (Resistance in Ohms)", y);
      //place the new row and value under the "Data" column
      newRow.setString("Event Name", eventName);
    }
  
  
  // Add the new point to the graph
  plot1.addPoint(x, y);
  }
 
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  // Draw Plot
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  plot1.beginDraw();
  plot1.drawBackground();
  plot1.drawBox();
  plot1.drawXAxis();
  plot1.drawYAxis();
  plot1.drawTitle();
  plot1.drawGridLines(GPlot.BOTH);
  plot1.drawLines();
  plot1.drawPoints();
  plot1.endDraw();
 
  textFont(font);
  text("Click to start and stop GSR measurement - double click \"off\" to turn off input", 100, 125);
  text("GSR (Ohms)", 200, 200);
  text(y, 200, 250);
  text("Participant Number:",350, 250);
  text(Pp, 525, 250);
  text("Current Event:", 625,250);
  text(eventName, 750,250);
  println(x,",",valString); //print it out in the console
  
}

//lets add some functions to our buttons
//so when you press any button, it sends perticular char over serial port

public void on(){
  myPort.write('x');
    //Start the stopwatch
  s.start();
}

public void off(){
  myPort.write('z');
  valString = "Off";
    //Start the stopwatch
  s.pause();
}

public void Participant(){
   
  Pp = showInputDialog("Please enter new Participant Number");
 
  if (Pp == null)   exit();
 
  else if ("".equals(Pp))
    showMessageDialog(null, "Empty Participant Number Input", 
    "Alert", ERROR_MESSAGE);
 
  else {
    showMessageDialog(null, "Participant Number \"" + Pp + "\" successfully added", 
    "Info", INFORMATION_MESSAGE);
 
  text(Pp, 550, 250);
  }
}
public void AddEvent()
{
      eventCounter=eventCounter+1;
      //add a new row for each value
      TableRow newRow = table.addRow();
      //place the new row and value under the "Data" column
      newRow.setString("Time in Seconds", "Event");
      //place the new row and value under the "Data" column
      newRow.setInt("GSR (Resistance)", eventCounter);
      //place the new row and value under the "Data" column
      newRow.setString("Event Name", eventName);
}

public void EventName(){
   
  eventName = showInputDialog("Please enter the name of the Event");
 
  if (eventName == null)   exit();
 
  else if ("".equals(eventName))
    showMessageDialog(null, "Empty Event Name", 
    "Alert", ERROR_MESSAGE);
 
  else {
    showMessageDialog(null, "Event Name \"" + eventName + "\" successfully added", 
    "Info", INFORMATION_MESSAGE);
 
  text(eventName, 550, 250);
  }
}

public void SaveAndExit()
{
  //variable as string under the data folder set 
  filename = "data/" + "Participant " + Pp + ".csv";
  //save as a table in csv format(data/table - data folder name table)
  saveTable(table, filename);
  exit();
}
  public void settings() {  size(1000, 1000); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "GSRV2" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
