package javazik.modele;

/**
 * Classe pour stocker les statistiques du système.
 * utilisée pae Admin pour afficher les stats.
 */
public class StatistiquesAdmin {
    private int nombreUtilisateurs;
    private int nombreClientsActifs;
    private int nombreMorceaux;
    private int nombreAlbums;
    private int nombreArtistes;
    private int nombreGroupes;
    private int nombreTotalEcoutes;

    /**
     * Constructeur par défaut
     */
    public StatistiquesAdmin() {
        this.nombreUtilisateurs = 0;
        this.nombreClientsActifs = 0;
        this.nombreMorceaux = 0;
        this.nombreAlbums = 0;
        this.nombreArtistes = 0;
        this.nombreGroupes = 0;
        this.nombreTotalEcoutes = 0;
    }

    // Getters
    public int getNombreUtilisateurs() {
        return nombreUtilisateurs;
    }

    public int getNombreClientsActifs() {
        return nombreClientsActifs;
    }

    public int getNombreMorceaux() {
        return nombreMorceaux;
    }

    public int getNombreAlbums() {
        return nombreAlbums;
    }

    public int getNombreArtistes() {
        return nombreArtistes;
    }

    public int getNombreGroupes() {
        return nombreGroupes;
    }

    public int getNombreTotalEcoutes() {
        return nombreTotalEcoutes;
    }

    // Setters
    public void setNombreUtilisateurs(int nombreUtilisateurs) {
        this.nombreUtilisateurs = nombreUtilisateurs;
    }

    public void setNombreClientsActifs(int nombreClientsActifs) {
        this.nombreClientsActifs = nombreClientsActifs;
    }

    public void setNombreMorceaux(int nombreMorceaux) {
        this.nombreMorceaux = nombreMorceaux;
    }

    public void setNombreAlbums(int nombreAlbums) {
        this.nombreAlbums = nombreAlbums;
    }

    public void setNombreArtistes(int nombreArtistes) {
        this.nombreArtistes = nombreArtistes;
    }

    public void setNombreGroupes(int nombreGroupes) {
        this.nombreGroupes = nombreGroupes;
    }

    public void setNombreTotalEcoutes(int nombreTotalEcoutes) {
        this.nombreTotalEcoutes = nombreTotalEcoutes;
    }

     // Demande à GestionCatalogue de calculer et remplir les stats musique
    public void rafraichirStatsCatalogue(GestionCatalogue gestionCatalogue) {
        if (gestionCatalogue != null) {
            gestionCatalogue.mettreAJourStatistiquesCatalogue(this);
        } else {
            System.out.println("Erreur : Le gestionnaire de catalogue est introuvable.");
        }
    }


     // Demande GestionUtilisateurs de calculer et remplir les stats utilisateurs
    public void rafraichirStatsUtilisateurs(GestionUtilisateurs gestionUtilisateurs) {
        if (gestionUtilisateurs != null) {
            gestionUtilisateurs.mettreAJourStatistiquesUtilisateurs(this);
        } else {
            System.out.println("Erreur : Le gestionnaire d'utilisateurs est introuvable.");
        }
    }

     //methode globale pour tout rafraichir d'un coup ( le grand frigo galactique)
    public void rafraichirToutesLesStats(GestionCatalogue gestionCat, GestionUtilisateurs gestionUtil) {
        this.rafraichirStatsCatalogue(gestionCat);
        this.rafraichirStatsUtilisateurs(gestionUtil);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("===== STATISTIQUES DU SYSTÈME =====\n\n");
        sb.append("--- Utilisateurs ---\n");
        sb.append("Nombre total d'utilisateurs: ").append(nombreUtilisateurs).append("\n");
        sb.append("Clients actifs: ").append(nombreClientsActifs).append("\n\n");
        sb.append("--- Catalogue ---\n");
        sb.append("Nombre de morceaux: ").append(nombreMorceaux).append("\n");
        sb.append("Nombre d'albums: ").append(nombreAlbums).append("\n");
        sb.append("Nombre d'artistes: ").append(nombreArtistes).append("\n");
        sb.append("Nombre de groupes: ").append(nombreGroupes).append("\n\n");
        sb.append("--- Écoutes ---\n");
        sb.append("Nombre total d'écoutes: ").append(nombreTotalEcoutes).append("\n");
        sb.append("====================================\n");
        return sb.toString();
    }
}