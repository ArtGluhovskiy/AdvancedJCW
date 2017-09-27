$(document).ready(function () {

    var pat_name = /\b[a-z]{1,20}\b/i;
    var pat_email = /\b[a-z][\w.]+@[a-z]{2,7}.[a-z]{2,3}\b/i;
    var pat_birth = /(0[1-9]|1[0-9]|2[0-9]|3[01])-(0[1-9]|1[012])-[0-9]{4}/i;
    var pat_password = /[\S]{1,20}/i;

    $('input.input_login').blur(function () {
        checkLogin($(this));
    });

    $('input.input_fname').blur(function () {
        validate($(this), pat_name, 'first name');
    })

    $('input.input_lname').blur(function () {
        validate($(this), pat_name, 'last name');
    })

    $('input.input_date').blur(function () {
        validate($(this), pat_birth, 'birth date');
    })

    $('input.input_email').blur(function () {
        validate($(this), pat_email, 'email');
    })

    $('input.input_password').blur(function () {
        validate($(this), pat_password, 'password');
    })

    $('.dws-submit').click(function () {
        if ($('.place_error').text() === "") {
            $(this).submit();
        } else {
            // $('.place_error').html('You have some errors!');
            alert('Please, fill in the form correctly!')
            return false;
        }
    })
});

function validate(element, pattern, field) {
    var match = pattern.test(element.val());
    if (match == false) {
        $('.place_error').html('Invalid ' + field + '!');
    } else {
        $('.place_error').html('');
    }
}

function checkLogin(element) {
  var login = $(element).val();
  $.ajax({
      type: 'post',
      url: contextPath + '/frontController',
      data: {command:'checkLogin', login:login}
  }).done(function (data) {
      var info = JSON.parse(data);
      console.log('Done block = ' + info);
      if (info == 'FAIL') {
          $('.place_error').html('Such login already exists!');
          $('.dws-submit').attr('disabled', 'disabled');
      } else {
          $('.place_error').html('');
      }
  }).fail(function (data) {
      var info = JSON.parse(data);
      console.log('Error. Data: ' + info);
  })
}


