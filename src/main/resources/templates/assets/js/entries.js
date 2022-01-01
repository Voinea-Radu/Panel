function entriesTemplate() {
    callAPI(`/api/check/staff?user=${user.username}&useCase=any`, {},
        () => {
        }, () => {
        }, () => {
            redirect("/401")
        }, () => {
            redirect("/401")
        })
}