package javazik.vue;

import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;

public class VueMenuAdmin extends JPanel {

    private JButton btnMesInfos;
    private JButton btnMorceaux;
    private JButton btnPlaylists;
    private JButton btnRecherche;
    private JButton btnHistorique;
    private JButton btnGestionUtilisateurs;
    private JButton btnGestionCatalogue;
    private JButton btnStatistiques;
    private JButton btnDeconnexion;

    public VueMenuAdmin() {
        setLayout(new BorderLayout());
        setBackground(new Color(253, 235, 241));
        construireUI();
    }

    private void construireUI() {
        add(creerHeader(), BorderLayout.NORTH);

        JPanel centre = new JPanel();
        centre.setLayout(new BoxLayout(centre, BoxLayout.Y_AXIS));
        centre.setOpaque(false);
        centre.setBorder(BorderFactory.createEmptyBorder(20, 60, 20, 60));

        // Séparation : fonctions client
        centre.add(creerSeparateur("Catalogue"));
        centre.add(Box.createVerticalStrut(8));
        btnMorceaux = creerBoutonMenu(" > Afficher tous les morceaux", new Color(251, 212, 222));
        btnPlaylists = creerBoutonMenu(" > Afficher mes playlists", new Color(251, 212, 222));
        btnMesInfos = creerBoutonMenu(" > Mes infos", new Color(251, 212, 222));
        btnRecherche = creerBoutonMenu(" > Recherche avancée", new Color(251, 212, 222));
        btnHistorique = creerBoutonMenu(" > Historique d'écoute", new Color(251, 212, 222));
        centre.add(btnMorceaux);
        centre.add(Box.createVerticalStrut(8));
        centre.add(btnPlaylists);
        centre.add(Box.createVerticalStrut(8));
        centre.add(btnMesInfos);
        centre.add(Box.createVerticalStrut(8));
        centre.add(btnRecherche);
        centre.add(Box.createVerticalStrut(8));
        centre.add(btnHistorique);
        centre.add(Box.createVerticalStrut(15));

        // Séparation : fonctions admin
        centre.add(creerSeparateur("Administration"));
        centre.add(Box.createVerticalStrut(8));
        btnGestionUtilisateurs = creerBoutonMenu(" > Gestion des utilisateurs", new Color(255, 185, 207));
        btnGestionCatalogue = creerBoutonMenu(" > Gestion du catalogue", new Color(255, 185, 207));
        btnStatistiques = creerBoutonMenu(" > Afficher les statistiques", new Color(255, 185, 207));
        centre.add(btnGestionUtilisateurs);
        centre.add(Box.createVerticalStrut(8));
        centre.add(btnGestionCatalogue);
        centre.add(Box.createVerticalStrut(8));
        centre.add(btnStatistiques);

        add(centre, BorderLayout.CENTER);

        btnDeconnexion = new JButton("Se déconnecter");
        btnDeconnexion.setBackground(new Color(200, 100, 100));
        btnDeconnexion.setForeground(Color.WHITE);
        btnDeconnexion.setFocusPainted(false);
        btnDeconnexion.setBorderPainted(false);
        btnDeconnexion.setFont(new Font("Arial", Font.BOLD, 12));
        JPanel bas = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bas.setOpaque(false);
        bas.add(btnDeconnexion);
        add(bas, BorderLayout.SOUTH);
    }

    private JPanel creerHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(204, 127, 151));
        header.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        JLabel lbl = new JLabel("JAVAZIK  |  Admin");
        lbl.setFont(new Font("Arial", Font.BOLD, 22));
        lbl.setForeground(Color.WHITE);
        header.add(lbl, BorderLayout.WEST);
        return header;
    }

    private JLabel creerSeparateur(String texte) {
        JLabel sep = new JLabel(texte.toUpperCase());
        sep.setFont(new Font("Arial", Font.BOLD, 11));
        sep.setForeground(new Color(204, 127, 151));
        sep.setAlignmentX(Component.LEFT_ALIGNMENT);
        return sep;
    }

    private JButton creerBoutonMenu(String texte, Color couleur) {
        JButton btn = new JButton(texte);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        btn.setBackground(couleur);
        btn.setForeground(new Color(60, 30, 100));
        btn.setFont(new Font("Arial", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 20));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    public void ajouterEcouteurMesInfos(ActionListener al) { btnMesInfos.addActionListener(al); }
    public void ajouterEcouteurMorceaux(ActionListener al) { btnMorceaux.addActionListener(al); }
    public void ajouterEcouteurPlaylists(ActionListener al) { btnPlaylists.addActionListener(al); }
    public void ajouterEcouteurRecherche(ActionListener al) { btnRecherche.addActionListener(al); }
    public void ajouterEcouteurHistorique(ActionListener al) { btnHistorique.addActionListener(al); }
    public void ajouterEcouteurGestionUtilisateurs(ActionListener al) { btnGestionUtilisateurs.addActionListener(al); }
    public void ajouterEcouteurGestionCatalogue(ActionListener al) { btnGestionCatalogue.addActionListener(al); }
    public void ajouterEcouteurStatistiques(ActionListener al) { btnStatistiques.addActionListener(al); }
    public void ajouterEcouteurDeconnexion(ActionListener al) { btnDeconnexion.addActionListener(al); }
}