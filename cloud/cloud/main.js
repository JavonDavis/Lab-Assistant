
// Use Parse.Cloud.define to define as many cloud functions as you want.
// For example:
Parse.Cloud.define("hello", function(request, response) {
  response.success("Hello world!");
});

Parse.Cloud.job("populate1161", function(request, status)
{
  var tokenurl = 'http://ourvle.mona.uwi.edu/login/token.php?username='+username+'&password='+password+'&service=moodle_mobile_app';
  console.log("Retrieving token to populate table");
  console.log(tokenurl);

  Parse.Cloud.httpRequest({
        method: 'GET',
        url: tokenurl
        }).then(function(httpResponse) {
          var message = httpResponse.text;
        //console.log(message);

        var object = JSON.parse(message);

        console.log("Got token:"+object.token);

        var token = object.token;
        var purl = 'http://ourvle.mona.uwi.edu/webservice/rest/server.php?wstoken='+token+'&wsfunction=moodle_user_get_users_by_courseid&moodlewsrestformat=json&courseid='+comp1161_id;

        console.log(purl);

        Parse.Cloud.httpRequest({
          method: 'GET',
          url: purl
          }).then(function(httpResponse) {
                var result = httpResponse.text;

                var participants = JSON.parse(result);
                var len = participants.length;
                var i = 0;
                for(; i< len; i++)
                {
                  var j = 0;
                  var participant = participants[i];
                  var students = [];

                  var roles = participant.roles;
                  var roles_len = roles.length;
                  for(; j<roles_len; j ++)
                  {
                    var role = roles[j];
                    if (role.roleid == 5)
                    {
                      
                      var Student = Parse.Object.extend("COMP1161_Grades");
                      var query = new Parse.Query(Student);

                      console.log("here:"+participant.idnumber);
                      query.equalTo("id_number", participant.idnumber);
                      query.first({
                        success: function(object) {
                          console.log("here");
                          if(typeof variable_here === 'undefined'){
                            var student = new Student();
                            student.set("id_number", participant.idnumber);
                            student.set("user_id", participant.id);
                            student.set("first_name", participant.firstname);
                            student.set("last_name", participant.lastname);
                            students.push(student);
                          }
                          else
                          {
                            object.set("user_id",participant.id);
                            object.set("first_name", participant.firstname);
                            object.set("last_name", participant.lastname);
                            students.push(object);
                          }

                          if(i == len - 1)
                          {
                            Parse.Object.saveAll(students, {
                              success: function(objs) {
                                  status.success("success");
                              },
                              error: function(error) { 
                                  status.error("error:"+error.code);
                              }
                          });
                          }
                          
                        },
                        error: function(error) {
                          console.log("Error: " + error.code + " " + error.message);
                        }
                      });
                    }
                  }
                }
          });
      });

});

Parse.Cloud.define("sendPushToUser", function(request, response) {
  //var senderUser = request.user;
  //var recipientUserId = request.params.recipientId;
  var message = request.params.message;

  // Validate that the sender is allowed to send to the recipient.
  // For example each user has an array of objectIds of friends
  // if (senderUser.get("friendIds").indexOf(recipientUserId) === -1) {
  //   response.error("The recipient is not the sender's friend, cannot send push.");
  // }

  // Validate the message text.
  // For example make sure it is under 140 characters
  // if (message.length > 140) {
  // // Truncate and add a ...
  //   message = message.substring(0, 137) + "...";
  // }

  // Send the push.
  // Find devices associated with the recipient user
  // var recipientUser = new Parse.User();
  // recipientUser.id = recipientUserId;
  // var pushQuery = new Parse.Query(Parse.Installation);
  // pushQuery.equalTo("user", recipientUser);

  var pushQuery = new Parse.Query(Parse.Installation);
 
  // Send the push notification to results of the query
  Parse.Push.send({
    where: pushQuery,
    data: {
      alert: message
    }
  }).then(function() {
      response.success("Push was sent successfully.")
  }, function(error) {
      response.error("Push failed to send with error: " + error.message);
  });
});

var test_lab_id = "3048";
var comp1126_id="380";
var lab_0_id="330";
var lab_3_id="3183";
var lab_2_id="541";
var lab_1_id="3150";
var lab_4_id="553";
var comp1161_id = "607";
var username="620068192";
var password="19950923";


Parse.Cloud.job("sendOffLab0Grades", function(request, status) {
  
  Parse.Cloud.useMasterKey();

  var tokenurl = 'http://ourvle.mona.uwi.edu/login/token.php?username='+username+'password='+password+'&service=moodle_mobile_app';
  console.log("Retrieving token to upload grades");

  Parse.Cloud.httpRequest({
        method: 'GET',
        url: tokenurl
        }).then(function(httpResponse) {
          var message = httpResponse.text;
        //console.log(message);

        var object = JSON.parse(message);

        console.log("Got token:"+object.token);

        var Student = Parse.Object.extend("COMP1126_Grades");
        var query = new Parse.Query(Student);
        var count = 0;

        console.log("Sending off lab 0 grades");

        var token = object.token;
        //var token = request.params.token;

        var lab_id = lab_0_id;
        
        query.limit(500);
        query.find({
          success: function(results) {
            //alert("Successfully retrieved " + results.length + " students.");
            // Do something with the returned Parse.Object values
            for (var i = 0; i < results.length; i++) {
              var object = results[i];
              var userid = object.get("user_id");
              var grade = object.get("lab_0");

              var url = "http://ourvle.mona.uwi.edu/webservice/rest/server.php?wstoken="+token+"&moodlewsrestformat=json&wsfunction=mod_assign_save_grade&assignmentid="+lab_id+"&userid="+userid+"&grade="+grade+"&attemptnumber=-1&addattempt=0&workflowstate=graded&applytoall=0&plugindata[assignfeedbackcomments_editor][text]=test&plugindata[assignfeedbackcomments_editor][format]=1&plugindata[files_filemanager]=0";
              Parse.Cloud.httpRequest({
                url: url
              }).always(function(httpResponse) {
                count++;
                if(count == 2)
                {
                  status.success("All grades uploaded");
                }
              });
              if(i==1)
              {
                break;
              }
            }
          },
          error: function(error) {
            alert("Error: " + error.code + " " + error.message);
            status.error("query failed");
          }
        });
        
      }, function(httpResponse) {
        console.error('Request failed with response code ' + httpResponse.status);
      });
  
});

Parse.Cloud.job("sendOffLab1Grades", function(request, status) {
  
  Parse.Cloud.useMasterKey();

  var tokenurl = 'http://ourvle.mona.uwi.edu/login/token.php?username='+username+'password='+password+'&service=moodle_mobile_app';
  
  console.log("Retrieving token to upload grades");

  Parse.Cloud.httpRequest({
        method: 'GET',
        url: tokenurl
        }).then(function(httpResponse) {
          var message = httpResponse.text;
        //console.log(message);

        var object = JSON.parse(message);

        console.log("Got token:"+object.token);

        var Student = Parse.Object.extend("COMP1126_Grades");
        var query = new Parse.Query(Student);
        var count = 0;

        console.log("Sending off lab 1 grades");

        var token = object.token;
        //var token = request.params.token;

        var lab_id = lab_1_id;
        
        query.limit(500);
        query.find({
          success: function(results) {
            //alert("Successfully retrieved " + results.length + " students.");
            // Do something with the returned Parse.Object values
            for (var i = 0; i < results.length; i++) {
              var object = results[i];
              var userid = object.get("user_id");
              var grade = object.get("lab_1");

              var url = "http://ourvle.mona.uwi.edu/webservice/rest/server.php?wstoken="+token+"&moodlewsrestformat=json&wsfunction=mod_assign_save_grade&assignmentid="+lab_id+"&userid="+userid+"&grade="+grade+"&attemptnumber=-1&addattempt=0&workflowstate=graded&applytoall=0&plugindata[assignfeedbackcomments_editor][text]=test&plugindata[assignfeedbackcomments_editor][format]=1&plugindata[files_filemanager]=0";
              Parse.Cloud.httpRequest({
                url: url
              }).always(function(httpResponse) {
                count++;
                if(count == 2)
                {
                  status.success("All grades uploaded");
                }

              });
              if(i==1)
              {
                break;
              }
            }
          },
          error: function(error) {
            alert("Error: " + error.code + " " + error.message);
            status.error("query failed");
          }
        });
        
      }, function(httpResponse) {
        console.error('Request failed with response code ' + httpResponse.status);
      });

  
});

Parse.Cloud.job("sendOffLab2Grades", function(request, status) {
  
  Parse.Cloud.useMasterKey();

  var tokenurl = 'http://ourvle.mona.uwi.edu/login/token.php?username='+username+'password='+password+'&service=moodle_mobile_app';
  console.log("Retrieving token to upload grades");

  Parse.Cloud.httpRequest({
        method: 'GET',
        url: tokenurl
        }).then(function(httpResponse) {
          var message = httpResponse.text;
        //console.log(message);

        var object = JSON.parse(message);

        console.log("Got token:"+object.token);

        var Student = Parse.Object.extend("COMP1126_Grades");
        var query = new Parse.Query(Student);
        var count = 0;

        console.log("Sending off lab 2 grades");

        var token = object.token;
        //var token = request.params.token;

        var lab_id = lab_2_id;
        
        query.limit(500);
        query.find({
          success: function(results) {
            //alert("Successfully retrieved " + results.length + " students.");
            // Do something with the returned Parse.Object values
            for (var i = 0; i < results.length; i++) {
              var object = results[i];
              var userid = object.get("user_id");
              var grade = object.get("lab_2");

              var url = "http://ourvle.mona.uwi.edu/webservice/rest/server.php?wstoken="+token+"&moodlewsrestformat=json&wsfunction=mod_assign_save_grade&assignmentid="+lab_id+"&userid="+userid+"&grade="+grade+"&attemptnumber=-1&addattempt=0&workflowstate=graded&applytoall=0&plugindata[assignfeedbackcomments_editor][text]=test&plugindata[assignfeedbackcomments_editor][format]=1&plugindata[files_filemanager]=0";
              Parse.Cloud.httpRequest({
                url: url
              }).always(function(httpResponse) {
                count++;
                if(count == 2)
                {
                  status.success("All grades uploaded");
                }

              });
              if(i==1)
              {
                break;
              }
            }
          },
          error: function(error) {
            alert("Error: " + error.code + " " + error.message);
            status.error("query failed");
          }
        });
        
      }, function(httpResponse) {
        console.error('Request failed with response code ' + httpResponse.status);
      });
  
});

Parse.Cloud.job("sendOffLab3Grades", function(request, status) {
  
  Parse.Cloud.useMasterKey();

  var tokenurl = 'http://ourvle.mona.uwi.edu/login/token.php?username='+username+'password='+password+'&service=moodle_mobile_app';
  console.log("Retrieving token to upload grades");

  Parse.Cloud.httpRequest({
        method: 'GET',
        url: tokenurl
        }).then(function(httpResponse) {
          var message = httpResponse.text;
        //console.log(message);

        var object = JSON.parse(message);

        console.log("Got token:"+object.token);

        var Student = Parse.Object.extend("COMP1126_Grades");
        var query = new Parse.Query(Student);
        var count = 0;

        console.log("Sending off lab 3 grades");

        var token = object.token;
        //var token = request.params.token;

        var lab_id = lab_3_id;
        
        query.limit(500);
        query.find({
          success: function(results) {
            //alert("Successfully retrieved " + results.length + " students.");
            // Do something with the returned Parse.Object values
            for (var i = 0; i < results.length; i++) {
              var object = results[i];
              var userid = object.get("user_id");
              var grade = object.get("lab_3");

              var url = "http://ourvle.mona.uwi.edu/webservice/rest/server.php?wstoken="+token+"&moodlewsrestformat=json&wsfunction=mod_assign_save_grade&assignmentid="+lab_id+"&userid="+userid+"&grade="+grade+"&attemptnumber=-1&addattempt=0&workflowstate=graded&applytoall=0&plugindata[assignfeedbackcomments_editor][text]=test&plugindata[assignfeedbackcomments_editor][format]=1&plugindata[files_filemanager]=0";
              Parse.Cloud.httpRequest({
                url: url
              }).always(function(httpResponse) {
                count++;
                if(count == 2)
                {
                  status.success("All grades uploaded");
                }



              });
              if(i==1)
              {
                break;
              }
            }
          },
          error: function(error) {
            alert("Error: " + error.code + " " + error.message);
            status.error("query failed");
          }
        });
        
      }, function(httpResponse) {
        console.error('Request failed with response code ' + httpResponse.status);
      });

  
});

Parse.Cloud.job("sendOffLab4Grades", function(request, status) {
  
  Parse.Cloud.useMasterKey();

  var tokenurl = 'http://ourvle.mona.uwi.edu/login/token.php?username='+username+'password='+password+'&service=moodle_mobile_app';
  console.log("Retrieving token to upload grades");

  Parse.Cloud.httpRequest({
        method: 'GET',
        url: tokenurl
        }).then(function(httpResponse) {
          var message = httpResponse.text;
        //console.log(message);

        var object = JSON.parse(message);

        console.log("Got token:"+object.token);

        var Student = Parse.Object.extend("COMP1126_Grades");
        var query = new Parse.Query(Student);
        var count = 0;

        console.log("Sending off lab 4 grades");

        var token = object.token;
        //var token = request.params.token;

        var lab_id = lab_4_id;
        
        query.limit(500);
        query.find({
          success: function(results) {
            //alert("Successfully retrieved " + results.length + " students.");
            // Do something with the returned Parse.Object values
            for (var i = 0; i < results.length; i++) {
              var object = results[i];
              var userid = object.get("user_id");
              var grade = object.get("lab_4");

              var url = "http://ourvle.mona.uwi.edu/webservice/rest/server.php?wstoken="+token+"&moodlewsrestformat=json&wsfunction=mod_assign_save_grade&assignmentid="+lab_id+"&userid="+userid+"&grade="+grade+"&attemptnumber=-1&addattempt=0&workflowstate=graded&applytoall=0&plugindata[assignfeedbackcomments_editor][text]=test&plugindata[assignfeedbackcomments_editor][format]=1&plugindata[files_filemanager]=0";
              Parse.Cloud.httpRequest({
                url: url
              }).always(function(httpResponse) {
                count++;
                if(count == 2)
                {
                  status.success("All grades uploaded");
                }
              });
              if(i==1)
              {
                break;
              }
            }
          },
          error: function(error) {
            alert("Error: " + error.code + " " + error.message);
            status.error("query failed");
          }
        });
        
      }, function(httpResponse) {
        console.error('Request failed with response code ' + httpResponse.status);
      });

  
});
