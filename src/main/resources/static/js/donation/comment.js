/*댓글 생성*/
document.getElementById('createCommentBtn').addEventListener('click', function() {
    const supportId = document.getElementById('supportId').value;
    const content = document.querySelector('.comment-content').value;

    //CommentRequestDto 객체 생성
    const commentReq = {
        content: content
    }

    //토큰을 로컬 스토리지에서 가져오기
    const token = localStorage.getItem('accessToken');
    if (!token) {
        alert("로그인 후 이용해주세요.")
    }

    axios.post(`/support/${supportId}/comment`, commentReq, {
        headers: {
            'Authorization': 'Bearer ' + token
        }
    })
    .then(function(response) {
        alert(response.data);
        window.location.href = `/support/detail/${supportId}`   //새로고침
    })
    .catch(function(error) {
        console.error(error);
        alert("댓글 작성에 실패했습니다.");
    });
});

/*댓글 삭제 함수*/
function deleteComment(supportId) {
    document.querySelectorAll('.delete-comment-btn').forEach(function(btn) {
        btn.addEventListener('click', function() {
            var commentId = this.parentNode.querySelector('.commentId').value;
            console.log('commentId:'+commentId);

            //토큰을 로컬 스토리지에서 가져오기
            const token = localStorage.getItem('accessToken');
            if (!token) {
                alert("로그인 후 이용해주세요.")
            }

            if (confirm("정말로 댓글을 삭제하시겠습니까?")) {
                //사용자가 확인을 누르면 컨트롤러 호출
                axios.delete(`/support/${supportId}/comment/${commentId}`, {
                    headers: {
                        'Authorization': 'Bearer ' + token
                    }
                }).then(function(response) {
                    alert(response.data);
                    window.location.href = `/support/detail/${supportId}`   //새로고침
                })
                .catch(function(error) {
                    console.error(error);
                    alert(error.response.data.message);
                });
            }
        });
    });
}