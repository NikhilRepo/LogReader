# LogReader
-Please note that this app only runs for logs with 3 attributes. It does not run for Application Server Logs with 5 Attributes.
-Steps to follow :
   1. Please edit runLogger.bat file to contain the expected path to Log and the logfile name.
   2. Then you could double click it and it will run maven commands and also the the application.
-Given the time, it was not possible to break the module into different submodules.
 But, if given a chance, they could always be broken into :
   1. Database Connection Module
   2. LogReader Module
   3. Database Handler Module
