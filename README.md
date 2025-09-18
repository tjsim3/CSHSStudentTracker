# CSHSStudentTracker
Tracks student's hours, and cording requirements for MVHS CSHS
How it works:
Type in the number of the function you would like to perform. Here is what each one does
1. Add record; Simply adds a student record to a database
2. View records; Displays all records for all students
3. Edit record; Edits a record for a specific student
4. Search for record; Searches for students name and displays their record
5. Delete record; Deletes a student record
6. Graduation check; returns true if the student can graduate, returns leftover requirements if not
7. Save and exit; saves and ends the program

## How to Run

1. Make sure you have **Java JDK** installed.
2. Download this repository as ZIP and extract it.
3. Double-click `run.bat` to compile and run the program.
4. Add, view, edit, or search records in the database.



TO RUN (if run.bat does not work)
Open windows powershell (shift right-click inside folder, open with windows powershell) and run this command
java -cp ".;.\gson-2.10.1.jar" main

For mac/linux use 
java -cp ".:gson-2.10.1.jar" main
