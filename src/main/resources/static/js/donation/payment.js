//사용자로부터 후원금액 입력 받는 함수
function promptAmount(token) {
    var amount = prompt("후원할 금액을 입력해주세요:");

    if (amount !== null) {
        //입력된 값이 숫자인지 확인
        if (!isNaN(amount)) {
            //결제하기 버튼 클릭
            requestPay(token, amount);
        } else {
            alert("올바른 숫자를 입력해주세요.");
            promptAmount();
        }
    }
}

/*후원하기(결제) 함수*/
function requestPay(token, amount) {
    api.get('/member/profile-detail')
        .then(function (response) {
            const memberData = response.data;   //회원 정보

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
                buyer_email: memberData.email,    //나중에 로그인한 사용자 정보로 수정
                buyer_name: memberData.name,
                buyer_tel: memberData.phoneNumber
            }, function (rsp) {
                if (rsp.success) {
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
                    api.post('/payment/validate', paymentReq)
                        .then(function(response) {
                            console.log(response);
                            alert("후원해주셔서 감사합니다!");
                            window.location.reload();   //새로고침
                        })
                        .catch(function (error) {
                            console.error(error);
                            alert(error.response.data.message);
                        });
                } else {
                    alert("결제 실패");
                    console.log(rsp);
                }
            });
        })
        .catch(function (error) {
            console.error(error);
        });
}