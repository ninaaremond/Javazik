package javazik.modele;

import java.io.*;
import java.util.*;

public class Client extends Utilisateur {
    private String nom, prenom;
    private ArrayList<Playlist> playlists;
    private int nextPlaylistId;
    private int compteurLectures;

    public Client(int id, String mdp, String email, String nom, String prenom) {
        super(id, mdp, email, 1);
        this.nom = nom;
        this.prenom = prenom;
        this.playlists = new ArrayList<>();
        this.nextPlaylistId = 1;
        this.compteurLectures = 0;
    }

    public void creerCompte() {
        creerCompte(1);
    }

    public void creerCompte(int droit) {
        try {
            // Trouver le prochain ID (le plus grand ID + 1)
            int maxId = 0;
            try (BufferedReader br = new BufferedReader(new FileReader("utilisateur.txt"))) {
                String ligne;
                while ((ligne = br.readLine()) != null) {
                    String[] p = ligne.split(";");
                    if (p.length >= 1) {
                        try {
                            int id = Integer.parseInt(p[0]);
                            if (id > maxId) {
                                maxId = id;
                            }
                        } catch (NumberFormatException e) {
                            // Ignorer les lignes avec des IDs invalides
                        }
                    }
                }
            }
            
            int newId = maxId + 1;
            
            // Écrire le nouvel utilisateur avec l'ID séquentiel
            try (FileWriter fw = new FileWriter("utilisateur.txt", true)) {
                fw.write(newId + ";" + droit + ";" + mdp + ";" + email + ";" + nom + ";" + prenom + ";true\n");
            }
            
            // Mise à jour de l'ID de cet objet
            this.id = newId;
        } catch (IOException e) { 
            e.printStackTrace(); 
        }
    }

    public String getNom() { return nom; }
    public String getPrenom() { return prenom; }

    @Override
    public boolean connexion(String emailSaisi, String mdpSaisi) {
        try (BufferedReader br = new BufferedReader(new FileReader("utilisateur.txt"))) {
            String ligne;
            while ((ligne = br.readLine()) != null) {
                String[] p = ligne.split(";");
                if (p.length >= 7 && p[3].equals(emailSaisi) && p[2].equals(mdpSaisi)) {
                    this.id = Integer.parseInt(p[0]);
                    this.droit = Integer.parseInt(p[1]);
                    this.mdp = p[2];
                    this.email = p[3];
                    this.nom = p[4];
                    this.prenom = p[5];
                    return true;
                }
            }
        } catch (IOException e) { e.printStackTrace(); }
        return false;
    }

    public void chargerPlaylists() {
        playlists.clear();
        nextPlaylistId = 1;
        String fichier = "Playlist" + id + ".txt";
        File f = new File(fichier);
        
        if (!f.exists()) {
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(fichier))) {
            String ligne;
            while ((ligne = br.readLine()) != null) {
                String[] parts = ligne.split(";", 3);
                if (parts.length >= 2) {
                    int playlistId = Integer.parseInt(parts[0]);
                    String playlistNom = parts[1];
                    Playlist p = new Playlist(playlistId, playlistNom);
                    
                    if (parts.length > 2 && !parts[2].trim().isEmpty()) {
                        String[] musicIds = parts[2].split(",");
                        for (String musicId : musicIds) {
                            if (!musicId.trim().isEmpty()) {
                                p.ajouterMusicID(Integer.parseInt(musicId.trim()));
                            }
                        }
                    }
                    
                    playlists.add(p);
                    if (playlistId >= nextPlaylistId) {
                        nextPlaylistId = playlistId + 1;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement des playlists : " + e.getMessage());
        }
    }

    public void sauvegarderPlaylists() {
        String fichier = "Playlist" + id + ".txt";
        
        try (FileWriter fw = new FileWriter(fichier)) {
            for (Playlist p : playlists) {
                fw.write(p.toFileFormat() + "\n");
            }
        } catch (IOException e) {
            System.out.println("Erreur lors de la sauvegarde des playlists : " + e.getMessage());
        }
    }

    public void ajouterPlaylist(String nom) {
        Playlist p = new Playlist(nextPlaylistId++, nom);
        playlists.add(p);
        sauvegarderPlaylists();
    }

    public ArrayList<Playlist> getPlaylists() {
        return playlists;
    }

    public Playlist getPlaylist(String nom) {
        for (Playlist p : playlists) {
            if (p.getNom().equals(nom)) {
                return p;
            }
        }
        return null;
    }

    public ArrayList<String> getNomPlaylists() {
        ArrayList<String> noms = new ArrayList<>();
        for (Playlist p : playlists) {
            noms.add(p.getNom());
        }
        return noms;
    }

    public void supprimerPlaylist(String nom) {
        playlists.removeIf(p -> p.getNom().equals(nom));
        sauvegarderPlaylists();
    }
    
    public int getCompteurLectures() {
        return compteurLectures;
    }
    
    public void incrementerCompteurLectures() {
        compteurLectures++;
    }
    
    public void reinitialiserCompteurLectures() {
        compteurLectures = 0;
    }
    
    public boolean peutLire() {
        return compteurLectures < 5;
    }
}
