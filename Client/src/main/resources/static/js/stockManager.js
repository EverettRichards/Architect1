const baseURL = "/dashboard/stocks/"
async function remove(event, ticker, redirect) {
    event.preventDefault();
    let response = await fetch(baseURL + ticker, {
        method: "DELETE",
    });
    //location.reload();
    window.location = redirect;
}