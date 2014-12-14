@echo off

echo Deleting old file

%ANDROID%\platform-tools\adb.exe shell rm -r /data/local/tmp/InjectionService.apk
%ANDROID%\platform-tools\adb.exe shell rm -r /data/local/tmp/dalvik-cache

echo Pushing new file
%ANDROID%\platform-tools\adb.exe push InjectionService.apk /data/local/tmp

echo Running the class in the apk
%ANDROID%\platform-tools\adb.exe shell mkdir /data/local/tmp/dalvik-cache
%ANDROID%\platform-tools\adb.exe shell ANDROID_DATA=/data/local/tmp dalvikvm -cp /data/local/tmp/InjectionService.apk com.example.injectionservice.InnowavesServer

Pause:
