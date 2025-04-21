import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

/**
 * La classe `RechercherContact` crée une fenêtre pour rechercher des contacts.
 */
public class RechercherContact extends JFrame {

    private JPanel contentPane;
    private JButton rechercherButton;
    private JButton annulerButton;
    private JCheckBox nomCheckBox;
    private JTextField nomTextField;
    private JCheckBox prenomCheckBox;
    private JTextField prenomTextField;
    private JCheckBox libelleCheckBox;
    private JTextField libelleTextField;
    private JCheckBox villeCheckBox;
    private JTextField villeTextField;
    private JCheckBox sexeCheckBox;
    private JComboBox<String> sexeComboBox;
    private JCheckBox categorieCheckBox;
    private JComboBox<String> categorieComboBox;

    private Contacts fenetreContacts; // Référence à la fenêtre principale.

    /**
     * Constructeur de la classe `RechercherContact`.
     * @param parent La fenêtre parente.
     * @param fenetreContacts Instance de la fenêtre principale `Contacts`.
     */
    public RechercherContact(JFrame parent, Contacts fenetreContacts) {
        setTitle("Rechercher des Contacts");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.fenetreContacts = fenetreContacts;
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(0, 0));

        JPanel champsPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        contentPane.add(champsPanel, BorderLayout.CENTER);

        nomCheckBox = new JCheckBox("Nom:");
        champsPanel.add(nomCheckBox);
        nomTextField = new JTextField();
        champsPanel.add(nomTextField);
        nomTextField.setColumns(10);

        prenomCheckBox = new JCheckBox("Prénom:");
        champsPanel.add(prenomCheckBox);
        prenomTextField = new JTextField();
        champsPanel.add(prenomTextField);
        prenomTextField.setColumns(10);

        libelleCheckBox = new JCheckBox("Libellé:");
        champsPanel.add(libelleCheckBox);
        libelleTextField = new JTextField();
        champsPanel.add(libelleTextField);
        libelleTextField.setColumns(10);

        villeCheckBox = new JCheckBox("Ville:");
        champsPanel.add(villeCheckBox);
        villeTextField = new JTextField();
        champsPanel.add(villeTextField);
        villeTextField.setColumns(10);

        sexeCheckBox = new JCheckBox("Sexe:");
        champsPanel.add(sexeCheckBox);
        sexeComboBox = new JComboBox<>(new String[]{"Male", "Female"});
        champsPanel.add(sexeComboBox);

        categorieCheckBox = new JCheckBox("Catégorie:");
        champsPanel.add(categorieCheckBox);
        categorieComboBox = new JComboBox<>(new String[]{"Ami", "Famille", "Travail"});
        champsPanel.add(categorieComboBox);

        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
        contentPane.add(buttonPane, BorderLayout.SOUTH);

        rechercherButton = new JButton("Rechercher");
        rechercherButton.setActionCommand("OK");
        buttonPane.add(rechercherButton);
        getRootPane().setDefaultButton(rechercherButton);

        annulerButton = new JButton("Annuler");
        annulerButton.setActionCommand("Cancel");
        buttonPane.add(annulerButton);

        rechercherButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                effectuerRecherche(); // Lance la recherche.
            }
        });

        annulerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Ferme la fenêtre.
            }
        });

        pack();
        setLocationRelativeTo(parent);
        setVisible(true);
    }

    /**
     * Collecte les critères de recherche et effectue la recherche.
     */
    private void effectuerRecherche() {
        Map<String, String> criteres = new HashMap<>(); // Crée une HashMap pour stocker les critères de recherche (clé = nom du champ, valeur = texte saisi/sélectionné).

        // Vérifie si la case à cocher du nom est sélectionnée et si le champ de texte n'est pas vide.
        if (nomCheckBox.isSelected() && !nomTextField.getText().trim().isEmpty()) {
            criteres.put("nom", nomTextField.getText().trim()); // Ajoute le nom saisi aux critères.
        }
        // Vérifie si la case à cocher du prénom est sélectionnée et si le champ de texte n'est pas vide.
        if (prenomCheckBox.isSelected() && !prenomTextField.getText().trim().isEmpty()) {
            criteres.put("prenom", prenomTextField.getText().trim()); // Ajoute le prénom saisi aux critères.
        }
        // Vérifie si la case à cocher du libellé est sélectionnée et si le champ de texte n'est pas vide.
        if (libelleCheckBox.isSelected() && !libelleTextField.getText().trim().isEmpty()) {
            criteres.put("libelle", libelleTextField.getText().trim()); // Ajoute le libellé saisi aux critères.
        }
        // Vérifie si la case à cocher de la ville est sélectionnée et si le champ de texte n'est pas vide.
        if (villeCheckBox.isSelected() && !villeTextField.getText().trim().isEmpty()) {
            criteres.put("Ville", villeTextField.getText().trim()); // Ajoute la ville saisie aux critères.
        }
        // Vérifie si la case à cocher du sexe est sélectionnée.
        if (sexeCheckBox.isSelected()) {
            criteres.put("sexe", (String) sexeComboBox.getSelectedItem()); // Ajoute le sexe sélectionné aux critères.
        }
        // Vérifie si la case à cocher de la catégorie est sélectionnée.
        if (categorieCheckBox.isSelected()) {
            criteres.put("Categorie", (String) categorieComboBox.getSelectedItem()); // Ajoute la catégorie sélectionnée aux critères.
        }

        // Vérifie si au moins un critère de recherche a été sélectionné.
        if (!criteres.isEmpty()) {
            fenetreContacts.rechercherContacts(criteres); // Appelle la méthode de recherche dans la fenêtre `Contacts` en passant les critères.
        } else {
            // Affiche un message informant l'utilisateur de sélectionner au moins un critère de recherche.
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner au moins un critère de recherche.", "Information", JOptionPane.INFORMATION_MESSAGE);
        }
        dispose(); // Ferme la fenêtre de recherche après avoir tenté d'effectuer la recherche.
    }
}