# cop4520-assignment-2
# Assignment 1 for COP4520
## Problem 1: Birthday Party
### Compilation Instructions:
1) Ensure you have Java 17 or higher installed
2) Download the code and locate BirthdayParty.java in your terminal
3) To compile, run the command
   > javac BirthdayParty.java
4) After compilation, you can execute the code by running
   > java BirthdayParty
5) This program will output a file called `primes.txt`, which will display the limit you used (set to 100 million by
   default), execution time, number of primes found, sum of all primes, and the 10 largest primes found.
6) If you want to change the value of n, simply change the variable named `NUM_GUESTS` that is located towards the start of
   BirthdayParty.java
### Code Explanation:
To solve the Minotaur's problem, I chose one guest to be a "counter" guest. This guest, in addition to its role of eating
the cupcake itself, would be responsible for counting the number of times that the cupcake had been eaten. More
specifically, when this counter guest would enter the labyrinth, it will first eat the cupcake if it needed to, and then
it will do one of two things: 1) if the cupcake is gone, increment its count by 1 and have the cupcake replaced or 2) if
the cupcake was still there, leave it be and leave the labyrinth. Once the counter counts up to the number of guests, it
will "notify" the minotaur that every guest has eaten.

For the other guests, when they enter the labyrinth, they will eat the cupcake if it is available and they
haven't already done so. They are not involved with counting nor replacing the cupcake.

For correctness, efficiency, and proof of correctness:

I've implemented an array called `hasEatenCupcake` that keeps track of who has eaten a cupcake. There are
checks to ensure that no guest has eaten more than one cupcake. In addition, the counter counts how many guests have eaten
a cupcake. If no guest has eaten more than one cupcake, and `NUM_GUESTS` guests have eaten a cupcake, it is safe to say
that every guest has had a cupcake. The function `runGuestThroughLabyrinth` is synchronized, meaning that it enables 
mutual exclusion to ensure that only one guest visits the labyrinth at a time.

Each guest will enter the labyrinth only if the Minotaur has selected them. Because the Minotaur selects guests
at random, it is hard to say how efficient this algorithm is. The Minotaur could theoretically avoid the counter guest
(i.e the guest responsible for replacing the cupcake) for as long as it wanted to. As far as I'm aware, this is the most
efficient solution, and the program does state its execution time at the end of every run.


## Problem 2: Crystal Vase
### Compilation Instructions:
1) Ensure you have Java 17 or higher installed
2) Download the code and locate CrystalVase.java in your terminal
3) To compile, run the command
   > javac CrystalVase.java
4) After compilation, you can execute the code by running
   > java CrystalVase
5) If you want to change the value of n, simply change the variable named `NUM_GUESTS` that is located towards the start of
   CrystalVase.java
### Code Explanation:
Advantages and disadvantages of each solution:
* Solution 1: An advantage to this solution would be the simplicity of it. However, this comes at the cost of creating a
large crowd outside the showroom. In addition, there is no guarantee that a particular guest would be able to see the vase
or when. It doesn't seem as if there is a mechanism implemented to ensure that only one guest is in the room at a time.
* Solution 2: An advantage to this solution would be the simplicity of it. In addition, there would be no crowd outside
the room from leaving the door open and guests will know whether they can enter. One disadvantage to this solution is that
there is no way to ensure the order in which guests enter.
* Solution 3: An advantage to this solution would be a guarantee that every guest can visit and that only one guest visits
at a time. There is also an order to the queue. 
However, guests are allowed multiple visits so we do not know how long it will take every guest to visit the vase.
In addition, this requires every guest to stand in a line.

After looking at the 3 solutions, I've decided to implement Solution 2. This, I believe, is the best solution because we
know when guests enter/leave, guests only enter once, and we do not care if the guests enter in a particular order.

For correctness, efficiency, and proof of correctness:

I've implemented an array called `hasSeenVase` that keeps track of who has seen the vase. There are checks to ensure that
no guest has seen the vase on multiple occasions. In addition, each thread will only end once it has seen the vase. As such,
we can be sure that every guest has seen the vase before the end of the program's execution, and as such the solution is
correct. The function `guestVisitsVase` is synchronized, meaning that it enables mutual exclusion to ensure that only
one guest visits the vase at a time.

The runtime of this program is O(n) where n is the number of guests. This is the most efficient solution and the program
states its execution time at the very end to ensure of this.

