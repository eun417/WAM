/*페이지 로드 시 함수 실행*/
document.addEventListener('DOMContentLoaded', function() {
    loadList(0);
});

/*나의 후원 목록 조회*/
function loadList(pageNo) {
   api.get(`/member/support-detail`, {
        params: {
            pageNo: pageNo
        }
   }).then(function (response) {
       //서버로부터 받은 데이터를 테이블에 추가
       const supportList = response.data.content;
       const tableBody = document.querySelector('#supportTableBody');

       //DocumentFragment 생성
       const fragment = new DocumentFragment();

       if (supportList.length === 0) {
           noResult(6, fragment);
       } else {
           supportList.forEach(function (support) {
               let row = `<tr>
                           <td>${support.supportId}</td>
                           <td><a href="/support/detail/${support.supportId}" class="title-hover">${support.title}</a></td>
                           <td>${support.goalAmount}</td>
                           <td>${support.supportAmount}</td>
                           <td>${support.createDate}</td>
                           <td><button class="answer-btn btn bs1 bc3}" style="cursor: default;">${support.supportStatus === 'START' ? '후원 시작' : support.supportStatus === 'SUPPORTING' ? '후원중' : '후원 종료'}</button></td>
                       </tr>`;
                let tr = document.createElement('tr');
                tr.innerHTML = row;

                fragment.appendChild(tr);
           });
       }

        //테이블 내용을 한 번에 추가
        tableBody.innerHTML = '';
        tableBody.appendChild(fragment);

       //페이지 내비게이션 업데이트
       renderPagination(response.data.totalPages, pageNo);
   })
   .catch(function (error) {
       console.error(error);
   });
}