/*QnA 조회 함수*/
function loadQna(pageNo) {
    axios.get('/qna', {
        params: {
            page: pageNo
        }
    })
    .then(function(response) {
        const qnaList = response.data.content;

        for (let i = 0; i < 3; i++) {
            const qna = qnaList[i];
            if (qna) {
                var row = `<div class="qna-detail-box">
                                <p class="qna-detail-title">${qna.title}</p>
                                <p class="gray-text">${qna.createDate}</p>
                                <p class="qna-content">${qna.content}</p>
                            </div>`;
                document.querySelector('.qna-list').innerHTML += row;
            } else {
                 console.log('QnA 없음');
             }
        }
    })
    .catch(function(error) {
        console.error(error);
    });
}

/*총 후원금 조회 함수*/
function loadTotalAmount() {
    axios.get('/payment/total-amount')
    .then(function(response) {
        const totalAmount = response.data;
        document.querySelector('.donation-total-amount>p>span').innerHTML = totalAmount;
    })
    .catch(function(error) {
        console.error(error);
    });
}

/*모든 후원 건수, 태그별 후원 조회 함수*/
document.querySelectorAll('.tag').forEach(function(btn) {
    btn.addEventListener('click', function() {
        //모든 버튼에서 .tag-active 클래스 제거
        document.querySelectorAll('.tag').forEach(function(element) {
            element.classList.remove('tag-active');
        });

        //클릭된 버튼에만 .tag-active 클래스 추가
        btn.classList.add('tag-active');

        const keyword = btn.id; //클릭된 버튼의 id 값을 keyword로 설정

        axios.get('/support/search/tag', {
        params: {
            keyword: keyword,
            page: 0
        }
        })
        .then(function(response) {
            //모든 후원 건수
            const totalSupport = response.data.totalElements;
            document.querySelector('.donation-total>p>span').innerHTML = totalSupport;

            const supportList = response.data.content;
            document.querySelector('.donation-line').innerHTML = '';

            for (let i = 0; i < 8; i++) {
                const support = supportList[i];

                if(support) {
                    //graph의 width 설정(목표금액의 몇 퍼센트 모였는지 보여줌)
                    const progress = (support.supportAmount / support.goalAmount) * 100;
                    const graphWidth = progress > 100 ? '100%' : progress + '%';
                    const graph = '<div class="graph" style="width: ' + graphWidth + ';"></div>';

                    var row = `<div class="donation-box">
                                    <div class="donation-img-box">
                                        <div class="donation-status start">
                                            ${support.supportStatus === 'START' ? '후원 시작' :
                                            support.supportStatus === 'SUPPORTING' ? '후원중' :
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

                    document.querySelector('.donation-line').innerHTML += row;
                } else {
                    console.log('후원 없음');
                }
            }
        })
        .catch(function(error) {
            console.error(error);
        });
    });
});

/*페이지 로드 시 함수 실행*/
document.addEventListener('DOMContentLoaded', function() {
    loadQna(0);
    loadTotalAmount();
    document.getElementById('mml').click(); //첫 번째 버튼 자동 클릭
});