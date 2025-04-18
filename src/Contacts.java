import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Contacts extends JFrame{
    // Declare the JTable and the model
    private JTable table;
    private DefaultTableModel model;
    //load data from database
    public void loadContactsFromDatabase() {
        String url = "jdbc:mysql://localhost:3306/gestion_contacts?useSSL=false&serverTimezone=UTC";
        String user = "root";
        String password = "";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // Optional but safe
            Connection conn = DriverManager.getConnection(url, user, password);

            String query = "SELECT \n" +
                    "    c.id,\n" +
                    "    c.nom,\n" +
                    "    c.prenom,\n" +
                    "    c.libelle,\n" +
                    "    c.telPerso,\n" +
                    "    c.telPro,\n" +
                    "    c.email,\n" +
                    "    c.sexe,\n" +
                    "    v.NomVille as 'Ville',\n" +
                    "    cat.NomCategorie as 'Categorie'\n" +
                    "FROM \n" +
                    "    contact c\n" +
                    "JOIN \n" +
                    "    Ville v ON c.NumVille = v.NumVille\n" +
                    "JOIN \n" +
                    "    Categorie cat ON c.NumCat = cat.NumCat\n"+
                    "ORDER BY c.id asc;\n";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            // Clear existing rows before loading
            model.setRowCount(0);

            while (rs.next()) {
                Object[] row = {
                        rs.getString("id"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("libelle"),
                        rs.getString("telPerso"),
                        rs.getString("telPro"),
                        rs.getString("email"),
                        rs.getString("sexe"),
                        rs.getString("Ville"),
                        rs.getString("Categorie")
                };
                model.addRow(row);
            }

            rs.close();
            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement des contacts.");
        }
    }
    public Contacts() {
        // Characteristiques de Frame
        setTitle("Gestion des Contacts");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Set the layout for the main panel
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Create column names for the contact table
        String[] columns = {"id", "nom", "prenom", "libelle", "telPerso", "telPro", "email", "sexe","Ville","Categorie"};

        // Initialize the table model with column names
        model = new DefaultTableModel(columns,0);

        // Initialize the table with the model
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Create a panel for the buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        // Create buttons for Add, Modify, Delete, Search, and Quit
        JButton addButton = new JButton("Ajouter");
        JButton modifyButton = new JButton("Modifier");
        JButton deleteButton = new JButton("Supprimer");
        JButton searchButton = new JButton("Rechercher");
        JButton quitButton = new JButton("Quitter");
        loadContactsFromDatabase();

        // Add button action listeners
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle adding new contact (simple input dialog for now)
                new AjouterContact(model);

            }
        });

        modifyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle modifying an existing contact (simple edit of the selected row)

                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    String telPerso = (table.getValueAt(table.getSelectedRow(),4)).toString();
                    String telPro = (table.getValueAt(table.getSelectedRow(),5)).toString();
                    new ModifierContact(model, selectedRow, telPerso,telPro);
                } else {
                    JOptionPane.showMessageDialog(null, "Sélectionnez une ligne à modifier.");
                }
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle deleting a selected contact
                Object[] options = {"Confirmer", "Annuler"};
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    // Ask for confirmation before deleting
                    int confirmation = JOptionPane.showConfirmDialog(
                            null,
                            "Êtes-vous sûr de vouloir supprimer ce contact ?",
                            "Confirmer la suppression",
                            JOptionPane.DEFAULT_OPTION
                    );

                    if (confirmation == JOptionPane.YES_OPTION) {
                        // If user confirms, remove the row from the table
                        model.removeRow(selectedRow);
                        JOptionPane.showMessageDialog(null, "Contact supprimé avec succès.");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Sélectionnez un contact à supprimer.");
                }
            }
        });

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new RechercheContact(model,table);
            }
        });

        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle quitting the application
                //JOptionPane.showMessageDialog(null, table.getValueAt(table.getSelectedRow(),3));
                System.exit(0);
            }
        });

        // Add buttons to the button panel
        buttonPanel.add(addButton);
        buttonPanel.add(modifyButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(searchButton);
        buttonPanel.add(quitButton);

        // Add the button panel to the main panel
        panel.add(buttonPanel, BorderLayout.SOUTH);

        // Add the main panel to the frame
        add(panel);

        setVisible(true);
    }
    public static void main(String[] args) {
        // Run the application
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Contacts();
            }
        });
    }
}