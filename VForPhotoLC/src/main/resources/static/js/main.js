var x=0;
var y=0;
function changeColor1() {
    if (x % 2 ==0) {
        document.getElementById("1").style.backgroundColor = "green";
        document.getElementById("2").style.backgroundColor = "white";
        x=x+1;
    }else{
        document.getElementById("1").style.backgroundColor = "white";
        x=x+1;
        y=y+1;
    }
}
function changeColor2() {
    if (y % 2 ==0) {
        document.getElementById("2").style.backgroundColor = "red";
        document.getElementById("1").style.backgroundColor = "white";
        y=y+1;
    }else{
        document.getElementById("2").style.backgroundColor = "white";
        y=y+1;
        x=x+1;
    }
}
function changeColor3() {
    if (x % 2 ==0) {
        document.getElementById("3").style.backgroundColor = "green";
        document.getElementById("4").style.backgroundColor = "white";
        x=x+1;
    }else{
        document.getElementById("3").style.backgroundColor = "white";
        x=x+1;
        y=y+1;
    }
}
function changeColor4() {
    if (y % 2 ==0) {
        document.getElementById("4").style.backgroundColor = "red";
        document.getElementById("3").style.backgroundColor = "white";
        y=y+1;
    }else{
        document.getElementById("4").style.backgroundColor = "white";
        y=y+1;
        x=x+1;
    }
}
function changeColor5() {
    if (x % 2 ==0) {
        document.getElementById("5").style.backgroundColor = "green";
        document.getElementById("6").style.backgroundColor = "white";
        x=x+1;
    }else{
        document.getElementById("5").style.backgroundColor = "white";
        x=x+1;
        y=y+1;
    }
}
function changeColor6() {
    if (y % 2 ==0) {
        document.getElementById("6").style.backgroundColor = "red";
        document.getElementById("5").style.backgroundColor = "white";
        y=y+1;
    }else{
        document.getElementById("6").style.backgroundColor = "white";
        y=y+1;
        x=x+1;
    }
}
function changeColor7() {
    if (x % 2 ==0) {
        document.getElementById("7").style.backgroundColor = "green";
        document.getElementById("8").style.backgroundColor = "white";
        x=x+1;
    }else{
        document.getElementById("7").style.backgroundColor = "white";
        x=x+1;
        y=y+1;
    }
}
function changeColor8() {
    if (y % 2 ==0) {
        document.getElementById("8").style.backgroundColor = "red";
        document.getElementById("7").style.backgroundColor = "white";
        y=y+1;
    }else{
        document.getElementById("8").style.backgroundColor = "white";
        y=y+1;
        x=x+1;
    }
}

function show_menu() {
    document.getElementById("").classList.toggle("show");
}
