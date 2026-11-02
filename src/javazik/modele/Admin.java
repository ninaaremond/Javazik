package javazik.modele;

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Classe représentant un administrateur du système.
 * Hérite de Utilisateur avec droits = 2
 */
public class Admin extends Utilisateur implements Serializable {
    private static final long serialVersionUID = 1L;

    private String nomAdmin;
    private String prenomAdmin; // Ajout du prénom pour uniformiser
    private LocalDateTime dateCreation;
    private boolean actif;
    private ArrayList<Playlist> playlists;
    private int nextPlaylistId;

    /**
     * Constructeur d'un administrateur
     * @param id
     * @param mdp
     * @param email
     * @param nomAdmin
     * @param prenomAdmin
     */

    public Admin(int id, String mdp, String email, String nomAdmin, String prenomAdmin) {
        super(id, mdp, email, 2); // droits = 2 pour admin
        this.nomAdmin = nomAdmin;
        this.prenomAdmin = prenomAdmin;
        this.dateCreation = LocalDateTime.now();
        this.actif = true;
        this.playlists = new ArrayList<>();
        this.nextPlaylistId = 1;
    }

    public void creerCompte() {
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
            
            // Écrire le nouvel administrateur avec l'ID séquentiel
            try (FileWriter fw = new FileWriter("utilisateur.txt", true)) {
                fw.write(newId + ";2;" + mdp + ";" + email + ";" + nomAdmin + ";" + prenomAdmin + ";true\n");
            }
            
            // Mise à jour de l'ID de cet objet
            this.id = newId;
        } catch (IOException e) { 
            e.printStackTrace(); 
        }
    }

    @Override
    public boolean connexion(String emailSaisi, String mdpSaisi) {
        try (BufferedReader br = new BufferedReader(new FileReader("utilisateur.txt"))) {
            String ligne;
            while ((ligne = br.readLine()) != null) {
                String[] p = ligne.split(";");
                if (p.length >= 7 && p[3].equals(emailSaisi) && p[2].equals(mdpSaisi) && Integer.parseInt(p[1]) == 2) {
                    this.id = Integer.parseInt(p[0]);
                    this.droit = Integer.parseInt(p[1]);
                    this.mdp = p[2];
                    this.email = p[3];
                    this.nomAdmin = p[4];
                    this.prenomAdmin = p[5];
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

    // Getters
    public String getNomAdmin() { return nomAdmin; }
    public String getPrenomAdmin() { return prenomAdmin; }
    public boolean isActif() { return actif; }

    // Setters
    public void setNomAdmin(String nomAdmin) { this.nomAdmin = nomAdmin; }
    public void setActif(boolean actif) { this.actif = actif; }

    //@Override
    public String afficherInfos() {
        StringBuilder sb = new StringBuilder();
        sb.append("===== Informations Administrateur =====\n");
        sb.append("ID: ").append(id).append("\n");
        sb.append("Nom: ").append(prenomAdmin).append(" ").append(nomAdmin).append("\n");
        sb.append("Email: ").append(email).append("\n");
        sb.append("Statut: ").append(actif ? "Actif" : "Inactif").append("\n");
        return sb.toString();
    }
}