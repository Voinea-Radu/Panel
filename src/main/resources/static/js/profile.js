function profileTemplate() {
    var user = JSON.parse(getCookie("login_data"));

    callAPI(`/api/check/staff?user=${user.username}&useCase=any`, {},
        () => {
            document.getElementById("sanctions-row").hidden = false;
            document.getElementById("sanctions-row-data").hidden = false;
        });
}