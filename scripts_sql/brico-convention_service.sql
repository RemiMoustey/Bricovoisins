-- phpMyAdmin SQL Dump
-- version 4.8.5
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1:3306
-- Généré le :  mer. 19 fév. 2020 à 17:53
-- Version du serveur :  5.7.26
-- Version de PHP :  7.2.18

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données :  `brico-convention_service`
--

-- --------------------------------------------------------

--
-- Structure de la table `convention`
--

DROP TABLE IF EXISTS `convention`;
CREATE TABLE IF NOT EXISTS `convention` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `sender_id` int(11) NOT NULL,
  `recipient_id` int(11) NOT NULL,
  `first_name_sender` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `last_name_sender` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `first_name_recipient` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `last_name_recipient` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `date_convention` date NOT NULL,
  `date_beginning` date DEFAULT NULL,
  `date_end_convention` date DEFAULT NULL,
  `beginning_hour` time NOT NULL,
  `time_intervention` time NOT NULL,
  `place` text COLLATE utf8_unicode_ci NOT NULL,
  `phone_number_helped` text COLLATE utf8_unicode_ci,
  `message` text COLLATE utf8_unicode_ci NOT NULL,
  `is_validated_by_recipient` tinyint(1) NOT NULL DEFAULT '0',
  `is_ended_by_sender` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=63 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Déchargement des données de la table `convention`
--

INSERT INTO `convention` (`id`, `sender_id`, `recipient_id`, `first_name_sender`, `last_name_sender`, `first_name_recipient`, `last_name_recipient`, `date_convention`, `date_beginning`, `date_end_convention`, `beginning_hour`, `time_intervention`, `place`, `phone_number_helped`, `message`, `is_validated_by_recipient`, `is_ended_by_sender`) VALUES
(49, 63, 62, 'Jean', 'J.', 'Rémi', 'Moustey', '2020-02-19', '2020-02-01', NULL, '03:00:00', '02:15:00', 'hjmsdf', '', 'etsrgsg', 1, 1);

-- --------------------------------------------------------

--
-- Structure de la table `hibernate_sequence`
--

DROP TABLE IF EXISTS `hibernate_sequence`;
CREATE TABLE IF NOT EXISTS `hibernate_sequence` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Déchargement des données de la table `hibernate_sequence`
--

INSERT INTO `hibernate_sequence` (`next_val`) VALUES
(63);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
