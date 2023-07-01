/*ラジオボタン表示切り替え*/
 
 function formSwitch() {
        let sin = document.getElementById("sin");
        let book = document.getElementById("book");
        let acs = document.getElementById("acs");
        if (sin.checked) {
            // 新着順
            document.getElementById('box1').style.display = "";
            document.getElementById('box2').style.display = "none";
            document.getElementById('box3').style.display = "none";
        } else if (book.checked) {
            // ブクマ順
            document.getElementById('box1').style.display = "none";
            document.getElementById('box2').style.display = "";
            document.getElementById('box3').style.display = "none";
        } else if (acs.checked) {
            document.getElementById('box1').style.display = "none";
            document.getElementById('box2').style.display = "none";
            document.getElementById('box3').style.display = "";
        }
    }
    window.addEventListener('load', formSwitch());