import java.io.*;
import java.util.*;

// Room class
class Room {
    int roomId;
    String category;
    double price;
    boolean isBooked;

    public Room(int roomId, String category, double price) {
        this.roomId = roomId;
        this.category = category;
        this.price = price;
        this.isBooked = false;
    }

    @Override
    public String toString() {
        return "Room ID: " + roomId + " | Category: " + category + " | Price: " + price + " | Available: " + !isBooked;
    }
}

// Reservation class
class Reservation {
    int reservationId;
    String customerName;
    Room room;
    String checkInDate;
    String checkOutDate;
    String status;

    public Reservation(int reservationId, String customerName, Room room, String checkInDate, String checkOutDate) {
        this.reservationId = reservationId;
        this.customerName = customerName;
        this.room = room;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.status = "Confirmed";
    }

    @Override
    public String toString() {
        return "Reservation ID: " + reservationId +
                " | Customer: " + customerName +
                " | Room: " + room.category +
                " | Check-in: " + checkInDate +
                " | Check-out: " + checkOutDate +
                " | Status: " + status;
    }
}

// Hotel system
public class Hotel_Reservation {
    static List<Room> rooms = new ArrayList<>();
    static List<Reservation> reservations = new ArrayList<>();
    static Scanner sc = new Scanner(System.in);
    static int reservationCounter = 1;

    public static void main(String[] args) {
        loadRooms();
        loadReservations();

        while (true) {
            System.out.println("\n===== Hotel Reservation System =====");
            System.out.println("1. Search Available Rooms");
            System.out.println("2. Book a Room");
            System.out.println("3. Cancel Reservation");
            System.out.println("4. View Reservations");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    searchRooms();
                    break;
                case 2:
                    bookRoom();
                    break;
                case 3:
                    cancelReservation();
                    break;
                case 4:
                    viewReservations();
                    break;
                case 5:
                    saveReservations();
                    System.exit(0);
                default:
                    System.out.println("Invalid choice! Try again.");
            }
        }
    }

    // Load initial rooms
    static void loadRooms() {
        rooms.add(new Room(101, "Standard", 2000));
        rooms.add(new Room(102, "Standard", 2000));
        rooms.add(new Room(201, "Deluxe", 4000));
        rooms.add(new Room(202, "Deluxe", 4000));
        rooms.add(new Room(301, "Suite", 8000));
    }

    // Load reservations from file
    static void loadReservations() {
        try (BufferedReader br = new BufferedReader(new FileReader("reservations.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                int id = Integer.parseInt(data[0]);
                String customer = data[1];
                int roomId = Integer.parseInt(data[2]);
                String checkIn = data[3];
                String checkOut = data[4];
                String status = data[5];

                Room bookedRoom = findRoomById(roomId);
                if (bookedRoom != null)
                    bookedRoom.isBooked = true;

                Reservation r = new Reservation(id, customer, bookedRoom, checkIn, checkOut);
                r.status = status;
                reservations.add(r);
                reservationCounter = Math.max(reservationCounter, id + 1);
            }
        } catch (IOException e) {
            System.out.println("No previous reservations found.");
        }
    }

    // Save reservations to file
    static void saveReservations() {
        try (PrintWriter pw = new PrintWriter(new FileWriter("reservations.txt"))) {
            for (Reservation r : reservations) {
                pw.println(r.reservationId + "," + r.customerName + "," +
                        r.room.roomId + "," + r.checkInDate + "," +
                        r.checkOutDate + "," + r.status);
            }
            System.out.println("Reservations saved!");
        } catch (IOException e) {
            System.out.println("Error saving reservations.");
        }
    }

    // Search available rooms
    static void searchRooms() {
        System.out.print("Enter category (Standard/Deluxe/Suite): ");
        String category = sc.nextLine();
        System.out.println("Available " + category + " rooms:");
        for (Room r : rooms) {
            if (r.category.equalsIgnoreCase(category) && !r.isBooked) {
                System.out.println(r);
            }
        }
    }

    // Book a room
    static void bookRoom() {
        System.out.print("Enter your name: ");
        String name = sc.nextLine();
        System.out.print("Enter category (Standard/Deluxe/Suite): ");
        String category = sc.nextLine();
        System.out.print("Enter check-in date (dd-mm-yyyy): ");
        String checkIn = sc.nextLine();
        System.out.print("Enter check-out date (dd-mm-yyyy): ");
        String checkOut = sc.nextLine();

        Room availableRoom = null;
        for (Room r : rooms) {
            if (r.category.equalsIgnoreCase(category) && !r.isBooked) {
                availableRoom = r;
                break;
            }
        }

        if (availableRoom == null) {
            System.out.println("No rooms available in this category!");
            return;
        }

        // Simulate payment
        System.out.println("Room price: " + availableRoom.price);
        System.out.println("Processing payment...");
        System.out.println("Payment Successful!");

        availableRoom.isBooked = true;
        Reservation newRes = new Reservation(reservationCounter++, name, availableRoom, checkIn, checkOut);
        reservations.add(newRes);
        System.out.println("Booking Confirmed! Reservation ID: " + newRes.reservationId);
    }

    // Cancel reservation
    static void cancelReservation() {
        System.out.print("Enter Reservation ID to cancel: ");
        int id = sc.nextInt();
        sc.nextLine();

        for (Reservation r : reservations) {
            if (r.reservationId == id && r.status.equals("Confirmed")) {
                r.status = "Cancelled";
                r.room.isBooked = false;
                System.out.println("Reservation Cancelled!");
                return;
            }
        }
        System.out.println("Reservation not found or already cancelled.");
    }

    // View all reservations
    static void viewReservations() {
        if (reservations.isEmpty()) {
            System.out.println("No reservations found.");
            return;
        }
        for (Reservation r : reservations) {
            System.out.println(r);
        }
    }

    // Helper: find room by ID
    static Room findRoomById(int id) {
        for (Room r : rooms) {
            if (r.roomId == id)
                return r;
        }
        return null;
    }
}
