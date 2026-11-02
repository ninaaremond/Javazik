package javazik.vue;

import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;

public class VueConnexion extends JPanel {

    private JTextField champEmail;
    private JPasswordField champMotDePasse;
    private JButton btnConnexion;
    private JButton btnRetour;
    private JLabel lblErreur;

    public VueConnexion() {
        setLayout(new BorderLayout());
        setBackground(new Color(253, 235, 241));
        construireUI();
    }

    private void construireUI() {
        // Header
        JPanel header = creerHeader("Connexion");
        add(header, BorderLayout.NORTH);

        // Formulaire
        JPanel formulaire = new JPanel();
        formulaire.setLayout(new BoxLayout(formulaire, BoxLayout.Y_AXIS));
        formulaire.setOpaque(false);
        formulaire.setBorder(BorderFactory.createEmptyBorder(30, 80, 30, 80));

        champEmail = new JTextField();
        champEmail.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        champEmail.setFont(new Font("Arial", Font.PLAIN, 14));

        champMotDePasse = new JPasswordField();
        champMotDePasse.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        champMotDePasse.setFont(new Font("Arial", Font.PLAIN, 14));

        lblErreur = new JLabel(" ");
        lblErreur.setForeground(Color.RED);
        lblErreur.setAlignmentX(Component.CENTER_ALIGNMENT);

        btnConnexion = creerBouton("Se connecter", new Color(204, 127, 151));
        btnRetour = creerBouton("Retour", new Color(255, 185, 207));

        formulaire.add(creerLabel("Entrez votre email"));
        formulaire.add(Box.createVerticalStrut(5));
        formulaire.add(champEmail);
        formulaire.add(Box.createVerticalStrut(15));
        formulaire.add(creerLabel("Entrez votre mot de passe"));
        formulaire.add(Box.createVerticalStrut(5));
        formulaire.add(champMotDePasse);
        formulaire.add(Box.createVerticalStrut(10));
        formulaire.add(lblErreur);
        formulaire.add(Box.createVerticalStrut(20));
        formulaire.add(btnConnexion);
        formulaire.add(Box.createVerticalStrut(10));
        formulaire.add(btnRetour);

        add(formulaire, BorderLayout.CENTER);
    }

    private JPanel creerHeader(String titre) {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(204, 127, 151));
        header.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        JLabel lblTitre = new JLabel("JAVAZIK - " + titre);
        lblTitre.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitre.setForeground(Color.WHITE);
        header.add(lblTitre, BorderLayout.CENTER);
        return header;
    }

    private JLabel creerLabel(String texte) {
        JLabel lbl = new JLabel(texte);
        lbl.setFont(new Font("Arial", Font.PLAIN, 13));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    private JButton creerBouton(String texte, Color couleur) {
        JButton btn = new JButton(texte);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        btn.setBackground(couleur);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    public String getEmail() { return champEmail.getText().trim(); }
    public String getMotDePasse() { return new String(champMotDePasse.getPassword()); }
    public void afficherErreur(String msg) { lblErreur.setText(msg); }
    public void reinitialiser() { champEmail.setText(""); champMotDePasse.setText(""); lblErreur.setText(" "); }

    public void ajouterEcouteurConnexion(ActionListener al) { btnConnexion.addActionListener(al); }
    public void ajouterEcouteurRetour(ActionListener al) { btnRetour.addActionListener(al); }
}