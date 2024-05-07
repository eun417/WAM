/*페이지 로드 시 함수 실행*/
document.addEventListener('DOMContentLoaded', function() {
    loadList(0);
});


/*QnA 목록 조회*/
function loadList(pageNo) {
    axios.get('/qna', {
        params: {
            pageNo: pageNo
        }
    }).then(function(response) {
        const qnaList = response.data.content;
        const tableBody = document.querySelector('#qnaTableBody');

        //DocumentFragment 생성
        const fragment = new DocumentFragment();

        qnaList.forEach(function(qna) {
            let row = `<tr>
                        <td>${qna.qnaId}</td>
                        <td><a href="/qna/detail/${qna.qnaId}" class="title-hover">${qna.title}</a></td>
                        <td>${qna.nickname}</td>
                        <td>${qna.createDate}</td>
                        <td>${qna.viewCount}</td>
                        <td><button class="answer-btn btn bs1 ${qna.qnaCheck === 'CHECKING' ? 'bc3' : 'bc7'}" style="cursor: default;">${qna.qnaCheck === 'CHECKING' ? '확인 중' : '답변 완료'}</button></td>
                    </tr>`;
            let tr = document.createElement('tr');
            tr.innerHTML = row;

            fragment.appendChild(tr);
        });

        //테이블 내용을 한 번에 추가
        tableBody.innerHTML = '';
        tableBody.appendChild(fragment);

        //페이지 내비게이션 업데이트
        renderPagination(response.data.totalPages, pageNo);
    })
    .catch(function(error) {
        console.error(error);
    });
}