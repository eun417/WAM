//payload에서 데이터 가져오기
function getPayloadData(accessToken) {
    //토큰이 없는 경우 null 반환
    if (!accessToken) {
        return null;
    }
    //토큰의 payload 부분 추출
    var base64Payload = accessToken.split('.')[1];    //value 0: header, 1: payload, 2: VERIFY SIGNATURE
    //base64 디코딩 및 JSON 객체로 변환
    return JSON.parse(atob(base64Payload.replace(/-/g, '+').replace(/_/g, '/')));
}

//토큰 만료 시간 확인
function getTokenExpirationDate(exp) {
    //UNIX 시간 스탬프를 밀리초 단위로 변환
    const milliseconds = exp * 1000;

    //밀리초를 기반으로 Date 객체 생성
    const date = new Date(milliseconds);

    //Date 객체를 사용하여 날짜 및 시간 표시
    console.log("토큰 만료 시간:"+date);
}