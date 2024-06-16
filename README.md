# Rapport - MCR - Chaîne de responsabilités

## Le Donjon d'Edaryaban :crossed_swords:
> Auteurs : Arthur Junod, Edwin Häffner, Yanis Ouadahi, Esteban Lopez

## Table des matières
1. [Introduction](##Introduction)
2. [LibGDX](##LibGDX)
3. [Présentation du jeu](##Présentation-du-jeu)
    - [Listes des éléments du jeu](###Listes-des-éléments-du-jeu)
    - [HUD du jeu](###HUD-du-jeu)
4. [Comment jouer](##Comment-jouer)
    - [Touches utilisées par le jeu](###Touches-utilisées-par-le-jeu)
5. [Choix d'implémentation](##Choix-d'implémentation)
    - [Écran de jeu (Jeu et Win/Lose)](###Écran-de-jeu-(Jeu-et-Win/Lose)) 
    - [Génération aléatoire des niveaux](###-Génération-aléatoire-des-niveaux)
    - [Gestion des ennemis (IA, spawn, etc.)](###Gestion-des-ennemis-(IA,-spawn,-etc.))
    - [Gestion des objets par le joueur](###Gestion-des-objets-par-le-joueur)
6. [Chaînes de responsabilités](##Chaînes-de-responsabilités)
    - [Chaîne d'attaque](###Chaîne-d'attaque)
    - [Chaîne de requête de dégâts](###Chaîne-de-requête-de-dégâts)
    - [Contexte d'appels de nos chaînes](###Contexte-d'appels-de-nos-chaînes)
7. [Défis rencontrés](##Défis-rencontrés)
8. [Conclusion](##Conclusion)
    - [Améliorations](###Améliorations)

## Introduction
Le but de notre projet est de montrer l'utilisation du modèle de conception "Chaine de responsabilité" en le mettant en place dans un petit jeu (de type "Dungeon Crawler"). Pour effectuer cela, nous avons utilisé le framework LibGDX et donc l'entièreté de notre projet est conçu en Java.
## LibGDX

LibGDX est un framework de développement basé sur OpenGL(ES) de jeux libres sous licence Apache et multiplateforme écrit en Java. Le choix de cette librairie a été fait, car nous voulions rester sur une approche relativement proche du code, sans utiliser un game engine complet. Nous voulions également explorer un framework plus complet que JavaSwing.

Il y a aussi beaucoup de documentation et d'exemple pour utiliser LibGDX ce qui a fait pencher la balance pour le choix de ce framework.
Les projets créés avec ce framework nous ont de plus donné envie d'apprendre à l'utiliser ([Slay The Spire](https://fr.wikipedia.org/wiki/Slay_the_Spire), ...).

## Présentation du jeu
Le Donjon d'Edaryaban est un jeu d'exploration de type "Dungeon Crawler" où le joueur est piégé dans un labyrinthe souterrain généré aléatoirement et peuplé d'ennemis. L'objectif principal est de trouver la sortie du donjon.

Le temps en jeu avance à chaque déplacement ou attaque que fait le joueur (step), tous les ennemis n'effectuent des actions qu'à chaque step.

Au début d'une partie, le joueur apparaît aléatoirement dans l'une des salles du donjon. Il peut ensuite trouver de l'équipement éparpillé sur le sol, ce qui lui permettra de changer d'arme, afin d'avoir plus de dégâts ou plus de portée, ou de récupérer des équipements qui augmenteront les dégâts de ses armes ou réduiront les coûts d'utilisations en mana ou en vigueur.

On peut se déplacer dans quatre directions différentes, HAUT, BAS, GAUCHE et DROITE. Notre personnage n'attaque que dans la direction dans laquelle son dernier déplacement s'est fait, puis une fois au corps à corps d'un ennemi, nous pouvons nous déplacer dans sa direction afin de se tourner face à lui et l'attaquer (il n'y a pas de repère visuel pour savoir dans quelle direction notre personnage regarde, il faut attaquer pour savoir).

Durant toute la partie il faut gérer notre mana, vigueur et vie. La vigueur et la mana se régénèrent avec le temps, mais il n'y a aucun moyen de faire remonter sa vie. Nous avons une plus grande quantité de mana que de vigueur, mais elle se régénère moins vite que cette dernière.

### Listes des éléments du jeu

| Arme            | Dégâts | Portée | Cooldown (nb steps d'attente) |    Coût    |                        Sprite                        |
| --------------- |:------:|:------:|:-----------------------------:|:----------:|:----------------------------------------------------:|
| Poings          |   1    |   1    |               0               | 5 vigueur  | ![griffe](https://hackmd.io/_uploads/r150ztnBC.png)  |
| Épée            |   3    |   3    |               0               | 10 vigueur |  ![epee](https://hackmd.io/_uploads/HkBrEtnB0.png)   |
| Gourdin         |   5    |   1    |               1               | 20 vigueur | ![gourdin](https://hackmd.io/_uploads/B1FBVthSA.png) |
| Sceptre magique |   3    |   10   |               1               |  30 mana   | ![sceptre](https://hackmd.io/_uploads/BJd84KhHC.png) |

| Item              | Description                                            |                         Sprite                         |
| ----------------- | ------------------------------------------------------ |:------------------------------------------------------:|
| Anneau de dégâts  | Double les dégâts de toutes les attaques               | ![damagerin](https://hackmd.io/_uploads/Sykv4t3SR.png) |
| Anneau de vigueur | Divise par 2 le coût en vigueur des attaques physiques | ![virgorrin](https://hackmd.io/_uploads/r1fDEt2H0.png) |
| Anneau de mana    | Divise par 2 le coût en mana des attaques magiques     |  ![manarin](https://hackmd.io/_uploads/S1vDVt3SC.png)  |
| Echelle           | Nous fait gagner la partie                             |   ![echelle](https://hackmd.io/_uploads/HJEmBF2H0.png) |

| Entité | Description                                            |                       Sprite                        |
| ------ | ------------------------------------------------------ |:---------------------------------------------------:|
| Joueur | Le personnage que nous contrôlons                      | ![player](https://hackmd.io/_uploads/SyA7BF2BA.png) |
| Ennemi | L'ennemi de base du jeu, il attaque avec l'arme poings | ![enemy](https://hackmd.io/_uploads/r1Z4rF2SR.png)  |

Tous les sprites utilisés ont été pris sur Itch.io : 

- Murs, échelle, joueur et ennemis : https://pixel-poem.itch.io/dungeon-assetpuck
- Anneaux : https://mr-pixelz.itch.io/16x16-pixel-magic-items
- Armes : https://powered-by-decaf.itch.io/simple-pixel-art-weapons-16x16

### HUD du jeu

![infohud](https://hackmd.io/_uploads/ry5necnSC.png)

Dans notre, jeu nous avons trois informations essentielles : 
1. Les informations des ressources du joueur, dans l'ordre : 
    - La mana (Bleu)
    - La vigueur (Jaune)
    - La vie (Rouge)
2. L'arme équipée actuellement
3. Les objets présents dans l'inventaire du joueur

## Comment jouer

Téléchargez le .jar du jeu sur [GitHub](https://github.com/Neroil/Chaine_Responsabilite/releases/tag/1.0). 

Vous pouvez le lancer en cliquant sur le fichier .jar ou bien en utilisant ceci dans le fichier racine du téléchargement du jeu : 
```bash!
java -jar ./desktop-1.0.jar
```

Il est nécessaire d'avoir java d'installé sur votre machine.

### Touches utilisées par le jeu
- **Flèche du clavier** - Déplacement du joueur
- **Shift Gauche** - Accélérer les déplacements du joueur
- **R** - Reset du jeu
- **Y** - Attaquer

## Choix d'implémentation

Le diagramme UML du projet est situé en annexe.

Vu que libGDX génère le projet, le code personnel de notre application se trouve dans `core/src/mcr/gdx/dungeon/`

### Écran de jeu (Jeu et Win/Lose)

L'écran principal du jeu est composé des éléments suivants et se comporte ainsi :

- La caméra est centrée sur le personnage que le joueur manipule, et suit donc chaque déplacement de celui-ci
- Le personnage peut se déplacer selon les 4 points cardinaux, NORD, SUD, EST et OUEST
- Des ennemis (tous du même type) sont disséminés sur le niveau et se déplacent également
- Des armes ou objets que le joueur peut ramasser sont présents sur le sol

Il y a également deux écrans supplémentaires, pour la victoire et la défaite, qui affiche simplement un message de victoire/défaite et comment relancer le jeu. On meurt si on se trouve sans vie et on gagne si on trouve la sortie.

### Génération aléatoire des niveaux

Nous avons implémenté un système de génération procédurale de niveaux simple pour ce jeu, un élément clé du genre "dungeon crawler" pour rendre chaque niveau aléatoire et imprévisible.

Le processus commence par la création d'une grille de cases remplie de murs. Des pièces rectangulaires sont ensuite générées aléatoirement dans cette grille, en respectant des contraintes de taille minimale et maximale. Ces pièces sont ensuite creusées en supprimant les cases de mur correspondantes dans la grille des murs.

Pour rendre le donjon navigable, des couloirs sont créés pour relier les pièces entre elles dans une façon semi-aléatoire pour rendre le donjon plus intéressant. 

Les salles et les couloirs sont ensuite sauvegardés dans une liste, ce qui permet de placer les ennemis et les objets uniquement dans les zones accessibles du niveau. Enfin, les murs restants qui sont adjacents aux parties jouables sont décorés pour donner un aspect visuel cohérent au donjon.

### Gestion des ennemis (IA, spawn, etc.)

Tous les ennemis savent où le joueur se trouve à tout moment. À chaque *game step*, l'ennemi peut choisir d'effectuer une action, se déplacer ou attaquer.

**Déplacement**
- Si l'ennemi est proche du joueur, il va se déplacer vers lui. C'est une simple distance définie donc il n'y a aucun système de pathing. Il va simplement se déplacer en ligne droite, même si un mur peut le bloquer.
- Si le joueur n'est pas assez proche, l'ennemi va uniquement se déplacer aléatoirement sur la map.

**Attaque**
- Si l'ennemi est sur une même coordonnée x ou y que le joueur et qu'il est assez proche pour effectuer son attaque, il le fait. Sinon, il essaie de se déplacer. Il utilise le même système que le joueur pour effectuer des attaques, c'est-à-dire qu'il a également un risque (défini à une chance sur 5) de manquer son attaque.
- Tous les ennemis ont 10 points de vie et il en apparait un certain nombre (défini à 5) dans le niveau. Il n'y a pas de système de *respawn*, donc si on a nettoyé le donjon, tant mieux pour nous !

### Gestion des objets par le joueur

Comme dit précédemment, le joueur peut ramasser des objets disséminés sur la carte. Leurs différents effets sont décrits auparavant dans le rapport.

À chaque step du jeu, on vérifie si le joueur se trouve sur une case comportant un objet, si c'est le cas, il le ramasse automatiquement. L'objet ramassé va ensuite faire une action qui va dépendre de son type : 

- Si c'est un objet de type "Arme", il va remplacer l'arme que le joueur a actuellement en sa possession, l'arme remplacée est ensuite supprimée du jeu.
- Si c'est un objet de type "Objet", il va se ranger, selon son type spécialisé (attaque ou défense) dans l'inventaire du joueur. Il n'y a pas encore d'objet défensifs, mais l'implémentation est déjà disponible. Il n'y a pas non plus de limite d'objets que peut avoir le joueur.
- Si c'est un objet de type "Echelle", il va appeler la fonction de fin de niveau du joueur. Dans l'état actuel de notre jeu, terminer le niveau va aussi appeler la fonction de fin de jeu qui met fin à la partie.

## Chaînes de responsabilités
Les chaînes de responsabilités sont simples à mettre en place quand nous devons faire un système qui gère une requête que l'on doit traiter ou non suivant la nature de celle-ci. Elles sont également intéressantes, car elles nous permettent de les modifier dynamiquement.

Dans notre code, nous avons mis en place une classe abstraite *Handler* et l'interface *Request* qui nous permettent de mettre en place la logique générale de nos chaînes de responsabilité.

Nous avons deux chaînes distinctes dans notre jeu, la chaîne d'attaque et la chaîne de requête de dégâts, qui sont appelées l'une après l'autre.

Les handlers disposent d'une fonction `setSuccessor()` qui leur permet de chaîne la construction de notre chaîne et que nous avons surtout utilisée pour la création de notre chaîne d'attaque.

Nos chaînes retournent un *boolean* qui l'on peut utiliser suivant la chaîne pour avoir une information sur l'état de celle-ci. Nous avons défini que si nous arrivons à la fin de nos chaînes, celles-ci retournent *true*.

À noter que l'interface *Request* ne sert qu'à manipuler tous les types de requête différent avec la classe abstraite *Handler* et qu'il existe deux spécialisations de *Handler* (une par chaîne) qui servent à cast l'interface *Request* en un type de requête associé à la chaîne actuelle afin que l'on puisse accéder aux attributs de cette requête.

| Handlers                                              | Requests                                              |
|:-----------------------------------------------------:|:-----------------------------------------------------:|
| ![Handlers](https://hackmd.io/_uploads/B1kaL5hH0.png) | ![Requests](https://hackmd.io/_uploads/H1yxPq2HA.png) |

### Chaîne d'attaque
Cette chaîne est appelée à chaque fois que nous voulons faire une attaque avec notre arme et sert à savoir si notre attaque va réussir ou échouer. Elle est également recréée dynamiquement à chaque fois que nous ramassons une arme ou un item, car sa composition va changer suivant l'équipement de notre personnage.

Elle utilise l'objet *AttackRequest* afin de passer les informations de l'attaque tout son long.

Cette *AttackRequest* a comme informations
- Les dégâts de l'attaque
- Le coût de l'attaque
- Le type d'attaque (physique ou magique)
- Le temps de récupération de l'arme utilisée
- La dernière attaque lancée avec l'arme actuelle
- Le temps en jeu du lancement de l'attaque
- Le joueur qui a lancé l'attaque

L'arme équipée de notre personnage change donc les informations de base de cette *AttackRequest*, car nous construisons cette requête par rapport à celle-ci. Ensuite, les items viennent modifier certaines parties de la requête (ici, le coût et les dégâts) avant que l'on fasse des vérifications sur les informations de la requête.

Tous les handlers composant cette chaîne héritent de *AttackHandler* qui est une spécialisation de *Handler*.

| Handler               | Description                                                                                        |
| --------------------- | -------------------------------------------------------------------------------------------------- |
| CooldownHandler       | Vérifie si notre arme est en temps de récupération ou non                                          |
| HitChanceHandler      | Vérifie si nous passons le "jet de dés" à chaque attaque qui peut la faire échouer aléatoirement   |
| ManaHandler           | Vérifie si nous avons assez de mana pour lancer l'attaque et si oui baisse la mana du joueur       |
| VigorHandler          | Vérifie si nous avons assez de vigueur pour lancer l'attaque et si oui baisse la vigueur du joueur |
| CostModifierHandler   | Modifie le coût de l'attaque contenu dans *AttackRequest*                                          |
| DamageModifierHandler | Modifie les dégâts de l'attaque contenus dans *AttackRequest*                                      |

À savoir que *ManaHandler* et *VigorHandler* sont tous les deux des enfants de la classe abstraite *RessourceHandler* qui met en place la logique générale de la vérification des ressources du joueur. 

```java!
attackChain = new CooldownHandler();
Handler chaining = attackChain.setSuccessor(new HitChanceHandler());
for(ItemTile i : attackItems){
    chaining = chaining.setSuccessor(i.handler());
}
chaining.setSuccessor(weapon.handler());
```

Notre chaîne est donc construite de la façon suivante :
1. On commence par ajouter le *CooldownHandler* pour faire échouer, au plus tôt, notre attaque si l'arme est en temps de récupération.
2. Puis, on ajoute le *HitChanceHandler* qui nous permet de faire le "jet de dés" (encore une fois, on veut faire échouer l'attaque au plus tôt pour éviter les passages dans des handlers inutiles).
3. On ajoute, s'il y en a, les handlers des items, ce qui nous permet de changer les informations de notre *AttackRequest* avant d'aller plus loin.
4. On finit par mettre le *RessourceHandler* correspondant à l'arme.

Comme chaîne sert à savoir si l'attaque a réussi ou échoué, nous utilisons le *boolean* de retour pour avoir cette information. Si celui-ci est égal à *false*, on considère l'attaque ratée et on s'arrête là pour l'action d'attaque, cependant s'il est égal à *true*, nous allons créer une chaine de requête pour la chaîne de requête de dégât qui utilisera les dégâts de notre *AttackRequest*.

![AttackChain](https://hackmd.io/_uploads/SJ-Jq9nSC.png)

### Chaîne de requête de dégâts

![CallChainofChain](https://hackmd.io/_uploads/S1hEo53r0.png)

Cette chaîne est appelée après la chaîne précédente quand l'attaque est considérée comme réussie et elle nous permet de gérer l'application des dégâts sur les potentielles cibles que l'on peut trouver dans la portée de notre attaque. Elle se crée dans chaque entité (joueur ou ennemis) et ne change pas dynamiquement.

Elle utilise l'objet *DamageRequest* pour garder ses informations tout son long.

*DamageRequest* contient :
- Les dégâts à appliquer
- La liste des positions que l'on attaque avec notre arme (calculées en dehors de la chaîne)
- La liste de toutes les entités du jeu
- La liste des cibles de notre attaque (vide au début, remplie dans la chaîne)

Tous les handlers composant cette chaîne héritent de *DamageHandler* qui est une spécialisation de *Handler*.

| Handler       | Description                                                                                                               |
| ------------- | ------------------------------------------------------------------------------------------------------------------------- |
| TargetHandler | Parcours la liste des positions attaquées puis la liste des entités afin de trouver les cibles touchées par notre attaque |
| HitHandler    | Applique les dégâts à nos cibles, s'il y en a, en appelant leur fonction de dégâts                                        |

Il faut donc que les deux handlers soient appelés dans l'ordre données suivant : *TargetHandler* puis *HitHandler*.

![DamageChain](https://hackmd.io/_uploads/HJPCqchBC.png)

### Contexte d'appels de nos chaînes

Comme mentionné plus haut, ces chaînes sont appelées pour gérer des attaques, mais différents types d'entités peuvent attaquer :

Quand le joueur attaque, toutes les fonctionnalités décrites au-dessus sont utilisées, mais si un ennemi attaque, il va utiliser les mêmes handlers que notre joueur.

La différence est que l'ennemi ne va qu'utiliser le *HitChanceHandler* dans sa chaîne d'attaque, il ne va donc construire sa requête qu'avec les informations de son arme, car il veut juste en récupérer les dégâts.

Par contre, il fera appel à l'entièreté de la chaîne de requête de dégâts afin d'appliquer les dégâts de ses poings au joueur.

## Défis rencontrés 

Étant donné que nous n'avions jamais réalisé de projet de ce type auparavant, le premier défi a été de répartir les tâches. Comme personne ne maîtrisait LibGDX, nous avons commencé par nous familiariser individuellement avec ce framework. 

Donc beaucoup de temps a été "perdu" à cause de cela, ce qui nous a limités dans ce que nous pouvions faire dans notre jeu. 

Comprendre comment le rendering fonctionne sur LibGDX notamment en utilisant des sprites batch et des couches pour le visuel, était un peu complexe. De même avec la gestion de la caméra. Nous avons encore le problème des lignes verticales et horizontales lorsqu'on redimensionne la fenêtre du jeu par exemple. 

## Conclusion

Nous avons trouvé l'utilisation des chaînes de responsabilités intuitive pour les cas auxquels nous les avons appliquées. Ce modèle semble être très flexible dans son utilisation, ce qui le rend d'autant plus simple à mettre en œuvre dans différents cas. La flexibilité du modèle vient du fait que la définition de la requête et de son contenu est laissée libre. Nous choisissons les différents types de handler et ils peuvent gérer ou non une requête dans une chaîne, etc.  Par ailleurs, l'utilisation de LibGDX était aussi intéressante, car nous n'avions pas d'expérience avec a priori. Cela nous a obligés à apprendre plusieurs mécaniques que le framework met en place, tels que la gestion des sprites, comme expliqué plus haut. 

Mettre en commun le modèle et LibGDX était simple, puisque celui-ci se base sur Java, nous savions comment implémenter ce modèle. En plus, le choix du projet nous a permis d'approcher pour la première fois la programmation d'un jeu vidéo et l'utilisation d'un framework basé sur OpenGL.

### Améliorations

- Rajouter un handler que l'on pourrait ajouter dynamiquement, suivant l'arme, dans la chaîne de requête de dégâts, qui gérerait le calcul des positions attaquées. De cette manière, une arme pourrait retourner ce handler et indiquer plus facilement si elle n'attaque pas en ligne droite.
- Créer un écran-titree, des menus d'options, et intégrer de la musique et des effets sonores pour enrichir l'expérience de jeu. Concevoir un tileset personnalisé pour éviter de devoir utiliser les "placeholders" actuels. 
- Diversifier les types de salles générées et ajouter des éléments interactifs tels que des pièges pour augmenter la complexité et l'intérêt des niveaux.
- Optimiser le déplacement des ennemis en utilisant des algorithmes de recherche de chemin plus performants que simplement se diriger en ligne droite vers le joueur, comme l'algorithme A*.
- Mettre en place un menu d'inventaire permettant au joueur de visualiser et de sélectionner ses armes et objets, sans avoir à les supprimer pour en changer.
- Ajouter différents types d'ennemis qui n'ont pas tous la même arme et donc un comportement différent vis-à-vis de la gestion de l'attaque (distance, mana restante, ...).
- Mettre en place un système qui compte combien de fois, on a trouvé la sortie et ainsi donner la possibilité de gagner si on a survécu à *n* étages du donjon.
- Ajouter une chaîne de défense qui pourrait être appelée depuis la chaîne de requête de dégâts, ainsi, si nous possédons des objets défensifs, nous pourrions réduire les dégâts subis ou les annuler totalement.
