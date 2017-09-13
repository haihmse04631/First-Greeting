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
var index;
var member = [];
var cvt = [];
var memberWait = [];
var cvtWait = [];


Array.prototype.contains = function(obj) {
    var i = this.length;
    while (i--) {
        if (this[i] === obj) {
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
                    memberWait[member.length - 1] = true;
                    console.log(member.length);
                    var check = setInterval(function() {
                        if (!memberWait[member.length - 1]) {
                            console.log("Session ID: " + sessionId[0]);
                            socket.emit('return-session-id', { sessionId: sessionId, token: opentok.generateToken(sessionId[0]) });
                        }
                    }, 1000);
                }
            } else if (!cvt.contains(data)) {
                cvt.push({ id: data.id, socket: socket });
                cvtWait[cvt.length - 1] = true;
                console.log(cvt.length);
                var ok = false;
                var check = setInterval(function() {
        
                    if (!cvtWait[cvt.length - 1]) {
                        if (ok === true) {
                            return;
                        }
                        console.log("Session ID sending: " + sessionId[0]);
                        socket.emit('return-session-id', { sessionId: sessionId[0], token: opentok.generateToken(sessionId[0]) });
                        ok = true;
                    }
                }, 1000);
            }

            if (cvt.length == 2) start();
        }
    });

    socket.on('start-call', function() {
        start();
    });
});

//------- set time to start every calls?? ------ 


//------- send sessionId and token for each client --------

for (var i = 0; i < 10; i++) {
    opentok.createSession(function(error, session) {
        console.log(API_KEY);
        console.log(API_SECRET);


        if (error) {
            console.log("Error creating session:\n", error)
        } else {
            sessionId[sessionId.length] = session.sessionId;
            console.log("created: " + sessionId.length);
        }

    });
}

// Start
function start() {
    console.log("Starting");
    n = member.length;
    m = cvt.length;
    console.log("n: " + n);
    console.log("m:" + m);
    var numberOfSession = Math.max(n, Math.trunc(m / 2))
    console.log("max: " + numberOfSession);
    for (index = 0; index < 10; index++) {
        console.log(sessionId[index])
        if (index < n) {
            memberWait[index] = false;
        }
        if (index * 2 < m) {
            cvtWait[index * 2] = false;
            console.log(index * 2 + " = " + cvtWait[index * 2]);
        }
        if (index * 2 + 1 < m) {
            cvtWait[index * 2 + 1] = false;
            console.log((index * 2 + 1) + " = ");
        }
    }
}