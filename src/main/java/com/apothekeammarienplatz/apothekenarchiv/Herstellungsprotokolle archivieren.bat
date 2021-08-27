cls
@echo off
chcp 1252
REM Dieses Script soll dabei helfen, Herstellungsprotokolle zu archivieren.
REM 1. Das Programm fragt den Benutzer nach PL-Nummer des Protokolls und nach dem Datum.
REM 2. Der Benutzer legt das Protokoll in den Scanner.
REM 3. Das Bild wird gespeichert als PDF. Das PDF wird eindeutig benannt.
REM 4. Das Papierprotokoll wird geschreddert oder abgeheftet.
title Herstellungsprotokoll archivieren

set naps_scan_profile="fi-7160_grau"
set pfad_zum_archiv=C:\Users\Apothekenadmin\Documents\Qsync\Rezeptur\Dokumentation\
set naps_scan_bin=C:\Program Files (x86)\NAPS2\NAPS2.Console.exe

set exif_tool_bin=C:\Users\Apothekenadmin\Documents\Qsync\Mitarbeiter persönlich\Mandelkow\Anwendungen\Abholerarchiv\exiftool.exe



:START
REM Leere Werte für die Variablen vorgeben:
set plausi_nummer=""
set datum=""


echo[
echo Um das Programm abzubrechen bitte den Buchstaben N eingeben und mit ENTER bestätgen.
echo Um die Eingabe abzubrechen und neu zu starten bitte den Buchstaben X eingeben und mit ENTER bestätgen.

echo[
echo Bitte die Plausinummer eingeben und mit ENTER bestätigen
echo z.B. PL 2015 - 03
set /p plausi_nummer=
IF "N" == "%plausi_nummer%" (
    GOTO :END
)
IF "n" == "%plausi_nummer%" (
    GOTO :END
)
IF "x" == "%plausi_nummer%" (
    GOTO :START
)
IF "X" == "%plausi_nummer%" (
    GOTO :START
)

echo[
echo Bitte das Datum der Herstellung eingeben und mit ENTER bestätigen
echo z.B. 2021-12-31
set /p datum=








echo "Bitte das Herstellungsprotokoll %plausi_nummer% in den Scanner legen und Enter drücken!"
set /p es_geht_los=

set archiv_file_name=%pfad_zum_archiv%Herstellungsprotokolle\%plausi_nummer%_%datum%.pdf
:BEFORE_IF_SUFFIX
IF exist "%archiv_file_name%" (
    echo Die Datei existiert bereits. Es wird eine weitere Datei mit Suffix erstellt.
    IF "%archiv_file_name:~-2%"=="_1" (%archiv_file_name% = %archiv_file_name:~0,2%_2; GOTO :AFTER_IF_SUFFIX)
    IF "%archiv_file_name:~-2%"=="_2" (%archiv_file_name% = %archiv_file_name:~0,2%_3; GOTO :AFTER_IF_SUFFIX)
    IF "%archiv_file_name:~-2%"=="_3" (%archiv_file_name% = %archiv_file_name:~0,2%_4; GOTO :AFTER_IF_SUFFIX)
    IF "%archiv_file_name:~-2%"=="_4" (%archiv_file_name% = %archiv_file_name:~0,2%_5; GOTO :AFTER_IF_SUFFIX)
    IF "%archiv_file_name:~-2%"=="_5" (%archiv_file_name% = %archiv_file_name:~0,2%_6; GOTO :AFTER_IF_SUFFIX)
    IF "%archiv_file_name:~-2%"=="_6" (%archiv_file_name% = %archiv_file_name:~0,2%_7; GOTO :AFTER_IF_SUFFIX)
    IF "%archiv_file_name:~-2%"=="_7" (%archiv_file_name% = %archiv_file_name:~0,2%_8; GOTO :AFTER_IF_SUFFIX)
    IF "%archiv_file_name:~-2%"=="_8" (%archiv_file_name% = %archiv_file_name:~0,2%_9; GOTO :AFTER_IF_SUFFIX)
    IF "%archiv_file_name:~-2%"=="_9" (%archiv_file_name% = %archiv_file_name:~0,2%_10; GOTO :AFTER_IF_SUFFIX)
    IF "%archiv_file_name:~-3%"=="_10" (%archiv_file_name% = %archiv_file_name:~0,3%_11; GOTO :AFTER_IF_SUFFIX)
    IF "%archiv_file_name:~-3%"=="_11" (%archiv_file_name% = %archiv_file_name:~0,3%_12; GOTO :AFTER_IF_SUFFIX)
    IF "%archiv_file_name:~-3%"=="_12" (%archiv_file_name% = %archiv_file_name:~0,3%_13; GOTO :AFTER_IF_SUFFIX)
    IF "%archiv_file_name:~-3%"=="_13" (%archiv_file_name% = %archiv_file_name:~0,3%_14; GOTO :AFTER_IF_SUFFIX)
    REM ELSE:
    REM Wenn die Suche vorher nichts gefunden hat, gibt es wohl noch kein Suffix?
    REM Wir hängen einfach nur _1 hinten dran.
    %archiv_file_name% = %archiv_file_name%_1
)
:AFTER_IF_SUFFIX
echo Die Datei wird abgelegt als:
echo %archiv_file_name%
REM Dokument scannen:
"%naps_scan_bin%" -o "%archiv_file_name%" --enableocr --profile %naps_scan_profile% --ocrlang "deu"
gpg --output "%archiv_file_name%".sig --detach-sig "%archiv_file_name%"

REM Sprache für OCR --ocrlang "eng" mit folgender Sprache:
REM ger (B) oder deu (T) laut http://www.loc.gov/standards/iso639-2/php/code_list.php

REM Erst mal anzeigen und gucken, wies es aussieht:
REM "C:\Program Files (x86)\Adobe\Acrobat Reader DC\Reader\AcroRd32.exe"  %archiv_file_name%

REM Benutzer über den Erfolg informieren
IF exist "%archiv_file_name%" (
    echo Die Datei wurde erfolgreich erstellt. Das Papier kann geschreddert werden.
) ELSE (
    echo Scheinbar gab es einen Fehler. Die Datei wurde nicht erstellt.
)
echo[
echo[
echo[
echo[


REM Auf neuen Vorgang oder Abbruch warten
REM echo "Wollen wir noch ein Herstellungsprotokollscannen? (J/N)"
REM set /p vorgang_wiederholen=
REM IF "J" == "%vorgang_wiederholen%" (
    GOTO :START
REM )
REM IF "j" == "%vorgang_wiederholen%" (
    GOTO :START
REM )

:END
echo "Auf Wiedersehen"