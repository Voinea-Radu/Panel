function entriesTemplate() {
    checkLoggedStatus();

    var user = JSON.parse(getCookie("login_data"));

    callAPI(`/api/check/staff?user=${user.username}&useCase=any`, {},
        () => {
        }, () => {
        }, () => {
            redirect("/401")
        }, () => {
            redirect("/401")
        })
}