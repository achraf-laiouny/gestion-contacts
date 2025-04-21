import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

/**
 * La classe `Contacts` représente la fenêtre principale de l'application
 * de gestion des contacts. Elle affiche une liste de contacts provenant
 * d'une base de données et permet d'effectuer des opérations telles que
 * l'ajout, la modification, la suppression et la recherche de contacts.
 * Elle hérite de `JFrame`, ce qui en fait une fenêtre graphique.
 */
public class Contacts extends JFrame {
    private JTable table;
    private NonEditableTableModel model;

    private static class NonEditableTableModel extends DefaultTableModel {
        public NonEditableTableModel(Object[] columnNames, int rowCount) {
            super(columnNames, rowCount);
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    }

    private Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/gestion_contacts?useSSL=false&serverTimezone=UTC";
        String user = "root";
        String password = "";
        return DriverManager.getConnection(url, user, password);
    }

    public void loadContactsFromDatabase() {
        String query = "SELECT c.id, c.nom, c.prenom, c.libelle, c.telPerso, c.telPro, c.email, c.sexe, v.NomVille AS Ville, cat.NomCategorie AS Categorie FROM contact c JOIN Ville v ON c.NumVille = v.NumVille JOIN Categorie cat ON c.NumCat = cat.NumCat ORDER BY c.id ASC";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

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
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement des contacts.");
        }
    }

    public void rechercherContactsExact(Map<String, String> criteres) {
        String url = "jdbc:mysql://localhost:3306/gestion_contacts?useSSL=false&serverTimezone=UTC";
        String user = "root";
        String password = "";
        StringBuilder query = new StringBuilder("SELECT c.id, c.nom, c.prenom, c.libelle, c.telPerso, c.telPro, c.email, c.sexe, v.NomVille AS Ville, cat.NomCategorie AS Categorie FROM contact c JOIN Ville v ON c.NumVille = v.NumVille JOIN Categorie cat ON c.NumCat = cat.NumCat WHERE ");
        java.util.List<String> conditions = new java.util.ArrayList<>();

        for (Map.Entry<String, String> entry : criteres.entrySet()) {
            String cle = entry.getKey();
            String valeur = entry.getValue();
            if (!valeur.isEmpty()) {
                if (cle.equals("Categorie")) {
                    conditions.add("cat.NomCategorie = ?"); // Cibler la colonne NomCategorie de la table Categorie
                } else if (cle.equals("Ville")) {
                    conditions.add("v.NomVille = ?"); // Cibler la colonne NomVille de la table Ville
                } else {
                    conditions.add("c." + cle + " = ?"); // Cibler les colonnes de la table contact
                }
            }
        }

        if (conditions.isEmpty()) {
            loadContactsFromDatabase();
            return;
        }

        query.append(String.join(" AND ", conditions));
        query.append(" ORDER BY c.id ASC");

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(query.toString())) {

            int parameterIndex = 1;
            for (Map.Entry<String, String> entry : criteres.entrySet()) {
                String valeur = entry.getValue();
                if (!valeur.isEmpty()) {
                    stmt.setString(parameterIndex++, valeur);
                }
            }

            ResultSet rs = stmt.executeQuery();
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
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors de la recherche des contacts.");
        }
    }

    public Contacts() {
        setTitle("Gestion des Contacts");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout());

        String[] columns = {"id", "nom", "prenom", "libelle", "telPerso", "telPro", "email", "sexe", "Ville", "Categorie"};
        model = new NonEditableTableModel(columns, 0);
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());

        JButton addButton = new JButton("Ajouter");
        JButton modifyButton = new JButton("Modifier");
        JButton deleteButton = new JButton("Supprimer");
        JButton searchButton = new JButton("Rechercher");
        JButton quitButton = new JButton("Quitter");
        JButton restartButton = new JButton("Restart");

        loadContactsFromDatabase();

        Contacts self = this;
        addButton.addActionListener(e -> new AjouterContact((DefaultTableModel) table.getModel()));

        modifyButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                int contactId = Integer.parseInt(table.getValueAt(selectedRow, 0).toString());
                new ModifierContact(model, selectedRow, contactId);
            } else {
                JOptionPane.showMessageDialog(null, "Sélectionnez une ligne à modifier.");
            }
        });

        deleteButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                new SupprimerContact(Contacts.this, Contacts.this, table);
            } else {
                JOptionPane.showMessageDialog(null, "Sélectionnez un contact à supprimer.");
            }
        });

        searchButton.addActionListener(e -> new RechercherContact(Contacts.this, Contacts.this));

        quitButton.addActionListener(e -> System.exit(0));

        restartButton.addActionListener(e -> loadContactsFromDatabase());

        buttonPanel.add(addButton);
        buttonPanel.add(modifyButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(searchButton);
        buttonPanel.add(restartButton);
        buttonPanel.add(quitButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);
        add(panel);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Contacts::new);
    }
}