# Nom du Projet

## Description
Ce projet est une application de gestion de base de données utilisant Java. Il intègre plusieurs composants tels que la connexion à la base de données, la manipulation des données et l'exécution de requêtes SQL. Le projet est structuré pour prendre en charge plusieurs fournisseurs de base de données comme Oracle et PostgreSQL.

## Structure du Projet
- `src/database/core/`: Contient les classes principales pour la gestion des connexions et des opérations sur la base de données.
- `src/database/provider/`: Contient les classes spécifiques à chaque fournisseur de base de données.
- `src/test/`: Contient les classes de test pour vérifier le bon fonctionnement des différentes parties de l'application.

## Installation
1. Clonez le repository sur votre machine locale.
2. Assurez-vous d'avoir Java installé sur votre système.
3. Configurez votre IDE préféré (par exemple, IntelliJ, Eclipse) pour utiliser le JDK approprié.
4. Configurez les paramètres de connexion à votre base de données dans le fichier `Config.java`.

## Utilisation
- Exécutez les classes de test dans le dossier `src/test/` pour vérifier la connectivité avec la base de données et le fonctionnement des opérations CRUD.
- Modifiez les classes ou ajoutez-en de nouvelles selon vos besoins pour étendre la fonctionnalité du projet.

## Contributeurs
- L'équipe de développement de Vydata

## Licence
Ce projet est sous licence MIT. Voir le fichier `LICENSE` pour plus de détails.