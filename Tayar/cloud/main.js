
// Use Parse.Cloud.define to define as many cloud functions as you want.
// For example:
Parse.Cloud.define("hello", function(request, response) { //returns the current date
  
  var d= new Date();
  var n = d.toString();
  response.success(n);
});



Parse.Cloud.job("userMigration", function(request, response) { //50 minutes job
  // Set up to modify user data
  Parse.Cloud.useMasterKey();
var GameScore = Parse.Object.extend("Not");
var save_notifications= Parse.Object.extend("Notifications");
var listing = new save_notifications();
var query = new Parse.Query(GameScore);
query.contains("Order_Status", "Serving");
query.descending("createdAt");
query.find({
   success: function(results) {
    var time_now = new Date();
   	 //response.success(String(time_now));
   // alert("Successfully retrieved " + results.length + " scores.");
    // Do something with the returned Parse.Object values
   for (var i = 0; i < results.length; i++) 
   { 
      var time_take = results[i].get("Time_Take_was_pressed");    
      var difference = time_now - new Date (time_take);
      var  seconds=(difference/1000)%60;
      var minutes=(difference/(1000*60))%60;
      var hours=(difference/(1000*60*60))%24;
      var days= (difference/(1000 * 3600 * 24)); 
   
     if ((days==0)||(days<0.042)) 
     {
      if((minutes>=50)&&(minutes<51))// if difference is greater than 50 minutes
      {
        var notify= results[i].get("Alert_50min");
     if(notify=="false")
{
          results[i].set("Alert_50min","True");
           results[i].save();
    var query = new Parse.Query(Parse.Installation);

          Parse.Push.send({
    where: query, // Set our Installation query
    data: {
    alert: "50 min. passed for client "+results[i].get("Client_Name")+" with #"+ results[i].get("Client_Number")
    }
    }, {
    success: function() {
    // Push was successful
    },
   error: function(error) {
    // Handle error
    }
                      });

 listing.set("notifications","50 min. passed for client "+results[i].get("Client_Name")+" with #"+ results[i].get("Client_Number"));
 listing.save();  
     }
   }
    }
   //  alert(object.id + ' - ' + object.get('playerName'));
   }
  
  },
  error: function(error) {
   // alert("Error: " + error.code + " " + error.message);
   response.success("failed");
  }
});
  // response.success(object);
});


Parse.Cloud.job("userMigration1", function(request, response) { //two minutes
  // Set up to modify user data
  Parse.Cloud.useMasterKey();
var GameScore = Parse.Object.extend("Not");
var save_notifications= Parse.Object.extend("Notifications");
var listing = new save_notifications();
var query = new Parse.Query(GameScore);
query.contains("Order_Status", "pending");
query.descending("createdAt");
query.find({
   success: function(results) {
    var time_now = new Date();
     //response.success(String(time_now));
   
    // Do something with the returned Parse.Object values
   for (var i = 0; i < results.length; i++) 
   { 
      var time_take = results[i].get("createdAt");    
     var difference = time_now - new Date (time_take);
    
      var  seconds=(difference/1000)%60;
      var minutes=(difference/(1000*60))%60;
      var hours=(difference/(1000*60*60))%24;
      var days= (difference/(1000 * 3600 * 24)); 
   var client= results[i].get("Client_Number");
    // results[i].set("Alert_generated","false");
      //     results[i].save();
  if ((days<1)) //days limit is important to make sure we are on the same day!
     {
      if(hours<1)
      {
     if((minutes>=2)&&(minutes<=5))// if difference is greater than 2 minutes
      {

    var query = new Parse.Query(Parse.Installation);
     var notify= results[i].get("Alert_2min");
     if(notify=="false")
{
          results[i].set("Alert_2min","True");

           results[i].save();
          Parse.Push.send({
    where: query, // Set our Installation query
    data: {
    alert: "2 min. passed for client "+results[i].get("Client_Name")+" with #"+ results[i].get("Client_Number")

   //  alert: results[i].get("Client_Number")

    }
    }, {
    success: function() {
     alert("Successfully retrieved ");
    },
   error: function(error) {
 alert("UnSuccessfully retrieved ");
    }
                      });
 listing.set("notifications","2 min. passed for client "+results[i].get("Client_Name")+" with #"+ results[i].get("Client_Number"));
 listing.save();        
}
}
     }
    }
   //  alert(object.id + ' - ' + object.get('playerName'));
   }
  
  },
  error: function(error) {
   // alert("Error: " + error.code + " " + error.message);
   response.success("failed");
  }
});
  // response.success(object);
});


Parse.Cloud.job("userMigration2", function(request, response) {
  // Set up to modify user data
  Parse.Cloud.useMasterKey();
//var GameScore = Parse.Object.extend("_User");
var query = new Parse.Query(Parse.User);
 query.equalTo("Value", "TAYAR");

query.find({
   success: function(results) {
   // alert("Successfully retrieved " + results.length + " scores.");
    // Do something with the returned Parse.Object values
 for (var i = 0; i < results.length; i++) 
 {
    results[i].set("Tayar_State","free");
    results[i].save();
     // ParseUser ob = results.get(i);
       // ob.put("Tayar_State","free");
 }
  
  },
  error: function(error) {
   // alert("Error: " + error.code + " " + error.message);
   response.success("failed");
  }
});
  // response.success(object);
});

