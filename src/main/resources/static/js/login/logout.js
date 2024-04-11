/*로그아웃*/
function logout(token) {
    axios.get('/auth/logout', {
        headers: {
            'Authorization': 'Bearer ' + token
        }
    }).then(function(response) {
      console.log(response.data);
      removeTokenInLocalStorage();
      window.location.href = '/';
    })
    .catch(function(error) {
      console.error(error);
    });
}