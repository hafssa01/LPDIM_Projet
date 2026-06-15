import MG2D.Fenetre;
import MG2D.Souris;
import MG2D.Couleur;
import MG2D.geometrie.Point;
import MG2D.geometrie.Rectangle;
import MG2D.geometrie.Carre;
import MG2D.geometrie.Texte;
import MG2D.geometrie.Texture;

import java.awt.Font;

/**
 * Jeu d'échecs Pokémon — Partie 2 (graphique avec MG2D).
 *
 * Règles :
 *  - Plateau 9x9, chaque joueur commence avec 3 lignes de pokémons.
 *  - Tour par tour : clic sur son pokémon, puis clic sur case voisine pour déplacer
 *    ou sur pokémon ennemi voisin pour attaquer.
 *  - Victoire : tuer le Mewtwo adverse.
 *
 * Coordonnées MG2D : origine (0,0) en bas à gauche.
 */
public class Jeu {

    // ── Constantes ─────────────────────────────────────────────────────────
    static final int TAILLE_CASE   = 60;
    static final int TAILLE_GRILLE = Plateau.TAILLE * TAILLE_CASE;  // 540
    static final int PANNEAU_INFO  = 220;
    static final int LARGEUR       = TAILLE_GRILLE + PANNEAU_INFO;  // 760
    static final int HAUTEUR       = TAILLE_GRILLE;                 // 540

    // Couleurs
    static final Couleur BLANC        = new Couleur(255, 255, 255);
    static final Couleur NOIR         = new Couleur(0,   0,   0);
    static final Couleur GRIS_CLAIR   = new Couleur(210, 210, 210);
    static final Couleur GRIS_FONCE   = new Couleur(120, 120, 120);
    static final Couleur ROUGE        = new Couleur(220, 60,  60);
    static final Couleur BLEU         = new Couleur(60,  100, 220);
    static final Couleur VERT         = new Couleur(60,  190, 80);
    static final Couleur JAUNE        = new Couleur(255, 230, 0);
    static final Couleur VERT_CLAIR   = new Couleur(180, 255, 190);
    static final Couleur ORANGE       = new Couleur(255, 140, 0);
    static final Couleur FOND_INFO    = new Couleur(25,  25,  45);

    static final Font POLICE = new Font("Monospaced", Font.BOLD, 12);
    static final Font POLICE_TITRE = new Font("Monospaced", Font.BOLD, 14);

    // ── État ───────────────────────────────────────────────────────────────
    private Plateau plateau;
    private int     joueurCourant = 1;
    private int     selLigne = -1, selCol = -1;   // case sélectionnée
    private int     vainqueur = 0;
    private String  msg1 = "Joueur 1, cliquez";
    private String  msg2 = "sur votre Pokemon";

    // ── MG2D ──────────────────────────────────────────────────────────────
    private Fenetre fenetre;
    private Souris  souris;

    // ── Point d'entrée ────────────────────────────────────────────────────
    public static void main(String[] args) {
        new Jeu().lancer();
    }

    public void lancer() {
        plateau = new Plateau();
        fenetre = new Fenetre("Pokemon Echecs - BUT Info", LARGEUR, HAUTEUR);
        souris  = fenetre.getSouris();

        while (true) {
            // 1. Effacer
            fenetre.effacer();

            // 2. Dessiner
            dessinerPlateau();
            dessinerPanneauInfo();
            if (vainqueur != 0) dessinerVictoire();

            // 3. Rafraîchir l'écran
            fenetre.rafraichir();

            // 4. Traiter clic
            if (souris.getClicGauche()) {
                Point p = souris.getPosition();
                traiterClic((int) p.getX(), (int) p.getY());
            }

            try { Thread.sleep(30); } catch (InterruptedException ignored) {}
        }
    }

    // ── Rendu du plateau ──────────────────────────────────────────────────

    private void dessinerPlateau() {
        for (int l = 0; l < Plateau.TAILLE; l++) {
            for (int c = 0; c < Plateau.TAILLE; c++) {
                dessinerCase(l, c);
            }
        }
    }

    private void dessinerCase(int ligne, int col) {
        // MG2D : y=0 en bas → ligne 0 est affichée en bas de l'écran
        int px = col  * TAILLE_CASE;
        int py = ligne * TAILLE_CASE;

        // Fond damier
        Couleur fond;
        if (ligne == selLigne && col == selCol) {
            fond = JAUNE;
        } else if (estCibleAttaque(ligne, col)) {
            fond = ORANGE;
        } else if (estCibleDeplacement(ligne, col)) {
            fond = VERT_CLAIR;
        } else {
            fond = ((ligne + col) % 2 == 0) ? BLANC : GRIS_CLAIR;
        }

        // Fond de la case
        fenetre.ajouter(new Carre(fond, new Point(px, py), TAILLE_CASE, true));
        // Bordure
        fenetre.ajouter(new Carre(GRIS_FONCE, new Point(px, py), TAILLE_CASE, false));

        // Contenu
        Case c = plateau.getCase(ligne, col);
        if (c != null && !c.estVide()) {
            dessinerPokemon(c, px, py);
        }
    }

    private void dessinerPokemon(Case c, int px, int py) {
        int num = c.getPokemon().getNumPokedex();

        // Image du pokémon
        try {
            Texture tex = new Texture(
                "images/" + num + ".png",
                new Point(px + 2, py + 10),
                TAILLE_CASE - 4, TAILLE_CASE - 20
            );
            fenetre.ajouter(tex);
        } catch (Exception e) {
            // Fallback carré coloré
            Couleur coul = (c.getJoueur() == 1) ? ROUGE : BLEU;
            fenetre.ajouter(new Carre(coul, new Point(px + 8, py + 10),
                                      TAILLE_CASE - 16, true));
        }

        // Pastille joueur (coin haut-gauche)
        Couleur pastille = (c.getJoueur() == 1) ? ROUGE : BLEU;
        fenetre.ajouter(new Carre(pastille, new Point(px + 2, py + TAILLE_CASE - 10), 8, true));

        // Barre de vie (bas de la case)
        Pokemon p = c.getPokemon();
        int barMax = TAILLE_CASE - 4;
        int barPv  = (int)((double) p.getPv() / p.getPvMax() * barMax);
        Couleur barCoul = (p.getPv() > p.getPvMax() / 2) ? VERT : ROUGE;
        fenetre.ajouter(new Rectangle(NOIR,     new Point(px + 2, py + 2), barMax, 6, true));
        if (barPv > 0)
            fenetre.ajouter(new Rectangle(barCoul, new Point(px + 2, py + 2), barPv, 6, true));
    }

    // ── Panneau d'info ────────────────────────────────────────────────────

    private void dessinerPanneauInfo() {
        int x0 = TAILLE_GRILLE;

        // Fond
        fenetre.ajouter(new Rectangle(FOND_INFO, new Point(x0, 0), PANNEAU_INFO, HAUTEUR, true));

        int y = HAUTEUR - 25;

        // Tour courant
        Couleur coulTour = (joueurCourant == 1) ? ROUGE : BLEU;
        String  labelTour = (joueurCourant == 1) ? "=== JOUEUR 1 ===" : "=== JOUEUR 2 ===";
        fenetre.ajouter(new Texte(coulTour, labelTour, POLICE_TITRE, new Point(x0 + 10, y)));

        y -= 25;
        fenetre.ajouter(new Texte(BLANC, msg1, POLICE, new Point(x0 + 10, y)));
        y -= 18;
        fenetre.ajouter(new Texte(BLANC, msg2, POLICE, new Point(x0 + 10, y)));

        // Infos pokémon sélectionné
        if (selLigne >= 0) {
            Case c = plateau.getCase(selLigne, selCol);
            if (c != null && !c.estVide()) {
                Pokemon p = c.getPokemon();
                y -= 30;
                fenetre.ajouter(new Texte(JAUNE, "[ " + p.getNom() + " ]", POLICE_TITRE, new Point(x0 + 10, y)));
                y -= 20;
                String espece = (p.getNumPokedex() >= 1 && p.getNumPokedex() <= 151)
                              ? Type.getEspece(p.getNumPokedex()) : "???";
                fenetre.ajouter(new Texte(GRIS_CLAIR, espece, POLICE, new Point(x0 + 10, y)));
                y -= 18;
                fenetre.ajouter(new Texte(VERT, "PV : " + p.getPv() + "/" + p.getPvMax(), POLICE, new Point(x0 + 10, y)));
                y -= 18;
                fenetre.ajouter(new Texte(BLANC, "ATT: " + p.getAtt() + "  DEF: " + p.getDef(), POLICE, new Point(x0 + 10, y)));
                y -= 18;
                fenetre.ajouter(new Texte(BLANC, "VIT: " + p.getVit(), POLICE, new Point(x0 + 10, y)));
                y -= 18;
                fenetre.ajouter(new Texte(BLANC, "Type: " + Type.getNomType(p.getType1()), POLICE, new Point(x0 + 10, y)));
            }
        }

        // Légende en bas
        fenetre.ajouter(new Texte(ROUGE, "Joueur 1 = Rouge", POLICE, new Point(x0 + 10, 55)));
        fenetre.ajouter(new Texte(BLEU,  "Joueur 2 = Bleu",  POLICE, new Point(x0 + 10, 35)));
        fenetre.ajouter(new Texte(GRIS_CLAIR, "Tuer le Mewtwo !", POLICE, new Point(x0 + 10, 15)));
    }

    // ── Écran de victoire ─────────────────────────────────────────────────

    private void dessinerVictoire() {
        fenetre.ajouter(new Rectangle(NOIR, new Point(60, HAUTEUR / 2 - 35),
                                       TAILLE_GRILLE - 120, 70, true));
        Couleur c = (vainqueur == 1) ? ROUGE : BLEU;
        fenetre.ajouter(new Texte(c, "JOUEUR " + vainqueur + " GAGNE !!!",
                                   POLICE_TITRE, new Point(120, HAUTEUR / 2 + 10)));
        fenetre.ajouter(new Texte(BLANC, "Le Mewtwo adverse est K.O. !",
                                   POLICE, new Point(90, HAUTEUR / 2 - 15)));
    }

    // ── Logique de clic ────────────────────────────────────────────────────

    private void traiterClic(int mx, int my) {
        if (vainqueur != 0) return;
        if (mx >= TAILLE_GRILLE) return;  // clic dans panneau info

        int col   = mx / TAILLE_CASE;
        int ligne = my / TAILLE_CASE;
        if (!plateau.valide(ligne, col)) return;

        Case caseCliquee = plateau.getCase(ligne, col);

        if (selLigne < 0) {
            // Aucune sélection : tenter de sélectionner notre pokémon
            if (!caseCliquee.estVide() && caseCliquee.getJoueur() == joueurCourant) {
                selLigne = ligne;
                selCol   = col;
                msg1 = "Selecte: " + caseCliquee.getPokemon().getNom();
                msg2 = "Cliquez case ou ennemi";
            } else {
                msg1 = "Ce n'est pas votre";
                msg2 = "Pokemon !";
            }
        } else {
            // Une pièce est sélectionnée
            if (ligne == selLigne && col == selCol) {
                // Re-clic = désélectionner
                deselectionner();
                return;
            }
            if (!caseCliquee.estVide() && caseCliquee.getJoueur() == joueurCourant) {
                // Autre pièce alliée → changer sélection
                selLigne = ligne;
                selCol   = col;
                msg1 = "Selecte: " + caseCliquee.getPokemon().getNom();
                msg2 = "Cliquez case ou ennemi";
                return;
            }
            if (caseCliquee.estVide()) {
                // Déplacement
                if (plateau.deplacer(selLigne, selCol, ligne, col)) {
                    msg1 = "Deplacement effectue";
                    msg2 = "";
                    deselectionner();
                    changerTour();
                } else {
                    msg1 = "Impossible !";
                    msg2 = "(1 case max, 8 directions)";
                }
            } else if (caseCliquee.getJoueur() != joueurCourant) {
                // Attaque
                String nomDef = caseCliquee.getPokemon().getNom();
                if (plateau.attaquer(selLigne, selCol, ligne, col)) {
                    msg1 = "Attaque sur " + nomDef + "!";
                    msg2 = "";
                    deselectionner();
                    vainqueur = plateau.vainqueur();
                    if (vainqueur == 0) changerTour();
                } else {
                    msg1 = "Pas adjacent !";
                    msg2 = "(doit etre voisin)";
                }
            }
        }
    }

    private void deselectionner() {
        selLigne = -1;
        selCol   = -1;
    }

    private void changerTour() {
        joueurCourant = (joueurCourant == 1) ? 2 : 1;
        msg1 = "Tour Joueur " + joueurCourant;
        msg2 = "Cliquez votre Pokemon";
    }

    // ── Surlignage cases possibles ─────────────────────────────────────────

    private boolean estCibleAttaque(int l, int c) {
        if (selLigne < 0) return false;
        Case src = plateau.getCase(selLigne, selCol);
        Case dst = plateau.getCase(l, c);
        if (src == null || src.estVide() || dst == null || dst.estVide()) return false;
        if (dst.getJoueur() == joueurCourant) return false;
        return adjacent(l, c);
    }

    private boolean estCibleDeplacement(int l, int c) {
        if (selLigne < 0) return false;
        Case src = plateau.getCase(selLigne, selCol);
        Case dst = plateau.getCase(l, c);
        if (src == null || src.estVide() || dst == null || !dst.estVide()) return false;
        return adjacent(l, c);
    }

    private boolean adjacent(int l, int c) {
        int dl = Math.abs(l - selLigne);
        int dc = Math.abs(c - selCol);
        return dl <= 1 && dc <= 1 && !(dl == 0 && dc == 0);
    }
}
