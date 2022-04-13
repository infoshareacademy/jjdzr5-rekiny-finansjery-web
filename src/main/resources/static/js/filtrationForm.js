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