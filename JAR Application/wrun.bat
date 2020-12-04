if not defined in_subprocess (cmd /k set in_subprocess=y ^& %0 %*) & exit )


java --module-path C:\Users\P\Desktop\javafx-sdk-11.0.2\lib --add-modules javafx.controls,javafx.fxml,javafx.base,javafx.graphics -jar cnc-contact-time.jar