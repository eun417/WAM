/*페이지 로드 시 함수 실행*/
document.addEventListener('DOMContentLoaded', function() {
    loadSupportList(0);
});

//전체 페이지 수
let totalPages = 0

/*후원 목록 조회*/
function loadSupportList(pageNo) {
    axios.get('/support', {
        params: {
            pageNo: pageNo
        }
    })
    .then(function(response) {
        var supportList = response.data.content;

        //DocumentFragment 생성
        var fragment = document.createDocumentFragment();
        totalPages = response.data.totalPages;

        supportList.forEach(function(support) {
            //graph의 width 설정(목표금액의 몇 퍼센트 모였는지 보여줌)
            const progress = (support.supportAmount / support.goalAmount) * 100;
            const graphWidth = progress > 100 ? '100%' : progress + '%';
            const graph = '<div class="graph" style="width: ' + graphWidth + ';"></div>';

            var row = `<div class="donation-box">
                            <div class="donation-img-box">
                                <div class="donation-status ${support.supportStatus === 'START' ? 'start' :
                                                               support.supportStatus === 'ENDING_SOON' ? 'ending-soon' :
                                                               support.supportStatus === 'END' ? 'end' : ''}">
                                    ${support.supportStatus === 'START' ? '후원 시작' :
                                      support.supportStatus === 'ENDING_SOON' ? '종료 임박' :
                                      support.supportStatus === 'END' ? '후원 종료' : ''}
                                </div>
                                <div class="donation-img"><img src="${support.firstImg}" class="first-pic" alt="animal"></div>
                            </div>
                            <a href="/support/detail/${support.supportId}" class="donation-title title-hover">${support.title}</a>
                            <div class="donation-nickname gray-text">${support.nickname}</div>
                            <div class="donation-amount">
                                <div class="percent">
                                    ${graph}
                                    <div class="graph-bottom"></div>
                                </div>
                                <p class="support-amount"><span>${support.supportAmount}</span>원</p>
                            </div>
                        </div>`;
            var div = document.createElement('div');
            div.innerHTML = row;

            fragment.appendChild(div.firstChild);
        });

        //DOM에 fragment 추가
        var donationLine = document.querySelector('.donation-line');
        donationLine.appendChild(fragment);
    })
    .catch(function(error) {
        console.error(error);
    });
}