const express = require('express');
const app = express();
const server = require('http').Server(app);
const io = require('socket.io')(server);
const socketio = require('./socketio')(io);
const OpenTok = require('opentok');

const API_KEY = '45956802';
const API_SECRET = 'd79c3cfa1c01f93d4b1c461c8494ff1b9b5e75fe';

// get initial session id 
var opentok = new OpenTok(API_KEY, API_SECRET);
var m = 0, n = 0;
var a = new Array();
var b = new Array();


function scramble(array) {
    var tmp, current, top = array.length;

    if(top) while(--top) {
        current = Math.floor(Math.random() * (top + 1));
        tmp = array[current];
        array[current] = array[top];
        array[top] = tmp;
    }
    return array;
}

a = scramble(a); b = scramble(b);


io.on('connection', function (socket) {

    console.log('A user connected ');

    //---- load client data list ----

    socket.on('client-send', function(data){
        console.log(data);
        //==== storage data from client
        if (data.role === "member"){
            b[++n] = {id : data.id, socket : socket} 
        }else{
            a[++m] = {id : data.id, socket : socket}
        }
        io.emit('server-send', {noidung: 'ok'});            
    })

    // socket.on('get-session-id', function(data) {
    //     // return session id for client
    //     io.emit('return-session-id', {sessionId : sessionId, token : opentok.generateToken(sessionId)});
    // });
}); 



//------- set time to start every calls?? ------ 


//------- send sessionId and token for each client --------
var sessionId;

var n = Math.min(n, Math.trunc(m/2))

for (var i = 1; i <= n; i++){
    opentok.createSession(function(error, session) {

        // console.log(API_KEY);
        // console.log(API_SECRET);

        if (error) {
            console.log("Error creating session:", error)
        } else {
            sessionId = session.sessionId;

            //---- sending ---
            a[i].socket.emit('return-session-id', {sessionId : sessionId, token: opentok.generateToken(sessionId)});
            b[i*2-1].socket.emit('return-session-id', {sessionId : sessionId, token: opentok.generateToken(sessionId)});
            b[i*2].socket.emit('return-session-id', {sessionId : sessionId, token: opentok.generateToken(sessionId)});


            console.log("Session ID: " + sessionId);
        }

    });
        
}
    


server.listen(3000, () => {
    console.log(`Server is running at localhost:3000`);
});


