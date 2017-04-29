var express = require("express");
var app = express();
var server = require("http").createServer(app);
var io = require("socket.io").listen(server);
var fs = require("fs");
var mysql = require("mysql");
var bodyParser = require('body-parser')
app.use( bodyParser.json());       // to support JSON-encoded bodies
app.use(bodyParser.urlencoded({     // to support URL-encoded bodies
	extended: true
}));
app.use(express.static('public'));
server.listen(process.env.PORT || 3000);

//database connection

var connection = mysql.createConnection({
	host : 'mysql.cs.iastate.edu',
	user : 'dbu309sg4',
	password : 'N2IwYzIyOTdl',
	database : 'db309sg4'
});
connection.connect();
/**
 * @api {post} /register Register
 * @apiAuthor Fiesta
 * @apiName UserRegister
 * @apiGroup User
 *
 * @apiSuccess {String} result Register Successfully.
 *
 * @apiSuccessExample Success-Response:
 *     HTTP/1.1 200 OK
 *     {
 *       "result": "Register Successfully",
 *     }
 *
 * @apiError UserNameHasBeenExisted The username has been existed
 * @apiError ServerUnavailable The server is currently available
 *
 * @apiErrorExample UserNameHasBeenExisted:
 *     HTTP/1.1 409 Conflict
 *     {
 *       "error": "Username has been existed"
 *     }
 * @apiErrorExample {json} ServerUnavailable
 *    HTTP/1.1 500 Internal Server Error
 *     {
 *       "error": "Server is unavailable"
 *     }
 */
app.post("/register", function(req,res){
	var fname = req.body.fullname;
	var uname = req.body.username;
    var pass = req.body.pass;
	var ph = req.body.pass;
	var birth = req.body.dob;
	var eml = req.body.email;
	var gd = req.body.gender;
	var add = req.body.address;
	var portrait = new Buffer(req.body.avatar,'base64');
	var newuser = {
		  Username : uname,
		  Passwd : pass,
		  Fullname : fname,
		  DOB : birth,
		  Gender: gd,
		  Address: add,
		  Phone : ph,
		  Email : eml,
		  Rating: "0",
		  Avatar: "avatars/"+uname+".png",
		  IsAdmin: "0",
		  Block: "0"
	  };
	  console.log(newuser);
	  connection.query('insert into Users set ?',newuser,function(err,result){
		if (err)
		{
			console.log(err);
			res.send("Username has been existed");
			return;
		}
		else
		    fs.writeFile("avatars/"+uname+".png",portrait);
			console.log("Register");
			res.send("Register successfully");
		});


});
/**
 * @api {post} /login Login
 * @apiAuthor Fiesta
 * @apiName UserLogin
 * @apiGroup User
 *
 * @apiSuccess {String} User.Username Username of specific user
 * @apiSuccess {String} User.Passwd User's password
 * @apiSuccess {String} User.Fullname User's fullname
 * @apiSuccess {String} User.DOB Username's data of birth
 * @apiSuccess {String} User.Gender Username's gender
 * @apiSuccess {String} User.Address User's address
 * @apiSuccess {String} User.Phone User's phone number
 * @apiSuccess {String} User.Email User's email address
 * @apiSuccess {String} User.Rating User's rate
 * @apiSuccess {String} User.Avatar User's avatar picture location on server side
 * @apiSuccess {String} User.IsAdmin User's administration right
 * @apiSuccess {String} User.Block User's block status. If blocked, user can no longer login
 * @apiSuccess {String} User.Portrait User's avatar in base64 string format
 *
 * @apiSuccessExample Success-Response:
 *     HTTP/1.1 200 OK
 *     {
 *       "Username": "username",
 *		 "Passwd": "password",
 *		 "Fullname": "fullname",
 *		 "DOB": "mm/dd/yyyy",
 *		 "Gender": "gender",
 *		 "Address": "123 ABC St,City,State Zipcode",
 *		 "Phone": "1234567890",
 *		 "Email": "user@mail.com",
 *		 "Rating": "0",
 *		 "Avatar": "avatar/user.png",
 *		 "IsAdmin": "0",
 *		 "Block": "0",
 *		 "Portrait": "iVBKGgoAAEU......gIAAAAA3",
 *     }
 *
 * @apiError UserNotRegisterd The username has not been registered yet
 * @apiError PasswordNotMatched The password incorrect
 * @apiError UserBlocked The user has been blocked
 * @apiError ServerUnavailable The server is currently available
 *
 * @apiErrorExample {json} UserNotRegisterd
 *    HTTP/1.1 404 Not Found
 *     {
 *       "error": "Username not registered"
 *     }
 * @apiErrorExample {json} PasswordNotMatched
 *    HTTP/1.1 401 Unauthorized
 *     {
 *       "error": "Password not matched"
 *     }
 * @apiErrorExample {json} UserBlocked
 *    HTTP/1.1 403 Forbidden
 *     {
 *       "error": "Your account has been blocked"
 *     }
 * @apiErrorExample {json} ServerUnavailable
 *    HTTP/1.1 500 Internal Server Error
 *     {
 *       "error": "Server is unavailable"
 *     }
 */
app.post("/login", function(req,res){

	var uname = req.body.username;
    var pass = req.body.pass;

	connection.query("select * from Users where username='"+uname+"'",function(err,rows,fields){
		if (err) {
			console.log(err);
			res.send("Login Fail");
			return;
		}
		else{
			if(rows.length === 0){
				res.send("Username not registered");
			}
			else{
				var block = rows[0]['Block'];
				var pw = rows[0]['Passwd'];
				if(block == "0"){
					rows=JSON.stringify(rows);
					if(pw==pass){
						fs.readFile("avatars/"+uname+".png",function(err,data){
							if(err){
								console.log(err);
							}
							else{
								var base64string = ',"Portrait":"'+new Buffer(data).toString('base64')+'"';
								var position = rows.length-2;
								var output = [rows.slice(0, position), base64string, rows.slice(position)].join('');
								console.log("Login:");
								res.send(output);
							}
						});
					}
					else{
						res.send("Password not matched")
					}
				}
				else{
					res.send("Your account has been blocked")
				}
			}
		}
	});
});
/**
 * @api {post} /displayfriend FriendList
 * @apiAuthor Fiesta
 * @apiName UserFriendList
 * @apiGroup User
 *
 * @apiSuccess {String} User.Username Username of specific user
 * @apiSuccess {String} User.Passwd User's password
 * @apiSuccess {String} User.Fullname User's fullname
 * @apiSuccess {String} User.DOB Username's data of birth
 * @apiSuccess {String} User.Gender Username's gender
 * @apiSuccess {String} User.Address User's address
 * @apiSuccess {String} User.Phone User's phone number
 * @apiSuccess {String} User.Email User's email address
 * @apiSuccess {String} User.Rating User's rate
 * @apiSuccess {String} User.Avatar User's avatar picture location on server side
 * @apiSuccess {String} User.IsAdmin User's administration right
 * @apiSuccess {String} User.Block User's block status. If blocked, user can no longer login
 *
 *
 * @apiSuccessExample Success-Response:
 *     HTTP/1.1 200 OK
 *     {
 *       "Username": "username",
 *		 "Passwd": "password",
 *		 "Fullname": "fullname",
 *		 "DOB": "mm/dd/yyyy",
 *		 "Gender": "gender",
 *		 "Address": "123 ABC St,City,State Zipcode",
 *		 "Phone": "1234567890",
 *		 "Email": "user@mail.com",
 *		 "Rating": "0",
 *		 "Avatar": "avatar/user.png",
 *		 "IsAdmin": "0",
 *		 "Block": "0",
 *     }
 * @apiError ServerUnavailable The server is currently available
 *
 * @apiErrorExample {json} ServerUnavailable
 *    HTTP/1.1 500 Internal Server Error
 *     {
 *       "error": "Server is unavailable"
 *     }
 */
app.post("/displayfriend",function(req,res){
	var uname = req.body.username;
	var j=0;
	var ret=[];
	connection.query("select u.* from Users u,(select friendname from Friends where username ='"+uname+"') f"
						+" where f.friendname = u.username order by u.username",function(err,rows,fields){

		if (err)
		{
			console.log(err);
			return;
		}
		else{
			if(rows.length==0)
			{
				res.send("Nothing");
			}
			else{
			for(var i =0;i<rows.length;i++){
				var file = rows[i]["Avatar"];
				fs.readFile(file, function (err, data){
					if(err){
						console.log(err);
						return;
					}
					else{

						var u = JSON.stringify(rows[j++]);
						console.log(u);
						var base64string = ',"Portrait":"'+new Buffer(data).toString('base64')+'"';
						console.log(base64string);
						var position = u.length-1;
						u = [u.slice(0, position), base64string, u.slice(position)].join('');
						ret.push(JSON.parse(u));
						console.log("-----------------------------------------------------------------");
						if(j==rows.length){
						//	console.log(JSON.stringify(ret));
							res.send(JSON.stringify(ret));
						}
					}
				});
		}}
		}
	});
});

app.post("/displayallusers",function(req,res){
	var uname = req.body.username;
	var j=0;
	var ret=[];
	connection.query("select * from Users where username not in (select friendname from Friends where username ='"+uname+"')"
						+" and not username='"+uname+"' order by username",function(err,rows,fields){
		if (err)
		{
			console.log(err);
			return;
		}
		else{
			if(rows.length==0)
			{
				res.send("Nothing");
			}
			else{
			for(var i =0;i<rows.length;i++){
				var file = rows[i]["Avatar"];
				fs.readFile(file, function (err, data){
					if(err){
						console.log(err);
						return;
					}
					else{

						var u = JSON.stringify(rows[j++]);
						console.log(u);
						var base64string = ',"Portrait":"'+new Buffer(data).toString('base64')+'"';
						console.log(base64string);
						var position = u.length-1;
						u = [u.slice(0, position), base64string, u.slice(position)].join('');
						ret.push(JSON.parse(u));
						console.log("-----------------------------------------------------------------");
						if(j==rows.length){
						//	console.log(JSON.stringify(ret));
							res.send(JSON.stringify(ret));
						}
					}
				});
		}}
		}
		});
});
/**
 * @api {post} /displayevent EventList
 * @apiAuthor Fiesta
 * @apiName EventList
 * @apiGroup Event
 *
 * @apiSuccess {String} Event.EventCurrentID Identification number of event
 * @apiSuccess {String} Event.EventName Event title
 * @apiSuccess {String} Event.EventDescription Description of event
 * @apiSuccess {String} Event.EventAddress Event's location
 * @apiSuccess {String} Event.EventTime Event's time
 * @apiSuccess {String} Event.EventDate Event's date
 * @apiSuccess {String} Event.Hostname Event's host name
 * @apiSuccess {String} Event.Restriction Restriction for event
 * @apiSuccess {String} Event.EventType The type of event
 * @apiSuccess {String} Event.Longtitude The longtitude of event on google map
 * @apiSuccess {String} Event.Latitude The latitude of event on google map
 *
 *
 * @apiSuccessExample Success-Response:
 *     HTTP/1.1 200 OK
 *     {
 *       "EventCurrentID": "1",
 *		 "EventName": "Birthday",
 *		 "EventDescription": "The party is fun",
 *       "EventAddress": "123 ABC St,City,State Zipcode",
 *		 "EventTime": "1:00pm",
 *		 "EventDate": "mm/dd/yyyy",
 *		 "Hostname": "abc",
 *		 "Restriction": "21+",
 *		 "EventType": "Private",
 *		 "Longtitude": "102",
 *		 "Latitude": "95",
 *     }
 * @apiError ServerUnavailable The server is currently available
 *
 * @apiErrorExample {json} ServerUnavailable
 *    HTTP/1.1 500 Internal Server Error
 *     {
 *       "error": "Server is unavailable"
 *     }
 */
app.post("/displayevent",function(req,res){
	var uname = req.body.username;
	connection.query("select * from EventsCurrent where HostName ='"+uname+"'",function(err,rows,fields){

		if (err)
		{
			console.log(err);
			return;
		}
		else{
			if(rows.length==0)
			{
				res.send("Nothing");
			}
			else{
			console.log(rows);
			res.send(rows);}
		}
		});
});
/**
 * @api {post} /displayevent EventList
 * @apiAuthor Fiesta
 * @apiName EventList
 * @apiGroup Event
 *
 * @apiSuccess {String} Event.EventCurrentID Identification number of event
 * @apiSuccess {String} Event.EventName Event title
 * @apiSuccess {String} Event.EventDescription Description of event
 * @apiSuccess {String} Event.EventAddress Event's location
 * @apiSuccess {String} Event.EventTime Event's time
 * @apiSuccess {String} Event.EventDate Event's date
 * @apiSuccess {String} Event.Hostname Event's host name
 * @apiSuccess {String} Event.Restriction Restriction for event
 * @apiSuccess {String} Event.EventType The type of event
 * @apiSuccess {String} Event.Longtitude The longtitude of event on google map
 * @apiSuccess {String} Event.Latitude The latitude of event on google map
 *
 *
 * @apiSuccessExample Success-Response:
 *     HTTP/1.1 200 OK
 *     {
 *       "EventCurrentID": "1",
 *		 "EventName": "Birthday",
 *		 "EventDescription": "The party is fun",
 *       "EventAddress": "123 ABC St,City,State Zipcode",
 *		 "EventTime": "1:00pm",
 *		 "EventDate": "mm/dd/yyyy",
 *		 "Hostname": "abc",
 *		 "Restriction": "21+",
 *		 "EventType": "Private",
 *		 "Longtitude": "102",
 *		 "Latitude": "95",
 *     }
 * @apiError ServerUnavailable The server is currently available
 *
 * @apiErrorExample {json} ServerUnavailable
 *    HTTP/1.1 500 Internal Server Error
 *     {
 *       "error": "Server is unavailable"
 *     }
 */
app.post("/displayallevents",function(req,res){
	var uname = req.body.username;
	connection.query("select * from EventsCurrent where EventType = '0' order by EventName",function(err,rows,fields){

		if (err)
		{
			console.log(err);
			return;
		}
		else{
			if(rows.length==0)
			{
				res.send("Nothing");
			}
			else{
			console.log(rows);
			res.send(rows);
			}
		}
		});
});
/**
 * @api {post} /createevent EventCreate
 * @apiAuthor Fiesta
 * @apiName EventCreate
 * @apiGroup Event
 *
 * @apiSuccess {String} result Created successfully
 *
 * @apiSuccessExample Success-Response:
 *     HTTP/1.1 200 OK
 *     {
 *       "result": "Created successfully",
 *     }
 *
 * @apiError ServerUnavailable The server is currently available
 *
 * @apiErrorExample {json} ServerUnavailable
 *    HTTP/1.1 500 Internal Server Error
 *     {
 *       "error": "Server is unavailable"
 *     }
 */
app.post("/createevent", function(req,res){
	var username = req.body.userID;
	var name = req.body.eventName;
    var address = req.body.eventAddress;
	var date = req.body.eventDate;
	var time = req.body.eventTime;
	var minage = req.body.eventMinAge;
	var desc = req.body.eventDesc;
	var type = req.body.eventType;
	var t;
	if(type=="Public")
		t = "0";
	else
		t= "1";

	var newevent = {
		  EventName: name,
		  EventDescription:desc,
		  EventAddress : address,
		  EventTime : time,
		  EventDate : date,
		  Hostname    : username,
		  Restriction  : minage,
		  EventType : t,
	  };
	  console.log(newevent);
	  connection.query('insert into EventsCurrent set ?',newevent,function(err,result){
		if (err)
		{
			console.log(err);
			return;
		}
		else
			console.log("Create event:");
			res.send("Created successfully");
		});
});
/**
 * @api {post} /updateavatar AvatarUpdate
 * @apiAuthor Fiesta
 * @apiName AvatarUpdate
 * @apiGroup User
 *
 * @apiSuccess {String} avatar's base64 string format
 *
 * @apiSuccessExample Success-Response:
 *     HTTP/1.1 200 OK
 *     {
 *       "result": "iVBKGgoAAEU......gIAAAAA3",
 *     }
 *
 * @apiError ServerUnavailable The server is currently available
 *
 * @apiErrorExample {json} ServerUnavailable
 *    HTTP/1.1 500 Internal Server Error
 *     {
 *       "error": "Server is unavailable"
 *     }
 */
app.post("/updateavatar", function(req,res){
    var uname = req.body.username;
	var portrait = new Buffer(req.body.avatar,'base64');
    fs.writeFile("avatars/"+uname+".png",portrait);
    fs.readFile("avatars/"+uname+".png",function(err,data){
    	if(err)
    		 console.log(err);
        else{
    		  var base64string = new Buffer(data).toString('base64');
              res.send(base64string);
    		 }
    });

});
/**
 * @api {post} /friendrequest FriendRequest
 * @apiAuthor Fiesta
 * @apiName FriendRequest
 * @apiGroup User
 *
 * @apiSuccess {String} result Request has been sent successfully.
 *
 * @apiSuccessExample Success-Response:
 *     HTTP/1.1 200 OK
 *     {
 *       "result": "Friend request is sent to User",
 *     }
 *
 * @apiSuccess {String} result Request has been sent before.
 * @apiSuccessExample Success-Response:
 *     HTTP/1.1 200 OK
 *     {
 *       "result": ""You have sent friend request to User",
 *     }
 *
 * @apiError ServerUnavailable The server is currently available
 *
 * @apiErrorExample {json} ServerUnavailable
 *    HTTP/1.1 500 Internal Server Error
 *     {
 *       "error": "Server is unavailable"
 *     }
 */
app.post("/friendrequest", function(req,res){
    var uname = req.body.username;
	var fname = req.body.friendname;
	var fullname = req.body.friendfullname;
	var fr ={
		NotificationID : uname+'~'+fname,
		FromUser : uname,
		ToUser : fname,
		NotificationType : "F",
		EventCurrentID : -1
	};
	connection.query('insert ignore into Notification set ?',fr,function(err,result){
		if (err)
		{
			console.log(err);
			return;
		}
		else
			if(result["affectedRows"]==1)
				res.send("Friend request is sent to "+fullname);
			else
				res.send("You have sent friend request to "+fullname);
		});
});

app.post("/getnofitication", function(req,res){
    var uname = req.body.username;

	connection.query("select * from Notification where ToUser = '"+uname+"'",function(err,rows,fields){
		if (err)
		{
			console.log(err);
			return;
		}
		else{
			console.log(rows);
			res.send(rows);
		}
	});
});

app.post("/dismissallnotifs",function(req,res){
	var uname = req.body.username;

	connection.query("delete from Notification where ToUser = '"+uname+"'", function(err, result) {
		if (err) {
			console.log(err);
			return;
		}
		else{
			res.send("All notifications have been dismissed");
		}
	});
});

app.post("/updaterating",function(req,res){
	var n = req.body.toUser;
	var val = req.body.rateval;
	connection.query("update Users set Rating = cast(round((cast(Rating as decimal)+"+val+")/2,1) as char) where Username = '"+n+"'",function(err,result){
		if (err) {
			console.log(err);
			return;
		}
		else{
			res.send("You rated "+val+" stars for "+n);
		}
	});
});

app.post("/declinefriend",function(req,res){
	var del = req.body.did;
	connection.query("delete from Notification where NotificationID = '"+del+"' and NotificationType = 'F'", function(err, result) {
		if (err) {
			console.log(err);
			return;
		}
		else{
			res.send("You declined a friend request");
		}
	});
});
io.sockets.on('connection', function (socket) {

  console.log(socket.id + " is connected to chat");
  socket.on("client_send_username",function(data){
	  socket.un = data;
  });
  socket.on("client_send_chat_message",function(data){
	  console.log(socket.un+": "+data);
	  io.sockets.emit("server_send_chat_message",socket.un+": "+data);
  });

});
