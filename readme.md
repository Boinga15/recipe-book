A simple recipe book app that uses HTML, CSS, and Javascript as the frontend, while using PHP and Java as the backend. Also uses SQLite for the database.

The app will, once completed, allow users to login to accounts through a secure method (validation will be handled on the backend), and then view recipes they've saved to their list, as well as a shopping list they can customize at any time.

The demonsrtation, as of now, will simply use a local host to run the server. In its current state, it is not meant to be run on an external server yet.

To run tests on the backend, you can run the Java file Store.java, Get.java, and Delete.java using:<br>
java -classpath ".;sqlite-jdbc-3.48.0.0.jar" Store <i>ARGUMENTS</i><br>
java -classpath ".;sqlite-jdbc-3.48.0.0.jar" Get <i>ARGUMENTS</i><br>
java -classpath ".;sqlite-jdbc-3.48.0.0.jar" Delete <i>ARGUMENTS</i>