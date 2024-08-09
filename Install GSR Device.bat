@ECHO OFF
TITLE GSR Driver Installation
ECHO Please plug in the GSR device now . . .
Pause
CD %~dp0\Drivers\CH341SER\CH341SER\
SETUP.exe

