call mvn clean install -Dskiptests
call mvn dependency:copy-dependencies -DoutputDirectory=".\target"
java -cp ".\target\LogReader-1.0.0.jar;.\target\gson-2.8.9.jar;.\target\hsqldb-2.2.9.jar" com.logreader.main.LogReaderMain "Path to Logfile.txt"