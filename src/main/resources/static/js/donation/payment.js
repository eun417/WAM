//사용자로부터 후원금액 입력 받는 함수
function promptAmount() {
    var amount = prompt("후원할 금액을 입력해주세요:");

    if (amount !== null) {
        //변환된 값이 숫자인지 확인
        if (!isNaN(amount)) {
            //결제하기 버튼 클릭
            requestPay(amount);
        } else {
            alert("올바른 숫자를 입력해주세요.");
            promptAmount();
        }
    }
}

/*후원하기(결제) 함수*/
function requestPay(amount) {
    var impKey = document.getElementById('impKey').getAttribute('href');
    IMP.init(impKey);

    var today = new Date();
    var makeMerchantUid = today.getFullYear()+""+(today.getMonth()+1)+""+today.getDate()+""+today.getHours()+""+today.getMinutes()+""+today.getSeconds();

    IMP.request_pay({
        pg: 'html5_inicis.INIpayTest',
        pay_method: "card",
        merchant_uid: "IMP" + makeMerchantUid,    //주문번호
        name: "WAM 후원", //결제 이름
        amount: amount, //결제금액
        buyer_email: "eunahchung0417@gmail.com",    //나중에 로그인한 사용자 정보로 수정
        buyer_name: "정은아",
        buyer_tel: "01085249584",
        buyer_postcode: "57098"
    }, function (rsp) {
        if (rsp.success) {
            //토큰을 로컬 스토리지에서 가져오기
            const token = localStorage.getItem('accessToken');
            if (!token) {
                alert("로그인 후 이용해주세요.")
            }

            var supportId = document.getElementById("supportId").value;
            var impUid = rsp.imp_uid;
            var inputAmount = amount;

            //PaymentRequestDto 객체 생성
            const paymentReq = {
                supportId: supportId,
                inputAmount: inputAmount,
                impUid: impUid
            };

            //결제 검증
            axios.post('/payment/validate', paymentReq, {
                headers: {
                    'Authorization': 'Bearer ' + token
                }
            })
            .then(function(response) {
                console.log(response);
                alert("후원해주셔서 감사합니다!");
                window.location.href = `/support/detail/${supportId}`   //새로고침
            })
            .catch(function (error) {
                console.error(error);
                alert("후원을 실패했습니다.");
            });
        } else {
            alert("결제 실패");
            console.log(rsp);
        }
    });
}