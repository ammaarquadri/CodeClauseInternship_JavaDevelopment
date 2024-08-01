import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Movie {
    private String title;
    private List<Seat> seats;

    public Movie(String title, int numberOfSeats) {
        this.title = title;
        this.seats = new ArrayList<>();
        for (int i = 1; i <= numberOfSeats; i++) {
            seats.add(new Seat(i));
        }
    }

    public String getTitle() {
        return title;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public void displayAvailableSeats() {
        System.out.println("Available seats for " + title + ":");
        for (Seat seat : seats) {
            if (!seat.isBooked()) {
                System.out.print(seat.getSeatNumber() + " ");
            }
        }
        System.out.println();
    }

    public boolean bookSeat(int seatNumber) {
        for (Seat seat : seats) {
            if (seat.getSeatNumber() == seatNumber && !seat.isBooked()) {
                seat.book();
                return true;
            }
        }
        return false;
    }
}

class Seat {
    private int seatNumber;
    private boolean booked;

    public Seat(int seatNumber) {
        this.seatNumber = seatNumber;
        this.booked = false;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public boolean isBooked() {
        return booked;
    }

    public void book() {
        booked = true;
    }
}

class BookingSystem {
    private List<Movie> movies;

    public BookingSystem() {
        movies = new ArrayList<>();
        movies.add(new Movie("Avengers-Endgame", 10));
        movies.add(new Movie("Inception", 10));
        movies.add(new Movie("Interstellar", 10));
    }

    public void displayMovies() {
        System.out.println("Available movies:");
        for (int i = 0; i < movies.size(); i++) {
            System.out.println((i + 1) + ". " + movies.get(i).getTitle());
        }
    }

    public Movie selectMovie(int movieIndex) {
        if (movieIndex > 0 && movieIndex <= movies.size()) {
            return movies.get(movieIndex - 1);
        } else {
            System.out.println("Invalid movie selection.");
            return null;
        }
    }
}

public class MovieTicketBookingSystem {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        BookingSystem bookingSystem = new BookingSystem();

        while (true) {
            System.out.println("\n*** Movie Ticket Booking System ***");
            bookingSystem.displayMovies();
            System.out.print("Select a movie by entering its number (or 0 to exit): ");
            int movieChoice = scanner.nextInt();

            if (movieChoice == 0) {
                System.out.println("Exiting the system. Goodbye!");
                break;
            }

            Movie selectedMovie = bookingSystem.selectMovie(movieChoice);
            if (selectedMovie == null) continue;

            selectedMovie.displayAvailableSeats();
            System.out.print("Enter the seat number you want to book: ");
            int seatChoice = scanner.nextInt();

            if (selectedMovie.bookSeat(seatChoice)) {
                System.out.println("Seat booked successfully!");
                System.out.println("Movie: " + selectedMovie.getTitle());
                System.out.println("Seat Number: " + seatChoice);
            } else {
                System.out.println("Seat already booked or invalid selection.");
            }
        }

        scanner.close();
    }
}
