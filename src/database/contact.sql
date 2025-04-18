-- phpMyAdmin SQL Dump
-- version 5.1.0
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1
-- Généré le : ven. 18 avr. 2025 à 20:42
-- Version du serveur :  10.4.19-MariaDB
-- Version de PHP : 7.3.28

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `gestion_contacts`
--

-- --------------------------------------------------------

--
-- Structure de la table `contact`
--

CREATE TABLE `contact` (
  `id` int(11) NOT NULL,
  `nom` varchar(50) DEFAULT NULL,
  `prenom` varchar(50) DEFAULT NULL,
  `libelle` varchar(100) DEFAULT NULL,
  `telPerso` varchar(20) DEFAULT NULL,
  `telPro` varchar(20) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `sexe` varchar(10) DEFAULT NULL,
  `NumVille` int(11) DEFAULT NULL,
  `NumCat` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Déchargement des données de la table `contact`
--

INSERT INTO `contact` (`id`, `nom`, `prenom`, `libelle`, `telPerso`, `telPro`, `email`, `sexe`, `NumVille`, `NumCat`) VALUES
(1, 'Martin', 'Claire', 'Amie de lycée', '0601020304', '0145896325', 'claire.martin@example.com', 'Femme', 1, 2),
(2, 'Dupont', 'Jean', 'Collègue de travail', '0612345678', '0156478390', 'jean.dupont@example.com', 'Homme', 2, 3),
(3, 'Bernard', 'Sophie', 'Cousine', '0623456789', '0156789345', 'sophie.bernard@example.com', 'Male', 1, 2);

--
-- Index pour les tables déchargées
--

--
-- Index pour la table `contact`
--
ALTER TABLE `contact`
  ADD PRIMARY KEY (`id`),
  ADD KEY `NumVille` (`NumVille`),
  ADD KEY `NumCat` (`NumCat`);

--
-- AUTO_INCREMENT pour les tables déchargées
--

--
-- AUTO_INCREMENT pour la table `contact`
--
ALTER TABLE `contact`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=22;

--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `contact`
--
ALTER TABLE `contact`
  ADD CONSTRAINT `contact_ibfk_1` FOREIGN KEY (`NumVille`) REFERENCES `ville` (`NumVille`),
  ADD CONSTRAINT `contact_ibfk_2` FOREIGN KEY (`NumCat`) REFERENCES `categorie` (`NumCat`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
