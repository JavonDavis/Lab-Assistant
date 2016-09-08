module.exports.getToken = function()
{
  var tokenurl = 'http://ourvle.mona.uwi.edu/login/token.php?username=620068192&password=19950923&service=moodle_mobile_app';
  
  console.log(tokenurl);

  Parse.Cloud.httpRequest({
        method: 'GET',
        url: tokenurl
        }).then(function(httpResponse) {
          var message = httpResponse.text;
        //console.log(message);

        var object = JSON.parse(message);

        console.log(object.token);
        return object.token;
      }, function(httpResponse) {
        console.error('Request failed with response code ' + httpResponse.status);
      });
        console.log("done");
}