package javazik.vue;

import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;

public class VueMenuClient extends JPanel {

    private JButton btnRecherche;
    private JButton btnMorceaux;
    private JButton btnPlaylists;
    private JButton btnMesInfos;
    private JButton btnHistorique;
    private JButton btnDeconnexion;

    public VueMenuClient() {
        setLayout(new BorderLayout());
        setBackground(new Color(253, 235, 241));
        construireUI();
    }

    private void construireUI() {
        add(creerHeader(), BorderLayout.NORTH);

        JPanel centre = new JPanel();
        centre.setLayout(new BoxLayout(centre, BoxLayout.Y_AXIS));
        centre.setOpaque(false);
        centre.setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));

        btnRecherche = creerBoutonMenu("🔎  Rechercher un morceau / un artiste / un album / un groupe");
        btnMorceaux = creerBoutonMenu("🎵  Afficher tous les morceaux");
        btnPlaylists = creerBoutonMenu("📋  Afficher mes playlists");
        btnMesInfos = creerBoutonMenu("👤  Mes infos");
        btnHistorique = creerBoutonMenu("📜  Historique d'écoute");

        centre.add(btnRecherche);
        centre.add(Box.createVerticalStrut(15));
        centre.add(btnMorceaux);
        centre.add(Box.createVerticalStrut(15));
        centre.add(btnPlaylists);
        centre.add(Box.createVerticalStrut(15));
        centre.add(btnMesInfos);
        centre.add(Box.createVerticalStrut(15));
        centre.add(btnHistorique);

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
        JLabel lbl = new JLabel("JAVAZIK");
        lbl.setFont(new Font("Arial", Font.BOLD, 22));
        lbl.setForeground(Color.WHITE);
        header.add(lbl, BorderLayout.WEST);
        return header;
    }

    private JButton creerBoutonMenu(String texte) {
        JButton btn = new JButton(texte);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        btn.setBackground(new Color(251, 212, 222));
        btn.setForeground(new Color(80, 40, 120));
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 20));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    public void ajouterEcouteurHistorique(ActionListener al) {
        btnHistorique.addActionListener(al);
    }

    public void ajouterEcouteurRecherche(ActionListener al) {
        btnRecherche.addActionListener(al);
    }

    public void ajouterEcouteurMorceaux(ActionListener al) {
        btnMorceaux.addActionListener(al);
    }

    public void ajouterEcouteurPlaylists(ActionListener al) {
        btnPlaylists.addActionListener(al);
    }

    public void ajouterEcouteurMesInfos(ActionListener al) {
        btnMesInfos.addActionListener(al);
    }

    public void ajouterEcouteurDeconnexion(ActionListener al) {
        btnDeconnexion.addActionListener(al);
    }
}