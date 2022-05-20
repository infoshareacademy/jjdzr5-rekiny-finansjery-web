function clearForm(form_name){
    var inputs = document.forms[form_name].getElementsByTagName("input");
    for(let input of inputs){
        switch (input.type) {
            case "text":
            case "date":
            case "number":
                input.value = "";
                break;
            case "checkbox":
                input.checked = false;
                break;
        }
    }
}
function clearFormWithoutSearchBar(){
    let searchElement = document.getElementById("search_phrase");
    if(searchElement != null){
        let searchPhrase = searchElement.value;
        clearForm("filtration_settings_form");
        searchElement.value = searchPhrase;
    }
    else{
        clearForm("filtration_settings_form");
    }
}
function sendRequestForFiltrationPreferences(){
    var preferencesName = document.getElementById("filtration_preferences").value;
    var xmlHttp = new XMLHttpRequest();
    xmlHttp.onreadystatechange = function() {
        if (xmlHttp.readyState == 4 && xmlHttp.status == 200){
            loadFiltrationPreferences(xmlHttp.responseText);
        }
    }
    xmlHttp.open("GET", "/filtration_preferences/"+preferencesName, true); // true for asynchronous
    xmlHttp.send(null);
}

function loadFiltrationPreferences(response){
    clearFormWithoutSearchBar();

    var preferences = JSON.parse(response);
    document.getElementById("effectiveDateMin").value = preferences.effectiveDateMin;
    document.getElementById("effectiveDateMax").value = preferences.effectiveDateMax;
    document.getElementById("tradingDateMin").value = preferences.tradingDateMin;
    document.getElementById("tradingDateMax").value = preferences.tradingDateMax;
    document.getElementById("bidPriceMin").value = preferences.bidPriceMin;
    document.getElementById("bidPriceMax").value = preferences.bidPriceMax;
    document.getElementById("askPriceMin").value = preferences.askPriceMin;
    document.getElementById("askPriceMax").value = preferences.askPriceMax;
    for(const currency of preferences.currency){
        document.getElementById("currency_"+currency).checked = true;
    }
}
function fillSavingPreferencesForm(){
    var inputs = document.forms["saving_preference_form"].getElementsByTagName("input");
    for(const input of inputs){
        if(input.id === "" || input.id === "preference_name"){
            continue;
        }
        if(input.type === "checkbox"){
            input.checked = document.getElementById("currency_"+input.id.split("_")[1]).checked;
        }
        else{
            input.value = document.getElementById(input.name).value;
        }
    }
}

