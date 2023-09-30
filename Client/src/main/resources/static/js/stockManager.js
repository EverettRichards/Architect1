const baseURL = "/api/stocks/"
async function remove(event, id, redirect) {
    event.preventDefault();
    let response = await fetch(baseURL + id, {
        method: "DELETE",
    });
    //location.reload();
    window.location = redirect;
}