import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicComboBoxUI;
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
        super(parent, "🔎 Rechercher un Contact", true);
        this.fenetreContacts = fenetreContacts;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(600, 400);

        contentPane = new JPanel();
        contentPane.setBackground(new Color(245, 247, 250));
        contentPane.setBorder(new EmptyBorder(20, 20, 20, 20));
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(20, 20));

        JLabel title = new JLabel("Rechercher un Contact", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(new Color(44, 62, 80));
        contentPane.add(title, BorderLayout.NORTH);

        JPanel champsPanel = new JPanel(new GridLayout(0, 2, 10, 15));
        champsPanel.setOpaque(false);
        contentPane.add(champsPanel, BorderLayout.CENTER);

        Color labelColor = new Color(52, 73, 94);

        nomCheckBox = createCheckBox("Nom:", labelColor);
        nomTextField = createTextField();
        champsPanel.add(nomCheckBox);
        champsPanel.add(nomTextField);

        prenomCheckBox = createCheckBox("Prénom:", labelColor);
        prenomTextField = createTextField();
        champsPanel.add(prenomCheckBox);
        champsPanel.add(prenomTextField);

        libelleCheckBox = createCheckBox("Libellé:", labelColor);
        libelleTextField = createTextField();
        champsPanel.add(libelleCheckBox);
        champsPanel.add(libelleTextField);

        villeCheckBox = createCheckBox("Ville:", labelColor);
        villeTextField = createTextField();
        champsPanel.add(villeCheckBox);
        champsPanel.add(villeTextField);

        sexeCheckBox = createCheckBox("Sexe:", labelColor);
        sexeComboBox = createComboBox(new String[]{"", "Male", "Female"});
        champsPanel.add(sexeCheckBox);
        champsPanel.add(sexeComboBox);

        categorieCheckBox = createCheckBox("Catégorie:", labelColor);
        categorieComboBox = createComboBox(new String[]{"", "Amis", "Famille", "Travail"});
        champsPanel.add(categorieCheckBox);
        champsPanel.add(categorieComboBox);

        JPanel buttonPane = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPane.setOpaque(false);
        contentPane.add(buttonPane, BorderLayout.SOUTH);

        rechercherButton = createButton("🔍 Rechercher", new Color(52, 152, 219));
        buttonPane.add(rechercherButton);

        annulerButton = createButton("✖ Annuler", new Color(231, 76, 60));
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

        setLocationRelativeTo(parent);
        setVisible(true);
    }

    private JCheckBox createCheckBox(String label, Color color) {
        JCheckBox box = new JCheckBox(label);
        box.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        box.setBackground(new Color(245, 247, 250));
        box.setForeground(color);
        return box;
    }

    private JTextField createTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199)),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        return field;
    }

    private JComboBox<String> createComboBox(String[] options) {
        JComboBox<String> comboBox = new JComboBox<>(options);
        comboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        comboBox.setUI(new BasicComboBoxUI());
        return comboBox;
    }

    private JButton createButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        return button;
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
            fenetreContacts.rechercherContactsExact(criteres);
        } else {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner au moins un critère de recherche.", "Information", JOptionPane.INFORMATION_MESSAGE);
        }
        dispose();
    }
}
