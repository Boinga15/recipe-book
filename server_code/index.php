<?php
    function cors() {
        // Allow from any origin
        if (isset($_SERVER['HTTP_ORIGIN'])) {
            // Decide if the origin in $_SERVER['HTTP_ORIGIN'] is one
            // you want to allow, and if so:
            header("Access-Control-Allow-Origin: {$_SERVER['HTTP_ORIGIN']}");
            header('Access-Control-Allow-Credentials: true');
            header('Access-Control-Max-Age: 86400');    // cache for 1 day
        }
        
        // Access-Control headers are received during OPTIONS requests
        if ($_SERVER['REQUEST_METHOD'] == 'OPTIONS') {
            
            if (isset($_SERVER['HTTP_ACCESS_CONTROL_REQUEST_METHOD']))
                // may also be using PUT, PATCH, HEAD etc
                header("Access-Control-Allow-Methods: GET, POST, OPTIONS");
            
            if (isset($_SERVER['HTTP_ACCESS_CONTROL_REQUEST_HEADERS']))
                header("Access-Control-Allow-Headers: {$_SERVER['HTTP_ACCESS_CONTROL_REQUEST_HEADERS']}");
        
            exit(0);
        }
    }

    function executeQuery($queryType, $arguments) {
        $query = "java -classpath \".;utilityItems\sqlite-jdbc-3.48.0.0.jar\" " . $queryType . " ";

        foreach($arguments as $argument) {
            $query = $query . "\"" . $argument . "\" ";
        }

        chdir("java"); 
        $result = shell_exec($query);
        echo $result;
    }

    $chosenAction = $_POST["action"];
    $extraInformation = array();

    $currentItem = 0;

    $cKey = "item_$currentItem";
    while (array_key_exists($cKey, $_POST)) {
        $extraInformation[] = $_POST[$cKey];
        $currentItem += 1;
    }

    executeQuery($chosenAction, $extraInformation);
?>