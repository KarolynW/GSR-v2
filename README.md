# **GSR V2 - Galvanic Skin Response Monitoring System**  
A **Processing-based** application to monitor, visualise, and log **GSR (Galvanic Skin Response)** data in real time.  
Designed for use with **Arduino microcontrollers** during experiments.

---

## **Features**  
- **Real-Time Data Visualisation** - Graphs GSR data dynamically using the **grafica** library.  
- **Event Marking** - Allows adding events and labels during data collection.  
- **Participant Management** - Enables assigning participant numbers for data logging.  
- **CSV Logging** - Saves data, including timestamps and event markers, into structured CSV files.  
- **Interactive Controls** - Start, stop, and mark events with an intuitive interface.  
- **Serial Communication** - Reads GSR data through serial communication with the Arduino.  

---

## **Installation**  

### **1. Download Processing:**  
- Install **Processing IDE** from [processing.org](https://processing.org/).

### **2. Install Required Libraries:**  
- Open Processing IDE and go to **Sketch > Import Library > Add Library...**  
- Search and install the following libraries:  
  - **grafica** (for graphing).  
  - **controlP5** (for UI controls).  
  - **processing.serial** (for serial communication).  
  - **timing_utils** (for timing utilities).  

### **3. Connect Hardware:**  
- Connect the Arduino with a GSR sensor via the appropriate COM port.  
- Ensure the correct baud rate (9600) is set in the code.  

---

## **Usage**  

### **1. Start the Application:**  
- Open the sketch in Processing IDE and run it.  

### **2. Select COM Port:**  
- Choose the correct COM port when prompted.  
- Ensure the Arduino is connected and sending GSR data.  

### **3. Interactive Controls:**  

| Button         | Description                                           |
|----------------|-------------------------------------------------------|
| **ON**         | Starts GSR monitoring and data logging.               |
| **OFF**        | Stops GSR monitoring and pauses data logging.         |
| **Participant**| Assigns participant numbers for data logging.         |
| **AddEvent**   | Adds event markers to the data stream.                |
| **EventName**  | Assigns names to specific events for later analysis.  |
| **SaveAndExit**| Saves data to a CSV file and exits the application.    |

---

## **Data Logging Format (CSV):**  

| Time in Seconds | GSR (Resistance in Ohms) | Event Name |
|-----------------|--------------------------|------------|
| 1               | 520                      | Baseline   |
| 2               | 515                      | Start Task |
| 3               | 530                      | End Task   |

- Logs data in the `data/` folder with filenames based on participant IDs (e.g., `Participant_1.csv`).  

---

## **Code Overview:**  

### **Graphing (grafica):**  
- Dynamically plots GSR data against time.  
- Supports zooming, panning, and real-time updates.  

### **UI Controls (controlP5):**  
- Buttons for user interaction, including data start/stop and event marking.  

### **Serial Communication:**  
- Reads data through COM ports and buffers until newlines (`
`).  
- Handles errors and displays prompts for COM port selection.  

### **Event Marking:**  
- Allows adding event markers with timestamps and custom labels.  
- Facilitates analysis by separating phases of the experiment.  

---

## **Example Workflow:**  
1. Connect the Arduino and GSR sensor to the computer.  
2. Start the application and select the correct COM port.  
3. Enter participant details and initiate data collection.  
4. Add event markers during data logging.  
5. Save data at the end of the session as a CSV file.  

---

## **License**  
This project is licensed under the **MIT License**.


