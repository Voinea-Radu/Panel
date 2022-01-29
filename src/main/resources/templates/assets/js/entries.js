async function entriesTemplate() {
    await checkLoggedStatus();

    var user = JSON.parse(getCookie("login_data"));

    callAPI2(`/api/check/staff?user=${user.username}&useCase=any`, {},
        () => {
            document.getElementById("logged-in-required").style.visibility = "visible";
        }, () => {
            redirect("/401")
        });
}