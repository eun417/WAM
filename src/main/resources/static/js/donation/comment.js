/*댓글 생성*/
function createComment(token) {
    if (!token) {
        alert("로그인 후 이용해주세요.")
    }

    //CommentRequestDto 객체 생성
    const supportId = document.getElementById('supportId').value;
    const content = document.querySelector('.comment-content').value;

    const commentReq = {
        content: content
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
        alert(error.response.data.message);
    });
}

/*댓글 삭제 함수*/
function deleteComment(token) {
    document.querySelectorAll('.delete-comment-btn').forEach(function(btn) {
        btn.addEventListener('click', function() {
            var commentId = this.parentNode.querySelector('.commentId').value;
            console.log('commentId:'+commentId);

            if (!token) {
                alert("로그인 후 이용해주세요.")
            }

            if (confirm("정말로 댓글을 삭제하시겠습니까?")) {
                //사용자가 확인을 누르면 댓글 삭제 요청
                axios.delete(`/support/comment/${commentId}`, {
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