/*페이지 로드 시 함수 실행*/
document.addEventListener('DOMContentLoaded', function() {
    loadList(0);
});


/*나의 좋아요 목록 조회*/
function loadList(pageNo) {
    api.get(`/member/like-detail`, {
        params: {
            pageNo: pageNo
        }
    }).then(function(response) {
        //서버로부터 받은 데이터를 테이블에 추가
        var likeList = response.data.content;
        var tableBody = document.querySelector('#likeTableBody');

        //DocumentFragment 생성
        var fragment = new DocumentFragment();

        likeList.forEach(function(like) {
            var row = `<tr>
                        <td>${like.supportId}</td>
                        <td><a href="/support/detail/${like.supportId}" class="title-hover">${like.title}</a></td>
                        <td>${like.nickname}</td>
                        <td>${like.createDate}</td>
                        <td>
                            <button class="answer-btn btn bs1 bc3 style="cursor: default;">
                                ${like.supportStatus === 'START' ? '후원 시작'
                                : like.supportStatus === 'SUPPORTING' ? '후원중'
                                : like.supportStatus === 'ENDING_SOON' ? '종료임박'
                                : '후원 종료'}
                            </button>
                        </td>
                        <td><button class="deleteLike-btn btn bs1 bc6">삭제</button></td>
                    </tr>`;
            var tr = document.createElement('tr');
            tr.innerHTML = row;

            fragment.appendChild(tr);
        });

        //테이블 내용을 한 번에 추가
        tableBody.innerHTML = '';
        tableBody.appendChild(fragment);

        //페이지 내비게이션 업데이트
        renderPagination(response.data.totalPages, pageNo);

        //삭제 버튼에 이벤트 리스너 추가
        tableBody.querySelectorAll('.deleteLike-btn').forEach(function(button) {
            button.addEventListener('click', function() {
                var supportId = this.parentNode.parentNode.firstElementChild.textContent;
                console.log('삭제할 후원: ' + supportId);

                if (confirm("정말로 좋아요를 삭제하시겠습니까?")) {
                    //사용자가 확인을 누르면 좋아요 삭제 요청
                    api.delete('/support/' + supportId + '/like')
                        .then(function(response) {
                            console.log(response.data);
                            alert(response.data);
                            window.location.reload(); //새로고침
                        })
                        .catch(function(error) {
                            console.error('좋아요 삭제 실패', error);
                            alert(error.response.data.message);
                        });
                }
            });
        });
    }).catch(function(error) {
        console.error(error);
    });
}