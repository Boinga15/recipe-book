var createIngredients = [];
var createSteps = [];

var createType = 0; // 0 = New, 1 = Edit.

class Recipe {
    constructor(name, description, timeToBake, servingSize, ingredients, steps) {
        this.name = name;
        this.description = description;
        this.timeToBake = timeToBake;
        this.servingSize = servingSize;
        this.ingredients = ingredients;
        this.steps = steps;
    }

    setRecipe(name, description, timeToBake, servingSize, ingredients, steps) {
        this.name = name;
        this.description = description;
        this.timeToBake = timeToBake;
        this.servingSize = servingSize;
        this.ingredients = ingredients;
        this.steps = steps;
    }

    showRecipe() {
        document.getElementById("recipe-main").style = "display: inline;";
        document.getElementById("recipe-create").style = "display: none;";

        document.getElementById("show-recipe-name").innerHTML = this.name;
        document.getElementById("show-recipe-description").innerHTML = this.description;

        document.getElementById("show-time-to-bake").innerHTML = "Time to Bake: " + this.timeToBake;
        document.getElementById("show-serve").innerHTML = "Serves: " + this.servingSize;

        var ingrdientList = document.getElementById("show-ingredients");
        var stepList = document.getElementById("show-steps");

        ingrdientList.innerHTML = "";
        stepList.innerHTML = "";
    
        function addIngredient(ingredient) {
            var newListItem = document.createElement("li");
            newListItem.innerHTML = ingredient;

            ingrdientList.appendChild(newListItem);
        }

        function addStep(step) {
            var newListItem = document.createElement("li");
            newListItem.innerHTML = step;

            stepList.appendChild(newListItem);
            
            stepList.appendChild(document.createElement("br"));
        }

        this.ingredients.forEach(addIngredient);
        this.steps.forEach(addStep);
    }

    editRecipe() {
        createType = 1;

        document.getElementById("recipe-main").style = "display: none;";
        document.getElementById("recipe-create").style = "display: inline;";
    
        document.getElementById("recipe-name").value = this.name;
        document.getElementById("recipe-description").value = this.description;
    
        document.getElementById("bake-time").value = this.timeToBake;
        document.getElementById("serving-size").value = this.servingSize;
    
        document.getElementById("ingredient-list").innerHTML = "";
        document.getElementById("step-list").innerHTML = "";
    
        createIngredients = this.ingredients;
        createSteps = this.steps;

        createIngredientList(createIngredients);
        createStepList(createSteps);
    }
}

var selectedRecipe = new Recipe("", "", "", "", [], [])

var recipes = [
    new Recipe("Egg Fried Rice", "A simple egg fried rice that is totally legitimate.", "40 minutes", "4 people", ["Eggs", "Pre-Cooked Rice", "Spring Onions"], ["Rehet the rice.", "Add the eggs and spring onion", "Mix.", "Done."]),
    new Recipe("Glowstick Cake", "This is what got me on the wanted list. Because I fed someone this.", "15 minutes", "2 people", ["Cake", "Glowsticks"], ["Get the cake.", "Slam fifty glowsticks into it.", "Bake for fifteen more minutes.", "Serve to someone and run for the hills."]),
    new Recipe("Boiled Bread", "A warcrime against bread.", "10 minutes", "1 person", ["Bread"], ["Boil the bread.", "Congraltuations, you've now awoken the spirit of every chef on Earth who are all armed with pitchforks."]),
];

function createRecipeList(recipeList) {
    var targetDiv = document.getElementById("main-recipe-list");
    
    var searchQuery = document.getElementById("search-box").value;

    function createRecipe(recipe) {
        if (recipe.name.toLowerCase().includes(searchQuery.toLowerCase())) {
            var newButton = document.createElement("button");
            newButton.classList.add("recipe-button");
            newButton.innerHTML = recipe.name;
            
            newButton.onclick = function () {
                selectedRecipe = recipe;
                recipe.showRecipe();
            }
            
            targetDiv.appendChild(newButton);
            targetDiv.appendChild(document.createElement("br"));
            targetDiv.appendChild(document.createElement("br"));
        }
    }
    targetDiv.innerHTML = "";

    recipeList.forEach(createRecipe);
}

window.onload = function() {
    createRecipeList(recipes);
}


function reloadRecipeList() {
    createRecipeList(recipes);
}

function addRecipeInitialisation() {
    createType = 0;

    document.getElementById("recipe-main").style = "display: none;";
    document.getElementById("recipe-create").style = "display: inline;";

    document.getElementById("recipe-name").value = "";
    document.getElementById("recipe-description").value = "Add recipe description here...";

    document.getElementById("bake-time").value = "";
    document.getElementById("serving-size").value = "";

    document.getElementById("ingredient-list").innerHTML = "";
    document.getElementById("step-list").innerHTML = "";

    createIngredients = [];
    createSteps = [];
}

function createIngredientList(ingredients) {
    document.getElementById("ingredient-list").innerHTML = "";

    function addElement(ingredient) {
        var ingredientInput = document.createElement("input");
        ingredientInput.classList.add("search-box");
        ingredientInput.style = "text-align: center; font-size: 15px";
        ingredientInput.type = "text";
        ingredientInput.placeholder = "Enter ingredient here..."
        ingredientInput.value = ingredient;

        document.getElementById("ingredient-list").appendChild(ingredientInput);

        var buttonInput = document.createElement("button");
        buttonInput.innerHTML = "Remove";
        buttonInput.classList.add("remove-button");

        buttonInput.onclick = function () {
            createIngredients = [];

            var ingredientItems = document.getElementById("ingredient-list").children;
            for (var i = 0; i < ingredientItems.length; i++) {
                if (ingredientItems[i].tagName == "INPUT") {
                    createIngredients.push(ingredientItems[i].value)
                }
            }

            createIngredients.splice(createIngredients.indexOf(ingredientInput.value), 1);

            createIngredientList(createIngredients);
        }
    
        document.getElementById("ingredient-list").appendChild(buttonInput);
        document.getElementById("ingredient-list").appendChild(document.createElement("br"));
        document.getElementById("ingredient-list").appendChild(document.createElement("br"));
    }

    ingredients.forEach(addElement);
}

function addIngredient() {
    createIngredients = []

    var ingredientItems = document.getElementById("ingredient-list").children;
    for (var i = 0; i < ingredientItems.length; i++) {
        if (ingredientItems[i].tagName == "INPUT") {
            createIngredients.push(ingredientItems[i].value)
        }
    }

    createIngredients.push("");

    createIngredientList(createIngredients);
}

function createStepList(steps) {
    document.getElementById("step-list").innerHTML = "";

    function addElement(step) {
        var stepInput = document.createElement("textarea");
        stepInput.classList.add("recipe-description");
        stepInput.value = step;

        document.getElementById("step-list").appendChild(stepInput);

        var buttonInput = document.createElement("button");
        buttonInput.innerHTML = "Remove";
        buttonInput.classList.add("remove-button");

        buttonInput.onclick = function () {
            createSteps = []

            var stepItems = document.getElementById("step-list").children;
            for (var i = 0; i < stepItems.length; i++) {
                if (stepItems[i].tagName == "TEXTAREA") {
                    createSteps.push(stepItems[i].value)
                }
            }

            createSteps.splice(createSteps.indexOf(stepInput.value), 1);

            createStepList(createSteps);
        }
    
        document.getElementById("step-list").appendChild(buttonInput);
        document.getElementById("step-list").appendChild(document.createElement("br"));
        document.getElementById("step-list").appendChild(document.createElement("br"));
    }

    steps.forEach(addElement);
}

function addStep() {
    createSteps = []

    var stepItems = document.getElementById("step-list").children;
    for (var i = 0; i < stepItems.length; i++) {
        if (stepItems[i].tagName == "TEXTAREA") {
            createSteps.push(stepItems[i].value)
        }
    }

    createSteps.push("Enter step...");

    createStepList(createSteps)
}

function addRecipe() {
    var recipeName = document.getElementById("recipe-name").value;
    var recipeDescription = document.getElementById("recipe-description").value;
    var bakeTime = document.getElementById("bake-time").value;
    var servingTime = document.getElementById("serving-size").value;

    createIngredients = []

    var ingredientItems = document.getElementById("ingredient-list").children;
    for (var i = 0; i < ingredientItems.length; i++) {
        if (ingredientItems[i].tagName == "INPUT") {
            createIngredients.push(ingredientItems[i].value)
        }
    }

    createSteps = []

    var stepItems = document.getElementById("step-list").children;
    for (var i = 0; i < stepItems.length; i++) {
        if (stepItems[i].tagName == "TEXTAREA") {
            createSteps.push(stepItems[i].value)
        }
    }

    if (createType == 0) {
        recipes.push(new Recipe(recipeName, recipeDescription, bakeTime, servingTime, createIngredients, createSteps));
    } else {
        recipes[recipes.indexOf(selectedRecipe)] = new Recipe(recipeName, recipeDescription, bakeTime, servingTime, createIngredients, createSteps)
    }

    cancelCreate();
    reloadRecipeList();
}

function cancelCreate() {
    document.getElementById("recipe-main").style = "display: none;";
    document.getElementById("recipe-create").style = "display: none;";
}

function deleteRecipe() {
    recipes.splice(recipes.indexOf(selectedRecipe), 1);
    reloadRecipeList();
    cancelCreate();
}

function editRecipe() {
    selectedRecipe.editRecipe();
}