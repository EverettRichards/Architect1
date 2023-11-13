function search(){
    let action_src = "/search/" + document.getElementById("search-bar").value;
    let form = document.getElementById("search");
    window.history.pushState("searching...", "Loading...", action_src);
    form.action = action_src;
}