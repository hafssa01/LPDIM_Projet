import java.io.*;
import java.util.Scanner;

/**
 * Représente un Pokémon avec ses caractéristiques de combat.
 */
public class Pokemon {

    // ── Attributs privés ─────────────────────────────────────────────────────
    private int    numPokedex;  // numéro dans le pokédex (1–151)
    private String nom;         // surnom donné au pokémon
    private int    type1;       // type principal  (constante Type.XYZ)
    private int    type2;       // type secondaire (Type.SANS si aucun)
    private int    pv;          // points de vie courants
    private int    pvMax;       // points de vie maximum (pour affichage)
    private int    att;         // points d'attaque
    private int    def;         // points de défense
    private int    vit;         // vitesse

    // ── Constructeur par défaut : Missingno ──────────────────────────────────
    /**
     * Crée un Missingno (pokémon n°0 fictif) avec des stats minimales.
     */
    public Pokemon() {
        this.numPokedex = 0;
        this.nom        = "Missingno";
        this.type1      = Type.NORMAL;
        this.type2      = Type.SANS;
        this.pv         = 33;
        this.pvMax      = 33;
        this.att        = 5;
        this.def        = 5;
        this.vit        = 5;
    }

    // ── Constructeur complet ─────────────────────────────────────────────────
    /**
     * Crée un pokémon avec toutes ses caractéristiques.
     *
     * @param numPokedex numéro dans le pokédex
     * @param nom        surnom du pokémon
     * @param type1      type principal
     * @param type2      type secondaire (Type.SANS si aucun)
     * @param pv         points de vie
     * @param att        attaque
     * @param def        défense
     * @param vit        vitesse
     */
    public Pokemon(int numPokedex, String nom, int type1, int type2,
                   int pv, int att, int def, int vit) {
        this.numPokedex = numPokedex;
        this.nom        = nom;
        this.type1      = type1;
        this.type2      = type2;
        this.pv         = pv;
        this.pvMax      = pv;
        this.att        = att;
        this.def        = def;
        this.vit        = vit;
    }

    // ── Constructeur simplifié (lit le pokédex CSV) ──────────────────────────
    /**
     * Crée un pokémon en lisant ses stats dans le fichier pokedex_gen1.csv.
     * Seuls le numéro et le surnom sont passés en paramètre.
     *
     * @param numPokedex numéro dans le pokédex (1–151)
     * @param nom        surnom à donner au pokémon
     */
    public Pokemon(int numPokedex, String nom) {
        this.numPokedex = numPokedex;
        this.nom        = nom;

        // Valeurs par défaut au cas où le CSV serait introuvable
        this.type1 = Type.NORMAL;
        this.type2 = Type.SANS;
        this.pv    = 45;
        this.pvMax = 45;
        this.att   = 50;
        this.def   = 50;
        this.vit   = 45;

        // Lecture du CSV : Numéro;Nom;Type1;Type2;PV;Force;Défense;Vitesse;Spécial;Total
        try {
            // Essaie d'abord le chemin relatif depuis le répertoire d'exécution
            String[] paths = {"pokedex_gen1.csv", "../pokedex_gen1.csv"};
            Scanner sc = null;
            for (String path : paths) {
                File f = new File(path);
                if (f.exists()) {
                    sc = new Scanner(f, "UTF-8");
                    break;
                }
            }
            if (sc == null) return;

            sc.nextLine(); // skip header
            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                if (line.isEmpty()) continue;
                String[] cols = line.split(";", -1);
                if (cols.length < 8) continue;
                int num = Integer.parseInt(cols[0].trim());
                if (num == numPokedex) {
                    this.type1 = Type.getIndiceType(cols[2].trim());
                    this.type2 = cols[3].trim().isEmpty()
                               ? Type.SANS
                               : Type.getIndiceType(cols[3].trim());
                    this.pv    = Integer.parseInt(cols[4].trim());
                    this.pvMax = this.pv;
                    this.att   = Integer.parseInt(cols[5].trim());
                    this.def   = Integer.parseInt(cols[6].trim());
                    this.vit   = Integer.parseInt(cols[7].trim());
                    break;
                }
            }
            sc.close();
        } catch (Exception e) {
            System.err.println("Impossible de lire le pokédex : " + e.getMessage());
        }
    }

    // ── Getters ──────────────────────────────────────────────────────────────

    public int    getNumPokedex() { return numPokedex; }
    public String getNom()        { return nom; }
    public int    getType1()      { return type1; }
    public int    getType2()      { return type2; }
    public int    getPv()         { return pv; }
    public int    getPvMax()      { return pvMax; }
    public int    getAtt()        { return att; }
    public int    getDef()        { return def; }
    public int    getVit()        { return vit; }

    // ── Setters (uniquement pour ce qui peut évoluer) ────────────────────────

    public void setNom(String nom)     { this.nom = nom; }
    public void setPv(int pv)          { this.pv  = pv;  }
    public void setAtt(int att)        { this.att = att; }
    public void setDef(int def)        { this.def = def; }
    public void setVit(int vit)        { this.vit = vit; }

    // ── Méthode estVivant ─────────────────────────────────────────────────────

    /** Retourne true si le pokémon a encore des PV. */
    public boolean estVivant() {
        return pv > 0;
    }

    // ── equals ───────────────────────────────────────────────────────────────

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Pokemon)) return false;
        Pokemon other = (Pokemon) obj;
        return this.numPokedex == other.numPokedex
            && this.nom.equals(other.nom)
            && this.type1 == other.type1
            && this.type2 == other.type2
            && this.pvMax == other.pvMax
            && this.att   == other.att
            && this.def   == other.def
            && this.vit   == other.vit;
    }

    // ── toString ─────────────────────────────────────────────────────────────

    @Override
    public String toString() {
        String espece = (numPokedex >= 1 && numPokedex <= 151)
                      ? Type.getEspece(numPokedex)
                      : "???";
        String t2 = (type2 == Type.SANS) ? "ø" : Type.getNomType(type2);
        return String.format(
            "Numéro : %d | Espèce : %s | Nom : %s\n" +
            "Type 1 : %s | Type 2 : %s\n" +
            "PV : %d/%d | Att : %d | Def : %d | Vit : %d",
            numPokedex, espece, nom,
            Type.getNomType(type1), t2,
            pv, pvMax, att, def, vit
        );
    }

    // ── attaque ──────────────────────────────────────────────────────────────

    /**
     * Lance un tour d'attaque entre this et l'adversaire.
     * Le plus rapide attaque en premier ; l'autre contre-attaque s'il est encore vivant.
     * Les dégâts tiennent compte de l'efficacité de type.
     *
     * @param adversaire le pokémon en face
     */
    public void attaque(Pokemon adversaire) {
        Pokemon premier, second;

        // Le plus rapide attaque en premier
        if (this.vit >= adversaire.vit) {
            premier = this;
            second  = adversaire;
        } else {
            premier = adversaire;
            second  = this;
        }

        // 1ère attaque
        int degats1 = calculerDegats(premier, second);
        second.pv = Math.max(0, second.pv - degats1);
        afficherAttaque(premier, second, degats1);

        // Contre-attaque si l'adversaire est encore vivant
        if (second.estVivant()) {
            int degats2 = calculerDegats(second, premier);
            premier.pv = Math.max(0, premier.pv - degats2);
            afficherAttaque(second, premier, degats2);
        }
    }

    /**
     * Calcule les dégâts infligés par attaquant sur defenseur,
     * en tenant compte de l'efficacité de type.
     */
    private static int calculerDegats(Pokemon attaquant, Pokemon defenseur) {
        // Efficacité combinée des deux types du défenseur
        double eff = Type.getEfficacite(attaquant.type1, defenseur.type1);
        if (defenseur.type2 != Type.SANS) {
            eff *= Type.getEfficacite(attaquant.type1, defenseur.type2);
        }
        // Si le pokémon a un 2ème type en attaque, on prend le meilleur
        if (attaquant.type2 != Type.SANS) {
            double eff2 = Type.getEfficacite(attaquant.type2, defenseur.type1);
            if (defenseur.type2 != Type.SANS) {
                eff2 *= Type.getEfficacite(attaquant.type2, defenseur.type2);
            }
            if (eff2 > eff) eff = eff2;
        }

        int degats = (int)((attaquant.att - defenseur.def) * eff);
        if (degats < 1) degats = 1;
        return degats;
    }

    private static void afficherAttaque(Pokemon att, Pokemon def, int degats) {
        System.out.printf("  %s attaque %s → %d dégâts (PV restants : %d/%d)%n",
            att.getNom(), def.getNom(), degats, def.pv, def.pvMax);
    }
}
