/*
* Middleman between Android Application and Moodle.
* @author Javon Davis 20/1/2016
* Job names are verbose. updateX updates students doing course X
* populateX populates an empty table for students doing course X
* send off methods uploads grades to Moodle for the various labs
*/

//course ids
var comp1126_id="380";
var comp1127_id="381";
var comp1161_id = "607";


//COMP1126 lab ids
var comp_1126_lab_0_id="330";
var comp_1126_lab_3_id="3183";
var comp_1126_lab_2_id="541";
var comp_1126_lab_1_id="3150";
var comp_1126_lab_4_id ="553";

//creditials to upload grades
var username="620068192";
var password="19950923";

//job to update 1161 table
Parse.Cloud.job("update1161", function(request, status)
{
  var tokenurl = 'http://ourvle.mona.uwi.edu/login/token.php?username='+username+'&password='+password+'&service=moodle_mobile_app';
  console.log("Retrieving token to populate table");
  console.log(tokenurl);

  Parse.Cloud.httpRequest({
        method: 'GET',
        url: tokenurl
        }).then(function(httpResponse) {
          var message = httpResponse.text;

        var object = JSON.parse(message);

        var token = object.token;
        var purl = 'http://ourvle.mona.uwi.edu/webservice/rest/server.php?wstoken='+token+'&wsfunction=moodle_user_get_users_by_courseid&moodlewsrestformat=json&courseid='+comp1161_id;

        console.log(purl);

        Parse.Cloud.httpRequest({
          method: 'GET',
          url: purl
          }).then(function(httpResponse) {
                var result = httpResponse.text;

                var participants = JSON.parse(result);
                      var Student = Parse.Object.extend("COMP1161_Grades");
                      var query = new Parse.Query(Student);
                      query.limit(500);
                      query.find({
                        success: function(objects) {
                          console.log("here");
                          var len = participants.length;
                          var objects_len = objects.length;
                          var students = [];
                          var i = 0;
                          for(; i< len; i++)
                          {
                            var j = 0;
                            var participant = participants[i];

                            var roles = participant.roles;
                            var roles_len = roles.length;
                            
                            for(; j<roles_len; j ++)
                            {
                              var role = roles[j];
                              if (role.roleid == 5)
                              {
                                var k = 0;
                                for(; k<objects_len; k++)
                                {
                                  var student = objects[k];
                                  console.log(student.get('id_number')+"-"+participant.idnumber);
                                  var id_number = participant.idnumber;

                                  if(student.get("id_number") == id_number)
                                  {
                                    console.log("comparison done")
                                    student.set("id_number", participant.idnumber);
                                    student.set("user_id", participant.id+"");
                                    student.set("first_name", participant.firstname);
                                    student.set("last_name", participant.lastname);
                                    students.push(student);       
                                  }

                                }
                              }
                            }

                          }

                          Parse.Object.saveAll(students)
                   .then(function() {
                    status.success("ran to completion:"+students.length);
                    }, function(error) {
                        status.error("failure"+error.code+"-"+error.message);
                });
              },
              error: function(error) {
                console.log("Error: " + error.code + " " + error.message);
                status.error("error");
              }
            });
          });
      });

});

//job to populate the 1161 table
Parse.Cloud.job("populate1161", function(request, status)
{
  var tokenurl = 'http://ourvle.mona.uwi.edu/login/token.php?username='+username+'&password='+password+'&service=moodle_mobile_app';
  console.log("Retrieving token to populate table");

  Parse.Cloud.httpRequest({
        method: 'GET',
        url: tokenurl
        }).then(function(httpResponse) {
          var message = httpResponse.text;
          var object = JSON.parse(message);

        var token = object.token;
        var purl = 'http://ourvle.mona.uwi.edu/webservice/rest/server.php?wstoken='+token+'&wsfunction=moodle_user_get_users_by_courseid&moodlewsrestformat=json&courseid='+comp1161_id;

        Parse.Cloud.httpRequest({
          method: 'GET',
          url: purl
          }).then(function(httpResponse) {
                var result = httpResponse.text;

                var participants = JSON.parse(result);
                var len = participants.length;
                var students = [];
                var i = 0;
                for(; i< len; i++)
                {
                  var j = 0;
                  var participant = participants[i];

                  var roles = participant.roles;
                  var roles_len = roles.length;
                  
                  for(; j<roles_len; j ++)
                  {
                    var role = roles[j];
                    if (role.roleid == 5)
                    {
                      var Student = Parse.Object.extend("COMP1161_Grades");
                      var student = new Student();
                      student.set("id_number", participant.idnumber);
                      student.set("user_id", participant.id+"");
                      student.set("first_name", participant.firstname);
                      student.set("last_name", participant.lastname);
                      students.push(student);                
                    }
                  }
                }

                Parse.Object.saveAll(students)
                .then(function() {
                    status.success("ran to completion:"+students.length);
                }, function(error) {
                    status.success("failure"+error.code+"-"+error.message);
                });
                
          });
      });

});

Parse.Cloud.job("update1127", function(request, status)
{
  var tokenurl = 'http://ourvle.mona.uwi.edu/login/token.php?username='+username+'&password='+password+'&service=moodle_mobile_app';
  console.log("Retrieving token to populate table");

  Parse.Cloud.httpRequest({
        method: 'GET',
        url: tokenurl
        }).then(function(httpResponse) {
          var message = httpResponse.text;
        var object = JSON.parse(message);

        var token = object.token;
        var purl = 'http://ourvle.mona.uwi.edu/webservice/rest/server.php?wstoken='+token+'&wsfunction=moodle_user_get_users_by_courseid&moodlewsrestformat=json&courseid='+comp1127_id;

        console.log(purl);

        Parse.Cloud.httpRequest({
          method: 'GET',
          url: purl
          }).then(function(httpResponse) {
                var result = httpResponse.text;

                var participants = JSON.parse(result);
                      var Student = Parse.Object.extend("COMP1127_Grades");
                      var query = new Parse.Query(Student);
                      query.limit(500);
                      query.find({
                        success: function(objects) {
                          var len = participants.length;
                          var objects_len = objects.length;
                          var students = [];
                          var i = 0;
                          for(; i< len; i++)
                          {
                            var j = 0;
                            var participant = participants[i];

                            var roles = participant.roles;
                            var roles_len = roles.length;
                            
                            for(; j<roles_len; j ++)
                            {
                              var role = roles[j];
                              if (role.roleid == 5)
                              {
                                var k = 0;
                                for(; k<objects_len; k++)
                                {
                                  var student = objects[k];
                                  var id_number = participant.idnumber;

                                  if(student.get("id_number") == id_number)
                                  {
                                    console.log("comparison done")
                                    student.set("id_number", participant.idnumber);
                                    student.set("user_id", participant.id+"");
                                    student.set("first_name", participant.firstname);
                                    student.set("last_name", participant.lastname);
                                    students.push(student);       
                                  }

                                }
                              }
                            }

                          }

                          Parse.Object.saveAll(students)
                   .then(function() {
                    status.success("ran to completion:"+students.length);
                    }, function(error) {
                        status.error("failure"+error.code+"-"+error.message);
                });
              },
              error: function(error) {
                console.log("Error: " + error.code + " " + error.message);
                status.error("error");
              }
            });
          });
      });

});

Parse.Cloud.job("populate1127", function(request, status)
{
  var tokenurl = 'http://ourvle.mona.uwi.edu/login/token.php?username='+username+'&password='+password+'&service=moodle_mobile_app';

  Parse.Cloud.httpRequest({
        method: 'GET',
        url: tokenurl
        }).then(function(httpResponse) {
          var message = httpResponse.text;
          var object = JSON.parse(message);

        var token = object.token;
        var purl = 'http://ourvle.mona.uwi.edu/webservice/rest/server.php?wstoken='+token+'&wsfunction=moodle_user_get_users_by_courseid&moodlewsrestformat=json&courseid='+comp1127_id;

        console.log(purl);

        Parse.Cloud.httpRequest({
          method: 'GET',
          url: purl
          }).then(function(httpResponse) {
                var result = httpResponse.text;

                var participants = JSON.parse(result);
                var len = participants.length;
                var students = [];
                var i = 0;
                for(; i< len; i++)
                {
                  var j = 0;
                  var participant = participants[i];

                  var roles = participant.roles;
                  var roles_len = roles.length;
                  
                  for(; j<roles_len; j ++)
                  {
                    var role = roles[j];
                    if (role.roleid == 5)
                    {
                      var Student = Parse.Object.extend("COMP1127_Grades");
                      var student = new Student();
                      student.set("id_number", participant.idnumber);
                      student.set("user_id", participant.id+"");
                      student.set("first_name", participant.firstname);
                      student.set("last_name", participant.lastname);
                      students.push(student);                
                    }
                  }
                }

                Parse.Object.saveAll(students)
                .then(function() {
                    status.success("ran to completion:"+students.length);
                }, function(error) {
                    status.success("failure"+error.code+"-"+error.message);
                });
                
          });
      });

});

Parse.Cloud.job("update1126", function(request, status)
{
  var tokenurl = 'http://ourvle.mona.uwi.edu/login/token.php?username='+username+'&password='+password+'&service=moodle_mobile_app';
  console.log("Retrieving token to populate table");
  console.log(tokenurl);

  Parse.Cloud.httpRequest({
        method: 'GET',
        url: tokenurl
        }).then(function(httpResponse) {
          var message = httpResponse.text;

        var object = JSON.parse(message);

        var token = object.token;
        var purl = 'http://ourvle.mona.uwi.edu/webservice/rest/server.php?wstoken='+token+'&wsfunction=moodle_user_get_users_by_courseid&moodlewsrestformat=json&courseid='+comp1126_id;

        console.log(purl);

        Parse.Cloud.httpRequest({
          method: 'GET',
          url: purl
          }).then(function(httpResponse) {
                var result = httpResponse.text;

                var participants = JSON.parse(result);
                     console.log("role"); 
                      var Student = Parse.Object.extend("COMP1126_Grades");
                      var query = new Parse.Query(Student);
                      query.limit(500);

                      query.find({
                        success: function(objects) {
                          var len = participants.length;
                          var objects_len = objects.length;
                          var students = [];
                          var i = 0;
                          for(; i< len; i++)
                          {
                            var j = 0;
                            var participant = participants[i];

                            var roles = participant.roles;
                            var roles_len = roles.length;
                            
                            for(; j<roles_len; j ++)
                            {
                              var role = roles[j];
                              if (role.roleid == 5)
                              {
                                var k = 0;
                                for(; k<objects_len; k++)
                                {
                                  var student = objects[k];
                                  console.log(student.get('id_number')+"-"+participant.idnumber);
                                  var id_number = participant.idnumber;

                                  if(student.get("id_number") == id_number)
                                  {
                                    console.log("comparison done")
                                    student.set("id_number", participant.idnumber);
                                    student.set("user_id", participant.id+"");
                                    student.set("first_name", participant.firstname);
                                    student.set("last_name", participant.lastname);
                                    students.push(student);       
                                  }

                                }
                              }
                            }

                          }

                          Parse.Object.saveAll(students)
                        .then(function() {
                            status.success("ran to completion:"+students.length);
                        }, function(error) {
                            status.error("failure"+error.code+"-"+error.message);
                        });
              },
              error: function(error) {
                console.log("Error: " + error.code + " " + error.message);
                status.error("error");
              }
            });
          });
      });

});

Parse.Cloud.job("populate1126", function(request, status)
{
  var tokenurl = 'http://ourvle.mona.uwi.edu/login/token.php?username='+username+'&password='+password+'&service=moodle_mobile_app';

  Parse.Cloud.httpRequest({
        method: 'GET',
        url: tokenurl
        }).then(function(httpResponse) {
          var message = httpResponse.text;

        var object = JSON.parse(message);

        var token = object.token;
        var purl = 'http://ourvle.mona.uwi.edu/webservice/rest/server.php?wstoken='+token+'&wsfunction=moodle_user_get_users_by_courseid&moodlewsrestformat=json&courseid='+comp1126_id;

        Parse.Cloud.httpRequest({
          method: 'GET',
          url: purl
          }).then(function(httpResponse) {
                var result = httpResponse.text;

                var participants = JSON.parse(result);
                var len = participants.length;
                var students = [];
                var i = 0;
                for(; i< len; i++)
                {
                  var j = 0;
                  var participant = participants[i];

                  var roles = participant.roles;
                  var roles_len = roles.length;
                  
                  for(; j<roles_len; j ++)
                  {
                    var role = roles[j];
                    if (role.roleid == 5)
                    {
                      var Student = Parse.Object.extend("COMP1126_Grades");
                      var student = new Student();
                      student.set("id_number", participant.idnumber);
                      student.set("user_id", participant.id+"");
                      student.set("first_name", participant.firstname);
                      student.set("last_name", participant.lastname);
                      students.push(student);                
                    }
                  }
                }

                Parse.Object.saveAll(students)
                .then(function() {
                    status.success("ran to completion:"+students.length);
                }, function(error) {
                    status.success("failure"+error.code+"-"+error.message);
                });
                
          });
      });

});

Parse.Cloud.job("sendOff1126Lab0Grades", function(request, status) {
  
  Parse.Cloud.useMasterKey();

  var tokenurl = 'http://ourvle.mona.uwi.edu/login/token.php?username='+username+'&password='+password+'&service=moodle_mobile_app';
 
  Parse.Cloud.httpRequest({
        method: 'GET',
        url: tokenurl
        }).then(function(httpResponse) {
          var message = httpResponse.text;

        var object = JSON.parse(message);

        var Student = Parse.Object.extend("COMP1126_Grades");
        var query = new Parse.Query(Student);
        var token = object.token;

        var lab_id = comp_1126_lab_0_id;
        
        query.limit(500);
        query.find({
          success: function(results) {
            //alert("Successfully retrieved " + results.length + " students.");
            // Do something with the returned Parse.Object values
            for (var i = 0; i < results.length; i++) {
              var object = results[i];
              var userid = object.get("user_id");
              var grade = object.get("lab_0");
              if(typeof grade != 'undefined')
              {
                var url = "http://ourvle.mona.uwi.edu/webservice/rest/server.php?wstoken="+token+"&moodlewsrestformat=json&wsfunction=mod_assign_save_grade&assignmentid="+lab_id+"&userid="+userid+"&grade="+grade+"&attemptnumber=-1&addattempt=0&workflowstate=graded&applytoall=0&plugindata[assignfeedbackcomments_editor][text]=test&plugindata[assignfeedbackcomments_editor][format]=1&plugindata[files_filemanager]=0";

                Parse.Cloud.httpRequest({
                  url: url
                }).always(function(httpResponse) {
                  status.success("All grades uploaded");
                });
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

Parse.Cloud.job("sendOff1126Lab1Grades", function(request, status) {
  
  Parse.Cloud.useMasterKey();

  var tokenurl = 'http://ourvle.mona.uwi.edu/login/token.php?username='+username+'&password='+password+'&service=moodle_mobile_app';
 
  Parse.Cloud.httpRequest({
        method: 'GET',
        url: tokenurl
        }).then(function(httpResponse) {
          var message = httpResponse.text;

        var object = JSON.parse(message);

        var Student = Parse.Object.extend("COMP1126_Grades");
        var query = new Parse.Query(Student);
        var token = object.token;

        var lab_id = comp_1126_lab_1_id;
        
        query.limit(500);
        query.find({
          success: function(results) {
            for (var i = 0; i < results.length; i++) {
              var object = results[i];
              var userid = object.get("user_id");
              var grade = object.get("lab_1");
              if(typeof grade != 'undefined')
              {
                var url = "http://ourvle.mona.uwi.edu/webservice/rest/server.php?wstoken="+token+"&moodlewsrestformat=json&wsfunction=mod_assign_save_grade&assignmentid="+lab_id+"&userid="+userid+"&grade="+grade+"&attemptnumber=-1&addattempt=0&workflowstate=graded&applytoall=0&plugindata[assignfeedbackcomments_editor][text]=test&plugindata[assignfeedbackcomments_editor][format]=1&plugindata[files_filemanager]=0";

                Parse.Cloud.httpRequest({
                  url: url
                }).always(function(httpResponse) {
                  status.success("All grades uploaded");
                });
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

Parse.Cloud.job("sendOff1126Lab2Grades", function(request, status) {
  
  Parse.Cloud.useMasterKey();

  var tokenurl = 'http://ourvle.mona.uwi.edu/login/token.php?username='+username+'&password='+password+'&service=moodle_mobile_app';
 
  Parse.Cloud.httpRequest({
        method: 'GET',
        url: tokenurl
        }).then(function(httpResponse) {
          var message = httpResponse.text;

        var object = JSON.parse(message);

        var Student = Parse.Object.extend("COMP1126_Grades");
        var query = new Parse.Query(Student);
        var token = object.token;

        var lab_id = comp_1126_lab_2_id;
        
        query.limit(500);
        query.find({
          success: function(results) {
            for (var i = 0; i < results.length; i++) {
              var object = results[i];
              var userid = object.get("user_id");
              var grade = object.get("lab_2");
              if(typeof grade != 'undefined')
              {
              var url = "http://ourvle.mona.uwi.edu/webservice/rest/server.php?wstoken="+token+"&moodlewsrestformat=json&wsfunction=mod_assign_save_grade&assignmentid="+lab_id+"&userid="+userid+"&grade="+grade+"&attemptnumber=-1&addattempt=0&workflowstate=graded&applytoall=0&plugindata[assignfeedbackcomments_editor][text]=test&plugindata[assignfeedbackcomments_editor][format]=1&plugindata[files_filemanager]=0";
              console.log(url);
              Parse.Cloud.httpRequest({
                url: url
              }).always(function(httpResponse) {
                status.success("All grades uploaded");
              });
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

Parse.Cloud.job("sendOff1126Lab3Grades", function(request, status) {
  
  Parse.Cloud.useMasterKey();

  var tokenurl = 'http://ourvle.mona.uwi.edu/login/token.php?username='+username+'&password='+password+'&service=moodle_mobile_app';
 
  Parse.Cloud.httpRequest({
        method: 'GET',
        url: tokenurl
        }).then(function(httpResponse) {
          var message = httpResponse.text;

        var object = JSON.parse(message);

        var Student = Parse.Object.extend("COMP1126_Grades");
        var query = new Parse.Query(Student);
        var token = object.token;

        var lab_id = comp_1126_lab_3_id;
        
        query.limit(500);
        query.find({
          success: function(results) {
            //alert("Successfully retrieved " + results.length + " students.");
            // Do something with the returned Parse.Object values
            for (var i = 0; i < results.length; i++) {
              var object = results[i];
              var userid = object.get("user_id");
              var grade = object.get("lab_3");
              if(typeof grade != 'undefined')
              {
                var url = "http://ourvle.mona.uwi.edu/webservice/rest/server.php?wstoken="+token+"&moodlewsrestformat=json&wsfunction=mod_assign_save_grade&assignmentid="+lab_id+"&userid="+userid+"&grade="+grade+"&attemptnumber=-1&addattempt=0&workflowstate=graded&applytoall=0&plugindata[assignfeedbackcomments_editor][text]=test&plugindata[assignfeedbackcomments_editor][format]=1&plugindata[files_filemanager]=0";
              
                Parse.Cloud.httpRequest({
                  url: url
                }).always(function(httpResponse) {
                  console.log("msg2:"+httpResponse.text);
                  status.success("All grades uploaded");
                });
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

Parse.Cloud.job("sendOff1126Lab4Grades", function(request, status) {
  
  Parse.Cloud.useMasterKey();

  var tokenurl = 'http://ourvle.mona.uwi.edu/login/token.php?username='+username+'&password='+password+'&service=moodle_mobile_app';
 
  Parse.Cloud.httpRequest({
        method: 'GET',
        url: tokenurl
        }).then(function(httpResponse) {
          var message = httpResponse.text;

        var object = JSON.parse(message);

        var Student = Parse.Object.extend("COMP1126_Grades");
        var query = new Parse.Query(Student);
        var token = object.token;

        var lab_id = comp_1126_lab_4_id;
        
        query.limit(500);
        query.find({
          success: function(results) {
            //alert("Successfully retrieved " + results.length + " students.");
            // Do something with the returned Parse.Object values
            for (var i = 0; i < results.length; i++) {
              var object = results[i];
              var userid = object.get("user_id");
              var grade = object.get("lab_4");
              if(typeof grade != 'undefined')
              {
                var url = "http://ourvle.mona.uwi.edu/webservice/rest/server.php?wstoken="+token+"&moodlewsrestformat=json&wsfunction=mod_assign_save_grade&assignmentid="+lab_id+"&userid="+userid+"&grade="+grade+"&attemptnumber=-1&addattempt=0&workflowstate=graded&applytoall=0&plugindata[assignfeedbackcomments_editor][text]=test&plugindata[assignfeedbackcomments_editor][format]=1&plugindata[files_filemanager]=0";
              
                Parse.Cloud.httpRequest({
                  url: url
                }).always(function(httpResponse) {
                  status.success("All grades uploaded");
                });
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

Parse.Cloud.job("sendOff1127Lab0Grades", function(request, status) {
  
  Parse.Cloud.useMasterKey();

  var tokenurl = 'http://ourvle.mona.uwi.edu/login/token.php?username='+username+'&password='+password+'&service=moodle_mobile_app';
 
  Parse.Cloud.httpRequest({
        method: 'GET',
        url: tokenurl
        }).then(function(httpResponse) {
          var message = httpResponse.text;

        var object = JSON.parse(message);

        var Student = Parse.Object.extend("COMP1127_Grades");
        var query = new Parse.Query(Student);
        var token = object.token;

        var lab_id = comp_1127_lab_0_id;
        
        query.limit(500);
        query.find({
          success: function(results) {
            //alert("Successfully retrieved " + results.length + " students.");
            // Do something with the returned Parse.Object values
            for (var i = 0; i < results.length; i++) {
              var object = results[i];
              var userid = object.get("user_id");
              var grade = object.get("lab_0");
              if(typeof grade != 'undefined')
              {
                var url = "http://ourvle.mona.uwi.edu/webservice/rest/server.php?wstoken="+token+"&moodlewsrestformat=json&wsfunction=mod_assign_save_grade&assignmentid="+lab_id+"&userid="+userid+"&grade="+grade+"&attemptnumber=-1&addattempt=0&workflowstate=graded&applytoall=0&plugindata[assignfeedbackcomments_editor][text]=test&plugindata[assignfeedbackcomments_editor][format]=1&plugindata[files_filemanager]=0";

                Parse.Cloud.httpRequest({
                  url: url
                }).always(function(httpResponse) {
                  status.success("All grades uploaded");
                });
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

Parse.Cloud.job("sendOff1127Lab1Grades", function(request, status) {
  
  Parse.Cloud.useMasterKey();

  var tokenurl = 'http://ourvle.mona.uwi.edu/login/token.php?username='+username+'&password='+password+'&service=moodle_mobile_app';
 
  Parse.Cloud.httpRequest({
        method: 'GET',
        url: tokenurl
        }).then(function(httpResponse) {
          var message = httpResponse.text;

        var object = JSON.parse(message);

        var Student = Parse.Object.extend("COMP1127_Grades");
        var query = new Parse.Query(Student);
        var token = object.token;

        var lab_id = lab_1_id;
        
        query.limit(500);
        query.find({
          success: function(results) {
            for (var i = 0; i < results.length; i++) {
              var object = results[i];
              var userid = object.get("user_id");
              var grade = object.get("lab_1");
              if(typeof grade != 'undefined')
              {
                var url = "http://ourvle.mona.uwi.edu/webservice/rest/server.php?wstoken="+token+"&moodlewsrestformat=json&wsfunction=mod_assign_save_grade&assignmentid="+lab_id+"&userid="+userid+"&grade="+grade+"&attemptnumber=-1&addattempt=0&workflowstate=graded&applytoall=0&plugindata[assignfeedbackcomments_editor][text]=test&plugindata[assignfeedbackcomments_editor][format]=1&plugindata[files_filemanager]=0";

                Parse.Cloud.httpRequest({
                  url: url
                }).always(function(httpResponse) {
                  status.success("All grades uploaded");
                });
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

Parse.Cloud.job("sendOff1127Lab2Grades", function(request, status) {
  
  Parse.Cloud.useMasterKey();

  var tokenurl = 'http://ourvle.mona.uwi.edu/login/token.php?username='+username+'&password='+password+'&service=moodle_mobile_app';
 
  Parse.Cloud.httpRequest({
        method: 'GET',
        url: tokenurl
        }).then(function(httpResponse) {
          var message = httpResponse.text;

        var object = JSON.parse(message);

        var Student = Parse.Object.extend("COMP1127_Grades");
        var query = new Parse.Query(Student);
        var token = object.token;

        var lab_id = comp_1127_lab_2_id;
        
        query.limit(500);
        query.find({
          success: function(results) {
            for (var i = 0; i < results.length; i++) {
              var object = results[i];
              var userid = object.get("user_id");
              var grade = object.get("lab_2");
              if(typeof grade != 'undefined')
              {
              var url = "http://ourvle.mona.uwi.edu/webservice/rest/server.php?wstoken="+token+"&moodlewsrestformat=json&wsfunction=mod_assign_save_grade&assignmentid="+lab_id+"&userid="+userid+"&grade="+grade+"&attemptnumber=-1&addattempt=0&workflowstate=graded&applytoall=0&plugindata[assignfeedbackcomments_editor][text]=test&plugindata[assignfeedbackcomments_editor][format]=1&plugindata[files_filemanager]=0";
              console.log(url);
              Parse.Cloud.httpRequest({
                url: url
              }).always(function(httpResponse) {
                status.success("All grades uploaded");
              });
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

Parse.Cloud.job("sendOff1127Lab3Grades", function(request, status) {
  
  Parse.Cloud.useMasterKey();

  var tokenurl = 'http://ourvle.mona.uwi.edu/login/token.php?username='+username+'&password='+password+'&service=moodle_mobile_app';
 
  Parse.Cloud.httpRequest({
        method: 'GET',
        url: tokenurl
        }).then(function(httpResponse) {
          var message = httpResponse.text;

        var object = JSON.parse(message);

        var Student = Parse.Object.extend("COMP1127_Grades");
        var query = new Parse.Query(Student);
        var token = object.token;

        var lab_id = comp_1127_lab_3_id;
        
        query.limit(500);
        query.find({
          success: function(results) {
            //alert("Successfully retrieved " + results.length + " students.");
            // Do something with the returned Parse.Object values
            for (var i = 0; i < results.length; i++) {
              var object = results[i];
              var userid = object.get("user_id");
              var grade = object.get("lab_3");
              if(typeof grade != 'undefined')
              {
                var url = "http://ourvle.mona.uwi.edu/webservice/rest/server.php?wstoken="+token+"&moodlewsrestformat=json&wsfunction=mod_assign_save_grade&assignmentid="+lab_id+"&userid="+userid+"&grade="+grade+"&attemptnumber=-1&addattempt=0&workflowstate=graded&applytoall=0&plugindata[assignfeedbackcomments_editor][text]=test&plugindata[assignfeedbackcomments_editor][format]=1&plugindata[files_filemanager]=0";
              
                Parse.Cloud.httpRequest({
                  url: url
                }).always(function(httpResponse) {
                  console.log("msg2:"+httpResponse.text);
                  status.success("All grades uploaded");
                });
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

Parse.Cloud.job("sendOff1127Lab4Grades", function(request, status) {
  
  Parse.Cloud.useMasterKey();

  var tokenurl = 'http://ourvle.mona.uwi.edu/login/token.php?username='+username+'&password='+password+'&service=moodle_mobile_app';
 
  Parse.Cloud.httpRequest({
        method: 'GET',
        url: tokenurl
        }).then(function(httpResponse) {
          var message = httpResponse.text;

        var object = JSON.parse(message);

        var Student = Parse.Object.extend("COMP1127_Grades");
        var query = new Parse.Query(Student);
        var token = object.token;

        var lab_id = comp_1127_lab_4_id;
        
        query.limit(500);
        query.find({
          success: function(results) {
            //alert("Successfully retrieved " + results.length + " students.");
            // Do something with the returned Parse.Object values
            for (var i = 0; i < results.length; i++) {
              var object = results[i];
              var userid = object.get("user_id");
              var grade = object.get("lab_4");
              if(typeof grade != 'undefined')
              {
                var url = "http://ourvle.mona.uwi.edu/webservice/rest/server.php?wstoken="+token+"&moodlewsrestformat=json&wsfunction=mod_assign_save_grade&assignmentid="+lab_id+"&userid="+userid+"&grade="+grade+"&attemptnumber=-1&addattempt=0&workflowstate=graded&applytoall=0&plugindata[assignfeedbackcomments_editor][text]=test&plugindata[assignfeedbackcomments_editor][format]=1&plugindata[files_filemanager]=0";
              
                Parse.Cloud.httpRequest({
                  url: url
                }).always(function(httpResponse) {
                  status.success("All grades uploaded");
                });
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

Parse.Cloud.job("sendOff1161Lab0Grades", function(request, status) {
  
  Parse.Cloud.useMasterKey();

  var tokenurl = 'http://ourvle.mona.uwi.edu/login/token.php?username='+username+'&password='+password+'&service=moodle_mobile_app';
 
  Parse.Cloud.httpRequest({
        method: 'GET',
        url: tokenurl
        }).then(function(httpResponse) {
          var message = httpResponse.text;

        var object = JSON.parse(message);

        var Student = Parse.Object.extend("COMP1161_Grades");
        var query = new Parse.Query(Student);
        var token = object.token;

        var lab_id = comp_1161_lab_0_id;
        
        query.limit(500);
        query.find({
          success: function(results) {
            for (var i = 0; i < results.length; i++) {
              var object = results[i];
              var userid = object.get("user_id");
              var grade = object.get("lab_0");
              if(typeof grade != 'undefined')
              {
                var url = "http://ourvle.mona.uwi.edu/webservice/rest/server.php?wstoken="+token+"&moodlewsrestformat=json&wsfunction=mod_assign_save_grade&assignmentid="+lab_id+"&userid="+userid+"&grade="+grade+"&attemptnumber=-1&addattempt=0&workflowstate=graded&applytoall=0&plugindata[assignfeedbackcomments_editor][text]=test&plugindata[assignfeedbackcomments_editor][format]=1&plugindata[files_filemanager]=0";

                Parse.Cloud.httpRequest({
                  url: url
                }).always(function(httpResponse) {
                  status.success("All grades uploaded");
                });
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

Parse.Cloud.job("sendOff1161Lab1Grades", function(request, status) {
  
  Parse.Cloud.useMasterKey();

  var tokenurl = 'http://ourvle.mona.uwi.edu/login/token.php?username='+username+'&password='+password+'&service=moodle_mobile_app';
 
  Parse.Cloud.httpRequest({
        method: 'GET',
        url: tokenurl
        }).then(function(httpResponse) {
          var message = httpResponse.text;

        var object = JSON.parse(message);

        var Student = Parse.Object.extend("COMP1161_Grades");
        var query = new Parse.Query(Student);
        var token = object.token;

        var lab_id = lab_1_id;
        
        query.limit(500);
        query.find({
          success: function(results) {
            for (var i = 0; i < results.length; i++) {
              var object = results[i];
              var userid = object.get("user_id");
              var grade = object.get("lab_1");
              if(typeof grade != 'undefined')
              {
                var url = "http://ourvle.mona.uwi.edu/webservice/rest/server.php?wstoken="+token+"&moodlewsrestformat=json&wsfunction=mod_assign_save_grade&assignmentid="+lab_id+"&userid="+userid+"&grade="+grade+"&attemptnumber=-1&addattempt=0&workflowstate=graded&applytoall=0&plugindata[assignfeedbackcomments_editor][text]=test&plugindata[assignfeedbackcomments_editor][format]=1&plugindata[files_filemanager]=0";

                Parse.Cloud.httpRequest({
                  url: url
                }).always(function(httpResponse) {
                  status.success("All grades uploaded");
                });
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

Parse.Cloud.job("sendOff1161Lab2Grades", function(request, status) {
  
  Parse.Cloud.useMasterKey();

  var tokenurl = 'http://ourvle.mona.uwi.edu/login/token.php?username='+username+'&password='+password+'&service=moodle_mobile_app';
 
  Parse.Cloud.httpRequest({
        method: 'GET',
        url: tokenurl
        }).then(function(httpResponse) {
          var message = httpResponse.text;

        var object = JSON.parse(message);

        var Student = Parse.Object.extend("COMP1161_Grades");
        var query = new Parse.Query(Student);
        var token = object.token;

        var lab_id = comp_1161_lab_2_id;
        
        query.limit(500);
        query.find({
          success: function(results) {
            for (var i = 0; i < results.length; i++) {
              var object = results[i];
              var userid = object.get("user_id");
              var grade = object.get("lab_2");
              if(typeof grade != 'undefined')
              {
              var url = "http://ourvle.mona.uwi.edu/webservice/rest/server.php?wstoken="+token+"&moodlewsrestformat=json&wsfunction=mod_assign_save_grade&assignmentid="+lab_id+"&userid="+userid+"&grade="+grade+"&attemptnumber=-1&addattempt=0&workflowstate=graded&applytoall=0&plugindata[assignfeedbackcomments_editor][text]=test&plugindata[assignfeedbackcomments_editor][format]=1&plugindata[files_filemanager]=0";
              console.log(url);
              Parse.Cloud.httpRequest({
                url: url
              }).always(function(httpResponse) {
                status.success("All grades uploaded");
              });
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

Parse.Cloud.job("sendOff1161Lab3Grades", function(request, status) {
  
  Parse.Cloud.useMasterKey();

  var tokenurl = 'http://ourvle.mona.uwi.edu/login/token.php?username='+username+'&password='+password+'&service=moodle_mobile_app';
 
  Parse.Cloud.httpRequest({
        method: 'GET',
        url: tokenurl
        }).then(function(httpResponse) {
          var message = httpResponse.text;

        var object = JSON.parse(message);

        var Student = Parse.Object.extend("COMP1161_Grades");
        var query = new Parse.Query(Student);
        var token = object.token;

        var lab_id = comp_1161_lab_3_id;
        
        query.limit(500);
        query.find({
          success: function(results) {
            //alert("Successfully retrieved " + results.length + " students.");
            // Do something with the returned Parse.Object values
            for (var i = 0; i < results.length; i++) {
              var object = results[i];
              var userid = object.get("user_id");
              var grade = object.get("lab_3");
              if(typeof grade != 'undefined')
              {
                var url = "http://ourvle.mona.uwi.edu/webservice/rest/server.php?wstoken="+token+"&moodlewsrestformat=json&wsfunction=mod_assign_save_grade&assignmentid="+lab_id+"&userid="+userid+"&grade="+grade+"&attemptnumber=-1&addattempt=0&workflowstate=graded&applytoall=0&plugindata[assignfeedbackcomments_editor][text]=test&plugindata[assignfeedbackcomments_editor][format]=1&plugindata[files_filemanager]=0";
              
                Parse.Cloud.httpRequest({
                  url: url
                }).always(function(httpResponse) {
                  console.log("msg2:"+httpResponse.text);
                  status.success("All grades uploaded");
                });
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

Parse.Cloud.job("sendOff1161Lab4Grades", function(request, status) {
  
  Parse.Cloud.useMasterKey();

  var tokenurl = 'http://ourvle.mona.uwi.edu/login/token.php?username='+username+'&password='+password+'&service=moodle_mobile_app';
 
  Parse.Cloud.httpRequest({
        method: 'GET',
        url: tokenurl
        }).then(function(httpResponse) {
          var message = httpResponse.text;

        var object = JSON.parse(message);

        var Student = Parse.Object.extend("COMP1161_Grades");
        var query = new Parse.Query(Student);
        var token = object.token;

        var lab_id = comp_1161_lab_4_id;
        
        query.limit(500);
        query.find({
          success: function(results) {
            //alert("Successfully retrieved " + results.length + " students.");
            // Do something with the returned Parse.Object values
            for (var i = 0; i < results.length; i++) {
              var object = results[i];
              var userid = object.get("user_id");
              var grade = object.get("lab_4");
              if(typeof grade != 'undefined')
              {
                var url = "http://ourvle.mona.uwi.edu/webservice/rest/server.php?wstoken="+token+"&moodlewsrestformat=json&wsfunction=mod_assign_save_grade&assignmentid="+lab_id+"&userid="+userid+"&grade="+grade+"&attemptnumber=-1&addattempt=0&workflowstate=graded&applytoall=0&plugindata[assignfeedbackcomments_editor][text]=test&plugindata[assignfeedbackcomments_editor][format]=1&plugindata[files_filemanager]=0";
              
                Parse.Cloud.httpRequest({
                  url: url
                }).always(function(httpResponse) {
                  status.success("All grades uploaded");
                });
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

Parse.Cloud.job("sendOff1161Lab5Grades", function(request, status) {
  
  Parse.Cloud.useMasterKey();

  var tokenurl = 'http://ourvle.mona.uwi.edu/login/token.php?username='+username+'&password='+password+'&service=moodle_mobile_app';
 
  Parse.Cloud.httpRequest({
        method: 'GET',
        url: tokenurl
        }).then(function(httpResponse) {
          var message = httpResponse.text;

        var object = JSON.parse(message);

        var Student = Parse.Object.extend("COMP1161_Grades");
        var query = new Parse.Query(Student);
        var token = object.token;

        var lab_id = comp_1161_lab_5_id;
        
        query.limit(500);
        query.find({
          success: function(results) {
            for (var i = 0; i < results.length; i++) {
              var object = results[i];
              var userid = object.get("user_id");
              var grade = object.get("lab_5");
              if(typeof grade != 'undefined')
              {
                var url = "http://ourvle.mona.uwi.edu/webservice/rest/server.php?wstoken="+token+"&moodlewsrestformat=json&wsfunction=mod_assign_save_grade&assignmentid="+lab_id+"&userid="+userid+"&grade="+grade+"&attemptnumber=-1&addattempt=0&workflowstate=graded&applytoall=0&plugindata[assignfeedbackcomments_editor][text]=test&plugindata[assignfeedbackcomments_editor][format]=1&plugindata[files_filemanager]=0";

                Parse.Cloud.httpRequest({
                  url: url
                }).always(function(httpResponse) {
                  status.success("All grades uploaded");
                });
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

Parse.Cloud.job("sendOff1161Lab6Grades", function(request, status) {
  
  Parse.Cloud.useMasterKey();

  var tokenurl = 'http://ourvle.mona.uwi.edu/login/token.php?username='+username+'&password='+password+'&service=moodle_mobile_app';
 
  Parse.Cloud.httpRequest({
        method: 'GET',
        url: tokenurl
        }).then(function(httpResponse) {
          var message = httpResponse.text;

        var object = JSON.parse(message);

        var Student = Parse.Object.extend("COMP1161_Grades");
        var query = new Parse.Query(Student);
        var token = object.token;

        var lab_id = lab_6_id;
        
        query.limit(500);
        query.find({
          success: function(results) {
            for (var i = 0; i < results.length; i++) {
              var object = results[i];
              var userid = object.get("user_id");
              var grade = object.get("lab_6");
              if(typeof grade != 'undefined')
              {
                var url = "http://ourvle.mona.uwi.edu/webservice/rest/server.php?wstoken="+token+"&moodlewsrestformat=json&wsfunction=mod_assign_save_grade&assignmentid="+lab_id+"&userid="+userid+"&grade="+grade+"&attemptnumber=-1&addattempt=0&workflowstate=graded&applytoall=0&plugindata[assignfeedbackcomments_editor][text]=test&plugindata[assignfeedbackcomments_editor][format]=1&plugindata[files_filemanager]=0";

                Parse.Cloud.httpRequest({
                  url: url
                }).always(function(httpResponse) {
                  status.success("All grades uploaded");
                });
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

Parse.Cloud.job("sendOff1161Lab7Grades", function(request, status) {
  
  Parse.Cloud.useMasterKey();

  var tokenurl = 'http://ourvle.mona.uwi.edu/login/token.php?username='+username+'&password='+password+'&service=moodle_mobile_app';
 
  Parse.Cloud.httpRequest({
        method: 'GET',
        url: tokenurl
        }).then(function(httpResponse) {
          var message = httpResponse.text;

        var object = JSON.parse(message);

        var Student = Parse.Object.extend("COMP1161_Grades");
        var query = new Parse.Query(Student);
        var token = object.token;

        var lab_id = comp_1161_lab_7_id;
        
        query.limit(500);
        query.find({
          success: function(results) {
            for (var i = 0; i < results.length; i++) {
              var object = results[i];
              var userid = object.get("user_id");
              var grade = object.get("lab_7");
              if(typeof grade != 'undefined')
              {
              var url = "http://ourvle.mona.uwi.edu/webservice/rest/server.php?wstoken="+token+"&moodlewsrestformat=json&wsfunction=mod_assign_save_grade&assignmentid="+lab_id+"&userid="+userid+"&grade="+grade+"&attemptnumber=-1&addattempt=0&workflowstate=graded&applytoall=0&plugindata[assignfeedbackcomments_editor][text]=test&plugindata[assignfeedbackcomments_editor][format]=1&plugindata[files_filemanager]=0";
              console.log(url);
              Parse.Cloud.httpRequest({
                url: url
              }).always(function(httpResponse) {
                status.success("All grades uploaded");
              });
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

Parse.Cloud.job("sendOff1161Lab8Grades", function(request, status) {
  
  Parse.Cloud.useMasterKey();

  var tokenurl = 'http://ourvle.mona.uwi.edu/login/token.php?username='+username+'&password='+password+'&service=moodle_mobile_app';
 
  Parse.Cloud.httpRequest({
        method: 'GET',
        url: tokenurl
        }).then(function(httpResponse) {
          var message = httpResponse.text;

        var object = JSON.parse(message);

        var Student = Parse.Object.extend("COMP1161_Grades");
        var query = new Parse.Query(Student);
        var token = object.token;

        var lab_id = comp_1161_lab_8_id;
        
        query.limit(500);
        query.find({
          success: function(results) {
            //alert("Successfully retrieved " + results.length + " students.");
            // Do something with the returned Parse.Object values
            for (var i = 0; i < results.length; i++) {
              var object = results[i];
              var userid = object.get("user_id");
              var grade = object.get("lab_8");
              if(typeof grade != 'undefined')
              {
                var url = "http://ourvle.mona.uwi.edu/webservice/rest/server.php?wstoken="+token+"&moodlewsrestformat=json&wsfunction=mod_assign_save_grade&assignmentid="+lab_id+"&userid="+userid+"&grade="+grade+"&attemptnumber=-1&addattempt=0&workflowstate=graded&applytoall=0&plugindata[assignfeedbackcomments_editor][text]=test&plugindata[assignfeedbackcomments_editor][format]=1&plugindata[files_filemanager]=0";
              
                Parse.Cloud.httpRequest({
                  url: url
                }).always(function(httpResponse) {
                  console.log("msg2:"+httpResponse.text);
                  status.success("All grades uploaded");
                });
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

Parse.Cloud.job("sendOff1161Lab9Grades", function(request, status) {
  
  Parse.Cloud.useMasterKey();

  var tokenurl = 'http://ourvle.mona.uwi.edu/login/token.php?username='+username+'&password='+password+'&service=moodle_mobile_app';
 
  Parse.Cloud.httpRequest({
        method: 'GET',
        url: tokenurl
        }).then(function(httpResponse) {
          var message = httpResponse.text;

        var object = JSON.parse(message);

        var Student = Parse.Object.extend("COMP1161_Grades");
        var query = new Parse.Query(Student);
        var token = object.token;

        var lab_id = comp_1161_lab_9_id;
        
        query.limit(500);
        query.find({
          success: function(results) {
            //alert("Successfully retrieved " + results.length + " students.");
            // Do something with the returned Parse.Object values
            for (var i = 0; i < results.length; i++) {
              var object = results[i];
              var userid = object.get("user_id");
              var grade = object.get("lab_9");
              if(typeof grade != 'undefined')
              {
                var url = "http://ourvle.mona.uwi.edu/webservice/rest/server.php?wstoken="+token+"&moodlewsrestformat=json&wsfunction=mod_assign_save_grade&assignmentid="+lab_id+"&userid="+userid+"&grade="+grade+"&attemptnumber=-1&addattempt=0&workflowstate=graded&applytoall=0&plugindata[assignfeedbackcomments_editor][text]=test&plugindata[assignfeedbackcomments_editor][format]=1&plugindata[files_filemanager]=0";
              
                Parse.Cloud.httpRequest({
                  url: url
                }).always(function(httpResponse) {
                  status.success("All grades uploaded");
                });
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

Parse.Cloud.job("sendOff1161Lab10Grades", function(request, status) {
  
  Parse.Cloud.useMasterKey();

  var tokenurl = 'http://ourvle.mona.uwi.edu/login/token.php?username='+username+'&password='+password+'&service=moodle_mobile_app';
 
  Parse.Cloud.httpRequest({
        method: 'GET',
        url: tokenurl
        }).then(function(httpResponse) {
          var message = httpResponse.text;

        var object = JSON.parse(message);

        var Student = Parse.Object.extend("COMP1161_Grades");
        var query = new Parse.Query(Student);
        var token = object.token;

        var lab_id = comp_1161_lab_10_id;
        
        query.limit(500);
        query.find({
          success: function(results) {
            for (var i = 0; i < results.length; i++) {
              var object = results[i];
              var userid = object.get("user_id");
              var grade = object.get("lab_10");
              if(typeof grade != 'undefined')
              {
                var url = "http://ourvle.mona.uwi.edu/webservice/rest/server.php?wstoken="+token+"&moodlewsrestformat=json&wsfunction=mod_assign_save_grade&assignmentid="+lab_id+"&userid="+userid+"&grade="+grade+"&attemptnumber=-1&addattempt=0&workflowstate=graded&applytoall=0&plugindata[assignfeedbackcomments_editor][text]=test&plugindata[assignfeedbackcomments_editor][format]=1&plugindata[files_filemanager]=0";

                Parse.Cloud.httpRequest({
                  url: url
                }).always(function(httpResponse) {
                  status.success("All grades uploaded");
                });
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

Parse.Cloud.job("sendOff1161Lab11Grades", function(request, status) {
  
  Parse.Cloud.useMasterKey();

  var tokenurl = 'http://ourvle.mona.uwi.edu/login/token.php?username='+username+'&password='+password+'&service=moodle_mobile_app';
 
  Parse.Cloud.httpRequest({
        method: 'GET',
        url: tokenurl
        }).then(function(httpResponse) {
          var message = httpResponse.text;

        var object = JSON.parse(message);

        var Student = Parse.Object.extend("COMP1161_Grades");
        var query = new Parse.Query(Student);
        var token = object.token;

        var lab_id = lab_11_id;
        
        query.limit(500);
        query.find({
          success: function(results) {
            for (var i = 0; i < results.length; i++) {
              var object = results[i];
              var userid = object.get("user_id");
              var grade = object.get("lab_11");
              if(typeof grade != 'undefined')
              {
                var url = "http://ourvle.mona.uwi.edu/webservice/rest/server.php?wstoken="+token+"&moodlewsrestformat=json&wsfunction=mod_assign_save_grade&assignmentid="+lab_id+"&userid="+userid+"&grade="+grade+"&attemptnumber=-1&addattempt=0&workflowstate=graded&applytoall=0&plugindata[assignfeedbackcomments_editor][text]=test&plugindata[assignfeedbackcomments_editor][format]=1&plugindata[files_filemanager]=0";

                Parse.Cloud.httpRequest({
                  url: url
                }).always(function(httpResponse) {
                  status.success("All grades uploaded");
                });
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

Parse.Cloud.job("sendOff1161Lab12Grades", function(request, status) {
  
  Parse.Cloud.useMasterKey();

  var tokenurl = 'http://ourvle.mona.uwi.edu/login/token.php?username='+username+'&password='+password+'&service=moodle_mobile_app';
 
  Parse.Cloud.httpRequest({
        method: 'GET',
        url: tokenurl
        }).then(function(httpResponse) {
          var message = httpResponse.text;

        var object = JSON.parse(message);

        var Student = Parse.Object.extend("COMP1161_Grades");
        var query = new Parse.Query(Student);
        var token = object.token;

        var lab_id = comp_1161_lab_12_id;
        
        query.limit(500);
        query.find({
          success: function(results) {
            for (var i = 0; i < results.length; i++) {
              var object = results[i];
              var userid = object.get("user_id");
              var grade = object.get("lab_12");
              if(typeof grade != 'undefined')
              {
              var url = "http://ourvle.mona.uwi.edu/webservice/rest/server.php?wstoken="+token+"&moodlewsrestformat=json&wsfunction=mod_assign_save_grade&assignmentid="+lab_id+"&userid="+userid+"&grade="+grade+"&attemptnumber=-1&addattempt=0&workflowstate=graded&applytoall=0&plugindata[assignfeedbackcomments_editor][text]=test&plugindata[assignfeedbackcomments_editor][format]=1&plugindata[files_filemanager]=0";
              console.log(url);
              Parse.Cloud.httpRequest({
                url: url
              }).always(function(httpResponse) {
                status.success("All grades uploaded");
              });
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
