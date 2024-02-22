public class CrystalVase {
  public static final int NUM_GUESTS = 10;
  public static boolean[] hasSeenVase = new boolean[NUM_GUESTS];
  public static int nextGuest = -1;

  public static void main(String[] args) throws InterruptedException {
    // Ensure proper NUM_GUESTS value
    if (NUM_GUESTS <= 0) {
      System.out.println("Please choose a valid number of guests...");
      System.exit(1);
    }
    // Create showroom object (shared object among threads w/ mutual exclusion)
    Showroom showroom = new Showroom();
    // Create and start guest threads
    VaseGuestThread[] guests = new VaseGuestThread[NUM_GUESTS];
    final long executionStartTime = System.currentTimeMillis();
    for (int i = 0; i < NUM_GUESTS; i++) {
      guests[i] = new VaseGuestThread(i, showroom);
      guests[i].start();
    }
    // Wait for all the guests to "arrive" before allowing guests into showroom
    showroom.makeRoomAvailable();
    for (int i = 0; i < NUM_GUESTS; i++) {
      guests[i].join();
    }
    if (showroom.getNumberOfGuestsVisited() != NUM_GUESTS) {
      System.out.println("Error: Number of guests visited does not equal number of guests!");
      System.exit(1);
    }
    final long executionEndTime = System.currentTimeMillis();
    System.out.println("Done!");
    System.out.println("Total execution time: " + (executionEndTime - executionStartTime) + "ms");
  }

}

class Showroom {
  private boolean isRoomAvailable;
  private int numberOfGuestsVisited;

  public Showroom() {
    this.isRoomAvailable = false;
    this.numberOfGuestsVisited = 0;
  }

  public synchronized void guestVisitsVase(VaseGuestThread guest) {
    this.makeRoomUnavailable();
    int guestIndex = guest.getGuestIndex();
    System.out.println("Guest " + guestIndex + " has visited the vase!");
    if (CrystalVase.hasSeenVase[guest.getGuestIndex()]) {
      System.out.println("Error: guest " + guestIndex + " has already seen the vase!");
      System.exit(1);
    } else {
      CrystalVase.hasSeenVase[guest.getGuestIndex()] = true;
    }
    this.numberOfGuestsVisited++;
    this.makeRoomAvailable();
  }

  public synchronized boolean roomIsAvailable() {return this.isRoomAvailable;}
  public void makeRoomAvailable() {this.isRoomAvailable = true;}
  public synchronized void makeRoomUnavailable() {this.isRoomAvailable = false;}
  public int getNumberOfGuestsVisited() {return this.numberOfGuestsVisited;}
}


class VaseGuestThread extends Thread {
  private boolean hasVisited;
  private int guestIndex;
  private Showroom showroom;

  public VaseGuestThread(int guestIndex, Showroom showroom) {
    this.guestIndex = guestIndex;
    this.hasVisited = false;
    this.showroom = showroom;
  }

  @Override
  public void run() {
    while (true) {
      if (showroom.roomIsAvailable()) {
        showroom.guestVisitsVase(this);
        break;
      }
    }
  }

  public boolean isHasVisited() {
    return hasVisited;
  }

  public void setHasVisited(boolean hasVisited) {
    this.hasVisited = hasVisited;
  }

  public int getGuestIndex() {
    return guestIndex;
  }

  public void setGuestIndex(int guestIndex) {
    this.guestIndex = guestIndex;
  }
}
