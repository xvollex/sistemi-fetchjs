async function login(body = null) 
{
    var dati;
    const requestOptions = 
    {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: body ? JSON.stringify(body) : undefined
    };

    return fetch('http://localhost:8080/api/user/login', requestOptions);
}

document.querySelector("form").addEventListener("submit", function(event) 
{
    event.preventDefault();

    var email = document.querySelector("input[name=email]").value;
    var password = document.querySelector("input[name=password]").value;

    login({"email": email, "password": password})
            .then(async response => await response.json())
            .then(data => {
                console.log(data);
                window.location.href="index.html";
            })
            .catch((error) => {
                console.error('Errore:', error);
                alert("Credeziali sbagliate");
            });
});
