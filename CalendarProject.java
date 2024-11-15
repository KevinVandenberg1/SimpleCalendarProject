import java.util.Calendar;
import java.util.Scanner;
import java.util.regex.*;
import java.io.File;
import java.io.*;
public class CalendarProject {
   public static String[][] events = new String[13][];
   // Main method
   
   public static void main(String[] args) {
      int[] months = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31, 29};
      event(months);
      // Creating objects for the Calendar and Scanner
      Calendar MyCalendar = Calendar.getInstance();
      Scanner Input = new Scanner(System.in);
      int day = MyCalendar.get(Calendar.DATE);
      int year = MyCalendar.get(Calendar.YEAR);
      int month = 0;
      int offset1 = offSetCalc(year);
      int UserDay = -600, UserMonth = -600, UserYear = -600, offset = -600;
      
      boolean apple = true; // placeholder
      
      while (apple) {
         displayMenu();
         String input = "";
         while (true) {
            try {
               input = (Input.nextLine().trim().toUpperCase());
               break;
            }
            catch (Exception e) {
               System.out.println("Select a valid command");
            }
         }
         switch (input) {
            case "E": {
               month = 0;
               System.out.printf("%n%n%nWhat date would you want to view? (MM/DD/YYYY)%n");
               while (true) {
                  while (true) {
                     String UserDate = Input.nextLine().trim();
                     UserDay = dayFromDate(UserDate);
                     UserMonth = monthFromDate(UserDate);
                     UserYear = yearFromDate(UserDate);
                     offset = offSetCalc(UserYear);
                     if (UserDay != -5 && UserMonth != -5 && UserYear != -5 && UserDay != 0 && UserMonth != 0 && UserYear != 0) {
                        break;
                     } else {
                     System.out.println("Please follow the format (MM/DD/YYYY):");
                     }
                  }
                  boolean correctDay = false, correctMonth = false;
                  int counter = 0;
                  for (int i : months) {
                     if (UserMonth >= 1 && UserMonth <=12) {
                        correctMonth = true;
                        
                     } 
                     if (i>=UserDay) {
                        correctDay = true;
                     }
                  }
                  if (correctMonth == false) {
                     System.out.println("Input a valid month.");
                  } else if (correctDay == false) { 
                     System.out.println("Please input a valid day for a month.");
                  } else {
                  break;
                  }
               }
               callMethods(UserMonth, UserDay, UserYear, offset);
               
               while (true) {
                  System.out.println("Please enter a command.");
                  System.out.println("\"n\" to get the next months calendar.");
                  System.out.println("\"p\" to get the previous months calendar.");
                  System.out.println("\"q\" to return to the menu");
                  System.out.println("\"fp\" to print the current calendar");
                  String input2 = (Input.nextLine().trim().toUpperCase());
                  if (input2.startsWith("Q")) {
                     break;
                  } else if (input2.startsWith("N")) {
                     UserMonth = UserMonth + 1;
                     if (UserMonth == 13) {
                     UserMonth = 1;
                     UserYear = UserYear + 1;
                     offset = offSetCalc(UserYear);
                     }
                     callMethods(UserMonth, UserDay, UserYear, offset);
                  } else if (input2.startsWith("P")) {
                     UserMonth = UserMonth - 1;
                     if (UserMonth == 0) { 
                        UserMonth = 12;
                        UserYear = UserYear - 1;
                        offset = offSetCalc(UserYear);
                     }
                     callMethods(UserMonth, UserDay, UserYear, offset);
                  }else if (input2.startsWith("FP")) {
                     printToFile(UserMonth, UserDay, UserYear, offset, Input);
                  }else {
                  System.out.println("Input a valid option");
                  }
               }
            break;
            }
            case "T": { // Draws the first calendar
               month = (MyCalendar.get(Calendar.MONTH)+1);
               callMethods(month, day, year, offset1);
               System.out.printf("\n");
            break;
            }
            case "N": {
               if (month!=0){
                  month = month + 1;
                  if (month >12) { 
                     month = month - 12;
                     year = year + 1; 
                  }
                  callMethods(month, day, year, offset1);
               } else {
                  System.out.println("Please select a calendar before trying to select the next month");
               }
               try {
                 Thread.sleep(1000);
               } catch (Exception e) {}
            break;
            }
            case "P": {
               if (month != 0) {
                  month = month - 1;
                  if (month < 1) { 
                     month = month + 12;
                     year = year - 1; 
                  }
                  callMethods(month, day, year, offset1);
               } else {
                  System.out.println("Please select a calendar before trying to view a previous month.");
               }
               try {
                  Thread.sleep(1000);
               } catch (Exception e) {}
            break;
            }
            case "Q": { // Quits the program
               apple = false;
               break;
            }
            case "FP": {
               if (month != 0) {
                  printToFile(month, day, year, offset1, Input);
                  break;
               } else {
               System.out.println("Select a calendar before trying to print the calendar");
               break;
               }
            }
            case "EV": {
               addEvent(Input);
               break;
            }
            default: {
            System.out.println("Select a valid command");
            try {
            Thread.sleep(1000);
            } catch (Exception e) {}
            }
            
         }
         System.out.printf("\n\n\n\n\n\n");
         
      }    
   } 
   public static void addEvent(Scanner Input) {
      System.out.println("To add an event, follow the following format:");
      System.out.println("MM/DD [event_name]");
      while (true) {
         try {
            String userInput = Input.nextLine();
            File file = new File("calendarEvents.txt");
            if (!file.exists()) {
               System.out.println("calendarEvents.txt file does not exist. Create one before trying to add an event");
               break;
            }
            if (userInput.contains("/") && userInput.contains(" ") && userInput.indexOf("/") == 2 && userInput.indexOf(" ") == 5) {
               try (PrintStream writer = new PrintStream(new FileOutputStream(file, true))){
                  writer.println("");
                  writer.print(userInput);
                  writer.flush();
                  Scanner reader = new Scanner(file);
                  while (reader.hasNextLine()) {
                     String currentLine = reader.nextLine();
                     String[] splitted = currentLine.split(" ");
                     String[] dayMonth = splitted[0].split("/");
                     int[] apples = {0, 0};
                     for (int i = 0; i < dayMonth.length; i++) {
                        apples[i] = Integer.valueOf(dayMonth[i]);
                     }
                     if (apples[0] == 2 && apples[1] == 29){
                        events[12][28] = splitted[1];
                     } else {
                        events[apples[0]-1][apples[1]-1] = splitted[1];
                     }
                  }
                  System.out.println("Event added to calendar");
                } catch(Exception e) {
                  System.out.println("File not found. You need a file called \"calendarEvents.txt\"");
                }
                break;
             } else {
               System.out.println("Follow the format: MM/DD [event_name]");
             }
             
             
             
         } catch (Exception e) {
             System.out.println("Follow the format MM/DD [event_name]");
         }
      }
      

   }
   public static void printToFile(int month, int day, int year, int offset1, Scanner Input) {
      PrintStream originalPrintStream = System.out;
      System.out.println("Where would you like to print the calendar to?");
      File file = new File("apple");
      boolean correct = false;
         do {
             String location = Input.nextLine();
             if (location.endsWith(".txt")) {
                file = new File(location);
                correct = true;
             } else {
                System.out.println("The file must be a .txt file");
             }
         } while (correct == false);
            try {
                PrintStream filePrintStream = new PrintStream(new FileOutputStream(file));
                System.setOut(filePrintStream);
                callMethods(month,  day, year, offset1);
                System.setOut(originalPrintStream);
            } catch (Exception e) {
                System.out.println("Error when printing calendar to file");
            }
   
   }
   //{31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31, 29}
   // method to setup the initial array with all the events in the calendarevents.txt file
   public static void event (int[] months) {
      int counter = 0;
      for (int i = 0; i < months.length; i++) {
         events[counter] = new String[months[i]];
         counter++;
      }
      File file = new File("calendarEvents.txt");
      try {
         if (file.exists()) {
            Scanner reader = new Scanner(file);
            while (reader.hasNextLine()) {
               String currentLine = reader.nextLine();
               String[] splitted = currentLine.split(" ");
               String[] dayMonth = splitted[0].split("/");
               int[] apples = {0, 0};
               for (int i = 0; i < dayMonth.length; i++) {
                  apples[i] = Integer.valueOf(dayMonth[i]);
               }
               if (apples[0] == 2 && apples[1] == 29){
                  events[12][28] = splitted[1];
               } else {
               events[apples[0]-1][apples[1]-1] = splitted[1];
               }
            }
            
         }
      } catch (Exception e) {
      
      }
      
      /*for (int i = 0; i < events.length; i++) {
         for (int z = 0; z < events[i].length; z++) {
             System.out.println(events[i][z]);      
         }
      */
      
   }
   public static void callMethods(int month, int day, int year, int offset) {
      askiiArt();
      displayDate(month, day, 1, year);
      drawMonth(month, offset, year, day);
      displayDate(month, day, 2, year);
   }
   
   
   // Displays the menu
   public static void displayMenu() {
      System.out.println("Type a command");
      System.out.println("\"e\" to choose a date and see the calendar for that date.");
      System.out.println("\"t\" to get todays date and calendar.");
      System.out.println("\"n\" to get next months calendar.");
      System.out.println("\"p\" to get the previous months calendar.");
      System.out.println("\"q\" to quit the program");
      System.out.println("\"fp\" to print the current calendar");
      System.out.println("\"ev\" to add an event to the calendar");
   }
   
   // Displays the date 
   public static void displayDate(int month, int day, int variant, int year) {
      switch(month) {
         case 1: {
         if (variant == 1) printMonth("January");
         else printMonthDay("January", day, year);
         break;
         }
         case 2: {
         if (variant == 1) printMonth("February");
         else printMonthDay("February", day, year);
         break;
         }
         case 3: {
         if (variant == 1) printMonth("March");
         else printMonthDay("March", day, year);
         break;
         }
         case 4: {
         if (variant == 1) printMonth("April");
         else printMonthDay("April", day, year);
         break;
         }
         case 5: {
         if (variant == 1) printMonth("May");
         else printMonthDay("May", day, year);
         break;
         }
         case 6: {
         if (variant == 1) printMonth("June");
         else printMonthDay("June", day, year);
         break;
         }
         case 7: {
         if (variant == 1) printMonth("July");
         else printMonthDay("July", day, year);
         break;
         }
         case 8: {
         if (variant == 1) printMonth("August");
         else printMonthDay("August", day, year);
         break;
         }
         case 9: {
         if (variant == 1) printMonth("September");
         else printMonthDay("September", day, year);
         break;
         }
         case 10: {
         if (variant == 1) printMonth("October");
         else printMonthDay("October", day, year);
         break;
         }
         case 11: {
         if (variant == 1) printMonth("November");
         else printMonthDay("November", day, year);
         break;
         }
         case 12: {
         if (variant == 1) printMonth("December");
         else printMonthDay("December", day, year);
         break;
         }
      }
   }
   // Prints the month at the top of the calendar
   public static void printMonth(String month) {
      int ScaleFactor = 1;
      int Length = 70 * ScaleFactor;
      int PreMidpoint = (int) (Math.floor(Length/2));
      int MonthLength = (int) (Math.floor((month.length())/2));
      int Midpoint = PreMidpoint - MonthLength;
      for (int i=1; i<=Midpoint; i++) {
         System.out.print(" ");
      }
      System.out.printf("%s%n", month);
   }
   // Prints the Month and day underneath the calendar
   public static void printMonthDay(String month, int day, int year) {
      System.out.printf("Month: %s%n", month);
      if (day == -5) {
      } else { 
         System.out.printf("Day: %s%n", day);
      }
      System.out.printf("Year: %s%n", year);
   }
   public static void askiiArt() {
      topOfHouse();
      bottomOfHouse();
      System.out.printf("%n%n%n%n");
   }
   // Draws the top of the house for my askiiArt
   public static void topOfHouse() {
      for (int i=1; i<=25; i++) System.out.print(" ");
      for (int i=1; i<=20; i++) System.out.print("_");
      System.out.println("");
      for (int i=0; i<5; i++) {
         for(int x = 1; x<(25-i); x++) System.out.print(" ");
         System.out.print("/");
         for(int x = 1; x<(21+i+i); x++) {
            if(i==4) System.out.print("_");
            else System.out.print(" ");
         }
         System.out.println("\\");
      }
   }
   // Draws the bottom of my house in the askiiArt
   public static void bottomOfHouse() {
      for(int x = 1; x<=7; x++) {
         for(int i = 1; i<=20; i++) System.out.print(" ");
         System.out.print("|");
         if (x == 2) {
            for(int i = 1; i<=18; i++) System.out.print(" ");
            for(int i = 1; i<=5; i++) System.out.print("_");
            for(int i = 1; i<=5; i++) System.out.print(" ");
         } else if (x==3 || x==4) {
            for(int i = 1; i<=17; i++) System.out.print(" ");
            System.out.print("|");
            
            if (x==3) for(int i = 1; i<=5; i++) System.out.print(" ");
            else for(int i = 1; i<=5; i++) System.out.print("_");
            System.out.print("|");
            for(int i = 1; i<=4; i++) System.out.print(" ");
         } else if (x==6 ||x==7) {
            if (x==6) {
            for (int i = 1; i<=9; i++) System.out.print(" ");
            System.out.print("_");
            for(int i = 1; i<=18; i++) System.out.print(" ");
            } else {
            for (int i = 1; i<=8; i++) System.out.print(" ");
            System.out.print("|.|");
            for(int i = 1; i<=17; i++) System.out.print(" ");
            }
         }
         else {
            for(int i = 1; i<=28; i++) System.out.print(" ");
         }
         System.out.println("|");
      }
      for(int i = 1; i<=20; i++) System.out.print(" ");
      System.out.print("|");
      for (int i = 1; i<=8; i++) System.out.print("_");
      System.out.print("|_|");
      for(int i = 1; i<=17; i++) System.out.print("_");
      System.out.println("|");
   }
   
   
  /*    _________________
       /                 \
      /                   \
     /                     \
    /                       \
   /                         \
  /___________________________\
   |                         |
   |               _____     |
   |              |     |    |
   |              |_____|    |
   |                         |
   |         _               |
   |        |.|              |
   |________|_|______________|
  
  Should look similar to this.
   */
   
   
   // ONLY ACCURATE AFTER YEAR 1753
   // This method determines the day that a year begins on, which it includes each potential leap year.
   public static int offSetCalc(int Year) {
      double Offset = 7; 
      double z = Math.floor(Year/400); 
      double x = Math.floor(Year/100); 
      double c = Math.floor((Year+3)/4); 
      double NetOffset = Offset+z-x+c+Year;
      double FinalOffset = (NetOffset%7);
      
      if(FinalOffset == 0) {
         FinalOffset = FinalOffset + 7;
      }
      
      int FOffset = (int)FinalOffset; 
      return FOffset; 
   }
   
   // This method calls on other methods as well as hands them values to draw the month
   public static void drawMonth(int month, int offset, int year, int day) {
      boolean LeapYear = (isLeapYear(year));
      int Jan = 31, Mar = 31, Apr = 30, May = 31, Jun = 30, Jul = 31, Aug = 31, Sep = 30, Oct = 31, Nov = 30, Dec = 31;
      int Feb = 0;
      if(LeapYear == true) { 
         Feb = 29;
      } else {
      Feb = 28;
      }
      switch(month) {
         case 1: {
            drawRowLoop(offset, Dec, Jan, day, month, year);
            drawLine(1);
            break;
         }   
         case 2: {
            offset = offset + 3;
            if(LeapYear == true) {
               month = 13;
            }
            drawRowLoop(offset, Jan, Feb, day,month, year);
            drawLine(1);
            break;
         }   
         case 3: {
            if(LeapYear == true) offset = offset + 4;
            else offset = offset + 3;
            drawRowLoop(offset, Feb, Mar, day, month, year);
            drawLine(1);
            break;
         }   
         case 4: {
            if(LeapYear == true) offset = offset;
            else offset = offset + 6;
            drawRowLoop(offset, Mar, Apr, day,month, year);
            drawLine(1);
            break;
         }   
         case 5: {
            if(LeapYear == true) offset = offset + 2;
            else offset = offset + 1;
            drawRowLoop(offset, Apr, May, day,month, year);
            drawLine(1);
            break;
         }   
         case 6: {
            if(LeapYear == true) offset = offset + 5;
            else offset = offset + 4;
            drawRowLoop(offset, May, Jun, day,month, year);
            drawLine(1);
            break;
         }   
         case 7: {
            if(LeapYear == true) offset = offset;
            else offset = offset + 6;
            drawRowLoop(offset, Jun, Jul, day,month, year);
            drawLine(1);
            break;
         }   
         case 8: {
            if(LeapYear == true) offset = offset + 3;
            else offset = offset + 2;
            drawRowLoop(offset, Jul, Aug, day,month, year);
            drawLine(1);
            break;
         }   
         case 9: {
            if(LeapYear == true) offset = offset + 6;
            else offset = offset + 5;
            drawRowLoop(offset, Aug, Sep, day,month, year);
            drawLine(1);
            break;
         }   
         case 10: {
            if(LeapYear == true) offset = offset + 1;
            else offset = offset;
            drawRowLoop(offset, Sep, Oct, day,month, year);
            drawLine(1);
            break;
         }   
         case 11: {
            if(LeapYear == true) offset = offset + 4;
            else offset = offset + 3;
            drawRowLoop(offset, Oct, Nov, day,month, year);
            drawLine(1);
            break;
         }   
         case 12: {
            if(LeapYear == true) offset = offset + 6;
            else offset = offset + 5;
            drawRowLoop(offset, Nov, Dec, day,month, year);
            drawLine(1);
            break;
         }   
      }
   
   }
   // Loops through the draw row method after being passed the values from the Draw Month method.
   public static void drawRowLoop(int offset, int PrevMDay, int CMFDay, int day, int month, int year) {
      double c = Math.floor(offset/7);
      int z = (int) c;
      if ((offset%7)==(0)) offset=7;
      else offset= offset - (z*7);
      for(int dayCounter = 1; dayCounter <= CMFDay+offset; dayCounter = dayCounter+7) {
         drawRow(dayCounter, offset, PrevMDay, CMFDay, day, month, year);
      }
      
      
   }
   // This checks if its a leap year or not.
   public static boolean isLeapYear(int Year) {
      if (Year%400 == 0) return true;
      else if (Year%100 == 0) return false;
      else if (Year%4 == 0) return true;
      else return false;
   }
   
   // Draws a row of the month
   public static void drawRow(int day, int offset, int PrevMDay, int CMFDay, int MDay, int month, int year) {
      if (isLeapYear(year)) {
         int counter = 0;
         for (String i : events[1]) {
            events[12][counter] = i;
            counter++;
         }
      }
      int scaleFactor = 1; // Probably wont use
      int FDay = day - offset+1; // FDay is the counter used to mark what day it is on right now. I dont know why I used FDay and not CDay or something.
      boolean done = true;
      int Current = FDay;
      if (FDay <= 0) {
         FDay = PrevMDay + FDay;
         done = false;
      }
      drawLine(scaleFactor);
         // Draws the row with the numbers (Bit more complicated)
         for (int x = 1; x<=7; x++) {
            if ((FDay == (PrevMDay + 1)) && done == false) {
               FDay = day;
               done = true;
            }
            if ((FDay >= CMFDay + 1) && done == true) {
               FDay = 1;
            }
            if(FDay >= 10) { // If the current day is 10, it reduces the spaces by 1
               if (FDay == MDay && done == true) {
                  System.out.printf("|%d%s      ", FDay, "*"); // Prints a * if its the current day selected
               } else {
                  System.out.printf("|%d       ", FDay); // Doesnt otherwise
               }
            } else if (FDay == MDay && done == true) {
               System.out.printf("|%d%s       ", FDay, "*"); // Prints a * if its the current day selected
            }
            else System.out.printf("|%d        ", FDay); // Doesnt otherwise
            FDay++;
            if(x==7) System.out.println("|");
         }
         
         
         
         
         int idk = Current;
         int idk2 = month;
         // Draws bars + events
         for(int z = 1; z<=3; z++) {
            boolean e = false;
            if (Current <= 0 && e == false) {
               Current = PrevMDay+Current;
               month = month-1;
               e = true;
            }
            for(int x =1; x<=7; x++) {
            if (Current > PrevMDay && e == true) {
               Current = 1;
               month = month + 1;
            } else if (Current > CMFDay && e == false) {
               Current = 1;
               month = month + 1;
               if (month == 13) {
               month = 1;
               }
            }
            if (month == 0) {
               month = 12;
            }
            if (month == 2 && isLeapYear(year)) {
               month = 13;
            }
            if (month == 14) {
            month = 3;
            }
            if (events[month-1][Current-1] != null) {
               String event = (events[month-1][Current-1]).replace("_", "");
               if (event.length() > 9 && z == 1) {
                  String zebra = event.substring(0, 9);
                  System.out.print("|" + zebra);
               } else if (event.length() < 9 && z == 1) {
                  System.out.printf("|%s", event);
                  for (int o = event.length(); o < 9; o++) {
                  System.out.print(" ");
                  }
               } else if (event.length() > 9 && event.length() < 18 && z == 2) {
                  String zebra = event.substring(9, event.length());
                  System.out.print("|" + zebra);
                  for (int o = event.length(); o < 18; o++) {
                     System.out.print(" ");
                  }
               } else if (event.length() > 9 && event.length() > 18 && z == 2) {
                  String zebra = event.substring(9, 18);
                  System.out.print("|" + zebra);
               } else if (event.length() > 18 && z == 3) {
                  String zebra = event.substring(18, event.length());
                  System.out.print("|" + zebra);
                  for (int o = event.length(); o < 27; o++) {
                     System.out.print(" ");
                  }
               } else {
                  System.out.print("|         ");
               }
               
            } else {
               System.out.print("|         ");
            }
            
            Current++;
            }
            System.out.println("|");
            Current = idk;
            month = idk2;
         }
   }
   // Draws a line
   public static void drawLine(int scaleFactor) {
      for (int x = scaleFactor * 70; x >= 0; x--) {
         System.out.print("_");
      }
      System.out.println("");
   }
   
   

   
   // Determines the month from the date that is inputted.
   public static int monthFromDate(String date) {
      try {
         int SlashLocation = date.indexOf("/");
         String MonthLocation = date.substring(0,SlashLocation);
         int x = Integer.parseInt(MonthLocation);
         return x;
      } catch (Exception e) {
         return -5;
      }
   
   
   }
   
   // Determines the day from the date that is inputted
   public static int dayFromDate(String date) {
      try {
         int SlashLocation = date.indexOf("/");
         int secondSlashLocation = date.indexOf("/", SlashLocation+1);
         String DayLocation = date.substring(SlashLocation+1,secondSlashLocation);
         int x = Integer.parseInt(DayLocation);
         return x;
      } catch (Exception e) {
      return -5;
      }
   }
   // Determines the year from the date that is inputted.
   public static int yearFromDate(String date) {
      try {
         int SlashLocation = date.indexOf("/");
         int secondSlashLocation = date.indexOf("/", SlashLocation+1);
         // Testing to see if the second part isnt necessary
         String YearLocation = date.substring(secondSlashLocation+1);
         int x = Integer.parseInt(YearLocation, 10);
         return x;
      } catch (Exception e) {
      return -5;
      }
   }
}
