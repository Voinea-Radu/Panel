function profileTemplate() {
    callAPI(`/api/check/staff?user=${user.username}&useCase=any`, {},
        () => {
        }, () => {
        }, () => {
            document.getElementById("sanctions-row").hidden = true;
        }, () => {
            document.getElementById("sanctions-row").hidden = false;
        })
}