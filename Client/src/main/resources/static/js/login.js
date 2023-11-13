function broadcastMessage(message) { //only here for testing reasons
    alert(message);
}

(function() {
    let loginForm = document.getElementById("login-form");

    loginForm.onsubmit = login;

    async function login(event) {
        event.preventDefault();

        let formData = new FormData(loginForm);

        let response = await fetch("/api/login", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                "username": formData.get("username"),
                "password": formData.get("password"),
            }),
            // redirect: "follow",
        });

        let message = await response.text();
        broadcastMessage(message);

        // in case follow didn't work
        let location = response.headers.get("Location");
        if (location !== null) {
            window.location.href = location;
        }
    }
        
})();