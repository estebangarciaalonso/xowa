== Overview ==
XOWA is an offline Wikipedia application. It can run an offline copy of Wikipedia on your computer by using any of the dumps at https://dumps.wikimedia.org. It can run as a standalone GUI application, as a Firefox addon, or as a HTTP server.

== License ==
XOWA is released under the AGPLv3 license. See LICENSE.txt for more information.

== Operating Systems ==
XOWA runs on Linux, Windows, and Mac OS X. These instructions will assume the user is on a 64-bit Linux system. However, if you're on a different system, the same instructions still apply. Simply substitute "linux_64" with the appropriate XOWA operating system name from below:
* Linux 64-bit    : "linux_64"
* Linux 32-bit    : "linux"
* Windows 64-bit  : "windows_64"
* Windows 32-bit  : "windows"
* Mac OS X 64-bit : "macosx_64"
* Mac OS X 32-bit : "macosx"

== Software Requirements ==
XOWA is written in Java and requires 1.6 or above. It has seven dependencies:

# JUnit 4.8.2 (default version with Eclipse)
# [https://download.eclipse.org/eclipse/downloads/drops4/R-4.2.1-201209141800/#SWT SWT 4.2.1]: GUI library
# [https://github.com/gnosygnu/luaj_xowa luaj_xowa.jar]: Lua library
# [https://github.com/gnosygnu/jtidy_xowa jtidy_xowa.jar]: HTML tidy library
# [https://bitbucket.org/xerial/sqlite-jdbc/downloads sqlite-jdbc-3.7.15-SNAPSHOT-2.jar]: Database library
# [https://dev.mysql.com/downloads/connector/j/ mysql-connector-java-5.1.12-bin.jar]: Database library
# [https://jdbc.postgresql.org/download.html postgresql-8.4-701.jdbc4.jar]: Database library

Note that the last two libraries are not currently used in XOWA.

== Compilation instructions (ANT command-line) ==
=== Setup the XOWA app ===
* Download the latest XOWA app package for your operating system. For example, if you're on a 64-bit Linux system, "xowa_app_linux_64_v1.9.5.1.zip".
* Unzip the XOWA app package to a directory. For the sake of simplicity, these instructions assume this directory is "/xowa/"
* Review your directories. You should have the following:
** An XOWA jar: "/xowa/xowa_linux_64.jar"
** An XOWA "/bin/any/" directory with several jar files. For example, "/xowa/bin/any/java/apache/commons-compress-1.5.jar"
** An XOWA "/bin/linux_64/" directory with an SWT jar: "/xowa/bin/linux_64/swt/swt.jar"

=== Setup the XOWA source ===
* Download the latest XOWA source archive. For example: "xowa_source_v1.9.5.1.7z"
* Unzip the source to "/xowa/dev". When you're done, you'll have a file called "/xowa/dev/build.xml" as well as others
: NOTE: if you're not on a Linux 64-bit system, overwrite the swt jar at "/xowa/dev/150_gfui/lib/swt.jar" with the copy from your "/bin/OS" directory. For example, if you're on a 64 bit Windows system, replace "/xowa/dev/150_gfui/lib/swt.jar" with "/bin/windows_64/swt/swt/jar"

=== Run the ant file ===
* Open up a console, and run "ant -buildfile build.xml -Dplat_name=linux_64"

== IDE instructions (Eclipse) ==
=== Environment ===
The '''xowa_source.7z''' was built with Eclipse Indigo. There are no OS dependencies, nor are there dependencies on Eclipse.

=== Setup ===
* Follow the steps in these two sections from above:
** Setup the XOWA app
** Setup the XOWA source
* Launch Eclipse. Choose a workbench folder of "/xowa/dev"
* If the projects don't load, do File -> Import -> Existing Projects Into Workspace
* Select all projects. Do File -> Refresh.
* Right-click on 400_xowa in the Package Explorer. Select Debug As -> Java Application. Select Xowa_main. XOWA should launch.
* Right-click on 400_xowa in the Package Explorer. Select Debug As -> JUnit Test. All tests should pass.

=== Eclipse-specific settings ===
This section documents specific project customizations that differ from the standard Eclipse defaults.

==== Project properties ====
Resource -> Text file encoding -> Other -> UTF-8

==== Preferences ====
These settings are available under Window -> Preferences

*Disable Spelling
:General -> Editors -> Text Editors -> Spelling
*Ignore Warnings
:Java -> Compiler -> Errors/Warnings
:: Annotations -> Unhandled token in '@SuppressWarnings'
:: Potential programming problems -> Serializable class without serialVersionUID
:: Generic Types -> Unnecessary generic type operation (In Eclipse Luna: "Unchecked generic type operation")
:: Generic Types -> Usage of a raw type
:: Unnecessary Code -> Unused import

==== Configuration arguments ====
*Configuration arguments
:Run -> Debug Configurations -> Arguments
::<code>--root_dir /xowa/ --show_license n --show_args n</code>
