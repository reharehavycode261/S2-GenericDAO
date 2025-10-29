# Projet de gestion de base de données

Ce projet est un système de gestion de base de données utilisant Java, axé sur l'intégration avec différents SGBD (Systèmes de Gestion de Bases de Données) comme PostgreSQL. Il est conçu pour aider au développement et aux tests d'applications en fournissant une couche d'abstraction simplifiée pour les interactions avec la base de données.

## Fonctionnalités

- **CRUD**: Créez, lisez, mettez à jour et supprimez des enregistrements dans la base de données.
- **Abstraction**: Interagissez avec la base de données sans vous soucier des spécificités SQL.
- **Extensibilité**: Ajoutez facilement de nouveaux types de bases de données.

## Configuration

1. **Prérequis**: Assurez-vous d'avoir Java installé sur votre machine.
2. **Dépendances**: Le projet utilise Maven pour la gestion des dépendances. Toutes les dépendances nécessaires sont spécifiées dans le fichier `pom.xml`.
3. **Configuration de la base de données**: Mettez à jour le fichier de configuration `application.properties` avec les paramètres de connexion à votre base de données.

## Exécution

Pour exécuter le projet, utilisez votre IDE préféré ou compilez et exécutez les classes Java en utilisant Maven ou la ligne de commande. Voici un exemple de commande pour exécuter l'application :