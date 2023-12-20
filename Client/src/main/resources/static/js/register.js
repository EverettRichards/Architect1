function broadcastMessage(message) {
    alert(message);
}

(function() {
    let registerForm = document.getElementById("register-form");

    registerForm.onsubmit = register;
    
    async function register(event) {
        event.preventDefault();
        let formData = new FormData(registerForm);

        let response = await fetch("/api/register", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                "email": formData.get("email"),
                "nickname": formData.get("nickname"),
                "username": formData.get("username"),
                "password": formData.get("password"),
            }),
            //redirect: "follow",
        });


        let message = await response;
        // broadcastMessage(message);
        console.log(message);

        // in case follow didn't work
        let location = response.headers.get("Location");
        if (location !== null) {
            window.location.href = location;
        }
    }
})();