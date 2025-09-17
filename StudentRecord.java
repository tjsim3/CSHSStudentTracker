public class StudentRecord {
    private String name;
    private int hours;
    private int codingChallenges;
    private int guestSpeakers;

    public StudentRecord(String name, int hours, int codingChallenges, int guestSpeakers) {
        this.name = name;
        this.hours = hours;
        this.codingChallenges = codingChallenges;
        this.guestSpeakers = guestSpeakers;
    }

    public String getName() { return name; }
    public int getHours() { return hours; }
    public int getCodingChallenges() { return codingChallenges; }
    public int getGuestSpeakers() { return guestSpeakers; }

    public void setHours(int hours) { this.hours = hours; }
    public void setCodingChallenges(int codingChallenges) { this.codingChallenges = codingChallenges; }
    public void setGuestSpeakers(int guestSpeakers) { this.guestSpeakers = guestSpeakers; }

    @Override
    public String toString() {
        return name + " | Hours: " + hours +
               " | Coding Challenges: " + codingChallenges +
               " | Guest Speakers: " + guestSpeakers;
    }
}
