<?php
  //THE ROOTDIR VARIABLE WILL BE SHOWN AT THE TOP
  //PLEASE NOTE: STRING MUST END WITH A SLASH, "/"
  $rootdir = "http://localhost/mydir/";
 
  //PASSWORD IS USED FOR UPLOADING FILES. ADVISE: CREATE A PASSWORD LONGER THAN 8 CHARS!
  $password = "pass";

  //LANGUAGE SETTINGS - CHOOSE english OR dutch OR danish
  $language = "english";

  //DO NOT EDIT ANYTHING BEHIND THIS LINE IF YOU DONT KNOW ANYTHING ABOUT PHP
  if ($language == "english")
  {
  //ENGLISH LANGUAGE
  $title 			= "[ OpenDirectory v1.0 - Created By DutchVille ]";
  $welcomemessage		= "Welcome to my Open Directory! [ <a href=index.php>home</a> ]";
  $nofiles			= "<font color=red>There are no files in this directory right now!</font>";
  $currently 			= "You are currently in directory: ";
  $filename			= "Filename:";
  $type				= "Type:";
  $size				= "Size:";
  $filedir			= "File/Directory:";  
  $filedir_file 		= "File";
  $filedir_dir			= "<<i>DIR</i>>";
  $created			= "Script Created By ";
  $sorted			= "Files are currently sorted on: ";
  $sortfilename			= "Filename";
  $sorttypefile			= "Type";
  $sortsize			= "Size";
  $sortfiledir			= "File/Directory";
  $sortasc			= "Ascending";
  $sortdesc			= "Descending";
  $levelup			= "<< BACK";
  $passnotcorrect               = "<font color=red>The password is not correct. Please try again</font>";
  $uploadfiletext		= "File: ";
  $uploadpasswordtext		= "Password: ";
  $uploadfiletext2		= "Upload File!";
  $uploadfilefinished		= "<font color=blue>The file has been uploaded!</font>";
  $uploadfilenotfinished	= "<font color=red>The file could not be uploaded. Please try again!</font>";
  $uploadlink			= "U";
  $uploadtext 			= "Upload a file to the current directory";
  $deletelink			= "D";
  $deletetext			= "Delete a file from the current directory";
  $directorylink		= "C";
  $directorytext		= "Create a new directory in the current one";
  $deletefinished		= "<font color=blue>The file has been deleted!</font>";
  $newdirectory			= "Directory Name";
  $newdirectorycreate		= "Create Directory";
  $newdirectorycreated		= "<font color=blue>The new directory has been created!</font>";
  $newdirectoryexistsalready	= "<font color=red>The directory already exists!</font>";
  }

  if ($language == "dutch")
  {
  //DUTCH LANGUAGE
  $title 			= "[ OpenDirectory v1.0 - Gemaakt Door DutchVille ]";
  $welcomemessage		= "Welkom in mijn Open Directory! [ <a href=index.php>Start</a> ]";
  $nofiles			= "<font color=red>Er zijn op dit moment geen bestanden in deze directory!</font>";
  $currently 			= "U bent op dit moment in directory: ";
  $filename			= "Bestandsnaam:";
  $type				= "Type:";
  $size				= "Grootte:";
  $filedir			= "Bestand/Directory:";  
  $filedir_file 		= "Bestand";
  $filedir_dir			= "<<i>DIR</i>>";
  $created			= "Script Gemaakt Door ";
  $sorted			= "Bestanden zijn gesorteerd op: ";
  $sortfilename			= "Bestandsnaam";
  $sorttypefile			= "Type";
  $sortsize			= "Grootte";
  $sortfiledir			= "Bestand/Directory";
  $sortasc			= "Oplopend";
  $sortdesc			= "Aflopend";
  $levelup			= "<< TERUG";
  $passnotcorrect               = "<font color=red>Het wachtwoord is niet correct. Probeer het nog eens</font>";
  $uploadfiletext		= "Bestand: ";
  $uploadpasswordtext		= "Wachtwoord: ";
  $uploadfiletext2		= "Upload Bestand!";
  $uploadfilefinished		= "<font color=blue>Het bestand is geüpload!</font>";
  $uploadfilenotfinished	= "<font color=red>Het bestand kon niet worden geüpload. Probeer het nog eens!</font>";
  $uploadlink			= "U";
  $uploadtext 			= "Upload een bestand naar de huidige directory";
  $deletelink			= "V";
  $deletetext			= "Verwijder een bestand uit de huidige directory";
  $directorylink		= "M";
  $directorytext		= "Maak een nieuwe directory aan in de huidige";
  $deletefinished		= "<font color=blue>Het bestand is verwijderd!</font>";
  $newdirectory			= "Directory Naam";
  $newdirectorycreate		= "Maak Directory";
  $newdirectorycreated		= "<font color=blue>De nieuwe directory is aangemaakt!</font>";
  $newdirectoryexistsalready	= "<font color=red>De directory bestaat reeds!</font>";
  }

  if ($language == "danish")
  {
  //DANISH LANGUAGE
  $title    = "[ OpenDirectory v1.0 - Leveret af DutchVille ]";
  $welcomemessage  = "Velkommen til Open Directory! [ <a href=index.php>home</a> ]";
  $nofiles   = "<font color=red>Der er ingen filer i denne folder!</font>";
  $currently    = "Du står i denne folder: ";
  $filename   = "Filnavn:";
  $type    = "Type:";
  $size    = "Størrelse:";
  $filedir   = "Fil/Folder:"; 
  $filedir_file   = "Fil";
  $filedir_dir   = "<<i>Folder</i>>";
  $created   = "Script lavet af ";
  $sorted   = "Filer sorteres efter: ";
  $sortfilename   = "Filnavn";
  $sorttypefile   = "Type";
  $sortsize   = "Størrelse";
  $sortfiledir   = "Fil/Folder";
  $sortasc   = "Alfabetisk";
  $sortdesc   = "Omvendt Alfabetisk";
  $levelup   = "<< Tilbage";
  $passnotcorrect               = "<font color=red>Forkert password. Prøv igen</font>";
  $uploadfiletext  = "Fil: ";
  $uploadpasswordtext  = "Password: ";
  $uploadfiletext2  = "Upload Fil!";
  $uploadfilefinished  = "<font color=blue>Filen blev uploaded!</font>";
  $uploadfilenotfinished = "<font color=red>Filen kunne ikke overføres. Prøv igen!</font>";
  $uploadlink   = "Upload";
  $uploadtext    = "Upload fil til denne folder";
  $deletelink   = "S";
  $deletetext   = "Slet fil fra denne folder";
  $directorylink  = "O";
  $directorytext  = "Opret underfolder i denne folder";
  $deletefinished  = "<font color=blue>Filen blev slettet!</font>";
  $newdirectory   = "Folder Navn";
  $newdirectorycreate  = "Opret folder";
  $newdirectorycreated  = "<font color=blue>Folder blev oprettet!</font>";
  $newdirectoryexistsalready = "<font color=red>Folder eksisterer allerede!</font>";
  }

?>
