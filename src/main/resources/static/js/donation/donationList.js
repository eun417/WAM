/*페이지 로드 시 함수 실행*/
document.addEventListener('DOMContentLoaded', function() {
    isLoading = true;
    loadSupportList();
});

//전체 페이지 수
let lastId = null;

/*후원 목록 조회*/
function loadSupportList() {
    axios.get('/support', {
        params: {
            lastId: lastId
        }
    })
    .then(function(response) {
        const supportList = response.data;

        //DocumentFragment 생성
        const fragment = document.createDocumentFragment();

        supportList.forEach(function(support) {
            //graph의 width 설정(목표금액의 몇 퍼센트 모였는지 보여줌)
            const progress = (support.supportAmount / support.goalAmount) * 100;
            const graphWidth = progress > 100 ? '100%' : progress + '%';
            const graph = '<div class="graph" style="width: ' + graphWidth + ';"></div>';

            let row = `<div class="donation-box">
                            <div class="donation-img-box">
                                <div class="donation-status ${support.supportStatus === 'ENDING_SOON' ? 'ending-soon' :
                                                               support.supportStatus === 'END' ? 'end' : ''}">
                                    ${support.supportStatus === 'ENDING_SOON' ? '종료 임박' :
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
            let div = document.createElement('div');
            div.innerHTML = row;

            fragment.appendChild(div.firstChild);
        });

        //DOM에 fragment 추가
        const donationLine = document.querySelector('.donation-line');
        donationLine.appendChild(fragment);

        isLoading = false; //데이터 요청 완료

        if (supportList.length === 0) {
            //더 이상 데이터가 없으면 무한 스크롤 멈춤
            window.removeEventListener('scroll', onScroll);
        } else {
            const lastSupport = supportList[supportList.length - 1];
            lastId = lastSupport.supportId;
            console.log("lastId: ", lastId);
        }
    })
    .catch(function(error) {
        console.error(error);
        isLoading = false; // 오류 발생 시에도 데이터 요청 상태를 초기화
    });
}