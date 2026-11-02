package javazik.controleur;

import java.io.*;
import java.util.*;
import javazik.modele.*;

public class MainController {
    private Utilisateur user;
    private LecteurAudio lecteur;

    public MainController() {
        this.lecteur = new LecteurAudio();
    }

    public boolean connexion(String email, String mdp) {
        // Essayer de charger comme client d'abord
        Client client = new Client(0, mdp, email, "", "");
        if (client.connexion(email, mdp)) {
            System.out.println("DEBUG - Connexion client réussie. ID chargé: " + client.getId() + ", Droit: " + client.getDroit());
            
            // Vérifier le droit pour créer le bon type d'utilisateur
            if (client.getDroit() == 2) {
                // C'est un admin, créer un objet Admin
                int adminId = client.getId();
                Admin admin = new Admin(adminId, mdp, email, "", "");
                if (admin.connexion(email, mdp)) {
                    user = admin;
                    System.out.println("DEBUG - Connexion admin réussie. ID de l'admin: " + admin.getId());
                    admin.chargerPlaylists();
                    System.out.println("Connexion réussie (Admin) : " + email + " (ID: " + admin.getId() + ")");
                    return true;
                }
            } else {
                // C'est un client
                user = client;
                System.out.println("DEBUG - Client défini. ID du client: " + client.getId());
                client.chargerPlaylists();
                System.out.println("Connexion réussie (Client) : " + email + " (ID: " + client.getId() + ")");
                return true;
            }
        }
        System.out.println("Connexion échouée");
        return false;
    }

    public boolean inscription(String email, String mdp, String nom, String prenom, int droit) {
        try {
            // Vérifier si l'email existe déjà
            Client client = new Client(0, mdp, email, "", "");
            if (client.connexion(email, mdp)) {
                System.out.println("Email existe déjà");
                return false;
            }
            
            // Créer le compte selon le droit
            if (droit == 2) {
                // Créer un admin
                Admin admin = new Admin(0, mdp, email, nom, prenom);
                admin.creerCompte();
                
                // Charger l'utilisateur
                if (admin.connexion(email, mdp)) {
                    user = admin;
                    admin.chargerPlaylists();
                    System.out.println("Inscription réussie (Admin) : " + email);
                    return true;
                }
            } else {
                // Créer un client
                Client newClient = new Client(0, mdp, email, nom, prenom);
                newClient.creerCompte(1);
                
                // Charger l'utilisateur
                if (newClient.connexion(email, mdp)) {
                    user = newClient;
                    newClient.chargerPlaylists();
                    System.out.println("Inscription réussie (Client) : " + email);
                    return true;
                }
            }
        } catch (Exception e) {
            System.out.println("Erreur inscription : " + e.getMessage());
        }
        return false;
    }

    public void modeVisiteur() {
        user = new Visiteur();
        System.out.println("Mode visiteur activé");
    }
    
    public void deconnexion() {
        if (user instanceof Client) {
            ((Client) user).sauvegarderPlaylists();
        } else if (user instanceof Admin) {
            ((Admin) user).sauvegarderPlaylists();
        }
        user = null;
        System.out.println("Déconnexion réussie");
    }

    
    public ArrayList<String[]> chargerTousMorceaux() {
        ArrayList<String[]> morceaux = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("catalogueJavazik.txt"))) {
            String ligne;
            while ((ligne = br.readLine()) != null) {
                String[] data = ligne.split(";", -1);
                morceaux.add(data);
            }
        } catch (IOException e) {
            System.out.println("Erreur lors de la lecture du catalogue : " + e.getMessage());
        }
        return morceaux;
    }

    public ArrayList<String[]> rechercher(Map<String, String> criteres) {
        if (user != null) {
            return user.rechercherDansCatalogue(criteres);
        }
        return new ArrayList<>();
    }

    public ArrayList<String[]> rechercherParArtiste(String artiste) {
        Map<String, String> criteres = new HashMap<>();
        criteres.put("Artiste", artiste);
        return rechercher(criteres);
    }

    public ArrayList<String[]> rechercherParMorceau(String morceau) {
        Map<String, String> criteres = new HashMap<>();
        criteres.put("Morceau", morceau);
        return rechercher(criteres);
    }

    public ArrayList<String[]> rechercherParCatalogueSimple(String terme, String type) {
        ArrayList<String[]> resultats = new ArrayList<>();
        ArrayList<String[]> catalogue = chargerTousMorceaux();
        
        for (String[] morceau : catalogue) {
            boolean match = false;
            // Format: ID;Artiste;Titre;Durée;Album;Groupe
            switch (type.toLowerCase()) {
                case "artiste":
                    if (morceau.length > 1 && morceau[1].toLowerCase().contains(terme.toLowerCase())) {
                        match = true;
                    }
                    break;
                case "artistegroupe":
                    // Chercher dans Artiste (index 1) OU Groupe (index 5)
                    if ((morceau.length > 1 && morceau[1].toLowerCase().contains(terme.toLowerCase())) ||
                        (morceau.length > 5 && morceau[5].toLowerCase().contains(terme.toLowerCase()))) {
                        match = true;
                    }
                    break;
                case "morceau":
                    if (morceau.length > 2 && morceau[2].toLowerCase().contains(terme.toLowerCase())) {
                        match = true;
                    }
                    break;
                case "album":
                    if (morceau.length > 4 && morceau[4].toLowerCase().contains(terme.toLowerCase())) {
                        match = true;
                    }
                    break;
                case "groupe":
                    if (morceau.length > 5 && morceau[5].toLowerCase().contains(terme.toLowerCase())) {
                        match = true;
                    }
                    break;
            }
            if (match) {
                resultats.add(morceau);
            }
        }
        
        return resultats;
    }

    public ArrayList<String> chargerPlaylists() {
        if (user instanceof Client) {
            return ((Client) user).getNomPlaylists();
        } else if (user instanceof Admin) {
            return ((Admin) user).getNomPlaylists();
        }
        return new ArrayList<>();
    }

    public void creerPlaylist(String nom) {
        if (user instanceof Client) {
            ((Client) user).ajouterPlaylist(nom);
        } else if (user instanceof Admin) {
            ((Admin) user).ajouterPlaylist(nom);
        }
    }

    public Playlist obtenirPlaylist(String nom) {
        if (user instanceof Client) {
            return ((Client) user).getPlaylist(nom);
        } else if (user instanceof Admin) {
            return ((Admin) user).getPlaylist(nom);
        }
        return null;
    }

    public ArrayList<String[]> obtenirMorceauxPlaylist(String nomPlaylist) {
        Playlist playlist = obtenirPlaylist(nomPlaylist);
        if (playlist == null) {
            return new ArrayList<>();
        }

        ArrayList<String[]> catalogue = chargerTousMorceaux();
        ArrayList<String[]> morceaux = new ArrayList<>();
        
        for (int musicID : playlist.getMusicsID()) {
            // Chercher la musique avec cet ID dans le catalogue
            // Le premier champ est censé être l'ID
            for (String[] morceau : catalogue) {
                if (morceau.length > 0 && morceau[0].equals(String.valueOf(musicID))) {
                    morceaux.add(morceau);
                    break;
                }
            }
        }
        
        return morceaux;
    }

    public int obtenirMusicID(String titreMorceau) {
        ArrayList<String[]> catalogue = chargerTousMorceaux();
        for (String[] morceau : catalogue) {
            // Format: ID;Artiste;Titre;Durée;...
            // morceau[2] contient le titre
            if (morceau.length > 2 && morceau[2].toLowerCase().contains(titreMorceau.toLowerCase())) {
                try {
                    return Integer.parseInt(morceau[0]);
                } catch (NumberFormatException e) {
                    return -1;
                }
            }
        }
        return -1;
    }

    public boolean ajouterMorceauAPlaylist(String nomPlaylist, String titreMorceau) {
        int musicID = obtenirMusicID(titreMorceau);
        if (musicID == -1) {
            return false;
        }

        Playlist playlist = obtenirPlaylist(nomPlaylist);
        if (playlist != null) {
            playlist.ajouterMusicID(musicID);
            if (user instanceof Client) {
                ((Client) user).sauvegarderPlaylists();
            } else if (user instanceof Admin) {
                ((Admin) user).sauvegarderPlaylists();
            }
            return true;
        }
        return false;
    }

    public Utilisateur getUser() { return user; }
    
    public boolean modifierMotDePasse(String nouveauMdp) {
        if (user == null) return false;
        
        try {
            // Lire tous les utilisateurs
            ArrayList<String> lines = new ArrayList<>();
            try (BufferedReader br = new BufferedReader(new FileReader("utilisateur.txt"))) {
                String ligne;
                while ((ligne = br.readLine()) != null) {
                    String[] parts = ligne.split(";");
                    if (parts.length >= 3 && Integer.parseInt(parts[0]) == user.getId()) {
                        // Modifier le mot de passe (index 2)
                        parts[2] = nouveauMdp;
                        lines.add(String.join(";", parts));
                    } else {
                        lines.add(ligne);
                    }
                }
            }
            
            // Réécrire le fichier
            try (FileWriter fw = new FileWriter("utilisateur.txt")) {
                for (String line : lines) {
                    fw.write(line + "\n");
                }
            }
            
            // Mettre à jour l'objet utilisateur
            user.setMdp(nouveauMdp);
            return true;
        } catch (IOException e) {
            System.out.println("Erreur lors de la modification du mot de passe : " + e.getMessage());
            return false;
        }
    }
    
    public boolean supprimerCompte() {
        if (user == null) return false;
        
        try {
            // Lire tous les utilisateurs sauf celui à supprimer
            ArrayList<String> lines = new ArrayList<>();
            try (BufferedReader br = new BufferedReader(new FileReader("utilisateur.txt"))) {
                String ligne;
                while ((ligne = br.readLine()) != null) {
                    String[] parts = ligne.split(";");
                    if (parts.length >= 1 && Integer.parseInt(parts[0]) != user.getId()) {
                        lines.add(ligne);
                    }
                }
            }
            
            // Réécrire le fichier sans cet utilisateur
            try (FileWriter fw = new FileWriter("utilisateur.txt")) {
                for (String line : lines) {
                    fw.write(line + "\n");
                }
            }
            
            // Supprimer les playlists de cet utilisateur
            String fichierPlaylist = "Playlist" + user.getId() + ".txt";
            File f = new File(fichierPlaylist);
            if (f.exists()) {
                f.delete();
            }
            
            // Déconnecter l'utilisateur
            user = null;
            return true;
        } catch (IOException e) {
            System.out.println("Erreur lors de la suppression du compte : " + e.getMessage());
            return false;
        }
    }


    public String chargerCatalogueComplet() {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader("catalogueJavazik.txt"))) {
            String ligne;
            while ((ligne = br.readLine()) != null) {
                sb.append(ligne).append("\n");
            }
        } catch (FileNotFoundException e) {
            return "Fichier catalogueJavazik.txt non trouvé";
        } catch (IOException e) {
            return "Erreur lors de la lecture du catalogue : " + e.getMessage();
        }
        return sb.toString();
    }

    public boolean ajouterMorceauCatalogue(String artiste, String titre, String duree, String album) {
        if (artiste.isEmpty() || titre.isEmpty() || duree.isEmpty() || album.isEmpty()) {
            return false;
        }
        
        try {
            // Trouver le prochain ID
            int maxId = 0;
            try (BufferedReader br = new BufferedReader(new FileReader("catalogueJavazik.txt"))) {
                String ligne;
                while ((ligne = br.readLine()) != null) {
                    String[] parts = ligne.split(";");
                    if (parts.length > 0) {
                        try {
                            int id = Integer.parseInt(parts[0]);
                            if (id > maxId) {
                                maxId = id;
                            }
                        } catch (NumberFormatException e) {
                            // Ignorer
                        }
                    }
                }
            }
            
            // Ajouter le nouveau morceau
            int nouvelId = maxId + 1;
            try (FileWriter fw = new FileWriter("catalogueJavazik.txt", true)) {
                fw.write(nouvelId + ";" + artiste + ";" + titre + ";" + duree + ";" + album + ";\n");
            }
            
            System.out.println("Morceau ajouté avec l'ID : " + nouvelId);
            return true;
        } catch (IOException e) {
            System.out.println("Erreur lors de l'ajout du morceau : " + e.getMessage());
            return false;
        }
    }

    public boolean supprimerMorceauCatalogue(int morceauId) {
        try {
            // Lire tous les morceaux sauf celui à supprimer
            ArrayList<String> lignes = new ArrayList<>();
            try (BufferedReader br = new BufferedReader(new FileReader("catalogueJavazik.txt"))) {
                String ligne;
                while ((ligne = br.readLine()) != null) {
                    String[] parts = ligne.split(";");
                    if (parts.length > 0) {
                        try {
                            int id = Integer.parseInt(parts[0]);
                            if (id != morceauId) {
                                lignes.add(ligne);
                            }
                        } catch (NumberFormatException e) {
                            lignes.add(ligne);
                        }
                    }
                }
            }
            
            // Réécrire le fichier sans ce morceau
            try (FileWriter fw = new FileWriter("catalogueJavazik.txt")) {
                for (String ligne : lignes) {
                    fw.write(ligne + "\n");
                }
            }
            
            System.out.println("Morceau avec l'ID " + morceauId + " supprimé");
            return true;
        } catch (IOException e) {
            System.out.println("Erreur lors de la suppression du morceau : " + e.getMessage());
            return false;
        }
    }


    public String chargerUtilisateursComplets() {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader("utilisateur.txt"))) {
            String ligne;
            while ((ligne = br.readLine()) != null) {
                sb.append(ligne).append("\n");
            }
        } catch (FileNotFoundException e) {
            return "Fichier utilisateur.txt non trouvé";
        } catch (IOException e) {
            return "Erreur lors de la lecture des utilisateurs : " + e.getMessage();
        }
        return sb.toString();
    }

    public boolean ajouterUtilisateur(String email, String mdp, String nom, String prenom, int droit) {
        if (email.isEmpty() || mdp.isEmpty() || nom.isEmpty() || prenom.isEmpty()) {
            return false;
        }
        
        try {
            // Vérifier si l'email existe déjà
            try (BufferedReader br = new BufferedReader(new FileReader("utilisateur.txt"))) {
                String ligne;
                while ((ligne = br.readLine()) != null) {
                    String[] parts = ligne.split(";");
                    if (parts.length >= 4 && parts[3].equals(email)) {
                        return false; // Email existe déjà
                    }
                }
            }
            
            // Ajouter le nouvel utilisateur
            try (FileWriter fw = new FileWriter("utilisateur.txt", true)) {
                int nouvelId = new Random().nextInt(10000);
                fw.write(nouvelId + ";" + droit + ";" + mdp + ";" + email + ";" + nom + ";" + prenom + ";true\n");
            }
            
            System.out.println("Utilisateur ajouté : " + email);
            return true;
        } catch (IOException e) {
            System.out.println("Erreur lors de l'ajout d'un utilisateur : " + e.getMessage());
            return false;
        }
    }

    public boolean modifierRoleUtilisateur(String email, int nouveauDroit) {
        try {
            ArrayList<String> lignes = new ArrayList<>();
            boolean trouve = false;
            
            try (BufferedReader br = new BufferedReader(new FileReader("utilisateur.txt"))) {
                String ligne;
                while ((ligne = br.readLine()) != null) {
                    String[] parts = ligne.split(";");
                    if (parts.length >= 4 && parts[3].equals(email)) {
                        // Modifier le droit (index 1)
                        parts[1] = String.valueOf(nouveauDroit);
                        lignes.add(String.join(";", parts));
                        trouve = true;
                    } else {
                        lignes.add(ligne);
                    }
                }
            }
            
            if (!trouve) {
                return false;
            }
            
            try (FileWriter fw = new FileWriter("utilisateur.txt")) {
                for (String ligne : lignes) {
                    fw.write(ligne + "\n");
                }
            }
            
            System.out.println("Rôle modifié pour : " + email);
            return true;
        } catch (IOException e) {
            System.out.println("Erreur lors de la modification du rôle : " + e.getMessage());
            return false;
        }
    }

    public boolean supprimerUtilisateur(String email) {
        try {
            ArrayList<String> lignes = new ArrayList<>();
            boolean trouve = false;
            
            try (BufferedReader br = new BufferedReader(new FileReader("utilisateur.txt"))) {
                String ligne;
                while ((ligne = br.readLine()) != null) {
                    String[] parts = ligne.split(";");
                    if (parts.length >= 4 && parts[3].equals(email)) {
                        trouve = true;
                        // Supprimer les playlists de cet utilisateur
                        try {
                            int userId = Integer.parseInt(parts[0]);
                            String fichierPlaylist = "Playlist" + userId + ".txt";
                            File f = new File(fichierPlaylist);
                            if (f.exists()) {
                                f.delete();
                            }
                        } catch (NumberFormatException e) {
                            // Ignorer
                        }
                    } else {
                        lignes.add(ligne);
                    }
                }
            }
            
            if (!trouve) {
                return false;
            }
            
            try (FileWriter fw = new FileWriter("utilisateur.txt")) {
                for (String ligne : lignes) {
                    fw.write(ligne + "\n");
                }
            }
            
            System.out.println("Utilisateur supprimé : " + email);
            return true;
        } catch (IOException e) {
            System.out.println("Erreur lors de la suppression d'un utilisateur : " + e.getMessage());
            return false;
        }
    }

    public int[] obtenirStatistiques() {
        int nbUtilisateurs = 0;
        int nbClients = 0;
        int nbMorceaux = 0;
        Set<String> artistesUniques = new HashSet<>();
        Set<String> albumsUniques = new HashSet<>();
        
        // Compter les utilisateurs et clients
        try (BufferedReader br = new BufferedReader(new FileReader("utilisateur.txt"))) {
            String ligne;
            while ((ligne = br.readLine()) != null) {
                nbUtilisateurs++;
                String[] parts = ligne.split(";");
                if (parts.length >= 2) {
                    try {
                        int droit = Integer.parseInt(parts[1]);
                        if (droit == 1) {
                            nbClients++;
                        }
                    } catch (NumberFormatException e) {
                        // Ignorer les lignes mal formatées
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Fichier utilisateur.txt non trouvé");
        } catch (IOException e) {
            System.out.println("Erreur lors de la lecture d'utilisateur.txt : " + e.getMessage());
        }
        
        // Compter les morceaux, artistes et albums uniques
        try (BufferedReader br = new BufferedReader(new FileReader("catalogueJavazik.txt"))) {
            String ligne;
            while ((ligne = br.readLine()) != null) {
                nbMorceaux++;
                String[] parts = ligne.split(";");
                if (parts.length >= 2 && !parts[1].trim().isEmpty()) {
                    artistesUniques.add(parts[1].trim());
                }
                if (parts.length >= 5 && !parts[4].trim().isEmpty()) {
                    albumsUniques.add(parts[4].trim());
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Fichier catalogueJavazik.txt non trouvé");
        } catch (IOException e) {
            System.out.println("Erreur lors de la lecture de catalogueJavazik.txt : " + e.getMessage());
        }
        
        // Retourner [nbUtilisateurs, nbClients, nbMorceaux, nbAlbums, nbArtistes, nbGroupes(0), nbEcoutes(0)]
        return new int[]{nbUtilisateurs, nbClients, nbMorceaux, albumsUniques.size(), artistesUniques.size(), 0, 0};
    }

    // ========== Méthodes pour le lecteur audio ==========
    
    /**
     * Lance la lecture d'un fichier audio
     * @param cheminFichier Chemin du fichier audio à lancer
     * @return true si la lecture a démarré
     */
    public boolean lancerMusique(String cheminFichier) {
        return lecteur.lancer(cheminFichier);
    }

    /**
     * Met en pause la musique en cours
     */
    public void pauserMusique() {
        lecteur.pause();
    }

    /**
     * Reprend la musique après une pause
     */
    public void reprendreMusique() {
        lecteur.reprendre();
    }

    /**
     * Arrête complètement la musique
     */
    public void arreterMusique() {
        lecteur.arreter();
    }

    /**
     * Vérifie si une musique est en cours de lecture
     */
    public boolean estMusiqueLancee() {
        return lecteur.estJouant();
    }

    /**
     * Retourne le chemin du fichier en cours de lecture
     */
    public String getFichierActuel() {
        return lecteur.getFichierActuel();
    }

    /**
     * Retourne la durée totale en secondes
     */
    public int getDureeMusique() {
        return lecteur.getDureeTotal();
    }

    /**
     * Retourne la position actuelle en secondes
     */
    public int getPositionMusique() {
        return lecteur.getPositionActuelle();
    }

    // ========== Méthodes pour supprimer playlists et morceaux ==========

    /**
     * Supprime une playlist de l'utilisateur
     */
    public boolean supprimerPlaylist(String nomPlaylist) {
        try {
            if (user instanceof Client) {
                ((Client) user).supprimerPlaylist(nomPlaylist);
                ((Client) user).sauvegarderPlaylists();
                return true;
            } else if (user instanceof Admin) {
                ((Admin) user).supprimerPlaylist(nomPlaylist);
                ((Admin) user).sauvegarderPlaylists();
                return true;
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de la suppression de la playlist : " + e.getMessage());
        }
        return false;
    }

    /**
     * Supprime un morceau d'une playlist
     */
    public boolean supprimerMorceauDePlaylist(String nomPlaylist, String idMorceau) {
        try {
            Playlist playlist = obtenirPlaylist(nomPlaylist);
            if (playlist != null) {
                int idMusic = Integer.parseInt(idMorceau);
                playlist.supprimerMusicID(idMusic);
                
                // Sauvegarder les changements
                if (user instanceof Client) {
                    ((Client) user).sauvegarderPlaylists();
                } else if (user instanceof Admin) {
                    ((Admin) user).sauvegarderPlaylists();
                }
                
                System.out.println("Morceau supprimé de la playlist : " + nomPlaylist);
                return true;
            }
        } catch (NumberFormatException e) {
            System.err.println("Erreur : ID de morceau invalide");
        } catch (Exception e) {
            System.err.println("Erreur lors de la suppression du morceau : " + e.getMessage());
        }
        return false;
    }
    
    public boolean peutLireMusique() {
        if (user instanceof Client) {
            Client client = (Client) user;
            return client.peutLire();
        } else if (user instanceof Visiteur) {
            Visiteur visiteur = (Visiteur) user;
            return visiteur.peutEcouter();
        }
        // Les admins peuvent lire sans limite
        return true;
    }
    
    public void incrementerCompteurLectures() {
        if (user instanceof Client) {
            Client client = (Client) user;
            client.incrementerCompteurLectures();
        } else if (user instanceof Visiteur) {
            Visiteur visiteur = (Visiteur) user;
            visiteur.incrementerEcoute();
        }
    }
    
    public int getCompteurLectures() {
        if (user instanceof Client) {
            Client client = (Client) user;
            return client.getCompteurLectures();
        } else if (user instanceof Visiteur) {
            Visiteur visiteur = (Visiteur) user;
            return visiteur.getCompteurEcoutes();
        }
        return 0;
    }
    
    /**
     * Enregistre une lecture de morceau dans l'historique
     */
    public boolean enregistrerLecture(String[] morceauData) {
        if (user == null) {
            return false;
        }
        
        GestionHistorique gestionHistorique = new GestionHistorique();
        return gestionHistorique.enregistrerLecture(user.getId(), morceauData);
    }

    /**
     * Charge l'historique d'écoute de l'utilisateur
     */
    public ArrayList<String> chargerHistorique() {
        if (user == null) {
            return new ArrayList<>();
        }
        
        GestionHistorique gestionHistorique = new GestionHistorique();
        return gestionHistorique.chargerHistorique(user.getId());
    }

    /**
     * Efface l'historique d'écoute de l'utilisateur
     */
    public boolean effacerHistorique() {
        if (user == null) {
            return false;
        }
        
        GestionHistorique gestionHistorique = new GestionHistorique();
        return gestionHistorique.supprimerHistorique(user.getId());
    }
    
    /**
     * Charge les informations d'un artiste depuis ArtisteGroupe.txt
     * @return Tableau: [nom, année, résumé] ou null si non trouvé
     */
    public String[] chargerInfosArtiste(String nomArtiste) {
        try (BufferedReader br = new BufferedReader(new FileReader("ArtisteGroupe.txt"))) {
            String ligne;
            while ((ligne = br.readLine()) != null) {
                String[] parts = ligne.split(";");
                if (parts.length >= 3 && parts[0].trim().equalsIgnoreCase(nomArtiste.trim())) {
                    System.out.println("DEBUG - Artiste trouvé: " + parts[0].trim());
                    return new String[]{
                        parts[0].trim(),
                        parts[1].trim(),
                        parts[2].trim()
                    };
                }
            }
            System.out.println("DEBUG - Artiste '" + nomArtiste + "' non trouvé");
        } catch (IOException e) {
            System.out.println("Fichier ArtisteGroupe.txt non trouvé");
        }
        return null;
    }
    
    /**
     * Charge tous les morceaux d'un artiste
     */
    public ArrayList<String[]> chargerMorceauxArtiste(String nomArtiste) {
        ArrayList<String[]> morceaux = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("catalogueJavazik.txt"))) {
            String ligne;
            while ((ligne = br.readLine()) != null) {
                String[] parts = ligne.split(";");
                if (parts.length >= 2 && parts[1].trim().equalsIgnoreCase(nomArtiste.trim())) {
                    morceaux.add(parts);
                }
            }
        } catch (IOException e) {
            System.out.println("Fichier catalogueJavazik.txt non trouvé");
        }
        return morceaux;
    }
    
    /**
     * Charge tous les albums uniques d'un artiste
     */
    public java.util.Set<String> chargerAlbumsArtiste(String nomArtiste) {
        java.util.Set<String> albums = new java.util.LinkedHashSet<>();
        try (BufferedReader br = new BufferedReader(new FileReader("catalogueJavazik.txt"))) {
            String ligne;
            while ((ligne = br.readLine()) != null) {
                String[] parts = ligne.split(";");
                if (parts.length >= 5 && parts[1].trim().equalsIgnoreCase(nomArtiste.trim())) {
                    albums.add(parts[4].trim());
                }
            }
        } catch (IOException e) {
            System.out.println("Fichier catalogueJavazik.txt non trouvé");
        }
        return albums;
    }
}
