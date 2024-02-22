public class BirthdayParty {
  public static final int NUM_GUESTS = 100;
  public static boolean[] hasEatenCupcake = new boolean[NUM_GUESTS];
  public static int selectedGuest = -1;

  public static void main(String[] args) throws InterruptedException {
    // Ensure proper NUM_GUESTS value
    if (NUM_GUESTS <= 0) {
      System.out.println("Please choose a valid number of guests...");
      System.exit(1);
    }
    // Create labyrinth object (shared object among threads w/ mutual exclusion)
    Labyrinth labyrinth = new Labyrinth();
    // Create and start guest threads (where the first guest is designated as a counter)
    Thread[] guests = new Thread[NUM_GUESTS];
    final long executionStartTime = System.currentTimeMillis();
    for (int i = 0; i < NUM_GUESTS; i++) {
      guests[i] = new GuestThread(i, labyrinth);
      guests[i].start();
    }

    // Now minotaur will continue selecting guests until all have eaten a cupcake
    while (!labyrinth.hasEveryGuestEaten()) {
      selectedGuest = (int) (Math.random() * BirthdayParty.NUM_GUESTS);
    }
    // Once every guest has eaten a cupcake, kill the threads
    for (int i = 0; i < NUM_GUESTS; i++) {
      guests[i].join();
    }
    final long executionEndTime = System.currentTimeMillis();
    System.out.println("Done!");
    System.out.println("Total execution time: " + (executionEndTime - executionStartTime) + "ms");
  }
}

class Labyrinth {
  private boolean cupcakeIsAvailable;
  private int numCupcakesEaten; // Only changed by the counter guest

  public Labyrinth() {
    this.cupcakeIsAvailable = true;
    this.numCupcakesEaten = 0;
  }

  public synchronized void runGuestThroughLabyrinth(GuestThread guest) {
    if (!guest.isFull() && this.cupcakeIsAvailable) {
      this.eatCupcake();
      guest.setToFull();
      System.out.println("Guest " + guest.getGuestIndex() + " has eaten a cupcake!");
      // This is to check for accuracy of algorithm
      if (BirthdayParty.hasEatenCupcake[guest.getGuestIndex()]) {
        System.out.println("Error: Guest has already eaten a cupcake!");
        System.exit(1);
      }
      BirthdayParty.hasEatenCupcake[guest.getGuestIndex()] = true;
    }
    // If this is the counter guest, check to see if cupcake has been eaten (and replace when necessary)
    if (guest.getGuestIndex() == 0) {
      if (!cupcakeIsAvailable) {
        this.replaceCupcakeAndUpdateCount();
      }
    }
  }

  public synchronized void eatCupcake() {this.cupcakeIsAvailable = false;}
  public synchronized void replaceCupcakeAndUpdateCount() {
    this.cupcakeIsAvailable = true;
    this.numCupcakesEaten++;
  }
  public synchronized int getNumCupcakesEaten() {return this.numCupcakesEaten;}
  public synchronized boolean hasEveryGuestEaten() {return (this.numCupcakesEaten >= BirthdayParty.NUM_GUESTS);}
}

class GuestThread extends Thread {
  private final int guestIndex;
  private final Labyrinth labyrinth;
  private boolean isFull;

  public GuestThread(int guestIndex, Labyrinth labyrinth) {
    this.guestIndex = guestIndex;
    this.isFull = false;
    this.labyrinth = labyrinth;
  }

  @Override
  public void run() {
    while (labyrinth.getNumCupcakesEaten() < BirthdayParty.NUM_GUESTS) {
      // If this is the selected guest, go through labyrinth
      if (BirthdayParty.selectedGuest == this.guestIndex) {
        labyrinth.runGuestThroughLabyrinth(this);
      }
    }
  }

  public int getGuestIndex() {
    return guestIndex;
  }

  public boolean isFull() {
    return isFull;
  }

  public void setToFull() {
    this.isFull = true;
  }
}
