/**
 * Représente une case du plateau de jeu (9x9).
 * Une case peut contenir un Pokémon appartenant au joueur 1 ou 2, ou être vide.
 */
public class Case {

    private Pokemon pokemon;  // null si la case est vide
    private int     joueur;   // 1 ou 2 (0 si vide)

    /** Case vide. */
    public Case() {
        this.pokemon = null;
        this.joueur  = 0;
    }

    /** Case avec un pokémon. */
    public Case(Pokemon pokemon, int joueur) {
        this.pokemon = pokemon;
        this.joueur  = joueur;
    }

    public boolean estVide()    { return pokemon == null; }
    public Pokemon getPokemon() { return pokemon; }
    public int     getJoueur()  { return joueur; }

    public void setPokemon(Pokemon pokemon, int joueur) {
        this.pokemon = pokemon;
        this.joueur  = joueur;
    }

    public void vider() {
        this.pokemon = null;
        this.joueur  = 0;
    }
}
