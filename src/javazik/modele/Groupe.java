package javazik.modele;

import java.util.ArrayList;

public class Groupe {

    private int idGroupe;
    private String nom;
    private ArrayList<Artiste> listeArtistes;

    public Groupe(int idGroupe, String nom){
        this.idGroupe = idGroupe;
        this.nom = nom;
        this.listeArtistes = new ArrayList<>();
    }

    public int getIdGroupe() {
        return this.idGroupe;
    }

    public String getNom() {
        return this.nom;
    }

    public ArrayList<Artiste> getListeArtistes() {
        return this.listeArtistes;
    }

    // Ajouter un artiste au groupe
    public void ajouterArtiste(Artiste artiste) {

        if (artiste == null) {
            return;
        }

        for (Artiste a : this.listeArtistes) {
            if (a.getIdArtiste() == artiste.getIdArtiste()) {
                return;
            }
        }

        this.listeArtistes.add(artiste);

        boolean dejaLie = false;

        for (Groupe g : artiste.getListeGroupes()) {
            if (g.getIdGroupe() == this.idGroupe) {
                dejaLie = true;
                break;
            }
        }

        if (!dejaLie) {
            artiste.ajouterGroupe(this);
        }
    }

    // Supprimer un artiste du groupe
    public void supprimerArtiste(Artiste artiste) {

        if (artiste == null) {
            return;
        }

        Artiste artisteASupprimer = null;

        for (Artiste a : this.listeArtistes) {
            if (a.getIdArtiste() == artiste.getIdArtiste()) {
                artisteASupprimer = a;
                break;
            }
        }

        if (artisteASupprimer != null) {
            this.listeArtistes.remove(artisteASupprimer);
        }
    }

    public boolean contientArtiste(Artiste artiste) {

        if (artiste == null) {
            return false;
        }

        for (Artiste a : this.listeArtistes) {
            if (a.getIdArtiste() == artiste.getIdArtiste()) {
                return true;
            }
        }

        return false;
    }
}