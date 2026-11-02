package javazik.modele;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Album {

    private int idAlbum;
    private String nomAlbum;

    public Album(int idAlbum, String nomAlbum){
        this.idAlbum = idAlbum;
        this.nomAlbum = nomAlbum;
    }

    public int getIdAlbum() {
        return this.idAlbum;
    }

    public String getNomAlbum() {
        return this.nomAlbum;
    }

    // Rechercher un album dans le catalogue txt
    public List<Album> rechercherAlbum(String nom){

        List<Album> albumsTrouves = new ArrayList<>();

        File f = new File(GestionCatalogue.nomFichierCatalogue);

        try (BufferedReader r = new BufferedReader(new FileReader(f))) {

            String ligne;

            while ((ligne = r.readLine()) != null) {

                String[] parts = ligne.split(";", -1);

                // ID;Artiste;Morceau;Temps;Groupe;Album
                if (parts.length >= 6) {

                    String nomAlbum = parts[5];

                    if (nomAlbum != null && !nomAlbum.isEmpty() && nomAlbum.equalsIgnoreCase(nom)) {

                        int id = Integer.parseInt(parts[0]);
                        albumsTrouves.add(new Album(id, nomAlbum));
                    }
                }
            }

        } catch (IOException e) {
            System.out.println("Erreur lecture catalogue");
            e.printStackTrace();
        }

        return albumsTrouves;
    }

    public static void ecouterAlbum(List<Album> resultatsRecherche){

        if (resultatsRecherche == null || resultatsRecherche.isEmpty()) {
            System.out.println("Aucun album trouvé");
            return;
        }

        for (Album alb : resultatsRecherche) {
            System.out.println("Album : " + alb.getNomAlbum());
        }
    }
}