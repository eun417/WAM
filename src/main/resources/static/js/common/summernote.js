//summernote 설정
$(document).ready(function () {
    $('#summernote').summernote({
        placeholder: '내용을 작성하세요',
        height: 500,
        maxHeight: 500,
        lang: "ko-KR",
        toolbar: [
            ['style', ['style']], // 글자 스타일 설정 옵션
            ['fontsize', ['fontsize']], // 글꼴 크기 설정 옵션
            ['font', ['bold', 'underline', 'clear']], // 글자 굵게, 밑줄, 포맷 제거 옵션
            ['color', ['color']], // 글자 색상 설정 옵션
            ['table', ['table']], // 테이블 삽입 옵션
            ['para', ['ul', 'ol', 'paragraph']], // 문단 스타일, 순서 없는 목록, 순서 있는 목록 옵션
            ['height', ['height']], // 에디터 높이 조절 옵션
            ['insert', ['picture', 'link', 'video']], // 이미지 삽입, 링크 삽입, 동영상 삽입 옵션
            ['view', ['codeview', 'fullscreen', 'help']], // 코드 보기, 전체 화면, 도움말 옵션
        ],
        fontSizes: [
            '8', '9', '10', '11', '12', '14', '16', '18',
            '20', '22', '24', '28', '30', '36', '50', '72',
        ], // 글꼴 크기 옵션
        styleTags: [
            'p',  // 일반 문단 스타일 옵션
            {
                title: 'Blockquote',
                tag: 'blockquote',
                className: 'blockquote',
                value: 'blockquote',
            },  // 인용구 스타일 옵션
            'pre',  // 코드 단락 스타일 옵션
            {
                title: 'code_light',
                tag: 'pre',
                className: 'code_light',
                value: 'pre',
            },  // 밝은 코드 스타일 옵션
            {
                title: 'code_dark',
                tag: 'pre',
                className: 'code_dark',
                value: 'pre',
            },  // 어두운 코드 스타일 옵션
            'h1', 'h2', 'h3', 'h4', 'h5', 'h6',  // 제목 스타일 옵션
        ],
        //callbacks: 이미지 업로드 처리
        callbacks : {
            //이미지 업로드
            onImageUpload : function(files, editor, welEditable){
                for (var i = files.length - 1; i >= 0; i--) {
                    console.log('fileName: ' + files[i].name);
                    uploadSummernoteImageFile(files[i], this);
                }
            },
            //이미지 삭제
            onMediaDelete: function ($target, editor, $editable) {
                if (confirm('이미지를 삭제 하시겠습니까?')) {
                    var deletedImageUrl = $target.attr('src');  //이미지의 URL 반환
                    console.log('deletedImageUrl: ' + deletedImageUrl);
                    deleteSummernoteImageFile(deletedImageUrl);
                }
            }
        }
    });
});

/*이미지 업로드 함수*/
function uploadSummernoteImageFile(file, editor) {
    uploadImage(file)
        .then(function(response) {
            var imageUrl = response.data; //이미지 업로드 후의 이미지 URL
            var $image = $('<img>').attr('src', imageUrl);
            $(editor).summernote('insertNode', $image[0]);
            console.log(response.data); //값이 잘 넘어오는지 확인
        })
        .catch(function(error) {
            console.error(error);
        });
}

/*이미지 삭제 함수*/
function deleteSummernoteImageFile(imgUrl) {
    deleteImage(imgUrl)
        .then(function(response) {
            console.log(response.data);
        })
        .catch(function(error) {
            console.error('이미지 삭제 실패:', error);
        });
}