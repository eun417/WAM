/*댓글 생성*/
function createComment() {
    //CommentRequestDto 객체 생성
    const supportId = document.getElementById('supportId').value;
    const content = document.querySelector('.comment-content').value;

    const commentReq = {
        content: content
    }

    api.post(`/support/${supportId}/comment`, commentReq)
        .then(function(response) {
            alert(response.data);
            window.location.reload();   //새로고침
        })
        .catch(function(error) {
            console.error(error);
            alert(error.response.data.message);
        });
}

/*댓글 삭제 함수*/
function deleteComment() {
    document.querySelectorAll('.delete-comment-btn').forEach(function(btn) {
        btn.addEventListener('click', function() {
            const commentId = this.parentNode.querySelector('.commentId').value;
//            console.log('삭제할 commentId:'+commentId);

            if (confirm("댓글을 삭제하시겠습니까?")) {
                //사용자가 확인을 누르면 댓글 삭제 요청
                api.delete(`/support/comment/${commentId}`)
                    .then(function(response) {
                        alert(response.data);
                        window.location.reload();   //새로고침
                    })
                    .catch(function(error) {
                        console.error(error);
                        alert(error.response.data.message);
                    });
            }
        });
    });
}