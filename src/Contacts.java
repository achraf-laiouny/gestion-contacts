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
        return DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/gestion_contacts?useSSL=false&serverTimezone=UTC", "root", "");
    }

    public void loadContactsFromDatabase() {
        String query = "SELECT c.id, c.nom, c.prenom, c.libelle, c.telPerso, c.telPro, c.email, c.sexe, " +
                "v.NomVille AS Ville, cat.NomCategorie AS Categorie FROM contact c " +
                "JOIN Ville v ON c.NumVille = v.NumVille " +
                "JOIN Categorie cat ON c.NumCat = cat.NumCat ORDER BY c.id ASC";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            model.setRowCount(0);
            while (rs.next()) {
                model.addRow(new Object[] {
                        rs.getString("id"), rs.getString("nom"), rs.getString("prenom"), rs.getString("libelle"),
                        rs.getString("telPerso"), rs.getString("telPro"), rs.getString("email"),
                        rs.getString("sexe"), rs.getString("Ville"), rs.getString("Categorie")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement des contacts.");
        }
    }

    public void rechercherContactsExact(Map<String, String> criteres) {
        String baseQuery = "SELECT c.id, c.nom, c.prenom, c.libelle, c.telPerso, c.telPro, c.email, c.sexe, " +
                "v.NomVille AS Ville, cat.NomCategorie AS Categorie FROM contact c " +
                "JOIN Ville v ON c.NumVille = v.NumVille JOIN Categorie cat ON c.NumCat = cat.NumCat WHERE ";
        StringBuilder query = new StringBuilder(baseQuery);
        java.util.List<String> conditions = new java.util.ArrayList<>();

        for (Map.Entry<String, String> entry : criteres.entrySet()) {
            if (!entry.getValue().isEmpty()) {
                switch (entry.getKey()) {
                    case "Categorie": conditions.add("cat.NomCategorie = ?"); break;
                    case "Ville": conditions.add("v.NomVille = ?"); break;
                    default: conditions.add("c." + entry.getKey() + " = ?"); break;
                }
            }
        }

        if (conditions.isEmpty()) {
            loadContactsFromDatabase();
            return;
        }

        query.append(String.join(" AND ", conditions)).append(" ORDER BY c.id ASC");

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query.toString())) {

            int index = 1;
            for (String key : criteres.keySet()) {
                String value = criteres.get(key);
                if (!value.isEmpty()) stmt.setString(index++, value);
            }

            ResultSet rs = stmt.executeQuery();
            model.setRowCount(0);
            while (rs.next()) {
                model.addRow(new Object[] {
                        rs.getString("id"), rs.getString("nom"), rs.getString("prenom"), rs.getString("libelle"),
                        rs.getString("telPerso"), rs.getString("telPro"), rs.getString("email"),
                        rs.getString("sexe"), rs.getString("Ville"), rs.getString("Categorie")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors de la recherche des contacts.");
        }
    }

    public Contacts() {
        setTitle("\uD83D\uDC64 Gestion des Contacts");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] columns = {"id", "nom", "prenom", "libelle", "telPerso", "telPro", "email", "sexe", "Ville", "Categorie"};
        model = new NonEditableTableModel(columns, 0);
        table = new JTable(model);
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JScrollPane scrollPane = new JScrollPane(table);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        String[] buttonLabels = {"Ajouter", "Modifier", "Supprimer", "Rechercher", "Actualiser", "Quitter"};
        JButton[] buttons = new JButton[buttonLabels.length];

        for (int i = 0; i < buttonLabels.length; i++) {
            buttons[i] = new JButton(buttonLabels[i]);
            buttons[i].setFocusPainted(false);
            buttons[i].setFont(new Font("Segoe UI", Font.PLAIN, 14));
            buttons[i].setBackground(new Color(60, 130, 200));
            buttons[i].setForeground(Color.WHITE);
            buttons[i].setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
            buttonPanel.add(buttons[i]);
        }

        buttons[0].addActionListener(e -> new AjouterContact(model));
        buttons[1].addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                int id = Integer.parseInt(table.getValueAt(row, 0).toString());
                new ModifierContact(model, row, id);
            } else {
                JOptionPane.showMessageDialog(this, "Sélectionnez un contact à modifier.");
            }
        });
        buttons[2].addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                new SupprimerContact(this, this, table);
            } else {
                JOptionPane.showMessageDialog(this, "Sélectionnez un contact à supprimer.");
            }
        });
        buttons[3].addActionListener(e -> new RechercherContact(this, this));
        buttons[4].addActionListener(e -> loadContactsFromDatabase());
        buttons[5].addActionListener(e -> System.exit(0));

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(mainPanel);
        loadContactsFromDatabase();
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Contacts::new);
    }
}
