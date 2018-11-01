var currentDate = new Date();
var lastDayOfMonth = new Date(currentDate.getFullYear(), currentDate.getMonth()+1,0);
var monthNames = ["January", "February", "March", "April", "May", "June","July", "August", "September", "October", "November","December"];
var weekdayNames = ["Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"];

function monthName() {
    document.getElementById("boi").innerHTML += ("<td>" + monthNames[currentDate.getMonth()] + "</td>");
}

function weekdaysName() {
    
}
function calendar() {
    document.getElementById("boi2").innerHTML += ("<table><tr><td colspan=7>" + monthNames[currentDate.getMonth()] + "<tr>");
    for(var p = 0; p < weekdayNames.length; p++) {
    document.getElementById("boi2").innerHTML += ("<td>" + weekdayNames[p] + "</td>");
    }
    document.getElementById("boi2").innerHTML += ("</tr><tr>");
    for(var w = 1; w <= lastDayOfMonth.getDate(); w++) {
        document.getElementById("boi2").innerHTML += ("<td>" + w + "</td>");
    }
}
function calendars() {
    for(var w = 1; w <= lastDayOfMonth.getDate(); w++) {
        if(w % 7 === 0) {
            document.getElementdByTagName("tbody").innerHTML += ("</tr><tr class=\"boi2\">");
        }
        document.getElementsByTagName("tbody").innerHTML += ("<td>" + w + "</td>");
    }
}