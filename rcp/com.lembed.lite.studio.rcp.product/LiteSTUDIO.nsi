; Script generated by the HM NIS Edit Script Wizard.

; HM NIS Edit Wizard helper defines
!define PRODUCT_NAME "LiteSTUDIO"
!define PRODUCT_VERSION "0.1.3"
!define PRODUCT_PUBLISHER "Lembed Electronic"
!define PRODUCT_WEB_SITE "http://www.lembed.com"
!define PRODUCT_DIR_REGKEY "Software\Microsoft\Windows\CurrentVersion\App Paths\LiteSTUDIO.exe"
!define PRODUCT_UNINST_KEY "Software\Microsoft\Windows\CurrentVersion\Uninstall\${PRODUCT_NAME}"
!define PRODUCT_UNINST_ROOT_KEY "HKLM"

# Additional Includes and Plugins
!addincludedir .\scripts\NSIS_Includes
!addplugindir .\scripts\NSIS_Plugins

!include 'FileFunc.nsh'
!insertmacro Locate

Var /GLOBAL switch_overwrite
;!include 'MoveFileFolder.nsh'

SetCompressor lzma

; MUI 1.67 compatible ------
!include "MUI.nsh"

!include "target\project.nsh"

; Set the Brand Text to Empty
BrandingText " "


; MUI Settings
!define MUI_ABORTWARNING
!define MUI_ICON "icons\win_icon.ico"
!define MUI_UNICON "icons\uninstall.ico"

; Welcome page
!insertmacro MUI_PAGE_WELCOME

; Set the welcome image
;!define MUI_WELCOMEFINISHPAGE_BITMAP "icons\welcome.bmp"
;!define MUI_UNWELCOMEFINISHPAGE_BITMAP "icons\welcome.bmp"

; License page
!define MUI_LICENSEPAGE_CHECKBOX
!insertmacro MUI_PAGE_LICENSE "license.txt"
; Directory page
!insertmacro MUI_PAGE_DIRECTORY
; Instfiles page
!insertmacro MUI_PAGE_INSTFILES
; Finish page
!define MUI_FINISHPAGE_RUN "$INSTDIR\LiteSTUDIO.exe"
!insertmacro MUI_PAGE_FINISH

; Uninstaller pages
!insertmacro MUI_UNPAGE_INSTFILES

; Language files
!insertmacro MUI_LANGUAGE "English"

; Reserve files
!insertmacro MUI_RESERVEFILE_INSTALLOPTIONS

; MUI end ------
!include "zipdll.nsh"

;!define TAR "LiteSTUDIO-*.win32.x86_64.zip"
!define TARPATH "target\products\${PROJECT_FINAL_NAME}"

Name "${PRODUCT_NAME} ${PRODUCT_VERSION}"
OutFile "LiteSTUDIO.exe"
InstallDir "$PROGRAMFILES\LiteSTUDIO"
InstallDirRegKey HKLM "${PRODUCT_DIR_REGKEY}" ""
ShowInstDetails show
ShowUnInstDetails show

Section "UNZIP"
  SectionIn RO
  SetOverwrite on
  SetOutPath "$INSTDIR"
  File "${TARPATH}"
  StrCpy $switch_overwrite 0
  
  ZipDLL::extractall "$INSTDIR\${PROJECT_FINAL_NAME}" "$INSTDIR"
  Delete "$INSTDIR\${PROJECT_FINAL_NAME}"
  ;CopyFiles "$INSTDIR\LiteSTUDIO\*.*" "$INSTDIR"
  ;RMDir /r /REBOOTOK "$INSTDIR\LiteSTUDIO"
  
SectionEnd

Section "MainSection" SEC01
  SetOutPath "$INSTDIR"
  ;SetOverwrite ifnewer
  ;File "..\target\products\LiteSTUDIO-0.1.1-20171022-0759-win32.win32.x86_64.zip"
  ;ZipDLL::extractall "..\target\products\LiteSTUDIO-0.1.1-20171022-0759-win32.win32.x86_64.zip" "d:\output"
  ;nsisunz::UnzipToLog ${TAR} "..\output"
  ;File "..\out\LiteSTUDIO\LiteSTUDIO.exe"
  ;File /r "..\out\LiteSTUDIO\*.*"
  CreateDirectory "$SMPROGRAMS\LiteSTUDIO"
  CreateShortCut "$SMPROGRAMS\LiteSTUDIO\LiteSTUDIO.lnk" "$INSTDIR\LiteSTUDIO.exe"
  CreateShortCut "$DESKTOP\LiteSTUDIO.lnk" "$INSTDIR\LiteSTUDIO.exe"
SectionEnd

Section -Additionalicons
  CreateShortCut "$SMPROGRAMS\LiteSTUDIO\Uninstall.lnk" "$INSTDIR\uninst.exe"
SectionEnd

Section -Post
  WriteUninstaller "$INSTDIR\uninst.exe"
  WriteRegStr HKLM "${PRODUCT_DIR_REGKEY}" "" "$INSTDIR\LiteSTUDIO.exe"
  WriteRegStr ${PRODUCT_UNINST_ROOT_KEY} "${PRODUCT_UNINST_KEY}" "DisplayName" "$(^Name)"
  WriteRegStr ${PRODUCT_UNINST_ROOT_KEY} "${PRODUCT_UNINST_KEY}" "UninstallString" "$INSTDIR\uninst.exe"
  WriteRegStr ${PRODUCT_UNINST_ROOT_KEY} "${PRODUCT_UNINST_KEY}" "DisplayIcon" "$INSTDIR\LiteSTUDIO.exe"
  WriteRegStr ${PRODUCT_UNINST_ROOT_KEY} "${PRODUCT_UNINST_KEY}" "DisplayVersion" "${PRODUCT_VERSION}"
  WriteRegStr ${PRODUCT_UNINST_ROOT_KEY} "${PRODUCT_UNINST_KEY}" "URLInfoAbout" "${PRODUCT_WEB_SITE}"
  WriteRegStr ${PRODUCT_UNINST_ROOT_KEY} "${PRODUCT_UNINST_KEY}" "Publisher" "${PRODUCT_PUBLISHER}"
SectionEnd


Function un.onUninstSuccess
  HideWindow
  MessageBox MB_ICONINFORMATION|MB_OK "$(^Name) Successfully removed from your computer."
FunctionEnd

Function un.onInit
  MessageBox MB_ICONQUESTION|MB_YESNO|MB_DEFBUTTON2 "Are you sure you want to completely remove $(^Name), and all of its components?" IDYES +2
  InitPluginsDir
  Abort
FunctionEnd

Section Uninstall
  Delete /REBOOTOK "$INSTDIR\uninst.exe"
  Delete /REBOOTOK "$INSTDIR\LiteSTUDIO.exe"

  Delete /REBOOTOK "$SMPROGRAMS\LiteSTUDIO\Uninstall.lnk"
  Delete /REBOOTOK "$DESKTOP\LiteSTUDIO.lnk"
  Delete /REBOOTOK "$SMPROGRAMS\LiteSTUDIO\LiteSTUDIO.lnk"

  RMDir /REBOOTOK "$SMPROGRAMS\LiteSTUDIO"
  RMDir /REBOOTOK "$INSTDIR"

  DeleteRegKey ${PRODUCT_UNINST_ROOT_KEY} "${PRODUCT_UNINST_KEY}"
  DeleteRegKey HKLM "${PRODUCT_DIR_REGKEY}"
  SetAutoClose true
SectionEnd