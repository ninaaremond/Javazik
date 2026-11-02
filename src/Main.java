import java.util.ArrayList;
import java.util.Map;
import javax.swing.*;
import javazik.controleur.*;
import javazik.modele.*;
import javazik.vue.*;

public class Main extends JFrame {
    private MainController controller;
    private VueAccueil vueAccueil;
    private VueConnexion vueConnexion;
    private VueInscription vueInscription;
    private VueMenuClient vueMenuClient;
    private VueMenuAdmin vueMenuAdmin;
    private VueRecherche vueRecherche;
    private VueMorceaux vueMorceaux;
    private VuePlaylists vuePlaylists;
    private VuePlaylistDetail vuePlaylistDetail;
    private VueMesInfos vueMesInfos;
    private VueHistoriqueEcoute vueHistoriqueEcoute;
    private VueDetailMorceau vueDetailMorceau;
    private VueArtiste vueArtiste;
    private VueStatistiques vueStatistiques;
    private VueGestionCatalogue vueGestionCatalogue;
    private VueGestionUtilisateur vueGestionUtilisateur;
    private String playlistActuelle;

    public Main() {
        setTitle("JAVAZIK");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Initialiser le contrôleur
        controller = new MainController();
        
        // Créer les vues
        vueAccueil = new VueAccueil();
        vueConnexion = new VueConnexion();
        vueInscription = new VueInscription();
        vueMenuClient = new VueMenuClient();
        vueMenuAdmin = new VueMenuAdmin();
        vueRecherche = new VueRecherche();
        vueMorceaux = new VueMorceaux();
        vuePlaylists = new VuePlaylists();
        vuePlaylistDetail = new VuePlaylistDetail();
        vueMesInfos = new VueMesInfos();
        vueHistoriqueEcoute = new VueHistoriqueEcoute();
        vueDetailMorceau = new VueDetailMorceau();
        vueArtiste = new VueArtiste();
        vueStatistiques = new VueStatistiques();
        vueGestionCatalogue = new VueGestionCatalogue();
        vueGestionUtilisateur = new VueGestionUtilisateur();
        
        // Connecter les événements
        connecterEvenements();
        
        // Afficher l'accueil
        setContentPane(vueAccueil);
    }
    
    private void connecterEvenements() {
        // VueAccueil
        vueAccueil.ajouterEcouteurConnexion(e -> afficherConnexion());
        vueAccueil.ajouterEcouteurInscription(e -> afficherInscription());
        vueAccueil.ajouterEcouteurVisiteur(e -> {
            controller.modeVisiteur();
            System.out.println("Visiteur : " + controller.getUser());
            afficherMenuClient();
        });
        
        // VueConnexion
        vueConnexion.ajouterEcouteurConnexion(e -> {
            String email = vueConnexion.getEmail();
            String mdp = vueConnexion.getMotDePasse();
            if (email.isEmpty() || mdp.isEmpty()) {
                vueConnexion.afficherErreur("Email et mot de passe requis");
            } else if (controller.connexion(email, mdp)) {
                vueConnexion.reinitialiser();
                afficherMenuParDroit();
            } else {
                vueConnexion.afficherErreur("Email ou mot de passe incorrect");
            }
        });
        vueConnexion.ajouterEcouteurRetour(e -> {
            vueConnexion.reinitialiser();
            afficherAccueil();
        });
        
        // VueInscription
        vueInscription.ajouterEcouteurInscription(e -> {
            String nom = vueInscription.getNom();
            String prenom = vueInscription.getPrenom();
            String email = vueInscription.getEmail();
            String mdp = vueInscription.getMotDePasse();
            int role = vueInscription.getDroit();
            if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || mdp.isEmpty()) {
                vueInscription.afficherErreur("Tous les champs sont requis");
            } else if (controller.inscription(email, mdp, nom, prenom, role)) {
                vueInscription.reinitialiser();
                afficherMenuParDroit();
            } else {
                vueInscription.afficherErreur("Erreur : email existe déjà ou inscription échouée");
            }
        });
        vueInscription.ajouterEcouteurRetour(e -> {
            vueInscription.reinitialiser();
            afficherAccueil();
        });
        
        // VueMenuClient
        vueMenuClient.ajouterEcouteurRecherche(e -> afficherRecherche());
        vueMenuClient.ajouterEcouteurMorceaux(e -> {
            vueMorceaux.afficherMorceaux(controller.chargerTousMorceaux());
            connecterBoutonsPlayMorceauxMorceaux();
            connecterBoutonsDetailMorceaux();
            afficherMorceaux();
        });
        vueMenuClient.ajouterEcouteurPlaylists(e -> {
            // Vérifier si l'utilisateur est un visiteur
            if (controller.getUser() instanceof Visiteur) {
                JOptionPane.showMessageDialog(Main.this, 
                    "Les playlists sont réservées aux clients connectés.\nVeuillez vous connecter ou vous inscrire.", 
                    "Accès non autorisé", JOptionPane.WARNING_MESSAGE);
            } else {
                vuePlaylists.afficherPlaylists(controller.chargerPlaylists());
                afficherPlaylists();
            }
        });
        vueMenuClient.ajouterEcouteurMesInfos(e -> {
            if (controller.getUser() instanceof Visiteur) {
                JOptionPane.showMessageDialog(Main.this, 
                    "Vous devez être connecté pour accéder à vos informations.\nVeuillez vous connecter ou vous inscrire.", 
                    "Accès non autorisé", JOptionPane.WARNING_MESSAGE);
            } else {
                Client client = (Client) controller.getUser();
                vueMesInfos.afficherInfos(client.getNom(), client.getPrenom(), client.getEmail());
                afficherMesInfos();
            }
        });
        vueMenuClient.ajouterEcouteurHistorique(e -> {
            if (controller.getUser() instanceof Visiteur) {
                JOptionPane.showMessageDialog(Main.this, 
                    "Vous devez être connecté pour accéder à votre historique d'écoute.\nVeuillez vous connecter ou vous inscrire.", 
                    "Accès non autorisé", JOptionPane.WARNING_MESSAGE);
            } else {
                ArrayList<String> historique = controller.chargerHistorique();
                vueHistoriqueEcoute.afficherHistorique(historique);
                afficherHistorique();
            }
        });
        vueMenuClient.ajouterEcouteurDeconnexion(e -> {
            controller.deconnexion();
            afficherAccueil();
        });
        
        // VueMenuAdmin
        vueMenuAdmin.ajouterEcouteurRecherche(e -> afficherRecherche());
        vueMenuAdmin.ajouterEcouteurMorceaux(e -> {
            vueMorceaux.afficherMorceaux(controller.chargerTousMorceaux());
            connecterBoutonsPlayMorceauxMorceaux();
            connecterBoutonsDetailMorceaux();
            afficherMorceaux();
        });
        vueMenuAdmin.ajouterEcouteurPlaylists(e -> {
            if (controller.getUser() instanceof Visiteur) {
                JOptionPane.showMessageDialog(Main.this, "Visiteur n'a pas accès aux playlists", "Accès refusé", JOptionPane.WARNING_MESSAGE);
            } else {
                vuePlaylists.afficherPlaylists(controller.chargerPlaylists());
                afficherPlaylists();
            }
        });
        vueMenuAdmin.ajouterEcouteurMesInfos(e -> {
            Utilisateur user = controller.getUser();
            if (user instanceof Admin) {
                Admin admin = (Admin) user;
                vueMesInfos.afficherInfos(admin.getNomAdmin(), admin.getPrenomAdmin(), admin.getEmail());
            } else if (user instanceof Client) {
                Client client = (Client) user;
                vueMesInfos.afficherInfos(client.getNom(), client.getPrenom(), client.getEmail());
            }
            afficherMesInfos();
        });
        vueMenuAdmin.ajouterEcouteurHistorique(e -> {
            ArrayList<String> historique = controller.chargerHistorique();
            vueHistoriqueEcoute.afficherHistorique(historique);
            afficherHistorique();
        });
        vueMenuAdmin.ajouterEcouteurStatistiques(e -> afficherStatistiques());
        vueMenuAdmin.ajouterEcouteurGestionCatalogue(e -> afficherGestionCatalogue());
        vueMenuAdmin.ajouterEcouteurGestionUtilisateurs(e -> afficherGestionUtilisateur());
        vueMenuAdmin.ajouterEcouteurDeconnexion(e -> {
            controller.deconnexion();
            afficherAccueil();
        });
        
        // VueGestionCatalogue
        vueGestionCatalogue.ajouterEcouteurRetour(e -> afficherMenuParDroit());
        vueGestionCatalogue.ajouterEcouteurAjouter(e -> {
            String artiste = vueGestionCatalogue.getArtiste();
            String titre = vueGestionCatalogue.getTitre();
            String duree = vueGestionCatalogue.getDuree();
            String album = vueGestionCatalogue.getAlbum();
            
            if (artiste.isEmpty() || titre.isEmpty() || duree.isEmpty() || album.isEmpty()) {
                JOptionPane.showMessageDialog(Main.this, "Tous les champs sont requis", "Erreur", JOptionPane.ERROR_MESSAGE);
            } else if (controller.ajouterMorceauCatalogue(artiste, titre, duree, album)) {
                JOptionPane.showMessageDialog(Main.this, "Morceau ajouté avec succès", "Succès", JOptionPane.INFORMATION_MESSAGE);
                vueGestionCatalogue.afficherCatalogue(controller.chargerCatalogueComplet());
                vueGestionCatalogue.reinitialiserFormulaires();
                // Mettre à jour les statistiques
                int[] stats = controller.obtenirStatistiques();
                vueStatistiques.afficherStatistiques(stats[0], stats[1], stats[2], stats[3], stats[4], stats[5], stats[6]);
            } else {
                JOptionPane.showMessageDialog(Main.this, "Erreur lors de l'ajout du morceau", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });
        vueGestionCatalogue.ajouterEcouteurSupprimer(e -> {
            String idStr = vueGestionCatalogue.getMorceauASupprimer();
            if (idStr.isEmpty()) {
                JOptionPane.showMessageDialog(Main.this, "Veuillez entrer l'ID du morceau à supprimer", "Erreur", JOptionPane.ERROR_MESSAGE);
            } else {
                try {
                    int id = Integer.parseInt(idStr);
                    int confirmation = JOptionPane.showConfirmDialog(Main.this, 
                        "Êtes-vous sûr de vouloir supprimer le morceau avec l'ID " + id + " ?", 
                        "Confirmer la suppression", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                    if (confirmation == JOptionPane.YES_OPTION) {
                        if (controller.supprimerMorceauCatalogue(id)) {
                            JOptionPane.showMessageDialog(Main.this, "Morceau supprimé avec succès", "Succès", JOptionPane.INFORMATION_MESSAGE);
                            vueGestionCatalogue.afficherCatalogue(controller.chargerCatalogueComplet());
                            vueGestionCatalogue.reinitialiserFormulaires();
                            // Mettre à jour les statistiques
                            int[] stats = controller.obtenirStatistiques();
                            vueStatistiques.afficherStatistiques(stats[0], stats[1], stats[2], stats[3], stats[4], stats[5], stats[6]);
                        } else {
                            JOptionPane.showMessageDialog(Main.this, "Erreur lors de la suppression du morceau", "Erreur", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(Main.this, "L'ID doit être un nombre", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        // VueGestionUtilisateur
        vueGestionUtilisateur.ajouterEcouteurRetour(e -> afficherMenuParDroit());
        vueGestionUtilisateur.ajouterEcouteurAjouter(e -> {
            String email = vueGestionUtilisateur.getEmail();
            String nom = vueGestionUtilisateur.getNom();
            String prenom = vueGestionUtilisateur.getPrenom();
            int droit = vueGestionUtilisateur.getRole();
            
            if (email.isEmpty() || nom.isEmpty() || prenom.isEmpty()) {
                JOptionPane.showMessageDialog(Main.this, "Tous les champs sont requis", "Erreur", JOptionPane.ERROR_MESSAGE);
            } else {
                String mdpGeneree = generatePassword();
                if (controller.ajouterUtilisateur(email, mdpGeneree, nom, prenom, droit)) {
                    String roleName = (droit == 2) ? "Admin" : "Client";
                    JOptionPane.showMessageDialog(Main.this, 
                        "Utilisateur ajouté avec succès\nMot de passe : " + mdpGeneree + "\nRôle : " + roleName, 
                        "Succès", JOptionPane.INFORMATION_MESSAGE);
                    vueGestionUtilisateur.afficherUtilisateurs(controller.chargerUtilisateursComplets());
                    vueGestionUtilisateur.reinitialiserFormulaires();
                } else {
                    JOptionPane.showMessageDialog(Main.this, "Erreur : Email existe déjà ou erreur lors de l'ajout", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        vueGestionUtilisateur.ajouterEcouteurModifier(e -> {
            String email = vueGestionUtilisateur.getEmailModifier();
            int droit = vueGestionUtilisateur.getRoleModifier();
            
            if (email.isEmpty()) {
                JOptionPane.showMessageDialog(Main.this, "Veuillez entrer un email", "Erreur", JOptionPane.ERROR_MESSAGE);
            } else if (controller.modifierRoleUtilisateur(email, droit)) {
                JOptionPane.showMessageDialog(Main.this, "Rôle modifié avec succès", "Succès", JOptionPane.INFORMATION_MESSAGE);
                vueGestionUtilisateur.afficherUtilisateurs(controller.chargerUtilisateursComplets());
                vueGestionUtilisateur.reinitialiserFormulaires();
            } else {
                JOptionPane.showMessageDialog(Main.this, "Erreur : Email introuvable ou erreur lors de la modification", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });
        vueGestionUtilisateur.ajouterEcouteurSupprimer(e -> {
            String email = vueGestionUtilisateur.getEmailSupprimer();
            
            if (email.isEmpty()) {
                JOptionPane.showMessageDialog(Main.this, "Veuillez entrer un email", "Erreur", JOptionPane.ERROR_MESSAGE);
            } else {
                int confirmation = JOptionPane.showConfirmDialog(Main.this, 
                    "Êtes-vous sûr de vouloir supprimer l'utilisateur " + email + " ?", 
                    "Confirmer la suppression", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if (confirmation == JOptionPane.YES_OPTION) {
                    if (controller.supprimerUtilisateur(email)) {
                        JOptionPane.showMessageDialog(Main.this, "Utilisateur supprimé avec succès", "Succès", JOptionPane.INFORMATION_MESSAGE);
                        vueGestionUtilisateur.afficherUtilisateurs(controller.chargerUtilisateursComplets());
                        vueGestionUtilisateur.reinitialiserFormulaires();
                    } else {
                        JOptionPane.showMessageDialog(Main.this, "Erreur : Email introuvable ou erreur lors de la suppression", "Erreur", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        
        // VueStatistiques
        vueStatistiques.ajouterEcouteurRetour(e -> afficherMenuParDroit());
        
        // VueMesInfos
        vueMesInfos.ajouterEcouteurRetour(e -> afficherMenuParDroit());
        
        vueMesInfos.ajouterEcouteurModifier(e -> {
            String nouveauMdp = JOptionPane.showInputDialog(Main.this, 
                "Entrez votre nouveau mot de passe :", 
                "Modifier mes informations", JOptionPane.QUESTION_MESSAGE);
            if (nouveauMdp != null && !nouveauMdp.trim().isEmpty()) {
                if (controller.modifierMotDePasse(nouveauMdp)) {
                    JOptionPane.showMessageDialog(Main.this, 
                        "Mot de passe modifié avec succès !", 
                        "Succès", JOptionPane.INFORMATION_MESSAGE);
                    Client client = (Client) controller.getUser();
                    vueMesInfos.afficherInfos(client.getNom(), client.getPrenom(), client.getEmail());
                } else {
                    JOptionPane.showMessageDialog(Main.this, 
                        "Erreur lors de la modification du mot de passe", 
                        "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        vueMesInfos.ajouterEcouteurSupprimer(e -> {
            int confirmation = JOptionPane.showConfirmDialog(Main.this, 
                "Êtes-vous sûr de vouloir supprimer votre compte ? Cette action est irréversible.", 
                "Confirmer la suppression", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (confirmation == JOptionPane.YES_OPTION) {
                if (controller.supprimerCompte()) {
                    controller.arreterMusique();
                    JOptionPane.showMessageDialog(Main.this, 
                        "Votre compte a été supprimé avec succès.", 
                        "Compte supprimé", JOptionPane.INFORMATION_MESSAGE);
                    afficherAccueil();
                } else {
                    JOptionPane.showMessageDialog(Main.this, 
                        "Erreur lors de la suppression du compte", 
                        "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        // VueRecherche
        vueRecherche.ajouterEcouteurRetour(e -> afficherMenuParDroit());
        vueRecherche.ajouterEcouteurRechercherArtisteGroupe(e -> {
            String terme = vueRecherche.getChampArtisteGroupe();
            if (terme.isEmpty()) {
                JOptionPane.showMessageDialog(Main.this, "Veuillez entrer un artiste ou un groupe", "Erreur", JOptionPane.ERROR_MESSAGE);
            } else {
                ArrayList<String[]> resultats = controller.rechercherParCatalogueSimple(terme, "artistegroupe");
                vueRecherche.afficherResultats(resultats);
                connecterBoutonsPlayMorceauxRecherche();
                connecterBoutonsDetailMorceaux();
            }
        });
        vueRecherche.ajouterEcouteurRechercherMorceau(e -> {
            String morceau = vueRecherche.getChampMorceau();
            if (morceau.isEmpty()) {
                JOptionPane.showMessageDialog(Main.this, "Veuillez entrer un morceau", "Erreur", JOptionPane.ERROR_MESSAGE);
            } else {
                ArrayList<String[]> resultats = controller.rechercherParCatalogueSimple(morceau, "morceau");
                vueRecherche.afficherResultats(resultats);
                connecterBoutonsPlayMorceauxRecherche();
                connecterBoutonsDetailMorceaux();
            }
        });
        vueRecherche.ajouterEcouteurRechercherAlbum(e -> {
            String album = vueRecherche.getChampAlbum();
            if (album.isEmpty()) {
                JOptionPane.showMessageDialog(Main.this, "Veuillez entrer un album", "Erreur", JOptionPane.ERROR_MESSAGE);
            } else {
                ArrayList<String[]> resultats = controller.rechercherParCatalogueSimple(album, "album");
                vueRecherche.afficherResultats(resultats);
                connecterBoutonsPlayMorceauxRecherche();
                connecterBoutonsDetailMorceaux();
            }
        });
        
        // VueMorceaux
        vueMorceaux.ajouterEcouteurRetour(e -> afficherMenuParDroit());
        
        // VueHistoriqueEcoute
        vueHistoriqueEcoute.ajouterEcouteurRetour(e -> afficherMenuParDroit());
        vueHistoriqueEcoute.ajouterEcouteurEffacer(e -> {
            int confirmation = JOptionPane.showConfirmDialog(Main.this, 
                "Êtes-vous sûr de vouloir effacer votre historique d'écoute ?", 
                "Confirmer l'effacement", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (confirmation == JOptionPane.YES_OPTION) {
                if (controller.effacerHistorique()) {
                    JOptionPane.showMessageDialog(Main.this, "Historique effacé avec succès", "Succès", JOptionPane.INFORMATION_MESSAGE);
                    vueHistoriqueEcoute.afficherHistorique(new ArrayList<>());
                } else {
                    JOptionPane.showMessageDialog(Main.this, "Erreur lors de l'effacement de l'historique", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        // VueDetailMorceau - Connecter les listeners une seule fois
        vueDetailMorceau.ajouterEcouteurRetour(e -> afficherMenuParDroit());
        vueDetailMorceau.ajouterEcouteurArtiste(e -> {
            String artiste = vueDetailMorceau.getArtisteActuel();
            if (artiste != null && !artiste.isEmpty()) {
                afficherArtiste(artiste);
            }
        });
        vueDetailMorceau.ajouterEcouteurJouer(e -> {
            if (!controller.peutLireMusique()) {
                JOptionPane.showMessageDialog(Main.this, 
                    "Limite de lectures atteinte (5 lectures maximum).\n\nVeuillez vous reconnecter pour continuer.",
                    "Limite dépassée", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            String[] donneesActuelles = vueDetailMorceau.getDonneesActuelles();
            if (donneesActuelles != null && donneesActuelles.length >= 3) {
                String titre = donneesActuelles[2];
                String cheminFichier = "musiques/" + titre + ".wav";
                
                java.io.File fichier = new java.io.File(cheminFichier);
                if (!fichier.exists()) {
                    JOptionPane.showMessageDialog(Main.this, 
                        "Fichier audio non trouvé :\n\n" + fichier.getAbsolutePath() + 
                        "\n\nVeuillez ajouter le fichier \"" + titre + ".wav\" " +
                        "\ndans le dossier \"musiques/\"",
                        "Fichier manquant", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                if (!controller.lancerMusique(cheminFichier)) {
                    JOptionPane.showMessageDialog(Main.this, 
                        "Impossible de lancer la musique.\n\nVérifiez que le format WAV est correct.",
                        "Erreur audio", JOptionPane.ERROR_MESSAGE);
                } else {
                    // Enregistrer dans l'historique
                    controller.enregistrerLecture(donneesActuelles);
                    // Incrémenter le compteur
                    controller.incrementerCompteurLectures();
                }
            }
        });
        vueDetailMorceau.ajouterEcouteurArreter(e -> {
            if (controller.estMusiqueLancee()) {
                controller.pauserMusique();
            }
        });
        
        // VueArtiste
        vueArtiste.ajouterEcouteurRetour(e -> afficherMenuParDroit());
        
        // VuePlaylists
        vuePlaylists.ajouterEcouteurRetour(e -> afficherMenuParDroit());
        vuePlaylists.ajouterEcouteurAjouter(e -> {
            String nom = JOptionPane.showInputDialog(Main.this, "Nom de la nouvelle playlist :");
            if (nom != null && !nom.trim().isEmpty()) {
                controller.creerPlaylist(nom);
                vuePlaylists.afficherPlaylists(controller.chargerPlaylists());
                afficherPlaylists();
            }
        });
        vuePlaylists.ajouterEcouteurOuvrirPlaylist(e -> {
            if (e.getSource() instanceof JButton) {
                JButton btn = (JButton) e.getSource();
                String nomPlaylist = btn.getActionCommand();
                afficherPlaylistDetail(nomPlaylist);
            }
        });
        vuePlaylists.ajouterEcouteurSupprimerPlaylist(e -> {
            if (e.getSource() instanceof JButton) {
                JButton btn = (JButton) e.getSource();
                String nomPlaylist = btn.getActionCommand();
                int confirmation = JOptionPane.showConfirmDialog(Main.this, 
                    "Êtes-vous sûr de vouloir supprimer la playlist \"" + nomPlaylist + "\" ?",
                    "Confirmer la suppression", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if (confirmation == JOptionPane.YES_OPTION) {
                    if (controller.supprimerPlaylist(nomPlaylist)) {
                        JOptionPane.showMessageDialog(Main.this, 
                            "Playlist supprimée avec succès", "Succès", JOptionPane.INFORMATION_MESSAGE);
                        vuePlaylists.afficherPlaylists(controller.chargerPlaylists());
                        afficherPlaylists();
                    } else {
                        JOptionPane.showMessageDialog(Main.this, 
                            "Erreur lors de la suppression de la playlist", "Erreur", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        
        // VuePlaylistDetail
        vuePlaylistDetail.ajouterEcouteurRetour(e -> afficherPlaylists());
        vuePlaylistDetail.ajouterEcouteurAjouterMorceau(e -> {
            String titre = vuePlaylistDetail.getTitreAjout();
            if (!titre.isEmpty()) {
                if (controller.ajouterMorceauAPlaylist(playlistActuelle, titre)) {
                    vuePlaylistDetail.viderChampAjout();
                    ArrayList<String[]> morceaux = controller.obtenirMorceauxPlaylist(playlistActuelle);
                    vuePlaylistDetail.afficherMorceaux(morceaux);
                    connecterBoutonsPlayMorceaux();
                } else {
                    JOptionPane.showMessageDialog(Main.this, 
                        "Morceau '" + titre + "' non trouvé dans le catalogue", 
                        "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(Main.this, 
                    "Veuillez entrer un titre", 
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });
        
    }
    
    private void afficherAccueil() {
        setContentPane(vueAccueil);
        revalidate();
        repaint();
    }
    
    private void afficherConnexion() {
        setContentPane(vueConnexion);
        revalidate();
        repaint();
    }
    
    private void afficherInscription() {
        setContentPane(vueInscription);
        revalidate();
        repaint();
    }
    
    private void afficherMenuClient() {
        setContentPane(vueMenuClient);
        revalidate();
        repaint();
    }
    
    private void afficherGestionUtilisateur() {
        vueGestionUtilisateur.afficherUtilisateurs(controller.chargerUtilisateursComplets());
        vueGestionUtilisateur.reinitialiserFormulaires();
        setContentPane(vueGestionUtilisateur);
        revalidate();
        repaint();
    }
    
    private String generatePassword() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder password = new StringBuilder();
        java.util.Random random = new java.util.Random();
        for (int i = 0; i < 8; i++) {
            password.append(chars.charAt(random.nextInt(chars.length())));
        }
        return password.toString();
    }
    
    private void afficherRecherche() {
        setContentPane(vueRecherche);
        revalidate();
        repaint();
    }
    
    private void afficherMorceaux() {
        setContentPane(vueMorceaux);
        revalidate();
        repaint();
    }

    private void afficherPlaylists() {
        setContentPane(vuePlaylists);
        revalidate();
        repaint();
    }

    private void afficherGestionCatalogue() {
        vueGestionCatalogue.afficherCatalogue(controller.chargerCatalogueComplet());
        vueGestionCatalogue.reinitialiserFormulaires();
        setContentPane(vueGestionCatalogue);
        revalidate();
        repaint();
    }

    private void afficherPlaylistDetail(String nomPlaylist) {
        Playlist playlist = controller.obtenirPlaylist(nomPlaylist);
        if (playlist != null) {
            playlistActuelle = nomPlaylist;
            vuePlaylistDetail.setNomPlaylist(nomPlaylist);
            // Récupérer les morceaux à partir des IDs stockés
            ArrayList<String[]> morceaux = controller.obtenirMorceauxPlaylist(nomPlaylist);
            vuePlaylistDetail.afficherMorceaux(morceaux);
            connecterBoutonsPlayMorceaux();
            setContentPane(vuePlaylistDetail);
            revalidate();
            repaint();
        }
    }

    private void afficherMesInfos() {
        setContentPane(vueMesInfos);
        revalidate();
        repaint();
    }

    private void connecterBoutonsPlayMorceaux() {
        Map<String, VuePlaylistDetail.InfoMorceau> infoMorceaux = vuePlaylistDetail.getInfoMorceaux();
        Map<String, JButton> boutonsPlay = vuePlaylistDetail.getBoutonsPlayParMorceaux();
        Map<String, JButton> boutonsPause = vuePlaylistDetail.getBoutonsPauseParMorceaux();
        Map<String, JButton> boutonsSupprimer = vuePlaylistDetail.getBoutonsSupprimerParMorceaux();
        
        for (Map.Entry<String, JButton> entry : boutonsPlay.entrySet()) {
            String cle = entry.getKey();
            JButton btn = entry.getValue();
            VuePlaylistDetail.InfoMorceau info = infoMorceaux.get(cle);
            
            if (info != null) {
                btn.addActionListener(e -> {
                    // Vérifier le compteur de lectures pour les clients
                    if (!controller.peutLireMusique()) {
                        JOptionPane.showMessageDialog(Main.this, 
                            "Limite de lectures atteinte (5 lectures maximum).\n\nVeuillez vous reconnecter pour continuer.",
                            "Limite dépassée", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    
                    String cheminFichier = "musiques/" + info.titre + ".wav";
                    
                    // Vérifier si le fichier existe
                    java.io.File fichier = new java.io.File(cheminFichier);
                    if (!fichier.exists()) {
                        JOptionPane.showMessageDialog(Main.this, 
                            "Fichier audio non trouvé :\n\n" + fichier.getAbsolutePath() + 
                            "\n\nVeuillez ajouter le fichier \"" + info.titre + ".wav\" " +
                            "\ndans le dossier \"musiques/\"",
                            "Fichier manquant", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    
                    if (!controller.lancerMusique(cheminFichier)) {
                        JOptionPane.showMessageDialog(Main.this, 
                            "Impossible de lancer la musique.\n\nVérifiez que le format WAV est correct.",
                            "Erreur audio", JOptionPane.ERROR_MESSAGE);
                    } else {
                        // Enregistrer dans l'historique
                        String[] donneesMorceau = vuePlaylistDetail.getDonneesMorceau(cle);
                        if (donneesMorceau != null) {
                            controller.enregistrerLecture(donneesMorceau);
                        }
                        
                        // Incrémenter le compteur si la musique a bien été lancée
                        controller.incrementerCompteurLectures();
                    }
                });
            }
        }
        
        for (Map.Entry<String, JButton> entry : boutonsPause.entrySet()) {
            String cle = entry.getKey();
            JButton btn = entry.getValue();
            
            if (btn != null) {
                btn.addActionListener(e -> {
                    if (controller.estMusiqueLancee()) {
                        controller.pauserMusique();
                    }
                });
            }
        }
        
        for (Map.Entry<String, JButton> entry : boutonsSupprimer.entrySet()) {
            String cle = entry.getKey();
            JButton btn = entry.getValue();
            VuePlaylistDetail.InfoMorceau info = infoMorceaux.get(cle);
            
            if (info != null) {
                btn.addActionListener(e -> {
                    int confirmation = JOptionPane.showConfirmDialog(Main.this, 
                        "Êtes-vous sûr de vouloir supprimer \"" + info.titre + "\" de cette playlist ?",
                        "Confirmer la suppression", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                    if (confirmation == JOptionPane.YES_OPTION) {
                        if (controller.supprimerMorceauDePlaylist(playlistActuelle, info.id)) {
                            JOptionPane.showMessageDialog(Main.this, 
                                "Morceau supprimé avec succès", "Succès", JOptionPane.INFORMATION_MESSAGE);
                            ArrayList<String[]> morceaux = controller.obtenirMorceauxPlaylist(playlistActuelle);
                            vuePlaylistDetail.afficherMorceaux(morceaux);
                            connecterBoutonsPlayMorceaux();
                        } else {
                            JOptionPane.showMessageDialog(Main.this, 
                                "Erreur lors de la suppression du morceau", "Erreur", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                });
            }
        }
    }

    private void afficherMenuAdmin() {
        setContentPane(vueMenuAdmin);
        revalidate();
        repaint();
    }

    private void afficherMenuParDroit() {
        Utilisateur user = controller.getUser();
        if (user == null) {
            afficherAccueil();
        } else if (user.getDroit() == 2) {  // Admin
            afficherMenuAdmin();
        } else {  // Client ou Visiteur
            afficherMenuClient();
        }
    }

    private void afficherHistorique() {
        setContentPane(vueHistoriqueEcoute);
        revalidate();
        repaint();
    }

    private void afficherStatistiques() {
        int[] stats = controller.obtenirStatistiques();
        vueStatistiques.afficherStatistiques(stats[0], stats[1], stats[2], stats[3], stats[4], stats[5], stats[6]);
        setContentPane(vueStatistiques);
        revalidate();
        repaint();
    }
    
    private void connecterBoutonsPlayMorceauxMorceaux() {
        Map<String, JButton> boutonsPlay = vueMorceaux.getBoutonsPlayParMorceaux();
        
        for (JButton btn : boutonsPlay.values()) {
            if (btn.getText().contains("Jouer")) {
                btn.addActionListener(e -> {
                    // Vérifier le compteur de lectures pour les clients
                    if (!controller.peutLireMusique()) {
                        JOptionPane.showMessageDialog(Main.this, 
                            "Limite de lectures atteinte (5 lectures maximum).\n\nVeuillez vous reconnecter pour continuer.",
                            "Limite dépassée", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    
                    if (e.getSource() instanceof JButton) {
                        JButton button = (JButton) e.getSource();
                        String morceauId = button.getActionCommand();
                        String[] parts = morceauId.split("_", 2);
                        if (parts.length > 1) {
                            String titre = parts[1];
                            String cheminFichier = "musiques/" + titre + ".wav";
                            
                            // Vérifier si le fichier existe
                            java.io.File fichier = new java.io.File(cheminFichier);
                            if (!fichier.exists()) {
                                JOptionPane.showMessageDialog(Main.this, 
                                    "Fichier audio non trouvé :\n\n" + fichier.getAbsolutePath() + 
                                    "\n\nVeuillez ajouter le fichier \"" + titre + ".wav\" " +
                                    "\ndans le dossier \"musiques/\"",
                                    "Fichier manquant", JOptionPane.WARNING_MESSAGE);
                                return;
                            }
                            
                            if (!controller.lancerMusique(cheminFichier)) {
                                JOptionPane.showMessageDialog(Main.this, 
                                    "Impossible de lancer la musique.\n\nVérifiez que le format WAV est correct.",
                                    "Erreur audio", JOptionPane.ERROR_MESSAGE);
                            } else {
                                // Enregistrer dans l'historique
                                String[] donneesMorceau = vueMorceaux.getDonneesMorceau(morceauId);
                                if (donneesMorceau != null) {
                                    controller.enregistrerLecture(donneesMorceau);
                                }
                                
                                // Incrémenter le compteur si la musique a bien été lancée
                                controller.incrementerCompteurLectures();
                            }
                        }
                    }
                });
            }
        }
        
        for (JButton btn : boutonsPlay.values()) {
            if (btn.getText().contains("Pause")) {
                btn.addActionListener(e -> {
                    if (controller.estMusiqueLancee()) {
                        controller.pauserMusique();
                    }
                });
            }
        }
    }

    private void connecterBoutonsPlayMorceauxRecherche() {
        Map<String, JButton> boutonsPlay = vueRecherche.getBoutonsPlayParMorceaux();
        
        for (JButton btn : boutonsPlay.values()) {
            if (btn.getText().contains("Jouer")) {
                btn.addActionListener(e -> {
                    // Vérifier le compteur de lectures pour les clients
                    if (!controller.peutLireMusique()) {
                        JOptionPane.showMessageDialog(Main.this, 
                            "Limite de lectures atteinte (5 lectures maximum).\n\nVeuillez vous reconnecter pour continuer.",
                            "Limite dépassée", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    
                    if (e.getSource() instanceof JButton) {
                        JButton button = (JButton) e.getSource();
                        String morceauId = button.getActionCommand();
                        String[] parts = morceauId.split("_", 2);
                        if (parts.length > 1) {
                            String titre = parts[1];
                            String cheminFichier = "musiques/" + titre + ".wav";
                            
                            // Vérifier si le fichier existe
                            java.io.File fichier = new java.io.File(cheminFichier);
                            if (!fichier.exists()) {
                                JOptionPane.showMessageDialog(Main.this, 
                                    "Fichier audio non trouvé :\n\n" + fichier.getAbsolutePath() + 
                                    "\n\nVeuillez ajouter le fichier \"" + titre + ".wav\" " +
                                    "\ndans le dossier \"musiques/\"",
                                    "Fichier manquant", JOptionPane.WARNING_MESSAGE);
                                return;
                            }
                            
                            if (!controller.lancerMusique(cheminFichier)) {
                                JOptionPane.showMessageDialog(Main.this, 
                                    "Impossible de lancer la musique.\n\nVérifiez que le format WAV est correct.",
                                    "Erreur audio", JOptionPane.ERROR_MESSAGE);
                            } else {
                                // Enregistrer dans l'historique
                                String[] donneesMorceau = vueRecherche.getDonneesMorceau(morceauId);
                                if (donneesMorceau != null) {
                                    controller.enregistrerLecture(donneesMorceau);
                                }
                                
                                // Incrémenter le compteur si la musique a bien été lancée
                                controller.incrementerCompteurLectures();
                            }
                        }
                    }
                });
            }
        }
        
        for (JButton btn : boutonsPlay.values()) {
            if (btn.getText().contains("Pause")) {
                btn.addActionListener(e -> {
                    if (controller.estMusiqueLancee()) {
                        controller.pauserMusique();
                    }
                });
            }
        }
    }

    private void connecterBoutonsDetailMorceaux() {
        // Connecter les boutons de détail de VueMorceaux
        Map<String, JButton> boutonsDetailMorceaux = vueMorceaux.getBoutonsDetailMorceau();
        for (String cle : boutonsDetailMorceaux.keySet()) {
            JButton btn = boutonsDetailMorceaux.get(cle);
            btn.addActionListener(e -> {
                String[] donneesMorceau = vueMorceaux.getDonneesMorceau(cle);
                if (donneesMorceau != null && donneesMorceau.length >= 6) {
                    vueDetailMorceau.afficherDetailMorceau(donneesMorceau);
                    vueDetailMorceau.definirBoutonJouer(cle);
                    afficherDetailMorceau();
                }
            });
        }
        
        // Connecter les boutons de détail de VueRecherche
        Map<String, JButton> boutonsDetailRecherche = vueRecherche.getBoutonsDetailMorceau();
        for (String cle : boutonsDetailRecherche.keySet()) {
            JButton btn = boutonsDetailRecherche.get(cle);
            btn.addActionListener(e -> {
                String[] donneesMorceau = vueRecherche.getDonneesMorceau(cle);
                if (donneesMorceau != null && donneesMorceau.length >= 6) {
                    vueDetailMorceau.afficherDetailMorceau(donneesMorceau);
                    vueDetailMorceau.definirBoutonJouer(cle);
                    afficherDetailMorceau();
                }
            });
        }
    }

    private void afficherDetailMorceau() {
        setContentPane(vueDetailMorceau);
        revalidate();
        repaint();
    }

    private void afficherArtiste(String nomArtiste) {
        // Charger toutes les infos
        String[] infos = controller.chargerInfosArtiste(nomArtiste);
        System.out.println("Chargement artiste: " + nomArtiste);
        if (infos != null && infos.length >= 3) {
            System.out.println("Infos trouvées: " + infos[0]);
            java.util.Set<String> albums = controller.chargerAlbumsArtiste(nomArtiste);
            System.out.println("Albums trouvés: " + albums.size());
            ArrayList<String[]> morceaux = controller.chargerMorceauxArtiste(nomArtiste);
            System.out.println("Morceaux trouvés: " + morceaux.size());
            
            // Afficher tout en une seule fois
            vueArtiste.afficherComplet(infos[0], infos[1], infos[2], albums, morceaux);
            connecterBoutonsPlayArtiste();
            
            setContentPane(vueArtiste);
            revalidate();
            repaint();
        } else {
            System.out.println("Infos non trouvées pour: " + nomArtiste);
        }
    }

    private void connecterBoutonsPlayArtiste() {
        Map<String, JButton> boutonsPlay = vueArtiste.getBoutonsPlayParMorceaux();
        Map<String, JButton> boutonsPause = vueArtiste.getBoutonsPauseParMorceaux();
        Map<String, String[]> donnees = vueArtiste.getDonneesMorceau("");

        // Connecter les boutons Play
        for (String cle : boutonsPlay.keySet()) {
            JButton btnPlay = boutonsPlay.get(cle);
            btnPlay.addActionListener(e -> {
                String[] morceau = donnees.get(cle);
                if (morceau != null && morceau.length > 2) {
                    // Utiliser le TITRE (index 2) pour le chemin du fichier, pas l'ID
                    String titre = morceau[2];
                    String cheminFichier = "musiques/" + titre + ".wav";
                    java.io.File fichier = new java.io.File(cheminFichier);
                    
                    if (!fichier.exists()) {
                        JOptionPane.showMessageDialog(Main.this, 
                            "Fichier audio non trouvé :\n\n" + fichier.getAbsolutePath() + 
                            "\n\nVeuillez ajouter le fichier \"" + titre + ".wav\" " +
                            "\ndans le dossier \"musiques/\"",
                            "Fichier manquant", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    
                    if (!controller.lancerMusique(cheminFichier)) {
                        JOptionPane.showMessageDialog(Main.this, 
                            "Impossible de lancer la musique.\n\nVérifiez que le format WAV est correct.",
                            "Erreur audio", JOptionPane.ERROR_MESSAGE);
                    } else {
                        // Enregistrer l'écoute si l'utilisateur est connecté et pas un visiteur
                        if (controller.getUser() != null && 
                            !controller.getUser().getClass().getSimpleName().equals("Visiteur")) {
                            controller.enregistrerLecture(morceau);
                        }
                    }
                }
            });
        }

        // Connecter les boutons Pause
        for (String cle : boutonsPause.keySet()) {
            JButton btnPause = boutonsPause.get(cle);
            btnPause.addActionListener(e -> {
                controller.pauserMusique();
            });
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Main frame = new Main();
            frame.setVisible(true);
        });
    }
}
