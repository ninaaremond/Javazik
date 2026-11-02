package javazik.modele;

/*
Sources
evian
https://docs.oracle.com/javase/8/docs/api/java/io/IOException.html
https://www.baeldung.com/java-write-to-file
https://www.baeldung.com/reading-file-in-java
https://www.w3schools.com/java/java_files_write.asp
 */

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Classe responsable de la gestion des utilisateurs du système.
 * Gère la création, suppression, recherche et authentification des utilisateurs.
 * Format Fichier texte : id(0);droit(1);mdp(2);email(3);nom(4);prenom(5);actif(6) !!!!!!!!!!!!!!!!!!
 */
public class GestionUtilisateurs {
    private final String FICHIER = "utilisateur.txt";
    private String nom, prenom;

    // Getter
    public String getNom() {
        return this.nom;
    }

    public String getPrenom() {
        return this.prenom;
    }

    /**
     * Calcule le prochain ID en lisant le fichier
     */
    private int genererProchainId() {
        int maxId = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(FICHIER))) {
            String ligne;
            while ((ligne = br.readLine()) != null) {
                String[] p = ligne.split(";");
                if (p.length >= 1) {
                    int id = Integer.parseInt(p[0]);
                    if (id > maxId) {
                        maxId = id;
                    }
                }
            }
        } catch (IOException e) {
        }
        return maxId + 1;
    }

    private boolean emailExiste(String email) {
        try (BufferedReader br = new BufferedReader(new FileReader(FICHIER))) {
            String ligne;
            while ((ligne = br.readLine()) != null) {
                String[] parties = ligne.split(";");
                if (parties.length >= 4 && parties[3].equals(email)) {
                    return true;
                }
            }
        } catch (IOException e) {
        }
        return false;
    }

    /**
     * Crée un nouveau client
     * Format : id(0);droit(1);mdp(2);email(3);nom(4);prenom(5);actif(6)
     */
    public Client creerClient(String mdp, String email, String nom, String prenom) {
        if (emailExiste(email)) {
            return null;
        }

        int id = genererProchainId();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FICHIER, true))) {
            bw.write(id + ";1;" + mdp + ";" + email + ";" + nom + ";" + prenom + ";true");
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new Client(id, mdp, email, nom, prenom);
    }

    /**
     * Crée un nouvel admin
     * Format : id(0);droit(1);mdp(2);email(3);nom(4);prenom(5);actif(6)
     */
    public Admin creerAdmin(String mdp, String email, String nom, String prenom) {
        if (emailExiste(email)) {
            return null;
        }

        int id = genererProchainId();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FICHIER, true))) {
            bw.write(id + ";2;" + mdp + ";" + email + ";" + nom + ";" + prenom + ";true");
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new Admin(id, mdp, email, nom, prenom);
    }

    /**
     * Authentifie un utilisateur
     * return utilisateur authentifié ou null si échec
     */
    public Utilisateur authentifier(String email, String mdp) {
        try (BufferedReader br = new BufferedReader(new FileReader(FICHIER))) {
            String ligne;
            while ((ligne = br.readLine()) != null) {
                String[] p = ligne.split(";");

                if (p.length >= 7) {
                    // Comparaison Email (index 3) et MDP (index 2)
                    if (p[3].equals(email) && p[2].equals(mdp)) {
                        int id = Integer.parseInt(p[0]);
                        int droit = Integer.parseInt(p[1]);
                        boolean actif = Boolean.parseBoolean(p[6]);

                        if (!actif) {
                            System.out.println("Erreur: Ce compte a été suspendu.");
                            return null;
                        }

                        System.out.println("Connexion réussie !");
                        this.nom = p[4];
                        this.prenom = p[5];

                        if (droit == 1) {
                            return new Client(id, mdp, email, p[4], p[5]);
                        }
                        if (droit == 2) {
                            return new Admin(id, mdp, email, p[4], p[5]);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Email ou mot de passe incorrect.");
        return null;
    }

    /**
     * modifier une information
     * lit toutes les lignes, identifie la ligne par ID, et modifie l'index voulu
     */
    private void modifierLigne(int idCible, int indexValeur, String nouvelleValeur) {
        List<String> lignes = new ArrayList<String>();

        try (BufferedReader br = new BufferedReader(new FileReader(FICHIER))) {
            String ligne;
            while ((ligne = br.readLine()) != null) {
                String[] p = ligne.split(";");
                if (p.length >= 7 && Integer.parseInt(p[0]) == idCible) {
                    p[indexValeur] = nouvelleValeur;
                    lignes.add(String.join(";", p));
                } else {
                    lignes.add(ligne);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FICHIER))) {
            for (String l : lignes) {
                bw.write(l);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Méthode pour verifier si l'ID existe ou non
    private int rechercherIdUtilisateur(int idRecherche) {
        File fichier = new File(FICHIER);
        try (BufferedReader br = new BufferedReader(new FileReader(fichier))) {
            String ligne;
            while ((ligne = br.readLine()) != null) {
                String[] parties = ligne.split(";");

                if (parties.length >= 1) {
                    int idFichier = Integer.parseInt(parties[0]);

                    if (idFichier == idRecherche) {
                        return idFichier; // trouvé
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Erreur lecture fichier utilisateur");
            e.printStackTrace();
        } catch (NumberFormatException e) {
            System.out.println("Erreur format ID dans le fichier");
        }

        return -1; // pas trouvé
    }

    /**
     * Suspend un compte (Client ou Admin)
     */
    public void suspendre(int id) {
        if (rechercherIdUtilisateur(id) == -1) {
            System.out.println("L'utilisateur n'a pas été trouvé");
            return;
        }
        modifierLigne(id, 6, "false");
        System.out.println("Compte " + id + " suspendu.");
    }

    /**
     * Réactive un compte (Client ou Admin)
     */
    public void reactiver(int id) {
        if (rechercherIdUtilisateur(id) == -1) {
            System.out.println("L'utilisateur n'a pas été trouvé");
            return;
        }
        modifierLigne(id, 6, "true");
        System.out.println("Compte " + id + " réactivé.");
    }

    /**
     * Change le mot de passe d'un utilisateur (index 2)
     */
    public void changerMotDePasse(int id, String nouveauMdp) {
        modifierLigne(id, 2, nouveauMdp);
        System.out.println("Mot de passe mis à jour.");
    }

    public void changerMail(int id, String nouveauMail) {
        modifierLigne(id, 3, nouveauMail);
        System.out.println("Mail mis à jour.");
    }

    public void changerNom(int id, String nouveauNom) {
        modifierLigne(id, 4, nouveauNom);
        System.out.println("Nom mis à jour.");
    }

    public void changerPrenom(int id, String nouveauPrenom) {
        modifierLigne(id, 5, nouveauPrenom);
        System.out.println("Prenom mis à jour.");
    }


     //fonction qui créé les statistiques nombre utilisateurs et nombre client actifs
    public void mettreAJourStatistiquesUtilisateurs(StatistiquesAdmin stats) {
        int totalUsers = 0;
        int clientsActifs = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(FICHIER))) {
            String ligne;
            while ((ligne = br.readLine()) != null) {
                String[] p = ligne.split(";");
                if (p.length >= 7) {
                    totalUsers++;

                    // 1= droit (1 piur client), 6 = actif (true)
                    if (p[1].equals("1") && p[6].equals("true")) {
                        clientsActifs++;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Erreur lors du calcul des stats utilisateurs.");
        }

        // On envoie a statsAdmin
        stats.setNombreUtilisateurs(totalUsers);
        stats.setNombreClientsActifs(clientsActifs);
    }

    // Méthode pour gérer le profil d'une personne quand il est admin
    public void menuSpeAdmin(Admin unAdmin, Scanner u) {
        // Attribut(s)
        int choix = -1;
        int id;
        GestionCatalogue catalogue = new GestionCatalogue(unAdmin.getDroit());
        ArrayList<String[]> resultatRechercheCatalogue = new ArrayList<String[]>();

        do {
            // Menu
            System.out.println("- Bienvenue dans la console des Admins -");
            System.out.println("Que voulez vous faire ?");
            System.out.println("1) Afficher mes informations");
            System.out.println("2) Créer un nouvel Admin");
            System.out.println("3) Suspendre un utilisateur");
            System.out.println("4) Ré-activer un utilisateur");
            System.out.println("5) Ajouter dans le catalogue");
            System.out.println("6) Modifier dans le catalogue");
            System.out.println("7) Supprimer dans le catalogue");
            System.out.println("8) Generer des statistiques");
            System.out.println("9) Quitter le menu Admin");
            System.out.print("Choix => ");

            if (u.hasNextInt()) {
                choix = u.nextInt();
                u.nextLine();
            } else {
                System.out.println("Votre choix ne correspond à rien !");
                u.nextLine();
                choix = -1;
                continue;
            }

            // Action
            switch (choix) {
                case 1 :
                    unAdmin.afficherInfos();
                    break;

                case 2 :
                    // Demander Infos
                    System.out.print("Donnez le nom de l'Admin : ");
                    String nom = u.nextLine();

                    System.out.print("Donnez le prénom de l'Admin : ");
                    String prenom = u.nextLine();

                    System.out.print("Donnez le mot de passe de l'Admin : ");
                    String mdpA = u.nextLine();

                    System.out.print("Donnez le mail de l'Admin : ");
                    String mailA = u.nextLine();

                    // Création
                    Admin nouvelAdmin = creerAdmin(mdpA, mailA, nom, prenom);

                    if (nouvelAdmin != null) {
                        System.out.println("Le profil d'admin " + nom + " " + prenom + " vient d'être créé");
                    } else {
                        System.out.println("Impossible de créer l'admin : mail déjà utilisé");
                    }
                    break;

                case 3 :
                    System.out.print("Donnez l'ID de la personne à suspendre : ");
                    if (u.hasNextInt()) {
                        id = u.nextInt();
                        u.nextLine();
                        suspendre(id);
                    } else {
                        System.out.println("ID invalide");
                        u.nextLine();
                    }
                    break;

                case 4 :
                    System.out.print("Donnez l'ID de la personne à ré-activer : ");
                    if (u.hasNextInt()) {
                        id = u.nextInt();
                        u.nextLine();
                        reactiver(id);
                    } else {
                        System.out.println("ID invalide");
                        u.nextLine();
                    }
                    break;

                // Ajouter
                case 5 :
                    catalogue.ajouterUneLigneDansCatalogue(catalogue.recupInfosPourCatalogue(u));
                    break;

                // Modifier
                case 6 :
                    resultatRechercheCatalogue = catalogue.rechercherDansCatalogueAdmin(u);

                    if (!resultatRechercheCatalogue.isEmpty()) {
                        catalogue.modifierUneLigneDansCatalogue(u, resultatRechercheCatalogue);
                    }
                    break;

                // Supprimer
                case 7 :
                    resultatRechercheCatalogue = catalogue.rechercherDansCatalogueAdmin(u);

                    if (!resultatRechercheCatalogue.isEmpty()) {
                        catalogue.supprimerUneLigneDansCatalogue(u, resultatRechercheCatalogue);
                    }
                    break;

                case 8 :
                    StatistiquesAdmin statsGlobales = new StatistiquesAdmin();
                    statsGlobales.rafraichirToutesLesStats(catalogue, this);
                    System.out.println(statsGlobales.toString());
                    break;

                case 9 :
                    System.out.println("Retour au menu précédent...");
                    break;

                default :
                    System.out.println("Votre choix ne correspond à rien !");
                    break;
            }
        } while (choix != 9);
    }
}