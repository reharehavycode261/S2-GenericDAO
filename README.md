# Nom du Projet

## Description
Ce projet est conçu pour démontrer l'utilisation d'une connexion et d'opérations de base de données en Java. Il présente une structure de base pour un DAO (Data Access Object) et comprend des exemples d'utilisation pour se connecter à différentes bases de données, notamment PostgreSQL et Oracle.

## Structure du Projet
- **src/database/core/**: Ce répertoire contient les classes principales pour les connexions et opérations de base de données, telles que `DBConnection`, `Database`, et `GenericDAO`.
- **src/database/provider/**: Ici, vous trouverez les implémentations spécifiques aux fournisseurs de base de données comme PostgreSQL et Oracle.
- **src/test/**: Contient des classes de test qui démontrent comment utiliser les fonctionnalités fournies par les classes de base de données.

## Prérequis
- Java 11 ou supérieur
- Un IDE tel qu'Eclipse ou IntelliJ IDEA
- Accès à une instance de PostgreSQL ou Oracle pour tester la connexion à la base de données

## Installation
1. Clonez ce dépôt dans votre environnement local.