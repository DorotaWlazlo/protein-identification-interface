const form = document.getElementById('form');
const username = document.getElementById('name2');
const email = document.getElementById('email2');
const password = document.getElementById('psw');
const password2 = document.getElementById('psw2');

function openForm() {
    document.getElementById("register").style.display = "block";
}
function closeForm() {
    document.getElementById("form").reset();
    setDefault(username);
    setDefault(email);
    setDefault(password);
    setDefault(password2);
    document.getElementById("register").style.display = "none";
}
form.addEventListener('submit', e => {
    validate(e);
});

const validate = (e) => {
    const usernameValue = username.value.trim();
    const emailValue = email.value.trim();
    const passwordValue = password.value.trim();
    const password2Value = password2.value.trim();

    if(usernameValue === '') {
        setError(username, 'Username is required');
        e.preventDefault();
    } else {
        setSuccess(username);
    }

    if(emailValue === '') {
        setError(email, 'Email is required');
        e.preventDefault();

    } else if (!isValidEmail(emailValue)) {
        setError(email, 'Provide a valid email address');
        e.preventDefault();

    } else {
        setSuccess(email);
    }

    if(passwordValue === '') {
        setError(password, 'Password is required');
        e.preventDefault();

    } else if (passwordValue.length < 6 ) {
        setError(password, 'Password must be at least 6 character.')
        e.preventDefault();

    } else {
        setSuccess(password);
    }

    if(password2Value === '') {
        setError(password2, 'Please confirm your password');
        e.preventDefault();

    } else if (password2Value !== passwordValue) {
        setError(password2, "Passwords doesn't match");
        e.preventDefault();

    } else {
        setSuccess(password2);
    }

};

const setError = (element, message) => {
    const inputControl = element.parentElement;
    const errorDisplay = inputControl.querySelector('.err');

    errorDisplay.innerText = message;
    inputControl.classList.add('error');
    inputControl.classList.remove('success')
}

const setSuccess = element => {
    const inputControl = element.parentElement;
    const errorDisplay = inputControl.querySelector('.err');

    errorDisplay.innerText = '';
    inputControl.classList.add('success');
    inputControl.classList.remove('error');
};

const setDefault = element => {
    const inputControl = element.parentElement;
    const errorDisplay = inputControl.querySelector('.err');

    errorDisplay.innerText = '';
    inputControl.classList.remove('success');
    inputControl.classList.remove('error');
};

const isValidEmail = email => {
    const re = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    return re.test(String(email).toLowerCase());
}


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

