import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Contacts extends JFrame{
    // Declare the JTable and the model
    private JTable table;
    private DefaultTableModel model;

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
        String[] columns = {"Nom", "Prénom", "Email", "Téléphone", "Ville", "Catégorie"};

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

        // Add button action listeners
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle adding new contact (simple input dialog for now)
                String nom = JOptionPane.showInputDialog("Entrez le Nom:");
                String prenom = JOptionPane.showInputDialog("Entrez le Prénom:");
                String email = JOptionPane.showInputDialog("Entrez l'Email:");
                String telephone = JOptionPane.showInputDialog("Entrez le Téléphone:");
                String ville = JOptionPane.showInputDialog("Entrez la Ville:");
                String categorie = JOptionPane.showInputDialog("Entrez la Catégorie:");

                // Add the contact to the table
                model.addRow(new Object[]{nom, prenom, email, telephone, ville, categorie});
            }
        });

        modifyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle modifying an existing contact (simple edit of the selected row)
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    String nom = JOptionPane.showInputDialog("Entrez le Nom:", model.getValueAt(selectedRow, 0));
                    String prenom = JOptionPane.showInputDialog("Entrez le Prénom:", model.getValueAt(selectedRow, 1));
                    String email = JOptionPane.showInputDialog("Entrez l'Email:", model.getValueAt(selectedRow, 2));
                    String telephone = JOptionPane.showInputDialog("Entrez le Téléphone:", model.getValueAt(selectedRow, 3));
                    String ville = JOptionPane.showInputDialog("Entrez la Ville:", model.getValueAt(selectedRow, 4));
                    String categorie = JOptionPane.showInputDialog("Entrez la Catégorie:", model.getValueAt(selectedRow, 5));

                    // Update the selected row with new values
                    model.setValueAt(nom, selectedRow, 0);
                    model.setValueAt(prenom, selectedRow, 1);
                    model.setValueAt(email, selectedRow, 2);
                    model.setValueAt(telephone, selectedRow, 3);
                    model.setValueAt(ville, selectedRow, 4);
                    model.setValueAt(categorie, selectedRow, 5);
                } else {
                    JOptionPane.showMessageDialog(null, "Sélectionnez une ligne à modifier.");
                }
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle deleting a selected contact
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    model.removeRow(selectedRow);
                } else {
                    JOptionPane.showMessageDialog(null, "Sélectionnez une ligne à supprimer.");
                }
            }
        });

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle search functionality
                String searchTerm = JOptionPane.showInputDialog("Entrez le terme de recherche:");
                boolean found = false;
                for (int i = 0; i < model.getRowCount(); i++) {
                    for (int j = 0; j < model.getColumnCount(); j++) {
                        if (model.getValueAt(i, j).toString().toLowerCase().contains(searchTerm.toLowerCase())) {
                            table.setRowSelectionInterval(i, i);  // Select the row containing the search term
                            found = true;
                            break;
                        }
                    }
                    if (found) break;
                }
                if (!found) {
                    JOptionPane.showMessageDialog(null, "Aucun contact trouvé pour '" + searchTerm + "'");
                }
            }
        });

        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle quitting the application
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