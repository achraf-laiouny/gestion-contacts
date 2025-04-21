import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Fenêtre de confirmation pour la suppression d'un contact sélectionné.
 */
public class SupprimerContact extends JFrame {

    private int contactIdToDelete = -1; // ID du contact à supprimer, initialisé à -1.
    private Contacts mainFrame; // Référence à la fenêtre principale pour recharger les données.
    private JTable contactTable; // Référence au tableau de contacts pour obtenir la sélection.

    /**
     * Constructeur de la fenêtre de suppression.
     * @param parent La fenêtre parente.
     * @param mainFrame Instance de la fenêtre principale `Contacts`.
     * @param contactTable Le tableau de contacts affiché.
     */
    public SupprimerContact(JFrame parent, Contacts mainFrame, JTable contactTable) {
        setTitle("Supprimer un Contact"); // Titre de la fenêtre.
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Fermer uniquement cette fenêtre.
        this.mainFrame = mainFrame; // Stockage de la référence à la fenêtre principale.
        this.contactTable = contactTable; // Stockage de la référence au tableau de contacts.

        JPanel contentPane = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10)); // Panneau avec FlowLayout centré.
        contentPane.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15)); // Bordure autour du panneau.

        JLabel messageLabel = new JLabel("Êtes-vous sûr de vouloir supprimer le contact sélectionné ?"); // Message de confirmation.
        JButton confirmerButton = new JButton("Confirmer"); // Bouton pour confirmer la suppression.
        JButton annulerButton = new JButton("Annuler"); // Bouton pour annuler la suppression.

        confirmerButton.addActionListener(e -> supprimerContact()); // Action lors du clic sur "Confirmer".

        annulerButton.addActionListener(e -> dispose()); // Action lors du clic sur "Annuler" (ferme la fenêtre).

        contentPane.add(messageLabel); // Ajout du message au panneau.
        contentPane.add(confirmerButton); // Ajout du bouton "Confirmer" au panneau.
        contentPane.add(annulerButton); // Ajout du bouton "Annuler" au panneau.

        add(contentPane); // Ajout du panneau de contenu à la fenêtre.
        pack(); // Ajuste la taille de la fenêtre à ses composants.
        setLocationRelativeTo(parent); // Centre la fenêtre par rapport à la fenêtre parente.
        setVisible(true); // Rend la fenêtre visible.
    }

    /**
     * Méthode pour supprimer le contact sélectionné de la base de données.
     */
    private void supprimerContact() {
        int selectedRow = contactTable.getSelectedRow(); // Récupère l'index de la ligne sélectionnée.

        if (selectedRow == -1) { // Si aucune ligne n'est sélectionnée.
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un contact dans le tableau.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return; // Quitte la méthode.
        }

        try {
            contactIdToDelete = Integer.parseInt(contactTable.getValueAt(selectedRow, 0).toString()); // Récupère l'ID du contact.
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) { // En cas d'erreur de format ou d'index.
            JOptionPane.showMessageDialog(this, "Erreur lors de la récupération de l'ID.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return; // Quitte la méthode.
        }

        String url = "jdbc:mysql://localhost:3306/gestion_contacts?useSSL=false&serverTimezone=UTC"; // URL de la base de données.
        String user = "root"; // Nom d'utilisateur de la base de données.
        String password = ""; // Mot de passe de la base de données.
        String deleteQuery = "DELETE FROM contact WHERE id = ?"; // Requête SQL pour supprimer un contact par ID.

        try (Connection conn = DriverManager.getConnection(url, user, password); // Établit la connexion à la base de données.
             PreparedStatement pstmt = conn.prepareStatement(deleteQuery)) { // Prépare la requête SQL.

            pstmt.setInt(1, contactIdToDelete); // Définit la valeur du paramètre ID dans la requête.
            int affectedRows = pstmt.executeUpdate(); // Exécute la requête de suppression.

            if (affectedRows > 0) { // Si la suppression a réussi.
                JOptionPane.showMessageDialog(this, "Contact supprimé.", "Succès", JOptionPane.INFORMATION_MESSAGE);
                mainFrame.loadContactsFromDatabase(); // Recharge les données dans la fenêtre principale.
            } else { // Si aucune ligne n'a été supprimée.
                JOptionPane.showMessageDialog(this, "Erreur lors de la suppression.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException e) { // Gère les erreurs liées à la base de données.
            e.printStackTrace(); // Affiche l'erreur dans la console.
            JOptionPane.showMessageDialog(this, "Erreur de base de données.", "Erreur", JOptionPane.ERROR_MESSAGE);
        } finally {
            dispose(); // Ferme la fenêtre de suppression.
        }
    }
}