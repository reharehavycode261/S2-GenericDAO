# Projet de Gestion de Base de Données en Java

## Description
Ce projet est un système de gestion de base de données écrit en Java. Il permet de se connecter à différentes bases de données, d'exécuter des requêtes SQL, et de gérer des entités au sein d'un système. Les principaux composants incluent une interface de connexion, des objets de transfert de données (DAO), et un ensemble de classes pour interagir avec divers types de bases de données.

## Structure du Projet
- **src/database/core**: Contient les classes principales pour la gestion des connexions et transactions avec la base de données.
- **src/database/provider**: Inclut les implémentations spécifiques aux fournisseurs de bases de données comme Oracle et PostgreSQL.
- **src/test**: Conçu pour tester les fonctionnalités du système avec des exemples tels que `Student`, `Emp`, et `Plat`.

## Technologies utilisées
- Java SE
- JDBC pour la connexion à la base de données
- Support pour multiple SGBD via des fournisseurs

## Installation
1. Clonez le repository dans votre dispositif local :