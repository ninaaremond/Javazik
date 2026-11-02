package javazik.vue;

import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;

public class VueAccueil extends JPanel {

    private JButton btnConnexion;
    private JButton btnInscription;
    private JButton btnVisiteur;

    public VueAccueil() {
        setLayout(new BorderLayout());
        setBackground(new Color(253, 235, 241));
        construireUI();
    }

    private void construireUI() {
        // Header
        JLabel titre = new JLabel("JAVAZIK", SwingConstants.CENTER);
        titre.setFont(new Font("Arial", Font.BOLD, 36));
        titre.setForeground(new Color(204, 127, 151));
        titre.setBorder(BorderFactory.createEmptyBorder(60, 0, 40, 0));
        add(titre, BorderLayout.NORTH);

        // Centre - boutons
        JPanel panneauBoutons = new JPanel();
        panneauBoutons.setLayout(new BoxLayout(panneauBoutons, BoxLayout.Y_AXIS));
        panneauBoutons.setOpaque(false);
        panneauBoutons.setBorder(BorderFactory.createEmptyBorder(20, 100, 20, 100));

        btnConnexion = creerBouton("Se connecter", new Color(204, 127, 151));
        btnInscription = creerBouton("S'inscrire", new Color(204, 127, 151));
        btnVisiteur = creerBouton("Continuer en tant que visiteur", new Color(251, 212, 222));

        panneauBoutons.add(btnConnexion);
        panneauBoutons.add(Box.createVerticalStrut(15));
        panneauBoutons.add(btnInscription);
        panneauBoutons.add(Box.createVerticalStrut(30));
        panneauBoutons.add(btnVisiteur);

        add(panneauBoutons, BorderLayout.CENTER);
    }

    private JButton creerBouton(String texte, Color couleur) {
        JButton btn = new JButton(texte);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(300, 45));
        btn.setPreferredSize(new Dimension(300, 45));
        btn.setBackground(couleur);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    public void ajouterEcouteurConnexion(ActionListener al) { btnConnexion.addActionListener(al); }
    public void ajouterEcouteurInscription(ActionListener al) { btnInscription.addActionListener(al); }
    public void ajouterEcouteurVisiteur(ActionListener al) { btnVisiteur.addActionListener(al); }
}