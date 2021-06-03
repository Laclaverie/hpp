# Projet Programmation Haute Performance
## Contributeurs :
- Julien Chavas
- Pierre Laclaverie

## Principe de la solution
Le programme Java que nous avons élaboré consiste à prendre en entrée des fichiers .csv étant dans un  même répertoire.
Les données ont toutes étés formatées au préalable de manière à ce qu'elles soient présentées de la manière suivante :
```
<person_id, person_surname, person_family_name, date_of_birth, diagnosed_ts, contaminated_by, contaminated_reason>
```
L'objectif est ici de mettre en place une solution permettant de trouver le " top 3 " des chaines de contaminations, c'est à dire celles qui ont le poids le plus important.
Le poids d'une chaine est donné par la relation suivante :
On se place à une date <strong>t fixée</strong>
- Si la personne a été contaminée depuis moins de 7 jours par rapport à cette date, son score est de 10
- Si la personne a été contaminée depuis plus de 7 jours mais moins de 14 jours, on lui attribue un score de 4
- Sinon on lui attribue un score de 0
Le poids de la chaine est donc la somme des scores de chaque personne.
Il est à noter que le poids de la chaine est <strong>variable</strong> en fonction de la date choisie.
Pour toute information supplémentaire sur le détail des exigences, veuillez consulter le sujet [ici](https://github.com/telecom-se/hpp/tree/2020-2021/project)

## Comment l'avons nous implémentée?
### Général
Nous avons implémenté une version multiThread et une version monothread de notre application. Notre objectif initial était de mettre en valeur le multithreading et le gain de temps que cela aurait apporté pour cette solution lorsque nous avons un jeu de données importantes.
### Reading

Au début nous utilisions des méthodes " basiques " avec ```getLine() et skip()```, cependant celles-ci n'étaient pas optimisées car il y avait un parcours inutile de l'ensemble des lignes. Pour remédier à cela, nous avons utilisé des scanneurs, qui ont grandement amélioré notre vitesse de lecture.
### Processing
Au début nous avions utilisé une unique liste chainée afin d'accomplir le tavail demandé de manière "naïve". Nous avons une LinkedList de linkedList de Malade. Nous avons une classe Malade qui contient toutes les informations qui sont utiles pour la suite : l'ID du Malade, la date pour laquelle il a été infectée, son pays d'origine, l'id de la personne qui l'a contaminée, l'id de son pays d'origine.
```java
LinkedList<LinkedList<Malade>> chaine
```
Cependant, utiliser uniquement des listes chaines pour ce cas est problématique car lorqu'on souhaite accéder à un élément par la commande ```chaine.get(index)```, cela va prendre un temps considérable, notamment s'il y a plus de 200 000 chaines de contaminations différentes (cas où l'on a 1 Millions d'entrées)

Un deuxième élément important à prendre en compte est le fait que dès les premiers tests qui joignaient la partie Reading et la partie Processing, nous avons observé à l'aide de VisualVm un piste d'amélioration non-négligeable : + 91% du temps, notre programme était en attente !

La totalité de chaine de traitement mettait plus de 1h pour le million de données en entrée, ce qui ne nous satisfaisait pas.

C'est pourquoi nous avons opté pour la solution suivante : toutes les opérations de complexité O(N) doivent être améliorées au possible pour se rapprocher de O(1). 
En cela, nous avons utilisé 5 HashMap :
```java
	private HashMap<Long, Integer> map; // <Id du malade,chaine associée>
	private HashMap<Integer,Long> chainCountyMap; // <id de la chaine, id du pays>
	private HashMap<Integer,LinkedList<Malade>> chainIndexLinkedList; // <index, chaine de numéro index>
	private HashMap<Integer,Long> chainScoreMap; // <index, score de la chaine de numéro index>
```
 - ``` HashMap<Long, Integer> map;```est une HashMap du type <Id du malade,chaine dans laquelle le malade se trouve>
 - ```HashMap<Integer,Long> chainCountyMap;```est une Hashmap du type <Id de la chaine, Pays de la chaine> ( voir consignes )
 - ```HashMap<Integer,Long> chainScoreMap;``` est une Hashmap du type <Id de la chaine, score de la chaine>
 - ```HashMap<Integer,LinkedList<Malade>> chainIndexLinkedList;``` est une Hashmap du type <Id de la chaine, pointeur vers la chaine en question> (nous reviendrons plus tard sur cette HashMap)
 A ce sujet, nous avons numéroté les chaines par ordre de création : La première chaine est d'id 0, la seconde, id=1,....

L'utilisation des trois premières tables de hash nous a permit de passer de plus de 1h de traitement à une vingtaine de minutes.
Cependant, nous n'étions toujours pas satisfait et nous avions encore nos 90% d'attente durant l'execution du programme.

C'est pourquoi nous avons choisi de nous attaquer directement à la méthode ```get()``` d'une liste chainée, qui ne nous satisfaisait pas du fait de sa lenteur lorsque le nombre de malade commençait à être grand.

En cela, nous avons mit en place la ```HashMap<Integer,LinkedList<Malade>> chainIndexLinkedList```. En effet, il nous suffit de récuperer l'id de la chaine, et nous avons directement accès à la chaine qui nous intéresse grâce à la valeur.
Grâce à cette amélioration, nous avons gagné un temps de calcul important : en effet, nous sommes actuellement à moins de 10 secondes de calculs.

### Writting
Nous écrivons les résultats comme demandé dans l'énoncé, dans un fichier.csv

### Au sujet des tests
Pour vérifier si nous avions des résultats cohérents, nous avons calculés manuellement la sortie espérée du programme avec un code matlab présent dans testhpp.m.
### Résultats
Nous avons en premier lieu implémenté une version multithread puis l'avons testé. Les résultats sont présentés sur cette courbe.

Nous avons ensuite adapté notre code pour produire une version monothread.... Quelle ne fût pas notre stupéfaction que de voir que la version monothread prend moins de temps à s'executer que la version multithreadée!

Plusieurs pistes :
- La création de threads est potentiellement trop coûteuse par rapport au temps de calcul global
- Nous ne faisons pas de calculs de benchmark, nous calculons un temps ( avec nanotime), un benchmark pourrait nous éclairer un peu plus
- Le jeu de données n'est pas assez grand pour nous permettre de voir une différence notable.
