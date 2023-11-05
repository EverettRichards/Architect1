function broadcastMessage(message) { //only here for testing reasons
    alert(message);
}

(function() {
    let signInButton = document.getElementById("sign-in-button");
    let signInForm = document.getElementById("sign-in-form");

    async function signIn(event) {
        event.preventDefault();
        let formData = new FormData(signInForm);

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

        // // in case follow didn't work
        // if (response.redirected) {
        //     window.location.href = response.url;
        // }

        let message = await response.text();
        broadcastMessage(message);

        // let reply = await response.json();

        // if(response.status != 200) {
        //     boardcastMessage(reply.message, "error");
        //     return;
        // }
    }

    // loginForm.onsubmit = login;

    signInButton.addEventListener("click", login)
})();
