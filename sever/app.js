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
var sessionId = [];
var m = 0,
    n = 0;
var member = [];
var cvt = [];
var memberWait = [];
var cvtWait = [];


Array.prototype.contains = function(obj) {
    var i = this.length;
    while (i--) {
        if (this[i].data.id === obj.data.id) {
            return true;
        }
    }
    return false;
}

Array.prototype.push = function(obj) {
    this[this.length++] = obj;
}

Array.prototype.get = function(i) {
    return this[i];
}

function scramble(array) {
    var tmp, current, top = array.length;

    if (top)
        while (--top) {
            current = Math.floor(Math.random() * (top + 1));
            tmp = array[current];
            array[current] = array[top];
            array[top] = tmp;
        }
    return array;
}

member = scramble(member);
cvt = scramble(cvt);

server.listen(3000, () => {
    console.log(`Server is running at localhost:3000`);
});

io.on('connection', function(socket) {

    console.log('A user connected ');

    socket.on('get-session-id', function(data) {
         {
            //==== storage data from client
            if (data.role === "Member") {
                if (!member.contains(data)) {
                    member.push({ id: data.id, socket: socket });
                    memberWait[member.length-1] = true;
                    console.log(member.length);
                    var check = setInterval(function() {
                    if (!memberWait[member.length-1]) {
                        console.log("Session ID: " + sessionId);
                        socket.emit('return-session-id', { sessionId: sessionId, token: opentok.generateToken(sessionId) });
                    }
                }, 1000);
                }
            } else if (!cvt.contains(data)){
                cvt.push({ id: data.id, socket: socket });
                cvtWait[cvt.length-1] = true;
                console.log(cvt.length);
                var check = setInterval(function() {
                    if (!cvtWait[cvt.length-1]) {
                        console.log("Session ID: " + sessionId);
                        socket.emit('return-session-id', { sessionId: sessionId, token: opentok.generateToken(sessionId) });
                    }
                }, 1000);
            }
            
            if (cvt.length == 2) start();
        }
    });
});

//------- set time to start every calls?? ------ 


//------- send sessionId and token for each client --------
start();
// Start
function start() {
    console.log("Starting");
    n = member.length;
    m = cvt.length;
    // console.log(n);
    // console.log(m);
    // var numberOfSession = Math.max(n, Math.trunc(m / 2))
    // console.log("max" + numberOfSession);
    for (var i = 0; i < 10; i++) {
        console.log(i);
        opentok.createSession(function(error, session) {
            console.log(API_KEY);
            console.log(API_SECRET);
            

            if (error) {
                console.log("Error creating session:", error)
            } else {

                // return session id for client

                sessionId[sessionId.length] = session.sessionId;

                // -- --sending-- -

                console.log("created: " + sessionId.length);
                // if (i < n) {
                //     memberWait[i] = false;
                // }
                // if (i * 2 < m) {
                //     cvtWait[i * 2] = false;
                //     console.log(i*2 + " = " + cvtWait[i*2]);
                // }
                // if (i * 2 + 1 < m) {
                //     cvtWait[i * 2 + 1] = false;
                //     console.log((i*2+1) + " = ");
                // }

                console.log("Session ID: " + sessionId);
            }

        });
    }
}