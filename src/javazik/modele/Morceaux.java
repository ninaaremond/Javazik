package javazik.modele;

import java.util.ArrayList;

public class Morceaux {

    // Attributs
    private int idMusique;
    private String titre;
    private double duree;
    private ArrayList<Album> listeAlbums;

    // Constructeur principal
    public Morceaux(int idMusique, String titre, double duree){
        this.idMusique = idMusique;
        this.titre = titre;
        this.duree = duree;
        this.listeAlbums = new ArrayList<>();
    }

    // Constructeur simplifié (utile pour lecture rapide)
    public Morceaux(String titre) {
        this.titre = titre;
        this.listeAlbums = new ArrayList<>();
    }

    public double getDuree() {
        return this.duree;
    }

    public int getIdMusique() {
        return this.idMusique;
    }

    public String getTitre() {
        return this.titre;
    }

    public ArrayList<Album> getListeAlbums() {
        return this.listeAlbums;
    }

    // Ajouter un album au morceau
    public void ajouterAlbum(Album album) {

        if (album == null) {
            return;
        }

        for (Album a : this.listeAlbums) {
            if (a.getIdAlbum() == album.getIdAlbum()) {
                return;
            }
        }

        this.listeAlbums.add(album);
    }

    // Supprimer un album du morceau
    public void supprimerAlbum(Album album) {

        if (album == null) {
            return;
        }

        Album albumASupprimer = null;

        for (Album a : this.listeAlbums) {
            if (a.getIdAlbum() == album.getIdAlbum()) {
                albumASupprimer = a;
                break;
            }
        }

        if (albumASupprimer != null) {
            this.listeAlbums.remove(albumASupprimer);
        }
    }

    public boolean appartientAAlbum() {
        return !this.listeAlbums.isEmpty();
    }

    // Ecouter une musique
    public void ecouterMusique(){
        System.out.println("Lecture en cours du morceau : " + this.titre);
    }

    public static void quitterMusiqueEnCours(){
        System.out.println("Musique arrêtée.");
    }
}