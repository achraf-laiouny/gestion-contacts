import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;
/**
 * La classe `Contacts` représente la fenêtre principale de l'application
 * de gestion des contacts. Elle affiche une liste de contacts provenant
 * d'une base de données et permet d'effectuer des opérations telles que
 * l'ajout, la modification, la suppression et la recherche de contacts.
 * Elle hérite de `JFrame`, ce qui en fait une fenêtre graphique.
 */
public class Contacts extends JFrame {
    private JTable table; // Tableau Swing pour afficher les contacts.
    private NonEditableTableModel model; // Modèle de table personnalisé pour rendre les cellules non éditables.

    /**
     * Classe interne statique pour définir un modèle de table où aucune cellule n'est éditable.
     */
    private static class NonEditableTableModel extends DefaultTableModel {
        public NonEditableTableModel(Object[] columnNames, int rowCount) {
            super(columnNames, rowCount);
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return false; // Override la méthode pour retourner toujours false, rendant ainsi les cellules non éditables.
        }
    }

    /**
     * Charge les contacts depuis la base de données et les affiche dans le tableau.
     */
    public void loadContactsFromDatabase() {
        String url = "jdbc:mysql://localhost:3306/gestion_contacts?useSSL=false&serverTimezone=UTC"; // URL de connexion à la base de données MySQL.
        String user = "root"; // Nom d'utilisateur de la base de données.
        String password = ""; // Mot de passe de la base de données.

        // Bloc try-with-resources pour assurer la fermeture automatique de la connexion, du PreparedStatement et du ResultSet.
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement("SELECT c.id, c.nom, c.prenom, c.libelle, c.telPerso, c.telPro, c.email, c.sexe, v.NomVille AS Ville, cat.NomCategorie AS Categorie FROM contact c JOIN Ville v ON c.NumVille = v.NumVille JOIN Categorie cat ON c.NumCat = cat.NumCat ORDER BY c.id ASC");
             ResultSet rs = stmt.executeQuery()) {

            model.setRowCount(0); // Supprime toutes les lignes existantes du modèle de table avant de charger de nouvelles données.
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
                model.addRow(row); // Ajoute une nouvelle ligne au modèle de table avec les données du contact récupérées de la base de données.
            }
        } catch (Exception e) {
            e.printStackTrace(); // Affiche l'erreur dans la console pour le débogage.
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement des contacts."); // Affiche un message d'erreur à l'utilisateur.
        }
    }

    /**
     * Recherche les contacts dans la base de données en fonction des critères fournis.
     * @param criteres Une map contenant les critères de recherche (nom du champ -> valeur).
     */
    public void rechercherContacts(Map<String, String> criteres) {
        String url = "jdbc:mysql://localhost:3306/gestion_contacts?useSSL=false&serverTimezone=UTC"; // URL de connexion à la base de données MySQL.
        String user = "root"; // Nom d'utilisateur de la base de données.
        String password = ""; // Mot de passe de la base de données.
        StringBuilder query = new StringBuilder("SELECT c.id, c.nom, c.prenom, c.libelle, c.telPerso, c.telPro, c.email, c.sexe, v.NomVille AS Ville, cat.NomCategorie AS Categorie FROM contact c JOIN Ville v ON c.NumVille = v.NumVille JOIN Categorie cat ON c.NumCat = cat.NumCat WHERE "); // Début de la requête SQL.
        java.util.List<String> conditions = new java.util.ArrayList<>(); // Liste pour stocker les conditions WHERE.

        // Itère sur les entrées de la map des critères de recherche.
        for (Map.Entry<String, String> entry : criteres.entrySet()) {
            String cle = entry.getKey(); // Nom du champ (clé).
            String valeur = entry.getValue(); // Valeur du critère (valeur).
            if (!valeur.isEmpty()) {
                // Construit la condition WHERE en fonction du nom du champ.
                if (cle.equals("Ville")) {
                    conditions.add("v.NomVille LIKE ?");
                } else if (cle.equals("Categorie")) {
                    conditions.add("cat.NomCategorie LIKE ?");
                } else {
                    conditions.add("c." + cle + " LIKE ?");
                }
            }
        }

        // Si aucune condition de recherche n'est spécifiée, recharge tous les contacts.
        if (conditions.isEmpty()) {
            loadContactsFromDatabase();
            return;
        }

        query.append(String.join(" AND ", conditions)); // Ajoute les conditions WHERE à la requête SQL.
        query.append(" ORDER BY c.id ASC"); // Ajoute la clause ORDER BY pour trier par ID.

        // Bloc try-with-resources pour exécuter la requête de recherche.
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(query.toString())) {

            int parameterIndex = 1; // Index pour les paramètres de la requête préparée.
            // Définit les valeurs des paramètres '?' dans la requête préparée.
            for (String valeur : criteres.values()) {
                if (!valeur.isEmpty()) {
                    stmt.setString(parameterIndex++, "%" + valeur + "%"); // Utilise LIKE '%valeur%' pour une recherche partielle.
                }
            }

            ResultSet rs = stmt.executeQuery(); // Exécute la requête SQL.
            model.setRowCount(0); // Supprime les lignes existantes du modèle de table.

            // Parcours les résultats de la recherche et les ajoute au modèle de table.
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
            e.printStackTrace(); // Affiche l'erreur dans la console.
            JOptionPane.showMessageDialog(this, "Erreur lors de la recherche des contacts."); // Affiche un message d'erreur.
        }
    }

    /**
     * Constructeur de la classe `Contacts`. Initialise la fenêtre principale,
     * configure son apparence et ajoute les composants.
     */
    public Contacts() {
        setTitle("Gestion des Contacts"); // Définit le titre de la fenêtre.
        setSize(1000, 600); // Définit la taille initiale de la fenêtre.
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Définit l'opération de fermeture par défaut (fermer l'application).
        setLocationRelativeTo(null); // Centre la fenêtre à l'écran.

        JPanel panel = new JPanel(new BorderLayout()); // Crée un panneau principal avec une mise en page BorderLayout.

        String[] columns = {"id", "nom", "prenom", "libelle", "telPerso", "telPro", "email", "sexe", "Ville", "Categorie"}; // Définition des noms de colonnes du tableau.
        model = new NonEditableTableModel(columns, 0); // Crée une instance du modèle de table personnalisé.
        table = new JTable(model); // Crée le tableau Swing en utilisant le modèle.
        JScrollPane scrollPane = new JScrollPane(table); // Ajoute une barre de défilement au tableau si nécessaire.
        panel.add(scrollPane, BorderLayout.CENTER); // Ajoute le tableau (avec sa barre de défilement) au centre du panneau principal.

        JPanel buttonPanel = new JPanel(new FlowLayout()); // Crée un panneau pour les boutons avec une mise en page FlowLayout.

        JButton addButton = new JButton("Ajouter"); // Crée le bouton "Ajouter".
        JButton modifyButton = new JButton("Modifier"); // Crée le bouton "Modifier".
        JButton deleteButton = new JButton("Supprimer"); // Crée le bouton "Supprimer".
        JButton searchButton = new JButton("Rechercher"); // Crée le bouton "Rechercher".
        JButton quitButton = new JButton("Quitter"); // Crée le bouton "Quitter".
        JButton restartButton = new JButton("Restart"); // Crée le bouton "Restart" pour recharger les contacts.

        loadContactsFromDatabase(); // Charge les contacts depuis la base de données au démarrage de l'application.

        addButton.addActionListener(e -> new AjouterContact(model)); // Ajoute un ActionListener pour afficher la fenêtre d'ajout.

        modifyButton.addActionListener(e -> { // Ajoute un ActionListener pour afficher la fenêtre de modification.
            int selectedRow = table.getSelectedRow(); // Récupère l'index de la ligne sélectionnée dans le tableau.
            if (selectedRow != -1) { // Vérifie si une ligne est sélectionnée.
                int contactId = Integer.parseInt(table.getValueAt(selectedRow, 0).toString()); // Récupère l'ID du contact à modifier.
                System.out.println("Contacts - ID du contact sélectionné pour modification : " + contactId); // Ajout pour débogage
                new ModifierContact(model, selectedRow, contactId); // Crée et affiche la fenêtre de modification.
            } else {
                JOptionPane.showMessageDialog(null, "Sélectionnez une ligne à modifier."); // Affiche un message si aucune ligne n'est sélectionnée.
            }
        });

        deleteButton.addActionListener(e -> { // Ajoute un ActionListener pour afficher la fenêtre de suppression.
            int selectedRow = table.getSelectedRow(); // Récupère l'index de la ligne sélectionnée.
            if (selectedRow != -1) { // Vérifie si une ligne est sélectionnée.
                new SupprimerContact(Contacts.this, Contacts.this, table); // Crée et affiche la fenêtre de suppression.
            } else {
                JOptionPane.showMessageDialog(null, "Sélectionnez un contact à supprimer."); // Affiche un message si aucune ligne n'est sélectionnée.
            }
        });

        searchButton.addActionListener(e -> new RechercherContact(Contacts.this, Contacts.this)); // Ajoute un ActionListener pour afficher la fenêtre de recherche.

        quitButton.addActionListener(e -> System.exit(0)); // Ajoute un ActionListener pour quitter l'application.

        restartButton.addActionListener(e -> loadContactsFromDatabase()); // Ajoute un ActionListener pour recharger les contacts.

        buttonPanel.add(addButton); // Ajoute le bouton "Ajouter" au panneau des boutons.
        buttonPanel.add(modifyButton); // Ajoute le bouton "Modifier" au panneau des boutons.
        buttonPanel.add(deleteButton); // Ajoute le bouton "Supprimer" au panneau des boutons.
        buttonPanel.add(searchButton); // Ajoute le bouton "Rechercher" au panneau des boutons.
        buttonPanel.add(restartButton); // Ajoute le bouton "Restart" au panneau des boutons.
        buttonPanel.add(quitButton); // Ajoute le bouton "Quitter" au panneau des boutons.

        panel.add(buttonPanel, BorderLayout.SOUTH); // Ajoute le panneau des boutons au bas du panneau principal.
        add(panel); // Ajoute le panneau principal à la fenêtre.
        setVisible(true); // Rend la fenêtre visible.
    }

    /**
     * Méthode principale pour lancer l'application.
     * @param args Les arguments de la ligne de commande (non utilisés ici).
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Contacts::new); // Assure que l'interface graphique est créée sur le thread d'événements Swing.
    }
}