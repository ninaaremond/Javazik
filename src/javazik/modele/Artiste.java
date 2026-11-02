package javazik.modele;

import java.util.ArrayList;
import java.util.List;

public class Artiste {

    private int idArtiste;
    private String nom;
    private String prenom;
    private String pseudo;
    private ArrayList<Groupe> listeGroupes;

    public Artiste(int idArtiste, String nom, String prenom, String pseudo){
        this.idArtiste = idArtiste;
        this.nom = nom;
        this.prenom = prenom;
        this.pseudo = pseudo;
        this.listeGroupes = new ArrayList<>();
    }

    public int getIdArtiste() {
        return this.idArtiste;
    }

    public String getNom() {
        return this.nom;
    }

    public String getPrenom() {
        return this.prenom;
    }

    public String getPseudo() {
        return this.pseudo;
    }

    public ArrayList<Groupe> getListeGroupes() {
        return this.listeGroupes;
    }

    // Ajouter un groupe à l'artiste
    public void ajouterGroupe(Groupe groupe) {

        if (groupe == null) {
            return;
        }

        for (Groupe g : this.listeGroupes) {
            if (g.getIdGroupe() == groupe.getIdGroupe()) {
                return;
            }
        }

        this.listeGroupes.add(groupe);

        if (!groupe.contientArtiste(this)) {
            groupe.ajouterArtiste(this);
        }
    }

    // Supprimer un groupe de l'artiste
    public void supprimerGroupe(Groupe groupe) {

        if (groupe == null) {
            return;
        }

        Groupe groupeASupprimer = null;

        for (Groupe g : this.listeGroupes) {
            if (g.getIdGroupe() == groupe.getIdGroupe()) {
                groupeASupprimer = g;
                break;
            }
        }

        if (groupeASupprimer != null) {
            this.listeGroupes.remove(groupeASupprimer);

            if (groupe.contientArtiste(this)) {
                groupe.supprimerArtiste(this);
            }
        }
    }

    public boolean appartientAGroupe() {
        return !this.listeGroupes.isEmpty();
    }
}