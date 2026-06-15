/**
 * Classe principale pour la Partie 1 :
 * Crée deux pokémons et les fait combattre jusqu'à la mort de l'un d'eux.
 */
public class Main {

    public static void main(String[] args) {

        System.out.println("=== Pokémon Combat ===\n");

        // Création via le constructeur simplifié (lit le pokédex CSV)
        Pokemon p1 = new Pokemon(94, "Gustave");   // Ectoplasma
        Pokemon p2 = new Pokemon(57, "Robert");    // Colossinge

        System.out.println("Joueur 1 : " + p1);
        System.out.println();
        System.out.println("Joueur 2 : " + p2);
        System.out.println();
        System.out.println("══════════════════════════════════════");
        System.out.println("           DÉBUT DU COMBAT !");
        System.out.println("══════════════════════════════════════\n");

        int tour = 1;
        while (p1.estVivant() && p2.estVivant()) {
            System.out.println("── Tour " + tour + " ──");
            p1.attaque(p2);
            tour++;
        }

        System.out.println();
        System.out.println("══════════════════════════════════════");
        if (!p1.estVivant()) {
            System.out.println(p1.getNom() + " est K.O. ! " + p2.getNom() + " remporte le combat !");
        } else {
            System.out.println(p2.getNom() + " est K.O. ! " + p1.getNom() + " remporte le combat !");
        }
        System.out.println("══════════════════════════════════════");
    }
}
