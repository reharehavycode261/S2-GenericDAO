# Projet Java de gestion de base de données

## Description
Ce projet a pour but de proposer une architecture simple pour la gestion et la manipulation de bases de données. Il comporte des classes permettant de se connecter à une base de données, d'exécuter des requêtes, et de traiter les résultats obtenus.

## Structure du projet
- `src/database/core/`: Contient les classes de base pour la connexion aux bases de données et la gestion des transactions.
- `src/database/provider/`: Inclut les classes spécifiques aux différents fournisseurs de BD (comme Oracle).
- `src/test/`: Contient des exemples de tests et d'utilisation des classes de manipulation de données.

## Prérequis
- Java 8 ou version ultérieure
- Maven pour la gestion des dépendances (optionnel, si applicable)

## Installation
1. Clonez le repository dans votre machine locale.
2. Importez-le dans votre IDE préféré en tant que projet Java.
3. Configurez votre base de données en modifiant les paramètres dans les classes appropriées du package `provider`.

## Utilisation
- Exécutez les classes de test dans le package `src/test/` pour voir des exemples d'utilisation.

## Contribuer
1. Forkez le projet
2. Créez une branche pour votre fonctionnalité (`git checkout -b feature/NouvelleFonctionnalité`)
3. Commitez vos modifications (`git commit -m 'Ajout de NouvelleFonctionnalité'`)
4. Pushez vers la branche (`git push origin feature/NouvelleFonctionnalité`)
5. Ouvrez une Pull Request

## Auteur
- @vydata

## Licence
Ce projet est sous licence MIT.