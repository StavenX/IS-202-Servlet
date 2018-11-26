function enable() {
    //gets all input fields
    var inputs = document.getElementsByClassName('module-input');
    
    //NOTE: if 'module-input' is not added as a class again, the function will
    //end up skipping every other item because of how js works
    for (var i = 0; i < inputs.length; i++) {
         //turns off disabled, and changes their class to give them another look through css
        inputs[i].disabled = false;
        inputs[i].setAttribute('class','module-input one-module-enabled');
    }
   //swaps the visibilities of the edit and save buttons
   document.getElementById('one-module-edit').style.display = 'none';
   document.getElementById('one-module-save').style.display = 'block';
}