package javazik.modele;/* @Author : Mathis CM
 * Fichier pour faire de la gestion dans le catalogue musical
 * Méthodes : ajouter, supprimer, modifier dans le catalogue
 * Cette classe ne pourra être utilisé que par un Admin
 * Toutes les méthodes doivent vérifier si c'est un admin qui utilise
 */

/*
 * src :
 * https://www.datacamp.com/fr/doc/java/create-&-write-files
 * https://www.ionos.fr/digitalguide/sites-internet/developpement-web/stringbuilder-en-java/
 * https://www.developpez.net/forums/d472916/java/general-java/langage/nombre-ligne-d-fichier/
 * https://www.ionos.fr/digitalguide/sites-internet/developpement-web/java-hashmap/
 * https://stackoverflow.com/questions/42709454/pass-a-hashmap-as-parameter-in-java
 * https://www.w3schools.com/java/java_hashmap.asp
 * https://www.developpez.net/forums/d321903/java/general-java/langage/modifier-fichier-texte-java/
 * https://www.w3schools.com/java/java_hashset.asp
 * */

import java.io.*;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;

public class GestionCatalogue {
    // Attribut(s)
    private final int roleUtilisateur; // Impossible à changer après initialisation
    static final String nomFichierCatalogue = "catalogueJavazik.txt"; // Appartient à la classe et ne peut plus être changé
    private File f;

    // Constructor
    public GestionCatalogue(int unRole) {
        this.roleUtilisateur = unRole;
    }

    // Petite classe interne pour mieux utiliser les autres classes du projet
    private static class LigneCatalogue {
        private int id;
        private Artiste artiste;
        private Morceaux morceau;
        private Groupe groupe;
        private Album album;

        public LigneCatalogue(int id, Artiste artiste, Morceaux morceau, Groupe groupe, Album album) {
            this.id = id;
            this.artiste = artiste;
            this.morceau = morceau;
            this.groupe = groupe;
            this.album = album;
        }

        public int getId() {
            return id;
        }

        public Artiste getArtiste() {
            return artiste;
        }

        public Morceaux getMorceau() {
            return morceau;
        }

        public Groupe getGroupe() {
            return groupe;
        }

        public Album getAlbum() {
            return album;
        }
    }

    // Méthode pour verif si l'utilisateur est admin (méthode juste au cas où)
    private boolean verifAdmin() {
        if (this.roleUtilisateur != 2) {
            System.out.println("Impossible d'utiliser les fonctions de cette classe car le rôle Admin est necessaire");
            return false;
        } else {
            return true;
        }
    }

    // Méthode pour verifier l'existance du fichier lié au catalogue
    // S'il n'existe pas, nous devons le créer
    private File verifierFichierCatalogue() {
        // Nouvelle instance de fichier
        File fichier = new File(nomFichierCatalogue);

        // Try - catch pour une bonne maintenance
        try {
            // Verifier l'existance
            if (fichier.createNewFile()) {
                System.out.println("Le fichier " + nomFichierCatalogue + " existe maintenant");
            }
        } catch (IOException e) {
            System.out.println("Erreur dans la création du fichier " + nomFichierCatalogue);
            e.printStackTrace();
        }

        // Retourner le fichier
        return fichier;
    }

    // Méthode pour récupérer le prochain ID disponible
    private int recupererProchainIdCatalogue() {
        this.f = verifierFichierCatalogue();

        int maxId = 0;

        try (BufferedReader r = new BufferedReader(new FileReader(this.f))) {
            String ligne;

            while ((ligne = r.readLine()) != null) {
                String[] parts = ligne.split(";", -1);

                if (parts.length >= 1) {
                    try {
                        int id = Integer.parseInt(parts[0]);
                        if (id > maxId) {
                            maxId = id;
                        }
                    } catch (NumberFormatException e) {
                        // On ignore les lignes invalides
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Problème avec la lecture du catalogue");
            e.printStackTrace();
        }

        return maxId + 1;
    }

    // Transformer une ligne du fichier en objet utilisable
    private LigneCatalogue convertirLigneEnObjet(String[] parts) {
        if (parts == null || parts.length < 6) {
            return null;
        }

        int id;

        try {
            id = Integer.parseInt(parts[0]);
        } catch (NumberFormatException e) {
            return null;
        }

        Artiste unArtiste = new Artiste(id, parts[1], "", parts[1]);
        Morceaux unMorceau = new Morceaux(id, parts[2], convertirTempsEnDouble(parts[3]));

        Groupe unGroupe = null;
        if (parts[4] != null && !parts[4].isEmpty()) {
            unGroupe = new Groupe(id, parts[4]);
        }

        Album unAlbum = null;
        if (parts[5] != null && !parts[5].isEmpty()) {
            unAlbum = new Album(id, parts[5]);
        }

        return new LigneCatalogue(id, unArtiste, unMorceau, unGroupe, unAlbum);
    }

    // Convertir le temps si possible
    private double convertirTempsEnDouble(String temps) {
        if (temps == null || temps.isEmpty()) {
            return 0;
        }

        try {
            return Double.parseDouble(temps);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    // Recréer une ligne du catalogue proprement
    private String construireLigneCatalogue(int id, String artiste, String morceau, String temps, String groupe, String album) {
        StringBuilder ligne = new StringBuilder();

        ligne.append(id).append(";");
        ligne.append(artiste != null ? artiste : "").append(";");
        ligne.append(morceau != null ? morceau : "").append(";");
        ligne.append(temps != null ? temps : "").append(";");
        ligne.append(groupe != null ? groupe : "").append(";");
        ligne.append(album != null ? album : "");

        return ligne.toString();
    }

    // Lire toutes les lignes du catalogue
    private ArrayList<String[]> lireToutesLesLignesCatalogue() {
        this.f = verifierFichierCatalogue();

        ArrayList<String[]> lignes = new ArrayList<String[]>();

        try (BufferedReader r = new BufferedReader(new FileReader(this.f))) {
            String ligne;

            while ((ligne = r.readLine()) != null) {
                String[] parts = ligne.split(";", -1);

                if (parts.length >= 6) {
                    lignes.add(parts);
                }
            }
        } catch (IOException e) {
            System.out.println("Erreur lecture du catalogue");
            e.printStackTrace();
        }

        return lignes;
    }

    // Réécrire tout le catalogue
    private void reecrireCatalogue(List<String[]> lignes) {
        this.f = verifierFichierCatalogue();

        try (BufferedWriter w = new BufferedWriter(new FileWriter(this.f, false))) {
            for (int i = 0; i < lignes.size(); i++) {
                String[] parts = lignes.get(i);

                String ligne = construireLigneCatalogue(
                        i + 1,
                        parts[1],
                        parts[2],
                        parts[3],
                        parts[4],
                        parts[5]
                );

                w.write(ligne);
                w.newLine();
            }
        } catch (IOException e) {
            System.out.println("Une erreur s'est produite durant l'écriture du fichier");
            e.printStackTrace();
        }
    }

    // Méthode pour rechercher dans le catalogue pour l'admin
    public ArrayList<String[]> rechercherDansCatalogueAdmin(Scanner u) {
        if (!verifAdmin()) {
            return new ArrayList<String[]>();
        }

        this.f = verifierFichierCatalogue();

        ArrayList<String[]> resultats = new ArrayList<String[]>();

        System.out.println("- Recherche dans le catalogue -");
        System.out.println("Vous pouvez rechercher par : artiste / morceau / temps / groupe / album");
        System.out.print("Votre critère : ");
        String critere = u.nextLine().toLowerCase().trim();

        System.out.print("Votre recherche : ");
        String recherche = u.nextLine().trim();

        int indexRecherche = -1;

        if (critere.equals("artiste")) {
            indexRecherche = 1;
        } else if (critere.equals("morceau")) {
            indexRecherche = 2;
        } else if (critere.equals("temps")) {
            indexRecherche = 3;
        } else if (critere.equals("groupe")) {
            indexRecherche = 4;
        } else if (critere.equals("album")) {
            indexRecherche = 5;
        } else {
            System.out.println("Critère invalide");
            return resultats;
        }

        try (BufferedReader r = new BufferedReader(new FileReader(this.f))) {
            String ligne;

            while ((ligne = r.readLine()) != null) {
                String[] parts = ligne.split(";", -1);

                if (parts.length >= 6) {
                    if (parts[indexRecherche] != null && parts[indexRecherche].equalsIgnoreCase(recherche)) {
                        resultats.add(parts);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Erreur lecture catalogue");
            e.printStackTrace();
        }

        if (resultats.isEmpty()) {
            System.out.println("Aucun résultat trouvé");
        } else {
            System.out.println("Résultat(s) trouvé(s) :");
            for (int i = 0; i < resultats.size(); i++) {
                String[] res = resultats.get(i);
                LigneCatalogue uneLigne = convertirLigneEnObjet(res);

                if (uneLigne != null) {
                    System.out.println(
                            (i + 1) + " : ID : " + uneLigne.getId() +
                                    " | Artiste : " + uneLigne.getArtiste().getNom() +
                                    " | Morceau : " + uneLigne.getMorceau().getTitre() +
                                    " | Temps : " + res[3] +
                                    " | Groupe : " + (uneLigne.getGroupe() != null ? uneLigne.getGroupe().getNom() : "") +
                                    " | Album : " + (uneLigne.getAlbum() != null ? uneLigne.getAlbum().getNomAlbum() : "")
                    );
                }
            }
        }

        return resultats;
    }

    // Méthode pour ajouter dans le catalogue
    public void ajouterUneLigneDansCatalogue(Map<String, String> ligneAjout) {
        if (!verifAdmin()) {
            return;
        }

        if (ligneAjout == null || ligneAjout.isEmpty()) {
            System.out.println("Aucune donnée à ajouter");
            return;
        }

        // Verifier le fichier
        this.f = verifierFichierCatalogue();

        // Réajustement automatique des données dans le bon ordre
        String artiste = "";
        String morceau = "";
        String temps = "";
        String groupe = "";
        String album = "";

        for (Map.Entry<String, String> entree : ligneAjout.entrySet()) {
            String cle = entree.getKey();
            String valeur = entree.getValue();

            if (cle != null) {
                cle = cle.toLowerCase().trim();

                if (cle.equals("artiste")) {
                    artiste = valeur != null ? valeur.trim() : "";
                } else if (cle.equals("morceau")) {
                    morceau = valeur != null ? valeur.trim() : "";
                } else if (cle.equals("temps")) {
                    temps = valeur != null ? valeur.trim() : "";
                } else if (cle.equals("groupe")) {
                    groupe = valeur != null ? valeur.trim() : "";
                } else if (cle.equals("album")) {
                    album = valeur != null ? valeur.trim() : "";
                }
            }
        }

        // Verif s'il y a les élements necessaires
        if (artiste.isEmpty() || morceau.isEmpty()) {
            System.out.println("Vous devez au moins donner l'artiste et le morceau pour pouvoir ajouter quelque chose dans le catalogue");
            return;
        }

        int nouvelId = recupererProchainIdCatalogue();
        String ligne = construireLigneCatalogue(nouvelId, artiste, morceau, temps, groupe, album);

        // Inscription dans le fichier
        try (BufferedWriter w = new BufferedWriter(new FileWriter(this.f, true))) {
            w.write(ligne);
            w.newLine();
            System.out.println("La ligne a bien été ajoutée dans le catalogue");
        } catch (IOException e) {
            System.out.println("Une erreur s'est produite durant l'écriture du fichier");
            e.printStackTrace();
        }
    }

    // Méthode pour supprimer dans le catalogue
    public void supprimerUneLigneDansCatalogue(Scanner u, ArrayList<String[]> res) {
        if (!verifAdmin()) {
            return;
        }

        // Verifier le fichier
        this.f = verifierFichierCatalogue();

        if (res == null || res.isEmpty()) {
            System.out.println("Aucune ligne à supprimer");
            return;
        }

        // Demander quelle ligne supprimer
        System.out.println("Quelle ligne voulez vous supprimer ?");
        for (int i = 0; i < res.size(); i++) {
            LigneCatalogue uneLigne = convertirLigneEnObjet(res.get(i));

            if (uneLigne != null) {
                System.out.println(
                        (i + 1) + " : ID : " + uneLigne.getId() +
                                " | Artiste : " + uneLigne.getArtiste().getNom() +
                                " | Morceau : " + uneLigne.getMorceau().getTitre() +
                                " | Temps : " + res.get(i)[3] +
                                " | Groupe : " + (uneLigne.getGroupe() != null ? uneLigne.getGroupe().getNom() : "") +
                                " | Album : " + (uneLigne.getAlbum() != null ? uneLigne.getAlbum().getNomAlbum() : "")
                );
            }
        }

        System.out.print("Votre choix : ");
        String entree = u.nextLine();

        int choix = -1;
        try {
            choix = Integer.parseInt(entree);
        } catch (NumberFormatException e) {
            System.out.println("Choix invalide");
            return;
        }

        if (choix < 1 || choix > res.size()) {
            System.out.println("Choix invalide");
            return;
        }

        String[] ligneASupprimer = res.get(choix - 1);

        // Confirmation
        System.out.println("Vous allez supprimer la ligne suivante : ");
        System.out.println(
                "ID : " + ligneASupprimer[0] +
                        " | Artiste : " + ligneASupprimer[1] +
                        " | Morceau : " + ligneASupprimer[2] +
                        " | Temps : " + ligneASupprimer[3] +
                        " | Groupe : " + ligneASupprimer[4] +
                        " | Album : " + ligneASupprimer[5]
        );
        System.out.print("Confirmer ? (oui/non) : ");
        String confirmation = u.nextLine();

        if (!confirmation.equalsIgnoreCase("oui")) {
            System.out.println("Suppression annulée");
            return;
        }

        ArrayList<String[]> lignesRestantes = lireToutesLesLignesCatalogue();
        ArrayList<String[]> nouvellesLignes = new ArrayList<String[]>();

        for (String[] ligneCatalogue : lignesRestantes) {
            if (!ligneCatalogue[0].equals(ligneASupprimer[0])) {
                nouvellesLignes.add(ligneCatalogue);
            }
        }

        reecrireCatalogue(nouvellesLignes);
        System.out.println("La ligne a bien été supprimée et le fichier a été mis à jour");
    }

    // Méthode pour modifier dans le catalogue
    public void modifierUneLigneDansCatalogue(Scanner u, ArrayList<String[]> resultatRecherche) {
        if (!verifAdmin()) {
            return;
        }

        if (resultatRecherche == null || resultatRecherche.isEmpty()) {
            System.out.println("Aucune ligne à modifier");
            return;
        }

        // Vérifier le fichier
        this.f = verifierFichierCatalogue();

        System.out.println("- Modification d'une ligne -");
        System.out.println("Modifiez ce que vous voulez. Si vous ne voulez pas modifier, appuyez sur Entrée");

        // On stocke UNE nouvelle ligne par résultat
        ArrayList<String> nouvellesLignes = new ArrayList<String>();

        for (String[] ligneActuelle : resultatRecherche) {

            StringBuilder nouvelleLigne = new StringBuilder();

            // ID (non modifiable)
            nouvelleLigne.append(ligneActuelle[0]).append(";");

            System.out.println("Artiste avant : " + ligneActuelle[1]);
            String artiste = u.nextLine();
            nouvelleLigne.append(artiste.isEmpty() ? ligneActuelle[1] : artiste).append(";");

            System.out.println("Morceau avant : " + ligneActuelle[2]);
            String morceau = u.nextLine();
            nouvelleLigne.append(morceau.isEmpty() ? ligneActuelle[2] : morceau).append(";");

            System.out.println("Temps avant : " + ligneActuelle[3]);
            String temps = u.nextLine();
            nouvelleLigne.append(temps.isEmpty() ? ligneActuelle[3] : temps).append(";");

            System.out.println("Groupe avant : " + ligneActuelle[4]);
            String groupe = u.nextLine();
            nouvelleLigne.append(groupe.isEmpty() ? ligneActuelle[4] : groupe).append(";");

            System.out.println("Album avant : " + ligneActuelle[5]);
            String album = u.nextLine();
            nouvelleLigne.append(album.isEmpty() ? ligneActuelle[5] : album);

            nouvellesLignes.add(nouvelleLigne.toString());
        }

        // Modification du fichier
        try {
            File tempFile = new File("tempCatalogue.txt");
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
            BufferedReader reader = new BufferedReader(new FileReader(this.f));

            String ligne;
            while ((ligne = reader.readLine()) != null) {
                String[] parts = ligne.split(";", -1);

                boolean ligneModifiee = false;

                for (int i = 0; i < resultatRecherche.size(); i++) {
                    if (parts.length >= 1 && parts[0].equals(resultatRecherche.get(i)[0])) {

                        writer.write(nouvellesLignes.get(i));
                        writer.newLine();

                        ligneModifiee = true;
                        break;
                    }
                }

                if (!ligneModifiee) {
                    writer.write(ligne);
                    writer.newLine();
                }
            }

            reader.close();
            writer.close();

            if (this.f.delete()) {
                tempFile.renameTo(this.f);
            }

        } catch (IOException e) {
            System.out.println("Une erreur s'est produite durant la modification du fichier");
            e.printStackTrace();
            return;
        }

        System.out.println("Vos modifications ont été prises en compte");
    }

    /* 0 : Artiste
     * 1 : Morceau
     * Ajout : Temps musique (2)
     * 3 : Eventuellement le groupe lié
     * 4 : Eventuellement l'album lié */
    // Méthode pour rentrer les infos necessaires
    public Map<String, String> recupInfosPourCatalogue(Scanner u) {
        if (!verifAdmin()) {
            return null;
        }

        Map<String, String> infos = new LinkedHashMap<String, String>();

        System.out.println("- Ajout dans le catalogue -");
        System.out.println("Les champs Artiste et Morceau sont obligatoires");
        System.out.print("Artiste : ");
        String artiste = u.nextLine();

        System.out.print("Morceau : ");
        String morceau = u.nextLine();

        System.out.print("Temps : ");
        String temps = u.nextLine();

        System.out.print("Groupe : ");
        String groupe = u.nextLine();

        System.out.print("Album : ");
        String album = u.nextLine();

        infos.put("artiste", artiste);
        infos.put("morceau", morceau);
        infos.put("temps", temps);
        infos.put("groupe", groupe);
        infos.put("album", album);

        return infos;
    }

     //Fonction Antoine
     //Calcule les statistiques liées au catalogue et met a jour StatistiquesAdmin
     //Utilise HashSet pour s'assurer de ne compter que les éléments uniques
    public void mettreAJourStatistiquesCatalogue(StatistiquesAdmin stats) {
        if (!verifAdmin()) {
            return;
        }
        // On recup tout le contenu du fichier
        ArrayList<String[]> lignes = lireToutesLesLignesCatalogue();
        // nb morceaux = nb lignes fichier
        int nombreMorceaux = lignes.size();
        // HashSet pour stocker toutes les instances uniques
        HashSet<String> artistesUniques = new HashSet<>();
        HashSet<String> groupesUniques = new HashSet<>();
        HashSet<String> albumsUniques = new HashSet<>();

        for (String[] ligne : lignes) {
            // check que la ligne à le bon format
            if (ligne.length >= 6) {
                // 1 = artiste
                if (ligne[1] != null && !ligne[1].trim().isEmpty()) {
                    artistesUniques.add(ligne[1].trim().toLowerCase());
                }
                // 4 = groupe
                if (ligne[4] != null && !ligne[4].trim().isEmpty()) {
                    groupesUniques.add(ligne[4].trim().toLowerCase());
                }
                // 5 = album
                if (ligne[5] != null && !ligne[5].trim().isEmpty()) {
                    albumsUniques.add(ligne[5].trim().toLowerCase());
                }
            }
        }

        // envoi à stats admin
        stats.setNombreMorceaux(nombreMorceaux);
        stats.setNombreArtistes(artistesUniques.size());
        stats.setNombreGroupes(groupesUniques.size());
        stats.setNombreAlbums(albumsUniques.size());
    }
}