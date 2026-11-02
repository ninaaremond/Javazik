package javazik.vue;

import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;

public class VueModifierInfos extends JPanel {

    private JButton btnRetour;
    private JTextField champEmail;
    private JPasswordField champMdp;
    private JButton btnModifier;
    private JLabel lblMessage;

    public VueModifierInfos() {
        setLayout(new BorderLayout());
        setBackground(new Color(253, 235, 241));
        construireUI();
    }

    private void construireUI() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(204, 127, 151));
        header.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        JLabel titre = new JLabel("JAVAZIK - Modifier mes infos");
        titre.setFont(new Font("Arial", Font.BOLD, 20));
        titre.setForeground(Color.WHITE);
        btnRetour = new JButton("← Retour");
        styleRetour(btnRetour);
        header.add(titre, BorderLayout.WEST);
        header.add(btnRetour, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setOpaque(false);
        form.setBorder(BorderFactory.createEmptyBorder(40, 80, 40, 80));

        champEmail = new JTextField();
        champEmail.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        champMdp = new JPasswordField();
        champMdp.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        lblMessage = new JLabel(" ");
        lblMessage.setAlignmentX(Component.CENTER_ALIGNMENT);

        btnModifier = new JButton("Modifier les infos");
        btnModifier.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnModifier.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        btnModifier.setBackground(new Color(204, 127, 151));
        btnModifier.setForeground(Color.WHITE);
        btnModifier.setFocusPainted(false);
        btnModifier.setBorderPainted(false);
        btnModifier.setFont(new Font("Arial", Font.BOLD, 14));

        form.add(creerLabel("Nouvel email"));
        form.add(Box.createVerticalStrut(5));
        form.add(champEmail);
        form.add(Box.createVerticalStrut(15));
        form.add(creerLabel("Nouveau mot de passe"));
        form.add(Box.createVerticalStrut(5));
        form.add(champMdp);
        form.add(Box.createVerticalStrut(15));
        form.add(lblMessage);
        form.add(Box.createVerticalStrut(10));
        form.add(btnModifier);

        add(form, BorderLayout.CENTER);
    }

    private JLabel creerLabel(String t) {
        JLabel l = new JLabel(t);
        l.setFont(new Font("Arial", Font.PLAIN, 13));
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }

    private void styleRetour(JButton btn) {
        btn.setBackground(new Color(255, 185, 207));
        btn.setForeground(new Color(80, 40, 120));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setFont(new Font("Arial", Font.BOLD, 12));
    }

    public String getNouvelEmail() { return champEmail.getText().trim(); }
    public String getNouveauMdp() { return new String(champMdp.getPassword()); }
    public void afficherMessage(String msg, boolean succes) {
        lblMessage.setForeground(succes ? new Color(0, 150, 0) : Color.RED);
        lblMessage.setText(msg);
    }

    public void ajouterEcouteurRetour(ActionListener al) { btnRetour.addActionListener(al); }
    public void ajouterEcouteurModifier(ActionListener al) { btnModifier.addActionListener(al); }
}