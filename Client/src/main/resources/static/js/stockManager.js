const baseURL = "/api/stocks/"
async function remove(event, id) {
    event.preventDefault();
    let response = await fetch(baseURL + id, {
        method: "DELETE",
    });
    location.reload();
}