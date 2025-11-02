# LAB 3 DAA
## Auteurs
- Duruz Florian
- Ferreira Silva Sven
- Richard Aurélien

---
Groupe : DAA_B_12

Classe : DAA 2025-26

Date : 02/11/2025

## Réponse aux questions

### 4.1
Pour le champ remark, destiné à accueillir un texte pouvant être plus long qu’une seule ligne, quelle configuration particulière faut-il faire
dans le fichier XML pour que son comportement soit correct ? Nous pensons notamment à la possibilité de faire des retours à la ligne, 
d’activer le correcteur orthographique et de permettre au champ de prendre la taille nécessaire.

* Pour la saisie suer plusieur ligne, il faut activer "textMultiLine" dans le champs InputType.
* Pour l'auto-correcteur, il faut activer "textAutoCorrect" dans le champs InputType.
* Pour que L'EditText ajuste sa taille, il faut mettre la valeur "height" à "wrap-content" et ne pas mettre de valeur à "maxLines"

```XML
<EditText
android:id="@+id/edit_text_comments"
android:layout_width="0dp"
android:layout_marginTop="8dp"
android:layout_height="wrap_content"
android:inputType="textMultiLine|textAutoCorrect"
android:minLines="3"
android:gravity="top|start"
app:layout_constraintStart_toStartOf="parent"
app:layout_constraintEnd_toEndOf="parent"
app:layout_constraintTop_toBottomOf="@id/text_view_comments"/>
```

### 4.2
Pour afficher la date sélectionnée via le DatePicker nous pouvons utiliser un DateFormat permettant par exemple d’afficher 12 juin 1996 
à partir d’une instance de Date. Le formatage des dates peut être relativement différent en fonction des langues, la traduction des mois par exemple,
mais également des habitudes régionales différentes : la même date en anglais britannique serait 12th June 1996 et en anglais américain June 12, 1996.
Comment peut-on gérer cela au mieux ?

Pour gérer l'affichage des dates selon la langue et la région on utilise la classe "DateFormat" fournie par Android
qui va ajuster le format de la date aux conventions locales.
```Java
private val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

val cal = Calendar.getInstance()
val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
	cal.set(Calendar.YEAR, year)
	cal.set(Calendar.MONTH, month)
	cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
	editBirthdate.setText(dateFormat.format(cal.time))
}

```

### 4.3
Veuillez choisir une question en fonction de votre choix d’implémentation :
a. Si vous avez utilisé le DatePickerDialog1 du SDK. En cas de rotation de l’écran du smartphone lorsque le dialogue est ouvert, 
une exception android.view.WindowLeaked sera présente dans les logs, 
à quoi est-elle due et comment pouvons-nous la corriger ?
b. Si vous avez utilisé le MaterialDatePicker2 de la librairie Material. Est-il possible de limiter les dates sélectionnables dans le dialogue ? 
Ceci en particulier pour une date de naissance car il est peu probable d’avoir une personne née il y a plus de 110 ans ou à une date dans le futur. 
Comment pouvons-nous mettre cela en place ?