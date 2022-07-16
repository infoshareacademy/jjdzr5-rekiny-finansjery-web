let passwordInput = document.querySelector('#password');
let poor = document.querySelector('#poor');
let weak = document.querySelector('#weak');
let strong = document.querySelector('#strong');
let lowercaseRegExp = /[a-z]/;
let uppercaseRegExp = /[A-Z]/;
let numbersRegExp = /(?=.*?[0-9])/;;
let specialRegExp = /(?=.*?[#?!@$%^&*-])/;

passwordInput.oninput = function (){
    let passwordValue = passwordInput.value;
    let passwordLength = passwordValue.length;
    let lowercasePassword= passwordValue.match(lowercaseRegExp);
    let uppercasePassword= passwordValue.match(uppercaseRegExp);
    let numbersPassword= passwordValue.match(numbersRegExp);
    let specialPassword= passwordValue.match(specialRegExp);

    if (passwordLength < 8) {
        poor.style.width = "0%";
        weak.style.width = "0%";
        strong.style.width = "0%";
    }
    if (passwordLength >= 8 && (lowercasePassword || uppercasePassword || numbersPassword || specialPassword)) {
        poor.style.width = "30%";
        // poor.textContent = "#{signup.password}"
        weak.style.width = "0%";
        strong.style.width = "0%";
    }
    if (passwordLength >= 8 && lowercasePassword && uppercasePassword && (numbersPassword || specialPassword)) {
        poor.style.width = "0%";
        weak.style.width = "60%";
        // weak.textContent = "Medium"
        strong.style.width = "0%";
    }
    if (passwordLength >= 8 && lowercasePassword && uppercasePassword && numbersPassword && specialPassword) {
        poor.style.width = "0%";
        weak.style.width = "0%";
        strong.style.width = "85%";
        // strong.textContent = "Strong"
    }
    if (passwordLength >= 10 && lowercasePassword && uppercasePassword && numbersPassword && specialPassword) {
        poor.style.width = "0%";
        weak.style.width = "0%";
        strong.style.width = "100%";
        // strong.textContent = "Very Strong"
    }
}