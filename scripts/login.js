function login() {

}

function signup() {
    var username = document.getElementById("signup-username").value;
    var password = document.getElementById("signup-password").value;
    var confirmPassword = document.getElementById("signup-password-confirmation").value;

     $.ajax({
        beforeSend: function(request) {
            request.setRequestHeader("Access-Control-Allow-Origin", "*")
        },
        url: 'http://localhost:8000',
        type: 'POST',
        crossDomain: true,
        data: { action: "Store", item_0: "CreateUser", item_1: username, item_2: password, item_3: confirmPassword },
        success : function(response) {
            
            switch (Math.round(response)) {
                case -1:
                    document.getElementById("signup-error").innerHTML = "ERROR: User already exists.";
                    break;
                case -2:
                    document.getElementById("signup-error").innerHTML = "ERROR: Password does not match password confirmation.";
                    break;
                case 1:
                    break;
                default:
                    console.log(response);
                    document.getElementById("signup-error").innerHTML = "ERROR: " + response;
                    break;
            }
        }
     })
}