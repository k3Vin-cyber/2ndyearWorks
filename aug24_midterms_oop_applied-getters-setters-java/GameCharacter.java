public class GameCharacter {
    private String heroName;
    private int healthPoints;

    // Constructor to initialize the character
    public GameCharacter(String heroName, int healthPoints) {
        this.heroName = heroName;
        this.healthPoints = healthPoints;
    }

    // Getter for Hero Name
    public String getHeroName() {
        return heroName;
    }

    // Getter for HP
    public int getHealthPoints() {
        return healthPoints;
    }

    // Setter for HP with real-world game logic
    public void setHealthPoints(int healthPoints) {
        // Prevent health from dropping below zero
        if (healthPoints < 0) {
            this.healthPoints = 0; 
        } else {
            this.healthPoints = healthPoints;
        }
    }

    public static void main(String[] args) {
        // 1. Initializing the object
        GameCharacter player = new GameCharacter("Esmeralda", 2500);

        System.out.println("Match started!");
        // Using getters to display initial stats
        System.out.println("Hero: " + player.getHeroName() + " | HP: " + player.getHealthPoints());

        System.out.println("\n--- Event: Taking Damage ---");
        int incomingDamage = 800;
        System.out.println(player.getHeroName() + " is hit for " + incomingDamage + " damage!");

        // 2. MAKING USE of the getter to calculate the new value, then the setter to update it
        int currentHP = player.getHealthPoints();
        player.setHealthPoints(currentHP - incomingDamage);

        // Using getter to verify the new state
        System.out.println("Remaining HP: " + player.getHealthPoints());

        System.out.println("\n--- Event: Taking Massive Damage ---");
        int massiveDamage = 3000;
        System.out.println(player.getHeroName() + " takes a massive hit of " + massiveDamage + " damage!");

        // 3. Making use of the setter's built-in safety logic
        // Even if the math results in a negative number, the setter catches it.
        player.setHealthPoints(player.getHealthPoints() - massiveDamage);

        System.out.println("Remaining HP: " + player.getHealthPoints());
        
        // Using a getter to trigger an in-game event
        if (player.getHealthPoints() == 0) {
            System.out.println(player.getHeroName() + " has been defeated!");
        }
    }
}