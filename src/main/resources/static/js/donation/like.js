/*좋아요 상태 확인 함수*/
function checkLikeStatus(supportId) {
    //토큰을 로컬 스토리지에서 가져오기
    const token = localStorage.getItem('accessToken');

    if (token) {
        //좋아요 상태 확인
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


//좋아요 상태에 따라 생성 or 삭제
function toggleLike() {
    var likeBtn = document.getElementById('likeBtn');
    const supportId = document.getElementById('supportId').value;

    if (likeBtn.classList.contains('liked')) {
        //이미 좋아요를 누름 -> 좋아요 취소
        deleteLike(supportId);

    } else {
        //좋아요를 누르지 않음 -> 좋아요 생성
        createLike(supportId);
    }
}

/*좋아요 생성 함수*/
function createLike(supportId) {
    //토큰을 로컬 스토리지에서 가져오기
    const token = localStorage.getItem('accessToken');
    if (!token) {
        alert("로그인 후 이용해주세요.")
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
        alert("좋아요를 실패했습니다.");
    });
}

/*좋아요 삭제 함수*/
function deleteLike(supportId) {
    //토큰을 로컬 스토리지에서 가져오기
    const token = localStorage.getItem('accessToken');
    if (!token) {
        alert("로그인 후 이용해주세요.")
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