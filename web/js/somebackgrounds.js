changeColors();

function changeColors() {
    statusFields = document.getElementsByClassName('module-status');
    for (i = 0; i < statusFields.length; i++) {
        statusField = statusFields[i];
        
        statusText = statusField.innerHTML;
        parent = statusField.parentElement;
        for (j = 0; j < 1; j++) {
            
            switch(statusText) {
                case 'Not delivered':   parent.style.background = 'darksalmon';
                                        break;

                case 'Pending':   parent.style.background = 'khaki';
                                        break;

                case 'Completed':       parent.style.background = 'darkseagreen';
                                        break;

                default:                parent.style.background = 'dodgeblue';
            }
        }
    }
}

