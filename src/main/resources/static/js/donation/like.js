/*좋아요 상태 확인 함수*/
function checkLikeStatus(token, supportId) {
    if (token) {
        axios.get(`/support/${supportId}/likeStatus`, {
            headers: {
                'Authorization': 'Bearer ' + token
            }
        })
        .then(function(response) {
            const isLiked = response.data;
            if (isLiked) {
                //좋아요 상태일 때 클래스 추가
                var likeBtn = document.getElementById('likeBtn');
                likeBtn.classList.add('liked');
            }
        })
        .catch(function(error) {
            console.error(error);
        });
    }
}

/*좋아요 생성 함수*/
function createLike(token, supportId) {
    if (!token) {
        alert("로그인 후 이용해주세요.")
        return;
    }

    axios.post(`/support/${supportId}/like`, {}, {
        headers: {
            'Authorization': 'Bearer ' + token
        }
    })
    .then(function(response) {
        console.log(response.data);

        var likeBtn = document.getElementById('likeBtn');
        likeBtn.classList.add('liked'); //하트 채움
        document.querySelector('.like-count').textContent++;
    })
    .catch(function(error) {
        console.error(error);
        alert(error.response.data.message);
    });
}

/*좋아요 삭제 함수*/
function deleteLike(token, supportId) {
    if (!token) {
        alert("로그인 후 이용해주세요.");
        return;
    }

    //사용자가 확인을 누르면 컨트롤러 호출
    axios.delete(`/support/${supportId}/like`, {
        headers: {
            'Authorization': 'Bearer ' + token
        }
    }).then(function(response) {
        console.log(response.data);

        var likeBtn = document.getElementById('likeBtn');
        likeBtn.classList.remove('liked');  //하트 비움
        document.querySelector('.like-count').textContent--;
    })
    .catch(function(error) {
        console.error(error);
        alert(error.response.data.message);
    });
}