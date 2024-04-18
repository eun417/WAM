/*로그아웃*/
function logout() {
    api.get('/auth/logout')
        .then(function(response) {
          console.log(response.data);
          localStorage.clear();
          window.location.href = '/';
        })
        .catch(function(error) {
          console.error(error);
        });
}