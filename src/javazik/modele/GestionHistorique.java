package javazik.modele;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class GestionHistorique {
    
    private static final String DOSSIER_HISTORIQUE = "./historique/";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    public GestionHistorique() {
        // Créer le dossier s'il n'existe pas
        File dossier = new File(DOSSIER_HISTORIQUE);
        if (!dossier.exists()) {
            dossier.mkdirs();
        }
    }
    
    /**
     * Enregistre une lecture de morceau dans l'historique de l'utilisateur
     * @param userId ID de l'utilisateur
     * @param morceauData Tableau contenant [id, artiste, titre, duree, album, groupe]
     */
    public boolean enregistrerLecture(int userId, String[] morceauData) {
        try {
            System.out.println("Enregistrement historique - UserID: " + userId + ", Données: " + 
                (morceauData != null && morceauData.length > 2 ? morceauData[2] : "null"));
            
            // Vérifier que l'ID est valide
            if (userId <= 0) {
                System.err.println("Erreur: ID utilisateur invalide: " + userId + " - Veuillez vous connecter correctement");
                return false;
            }
            
            // Créer le dossier s'il n'existe pas
            File dossier = new File(DOSSIER_HISTORIQUE);
            if (!dossier.exists()) {
                dossier.mkdirs();
                System.out.println("Dossier créé: " + dossier.getAbsolutePath());
            }
            
            String nomFichier = DOSSIER_HISTORIQUE + "historique" + userId + ".txt";
            String ligne = creerEntreeHistorique(morceauData);
            
            // Ajouter à la fin du fichier
            try (FileWriter fw = new FileWriter(nomFichier, true);
                 BufferedWriter bw = new BufferedWriter(fw)) {
                bw.write(ligne);
                bw.newLine();
            }
            System.out.println("Historique enregistré dans: " + new File(nomFichier).getAbsolutePath());
            return true;
        } catch (IOException e) {
            System.err.println("Erreur lors de l'enregistrement de l'historique : " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Crée une entrée formatée pour l'historique
     */
    private String creerEntreeHistorique(String[] morceauData) {
        String timestamp = LocalDateTime.now().format(formatter);
        String id = morceauData.length > 0 ? morceauData[0] : "?";
        String artiste = morceauData.length > 1 ? morceauData[1] : "?";
        String titre = morceauData.length > 2 ? morceauData[2] : "?";
        String duree = morceauData.length > 3 ? morceauData[3] : "?";
        String album = morceauData.length > 4 ? morceauData[4] : "?";
        
        return timestamp + " | ID: " + id + " | " + artiste + " - " + titre + " (" + duree + "s) | Album: " + album;
    }
    
    /**
     * Charge l'historique d'écoute d'un utilisateur
     * @param userId ID de l'utilisateur
     * @return Liste des entrées d'historique
     */
    public ArrayList<String> chargerHistorique(int userId) {
        ArrayList<String> historique = new ArrayList<>();
        
        System.out.println("Chargement historique pour UserID: " + userId);
        System.out.println("Chemin: " + new File(DOSSIER_HISTORIQUE).getAbsolutePath());
        
        String nomFichier = DOSSIER_HISTORIQUE + "historique" + userId + ".txt";
        
        try {
            File fichier = new File(nomFichier);
            System.out.println("Fichier à chercher: " + fichier.getAbsolutePath());
            System.out.println("Fichier existe: " + fichier.exists());
            
            if (!fichier.exists()) {
                System.out.println("Le fichier d'historique n'existe pas");
                return historique; // Retourner une liste vide si le fichier n'existe pas
            }
            
            try (BufferedReader br = new BufferedReader(new FileReader(nomFichier))) {
                String ligne;
                while ((ligne = br.readLine()) != null) {
                    historique.add(ligne);
                }
            }
            
            System.out.println("Nombre d'entrées chargées: " + historique.size());
            
            // Inverser l'ordre pour afficher le plus récent en premier
            Collections.reverse(historique);
            
        } catch (IOException e) {
            System.err.println("Erreur lors de la lecture de l'historique : " + e.getMessage());
            e.printStackTrace();
        }
        
        return historique;
    }
    
    /**
     * Supprime l'historique d'un utilisateur
     */
    public boolean supprimerHistorique(int userId) {
        try {
            String nomFichier = DOSSIER_HISTORIQUE + "historique" + userId + ".txt";
            File fichier = new File(nomFichier);
            if (fichier.exists()) {
                return fichier.delete();
            }
            return true;
        } catch (Exception e) {
            System.err.println("Erreur lors de la suppression de l'historique : " + e.getMessage());
            return false;
        }
    }
}
