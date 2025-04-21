import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

/**
 * La classe `RechercherContact` crée une fenêtre pour rechercher des contacts
 * avec des correspondances exactes basées sur les cases à cocher sélectionnées.
 */
public class RechercherContact extends JDialog {

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
        super(parent, "Rechercher des Contacts", true);
        this.fenetreContacts = fenetreContacts;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
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
        sexeComboBox = new JComboBox<>(new String[]{"", "Male", "Female"}); // Ajouter une option vide
        champsPanel.add(sexeComboBox);

        categorieCheckBox = new JCheckBox("Catégorie:");
        champsPanel.add(categorieCheckBox);
        categorieComboBox = new JComboBox<>(new String[]{"", "Amis", "Famille", "Travail"}); // Ajouter une option vide
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
                effectuerRecherche();
            }
        });

        annulerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        pack();
        setLocationRelativeTo(parent);
        setVisible(true);
    }

    /**
     * Collecte les critères de recherche (correspondance exacte) et effectue la recherche.
     */
    private void effectuerRecherche() {
        Map<String, String> criteres = new HashMap<>();

        if (nomCheckBox.isSelected() && !nomTextField.getText().trim().isEmpty()) {
            criteres.put("nom", nomTextField.getText().trim());
        }
        if (prenomCheckBox.isSelected() && !prenomTextField.getText().trim().isEmpty()) {
            criteres.put("prenom", prenomTextField.getText().trim());
        }
        if (libelleCheckBox.isSelected() && !libelleTextField.getText().trim().isEmpty()) {
            criteres.put("libelle", libelleTextField.getText().trim());
        }
        if (villeCheckBox.isSelected() && !villeTextField.getText().trim().isEmpty()) {
            criteres.put("Ville", villeTextField.getText().trim());
        }
        if (sexeCheckBox.isSelected() && sexeComboBox.getSelectedItem() != null && !((String) sexeComboBox.getSelectedItem()).isEmpty()) {
            criteres.put("sexe", (String) sexeComboBox.getSelectedItem());
        }
        if (categorieCheckBox.isSelected() && categorieComboBox.getSelectedItem() != null && !((String) categorieComboBox.getSelectedItem()).isEmpty()) {
            criteres.put("Categorie", (String) categorieComboBox.getSelectedItem());
        }

        if (!criteres.isEmpty()) {
            fenetreContacts.rechercherContactsExact(criteres); // Appelle la méthode de recherche exacte.
        } else {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner au moins un critère de recherche.", "Information", JOptionPane.INFORMATION_MESSAGE);
        }
        dispose();
    }
}