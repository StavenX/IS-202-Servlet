function enable() {
    //gets all input fields
    var inputs = document.getElementsByTagName('input');
       for (var i = 0; i < inputs.length; i++) {
            //checks if they're type 'text'
           if (inputs[i].type === 'text') {
                //turns off disabled, and changes their class to give them another look through css
               inputs[i].disabled = false;
               inputs[i].setAttribute('class','one-module-enabled');
           }
       }
   //swaps the visibilities of the edit and save buttons
   document.getElementById('one-module-edit').style.display = 'none';
   document.getElementById('one-module-save').style.display = 'block';
}