# 4 Questions complémentaires
## 4.1 
Pour obtenir le comportement souhaité il faut ajouter plusieurs attributs au champ dans le XML :
- `android:inputType="textMultiLine | textAutoCorrect"` : afin de rendre le champ multi-ligne et active l'auto correction
- `android:gravity="top|start"` : pour que le texte commence en haut à gauche du champ
- `android:minLines="3"` : définit le nombre de ligne min par défaut. Ici 3 pour match avec le visuel donné

## 4.2 
Le format des dates va dépendre de la langue/région (la locale).
Il est donc possible d'utiliser `DateFormat` pour formatter une `Date()` dans la locale souhaitée.


### TBDF quand le code est écrit

## 4.3 

