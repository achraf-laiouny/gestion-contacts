import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;

public class RechercheContact extends JFrame {
    public RechercheContact(DefaultTableModel model, JTable table) {
        setTitle("Recherche de Contacts");
        setSize(500, 350);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(7, 2, 10, 10));
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        String[] labels = {"Nom", "Prénom", "Email", "Téléphone", "Ville", "Catégorie"};
        JComboBox<String>[] combos = new JComboBox[labels.length];

        for (int i = 0; i < labels.length; i++) {
            panel.add(new JLabel(labels[i]));

            // Populate each combo with unique values from the model
            DefaultComboBoxModel<String> comboModel = new DefaultComboBoxModel<>();
            comboModel.addElement("Tous");

            for (int row = 0; row < model.getRowCount(); row++) {
                String value = model.getValueAt(row, i).toString();
                if (((DefaultComboBoxModel<String>) comboModel).getIndexOf(value) == -1) {
                    comboModel.addElement(value);
                }
            }

            combos[i] = new JComboBox<>(comboModel);
            panel.add(combos[i]);
        }

        JButton searchBtn = new JButton("Rechercher");
        JButton resetBtn = new JButton("Réinitialiser");

        panel.add(searchBtn);
        panel.add(resetBtn);

        add(panel);

        searchBtn.addActionListener((ActionEvent e) -> {
            for (int i = 0; i < model.getRowCount(); i++) {
                boolean match = true;
                for (int j = 0; j < combos.length; j++) {
                    String selected = (String) combos[j].getSelectedItem();
                    if (!selected.equals("Tous") && !model.getValueAt(i, j).toString().equals(selected)) {
                        match = false;
                        break;
                    }
                }

                if (match) {
                    table.setRowSelectionInterval(i, i);
                    table.scrollRectToVisible(table.getCellRect(i, 0, true));
                    return;
                }
            }

            JOptionPane.showMessageDialog(null, "Aucun contact ne correspond à la recherche.");
        });

        resetBtn.addActionListener(e -> dispose());

        setVisible(true);
    }
}
