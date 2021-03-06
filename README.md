***CNC Tool Contact Time Calculator***

<span id="anchor"></span>All CNC machinists know that its very important to replace cutting tools regularly, particularly when working with hard materials such as “INCONEL 718”.

With this application, you will be able to choose required operation and set time limit for tool to be in contact with material. After tool will reach required time, machine will stop and move to selected position for tool change/check.

It is for **CNC Turning only**. Application is able to take into consideration machining diameter and RPM limit(G50) in order to adapt to constant surface speed and RPM incease with smaller diameters.

**Getting Started**

Download zip and locate ‘Jar Application’ folder. You will find executable jar file, sample cnc program, drawing jpeg, run.sh, wrun.bat files.

**Prerequisites**

To run this Application you will need:

*JavaFX 11*

(Included in this repository)

But you can also download it at:

<https://gluonhq.com/products/javafx/>

JavaOpenJDK 11+

https://www.oracle.com/java/technologies/javase-jdk15-downloads.html/

To get Application running:

*For Linux systems:*

Edit run.sh

java --module-path **/path/to/javafx/javafx-sdk-11.0.2/lib/** --add-modules javafx.controls,javafx.fxml,javafx.base,javafx.graphics -jar cnc-contact-time.jar

Replace path to your own javaFx/lib location.

Then ./run.sh via terminal

*For Windows systems:*

Edit wrun.bat

jjava --module-path **"C:\\path\\to\\javafx-sdk-11.0.2\\lib"** --add-modules javafx.controls,javafx.fxml,javafx.base,javafx.graphics -jar cnc-contact-time.jar

Replace path to your own javaFx/lib location.

Then run wrun.bat

**Built With**

\* Intellij IDE

\* JavaFX SceneBuilder

**Authors**

\* All code is written by Pavel Naumovich https://gist.github.com/PavsterUK

Big thanks to my shop floor manager Tony Dougray for some technical knowledge contribution.
