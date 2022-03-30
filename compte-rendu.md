# Maxime SOUDANT | Groupe 7 | GL1

## Compte-rendu numéro 2 du projet de GL

Après avoir réalisé l’analyse du projet junit4 en binôme, voici la seconde partie du projet consistant à modifier le code du projet afin d’améliorer ce dernier. Voici donc une liste des modifications effectuées avec les raisons de ces dernières.


### Code smells

Passage à java SE1.7 afin de régler des code smells (Il est possible que la version de java utilisé lors de votre pull soit toujours la version SE1.6, il faut alors la modifier en 1.7 ou supérieur afin que de nombreuses modifications ne soit pas compter comme des erreurs pars java)

Fichier JUnit/src/main/java/runner/manipulation/Ordering.java
L40 :  Il n’est pas nécessaire de mettre Description dans les deuxième <> car il est déduit de c qu’il y a entre les première <>

List<Description> shuffled = new arrayList<Description>(descriptions); → List<Description> shuffled = new ArrayList<>(descriptions);

Junit/src/main/javajunitextentionsActiveTestSuite.java
L46 : Suppression d’un commentaire inutile et bloquant la lisibilité du code

JUnit/src/main/java/junit/extentionss/TestSetup.java
L33 et L40 : Les codes smells présents dans cette partie du code n’en sont en réalité pas, cela est expliqué en commentaire. Cela montre bien que SonarQube n’a pas la science infuse et se trompe également. Effectivement, habituellement cela aurait été un code smells car la fonction est vide, ici c’est un cas particulier et sonarqube ne peut pas le deviner.

JUnit/src/main/java/junit/runner/Version.java
L13 : return “4.13.3-SNAPSHOT”; → final String ID = “4.13.3-SNAPSHOT”; return ID;
Ici, il est plus logique de faire un final car ce n’est pas une valeur modifiable. Cela est bien sûr contestable mais SonarQube explique que c’est la meilleure façon de le faire.
L16 : Ici, c’est un affichage direct un systeme.out.println(), cependant, sonarqube préconise de ne pas afficher en console mais de logger l’information plus tôt. Nous aurions donc dû avoir quelque chose comme loger.log(id()); au lieu de syssteme.out.println(id()). Cependant la création d’une classe logger est trop importante en terme de travail et peu importante pour l’amélioration du projet à mon goût. Nous avons donc décidé d’en parler mais de ne pas le modifier.

JUnit/src/main/java/junit/textui/TestRunner.java
L61 : static public void run (...){...} → public static void run(...){...} Inversion des mots public et static afin de respecter la convention.
L75 : Même chose que la précédente
L84 : Même chose que la précédente
L130 : Ajout de return dans le catch car sinon rien n’est fait, et est donc inutile
L143 : le Systeme.err.println pourrait être remplacé (ou on pourrait ajouter) par un logger, j’ai décidé de souligner cela mais de ne pas faire la modification pour la même raison que précédemment.

JUnit/src/main/java/framework/Assert.java
L9 : Ici, le code est déclaré comme déprécié, il ne faudra donc pas oublier de le supprimer une fois que nous somme sûr qu’il n’est plus utilisé nul part. La modification n’a pas été faite pour éviter de faire bugger le programme.
L119 : if(!(Math.abs(expected - actual) <= delta)){..} → if(Math.abs(expected - actuel) > delta){...}. Ici, ils inversent le résultat de >=, faire directement < est donc plus propre et lisible.
L141 : Même chose que la précédente

JUnit/src/main/java/framework/ComparaisonFaillure.java
L12 : private String fExpected → private final String fExpected
Ici, la valeur de fExpected lui est donnée une fois dans le constructeur puis n’est plus changé, elle ne possède pas de setter et uniquement un getter, nous pouvons donc le mettre en final.
L13 : private String fActual → private final String fActual
Même justification que la précédente.

JUnit/src/main/java/framework/TestResult.java
L25 : fFailures = new ArrayList<TestFailure>(); → fFailures = new ArrayList<>();
Ici, nous pouvons enlever ce qu’il y a dans <> car cela est automatiquement induit par la création de fFailures plus haut.
L26 : Même chose que la précédente
L27 : Même chose que la précédente
L72 : Même chose que la précédente

JUnit/src/main/java/org/junit/AssumptionViolatedException.java
L14 : La classe extends une classe du même nom, il faut donc lui modifier son nom de classe avec, par exemple, un Jr à la fin, afin de préciser que c’est la classe “junior/enfant” de la classe étendue. Le refactoring de cette ligne en AssumptionViolatedExceptionJr à modifié de nombreux fichier avec la fonction refactor de eclipse permettant de réaliser tout les changement d’un seul coup. Cependant, cela pourrait avoir créer quelques erreurs.


JUnit/src/main/java/org/junit/ComparaisonFaillure.java
L12 : private String fExpected → private final String fExpected
Ici, la valeur de fExpected lui est donnée une fois dans le constructeur puis n’est plus changé, elle ne possède pas de setter et uniquement un getter, nous pouvons donc le mettre en final.
L13 : private String fActual → private final String fActual
Même justification que la précédente.

=> Ce sont les mêmes justifications que la partie ComparaisonFaillure.java de la partie précédente car comme dit dans l’analyse, cela fait partie du code dupliqué. Cela est valable pour différents fichiers que nous ne reverrons donc pas.

JUnit/src/main/java/org/junit/experimental/ParameterSignature.java
L18 : Map<class<?>, class<?> map = new HashMap<class<?>, class<?>> (); → 	
Map<class<?>, class<?> map = new HashMap<> ();
Suppression de ce qu’il y a dans les seconds <> car cela est induit par les premiers <>
L37 : public static ArrayList<ParameterSignature> signatures(Method method){...} →
public static List<ParameterSignature> signatures(Method method){...}
Ici, nous remplaçons ArrayList par List car le retour doit être issu d’une interface comme List car c’est “le type par défaut".

JUnit/src/main/java/org/junit/experimental/theories/Theories.java
L152 : remplacement dans un if de !(paramTypes.length == 0) par paramTypes.length != 0. En effet, cela est plus lisible et logique que de tester une égalité puis d’inverser le resultat.

JUnit/src/main/java/org/junit/experimental/categories/Categories.java
L151 : Il ne faut pas oublier de supprimer le code déprécié défini par les développeurs, cependant, je n’ai pas supprimer le code afin de ne pas avoir de bug en cas d’utilisation de ce code dans le projet.

JUnit/src/main/java/org/junit/rules/ErrorCollector.java
L96 : catch(Throwable e){...} → catch(Exception e){...}
Ici, nous remplacons Throwable par Exception car Throwable est la super class des erreurs, et cela pourrait créer des erreurs.

JUnit/src/main/java/org/junit/rules/TemporaryFolders.java
L314 : if(!tryDelete(){ if(assureDeletion){...}} → if(!tryDelete() && assureDeletion){...}
Ici, nous avons merge les deux if pour plus de lisibilité et une meilleur performance.

JUnit/src/main/java/org/junit/runners/RuleContainer.java
L48 : return a<b?1:(a==b?0:-1); → if (a<b){return 1;} return a==b ? 0 : -1;
Cela est bien plus lisible en décomposant le tout.


=> Il reste encore de nombreux codes smells à corriger, cependant, nous en avons déjà corrigé beaucoup et décidons de passer à d’autres parties du code également intéressantes à modifier. De plus, nous remarquons que Dans la majorité des codes smells restant, nous avons déjà traité des cas similaires dans les codes smells traité. Nous pouvons également voir dans les cas traités que ce sont souvent les mêmes choses qui reviennent.


### Code déprécié

En ce qui concerne le code déprécié, nous en avons parlé dans les codes smells. Nous réalisons cette partie dédiée afin de rappeler qu’il ne faut pas oublier, à terme, de supprimer les codes dépréciés (déclarer dépréciés par les développeurs. Nous n'avons pas pris la liberté de supprimer ce genre de code car cette opération doit se faire uniquement une fois que nous sommes sûrs que le code n’est plus appelé nul part. De plus, nous trouvons que l’avoir précisé est suffisant et permet de ne pas risquer de bug supplémentaire, sachant que la suppression de ce genre de code ne demande pas une expertise exceptionnelle… 

Nous pouvons également parler de code déprécié de Java, c'est-à-dire, l’utilisation de code déprécié provenant de bibliothèque java. Nous avons essayé de faire la chasse à l’utilisation de code déprécié mais nous n’en avons pas trouvé. Le code utilisé et déprécié venant de bibliothèque java est important car le code risque d'être, à terme, supprimé. De plus, si la documentation des bibliothèques contenant ce code est bien faite, il est en général facile de réaliser les modifications. 


### Bugs

Junit/src/main/java/junit/extentionsActiveTestSuite.java
L60 : Ajout de Thread.currentThread().interupt() car le catch ne faisait rien et ne servait donc à rien.

JUnit/src/main/java/org/junit/experimental/max/MaxHistory.java
L39 : Il y a deux bugs potentiels sur cette ligne. Tout d’abord, la suppression se fait par par file.delete(). Cela fonctionne mais pourrait être modifié, en modifiant la signature de la fonction avec en paramètre le path de fichier et non le fichier directement. Ainsi la suppression pourrait se faire directement par la bibliothèque java.io.File avec File.delete(path);. Cela reste discutable et nous avons décidé que cela n'était pas réellement un bug.
Le second “bug” que nous ne trouvons pas être réellement un bug, est la non utilisation du retour de file.delete() qui retourne un boolean. Il faudrait utiliser ce retour, surtout si le retour est un false car cela veut dire que le fichier n’as pas était supprimé. Ici, afin de ne pas faire planter le programme, nous ne faisons pas un return ou quelque chose comme ça, nous avons décidé de simplement alerter en console de la non suppression. Ainsi le bug est corrigé mais n’engendre pas d'autre problème.



JUnit/src/main/java/org/junit/experimental/ParallelComputer.java
L45 : Ajout de Thread.currentThread().interupt() car le catch ne faisait rien et ne servait donc à rien, hormis logger l’erreur avec e.printStackTrace.

JUnit/src/main/java/org/junit/internal/requests/MemoizingRequest.java
L11 : private volatile Runner runner -> private Runner runner
Ici, nous avons supprimé volatile qui peut causer des bugs. En effet, une variable volatile signifie qu’elle est volatile, or, l’objet ne l’est pas et ce n’est pas vraiment le but pour une variable comme celle la. Cela pourrait causer des problèmes en cas où plusieurs choses s'exécutent en même temps car les threads pourraient ne pas voir les modifications de cette variable.

JUnit/src/main/java/org/junit/internal/runners/JUnit38ClassRunner.java
L75 : private volatile Test test → private Test test
Même justification qu'au-dessus.

JUnit/src/main/java/org/junit/internal/runners/statements/FailOnTimeout.java
L171 : Nous avons choisis ce bug détecté par sonarqube car ici, il ne s'agit pas d’un bug, la classe qui appelle cette partie du code va ici recevoir l'exception et relevé cette dernière qui va elle, appelé Thread.currentThread().interupt(). Cela est précisé en commentaire par le développeur et sonarqube ne peut donc pas le deviner.

=> Nous n’avons pas corrigé tous les bugs présents dans le projet, cependant, nous pouvons dire qu’il y a réellement peu de bug présent dans le code. De plus, pour la majorité des bugs catégorisé de bug par sonarqube sont discutable, il s'agit souvent pas de réel bug ou encore de choses pouvant créer des bugs dans de rares cas. Certains s’apparentent même plus à des codes smells que des bugs. Pour finir, comme pour les codes smells, nous pouvons dire que les bugs restant à traiter sont, pour la plupart, des bugs de même origine que ceux déjà traité, et les bugs résolus ont également souvent la même origine.


### Tests

JUnit/src/main/java/org/junit/ComparaisonFailure.java
Les fonctions getActual et getExpected ne sont pas testé, nous avons donc créé le fichier JUnit/src/test/java/org/junit/ComparaisonFailureTest.java qui teste donc ces deux classes.

JUnit/src/main/java/org/junit/runner/JUnitCore.java
La fonction getVersion() n'était pas testé et l’est désormais dans JUnit/src/test/java/org/junit/runner/JUnitCoreTest.java.
JUnit/src/main/java/junit/runner/Version.java ne possèdais pas de test sur ca fonction id(), elle est désormais testé dans JUnit/src/test/java/junit/runner/VersionTest.java.

=> Nous avons ici réalisé quelques tests qui n’étaient pas présents dans le projet, afin de montrer que tout n’est pas testé à 100% et voir comment ajouter des tests simplement. Cependant, nous n’en avons que très peu pour plusieurs raisons. Tout d’abord, le projet est très bien testé (plus de 85% du code est couvert par des tests). Cela est rendu d’autant plus vrai que parfois, sonarqube trouve des lignes non testé qui correspondent simplement à un simple crochet fermant, ligne ne pouvant donc pas être testé car elle ne constitue pas vraiment du code. Secondement, beaucoup de tests non fait portent sur des lignes appelant simplement des méthodes super, chose qui ne se teste normalement pas (bien que tout le monde ne soit pas d’accord sur ce point), ou encore des méthodes de classe abstraites qui sont bien plus compliqué a tester. Pour finir, en ce qui concerne le test du code du projet, cela est un peu particulier dans le cas de ce projet. En effet, il s’agit de JUNIT4 qui est lui-même un outil de test, il ne peut donc pas réellement être utilisé pour se tester lui-même, hors, sonarqube regarde ce genre de tests pour définir la couverture de test. Certaines choses ne sont donc tout simplement pas testables, ou encore testé différemment de part cette spécificité, et sonarqube ne parvient donc pas nécessairement à détecter tous les tests.
