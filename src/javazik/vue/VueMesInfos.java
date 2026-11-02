package javazik.vue;

import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;

public class VueMesInfos extends JPanel {

    private JButton btnRetour;
    private JLabel lblNom;
    private JLabel lblPrenom;
    private JLabel lblEmail;
    private JLabel lblMdp;
    private JButton btnModifier;
    private JButton btnSupprimer;

    public VueMesInfos() {
        setLayout(new BorderLayout());
        setBackground(new Color(253, 235, 241));
        construireUI();
    }

    private void construireUI() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(204, 127, 151));
        header.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        JLabel titre = new JLabel("JAVAZIK - Mes infos");
        titre.setFont(new Font("Arial", Font.BOLD, 20));
        titre.setForeground(Color.WHITE);
        btnRetour = new JButton("← Retour");
        styleRetour(btnRetour);
        header.add(titre, BorderLayout.WEST);
        header.add(btnRetour, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        JPanel centre = new JPanel();
        centre.setLayout(new BoxLayout(centre, BoxLayout.Y_AXIS));
        centre.setOpaque(false);
        centre.setBorder(BorderFactory.createEmptyBorder(40, 80, 40, 80));

        lblNom = new JLabel("Nom : ");
        lblNom.setFont(new Font("Arial", Font.PLAIN, 16));
        lblNom.setForeground(new Color(40, 40, 40));
        
        lblPrenom = new JLabel("Prénom : ");
        lblPrenom.setFont(new Font("Arial", Font.PLAIN, 16));
        lblPrenom.setForeground(new Color(40, 40, 40));
        
        lblEmail = new JLabel("Email : ");
        lblEmail.setFont(new Font("Arial", Font.PLAIN, 16));
        lblEmail.setForeground(new Color(40, 40, 40));
        
        lblMdp = new JLabel("Mot de passe : ••••••••");
        lblMdp.setFont(new Font("Arial", Font.PLAIN, 16));
        lblMdp.setForeground(new Color(40, 40, 40));

        btnModifier = creerBouton("✏️  Modifier mes informations", new Color(160, 100, 220));
        btnSupprimer = creerBouton("🗑️  Supprimer mon compte", new Color(210, 90, 90));

        centre.add(lblNom);
        centre.add(Box.createVerticalStrut(15));
        centre.add(lblPrenom);
        centre.add(Box.createVerticalStrut(15));
        centre.add(lblEmail);
        centre.add(Box.createVerticalStrut(15));
        centre.add(lblMdp);
        centre.add(Box.createVerticalStrut(50));
        centre.add(btnModifier);
        centre.add(Box.createVerticalStrut(12));
        centre.add(btnSupprimer);

        add(centre, BorderLayout.CENTER);
    }

    public void afficherInfos(String nom, String prenom, String email) {
        lblNom.setText("Nom : " + nom);
        lblPrenom.setText("Prénom : " + prenom);
        lblEmail.setText("Email : " + email);
    }

    private JButton creerBouton(String texte, Color couleur) {
        JButton btn = new JButton(texte);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        btn.setBackground(couleur);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void styleRetour(JButton btn) {
        btn.setBackground(new Color(255, 185, 207));
        btn.setForeground(new Color(80, 40, 120));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setFont(new Font("Arial", Font.BOLD, 12));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    public void ajouterEcouteurRetour(ActionListener al) { btnRetour.addActionListener(al); }
    public void ajouterEcouteurModifier(ActionListener al) { btnModifier.addActionListener(al); }
    public void ajouterEcouteurSupprimer(ActionListener al) { btnSupprimer.addActionListener(al); }
}