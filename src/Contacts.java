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
                new AjouterContact(model);

            }
        });

        modifyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle modifying an existing contact (simple edit of the selected row)
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    new ModifierContact(model, selectedRow);
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
                new RechercheContact(model,table);
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