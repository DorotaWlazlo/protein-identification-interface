
// JavaScript code for populating the select elements


// Populating Enzyme select
fetch('/enzymeNames')
    .then(response => response.json())
    .then(names => {
        const enzymeSelect = document.getElementById('enzymeSelect');
        for (let i = 0; i < names.length; i++) {
            const option = document.createElement('option');
            option.value = names[i];
            option.text = names[i];
            enzymeSelect.appendChild(option);
        }
    });

// Populating Database select
fetch('/databaseNames')
    .then(response => response.json())
    .then(names => {
        const baseSelect = document.getElementById('baseSelect');
        for (let i = 0; i < names.length; i++) {
            const option = document.createElement('option');
            option.value = names[i];
            option.text = names[i];
            baseSelect.appendChild(option);
        }
    });

// Populating Mc select
const mcSelect = document.getElementById('mcSelect');
for (let i = 0; i < 10; i++) {
    const option = document.createElement('option');
    option.value = i.toString();
    option.text = i.toString();
    mcSelect.appendChild(option);
}


// Populating Fixed modifications select
fetch('/ptmNames')
    .then(response => response.json())
    .then(names => {
        const ptmFixSelect = document.getElementById('ptmFixSelect');
        for (let i = 0; i < names.length; i++) {
            const option = document.createElement('option');
            option.value = names[i];
            option.text = names[i];
            if (names[i] === "Carbamidomethyl (C)") {
                option.selected = true;
            }
            ptmFixSelect.appendChild(option);
        }
    });

// Populating Variable modifications select
fetch('/ptmNames')
    .then(response => response.json())
    .then(names => {
        const ptmVarSelect = document.getElementById('ptmVarSelect');
        for (let i = 0; i < names.length; i++) {
            const option = document.createElement('option');
            option.value = names[i];
            option.text = names[i];
            if (names[i]  === "Oxidation (M)") {
                option.selected = true;
            }
            ptmVarSelect.appendChild(option);
        }
    });

// Populating PepTolUnit select
fetch('/tolUnitNames')
    .then(response => response.json())
    .then(names => {
        const pepTolUnitSelect = document.getElementById('pepTolUnitSelect');
        for (let i = 0; i < names.length; i++) {
            const option = document.createElement('option');
            option.value = names[i];
            option.text = names[i];
            pepTolUnitSelect.appendChild(option);
        }
    });

// Populating fragTolUnit select
fetch('/tolUnitNames')
    .then(response => response.json())
    .then(names => {
        const fragTolUnitSelect = document.getElementById('fragTolUnitSelect');
        for (let i = 0; i < names.length; i++) {
            const option = document.createElement('option');
            option.value = names[i];
            option.text = names[i];
            if (names[i]  === "Da") {
                option.selected = true;
            }
            fragTolUnitSelect.appendChild(option);
        }
    });

