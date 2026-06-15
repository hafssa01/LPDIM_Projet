/**
 * Le plateau de jeu : une grille 9×9 de cases.
 *
 * Convention de coordonnées :
 *   ligne 0 = bas (joueur 1), ligne 8 = haut (joueur 2).
 *   colonne 0 = gauche, colonne 8 = droite.
 *
 * Disposition initiale :
 *   Joueur 1 occupe les lignes 0, 1, 2 (lignes du bas).
 *   Joueur 2 occupe les lignes 6, 7, 8 (lignes du haut).
 *   Mewtwo (numéro 150) est placé au centre de la ligne de fond de chaque joueur.
 */
public class Plateau {

    public static final int TAILLE = 9;

    // grille[ligne][colonne]
    private Case[][] grille;

    // Références aux Mewtwo pour vérifier la condition de victoire
    private Pokemon mewtwoJ1;
    private Pokemon mewtwoJ2;

    public Plateau() {
        grille = new Case[TAILLE][TAILLE];
        for (int l = 0; l < TAILLE; l++) {
            for (int c = 0; c < TAILLE; c++) {
                grille[l][c] = new Case();
            }
        }
        initialiser();
    }

    /**
     * Place les pokémons en début de partie.
     * Ligne 0 = fond joueur 1, ligne 8 = fond joueur 2.
     */
    private void initialiser() {

        // ── Joueur 1 (lignes 0, 1, 2) ─────────────────────────────────────

        // Ligne 0 : Mewtwo au centre, legendaires aux côtés, starters aux extrémités
        poser(new Pokemon(1,   "Bulbizarre"), 1, 0, 0);
        poser(new Pokemon(4,   "Salamèche"),  1, 0, 1);
        poser(new Pokemon(145, "Électhor"),   1, 0, 2);
        poser(new Pokemon(146, "Sulfura"),    1, 0, 3);
        Pokemon mj1 = new Pokemon(150, "Mewtwo");
        mewtwoJ1 = mj1;
        poser(mj1,                            1, 0, 4);  // centre
        poser(new Pokemon(144, "Artikodin"),  1, 0, 5);
        poser(new Pokemon(143, "Ronflex"),    1, 0, 6);
        poser(new Pokemon(7,   "Carapuce"),   1, 0, 7);
        poser(new Pokemon(25,  "Pikachu"),    1, 0, 8);

        // Ligne 1 : pokémons de niveau intermédiaire
        poser(new Pokemon(2,   "Herbizarre"), 1, 1, 0);
        poser(new Pokemon(5,   "Reptincel"),  1, 1, 1);
        poser(new Pokemon(6,   "Dracaufeu"),  1, 1, 2);
        poser(new Pokemon(149, "Dracolosse"), 1, 1, 3);
        poser(new Pokemon(131, "Lokhlass"),   1, 1, 4);
        poser(new Pokemon(148, "Draco"),      1, 1, 5);
        poser(new Pokemon(9,   "Tortank"),    1, 1, 6);
        poser(new Pokemon(8,   "Carabaffe"),  1, 1, 7);
        poser(new Pokemon(3,   "Florizarre"), 1, 1, 8);

        // Ligne 2 : pokémons d'avant-garde
        poser(new Pokemon(65,  "Alakazam"),   1, 2, 0);
        poser(new Pokemon(68,  "Mackogneur"), 1, 2, 1);
        poser(new Pokemon(59,  "Arcanin"),    1, 2, 2);
        poser(new Pokemon(94,  "Ectoplasma"), 1, 2, 3);
        poser(new Pokemon(130, "Léviator"),   1, 2, 4);
        poser(new Pokemon(91,  "Crustabri"),  1, 2, 5);
        poser(new Pokemon(112, "Rhinoféros"), 1, 2, 6);
        poser(new Pokemon(103, "Noadkoko"),   1, 2, 7);
        poser(new Pokemon(142, "Ptéra"),      1, 2, 8);

        // ── Joueur 2 (lignes 8, 7, 6) ─────────────────────────────────────

        // Ligne 8 (fond joueur 2)
        poser(new Pokemon(1,   "Bulbizarre"), 2, 8, 0);
        poser(new Pokemon(4,   "Salamèche"),  2, 8, 1);
        poser(new Pokemon(145, "Électhor"),   2, 8, 2);
        poser(new Pokemon(146, "Sulfura"),    2, 8, 3);
        Pokemon mj2 = new Pokemon(150, "Mewtwo");
        mewtwoJ2 = mj2;
        poser(mj2,                            2, 8, 4);
        poser(new Pokemon(144, "Artikodin"),  2, 8, 5);
        poser(new Pokemon(143, "Ronflex"),    2, 8, 6);
        poser(new Pokemon(7,   "Carapuce"),   2, 8, 7);
        poser(new Pokemon(25,  "Pikachu"),    2, 8, 8);

        // Ligne 7
        poser(new Pokemon(2,   "Herbizarre"), 2, 7, 0);
        poser(new Pokemon(5,   "Reptincel"),  2, 7, 1);
        poser(new Pokemon(6,   "Dracaufeu"),  2, 7, 2);
        poser(new Pokemon(149, "Dracolosse"), 2, 7, 3);
        poser(new Pokemon(131, "Lokhlass"),   2, 7, 4);
        poser(new Pokemon(148, "Draco"),      2, 7, 5);
        poser(new Pokemon(9,   "Tortank"),    2, 7, 6);
        poser(new Pokemon(8,   "Carabaffe"),  2, 7, 7);
        poser(new Pokemon(3,   "Florizarre"), 2, 7, 8);

        // Ligne 6
        poser(new Pokemon(65,  "Alakazam"),   2, 6, 0);
        poser(new Pokemon(68,  "Mackogneur"), 2, 6, 1);
        poser(new Pokemon(59,  "Arcanin"),    2, 6, 2);
        poser(new Pokemon(94,  "Ectoplasma"), 2, 6, 3);
        poser(new Pokemon(130, "Léviator"),   2, 6, 4);
        poser(new Pokemon(91,  "Crustabri"),  2, 6, 5);
        poser(new Pokemon(112, "Rhinoféros"), 2, 6, 6);
        poser(new Pokemon(103, "Noadkoko"),   2, 6, 7);
        poser(new Pokemon(142, "Ptéra"),      2, 6, 8);
    }

    /** Place un pokémon appartenant à joueur sur la case (ligne, col). */
    private void poser(Pokemon p, int joueur, int ligne, int col) {
        grille[ligne][col].setPokemon(p, joueur);
    }

    // ── Accesseurs ────────────────────────────────────────────────────────────

    public Case getCase(int ligne, int col) {
        if (!valide(ligne, col)) return null;
        return grille[ligne][col];
    }

    public Pokemon getMewtwoJ1() { return mewtwoJ1; }
    public Pokemon getMewtwoJ2() { return mewtwoJ2; }

    /** Vérifie si les coordonnées sont dans le plateau. */
    public boolean valide(int ligne, int col) {
        return ligne >= 0 && ligne < TAILLE && col >= 0 && col < TAILLE;
    }

    /**
     * Déplace le pokémon de (lDep, cDep) vers (lArr, cArr).
     * Vérifie que le mouvement d'une case est valide.
     * Retourne true si le déplacement a eu lieu.
     */
    public boolean deplacer(int lDep, int cDep, int lArr, int cArr) {
        if (!valide(lDep, cDep) || !valide(lArr, cArr)) return false;
        Case src = grille[lDep][cDep];
        Case dst = grille[lArr][cArr];
        if (src.estVide()) return false;

        // Mouvement d'une seule case (8 directions)
        int dl = Math.abs(lArr - lDep);
        int dc = Math.abs(cArr - cDep);
        if (dl > 1 || dc > 1 || (dl == 0 && dc == 0)) return false;

        // La case d'arrivée doit être vide (sinon utiliser attaquer())
        if (!dst.estVide()) return false;

        dst.setPokemon(src.getPokemon(), src.getJoueur());
        src.vider();
        return true;
    }

    /**
     * Attaque : le pokémon en (lAtt, cAtt) attaque celui en (lDef, cDef).
     * Les deux cases doivent être adjacentes, et appartenir à des joueurs différents.
     * Si le défenseur meurt, il est retiré du plateau.
     * Retourne true si l'attaque a eu lieu.
     */
    public boolean attaquer(int lAtt, int cAtt, int lDef, int cDef) {
        if (!valide(lAtt, cAtt) || !valide(lDef, cDef)) return false;
        Case src = grille[lAtt][cAtt];
        Case dst = grille[lDef][cDef];
        if (src.estVide() || dst.estVide()) return false;
        if (src.getJoueur() == dst.getJoueur()) return false;

        // Cases adjacentes uniquement
        int dl = Math.abs(lDef - lAtt);
        int dc = Math.abs(cDef - cAtt);
        if (dl > 1 || dc > 1 || (dl == 0 && dc == 0)) return false;

        // Combat
        src.getPokemon().attaque(dst.getPokemon());

        // Retirer les morts du plateau
        if (!dst.getPokemon().estVivant()) {
            dst.vider();
        }
        if (!src.getPokemon().estVivant()) {
            src.vider();
        }
        return true;
    }

    /**
     * Vérifie si un joueur a gagné (le Mewtwo adverse est mort).
     * @return 1 si joueur 1 gagne, 2 si joueur 2 gagne, 0 sinon.
     */
    public int vainqueur() {
        if (mewtwoJ2 != null && !mewtwoJ2.estVivant()) return 1;
        if (mewtwoJ1 != null && !mewtwoJ1.estVivant()) return 2;
        return 0;
    }
}
