@echo off

echo Deleting old file

%ANDROID%\platform-tools\adb.exe shell rm -r /data/local/tmp/server.dex

echo Pushing new file
%ANDROID%\platform-tools\adb.exe push server.dex /data/local/tmp/

echo Running the class in the apk
%ANDROID%\platform-tools\adb.exe shell export ANDROID=/data/local/tmp
%ANDROID%\platform-tools\adb.exe shell cd /data/local/tmp/
%ANDROID%\platform-tools\adb.exe shell dalvikvm -cp server.dex Hello


echo finished

Pause:
