import java.util.ArrayList;
import java.util.Scanner;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.OutputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

public class main {
    static ArrayList<StudentRecord> database = new ArrayList<>();
    static Scanner scanner = new Scanner(System.in);
    static final String FIREBASE_URL = "https://cshsdatabase-default-rtdb.firebaseio.com/students.json";
    static Gson gson = new Gson();

    public static void main(String[] args) {
        loadFromFirebase();  // 🔥 load from Firebase instead of CSV

        while (true) {
            System.out.println("\n1. Add Record\n2. View Records\n3. Edit Record\n4. Search for Record\n5. Delete Record\n6. Check Graduation Requirements\n7. Save & Exit");
            System.out.print("Choose an option: ");
            
            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }

            switch (choice) {
                case 1:
                    addRecord();
                    break;
                case 2:
                    viewRecords();
                    break;
                case 3:
                    editRecord();
                    break;
                case 4:
                    searchAndDisplayRecord();
                    break;
                case 5:
                    deleteRecord();
                    break;
                case 6:
                    graduationCheck();
                    break;
                case 7:
                    saveToFirebase();  // 🔥 save to Firebase instead of CSV
                    System.out.println("Goodbye!");
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    static void addRecord() {
        System.out.print("Enter name: ");
        String name = scanner.nextLine();
        System.out.print("Enter hours: ");
        int hours = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter number of coding challenges: ");
        int codingChallenges = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter number of guest speakers: ");
        int guestSpeakers = Integer.parseInt(scanner.nextLine());

        database.add(new StudentRecord(name, hours, codingChallenges, guestSpeakers));
        System.out.println("Record added.");
    }
    
    static void deleteRecord() {
        System.out.print("Enter name: ");
        String name = scanner.nextLine();
        boolean removed = false;

        for (int i = 0; i < database.size(); i++) {
            if (database.get(i).getName().equalsIgnoreCase(name)) {
                database.remove(i);
                removed = true;
                System.out.println("Record removed.");
                break;
            }
        }

        if (!removed) {
            System.out.println("Student not found.");
        }
    }

    static void viewRecords() {
        if (database.isEmpty()) {
            System.out.println("No records found.");
        } else {
            for (StudentRecord record : database) {
                System.out.println(record);
            }
        }
    }
    
    static void graduationCheck() {
        System.out.print("Enter the name of the student: ");
        String name = scanner.nextLine();
        StudentRecord record = searchRecord(name);

        if (record == null) {
            System.out.println("Student not found.");
            return;
        }

        boolean meetsHours = record.getHours() >= 10;
        boolean meetsSpeakers = record.getGuestSpeakers() >= 2;
        boolean meetsChallenges = record.getCodingChallenges() >= 1;

        if (meetsHours && meetsSpeakers && meetsChallenges) {
            System.out.println(record.getName() + " meets graduation requirements.");
        } else {
            System.out.println(record.getName() + " does NOT meet graduation requirements.");
            if(!meetsHours){
                System.out.println(record.getName() + " needs " + (10-record.getHours()) + " more hours.");
            }
            if(!meetsSpeakers){
                System.out.println(record.getName() + " needs to see " + (2-record.getGuestSpeakers()) + " more guest speaker(s).");
            }
            if(!meetsChallenges){
                System.out.println(record.getName() + " has not participated in a coding challenge.");
            }
        }
    }

    static void editRecord() {
        System.out.print("Enter the name of the student to edit: ");
        String name = scanner.nextLine();
        boolean found = false;

        for (StudentRecord record : database) {
            if (record.getName().equalsIgnoreCase(name)) {
                found = true;
                System.out.println("Editing record for: " + record.getName());

                System.out.println("Current hours: " + record.getHours());
                System.out.print("Enter new hours (or -1 to keep the same): ");
                int newHours = Integer.parseInt(scanner.nextLine());
                if (newHours >= 0) {
                    record.setHours(newHours);
                }

                System.out.println("Current coding challenges: " + record.getCodingChallenges());
                System.out.print("Enter new coding challenges (or -1 to keep the same): ");
                int newChallenges = Integer.parseInt(scanner.nextLine());
                if (newChallenges >= 0) {
                    record.setCodingChallenges(newChallenges);
                }

                System.out.println("Current guest speakers: " + record.getGuestSpeakers());
                System.out.print("Enter new guest speakers (or -1 to keep the same): ");
                int newSpeakers = Integer.parseInt(scanner.nextLine());
                if (newSpeakers >= 0) {
                    record.setGuestSpeakers(newSpeakers);
                }

                System.out.println("Record updated.");
                break;
            }
        }

        if (!found) {
            System.out.println("Student not found.");
        }
    }
    
    static StudentRecord searchRecord(String name) {
        for (StudentRecord record : database) {
            if (record.getName().equalsIgnoreCase(name)) {
                return record;
            }
        }
        return null;
    }

    static void searchAndDisplayRecord() {
        System.out.print("Enter the name of the student: ");
        String name = scanner.nextLine();
        StudentRecord found = searchRecord(name);
        if (found != null) {
            System.out.println(found);
        } else {
            System.out.println("Student not found.");
        }
    }

    static void saveToFirebase() {
        try {
            URL url = new URL(FIREBASE_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("PUT");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            String json = gson.toJson(database);

            try (OutputStream os = conn.getOutputStream()) {
                os.write(json.getBytes());
            }

            conn.getInputStream().close();
            System.out.println("Data saved to Firebase.");
        } catch (Exception e) {
            System.out.println("Error saving to Firebase: " + e.getMessage());
        }
    }

    static void loadFromFirebase() {
        try {
            URL url = new URL(FIREBASE_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            in.close();

            String json = response.toString();
            if (!json.equals("null")) {
                Type listType = new TypeToken<ArrayList<StudentRecord>>(){}.getType();
                database = gson.fromJson(json, listType);
                System.out.println("Data loaded from Firebase.");
            } else {
                System.out.println("No data found in Firebase.");
            }
        } catch (Exception e) {
            System.out.println("Error loading from Firebase: " + e.getMessage());
        }
    }
}
