/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

function makeSure(id) {
    var items = document.getElementsByClassName('makesure-' + id);
    for (var i = 0; i < items.length; i++) {
        flip(items[i]);
    }
}

function flip(item) {
    if (item.style.display === 'inline-block') {
        item.style.display = 'none';
    }
    else {
        item.style.display = 'inline-block';
    }
}
