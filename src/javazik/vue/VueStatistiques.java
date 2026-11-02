package javazik.vue;

import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;

public class VueStatistiques extends JPanel {

    private JButton btnRetour;
    private JLabel lblUtilisateurs;
    private JLabel lblClientsActifs;
    private JLabel lblMorceaux;
    private JLabel lblAlbums;
    private JLabel lblArtistes;
    private JLabel lblGroupes;
    private JLabel lblEcoutes;

    public VueStatistiques() {
        setLayout(new BorderLayout());
        setBackground(new Color(253, 235, 241));
        construireUI();
    }

    private void construireUI() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(204, 127, 151));
        header.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        JLabel titre = new JLabel("JAVAZIK - Statistiques");
        titre.setFont(new Font("Arial", Font.BOLD, 20));
        titre.setForeground(Color.WHITE);
        btnRetour = new JButton("← Retour");
        styleRetour(btnRetour);
        header.add(titre, BorderLayout.WEST);
        header.add(btnRetour, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        JPanel grille = new JPanel(new GridLayout(0, 2, 20, 15));
        grille.setOpaque(false);
        grille.setBorder(BorderFactory.createEmptyBorder(30, 60, 30, 60));

        lblUtilisateurs = creerStatLabel("0");
        lblClientsActifs = creerStatLabel("0");
        lblMorceaux = creerStatLabel("0");
        lblAlbums = creerStatLabel("0");
        lblArtistes = creerStatLabel("0");
        lblGroupes = creerStatLabel("0");
        lblEcoutes = creerStatLabel("0");

        grille.add(creerCarteStatistique("👥 Utilisateurs", lblUtilisateurs));
        grille.add(creerCarteStatistique("✅ Clients actifs", lblClientsActifs));
        grille.add(creerCarteStatistique("🎵 Morceaux", lblMorceaux));
        grille.add(creerCarteStatistique("💿 Albums", lblAlbums));
        grille.add(creerCarteStatistique("🎤 Artistes", lblArtistes));
        grille.add(creerCarteStatistique("🎸 Groupes", lblGroupes));
        grille.add(creerCarteStatistique("▶️ Total écoutes", lblEcoutes));

        add(grille, BorderLayout.CENTER);
    }

    private JPanel creerCarteStatistique(String libelle, JLabel valeur) {
        JPanel carte = new JPanel(new BorderLayout());
        carte.setBackground(new Color(251, 212, 222));
        carte.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        JLabel lbl = new JLabel(libelle);
        lbl.setFont(new Font("Arial", Font.BOLD, 13));
        lbl.setForeground(new Color(80, 40, 120));
        carte.add(lbl, BorderLayout.NORTH);
        carte.add(valeur, BorderLayout.CENTER);
        return carte;
    }

    private JLabel creerStatLabel(String val) {
        JLabel l = new JLabel(val, SwingConstants.CENTER);
        l.setFont(new Font("Arial", Font.BOLD, 28));
        l.setForeground(new Color(204, 127, 151));
        return l;
    }

    public void afficherStatistiques(int utilisateurs, int actifs, int morceaux, int albums, int artistes, int groupes, int ecoutes) {
        lblUtilisateurs.setText(String.valueOf(utilisateurs));
        lblClientsActifs.setText(String.valueOf(actifs));
        lblMorceaux.setText(String.valueOf(morceaux));
        lblAlbums.setText(String.valueOf(albums));
        lblArtistes.setText(String.valueOf(artistes));
        lblGroupes.setText(String.valueOf(groupes));
        lblEcoutes.setText(String.valueOf(ecoutes));
    }

    private void styleRetour(JButton btn) {
        btn.setBackground(new Color(255, 185, 207));
        btn.setForeground(new Color(80, 40, 120));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setFont(new Font("Arial", Font.BOLD, 12));
    }

    public void ajouterEcouteurRetour(ActionListener al) { btnRetour.addActionListener(al); }
}