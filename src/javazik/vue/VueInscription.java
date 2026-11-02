package javazik.vue;

import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;

public class VueInscription extends JPanel {

    private JTextField champNom;
    private JTextField champPrenom;
    private JTextField champEmail;
    private JPasswordField champMotDePasse;
    private JComboBox<String> comboRole;
    private JButton btnInscription;
    private JButton btnRetour;
    private JLabel lblErreur;

    public VueInscription() {
        setLayout(new BorderLayout());
        setBackground(new Color(253, 235, 241));
        construireUI();
    }

    private void construireUI() {
        add(creerHeader("Inscription"), BorderLayout.NORTH);

        JPanel formulaire = new JPanel();
        formulaire.setLayout(new BoxLayout(formulaire, BoxLayout.Y_AXIS));
        formulaire.setOpaque(false);
        formulaire.setBorder(BorderFactory.createEmptyBorder(30, 80, 30, 80));

        champNom = new JTextField();
        champNom.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        champNom.setFont(new Font("Arial", Font.PLAIN, 14));

        champPrenom = new JTextField();
        champPrenom.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        champPrenom.setFont(new Font("Arial", Font.PLAIN, 14));

        champEmail = new JTextField();
        champEmail.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        champEmail.setFont(new Font("Arial", Font.PLAIN, 14));

        champMotDePasse = new JPasswordField();
        champMotDePasse.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        champMotDePasse.setFont(new Font("Arial", Font.PLAIN, 14));

        String[] roles = {"Client", "Admin"};
        comboRole = new JComboBox<>(roles);
        comboRole.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        comboRole.setFont(new Font("Arial", Font.PLAIN, 14));

        lblErreur = new JLabel(" ");
        lblErreur.setForeground(Color.RED);
        lblErreur.setAlignmentX(Component.CENTER_ALIGNMENT);

        btnInscription = creerBouton("S'inscrire", new Color(204, 127, 151));
        btnRetour = creerBouton("Retour", new Color(255, 185, 207));

        formulaire.add(creerLabel("Entrez votre nom"));
        formulaire.add(Box.createVerticalStrut(5));
        formulaire.add(champNom);
        formulaire.add(Box.createVerticalStrut(15));
        formulaire.add(creerLabel("Entrez votre prénom"));
        formulaire.add(Box.createVerticalStrut(5));
        formulaire.add(champPrenom);
        formulaire.add(Box.createVerticalStrut(15));
        formulaire.add(creerLabel("Entrez votre email"));
        formulaire.add(Box.createVerticalStrut(5));
        formulaire.add(champEmail);
        formulaire.add(Box.createVerticalStrut(15));
        formulaire.add(creerLabel("Entrez votre mot de passe"));
        formulaire.add(Box.createVerticalStrut(5));
        formulaire.add(champMotDePasse);
        formulaire.add(Box.createVerticalStrut(15));
        formulaire.add(creerLabel("Choisissez votre rôle"));
        formulaire.add(Box.createVerticalStrut(5));
        formulaire.add(comboRole);
        formulaire.add(Box.createVerticalStrut(10));
        formulaire.add(lblErreur);
        formulaire.add(Box.createVerticalStrut(20));
        formulaire.add(btnInscription);
        formulaire.add(Box.createVerticalStrut(10));
        formulaire.add(btnRetour);

        add(formulaire, BorderLayout.CENTER);
    }

    private JPanel creerHeader(String titre) {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(204, 127, 151));
        header.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        JLabel lbl = new JLabel("JAVAZIK - " + titre);
        lbl.setFont(new Font("Arial", Font.BOLD, 20));
        lbl.setForeground(Color.WHITE);
        header.add(lbl, BorderLayout.CENTER);
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
    public String getNom() { return champNom.getText().trim(); }
    public String getPrenom() { return champPrenom.getText().trim(); }
    public int getDroit() { return comboRole.getSelectedIndex() == 0 ? 1 : 2; }
    public void afficherErreur(String msg) { lblErreur.setText(msg); }
    public void reinitialiser() { 
        champNom.setText(""); 
        champPrenom.setText(""); 
        champEmail.setText(""); 
        champMotDePasse.setText(""); 
        lblErreur.setText(" "); 
    }

    public void ajouterEcouteurInscription(ActionListener al) { btnInscription.addActionListener(al); }
    public void ajouterEcouteurRetour(ActionListener al) { btnRetour.addActionListener(al); }
}