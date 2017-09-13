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

Array.prototype.contains = function(data) {
    var i = this.length;
    while (i--) {
        if (this[i].id === data.id) {
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
            console.log(data);
            if (data.role != "Member") {
                if (!member.contains(data)) {
                    member.push({ id: data.fbId, hasSession: "false", sesionId: "", socket: socket });
                    console.log(member.length);
                    // createNewSession();
                    var ok = false;
                    var check = setInterval(function() {
                        if (member[member.length - 1].hasSession === "true") {
                            if (ok === true) {
                                return;
                            }
                            console.log("Session ID sending to member " + member.length + ":\n" + cvt[cvt.length - 1].sessionId);
                            socket.emit('return-session-id', { sessionId: member[member.length - 1].sessionId, token: opentok.generateToken(member[member.length - 1].sessionId) });
                            ok = true;
                        }
                    }, 1000);
                } else {
                    console.log("This user is existed");
                }
            } else if (!cvt.contains(data)) {
                cvt.push({ id: data.fbId, hasSession: "false", sesionId: "", socket: socket });
                console.log(cvt.length);
                var ok = false;
                var check = setInterval(function() {

                    if (cvt[cvt.length - 1].hasSession === "true") {
                        if (ok === true) {
                            return;
                        }
                        console.log("Session ID sending to cvt " + cvt.length + ":\n" + cvt[cvt.length - 1].sessionId);
                        socket.emit('return-session-id', { sessionId: cvt[cvt.length - 1].sessionId, token: opentok.generateToken(cvt[cvt.length - 1].sessionId) });
                        ok = true;
                    }
                }, 1000);
                if (cvt.length % 2 == 0) {
                    if (cvt.length / 2 > sessionId.length) {
                        createNewSession();
                    }
                }
            } else {
                console.log("This user is existed");
            }

        }
    });

    socket.on('start-call', function() {
        start();
    });
});

function createNewSession() {
    opentok.createSession({mediaMode:"routed"}, function(error, session) {
        if (error) {
            console.log("Error creating session:\n", error)
        } else {
            sessionId[sessionId.length] = session.sessionId;
            console.log("created: " + sessionId.length);
            start();
        }

    });
}

// Start connecting users
function start() {
    console.log("Starting");
    scramble(member);
    scramble(cvt);
    n = member.length;
    m = cvt.length;
    console.log("n: " + n);
    console.log("m:" + m);
    var numberOfSession = Math.max(n, Math.trunc(m / 2))
    console.log("max: " + numberOfSession);
    for (var index = 0; index < sessionId.length; index++) {
        if (index < n) {
            member[index].sessionId = sessionId[index];
            member[index].hasSession = "true";
        }
        if (index * 2 < m) {
            cvt[index * 2].sessionId = sessionId[index];
            cvt[index * 2].hasSession = "true";
        }
        if (index * 2 + 1 < m) {
            cvt[index * 2 + 1].sessionId = sessionId[index];
            cvt[index * 2 + 1].hasSession = "true";
        }
    }
}