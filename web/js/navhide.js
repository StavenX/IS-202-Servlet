function hide() {
    cont = document.getElementById('nav-container').style;
    nav = document.getElementById('nav').style;
    button = document.getElementById('nav-button');
    temp = '';
    temp_button = '';

    if (nav.display === 'none') {
        nav.display = 'block';
        button.setAttribute('value', '<<');
        cont.background = temp;
       document.getElementsByClassName('page-container')[0].style.marginLeft = '90px';        
    } else {
       nav.display = 'none';
       button.setAttribute('value', '>>');
       temp = cont.background;
       cont.background = 'rgba(0,0,0,0)';
       document.getElementsByClassName('page-container')[0].style.marginLeft = '65px';
    }
}