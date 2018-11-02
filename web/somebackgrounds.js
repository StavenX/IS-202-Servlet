window.onload = changeColors();

function changeColors() {
    elements = document.getElementsByClassName('module_status');
    for (i = 0; i < elements.length; i++) {
        e = elements[i];
        ei = e.innerHTML;
        console.log(ei);
        switch(ei) {
            case 'Not delivered':   e.style.background = 'darksalmon';
                                    break;
                                    
            case 'Pending':   e.style.background = 'khaki';
                                    break;
                                    
            case 'Completed':       e.style.background = 'darkseagreen';
                                    break;
                                    
            default:                e.style.background = 'dodgeblue';
        }
    }
}

