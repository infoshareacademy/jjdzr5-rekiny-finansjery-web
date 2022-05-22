var amount = document.getElementById('amount');
var rate = document.getElementById('rate');
var sum = document.getElementById('sum');
var mat = new Intl.NumberFormat('pl-PL', {style: 'currency', currency: 'PLN'}).format((amount.textContent * rate.textContent));
sum.textContent = mat;
amount.addEventListener('change', (e) => {
    mat = new Intl.NumberFormat('pl-PL', {style: 'currency', currency: 'PLN'}).format((e.target.value * rate.textContent));
    sum.textContent = mat;
});