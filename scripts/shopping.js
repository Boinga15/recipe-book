var shoppingList = ["Bread", "Glowsticks", "Sprinkles"];

function generateShoppingList() {
    var shoppingListRef = document.getElementById("shopping-list");
    shoppingListRef.innerHTML = "";

    function createItem(item) {
        var shoppingInput = document.createElement("input");
        shoppingInput.classList.add("shopping-entry");
        shoppingInput.type = "text";
        shoppingInput.placeholder = "Add Item...";
        shoppingInput.value = item;

        shoppingListRef.appendChild(shoppingInput);

        var removeButton = document.createElement("button");
        removeButton.classList.add("shopping-delete");
        removeButton.innerHTML = "Remove";
        removeButton.onclick = function () {
            shoppingList = [];
            var shoppingListItems = document.getElementById("shopping-list").children;
        
            for(var i = 0; i < shoppingListItems.length; i++) {
                if (shoppingListItems[i].tagName == "INPUT") {
                    shoppingList.push(shoppingListItems[i].value)
                }
            }

            shoppingList.splice(shoppingList.indexOf(shoppingInput.value), 1);

            generateShoppingList();
        }

        shoppingListRef.appendChild(removeButton);
        shoppingListRef.appendChild(document.createElement("br"));
    }

    shoppingList.forEach(createItem);
}

window.onload = function() {
    generateShoppingList();
}


function addItem() {
    shoppingList = [];
    var shoppingListItems = document.getElementById("shopping-list").children;

    for(var i = 0; i < shoppingListItems.length; i++) {
        if (shoppingListItems[i].tagName == "INPUT") {
            shoppingList.push(shoppingListItems[i].value)
        }
    }

    shoppingList.push("");

    generateShoppingList();
}